package com.bdbl.rms.controller;

import com.bdbl.rms.dto.TenantContactDTO;
import com.bdbl.rms.dto.TenantDTO;
import com.bdbl.rms.service.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    // ==========================================
    // TENANT ENDPOINTS
    // ==========================================

    @PostMapping
    public ResponseEntity<TenantDTO> createTenant(@RequestBody TenantDTO tenantDTO) {
        return new ResponseEntity<>(tenantService.createTenant(tenantDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenantDTO> updateTenant(@PathVariable Long id, @RequestBody TenantDTO tenantDTO) {
        return ResponseEntity.ok(tenantService.updateTenant(id, tenantDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantDTO> getTenant(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.getTenantById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<TenantDTO> getTenantByCode(@PathVariable String code) {
        return ResponseEntity.ok(tenantService.getTenantByCode(code));
    }

    @GetMapping
    public ResponseEntity<List<TenantDTO>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTenant(@PathVariable Long id) {
        tenantService.deleteTenant(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // TENANT CONTACT ENDPOINTS
    // ==========================================

    @PostMapping("/{tenantId}/contacts")
    public ResponseEntity<TenantContactDTO> addContact(@PathVariable Long tenantId,
            @RequestBody TenantContactDTO contactDTO) {
        return new ResponseEntity<>(tenantService.addContact(tenantId, contactDTO), HttpStatus.CREATED);
    }

    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<TenantContactDTO> updateContact(@PathVariable Long contactId,
            @RequestBody TenantContactDTO contactDTO) {
        return ResponseEntity.ok(tenantService.updateContact(contactId, contactDTO));
    }

    @GetMapping("/{tenantId}/contacts")
    public ResponseEntity<List<TenantContactDTO>> getContactsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(tenantService.getContactsByTenantId(tenantId));
    }

    @DeleteMapping("/contacts/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long contactId) {
        tenantService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tenantId}/contacts/{contactId}/primary")
    public ResponseEntity<TenantContactDTO> setPrimaryContact(@PathVariable Long tenantId,
            @PathVariable Long contactId) {
        return ResponseEntity.ok(tenantService.setPrimaryContact(tenantId, contactId));
    }
}
