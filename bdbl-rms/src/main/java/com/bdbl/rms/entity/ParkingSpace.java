package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "parking_spaces")
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "parking_number", length = 100)
    private String parkingNumber;

    @Column(name = "parking_type", length = 50)
    private String parkingType; // CAR, BIKE, HEAVY_VEHICLE

    @Column(length = 255)
    private String location; // Basement-1, Ground Floor etc.

    @Column(name = "area_sft", precision = 12, scale = 2)
    private BigDecimal areaSft;

    @Column(name = "monthly_rent", precision = 12, scale = 2)
    private BigDecimal monthlyRent;

    @Column(length = 20)
    private String status = "VACANT"; // VACANT, OCCUPIED, RESERVED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public ParkingSpace() {
    }

    public static ParkingSpaceBuilder builder() {
        return new ParkingSpaceBuilder();
    }

    public static class ParkingSpaceBuilder {
        private ParkingSpace ps;

        private ParkingSpaceBuilder() {
            ps = new ParkingSpace();
        }

        public ParkingSpaceBuilder id(Long id) {
            ps.setId(id);
            return this;
        }

        public ParkingSpaceBuilder building(Building building) {
            ps.setBuilding(building);
            return this;
        }

        public ParkingSpaceBuilder parkingNumber(String parkingNumber) {
            ps.setParkingNumber(parkingNumber);
            return this;
        }

        public ParkingSpaceBuilder parkingType(String parkingType) {
            ps.setParkingType(parkingType);
            return this;
        }

        public ParkingSpaceBuilder location(String location) {
            ps.setLocation(location);
            return this;
        }

        public ParkingSpaceBuilder areaSft(BigDecimal areaSft) {
            ps.setAreaSft(areaSft);
            return this;
        }

        public ParkingSpaceBuilder monthlyRent(BigDecimal monthlyRent) {
            ps.setMonthlyRent(monthlyRent);
            return this;
        }

        public ParkingSpaceBuilder status(String status) {
            ps.setStatus(status);
            return this;
        }

        public ParkingSpace build() {
            return ps;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getParkingNumber() {
        return parkingNumber;
    }

    public void setParkingNumber(String parkingNumber) {
        this.parkingNumber = parkingNumber;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getAreaSft() {
        return areaSft;
    }

    public void setAreaSft(BigDecimal areaSft) {
        this.areaSft = areaSft;
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
        ParkingSpace that = (ParkingSpace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
