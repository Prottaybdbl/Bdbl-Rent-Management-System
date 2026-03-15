package com.bdbl.rms.repository;

import com.bdbl.rms.entity.FloorAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FloorAllocationRepository extends JpaRepository<FloorAllocation, Long> {

    List<FloorAllocation> findByAgreementTenantId(Long tenantId);

    List<FloorAllocation> findByFloorId(Long floorId);

    List<FloorAllocation> findByAgreementId(Long agreementId);

    List<FloorAllocation> findByStatus(String status);
}
