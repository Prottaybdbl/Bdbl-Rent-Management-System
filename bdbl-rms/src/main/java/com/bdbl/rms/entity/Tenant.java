package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_code", unique = true, length = 50)
    private String tenantCode;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "business_type", length = 100)
    private String businessType;

    @Column(name = "trade_license_no", length = 100)
    private String tradeLicenseNo;

    @Column(name = "tin_number", length = 100)
    private String tinNumber;

    @Column(name = "registration_address", columnDefinition = "TEXT")
    private String registrationAddress;

    @Column(name = "contact_address", columnDefinition = "TEXT")
    private String contactAddress;

    @Column(name = "phone_primary", length = 20)
    private String phonePrimary;

    @Column(name = "phone_secondary", length = 20)
    private String phoneSecondary;

    @Column(length = 255)
    private String email;

    @Column(name = "logo_path", length = 500)
    private String logoPath;

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TenantContact> contacts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Tenant() {
    }

    public static TenantBuilder builder() {
        return new TenantBuilder();
    }

    public static class TenantBuilder {
        private Tenant tenant;

        private TenantBuilder() {
            tenant = new Tenant();
        }

        public TenantBuilder id(Long id) {
            tenant.setId(id);
            return this;
        }

        public TenantBuilder tenantCode(String tenantCode) {
            tenant.setTenantCode(tenantCode);
            return this;
        }

        public TenantBuilder companyName(String companyName) {
            tenant.setCompanyName(companyName);
            return this;
        }

        public TenantBuilder businessType(String businessType) {
            tenant.setBusinessType(businessType);
            return this;
        }

        public TenantBuilder tradeLicenseNo(String tradeLicenseNo) {
            tenant.setTradeLicenseNo(tradeLicenseNo);
            return this;
        }

        public TenantBuilder tinNumber(String tinNumber) {
            tenant.setTinNumber(tinNumber);
            return this;
        }

        public TenantBuilder registrationAddress(String registrationAddress) {
            tenant.setRegistrationAddress(registrationAddress);
            return this;
        }

        public TenantBuilder contactAddress(String contactAddress) {
            tenant.setContactAddress(contactAddress);
            return this;
        }

        public TenantBuilder phonePrimary(String phonePrimary) {
            tenant.setPhonePrimary(phonePrimary);
            return this;
        }

        public TenantBuilder phoneSecondary(String phoneSecondary) {
            tenant.setPhoneSecondary(phoneSecondary);
            return this;
        }

        public TenantBuilder email(String email) {
            tenant.setEmail(email);
            return this;
        }

        public TenantBuilder logoPath(String logoPath) {
            tenant.setLogoPath(logoPath);
            return this;
        }

        public TenantBuilder status(String status) {
            tenant.setStatus(status);
            return this;
        }

        public Tenant build() {
            return tenant;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getTradeLicenseNo() {
        return tradeLicenseNo;
    }

    public void setTradeLicenseNo(String tradeLicenseNo) {
        this.tradeLicenseNo = tradeLicenseNo;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(String registrationAddress) {
        this.registrationAddress = registrationAddress;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getPhonePrimary() {
        return phonePrimary;
    }

    public void setPhonePrimary(String phonePrimary) {
        this.phonePrimary = phonePrimary;
    }

    public String getPhoneSecondary() {
        return phoneSecondary;
    }

    public void setPhoneSecondary(String phoneSecondary) {
        this.phoneSecondary = phoneSecondary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<TenantContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<TenantContact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Tenant tenant = (Tenant) o;
        return Objects.equals(id, tenant.id) && Objects.equals(tenantCode, tenant.tenantCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantCode);
    }
}
