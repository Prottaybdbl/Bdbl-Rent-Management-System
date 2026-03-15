package com.bdbl.rms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BuildingDTO {

    private Long id;
    private String name;
    private String code;
    private String address;
    private String city;
    private String district;
    private Integer totalFloors;
    private BigDecimal totalAreaSft;
    private Integer constructionYear;
    private String parkingConfigType;
    private String status;
    private LocalDateTime createdAt;

    // Aggregated properties for list views
    private Integer activeFloorsCount;
    private Integer activeParkingSpacesCount;
    private BigDecimal totalAllocatedAreaSft;
    private BigDecimal totalAvailableAreaSft;

    public BuildingDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getTotalFloors() {
        return totalFloors;
    }

    public void setTotalFloors(Integer totalFloors) {
        this.totalFloors = totalFloors;
    }

    public BigDecimal getTotalAreaSft() {
        return totalAreaSft;
    }

    public void setTotalAreaSft(BigDecimal totalAreaSft) {
        this.totalAreaSft = totalAreaSft;
    }

    public Integer getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(Integer constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getParkingConfigType() {
        return parkingConfigType;
    }

    public void setParkingConfigType(String parkingConfigType) {
        this.parkingConfigType = parkingConfigType;
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

    public Integer getActiveFloorsCount() {
        return activeFloorsCount;
    }

    public void setActiveFloorsCount(Integer activeFloorsCount) {
        this.activeFloorsCount = activeFloorsCount;
    }

    public Integer getActiveParkingSpacesCount() {
        return activeParkingSpacesCount;
    }

    public void setActiveParkingSpacesCount(Integer activeParkingSpacesCount) {
        this.activeParkingSpacesCount = activeParkingSpacesCount;
    }

    public BigDecimal getTotalAllocatedAreaSft() {
        return totalAllocatedAreaSft;
    }

    public void setTotalAllocatedAreaSft(BigDecimal totalAllocatedAreaSft) {
        this.totalAllocatedAreaSft = totalAllocatedAreaSft;
    }

    public BigDecimal getTotalAvailableAreaSft() {
        return totalAvailableAreaSft;
    }

    public void setTotalAvailableAreaSft(BigDecimal totalAvailableAreaSft) {
        this.totalAvailableAreaSft = totalAvailableAreaSft;
    }
}
