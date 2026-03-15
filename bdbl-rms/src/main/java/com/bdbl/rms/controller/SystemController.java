package com.bdbl.rms.controller;

import com.bdbl.rms.dto.AuditLogDTO;
import com.bdbl.rms.dto.FloorAllocationDTO;
import com.bdbl.rms.dto.SystemSettingDTO;
import com.bdbl.rms.service.SystemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    private final SystemService systemService;

    public SystemController(SystemService systemService) {
        this.systemService = systemService;
    }

    // ==========================================
    // FLOOR ALLOCATION
    // ==========================================

    @PostMapping("/allocations")
    public ResponseEntity<FloorAllocationDTO> allocateFloor(@Valid @RequestBody FloorAllocationDTO dto) {
        return new ResponseEntity<>(systemService.allocateFloor(dto), HttpStatus.CREATED);
    }

    @PutMapping("/allocations/{id}")
    public ResponseEntity<FloorAllocationDTO> updateAllocation(@PathVariable Long id,
            @Valid @RequestBody FloorAllocationDTO dto) {
        return ResponseEntity.ok(systemService.updateAllocation(id, dto));
    }

    @GetMapping("/allocations/{id}")
    public ResponseEntity<FloorAllocationDTO> getAllocationById(@PathVariable Long id) {
        return ResponseEntity.ok(systemService.getAllocation(id));
    }

    @GetMapping("/allocations/tenant/{tenantId}")
    public ResponseEntity<List<FloorAllocationDTO>> getAllocationsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(systemService.getAllocationsByTenant(tenantId));
    }

    @GetMapping("/allocations/agreement/{agreementId}")
    public ResponseEntity<List<FloorAllocationDTO>> getAllocationsByAgreement(@PathVariable Long agreementId) {
        return ResponseEntity.ok(systemService.getAllocationsByAgreement(agreementId));
    }

    // ==========================================
    // SYSTEM SETTINGS
    // ==========================================

    @PostMapping("/settings")
    public ResponseEntity<SystemSettingDTO> saveSetting(@Valid @RequestBody SystemSettingDTO dto) {
        return ResponseEntity.ok(systemService.saveSetting(dto));
    }

    @GetMapping("/settings/{key}")
    public ResponseEntity<SystemSettingDTO> getSetting(@PathVariable String key) {
        return ResponseEntity.ok(systemService.getSetting(key));
    }

    @GetMapping("/settings")
    public ResponseEntity<List<SystemSettingDTO>> getAllSettings() {
        return ResponseEntity.ok(systemService.getAllSettings());
    }

    // ==========================================
    // AUDIT LOGS
    // ==========================================

    @GetMapping("/audit-logs/user/{userId}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(systemService.getAuditLogsByUser(userId));
    }

    @GetMapping("/audit-logs/recent")
    public ResponseEntity<List<AuditLogDTO>> getRecentAuditLogs(@RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(systemService.getRecentAuditLogs(limit));
    }
}
