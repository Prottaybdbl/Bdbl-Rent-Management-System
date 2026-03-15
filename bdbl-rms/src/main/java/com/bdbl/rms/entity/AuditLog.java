package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "action_type", length = 50)
    private String actionType; // CREATE, UPDATE, DELETE, LOGIN

    @Column(name = "table_name", length = 100)
    private String tableName;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "old_values", columnDefinition = "JSON")
    private String oldValues;

    @Column(name = "new_values", columnDefinition = "JSON")
    private String newValues;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "action_time", updatable = false)
    private LocalDateTime actionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        actionTime = LocalDateTime.now();
    }

    public AuditLog() {
    }

    public static AuditLogBuilder builder() {
        return new AuditLogBuilder();
    }

    public static class AuditLogBuilder {
        private AuditLog al;

        private AuditLogBuilder() {
            al = new AuditLog();
        }

        public AuditLogBuilder id(Long id) {
            al.setId(id);
            return this;
        }

        public AuditLogBuilder actionType(String actionType) {
            al.setActionType(actionType);
            return this;
        }

        public AuditLogBuilder tableName(String tableName) {
            al.setTableName(tableName);
            return this;
        }

        public AuditLogBuilder recordId(Long recordId) {
            al.setRecordId(recordId);
            return this;
        }

        public AuditLogBuilder oldValues(String oldValues) {
            al.setOldValues(oldValues);
            return this;
        }

        public AuditLogBuilder newValues(String newValues) {
            al.setNewValues(newValues);
            return this;
        }

        public AuditLogBuilder ipAddress(String ipAddress) {
            al.setIpAddress(ipAddress);
            return this;
        }

        public AuditLogBuilder user(User user) {
            al.setUser(user);
            return this;
        }

        public AuditLog build() {
            return al;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
