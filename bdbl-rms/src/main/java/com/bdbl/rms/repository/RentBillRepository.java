package com.bdbl.rms.repository;

import com.bdbl.rms.entity.RentBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RentBillRepository extends JpaRepository<RentBill, Long> {

    Optional<RentBill> findByBillNumber(String billNumber);

    List<RentBill> findByTenantId(Long tenantId);

    List<RentBill> findByAgreementId(Long agreementId);

    List<RentBill> findByBillingYearAndBillingMonth(Integer billingYear, String billingMonth);

    boolean existsByBillNumber(String billNumber);

    List<RentBill> findByStatus(String status);
}
