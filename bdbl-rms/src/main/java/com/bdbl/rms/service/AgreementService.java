package com.bdbl.rms.service;

import com.bdbl.rms.dto.AdvanceDepositDTO;
import com.bdbl.rms.dto.AgreementDocumentDTO;
import com.bdbl.rms.dto.AgreementParkingDTO;
import com.bdbl.rms.dto.LeaseAgreementDTO;

import java.util.List;

public interface AgreementService {

    // === Lease Agreement Operations ===
    LeaseAgreementDTO createAgreement(LeaseAgreementDTO dto);

    LeaseAgreementDTO updateAgreement(Long id, LeaseAgreementDTO dto);

    LeaseAgreementDTO getAgreementById(Long id);

    LeaseAgreementDTO getAgreementByNumber(String agreementNumber);

    List<LeaseAgreementDTO> getAgreementsByTenant(Long tenantId);

    List<LeaseAgreementDTO> getAgreementsByBuilding(Long buildingId);

    List<LeaseAgreementDTO> getAllAgreements();

    // === Agreement Parking Operations ===
    AgreementParkingDTO addParkingToAgreement(Long agreementId, AgreementParkingDTO dto);

    void removeParkingFromAgreement(Long agreementId, Long parkingId);

    List<AgreementParkingDTO> getAgreementParkings(Long agreementId);

    // === Agreement Document Operations ===
    AgreementDocumentDTO addDocument(Long agreementId, AgreementDocumentDTO dto);

    void removeDocument(Long documentId);

    List<AgreementDocumentDTO> getAgreementDocuments(Long agreementId);

    // === Advance Deposit Operations ===
    AdvanceDepositDTO addDeposit(Long agreementId, AdvanceDepositDTO dto);

    AdvanceDepositDTO updateDeposit(Long depositId, AdvanceDepositDTO dto);

    List<AdvanceDepositDTO> getDepositsByAgreement(Long agreementId);
}
