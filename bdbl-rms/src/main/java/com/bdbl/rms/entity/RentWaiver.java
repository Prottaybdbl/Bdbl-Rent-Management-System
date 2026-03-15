package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "rent_waivers")
public class RentWaiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_bill_id", nullable = false)
    private RentBill rentBill;

    @Column(name = "waiver_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal waiverAmount;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(name = "approval_document_path", length = 500)
    private String approvalDocumentPath; // Mandatory for waiver

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(length = 20)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public RentWaiver() {
    }

    public static RentWaiverBuilder builder() {
        return new RentWaiverBuilder();
    }

    public static class RentWaiverBuilder {
        private RentWaiver rw;

        private RentWaiverBuilder() {
            rw = new RentWaiver();
        }

        public RentWaiverBuilder id(Long id) {
            rw.setId(id);
            return this;
        }

        public RentWaiverBuilder rentBill(RentBill rentBill) {
            rw.setRentBill(rentBill);
            return this;
        }

        public RentWaiverBuilder waiverAmount(BigDecimal waiverAmount) {
            rw.setWaiverAmount(waiverAmount);
            return this;
        }

        public RentWaiverBuilder reason(String reason) {
            rw.setReason(reason);
            return this;
        }

        public RentWaiverBuilder approvalDocumentPath(String approvalDocumentPath) {
            rw.setApprovalDocumentPath(approvalDocumentPath);
            return this;
        }

        public RentWaiverBuilder approvedBy(String approvedBy) {
            rw.setApprovedBy(approvedBy);
            return this;
        }

        public RentWaiverBuilder status(String status) {
            rw.setStatus(status);
            return this;
        }

        public RentWaiverBuilder createdBy(User createdBy) {
            rw.setCreatedBy(createdBy);
            return this;
        }

        public RentWaiver build() {
            return rw;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RentBill getRentBill() {
        return rentBill;
    }

    public void setRentBill(RentBill rentBill) {
        this.rentBill = rentBill;
    }

    public BigDecimal getWaiverAmount() {
        return waiverAmount;
    }

    public void setWaiverAmount(BigDecimal waiverAmount) {
        this.waiverAmount = waiverAmount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getApprovalDocumentPath() {
        return approvalDocumentPath;
    }

    public void setApprovalDocumentPath(String approvalDocumentPath) {
        this.approvalDocumentPath = approvalDocumentPath;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
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
        RentWaiver that = (RentWaiver) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
