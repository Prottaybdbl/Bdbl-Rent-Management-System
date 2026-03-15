package com.bdbl.rms.dto;

import java.math.BigDecimal;

public class FloorDTO {

    private Long id;
    private Long buildingId;
    private String buildingName; // Optional, for display
    private String floorNumber;
    private String floorName;
    private BigDecimal totalAreaSft;
    private BigDecimal allocatedAreaSft;
    private BigDecimal commonAreaSft;
    private BigDecimal availableAreaSft; // Calculated field
    private String status;

    public FloorDTO() {
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

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public BigDecimal getTotalAreaSft() {
        return totalAreaSft;
    }

    public void setTotalAreaSft(BigDecimal totalAreaSft) {
        this.totalAreaSft = totalAreaSft;
    }

    public BigDecimal getAllocatedAreaSft() {
        return allocatedAreaSft;
    }

    public void setAllocatedAreaSft(BigDecimal allocatedAreaSft) {
        this.allocatedAreaSft = allocatedAreaSft;
    }

    public BigDecimal getCommonAreaSft() {
        return commonAreaSft;
    }

    public void setCommonAreaSft(BigDecimal commonAreaSft) {
        this.commonAreaSft = commonAreaSft;
    }

    public BigDecimal getAvailableAreaSft() {
        return availableAreaSft;
    }

    public void setAvailableAreaSft(BigDecimal availableAreaSft) {
        this.availableAreaSft = availableAreaSft;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
