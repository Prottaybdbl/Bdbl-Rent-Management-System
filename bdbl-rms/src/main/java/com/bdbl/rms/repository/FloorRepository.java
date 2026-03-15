package com.bdbl.rms.repository;

import com.bdbl.rms.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {

    List<Floor> findByBuildingId(Long buildingId);

    List<Floor> findByBuildingIdOrderByFloorNumberAsc(Long buildingId);

    Optional<Floor> findByBuildingIdAndFloorNumber(Long buildingId, String floorNumber);

    boolean existsByBuildingIdAndFloorNumber(Long buildingId, String floorNumber);

    List<Floor> findByBuildingIdAndStatus(Long buildingId, String status);
}
