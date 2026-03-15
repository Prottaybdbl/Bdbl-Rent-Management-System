package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tenant_contacts")
public class TenantContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "contact_name", length = 255)
    private String contactName;

    @Column(length = 100)
    private String designation;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String email;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    public TenantContact() {
    }

    public static TenantContactBuilder builder() {
        return new TenantContactBuilder();
    }

    public static class TenantContactBuilder {
        private TenantContact tc;

        private TenantContactBuilder() {
            tc = new TenantContact();
        }

        public TenantContactBuilder id(Long id) {
            tc.setId(id);
            return this;
        }

        public TenantContactBuilder tenant(Tenant tenant) {
            tc.setTenant(tenant);
            return this;
        }

        public TenantContactBuilder contactName(String contactName) {
            tc.setContactName(contactName);
            return this;
        }

        public TenantContactBuilder designation(String designation) {
            tc.setDesignation(designation);
            return this;
        }

        public TenantContactBuilder phone(String phone) {
            tc.setPhone(phone);
            return this;
        }

        public TenantContactBuilder email(String email) {
            tc.setEmail(email);
            return this;
        }

        public TenantContactBuilder isPrimary(Boolean isPrimary) {
            tc.setIsPrimary(isPrimary);
            return this;
        }

        public TenantContact build() {
            return tc;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TenantContact that = (TenantContact) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
