package com.bdbl.rms.repository;

import com.bdbl.rms.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    Optional<Building> findByCode(String code);

    List<Building> findByStatus(String status);

    boolean existsByCode(String code);
}
