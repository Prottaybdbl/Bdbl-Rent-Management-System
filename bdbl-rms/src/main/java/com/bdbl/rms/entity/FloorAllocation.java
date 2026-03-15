package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "floor_allocations")
public class FloorAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private LeaseAgreement agreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(name = "allocation_label", length = 255)
    private String allocationLabel;

    @Column(name = "area_sft", precision = 12, scale = 2)
    private BigDecimal areaSft;

    @Column(name = "rent_type", length = 20)
    private String rentType; // SQFT_BASED, LUMPSUM

    @Column(name = "rent_per_sft", precision = 10, scale = 2)
    private BigDecimal rentPerSft;

    @Column(name = "lumpsum_rent", precision = 12, scale = 2)
    private BigDecimal lumpsumRent;

    @Column(name = "monthly_rent", precision = 12, scale = 2)
    private BigDecimal monthlyRent;

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, TERMINATED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        calculateMonthlyRent();
    }

    @PreUpdate
    protected void onUpdate() {
        calculateMonthlyRent();
    }

    private void calculateMonthlyRent() {
        if ("SQFT_BASED".equals(rentType) && areaSft != null && rentPerSft != null) {
            monthlyRent = areaSft.multiply(rentPerSft);
        } else if ("LUMPSUM".equals(rentType)) {
            monthlyRent = lumpsumRent;
        }
    }

    public FloorAllocation() {
    }

    public static FloorAllocationBuilder builder() {
        return new FloorAllocationBuilder();
    }

    public static class FloorAllocationBuilder {
        private FloorAllocation fa;

        private FloorAllocationBuilder() {
            fa = new FloorAllocation();
        }

        public FloorAllocationBuilder id(Long id) {
            fa.setId(id);
            return this;
        }

        public FloorAllocationBuilder agreement(LeaseAgreement agreement) {
            fa.setAgreement(agreement);
            return this;
        }

        public FloorAllocationBuilder floor(Floor floor) {
            fa.setFloor(floor);
            return this;
        }

        public FloorAllocationBuilder allocationLabel(String allocationLabel) {
            fa.setAllocationLabel(allocationLabel);
            return this;
        }

        public FloorAllocationBuilder areaSft(BigDecimal areaSft) {
            fa.setAreaSft(areaSft);
            return this;
        }

        public FloorAllocationBuilder rentType(String rentType) {
            fa.setRentType(rentType);
            return this;
        }

        public FloorAllocationBuilder rentPerSft(BigDecimal rentPerSft) {
            fa.setRentPerSft(rentPerSft);
            return this;
        }

        public FloorAllocationBuilder lumpsumRent(BigDecimal lumpsumRent) {
            fa.setLumpsumRent(lumpsumRent);
            return this;
        }

        public FloorAllocationBuilder monthlyRent(BigDecimal monthlyRent) {
            fa.setMonthlyRent(monthlyRent);
            return this;
        }

        public FloorAllocationBuilder status(String status) {
            fa.setStatus(status);
            return this;
        }

        public FloorAllocation build() {
            return fa;
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

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public String getAllocationLabel() {
        return allocationLabel;
    }

    public void setAllocationLabel(String allocationLabel) {
        this.allocationLabel = allocationLabel;
    }

    public BigDecimal getAreaSft() {
        return areaSft;
    }

    public void setAreaSft(BigDecimal areaSft) {
        this.areaSft = areaSft;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public BigDecimal getRentPerSft() {
        return rentPerSft;
    }

    public void setRentPerSft(BigDecimal rentPerSft) {
        this.rentPerSft = rentPerSft;
    }

    public BigDecimal getLumpsumRent() {
        return lumpsumRent;
    }

    public void setLumpsumRent(BigDecimal lumpsumRent) {
        this.lumpsumRent = lumpsumRent;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FloorAllocation that = (FloorAllocation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
