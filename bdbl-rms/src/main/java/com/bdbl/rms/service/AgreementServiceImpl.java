package com.bdbl.rms.service;

import com.bdbl.rms.dto.AdvanceDepositDTO;
import com.bdbl.rms.dto.AgreementDocumentDTO;
import com.bdbl.rms.dto.AgreementParkingDTO;
import com.bdbl.rms.dto.LeaseAgreementDTO;
import com.bdbl.rms.entity.*;
import com.bdbl.rms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AgreementServiceImpl implements AgreementService {

    private static final Logger log = LoggerFactory.getLogger(AgreementServiceImpl.class);

    private final LeaseAgreementRepository agreementRepository;
    private final AgreementParkingRepository agreementParkingRepository;
    private final AgreementDocumentRepository agreementDocumentRepository;
    private final AdvanceDepositRepository advanceDepositRepository;
    private final TenantRepository tenantRepository;
    private final BuildingRepository buildingRepository;
    private final FloorRepository floorRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public AgreementServiceImpl(
            LeaseAgreementRepository agreementRepository,
            AgreementParkingRepository agreementParkingRepository,
            AgreementDocumentRepository agreementDocumentRepository,
            AdvanceDepositRepository advanceDepositRepository,
            TenantRepository tenantRepository,
            BuildingRepository buildingRepository,
            FloorRepository floorRepository,
            ParkingSpaceRepository parkingSpaceRepository) {
        this.agreementRepository = agreementRepository;
        this.agreementParkingRepository = agreementParkingRepository;
        this.agreementDocumentRepository = agreementDocumentRepository;
        this.advanceDepositRepository = advanceDepositRepository;
        this.tenantRepository = tenantRepository;
        this.buildingRepository = buildingRepository;
        this.floorRepository = floorRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    // ==========================================
    // LEASE AGREEMENT OPERATIONS
    // ==========================================

    @Override
    public LeaseAgreementDTO createAgreement(LeaseAgreementDTO dto) {
        log.info("Creating new lease agreement for tenant ID: {}", dto.getTenantId());

        // 1. Generate Agreement Number if not provided
        String agreementNumber = dto.getAgreementNumber();
        if (agreementNumber == null || agreementNumber.trim().isEmpty()) {
            java.time.LocalDate now = java.time.LocalDate.now();
            long count = agreementRepository.count() + 1;
            agreementNumber = String.format("BDBL/AGR/%d/%03d", now.getYear(), count);
        }

        if (agreementRepository.existsByAgreementNumber(agreementNumber)) {
            throw new IllegalArgumentException("Agreement number already exists: " + agreementNumber);
        }

        Tenant tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + dto.getTenantId()));

        Building building = buildingRepository.findById(dto.getBuildingId())
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + dto.getBuildingId()));

        Floor floor = null;
        if (dto.getFloorId() != null) {
            floor = floorRepository.findById(dto.getFloorId())
                    .orElseThrow(() -> new IllegalArgumentException("Floor not found with ID: " + dto.getFloorId()));
            
            // Validate area availability
            java.math.BigDecimal availableArea = floor.getAvailableAreaSft();
            if (dto.getAgreementAreaSft().compareTo(availableArea) > 0) {
                throw new IllegalArgumentException("Requested area (" + dto.getAgreementAreaSft() + ") exceeds available area (" + availableArea + ") on this floor.");
            }
        }

        LeaseAgreement agreement = LeaseAgreement.builder()
                .agreementNumber(agreementNumber)
                .tenant(tenant)
                .building(building)
                .floor(floor)
                .agreementAreaSft(dto.getAgreementAreaSft())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .durationMonths(dto.getDurationMonths())
                .rentPerSft(dto.getBaseRentRatePerSft() != null ? dto.getBaseRentRatePerSft() : dto.getRentPerSft())
                .totalMonthlyRent(dto.getBaseMonthlyRent() != null ? dto.getBaseMonthlyRent() : dto.getTotalMonthlyRent())
                .serviceChargeAmount(dto.getServiceCharge() != null ? dto.getServiceCharge() : dto.getServiceChargeAmount())
                .vatPercentage(dto.getVatPercent() != null ? dto.getVatPercent() : dto.getVatPercentage())
                .taxPercentage(dto.getTaxPercent() != null ? dto.getTaxPercent() : dto.getTaxPercentage())
                .advanceDepositAmount(dto.getAdvanceAmount() != null ? dto.getAdvanceAmount() : dto.getAdvanceDepositAmount())
                .securityDepositAmount(dto.getSecurityDepositAmount())
                .rentEscalationPercentage(dto.getRentEscalationPercentage())
                .escalationFrequencyMonths(dto.getEscalationFrequencyMonths())
                .billingCycle(dto.getBillingCycle())
                .gracePeriodDays(dto.getGracePeriodDays())
                .signedByBdbl(dto.getSignedByBdbl())
                .signedByTenant(dto.getSignedByTenant())
                .agreementDate(dto.getAgreementDate() != null ? dto.getAgreementDate() : java.time.LocalDate.now())
                .cancellationNoticePeriod(dto.getCancellationNoticePeriod())
                .status("ACTIVE") // Set to ACTIVE as per common flow
                .notes(dto.getRemarks() != null ? dto.getRemarks() : dto.getRemarks())
                .build();

        LeaseAgreement savedAgreement = agreementRepository.save(agreement);

        // 2. Handle Floor Allocation (Update floor's allocated area)
        if (floor != null) {
            java.math.BigDecimal currentAllocated = floor.getAllocatedAreaSft() != null ? floor.getAllocatedAreaSft() : java.math.BigDecimal.ZERO;
            floor.setAllocatedAreaSft(currentAllocated.add(dto.getAgreementAreaSft()));
            floorRepository.save(floor);
        }

        // 3. Create Advance Deposit Record if provided
        if (dto.getAdvanceAmount() != null && dto.getAdvanceAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            AdvanceDeposit advance = AdvanceDeposit.builder()
                    .agreement(savedAgreement)
                    .depositType("ADVANCE_RENT")
                    .originalAmount(dto.getAdvanceAmount())
                    .remainingAmount(dto.getAdvanceAmount())
                    .receivedDate(java.time.LocalDate.now())
                    .status("ACTIVE")
                    .build();
            advanceDepositRepository.save(advance);
        }

        // 4. Create Security Deposit Record if provided
        if (dto.getSecurityDepositAmount() != null && dto.getSecurityDepositAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            AdvanceDeposit security = AdvanceDeposit.builder()
                    .agreement(savedAgreement)
                    .depositType("SECURITY_DEPOSIT")
                    .originalAmount(dto.getSecurityDepositAmount())
                    .remainingAmount(dto.getSecurityDepositAmount())
                    .receivedDate(java.time.LocalDate.now())
                    .status("ACTIVE")
                    .build();
            advanceDepositRepository.save(security);
        }

        return mapToLeaseAgreementDTO(savedAgreement);
    }

    @Override
    public LeaseAgreementDTO updateAgreement(Long id, LeaseAgreementDTO dto) {
        log.info("Updating lease agreement with ID: {}", id);

        LeaseAgreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found with ID: " + id));

        if (!agreement.getAgreementNumber().equals(dto.getAgreementNumber()) &&
                agreementRepository.existsByAgreementNumber(dto.getAgreementNumber())) {
            throw new IllegalArgumentException("Agreement number already exists: " + dto.getAgreementNumber());
        }

        Floor floor = null;
        if (dto.getFloorId() != null) {
            floor = floorRepository.findById(dto.getFloorId())
                    .orElseThrow(() -> new IllegalArgumentException("Floor not found with ID: " + dto.getFloorId()));
        }

        agreement.setAgreementNumber(dto.getAgreementNumber());
        agreement.setFloor(floor);
        agreement.setAgreementAreaSft(dto.getAgreementAreaSft());
        agreement.setStartDate(dto.getStartDate());
        agreement.setEndDate(dto.getEndDate());
        agreement.setDurationMonths(dto.getDurationMonths());
        agreement.setRentPerSft(dto.getRentPerSft());
        agreement.setTotalMonthlyRent(dto.getTotalMonthlyRent());
        agreement.setServiceChargeAmount(dto.getServiceChargeAmount());
        agreement.setVatPercentage(dto.getVatPercentage());
        agreement.setTaxPercentage(dto.getTaxPercentage());
        agreement.setAdvanceDepositAmount(dto.getAdvanceDepositAmount());
        agreement.setSecurityDepositAmount(dto.getSecurityDepositAmount());
        agreement.setRentEscalationPercentage(dto.getRentEscalationPercentage());
        agreement.setEscalationFrequencyMonths(dto.getEscalationFrequencyMonths());
        agreement.setBillingCycle(dto.getBillingCycle());
        agreement.setGracePeriodDays(dto.getGracePeriodDays());
        agreement.setSignedByBdbl(dto.getSignedByBdbl());
        agreement.setSignedByTenant(dto.getSignedByTenant());
        agreement.setAgreementDate(dto.getAgreementDate());
        agreement.setCancellationNoticePeriod(dto.getCancellationNoticePeriod());
        agreement.setStatus(dto.getStatus());

        LeaseAgreement updatedAgreement = agreementRepository.save(agreement);
        return mapToLeaseAgreementDTO(updatedAgreement);
    }

    @Override
    public LeaseAgreementDTO getAgreementById(Long id) {
        LeaseAgreement agreement = agreementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found with ID: " + id));
        return mapToLeaseAgreementDTO(agreement);
    }

    @Override
    public LeaseAgreementDTO getAgreementByNumber(String agreementNumber) {
        LeaseAgreement agreement = agreementRepository.findByAgreementNumber(agreementNumber)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found with number: " + agreementNumber));
        return mapToLeaseAgreementDTO(agreement);
    }

    @Override
    public List<LeaseAgreementDTO> getAgreementsByTenant(Long tenantId) {
        return agreementRepository.findByTenantId(tenantId).stream()
                .map(this::mapToLeaseAgreementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaseAgreementDTO> getAgreementsByBuilding(Long buildingId) {
        return agreementRepository.findByBuildingId(buildingId).stream()
                .map(this::mapToLeaseAgreementDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaseAgreementDTO> getAllAgreements() {
        return agreementRepository.findAll().stream()
                .map(this::mapToLeaseAgreementDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // AGREEMENT PARKING OPERATIONS
    // ==========================================

    @Override
    public AgreementParkingDTO addParkingToAgreement(Long agreementId, AgreementParkingDTO dto) {
        LeaseAgreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found with ID: " + agreementId));

        ParkingSpace parkingSpace = parkingSpaceRepository.findById(dto.getParkingId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Parking space not found with ID: " + dto.getParkingId()));

        if (agreementParkingRepository.existsByAgreementIdAndParkingId(agreementId, dto.getParkingId())) {
            throw new IllegalArgumentException("Parking space already added to this agreement.");
        }

        AgreementParking agreementParking = AgreementParking.builder()
                .agreement(agreement)
                .parking(parkingSpace)
                .monthlyRent(dto.getMonthlyRent())
                .build();

        AgreementParking savedParking = agreementParkingRepository.save(agreementParking);

        // Update parking space status
        parkingSpace.setStatus("RESERVED");
        parkingSpaceRepository.save(parkingSpace);

        return mapToAgreementParkingDTO(savedParking);
    }

    @Override
    public void removeParkingFromAgreement(Long agreementId, Long parkingId) {
        AgreementParking agreementParking = agreementParkingRepository
                .findByAgreementIdAndParkingId(agreementId, parkingId)
                .orElseThrow(() -> new IllegalArgumentException("Parking not found for this agreement."));

        // Free up the parking space
        ParkingSpace parkingSpace = agreementParking.getParking();
        parkingSpace.setStatus("VACANT");
        parkingSpaceRepository.save(parkingSpace);

        agreementParkingRepository.delete(agreementParking);
    }

    @Override
    public List<AgreementParkingDTO> getAgreementParkings(Long agreementId) {
        return agreementParkingRepository.findByAgreementId(agreementId).stream()
                .map(this::mapToAgreementParkingDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // AGREEMENT DOCUMENT OPERATIONS
    // ==========================================

    @Override
    public AgreementDocumentDTO addDocument(Long agreementId, AgreementDocumentDTO dto) {
        LeaseAgreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found with ID: " + agreementId));

        AgreementDocument doc = AgreementDocument.builder()
                .agreement(agreement)
                .documentType(dto.getDocumentType())
                .documentName(dto.getDocumentName())
                .filePath(dto.getFilePath())
                .build();

        AgreementDocument savedDoc = agreementDocumentRepository.save(doc);
        return mapToAgreementDocumentDTO(savedDoc);
    }

    @Override
    public void removeDocument(Long documentId) {
        if (!agreementDocumentRepository.existsById(documentId)) {
            throw new IllegalArgumentException("Document not found with ID: " + documentId);
        }
        agreementDocumentRepository.deleteById(documentId);
    }

    @Override
    public List<AgreementDocumentDTO> getAgreementDocuments(Long agreementId) {
        return agreementDocumentRepository.findByAgreementId(agreementId).stream()
                .map(this::mapToAgreementDocumentDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // ADVANCE DEPOSIT OPERATIONS
    // ==========================================

    @Override
    public AdvanceDepositDTO addDeposit(Long agreementId, AdvanceDepositDTO dto) {
        LeaseAgreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found with ID: " + agreementId));

        AdvanceDeposit deposit = AdvanceDeposit.builder()
                .agreement(agreement)
                .depositType(dto.getDepositType())
                .originalAmount(dto.getOriginalAmount())
                .remainingAmount(dto.getOriginalAmount()) // Initial remaining is same as original
                .receivedDate(dto.getReceivedDate())
                .status("ACTIVE")
                .notes(dto.getNotes())
                .build();

        AdvanceDeposit savedDeposit = advanceDepositRepository.save(deposit);
        return mapToAdvanceDepositDTO(savedDeposit);
    }

    @Override
    public AdvanceDepositDTO updateDeposit(Long depositId, AdvanceDepositDTO dto) {
        AdvanceDeposit deposit = advanceDepositRepository.findById(depositId)
                .orElseThrow(() -> new IllegalArgumentException("Deposit not found with ID: " + depositId));

        deposit.setRemainingAmount(dto.getRemainingAmount());
        deposit.setStatus(dto.getStatus());
        deposit.setNotes(dto.getNotes());

        AdvanceDeposit updatedDeposit = advanceDepositRepository.save(deposit);
        return mapToAdvanceDepositDTO(updatedDeposit);
    }

    @Override
    public List<AdvanceDepositDTO> getDepositsByAgreement(Long agreementId) {
        return advanceDepositRepository.findByAgreementId(agreementId).stream()
                .map(this::mapToAdvanceDepositDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // MAPPING HELPER METHODS
    // ==========================================

    private LeaseAgreementDTO mapToLeaseAgreementDTO(LeaseAgreement agreement) {
        LeaseAgreementDTO dto = new LeaseAgreementDTO();
        dto.setId(agreement.getId());
        dto.setAgreementNumber(agreement.getAgreementNumber());
        dto.setTenantId(agreement.getTenant().getId());
        dto.setTenantCompanyName(agreement.getTenant().getCompanyName());
        dto.setBuildingId(agreement.getBuilding().getId());
        dto.setBuildingName(agreement.getBuilding().getName());

        if (agreement.getFloor() != null) {
            dto.setFloorId(agreement.getFloor().getId());
            dto.setFloorNumber(agreement.getFloor().getFloorNumber());
        }

        dto.setAgreementAreaSft(agreement.getAgreementAreaSft());
        dto.setAgreementType(agreement.getAgreementType());
        dto.setStartDate(agreement.getStartDate());
        dto.setEndDate(agreement.getEndDate());
        dto.setDurationMonths(agreement.getDurationMonths());
        dto.setRentPerSft(agreement.getRentPerSft());
        dto.setBaseRentRatePerSft(agreement.getRentPerSft());
        dto.setBaseMonthlyRent(agreement.getTotalMonthlyRent());
        dto.setTotalMonthlyRent(agreement.getTotalMonthlyRent());
        dto.setServiceChargeAmount(agreement.getServiceChargeAmount());
        dto.setServiceCharge(agreement.getServiceChargeAmount());
        dto.setVatPercentage(agreement.getVatPercentage());
        dto.setVatPercent(agreement.getVatPercentage());
        dto.setTaxPercentage(agreement.getTaxPercentage());
        dto.setTaxPercent(agreement.getTaxPercentage());
        dto.setAdvanceDepositAmount(agreement.getAdvanceDepositAmount());
        dto.setAdvanceAmount(agreement.getAdvanceDepositAmount());
        dto.setSecurityDepositAmount(agreement.getSecurityDepositAmount());
        dto.setRentEscalationPercentage(agreement.getRentEscalationPercentage());
        dto.setEscalationFrequencyMonths(agreement.getEscalationFrequencyMonths());
        dto.setBillingCycle(agreement.getBillingCycle());
        dto.setGracePeriodDays(agreement.getGracePeriodDays());
        dto.setSignedByBdbl(agreement.getSignedByBdbl());
        dto.setSignedByTenant(agreement.getSignedByTenant());
        dto.setAgreementDate(agreement.getAgreementDate());
        dto.setCancellationNoticePeriod(agreement.getCancellationNoticePeriod());
        dto.setStatus(agreement.getStatus());
        dto.setRemarks(agreement.getNotes());
        dto.setCreatedAt(agreement.getCreatedAt());
        return dto;
    }

    private AgreementParkingDTO mapToAgreementParkingDTO(AgreementParking parking) {
        AgreementParkingDTO dto = new AgreementParkingDTO();
        dto.setId(parking.getId());
        dto.setAgreementId(parking.getAgreement().getId());
        dto.setParkingId(parking.getParking().getId());
        dto.setParkingNumber(parking.getParking().getParkingNumber());
        dto.setMonthlyRent(parking.getMonthlyRent());
        return dto;
    }

    private AgreementDocumentDTO mapToAgreementDocumentDTO(AgreementDocument doc) {
        AgreementDocumentDTO dto = new AgreementDocumentDTO();
        dto.setId(doc.getId());
        dto.setAgreementId(doc.getAgreement().getId());
        dto.setDocumentType(doc.getDocumentType());
        dto.setDocumentName(doc.getDocumentName());
        dto.setFilePath(doc.getFilePath());
        dto.setUploadedAt(doc.getUploadedAt());
        return dto;
    }

    private AdvanceDepositDTO mapToAdvanceDepositDTO(AdvanceDeposit deposit) {
        AdvanceDepositDTO dto = new AdvanceDepositDTO();
        dto.setId(deposit.getId());
        dto.setAgreementId(deposit.getAgreement().getId());
        dto.setDepositType(deposit.getDepositType());
        dto.setOriginalAmount(deposit.getOriginalAmount());
        dto.setRemainingAmount(deposit.getRemainingAmount());
        dto.setReceivedDate(deposit.getReceivedDate());
        dto.setStatus(deposit.getStatus());
        dto.setNotes(deposit.getNotes());
        dto.setCreatedAt(deposit.getCreatedAt());
        dto.setUpdatedAt(deposit.getUpdatedAt());
        return dto;
    }
}
