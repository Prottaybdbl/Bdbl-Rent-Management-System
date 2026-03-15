package com.bdbl.rms.service;

import com.bdbl.rms.dto.BuildingDTO;
import com.bdbl.rms.dto.FloorDTO;
import com.bdbl.rms.dto.ParkingSpaceDTO;

import java.util.List;

public interface BuildingService {

    // === Building Operations ===
    BuildingDTO createBuilding(BuildingDTO buildingDTO);

    BuildingDTO updateBuilding(Long id, BuildingDTO buildingDTO);

    BuildingDTO getBuildingById(Long id);

    BuildingDTO getBuildingByCode(String code);

    List<BuildingDTO> getAllBuildings();

    void deleteBuilding(Long id);

    // === Floor Operations ===
    FloorDTO addFloor(Long buildingId, FloorDTO floorDTO);

    FloorDTO updateFloor(Long floorId, FloorDTO floorDTO);

    List<FloorDTO> getFloorsByBuildingId(Long buildingId);

    void deleteFloor(Long floorId);

    // === Parking Operations ===
    ParkingSpaceDTO addParkingSpace(Long buildingId, ParkingSpaceDTO parkingDTO);

    ParkingSpaceDTO updateParkingSpace(Long parkingId, ParkingSpaceDTO parkingDTO);

    List<ParkingSpaceDTO> getParkingSpacesByBuildingId(Long buildingId);

    void deleteParkingSpace(Long parkingId);
}
