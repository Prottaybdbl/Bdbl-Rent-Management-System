package com.bdbl.rms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RentBillDTO {

    private Long id;
    private String billNumber;
    private Long agreementId;
    private String agreementNumber;
    private Long tenantId;
    private String tenantCompanyName;
    private LocalDate billingMonthDate;
    private Integer billingYear;
    private String billingMonth;
    private Integer billingDays;
    private Integer totalDaysInMonth;
    private BigDecimal baseRent;
    private BigDecimal serviceCharge;
    private BigDecimal parkingCharge;
    private BigDecimal penaltyAmount;
    private BigDecimal taxAdjustment;
    private BigDecimal waiverAmount;
    private BigDecimal totalPayable;
    private BigDecimal paidAmount;
    private BigDecimal outstandingAmount;
    private LocalDate dueDate;
    private String status;
    private LocalDateTime generatedAt;
    private Long generatedById;

    public RentBillDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
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

    public LocalDate getBillingMonthDate() {
        return billingMonthDate;
    }

    public void setBillingMonthDate(LocalDate billingMonthDate) {
        this.billingMonthDate = billingMonthDate;
    }

    public Integer getBillingYear() {
        return billingYear;
    }

    public void setBillingYear(Integer billingYear) {
        this.billingYear = billingYear;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public Integer getBillingDays() {
        return billingDays;
    }

    public void setBillingDays(Integer billingDays) {
        this.billingDays = billingDays;
    }

    public Integer getTotalDaysInMonth() {
        return totalDaysInMonth;
    }

    public void setTotalDaysInMonth(Integer totalDaysInMonth) {
        this.totalDaysInMonth = totalDaysInMonth;
    }

    public BigDecimal getBaseRent() {
        return baseRent;
    }

    public void setBaseRent(BigDecimal baseRent) {
        this.baseRent = baseRent;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getParkingCharge() {
        return parkingCharge;
    }

    public void setParkingCharge(BigDecimal parkingCharge) {
        this.parkingCharge = parkingCharge;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getTaxAdjustment() {
        return taxAdjustment;
    }

    public void setTaxAdjustment(BigDecimal taxAdjustment) {
        this.taxAdjustment = taxAdjustment;
    }

    public BigDecimal getWaiverAmount() {
        return waiverAmount;
    }

    public void setWaiverAmount(BigDecimal waiverAmount) {
        this.waiverAmount = waiverAmount;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    public void setTotalPayable(BigDecimal totalPayable) {
        this.totalPayable = totalPayable;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Long getGeneratedById() {
        return generatedById;
    }

    public void setGeneratedById(Long generatedById) {
        this.generatedById = generatedById;
    }
}
