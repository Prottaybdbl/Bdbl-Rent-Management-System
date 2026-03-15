package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "advance_deposits")
public class AdvanceDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private LeaseAgreement agreement;

    @Column(name = "deposit_type", length = 30)
    private String depositType; // SECURITY_DEPOSIT, ADVANCE_PAYMENT

    @Column(name = "original_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal originalAmount;

    @Column(name = "remaining_amount", precision = 12, scale = 2)
    private BigDecimal remainingAmount;

    @Column(name = "received_date")
    private LocalDate receivedDate;

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, ADJUSTED, REFUNDED

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (remainingAmount == null)
            remainingAmount = originalAmount;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public AdvanceDeposit() {
    }

    public static AdvanceDepositBuilder builder() {
        return new AdvanceDepositBuilder();
    }

    public static class AdvanceDepositBuilder {
        private AdvanceDeposit ad;

        private AdvanceDepositBuilder() {
            ad = new AdvanceDeposit();
        }

        public AdvanceDepositBuilder id(Long id) {
            ad.setId(id);
            return this;
        }

        public AdvanceDepositBuilder agreement(LeaseAgreement agreement) {
            ad.setAgreement(agreement);
            return this;
        }

        public AdvanceDepositBuilder depositType(String depositType) {
            ad.setDepositType(depositType);
            return this;
        }

        public AdvanceDepositBuilder originalAmount(BigDecimal originalAmount) {
            ad.setOriginalAmount(originalAmount);
            return this;
        }

        public AdvanceDepositBuilder remainingAmount(BigDecimal remainingAmount) {
            ad.setRemainingAmount(remainingAmount);
            return this;
        }

        public AdvanceDepositBuilder receivedDate(LocalDate receivedDate) {
            ad.setReceivedDate(receivedDate);
            return this;
        }

        public AdvanceDepositBuilder status(String status) {
            ad.setStatus(status);
            return this;
        }

        public AdvanceDepositBuilder notes(String notes) {
            ad.setNotes(notes);
            return this;
        }

        public AdvanceDeposit build() {
            return ad;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaseAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(LeaseAgreement agreement) {
        this.agreement = agreement;
    }

    public String getDepositType() {
        return depositType;
    }

    public void setDepositType(String depositType) {
        this.depositType = depositType;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AdvanceDeposit that = (AdvanceDeposit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
