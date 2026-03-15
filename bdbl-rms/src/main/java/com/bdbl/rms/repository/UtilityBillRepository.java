package com.bdbl.rms.repository;

import com.bdbl.rms.entity.UtilityBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilityBillRepository extends JpaRepository<UtilityBill, Long> {

    Optional<UtilityBill> findByBillNumber(String billNumber);

    List<UtilityBill> findByTenantId(Long tenantId);

    List<UtilityBill> findByBuildingId(Long buildingId);

    List<UtilityBill> findByBillingYearAndBillingMonth(Integer billingYear, String billingMonth);

    boolean existsByBillNumber(String billNumber);
}
