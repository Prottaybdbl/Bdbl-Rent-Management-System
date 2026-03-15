package com.bdbl.rms.service;

import com.bdbl.rms.dto.TenantContactDTO;
import com.bdbl.rms.dto.TenantDTO;
import com.bdbl.rms.entity.Tenant;
import com.bdbl.rms.entity.TenantContact;
import com.bdbl.rms.repository.TenantContactRepository;
import com.bdbl.rms.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TenantServiceImpl implements TenantService {

    private static final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final TenantRepository tenantRepository;
    private final TenantContactRepository tenantContactRepository;

    public TenantServiceImpl(TenantRepository tenantRepository, TenantContactRepository tenantContactRepository) {
        this.tenantRepository = tenantRepository;
        this.tenantContactRepository = tenantContactRepository;
    }

    // ==========================================
    // TENANT OPERATIONS
    // ==========================================

    @Override
    public TenantDTO createTenant(TenantDTO dto) {
        log.info("Creating new tenant: {}", dto.getCompanyName());

        if (tenantRepository.existsByTenantCode(dto.getTenantCode())) {
            throw new IllegalArgumentException("Tenant code already exists: " + dto.getTenantCode());
        }

        Tenant tenant = Tenant.builder()
                .tenantCode(dto.getTenantCode())
                .companyName(dto.getCompanyName())
                .businessType(dto.getBusinessType())
                .tradeLicenseNo(dto.getTradeLicenseNo())
                .tinNumber(dto.getTinNumber())
                .registrationAddress(dto.getRegistrationAddress())
                .contactAddress(dto.getContactAddress())
                .phonePrimary(dto.getPhonePrimary())
                .phoneSecondary(dto.getPhoneSecondary())
                .email(dto.getEmail())
                .logoPath(dto.getLogoPath())
                .status("ACTIVE")
                .build();

        Tenant savedTenant = tenantRepository.save(tenant);
        return mapToTenantDTO(savedTenant);
    }

    @Override
    public TenantDTO updateTenant(Long id, TenantDTO dto) {
        log.info("Updating tenant with ID: {}", id);

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + id));

        if (!tenant.getTenantCode().equals(dto.getTenantCode()) &&
                tenantRepository.existsByTenantCode(dto.getTenantCode())) {
            throw new IllegalArgumentException("Tenant code already exists: " + dto.getTenantCode());
        }

        tenant.setTenantCode(dto.getTenantCode());
        tenant.setCompanyName(dto.getCompanyName());
        tenant.setBusinessType(dto.getBusinessType());
        tenant.setTradeLicenseNo(dto.getTradeLicenseNo());
        tenant.setTinNumber(dto.getTinNumber());
        tenant.setRegistrationAddress(dto.getRegistrationAddress());
        tenant.setContactAddress(dto.getContactAddress());
        tenant.setPhonePrimary(dto.getPhonePrimary());
        tenant.setPhoneSecondary(dto.getPhoneSecondary());
        tenant.setEmail(dto.getEmail());
        tenant.setLogoPath(dto.getLogoPath());
        tenant.setStatus(dto.getStatus());

        Tenant updatedTenant = tenantRepository.save(tenant);
        return mapToTenantDTO(updatedTenant);
    }

    @Override
    public TenantDTO getTenantById(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + id));
        return mapToTenantDTO(tenant);
    }

    @Override
    public TenantDTO getTenantByCode(String code) {
        Tenant tenant = tenantRepository.findByTenantCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with code: " + code));
        return mapToTenantDTO(tenant);
    }

    @Override
    public List<TenantDTO> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::mapToTenantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTenant(Long id) {
        log.info("Deleting tenant with ID: {}", id);
        if (!tenantRepository.existsById(id)) {
            throw new IllegalArgumentException("Tenant not found with ID: " + id);
        }
        tenantRepository.deleteById(id);
    }

    // ==========================================
    // TENANT CONTACT OPERATIONS
    // ==========================================

    @Override
    public TenantContactDTO addContact(Long tenantId, TenantContactDTO dto) {
        log.info("Adding contact to tenant ID: {}", tenantId);

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + tenantId));

        // If this is set as primary, un-set others
        if (Boolean.TRUE.equals(dto.getIsPrimary())) {
            List<TenantContact> existingContacts = tenantContactRepository.findByTenantId(tenantId);
            for (TenantContact contact : existingContacts) {
                if (Boolean.TRUE.equals(contact.getIsPrimary())) {
                    contact.setIsPrimary(false);
                    tenantContactRepository.save(contact);
                }
            }
        }

        TenantContact contact = TenantContact.builder()
                .tenant(tenant)
                .contactName(dto.getContactName())
                .designation(dto.getDesignation())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .isPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false)
                .build();

        TenantContact savedContact = tenantContactRepository.save(contact);
        return mapToTenantContactDTO(savedContact);
    }

    @Override
    public TenantContactDTO updateContact(Long contactId, TenantContactDTO dto) {
        log.info("Updating contact with ID: {}", contactId);

        TenantContact contact = tenantContactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with ID: " + contactId));

        if (Boolean.TRUE.equals(dto.getIsPrimary()) && !Boolean.TRUE.equals(contact.getIsPrimary())) {
            List<TenantContact> existingContacts = tenantContactRepository.findByTenantId(contact.getTenant().getId());
            for (TenantContact existingContact : existingContacts) {
                if (Boolean.TRUE.equals(existingContact.getIsPrimary()) && !existingContact.getId().equals(contactId)) {
                    existingContact.setIsPrimary(false);
                    tenantContactRepository.save(existingContact);
                }
            }
        }

        contact.setContactName(dto.getContactName());
        contact.setDesignation(dto.getDesignation());
        contact.setPhone(dto.getPhone());
        contact.setEmail(dto.getEmail());
        contact.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);

        TenantContact updatedContact = tenantContactRepository.save(contact);
        return mapToTenantContactDTO(updatedContact);
    }

    @Override
    public List<TenantContactDTO> getContactsByTenantId(Long tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new IllegalArgumentException("Tenant not found with ID: " + tenantId);
        }

        return tenantContactRepository.findByTenantId(tenantId).stream()
                .map(this::mapToTenantContactDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteContact(Long contactId) {
        log.info("Deleting contact with ID: {}", contactId);
        if (!tenantContactRepository.existsById(contactId)) {
            throw new IllegalArgumentException("Contact not found with ID: " + contactId);
        }
        tenantContactRepository.deleteById(contactId);
    }

    @Override
    public TenantContactDTO setPrimaryContact(Long tenantId, Long contactId) {
        log.info("Setting contact ID {} as primary for tenant ID: {}", contactId, tenantId);

        if (!tenantRepository.existsById(tenantId)) {
            throw new IllegalArgumentException("Tenant not found with ID: " + tenantId);
        }

        TenantContact newPrimary = tenantContactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found with ID: " + contactId));

        if (!newPrimary.getTenant().getId().equals(tenantId)) {
            throw new IllegalArgumentException("Contact does not belong to the specified tenant.");
        }

        List<TenantContact> allContacts = tenantContactRepository.findByTenantId(tenantId);
        for (TenantContact contact : allContacts) {
            boolean shouldBePrimary = contact.getId().equals(contactId);
            if (contact.getIsPrimary() != shouldBePrimary) {
                contact.setIsPrimary(shouldBePrimary);
                tenantContactRepository.save(contact);
            }
        }

        return mapToTenantContactDTO(newPrimary);
    }

    // ==========================================
    // MAPPING HELPER METHODS
    // ==========================================

    private TenantDTO mapToTenantDTO(Tenant tenant) {
        TenantDTO dto = new TenantDTO();
        dto.setId(tenant.getId());
        dto.setTenantCode(tenant.getTenantCode());
        dto.setCompanyName(tenant.getCompanyName());
        dto.setBusinessType(tenant.getBusinessType());
        dto.setTradeLicenseNo(tenant.getTradeLicenseNo());
        dto.setTinNumber(tenant.getTinNumber());
        dto.setRegistrationAddress(tenant.getRegistrationAddress());
        dto.setContactAddress(tenant.getContactAddress());
        dto.setPhonePrimary(tenant.getPhonePrimary());
        dto.setPhoneSecondary(tenant.getPhoneSecondary());
        dto.setEmail(tenant.getEmail());
        dto.setLogoPath(tenant.getLogoPath());
        dto.setStatus(tenant.getStatus());
        dto.setCreatedAt(tenant.getCreatedAt());

        if (tenant.getContacts() != null) {
            dto.setContacts(tenant.getContacts().stream()
                    .map(this::mapToTenantContactDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private TenantContactDTO mapToTenantContactDTO(TenantContact contact) {
        TenantContactDTO dto = new TenantContactDTO();
        dto.setId(contact.getId());
        dto.setTenantId(contact.getTenant().getId());
        dto.setContactName(contact.getContactName());
        dto.setDesignation(contact.getDesignation());
        dto.setPhone(contact.getPhone());
        dto.setEmail(contact.getEmail());
        dto.setIsPrimary(contact.getIsPrimary());
        return dto;
    }
}
