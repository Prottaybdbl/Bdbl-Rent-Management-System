package com.bdbl.rms.repository;

import com.bdbl.rms.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    List<ParkingSpace> findByBuildingId(Long buildingId);

    List<ParkingSpace> findByBuildingIdOrderByParkingNumberAsc(Long buildingId);

    Optional<ParkingSpace> findByBuildingIdAndParkingNumber(Long buildingId, String parkingNumber);

    boolean existsByBuildingIdAndParkingNumber(Long buildingId, String parkingNumber);

    List<ParkingSpace> findByBuildingIdAndStatus(Long buildingId, String status);
}
