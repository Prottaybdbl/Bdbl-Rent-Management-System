package com.bdbl.rms.service;

import com.bdbl.rms.dto.BuildingDTO;
import com.bdbl.rms.dto.FloorDTO;
import com.bdbl.rms.dto.ParkingSpaceDTO;
import com.bdbl.rms.entity.Building;
import com.bdbl.rms.entity.Floor;
import com.bdbl.rms.entity.ParkingSpace;
import com.bdbl.rms.repository.BuildingRepository;
import com.bdbl.rms.repository.FloorRepository;
import com.bdbl.rms.repository.ParkingSpaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {

    private static final Logger log = LoggerFactory.getLogger(BuildingServiceImpl.class);

    private final BuildingRepository buildingRepository;
    private final FloorRepository floorRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public BuildingServiceImpl(BuildingRepository buildingRepository, FloorRepository floorRepository,
            ParkingSpaceRepository parkingSpaceRepository) {
        this.buildingRepository = buildingRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    // ==========================================
    // BUILDING OPERATIONS
    // ==========================================

    @Override
    public BuildingDTO createBuilding(BuildingDTO dto) {
        log.info("Creating new building: {}", dto.getName());

        if (buildingRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Building code already exists: " + dto.getCode());
        }

        // Input totalAreaSft is treated as Area per Floor as per user request
        BigDecimal areaPerFloor = dto.getTotalAreaSft() != null ? dto.getTotalAreaSft() : BigDecimal.ZERO;

        Building building = Building.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .address(dto.getAddress())
                .city(dto.getCity())
                .district(dto.getDistrict())
                .totalFloors(dto.getTotalFloors())
                .totalAreaSft(areaPerFloor) // Storing per-floor area as requested
                .constructionYear(dto.getConstructionYear())
                .parkingConfigType(dto.getParkingConfigType())
                .status("ACTIVE")
                .build();

        Building savedBuilding = buildingRepository.save(building);

        // Auto-generate floors
        if (dto.getTotalFloors() != null && dto.getTotalFloors() > 0) {
            for (int i = 1; i <= dto.getTotalFloors(); i++) {
                Floor floor = Floor.builder()
                        .building(savedBuilding)
                        .floorNumber(String.valueOf(i))
                        .floorName("Floor " + i)
                        .totalAreaSft(areaPerFloor)
                        .status("ACTIVE")
                        .build();
                floorRepository.save(floor);
            }
        }

        return mapToBuildingDTO(savedBuilding);
    }

    @Override
    public BuildingDTO updateBuilding(Long id, BuildingDTO dto) {
        log.info("Updating building with ID: {}", id);

        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + id));

        // Check if code is being changed and if it already exists
        if (!building.getCode().equals(dto.getCode()) && buildingRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Building code already exists: " + dto.getCode());
        }

        building.setName(dto.getName());
        building.setCode(dto.getCode());
        building.setAddress(dto.getAddress());
        building.setCity(dto.getCity());
        building.setDistrict(dto.getDistrict());
        building.setTotalFloors(dto.getTotalFloors());
        building.setTotalAreaSft(dto.getTotalAreaSft());
        building.setConstructionYear(dto.getConstructionYear());
        building.setParkingConfigType(dto.getParkingConfigType());
        building.setStatus(dto.getStatus());

        Building updatedBuilding = buildingRepository.save(building);
        return mapToBuildingDTO(updatedBuilding);
    }

    @Override
    public BuildingDTO getBuildingById(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + id));
        return mapToBuildingDTO(building);
    }

    @Override
    public BuildingDTO getBuildingByCode(String code) {
        Building building = buildingRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with code: " + code));
        return mapToBuildingDTO(building);
    }

    @Override
    public List<BuildingDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(this::mapToBuildingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBuilding(Long id) {
        log.info("Deleting building with ID: {}", id);
        if (!buildingRepository.existsById(id)) {
            throw new IllegalArgumentException("Building not found with ID: " + id);
        }
        buildingRepository.deleteById(id);
    }

    // ==========================================
    // FLOOR OPERATIONS
    // ==========================================

    @Override
    public FloorDTO addFloor(Long buildingId, FloorDTO dto) {
        log.info("Adding floor to building ID: {}", buildingId);

        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + buildingId));

        if (floorRepository.existsByBuildingIdAndFloorNumber(buildingId, dto.getFloorNumber())) {
            throw new IllegalArgumentException(
                    "Floor number " + dto.getFloorNumber() + " already exists in this building.");
        }

        Floor floor = Floor.builder()
                .building(building)
                .floorNumber(dto.getFloorNumber())
                .floorName(dto.getFloorName())
                .totalAreaSft(dto.getTotalAreaSft())
                .commonAreaSft(dto.getCommonAreaSft())
                .status("ACTIVE")
                .build();

        Floor savedFloor = floorRepository.save(floor);
        return mapToFloorDTO(savedFloor);
    }

    @Override
    public FloorDTO updateFloor(Long floorId, FloorDTO dto) {
        log.info("Updating floor with ID: {}", floorId);

        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Floor not found with ID: " + floorId));

        Long buildingId = floor.getBuilding().getId();

        // Ensure floor number is unique within the building
        if (!floor.getFloorNumber().equals(dto.getFloorNumber()) &&
                floorRepository.existsByBuildingIdAndFloorNumber(buildingId, dto.getFloorNumber())) {
            throw new IllegalArgumentException(
                    "Floor number " + dto.getFloorNumber() + " already exists in this building.");
        }

        floor.setFloorNumber(dto.getFloorNumber());
        floor.setFloorName(dto.getFloorName());

        // Cannot reduce total area below currently allocated area
        if (dto.getTotalAreaSft().compareTo(floor.getAllocatedAreaSft()) < 0) {
            throw new IllegalArgumentException(
                    "Total area cannot be less than currently allocated area: " + floor.getAllocatedAreaSft());
        }

        floor.setTotalAreaSft(dto.getTotalAreaSft());
        floor.setCommonAreaSft(dto.getCommonAreaSft());
        floor.setStatus(dto.getStatus());

        Floor updatedFloor = floorRepository.save(floor);
        return mapToFloorDTO(updatedFloor);
    }

    @Override
    public List<FloorDTO> getFloorsByBuildingId(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + buildingId));

        List<Floor> floors = floorRepository.findByBuildingIdOrderByFloorNumberAsc(buildingId);

        // Lazy sync: If building claims to have floors but none exist in DB, generate
        // them
        if (floors.isEmpty() && building.getTotalFloors() != null && building.getTotalFloors() > 0) {
            log.info("Lazy generating {} floors for building ID: {}", building.getTotalFloors(), buildingId);
            BigDecimal areaPerFloor = building.getTotalAreaSft() != null ? building.getTotalAreaSft() : BigDecimal.ZERO;
            for (int i = 1; i <= building.getTotalFloors(); i++) {
                Floor floor = Floor.builder()
                        .building(building)
                        .floorNumber(String.valueOf(i))
                        .floorName("Floor " + i)
                        .totalAreaSft(areaPerFloor)
                        .status("ACTIVE")
                        .build();
                floorRepository.save(floor);
            }
            // Fetch again after generation
            floors = floorRepository.findByBuildingIdOrderByFloorNumberAsc(buildingId);
        }

        return floors.stream()
                .map(this::mapToFloorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteFloor(Long floorId) {
        log.info("Deleting floor with ID: {}", floorId);

        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new IllegalArgumentException("Floor not found with ID: " + floorId));

        if (floor.getAllocatedAreaSft() != null && floor.getAllocatedAreaSft().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Cannot delete floor because it has allocated area.");
        }

        floorRepository.deleteById(floorId);
    }

    // ==========================================
    // PARKING SPACE OPERATIONS
    // ==========================================

    @Override
    public ParkingSpaceDTO addParkingSpace(Long buildingId, ParkingSpaceDTO dto) {
        log.info("Adding parking space to building ID: {}", buildingId);

        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + buildingId));

        if (parkingSpaceRepository.existsByBuildingIdAndParkingNumber(buildingId, dto.getParkingNumber())) {
            throw new IllegalArgumentException(
                    "Parking number " + dto.getParkingNumber() + " already exists in this building.");
        }

        ParkingSpace parkingSpace = ParkingSpace.builder()
                .building(building)
                .parkingNumber(dto.getParkingNumber())
                .parkingType(dto.getParkingType())
                .location(dto.getLocation())
                .areaSft(dto.getAreaSft())
                .monthlyRent(dto.getMonthlyRent())
                .status("VACANT")
                .build();

        ParkingSpace savedParking = parkingSpaceRepository.save(parkingSpace);
        return mapToParkingSpaceDTO(savedParking);
    }

    @Override
    public ParkingSpaceDTO updateParkingSpace(Long parkingId, ParkingSpaceDTO dto) {
        log.info("Updating parking space with ID: {}", parkingId);

        ParkingSpace parking = parkingSpaceRepository.findById(parkingId)
                .orElseThrow(() -> new IllegalArgumentException("Parking space not found with ID: " + parkingId));

        Long buildingId = parking.getBuilding().getId();

        if (!parking.getParkingNumber().equals(dto.getParkingNumber()) &&
                parkingSpaceRepository.existsByBuildingIdAndParkingNumber(buildingId, dto.getParkingNumber())) {
            throw new IllegalArgumentException(
                    "Parking number " + dto.getParkingNumber() + " already exists in this building.");
        }

        parking.setParkingNumber(dto.getParkingNumber());
        parking.setParkingType(dto.getParkingType());
        parking.setLocation(dto.getLocation());
        parking.setAreaSft(dto.getAreaSft());
        parking.setMonthlyRent(dto.getMonthlyRent());
        parking.setStatus(dto.getStatus());

        ParkingSpace updatedParking = parkingSpaceRepository.save(parking);
        return mapToParkingSpaceDTO(updatedParking);
    }

    @Override
    public List<ParkingSpaceDTO> getParkingSpacesByBuildingId(Long buildingId) {
        if (!buildingRepository.existsById(buildingId)) {
            throw new IllegalArgumentException("Building not found with ID: " + buildingId);
        }

        return parkingSpaceRepository.findByBuildingIdOrderByParkingNumberAsc(buildingId).stream()
                .map(this::mapToParkingSpaceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteParkingSpace(Long parkingId) {
        log.info("Deleting parking space with ID: {}", parkingId);

        ParkingSpace parking = parkingSpaceRepository.findById(parkingId)
                .orElseThrow(() -> new IllegalArgumentException("Parking space not found with ID: " + parkingId));

        if (!"VACANT".equals(parking.getStatus())) {
            throw new IllegalStateException(
                    "Cannot delete parking space because it is currently " + parking.getStatus());
        }

        parkingSpaceRepository.deleteById(parkingId);
    }

    // ==========================================
    // MAPPING HELPER METHODS
    // ==========================================

    private BuildingDTO mapToBuildingDTO(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(building.getId());
        dto.setName(building.getName());
        dto.setCode(building.getCode());
        dto.setAddress(building.getAddress());
        dto.setCity(building.getCity());
        dto.setDistrict(building.getDistrict());
        dto.setTotalFloors(building.getTotalFloors());
        dto.setTotalAreaSft(building.getTotalAreaSft());
        dto.setConstructionYear(building.getConstructionYear());
        dto.setParkingConfigType(building.getParkingConfigType());
        dto.setStatus(building.getStatus());
        dto.setCreatedAt(building.getCreatedAt());

        // Calculate aggregated fields
        if (building.getFloors() != null) {
            dto.setActiveFloorsCount((int) building.getFloors().stream()
                    .filter(f -> "ACTIVE".equals(f.getStatus())).count());

            BigDecimal totalAllocated = building.getFloors().stream()
                    .map(Floor::getAllocatedAreaSft)
                    .filter(area -> area != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalAllocatedAreaSft(totalAllocated);

            BigDecimal totalAvailable = building.getFloors().stream()
                    .map(Floor::getAvailableAreaSft)
                    .filter(area -> area != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalAvailableAreaSft(totalAvailable);
        } else {
            dto.setActiveFloorsCount(0);
            dto.setTotalAllocatedAreaSft(BigDecimal.ZERO);
            dto.setTotalAvailableAreaSft(dto.getTotalAreaSft() != null ? dto.getTotalAreaSft() : BigDecimal.ZERO);
        }

        if (building.getParkingSpaces() != null) {
            dto.setActiveParkingSpacesCount((int) building.getParkingSpaces().stream()
                    .filter(p -> "VACANT".equals(p.getStatus()) || "OCCUPIED".equals(p.getStatus())).count());
        } else {
            dto.setActiveParkingSpacesCount(0);
        }

        return dto;
    }

    private FloorDTO mapToFloorDTO(Floor floor) {
        FloorDTO dto = new FloorDTO();
        dto.setId(floor.getId());
        dto.setBuildingId(floor.getBuilding().getId());
        dto.setBuildingName(floor.getBuilding().getName());
        dto.setFloorNumber(floor.getFloorNumber());
        dto.setFloorName(floor.getFloorName());
        dto.setTotalAreaSft(floor.getTotalAreaSft());
        dto.setAllocatedAreaSft(floor.getAllocatedAreaSft());
        dto.setCommonAreaSft(floor.getCommonAreaSft());
        dto.setAvailableAreaSft(floor.getAvailableAreaSft());
        dto.setStatus(floor.getStatus());
        return dto;
    }

    private ParkingSpaceDTO mapToParkingSpaceDTO(ParkingSpace parking) {
        ParkingSpaceDTO dto = new ParkingSpaceDTO();
        dto.setId(parking.getId());
        dto.setBuildingId(parking.getBuilding().getId());
        dto.setBuildingName(parking.getBuilding().getName());
        dto.setParkingNumber(parking.getParkingNumber());
        dto.setParkingType(parking.getParkingType());
        dto.setLocation(parking.getLocation());
        dto.setAreaSft(parking.getAreaSft());
        dto.setMonthlyRent(parking.getMonthlyRent());
        dto.setStatus(parking.getStatus());
        return dto;
    }
}
