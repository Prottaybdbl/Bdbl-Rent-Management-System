package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "system_settings")
public class SystemSetting {

    @Id
    @Column(name = "setting_key", length = 100)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public SystemSetting() {
    }

    public static SystemSettingBuilder builder() {
        return new SystemSettingBuilder();
    }

    public static class SystemSettingBuilder {
        private SystemSetting ss;

        private SystemSettingBuilder() {
            ss = new SystemSetting();
        }

        public SystemSettingBuilder settingKey(String settingKey) {
            ss.setSettingKey(settingKey);
            return this;
        }

        public SystemSettingBuilder settingValue(String settingValue) {
            ss.setSettingValue(settingValue);
            return this;
        }

        public SystemSettingBuilder description(String description) {
            ss.setDescription(description);
            return this;
        }

        public SystemSettingBuilder updatedBy(User updatedBy) {
            ss.setUpdatedBy(updatedBy);
            return this;
        }

        public SystemSetting build() {
            return ss;
        }
    }

    // Getters and Setters
    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SystemSetting that = (SystemSetting) o;
        return Objects.equals(settingKey, that.settingKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(settingKey);
    }
}
