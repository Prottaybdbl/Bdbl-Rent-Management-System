package com.bdbl.rms.repository;

import com.bdbl.rms.entity.AgreementParking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgreementParkingRepository extends JpaRepository<AgreementParking, Long> {

    List<AgreementParking> findByAgreementId(Long agreementId);

    Optional<AgreementParking> findByAgreementIdAndParkingId(Long agreementId, Long parkingId);

    boolean existsByAgreementIdAndParkingId(Long agreementId, Long parkingId);
}
