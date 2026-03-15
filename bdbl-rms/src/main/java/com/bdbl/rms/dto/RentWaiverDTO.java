package com.bdbl.rms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentWaiverDTO {

    private Long id;
    private Long rentBillId;
    private String rentBillNumber;
    private BigDecimal waiverAmount;
    private String reason;
    private String approvalDocumentPath;
    private String approvedBy;
    private String status;
    private LocalDateTime createdAt;
    private Long createdById;

    public RentWaiverDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRentBillId() {
        return rentBillId;
    }

    public void setRentBillId(Long rentBillId) {
        this.rentBillId = rentBillId;
    }

    public String getRentBillNumber() {
        return rentBillNumber;
    }

    public void setRentBillNumber(String rentBillNumber) {
        this.rentBillNumber = rentBillNumber;
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

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }
}
