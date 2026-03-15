package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rent_bills")
public class RentBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_number", unique = true, length = 100)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private LeaseAgreement agreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name = "billing_month_date", nullable = false)
    private LocalDate billingMonthDate;

    @Column(name = "billing_year", nullable = false)
    private Integer billingYear;

    @Column(name = "billing_month", nullable = false, length = 20)
    private String billingMonth;

    @Column(name = "billing_days")
    private Integer billingDays = 30;

    @Column(name = "total_days_in_month")
    private Integer totalDaysInMonth = 30;

    @Column(name = "base_rent", precision = 12, scale = 2)
    private BigDecimal baseRent;

    @Column(name = "service_charge", precision = 12, scale = 2)
    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @Column(name = "parking_charge", precision = 12, scale = 2)
    private BigDecimal parkingCharge = BigDecimal.ZERO;

    @Column(name = "penalty_amount", precision = 12, scale = 2)
    private BigDecimal penaltyAmount = BigDecimal.ZERO;

    @Column(name = "tax_adjustment", precision = 12, scale = 2)
    private BigDecimal taxAdjustment = BigDecimal.ZERO;

    @Column(name = "waiver_amount", precision = 12, scale = 2)
    private BigDecimal waiverAmount = BigDecimal.ZERO;

    @Column(name = "total_payable", precision = 12, scale = 2)
    private BigDecimal totalPayable;

    @Column(name = "paid_amount", precision = 12, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "outstanding_amount", precision = 12, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(length = 20)
    private String status = "GENERATED"; // GENERATED, PARTIALLY_PAID, PAID, OVERDUE

    @Column(name = "generated_at", updatable = false)
    private LocalDateTime generatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private User generatedBy;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        calculateOutstanding();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateOutstanding();
    }

    private void calculateOutstanding() {
        if (totalPayable != null) {
            outstandingAmount = totalPayable.subtract(paidAmount != null ? paidAmount : BigDecimal.ZERO);
            if (outstandingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                status = "PAID";
            }
        }
    }

    public RentBill() {
    }

    public static RentBillBuilder builder() {
        return new RentBillBuilder();
    }

    public static class RentBillBuilder {
        private RentBill rb;

        private RentBillBuilder() {
            rb = new RentBill();
        }

        public RentBillBuilder id(Long id) {
            rb.setId(id);
            return this;
        }

        public RentBillBuilder billNumber(String billNumber) {
            rb.setBillNumber(billNumber);
            return this;
        }

        public RentBillBuilder agreement(LeaseAgreement agreement) {
            rb.setAgreement(agreement);
            return this;
        }

        public RentBillBuilder tenant(Tenant tenant) {
            rb.setTenant(tenant);
            return this;
        }

        public RentBillBuilder billingMonthDate(LocalDate billingMonthDate) {
            rb.setBillingMonthDate(billingMonthDate);
            return this;
        }

        public RentBillBuilder billingYear(Integer billingYear) {
            rb.setBillingYear(billingYear);
            return this;
        }

        public RentBillBuilder billingMonth(String billingMonth) {
            rb.setBillingMonth(billingMonth);
            return this;
        }

        public RentBillBuilder billingDays(Integer billingDays) {
            rb.setBillingDays(billingDays);
            return this;
        }

        public RentBillBuilder totalDaysInMonth(Integer totalDaysInMonth) {
            rb.setTotalDaysInMonth(totalDaysInMonth);
            return this;
        }

        public RentBillBuilder baseRent(BigDecimal baseRent) {
            rb.setBaseRent(baseRent);
            return this;
        }

        public RentBillBuilder serviceCharge(BigDecimal serviceCharge) {
            rb.setServiceCharge(serviceCharge);
            return this;
        }

        public RentBillBuilder parkingCharge(BigDecimal parkingCharge) {
            rb.setParkingCharge(parkingCharge);
            return this;
        }

        public RentBillBuilder penaltyAmount(BigDecimal penaltyAmount) {
            rb.setPenaltyAmount(penaltyAmount);
            return this;
        }

        public RentBillBuilder taxAdjustment(BigDecimal taxAdjustment) {
            rb.setTaxAdjustment(taxAdjustment);
            return this;
        }

        public RentBillBuilder waiverAmount(BigDecimal waiverAmount) {
            rb.setWavierAmount(waiverAmount);
            return this;
        }

        public RentBillBuilder totalPayable(BigDecimal totalPayable) {
            rb.setTotalPayable(totalPayable);
            return this;
        }

        public RentBillBuilder paidAmount(BigDecimal paidAmount) {
            rb.setPaidAmount(paidAmount);
            return this;
        }

        public RentBillBuilder outstandingAmount(BigDecimal outstandingAmount) {
            rb.setOutstandingAmount(outstandingAmount);
            return this;
        }

        public RentBillBuilder dueDate(LocalDate dueDate) {
            rb.setDueDate(dueDate);
            return this;
        }

        public RentBillBuilder status(String status) {
            rb.setStatus(status);
            return this;
        }

        public RentBillBuilder generatedBy(User generatedBy) {
            rb.setGeneratedBy(generatedBy);
            return this;
        }

        public RentBill build() {
            return rb;
        }
    }

    // Getters and Setters
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

    public LeaseAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(LeaseAgreement agreement) {
        this.agreement = agreement;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
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

    public void setWavierAmount(BigDecimal waiverAmount) {
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

    public User getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(User generatedBy) {
        this.generatedBy = generatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RentBill rentBill = (RentBill) o;
        return Objects.equals(id, rentBill.id) && Objects.equals(billNumber, rentBill.billNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, billNumber);
    }
}
