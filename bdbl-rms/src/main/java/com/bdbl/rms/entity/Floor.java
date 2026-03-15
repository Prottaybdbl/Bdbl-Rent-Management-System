package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "floors")
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "floor_number", length = 50)
    private String floorNumber;

    @Column(name = "floor_name", length = 255)
    private String floorName;

    @Column(name = "total_area_sft", precision = 12, scale = 2)
    private BigDecimal totalAreaSft;

    @Column(name = "allocated_area_sft", precision = 12, scale = 2)
    private BigDecimal allocatedAreaSft = BigDecimal.ZERO;

    @Column(name = "common_area_sft", precision = 12, scale = 2)
    private BigDecimal commonAreaSft;

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, UNDER_MAINTENANCE

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (allocatedAreaSft == null)
            allocatedAreaSft = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Custom Transient Method for Available Area
    @Transient
    public BigDecimal getAvailableAreaSft() {
        if (totalAreaSft == null)
            return BigDecimal.ZERO;
        return totalAreaSft.subtract(allocatedAreaSft == null ? BigDecimal.ZERO : allocatedAreaSft);
    }

    public Floor() {
    }

    public static FloorBuilder builder() {
        return new FloorBuilder();
    }

    public static class FloorBuilder {
        private Floor floor;

        private FloorBuilder() {
            floor = new Floor();
        }

        public FloorBuilder id(Long id) {
            floor.setId(id);
            return this;
        }

        public FloorBuilder building(Building building) {
            floor.setBuilding(building);
            return this;
        }

        public FloorBuilder floorNumber(String floorNumber) {
            floor.setFloorNumber(floorNumber);
            return this;
        }

        public FloorBuilder floorName(String floorName) {
            floor.setFloorName(floorName);
            return this;
        }

        public FloorBuilder totalAreaSft(BigDecimal totalAreaSft) {
            floor.setTotalAreaSft(totalAreaSft);
            return this;
        }

        public FloorBuilder allocatedAreaSft(BigDecimal allocatedAreaSft) {
            floor.setAllocatedAreaSft(allocatedAreaSft);
            return this;
        }

        public FloorBuilder commonAreaSft(BigDecimal commonAreaSft) {
            floor.setCommonAreaSft(commonAreaSft);
            return this;
        }

        public FloorBuilder status(String status) {
            floor.setStatus(status);
            return this;
        }

        public Floor build() {
            return floor;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Floor floor = (Floor) o;
        return Objects.equals(id, floor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
