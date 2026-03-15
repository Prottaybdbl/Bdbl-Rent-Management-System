package com.bdbl.rms.service;

import com.bdbl.rms.dto.TenantContactDTO;
import com.bdbl.rms.dto.TenantDTO;

import java.util.List;

public interface TenantService {

    // === Tenant Operations ===
    TenantDTO createTenant(TenantDTO tenantDTO);

    TenantDTO updateTenant(Long id, TenantDTO tenantDTO);

    TenantDTO getTenantById(Long id);

    TenantDTO getTenantByCode(String code);

    List<TenantDTO> getAllTenants();

    void deleteTenant(Long id);

    // === Tenant Contact Operations ===
    TenantContactDTO addContact(Long tenantId, TenantContactDTO contactDTO);

    TenantContactDTO updateContact(Long contactId, TenantContactDTO contactDTO);

    List<TenantContactDTO> getContactsByTenantId(Long tenantId);

    void deleteContact(Long contactId);

    TenantContactDTO setPrimaryContact(Long tenantId, Long contactId);
}
