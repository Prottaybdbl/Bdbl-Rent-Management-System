package com.bdbl.rms.repository;

import com.bdbl.rms.entity.AdvanceDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvanceDepositRepository extends JpaRepository<AdvanceDeposit, Long> {

    List<AdvanceDeposit> findByAgreementId(Long agreementId);

    List<AdvanceDeposit> findByAgreementIdAndDepositType(Long agreementId, String depositType);
}
