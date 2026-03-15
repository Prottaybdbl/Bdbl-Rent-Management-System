package com.bdbl.rms.service;

import com.bdbl.rms.dto.FloorAllocationDTO;
import com.bdbl.rms.dto.SystemSettingDTO;
import com.bdbl.rms.dto.AuditLogDTO;

import java.util.List;

public interface SystemService {

    // Floor Allocation
    FloorAllocationDTO allocateFloor(FloorAllocationDTO dto);

    FloorAllocationDTO updateAllocation(Long id, FloorAllocationDTO dto);

    FloorAllocationDTO getAllocation(Long id);

    List<FloorAllocationDTO> getAllocationsByTenant(Long tenantId);

    List<FloorAllocationDTO> getAllocationsByAgreement(Long agreementId);

    // System Settings
    SystemSettingDTO saveSetting(SystemSettingDTO dto);

    SystemSettingDTO getSetting(String key);

    List<SystemSettingDTO> getAllSettings();

    // Audit Logs
    List<AuditLogDTO> getAuditLogsByUser(Long userId);

    List<AuditLogDTO> getRecentAuditLogs(int limit);
}
