package com.bdbl.rms.repository;

import com.bdbl.rms.entity.Arrear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrearRepository extends JpaRepository<Arrear, Long> {

    List<Arrear> findByTenantId(Long tenantId);

    List<Arrear> findByAgreementId(Long agreementId);

    List<Arrear> findBySourceBillId(Long sourceBillId);

    List<Arrear> findByStatus(String status);
}
