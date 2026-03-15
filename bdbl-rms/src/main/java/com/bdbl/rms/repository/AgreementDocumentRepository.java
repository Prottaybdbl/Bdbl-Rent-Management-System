package com.bdbl.rms.repository;

import com.bdbl.rms.entity.AgreementDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementDocumentRepository extends JpaRepository<AgreementDocument, Long> {

    List<AgreementDocument> findByAgreementId(Long agreementId);
}
