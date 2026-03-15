package com.bdbl.rms.dto;

import java.math.BigDecimal;

public class ParkingSpaceDTO {

    private Long id;
    private Long buildingId;
    private String buildingName; // Optional, for display
    private String parkingNumber;
    private String parkingType; // CAR, BIKE, HEAVY_VEHICLE
    private String location; // Basement-1, Ground Floor etc.
    private BigDecimal areaSft;
    private BigDecimal monthlyRent;
    private String status; // VACANT, OCCUPIED, RESERVED

    public ParkingSpaceDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
