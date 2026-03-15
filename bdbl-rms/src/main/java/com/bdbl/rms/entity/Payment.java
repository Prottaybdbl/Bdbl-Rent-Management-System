package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_number", unique = true, length = 100)
    private String paymentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_bill_id")
    private RentBill rentBill;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrear_id")
    private Arrear arrear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advance_deposit_id")
    private AdvanceDeposit advanceDeposit;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_method", length = 30)
    private String paymentMethod; // CASH, CHEQUE, BANK_TRANSFER, PAY_ORDER

    @Column(name = "cheque_number", length = 100)
    private String chequeNumber;

    @Column(name = "cheque_date")
    private LocalDate chequeDate;

    @Column(name = "bank_name", length = 255)
    private String bankName;

    @Column(name = "transaction_ref", length = 255)
    private String transactionRef;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "document_path", length = 500)
    private String documentPath;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 20)
    private String status = "COMPLETED"; // COMPLETED, BOUNCED, CANCELLED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Payment() {
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public static class PaymentBuilder {
        private Payment p;

        private PaymentBuilder() {
            p = new Payment();
        }

        public PaymentBuilder id(Long id) {
            p.setId(id);
            return this;
        }

        public PaymentBuilder paymentNumber(String paymentNumber) {
            p.setPaymentNumber(paymentNumber);
            return this;
        }

        public PaymentBuilder tenant(Tenant tenant) {
            p.setTenant(tenant);
            return this;
        }

        public PaymentBuilder rentBill(RentBill rentBill) {
            p.setRentBill(rentBill);
            return this;
        }

        public PaymentBuilder arrear(Arrear arrear) {
            p.setArrear(arrear);
            return this;
        }

        public PaymentBuilder advanceDeposit(AdvanceDeposit advanceDeposit) {
            p.setAdvanceDeposit(advanceDeposit);
            return this;
        }

        public PaymentBuilder amount(BigDecimal amount) {
            p.setAmount(amount);
            return this;
        }

        public PaymentBuilder paymentMethod(String paymentMethod) {
            p.setPaymentMethod(paymentMethod);
            return this;
        }

        public PaymentBuilder chequeNumber(String chequeNumber) {
            p.setChequeNumber(chequeNumber);
            return this;
        }

        public PaymentBuilder chequeDate(LocalDate chequeDate) {
            p.setChequeDate(chequeDate);
            return this;
        }

        public PaymentBuilder bankName(String bankName) {
            p.setBankName(bankName);
            return this;
        }

        public PaymentBuilder transactionRef(String transactionRef) {
            p.setTransactionRef(transactionRef);
            return this;
        }

        public PaymentBuilder paymentDate(LocalDate paymentDate) {
            p.setPaymentDate(paymentDate);
            return this;
        }

        public PaymentBuilder documentPath(String documentPath) {
            p.setDocumentPath(documentPath);
            return this;
        }

        public PaymentBuilder notes(String notes) {
            p.setNotes(notes);
            return this;
        }

        public PaymentBuilder status(String status) {
            p.setStatus(status);
            return this;
        }

        public PaymentBuilder createdBy(User createdBy) {
            p.setCreatedBy(createdBy);
            return this;
        }

        public Payment build() {
            return p;
        }
    }

    // Getters and Setters
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

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public RentBill getRentBill() {
        return rentBill;
    }

    public void setRentBill(RentBill rentBill) {
        this.rentBill = rentBill;
    }

    public Arrear getArrear() {
        return arrear;
    }

    public void setArrear(Arrear arrear) {
        this.arrear = arrear;
    }

    public AdvanceDeposit getAdvanceDeposit() {
        return advanceDeposit;
    }

    public void setAdvanceDeposit(AdvanceDeposit advanceDeposit) {
        this.advanceDeposit = advanceDeposit;
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
