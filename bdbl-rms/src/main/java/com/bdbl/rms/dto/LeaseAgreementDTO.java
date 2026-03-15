package com.bdbl.rms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LeaseAgreementDTO {

    private Long id;
    private String agreementNumber;
    private Long tenantId;
    private String tenantCompanyName; // Display
    private Long buildingId;
    private String buildingName; // Display
    private Long floorId;
    private String floorNumber; // Display
    private BigDecimal agreementAreaSft;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer durationMonths;
    private BigDecimal rentPerSft;
    private BigDecimal totalMonthlyRent;
    private BigDecimal serviceChargeAmount;
    private BigDecimal vatPercentage;
    private BigDecimal taxPercentage;
    private BigDecimal advanceDepositAmount;
    private BigDecimal securityDepositAmount;
    private Integer rentEscalationPercentage;
    private Integer escalationFrequencyMonths;
    private String billingCycle;
    private Integer gracePeriodDays;
    private String signedByBdbl;
    private String signedByTenant;
    private LocalDate agreementDate;
    private String cancellationNoticePeriod;
    private String status;
    private LocalDateTime createdAt;
    private Long createdById;

    public LeaseAgreementDTO() {
    }

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

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCompanyName() {
        return tenantCompanyName;
    }

    public void setTenantCompanyName(String tenantCompanyName) {
        this.tenantCompanyName = tenantCompanyName;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public BigDecimal getAgreementAreaSft() {
        return agreementAreaSft;
    }

    public void setAgreementAreaSft(BigDecimal agreementAreaSft) {
        this.agreementAreaSft = agreementAreaSft;
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

    public BigDecimal getRentPerSft() {
        return rentPerSft;
    }

    public void setRentPerSft(BigDecimal rentPerSft) {
        this.rentPerSft = rentPerSft;
    }

    public BigDecimal getTotalMonthlyRent() {
        return totalMonthlyRent;
    }

    public void setTotalMonthlyRent(BigDecimal totalMonthlyRent) {
        this.totalMonthlyRent = totalMonthlyRent;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
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

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }
}
