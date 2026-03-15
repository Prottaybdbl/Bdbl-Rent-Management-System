package com.bdbl.rms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FloorAllocationDTO {

    private Long id;
    private Long agreementId;
    private String agreementNumber;
    private Long floorId;
    private String floorName;
    private String allocationLabel;
    private BigDecimal areaSft;
    private String rentType;
    private BigDecimal rentPerSft;
    private BigDecimal lumpsumRent;
    private BigDecimal monthlyRent;
    private String status;
    private LocalDateTime createdAt;

    public FloorAllocationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    public String getAgreementNumber() {
        return agreementNumber;
    }

    public void setAgreementNumber(String agreementNumber) {
        this.agreementNumber = agreementNumber;
    }

    public Long getFloorId() {
        return floorId;
    }

    public void setFloorId(Long floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
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
}
