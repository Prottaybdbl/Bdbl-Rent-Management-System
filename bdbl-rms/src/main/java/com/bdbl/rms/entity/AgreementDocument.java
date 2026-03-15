package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "agreement_documents")
public class AgreementDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agreement_id", nullable = false)
    private LeaseAgreement agreement;

    @Column(name = "document_type", length = 50)
    private String documentType; // AGREEMENT, ANNEXURE, APPROVAL

    @Column(name = "document_name", length = 255)
    private String documentName;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    public AgreementDocument() {
    }

    public static AgreementDocumentBuilder builder() {
        return new AgreementDocumentBuilder();
    }

    public static class AgreementDocumentBuilder {
        private AgreementDocument ad;

        private AgreementDocumentBuilder() {
            ad = new AgreementDocument();
        }

        public AgreementDocumentBuilder id(Long id) {
            ad.setId(id);
            return this;
        }

        public AgreementDocumentBuilder agreement(LeaseAgreement agreement) {
            ad.setAgreement(agreement);
            return this;
        }

        public AgreementDocumentBuilder documentType(String documentType) {
            ad.setDocumentType(documentType);
            return this;
        }

        public AgreementDocumentBuilder documentName(String documentName) {
            ad.setDocumentName(documentName);
            return this;
        }

        public AgreementDocumentBuilder filePath(String filePath) {
            ad.setFilePath(filePath);
            return this;
        }

        public AgreementDocumentBuilder uploadedBy(User uploadedBy) {
            ad.setUploadedBy(uploadedBy);
            return this;
        }

        public AgreementDocument build() {
            return ad;
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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AgreementDocument that = (AgreementDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
