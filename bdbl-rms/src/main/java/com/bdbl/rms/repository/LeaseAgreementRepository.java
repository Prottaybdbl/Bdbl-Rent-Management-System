package com.bdbl.rms.repository;

import com.bdbl.rms.entity.LeaseAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaseAgreementRepository extends JpaRepository<LeaseAgreement, Long> {

    Optional<LeaseAgreement> findByAgreementNumber(String agreementNumber);

    boolean existsByAgreementNumber(String agreementNumber);

    List<LeaseAgreement> findByTenantId(Long tenantId);

    List<LeaseAgreement> findByBuildingId(Long buildingId);

    List<LeaseAgreement> findByStatus(String status);
}
