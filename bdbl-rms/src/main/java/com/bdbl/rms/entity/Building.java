package com.bdbl.rms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "buildings")
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(unique = true, length = 50)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String district;

    @Column(name = "total_floors")
    private Integer totalFloors;

    @Column(name = "total_area_sft", precision = 12, scale = 2)
    private BigDecimal totalAreaSft;

    @Column(name = "construction_year")
    private Integer constructionYear;

    @Column(name = "parking_config_type", length = 20)
    private String parkingConfigType; // UNIT_WISE or AREA_WISE

    @Column(length = 20)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, UNDER_MAINTENANCE

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // Relationships
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Floor> floors = new ArrayList<>();

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingSpace> parkingSpaces = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Building() {
    }

    public static BuildingBuilder builder() {
        return new BuildingBuilder();
    }

    public static class BuildingBuilder {
        private Building building;

        private BuildingBuilder() {
            building = new Building();
        }

        public BuildingBuilder id(Long id) {
            building.setId(id);
            return this;
        }

        public BuildingBuilder name(String name) {
            building.setName(name);
            return this;
        }

        public BuildingBuilder code(String code) {
            building.setCode(code);
            return this;
        }

        public BuildingBuilder address(String address) {
            building.setAddress(address);
            return this;
        }

        public BuildingBuilder city(String city) {
            building.setCity(city);
            return this;
        }

        public BuildingBuilder district(String district) {
            building.setDistrict(district);
            return this;
        }

        public BuildingBuilder totalFloors(Integer totalFloors) {
            building.setTotalFloors(totalFloors);
            return this;
        }

        public BuildingBuilder totalAreaSft(BigDecimal totalAreaSft) {
            building.setTotalAreaSft(totalAreaSft);
            return this;
        }

        public BuildingBuilder constructionYear(Integer constructionYear) {
            building.setConstructionYear(constructionYear);
            return this;
        }

        public BuildingBuilder parkingConfigType(String parkingConfigType) {
            building.setParkingConfigType(parkingConfigType);
            return this;
        }

        public BuildingBuilder status(String status) {
            building.setStatus(status);
            return this;
        }

        public BuildingBuilder createdBy(User createdBy) {
            building.setCreatedBy(createdBy);
            return this;
        }

        public Building build() {
            return building;
        }
    }

    // Getters and Setters
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public List<ParkingSpace> getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(List<ParkingSpace> parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Building building = (Building) o;
        return Objects.equals(id, building.id) && Objects.equals(code, building.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code);
    }
}
