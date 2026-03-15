package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "arrears")
public class Arrear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private LeaseAgreement agreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_bill_id")
    private RentBill sourceBill;

    @Column(name = "financial_year", length = 9)
    private String financialYear;

    @Column(name = "principal_amount", precision = 12, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "penalty_amount", precision = 12, scale = 2)
    private BigDecimal penaltyAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount", precision = 12, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Column(name = "outstanding_amount", precision = 12, scale = 2)
    private BigDecimal outstandingAmount;

    @Column(length = 20)
    private String status = "UNPAID"; // UNPAID, PARTIALLY_PAID, PAID

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateOutstanding();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateOutstanding();
    }

    private void calculateOutstanding() {
        if (principalAmount != null && penaltyAmount != null) {
            totalAmount = principalAmount.add(penaltyAmount);
            outstandingAmount = totalAmount.subtract(paidAmount != null ? paidAmount : BigDecimal.ZERO);
            if (outstandingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                status = "PAID";
            }
        }
    }

    public Arrear() {
    }

    public static ArrearBuilder builder() {
        return new ArrearBuilder();
    }

    public static class ArrearBuilder {
        private Arrear a;

        private ArrearBuilder() {
            a = new Arrear();
        }

        public ArrearBuilder id(Long id) {
            a.setId(id);
            return this;
        }

        public ArrearBuilder tenant(Tenant tenant) {
            a.setTenant(tenant);
            return this;
        }

        public ArrearBuilder agreement(LeaseAgreement agreement) {
            a.setAgreement(agreement);
            return this;
        }

        public ArrearBuilder sourceBill(RentBill sourceBill) {
            a.setSourceBill(sourceBill);
            return this;
        }

        public ArrearBuilder financialYear(String financialYear) {
            a.setFinancialYear(financialYear);
            return this;
        }

        public ArrearBuilder principalAmount(BigDecimal principalAmount) {
            a.setPrincipalAmount(principalAmount);
            return this;
        }

        public ArrearBuilder penaltyAmount(BigDecimal penaltyAmount) {
            a.setPenaltyAmount(penaltyAmount);
            return this;
        }

        public ArrearBuilder totalAmount(BigDecimal totalAmount) {
            a.setTotalAmount(totalAmount);
            return this;
        }

        public ArrearBuilder paidAmount(BigDecimal paidAmount) {
            a.setPaidAmount(paidAmount);
            return this;
        }

        public ArrearBuilder outstandingAmount(BigDecimal outstandingAmount) {
            a.setOutstandingAmount(outstandingAmount);
            return this;
        }

        public ArrearBuilder status(String status) {
            a.setStatus(status);
            return this;
        }

        public Arrear build() {
            return a;
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

    public LeaseAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(LeaseAgreement agreement) {
        this.agreement = agreement;
    }

    public RentBill getSourceBill() {
        return sourceBill;
    }

    public void setSourceBill(RentBill sourceBill) {
        this.sourceBill = sourceBill;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Arrear arrear = (Arrear) o;
        return Objects.equals(id, arrear.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
