package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "utility_bills")
public class UtilityBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_number", unique = true, length = 100)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "utility_type", length = 30)
    private String utilityType; // ELECTRICITY, WATER, GAS, OTHERS

    @Column(name = "billing_month_date", nullable = false)
    private LocalDate billingMonthDate;

    @Column(name = "billing_year", nullable = false)
    private Integer billingYear;

    @Column(name = "billing_month", nullable = false, length = 20)
    private String billingMonth;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "document_path", length = 500)
    private String documentPath; // Original bill copy

    @Column(length = 20)
    private String status = "UNPAID"; // UNPAID, PAID

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UtilityBill() {
    }

    public static UtilityBillBuilder builder() {
        return new UtilityBillBuilder();
    }

    public static class UtilityBillBuilder {
        private UtilityBill ub;

        private UtilityBillBuilder() {
            ub = new UtilityBill();
        }

        public UtilityBillBuilder id(Long id) {
            ub.setId(id);
            return this;
        }

        public UtilityBillBuilder billNumber(String billNumber) {
            ub.setBillNumber(billNumber);
            return this;
        }

        public UtilityBillBuilder tenant(Tenant tenant) {
            ub.setTenant(tenant);
            return this;
        }

        public UtilityBillBuilder building(Building building) {
            ub.setBuilding(building);
            return this;
        }

        public UtilityBillBuilder utilityType(String utilityType) {
            ub.setUtilityType(utilityType);
            return this;
        }

        public UtilityBillBuilder billingMonthDate(LocalDate billingMonthDate) {
            ub.setBillingMonthDate(billingMonthDate);
            return this;
        }

        public UtilityBillBuilder billingYear(Integer billingYear) {
            ub.setBillingYear(billingYear);
            return this;
        }

        public UtilityBillBuilder billingMonth(String billingMonth) {
            ub.setBillingMonth(billingMonth);
            return this;
        }

        public UtilityBillBuilder amount(BigDecimal amount) {
            ub.setAmount(amount);
            return this;
        }

        public UtilityBillBuilder dueDate(LocalDate dueDate) {
            ub.setDueDate(dueDate);
            return this;
        }

        public UtilityBillBuilder documentPath(String documentPath) {
            ub.setDocumentPath(documentPath);
            return this;
        }

        public UtilityBillBuilder status(String status) {
            ub.setStatus(status);
            return this;
        }

        public UtilityBillBuilder createdBy(User createdBy) {
            ub.setCreatedBy(createdBy);
            return this;
        }

        public UtilityBill build() {
            return ub;
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

    public String getUtilityType() {
        return utilityType;
    }

    public void setUtilityType(String utilityType) {
        this.utilityType = utilityType;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UtilityBill that = (UtilityBill) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
