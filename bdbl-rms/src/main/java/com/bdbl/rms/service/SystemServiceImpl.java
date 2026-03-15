package com.bdbl.rms.service;

import com.bdbl.rms.dto.AuditLogDTO;
import com.bdbl.rms.dto.FloorAllocationDTO;
import com.bdbl.rms.dto.SystemSettingDTO;
import com.bdbl.rms.entity.*;
import com.bdbl.rms.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SystemServiceImpl implements SystemService {

    private final FloorAllocationRepository floorAllocationRepository;
    private final SystemSettingRepository systemSettingRepository;
    private final AuditLogRepository auditLogRepository;
    private final LeaseAgreementRepository leaseAgreementRepository;
    private final FloorRepository floorRepository;
    private final UserRepository userRepository;

    public SystemServiceImpl(
            FloorAllocationRepository floorAllocationRepository,
            SystemSettingRepository systemSettingRepository,
            AuditLogRepository auditLogRepository,
            LeaseAgreementRepository leaseAgreementRepository,
            FloorRepository floorRepository,
            UserRepository userRepository) {
        this.floorAllocationRepository = floorAllocationRepository;
        this.systemSettingRepository = systemSettingRepository;
        this.auditLogRepository = auditLogRepository;
        this.leaseAgreementRepository = leaseAgreementRepository;
        this.floorRepository = floorRepository;
        this.userRepository = userRepository;
    }

    // ==========================================
    // FLOOR ALLOCATION
    // ==========================================

    @Override
    public FloorAllocationDTO allocateFloor(FloorAllocationDTO dto) {
        LeaseAgreement agreement = leaseAgreementRepository.findById(dto.getAgreementId())
                .orElseThrow(() -> new IllegalArgumentException("Agreement not found"));
        Floor floor = floorRepository.findById(dto.getFloorId())
                .orElseThrow(() -> new IllegalArgumentException("Floor not found"));

        FloorAllocation allocation = FloorAllocation.builder()
                .agreement(agreement)
                .floor(floor)
                .allocationLabel(dto.getAllocationLabel())
                .areaSft(dto.getAreaSft())
                .rentType(dto.getRentType())
                .rentPerSft(dto.getRentPerSft())
                .lumpsumRent(dto.getLumpsumRent())
                .status(dto.getStatus() != null ? dto.getStatus() : "ACTIVE")
                // monthlyRent is calculated in PrePersist
                .build();

        return mapToFloorAllocationDTO(floorAllocationRepository.save(allocation));
    }

    @Override
    public FloorAllocationDTO updateAllocation(Long id, FloorAllocationDTO dto) {
        FloorAllocation allocation = floorAllocationRepository.findById(id).orElseThrow();
        allocation.setAllocationLabel(dto.getAllocationLabel());
        allocation.setAreaSft(dto.getAreaSft());
        allocation.setRentType(dto.getRentType());
        allocation.setRentPerSft(dto.getRentPerSft());
        allocation.setLumpsumRent(dto.getLumpsumRent());

        return mapToFloorAllocationDTO(floorAllocationRepository.save(allocation));
    }

    @Override
    public FloorAllocationDTO getAllocation(Long id) {
        return mapToFloorAllocationDTO(floorAllocationRepository.findById(id).orElseThrow());
    }

    @Override
    public List<FloorAllocationDTO> getAllocationsByTenant(Long tenantId) {
        return floorAllocationRepository.findByAgreementTenantId(tenantId).stream().map(this::mapToFloorAllocationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FloorAllocationDTO> getAllocationsByAgreement(Long agreementId) {
        return floorAllocationRepository.findByAgreementId(agreementId).stream().map(this::mapToFloorAllocationDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // SYSTEM SETTINGS
    // ==========================================

    @Override
    public SystemSettingDTO saveSetting(SystemSettingDTO dto) {
        SystemSetting setting = systemSettingRepository.findBySettingKey(dto.getSettingKey())
                .orElse(new SystemSetting());

        setting.setSettingKey(dto.getSettingKey());
        setting.setSettingValue(dto.getSettingValue());
        setting.setDescription(dto.getDescription());

        if (dto.getUpdatedById() != null) {
            userRepository.findById(dto.getUpdatedById()).ifPresent(setting::setUpdatedBy);
        }

        return mapToSystemSettingDTO(systemSettingRepository.save(setting));
    }

    @Override
    public SystemSettingDTO getSetting(String key) {
        return mapToSystemSettingDTO(systemSettingRepository.findBySettingKey(key)
                .orElseThrow(() -> new IllegalArgumentException("Setting not found: " + key)));
    }

    @Override
    public List<SystemSettingDTO> getAllSettings() {
        return systemSettingRepository.findAll().stream().map(this::mapToSystemSettingDTO).collect(Collectors.toList());
    }

    // ==========================================
    // AUDIT LOGS
    // ==========================================

    @Override
    public List<AuditLogDTO> getAuditLogsByUser(Long userId) {
        return auditLogRepository.findByUserId(userId).stream().map(this::mapToAuditLogDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLogDTO> getRecentAuditLogs(int limit) {
        return auditLogRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "actionTime")))
                .stream().map(this::mapToAuditLogDTO).collect(Collectors.toList());
    }

    // ==========================================
    // MAPPING
    // ==========================================

    private FloorAllocationDTO mapToFloorAllocationDTO(FloorAllocation entity) {
        FloorAllocationDTO d = new FloorAllocationDTO();
        d.setId(entity.getId());
        d.setAgreementId(entity.getAgreement().getId());
        d.setAgreementNumber(entity.getAgreement().getAgreementNumber());
        d.setFloorId(entity.getFloor().getId());
        d.setFloorName(entity.getFloor().getFloorName());
        d.setAllocationLabel(entity.getAllocationLabel());
        d.setAreaSft(entity.getAreaSft());
        d.setRentType(entity.getRentType());
        d.setRentPerSft(entity.getRentPerSft());
        d.setLumpsumRent(entity.getLumpsumRent());
        d.setMonthlyRent(entity.getMonthlyRent());
        d.setStatus(entity.getStatus());
        d.setCreatedAt(entity.getCreatedAt());
        return d;
    }

    private SystemSettingDTO mapToSystemSettingDTO(SystemSetting entity) {
        SystemSettingDTO d = new SystemSettingDTO();
        d.setSettingKey(entity.getSettingKey());
        d.setSettingValue(entity.getSettingValue());
        d.setDescription(entity.getDescription());
        d.setUpdatedAt(entity.getUpdatedAt());
        if (entity.getUpdatedBy() != null) {
            d.setUpdatedById(entity.getUpdatedBy().getId());
            d.setUpdatedByUsername(entity.getUpdatedBy().getUsername());
        }
        return d;
    }

    private AuditLogDTO mapToAuditLogDTO(AuditLog entity) {
        AuditLogDTO d = new AuditLogDTO();
        d.setId(entity.getId());
        d.setActionType(entity.getActionType());
        d.setTableName(entity.getTableName());
        d.setRecordId(entity.getRecordId());
        d.setOldValues(entity.getOldValues());
        d.setNewValues(entity.getNewValues());
        d.setIpAddress(entity.getIpAddress());
        d.setActionTime(entity.getActionTime());
        if (entity.getUser() != null) {
            d.setUserId(entity.getUser().getId());
            d.setUsername(entity.getUser().getUsername());
        }
        return d;
    }
}
