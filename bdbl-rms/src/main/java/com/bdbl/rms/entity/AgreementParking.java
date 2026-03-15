package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "agreement_parking")
public class AgreementParking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private LeaseAgreement agreement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_id", nullable = false)
    private ParkingSpace parking;

    @Column(name = "monthly_rent", precision = 12, scale = 2)
    private BigDecimal monthlyRent;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public AgreementParking() {
    }

    public static AgreementParkingBuilder builder() {
        return new AgreementParkingBuilder();
    }

    public static class AgreementParkingBuilder {
        private AgreementParking ap;

        private AgreementParkingBuilder() {
            ap = new AgreementParking();
        }

        public AgreementParkingBuilder id(Long id) {
            ap.setId(id);
            return this;
        }

        public AgreementParkingBuilder agreement(LeaseAgreement agreement) {
            ap.setAgreement(agreement);
            return this;
        }

        public AgreementParkingBuilder parking(ParkingSpace parking) {
            ap.setParking(parking);
            return this;
        }

        public AgreementParkingBuilder monthlyRent(BigDecimal monthlyRent) {
            ap.setMonthlyRent(monthlyRent);
            return this;
        }

        public AgreementParking build() {
            return ap;
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

    public ParkingSpace getParking() {
        return parking;
    }

    public void setParking(ParkingSpace parking) {
        this.parking = parking;
    }

    public BigDecimal getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(BigDecimal monthlyRent) {
        this.monthlyRent = monthlyRent;
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
        AgreementParking that = (AgreementParking) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
