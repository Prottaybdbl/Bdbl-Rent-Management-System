package com.bdbl.rms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentDTO {

    private Long id;
    private String paymentNumber;
    private Long tenantId;
    private String tenantCompanyName;
    private Long rentBillId;
    private String rentBillNumber;
    private Long arrearId;
    private Long advanceDepositId;
    private BigDecimal amount;
    private String paymentMethod;
    private String chequeNumber;
    private LocalDate chequeDate;
    private String bankName;
    private String transactionRef;
    private LocalDate paymentDate;
    private String documentPath;
    private String notes;
    private String status;
    private LocalDateTime createdAt;
    private Long createdById;

    public PaymentDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCompanyName() {
        return tenantCompanyName;
    }

    public void setTenantCompanyName(String tenantCompanyName) {
        this.tenantCompanyName = tenantCompanyName;
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

    public Long getArrearId() {
        return arrearId;
    }

    public void setArrearId(Long arrearId) {
        this.arrearId = arrearId;
    }

    public Long getAdvanceDepositId() {
        return advanceDepositId;
    }

    public void setAdvanceDepositId(Long advanceDepositId) {
        this.advanceDepositId = advanceDepositId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public LocalDate getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(LocalDate chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
