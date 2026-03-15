package com.bdbl.rms.repository;

import com.bdbl.rms.entity.RentWaiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentWaiverRepository extends JpaRepository<RentWaiver, Long> {

    List<RentWaiver> findByRentBillTenantId(Long tenantId);

    List<RentWaiver> findByRentBillAgreementId(Long agreementId);

    List<RentWaiver> findByRentBillId(Long rentBillId);

    List<RentWaiver> findByStatus(String status);
}
