package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lease_agreements")
public class LeaseAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agreement_number", unique = true, length = 100)
    private String agreementNumber; // Automatically generated

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "agreement_type", length = 20)
    private String agreementType; // NEW, RENEWAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_agreement_id")
    private LeaseAgreement parentAgreement; // For renewals

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "duration_months")
    private Integer durationMonths;

    @Column(name = "controlled_by", length = 100)
    private String controlledBy; // BDBL, OTHER

    @Column(name = "service_charge", precision = 12, scale = 2)
    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @Column(name = "total_monthly_rent", precision = 12, scale = 2)
    private BigDecimal totalMonthlyRent; // Base + Service + Parking

    @Column(name = "has_security_deposit")
    private Boolean hasSecurityDeposit = false;

    @Column(name = "has_advance_payment")
    private Boolean hasAdvancePayment = false;

    @Column(name = "has_penalty")
    private Boolean hasPenalty = false;

    @Column(name = "penalty_percentage", precision = 5, scale = 2)
    private BigDecimal penaltyPercentage;

    @Column(name = "has_annual_increment")
    private Boolean hasAnnualIncrement = false;

    @Column(name = "increment_percentage", precision = 5, scale = 2)
    private BigDecimal incrementPercentage;

    @Column(name = "increment_period_months")
    private Integer incrementPeriodMonths;

    @Column(name = "notice_period_days")
    private Integer noticePeriodDays = 60;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;

    @Column(name = "agreement_area_sft", precision = 12, scale = 2)
    private BigDecimal agreementAreaSft;

    @Column(name = "rent_per_sft", precision = 12, scale = 2)
    private BigDecimal rentPerSft;

    @Column(name = "vat_percentage", precision = 5, scale = 2)
    private BigDecimal vatPercentage;

    @Column(name = "tax_percentage", precision = 5, scale = 2)
    private BigDecimal taxPercentage;

    @Column(name = "advance_deposit_amount", precision = 15, scale = 2)
    private BigDecimal advanceDepositAmount;

    @Column(name = "security_deposit_amount", precision = 15, scale = 2)
    private BigDecimal securityDepositAmount;

    @Column(name = "rent_escalation_percentage")
    private Integer rentEscalationPercentage;

    @Column(name = "escalation_frequency_months")
    private Integer escalationFrequencyMonths;

    @Column(name = "billing_cycle", length = 20)
    private String billingCycle;

    @Column(name = "grace_period_days")
    private Integer gracePeriodDays;

    @Column(name = "signed_by_bdbl", length = 100)
    private String signedByBdbl;

    @Column(name = "signed_by_tenant", length = 100)
    private String signedByTenant;

    @Column(name = "agreement_date")
    private LocalDate agreementDate;

    @Column(name = "cancellation_notice_period", length = 50)
    private String cancellationNoticePeriod;

    @Column(length = 20)
    private String status = "ACTIVE"; // DRAFT, ACTIVE, EXPIRED, TERMINATED, RENEWED

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // Relationships
    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FloorAllocation> floorAllocations = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementParking> agreementParkings = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgreementDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "agreement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdvanceDeposit> deposits = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (serviceCharge == null)
            serviceCharge = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LeaseAgreement() {
    }

    public static LeaseAgreementBuilder builder() {
        return new LeaseAgreementBuilder();
    }

    public static class LeaseAgreementBuilder {
        private LeaseAgreement la;

        private LeaseAgreementBuilder() {
            la = new LeaseAgreement();
        }

        public LeaseAgreementBuilder id(Long id) {
            la.setId(id);
            return this;
        }

        public LeaseAgreementBuilder agreementNumber(String agreementNumber) {
            la.setAgreementNumber(agreementNumber);
            return this;
        }

        public LeaseAgreementBuilder tenant(Tenant tenant) {
            la.setTenant(tenant);
            return this;
        }

        public LeaseAgreementBuilder building(Building building) {
            la.setBuilding(building);
            return this;
        }

        public LeaseAgreementBuilder agreementType(String agreementType) {
            la.setAgreementType(agreementType);
            return this;
        }

        public LeaseAgreementBuilder parentAgreement(LeaseAgreement parentAgreement) {
            la.setParentAgreement(parentAgreement);
            return this;
        }

        public LeaseAgreementBuilder startDate(LocalDate startDate) {
            la.setStartDate(startDate);
            return this;
        }

        public LeaseAgreementBuilder endDate(LocalDate endDate) {
            la.setEndDate(endDate);
            return this;
        }

        public LeaseAgreementBuilder durationMonths(Integer durationMonths) {
            la.setDurationMonths(durationMonths);
            return this;
        }

        public LeaseAgreementBuilder controlledBy(String controlledBy) {
            la.setControlledBy(controlledBy);
            return this;
        }

        public LeaseAgreementBuilder serviceChargeAmount(BigDecimal serviceChargeAmount) {
            la.setServiceChargeAmount(serviceChargeAmount);
            return this;
        }

        public LeaseAgreementBuilder totalMonthlyRent(BigDecimal totalMonthlyRent) {
            la.setTotalMonthlyRent(totalMonthlyRent);
            return this;
        }

        public LeaseAgreementBuilder hasSecurityDeposit(Boolean hasSecurityDeposit) {
            la.setHasSecurityDeposit(hasSecurityDeposit);
            return this;
        }

        public LeaseAgreementBuilder hasAdvancePayment(Boolean hasAdvancePayment) {
            la.setHasAdvancePayment(hasAdvancePayment);
            return this;
        }

        public LeaseAgreementBuilder hasPenalty(Boolean hasPenalty) {
            la.setHasPenalty(hasPenalty);
            return this;
        }

        public LeaseAgreementBuilder penaltyPercentage(BigDecimal penaltyPercentage) {
            la.setPenaltyPercentage(penaltyPercentage);
            return this;
        }

        public LeaseAgreementBuilder hasAnnualIncrement(Boolean hasAnnualIncrement) {
            la.setHasAnnualIncrement(hasAnnualIncrement);
            return this;
        }

        public LeaseAgreementBuilder incrementPercentage(BigDecimal incrementPercentage) {
            la.setIncrementPercentage(incrementPercentage);
            return this;
        }

        public LeaseAgreementBuilder incrementPeriodMonths(Integer incrementPeriodMonths) {
            la.setIncrementPeriodMonths(incrementPeriodMonths);
            return this;
        }

        public LeaseAgreementBuilder noticePeriodDays(Integer noticePeriodDays) {
            la.setNoticePeriodDays(noticePeriodDays);
            return this;
        }

        public LeaseAgreementBuilder status(String status) {
            la.setStatus(status);
            return this;
        }

        public LeaseAgreementBuilder notes(String notes) {
            la.setNotes(notes);
            return this;
        }

        public LeaseAgreementBuilder createdBy(User createdBy) {
            la.setCreatedBy(createdBy);
            return this;
        }

        public LeaseAgreementBuilder floor(Floor floor) {
            la.setFloor(floor);
            return this;
        }

        public LeaseAgreementBuilder agreementAreaSft(BigDecimal agreementAreaSft) {
            la.setAgreementAreaSft(agreementAreaSft);
            return this;
        }

        public LeaseAgreementBuilder rentPerSft(BigDecimal rentPerSft) {
            la.setRentPerSft(rentPerSft);
            return this;
        }

        public LeaseAgreementBuilder vatPercentage(BigDecimal vatPercentage) {
            la.setVatPercentage(vatPercentage);
            return this;
        }

        public LeaseAgreementBuilder taxPercentage(BigDecimal taxPercentage) {
            la.setTaxPercentage(taxPercentage);
            return this;
        }

        public LeaseAgreementBuilder advanceDepositAmount(BigDecimal advanceDepositAmount) {
            la.setAdvanceDepositAmount(advanceDepositAmount);
            return this;
        }

        public LeaseAgreementBuilder securityDepositAmount(BigDecimal securityDepositAmount) {
            la.setSecurityDepositAmount(securityDepositAmount);
            return this;
        }

        public LeaseAgreementBuilder rentEscalationPercentage(Integer rentEscalationPercentage) {
            la.setRentEscalationPercentage(rentEscalationPercentage);
            return this;
        }

        public LeaseAgreementBuilder escalationFrequencyMonths(Integer escalationFrequencyMonths) {
            la.setEscalationFrequencyMonths(escalationFrequencyMonths);
            return this;
        }

        public LeaseAgreementBuilder billingCycle(String billingCycle) {
            la.setBillingCycle(billingCycle);
            return this;
        }

        public LeaseAgreementBuilder gracePeriodDays(Integer gracePeriodDays) {
            la.setGracePeriodDays(gracePeriodDays);
            return this;
        }

        public LeaseAgreementBuilder signedByBdbl(String signedByBdbl) {
            la.setSignedByBdbl(signedByBdbl);
            return this;
        }

        public LeaseAgreementBuilder signedByTenant(String signedByTenant) {
            la.setSignedByTenant(signedByTenant);
            return this;
        }

        public LeaseAgreementBuilder agreementDate(LocalDate agreementDate) {
            la.setAgreementDate(agreementDate);
            return this;
        }

        public LeaseAgreementBuilder cancellationNoticePeriod(String cancellationNoticePeriod) {
            la.setCancellationNoticePeriod(cancellationNoticePeriod);
            return this;
        }

        public LeaseAgreement build() {
            return la;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public LeaseAgreement getParentAgreement() {
        return parentAgreement;
    }

    public void setParentAgreement(LeaseAgreement parentAgreement) {
        this.parentAgreement = parentAgreement;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getControlledBy() {
        return controlledBy;
    }

    public void setControlledBy(String controlledBy) {
        this.controlledBy = controlledBy;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getTotalMonthlyRent() {
        return totalMonthlyRent;
    }

    public void setTotalMonthlyRent(BigDecimal totalMonthlyRent) {
        this.totalMonthlyRent = totalMonthlyRent;
    }

    public Boolean getHasSecurityDeposit() {
        return hasSecurityDeposit;
    }

    public void setHasSecurityDeposit(Boolean hasSecurityDeposit) {
        this.hasSecurityDeposit = hasSecurityDeposit;
    }

    public Boolean getHasAdvancePayment() {
        return hasAdvancePayment;
    }

    public void setHasAdvancePayment(Boolean hasAdvancePayment) {
        this.hasAdvancePayment = hasAdvancePayment;
    }

    public Boolean getHasPenalty() {
        return hasPenalty;
    }

    public void setHasPenalty(Boolean hasPenalty) {
        this.hasPenalty = hasPenalty;
    }

    public BigDecimal getPenaltyPercentage() {
        return penaltyPercentage;
    }

    public void setPenaltyPercentage(BigDecimal penaltyPercentage) {
        this.penaltyPercentage = penaltyPercentage;
    }

    public Boolean getHasAnnualIncrement() {
        return hasAnnualIncrement;
    }

    public void setHasAnnualIncrement(Boolean hasAnnualIncrement) {
        this.hasAnnualIncrement = hasAnnualIncrement;
    }

    public BigDecimal getIncrementPercentage() {
        return incrementPercentage;
    }

    public void setIncrementPercentage(BigDecimal incrementPercentage) {
        this.incrementPercentage = incrementPercentage;
    }

    public Integer getIncrementPeriodMonths() {
        return incrementPeriodMonths;
    }

    public void setIncrementPeriodMonths(Integer incrementPeriodMonths) {
        this.incrementPeriodMonths = incrementPeriodMonths;
    }

    public Integer getNoticePeriodDays() {
        return noticePeriodDays;
    }

    public void setNoticePeriodDays(Integer noticePeriodDays) {
        this.noticePeriodDays = noticePeriodDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public BigDecimal getAgreementAreaSft() {
        return agreementAreaSft;
    }

    public void setAgreementAreaSft(BigDecimal agreementAreaSft) {
        this.agreementAreaSft = agreementAreaSft;
    }

    public BigDecimal getRentPerSft() {
        return rentPerSft;
    }

    public void setRentPerSft(BigDecimal rentPerSft) {
        this.rentPerSft = rentPerSft;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceCharge; // Make this work correctly based on entity properties
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceCharge = serviceChargeAmount; // Map back
    }

    public BigDecimal getVatPercentage() {
        return vatPercentage;
    }

    public void setVatPercentage(BigDecimal vatPercentage) {
        this.vatPercentage = vatPercentage;
    }

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    public void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public BigDecimal getAdvanceDepositAmount() {
        return advanceDepositAmount;
    }

    public void setAdvanceDepositAmount(BigDecimal advanceDepositAmount) {
        this.advanceDepositAmount = advanceDepositAmount;
    }

    public BigDecimal getSecurityDepositAmount() {
        return securityDepositAmount;
    }

    public void setSecurityDepositAmount(BigDecimal securityDepositAmount) {
        this.securityDepositAmount = securityDepositAmount;
    }

    public Integer getRentEscalationPercentage() {
        return rentEscalationPercentage;
    }

    public void setRentEscalationPercentage(Integer rentEscalationPercentage) {
        this.rentEscalationPercentage = rentEscalationPercentage;
    }

    public Integer getEscalationFrequencyMonths() {
        return escalationFrequencyMonths;
    }

    public void setEscalationFrequencyMonths(Integer escalationFrequencyMonths) {
        this.escalationFrequencyMonths = escalationFrequencyMonths;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Integer getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(Integer gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    public String getSignedByBdbl() {
        return signedByBdbl;
    }

    public void setSignedByBdbl(String signedByBdbl) {
        this.signedByBdbl = signedByBdbl;
    }

    public String getSignedByTenant() {
        return signedByTenant;
    }

    public void setSignedByTenant(String signedByTenant) {
        this.signedByTenant = signedByTenant;
    }

    public LocalDate getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(LocalDate agreementDate) {
        this.agreementDate = agreementDate;
    }

    public String getCancellationNoticePeriod() {
        return cancellationNoticePeriod;
    }

    public void setCancellationNoticePeriod(String cancellationNoticePeriod) {
        this.cancellationNoticePeriod = cancellationNoticePeriod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<FloorAllocation> getFloorAllocations() {
        return floorAllocations;
    }

    public void setFloorAllocations(List<FloorAllocation> floorAllocations) {
        this.floorAllocations = floorAllocations;
    }

    public List<AgreementParking> getAgreementParkings() {
        return agreementParkings;
    }

    public void setAgreementParkings(List<AgreementParking> agreementParkings) {
        this.agreementParkings = agreementParkings;
    }

    public List<AgreementDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<AgreementDocument> documents) {
        this.documents = documents;
    }

    public List<AdvanceDeposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<AdvanceDeposit> deposits) {
        this.deposits = deposits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LeaseAgreement that = (LeaseAgreement) o;
        return Objects.equals(id, that.id) && Objects.equals(agreementNumber, that.agreementNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agreementNumber);
    }
}
