package com.bdbl.rms.controller;

import com.bdbl.rms.dto.AdvanceDepositDTO;
import com.bdbl.rms.dto.AgreementDocumentDTO;
import com.bdbl.rms.dto.AgreementParkingDTO;
import com.bdbl.rms.dto.LeaseAgreementDTO;
import com.bdbl.rms.service.AgreementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agreements")
public class AgreementController {

    private final AgreementService agreementService;

    public AgreementController(AgreementService agreementService) {
        this.agreementService = agreementService;
    }

    // ==========================================
    // LEASE AGREEMENT ENDPOINTS
    // ==========================================

    @PostMapping
    public ResponseEntity<LeaseAgreementDTO> createAgreement(@RequestBody LeaseAgreementDTO dto) {
        return new ResponseEntity<>(agreementService.createAgreement(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaseAgreementDTO> updateAgreement(@PathVariable Long id,
            @RequestBody LeaseAgreementDTO dto) {
        return ResponseEntity.ok(agreementService.updateAgreement(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaseAgreementDTO> getAgreement(@PathVariable Long id) {
        return ResponseEntity.ok(agreementService.getAgreementById(id));
    }

    @GetMapping("/number/{agreementNumber}")
    public ResponseEntity<LeaseAgreementDTO> getAgreementByNumber(@PathVariable String agreementNumber) {
        return ResponseEntity.ok(agreementService.getAgreementByNumber(agreementNumber));
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<LeaseAgreementDTO>> getAgreementsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(agreementService.getAgreementsByTenant(tenantId));
    }

    @GetMapping("/building/{buildingId}")
    public ResponseEntity<List<LeaseAgreementDTO>> getAgreementsByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(agreementService.getAgreementsByBuilding(buildingId));
    }

    @GetMapping
    public ResponseEntity<List<LeaseAgreementDTO>> getAllAgreements() {
        return ResponseEntity.ok(agreementService.getAllAgreements());
    }

    // ==========================================
    // AGREEMENT PARKING ENDPOINTS
    // ==========================================

    @PostMapping("/{agreementId}/parking")
    public ResponseEntity<AgreementParkingDTO> addParkingToAgreement(@PathVariable Long agreementId,
            @RequestBody AgreementParkingDTO dto) {
        return new ResponseEntity<>(agreementService.addParkingToAgreement(agreementId, dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{agreementId}/parking/{parkingId}")
    public ResponseEntity<Void> removeParkingFromAgreement(@PathVariable Long agreementId,
            @PathVariable Long parkingId) {
        agreementService.removeParkingFromAgreement(agreementId, parkingId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{agreementId}/parking")
    public ResponseEntity<List<AgreementParkingDTO>> getAgreementParkings(@PathVariable Long agreementId) {
        return ResponseEntity.ok(agreementService.getAgreementParkings(agreementId));
    }

    // ==========================================
    // AGREEMENT DOCUMENT ENDPOINTS
    // ==========================================

    @PostMapping("/{agreementId}/documents")
    public ResponseEntity<AgreementDocumentDTO> addDocument(@PathVariable Long agreementId,
            @RequestBody AgreementDocumentDTO dto) {
        return new ResponseEntity<>(agreementService.addDocument(agreementId, dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> removeDocument(@PathVariable Long documentId) {
        agreementService.removeDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{agreementId}/documents")
    public ResponseEntity<List<AgreementDocumentDTO>> getAgreementDocuments(@PathVariable Long agreementId) {
        return ResponseEntity.ok(agreementService.getAgreementDocuments(agreementId));
    }

    // ==========================================
    // ADVANCE DEPOSIT ENDPOINTS
    // ==========================================

    @PostMapping("/{agreementId}/deposits")
    public ResponseEntity<AdvanceDepositDTO> addDeposit(@PathVariable Long agreementId,
            @RequestBody AdvanceDepositDTO dto) {
        return new ResponseEntity<>(agreementService.addDeposit(agreementId, dto), HttpStatus.CREATED);
    }

    @PutMapping("/deposits/{depositId}")
    public ResponseEntity<AdvanceDepositDTO> updateDeposit(@PathVariable Long depositId,
            @RequestBody AdvanceDepositDTO dto) {
        return ResponseEntity.ok(agreementService.updateDeposit(depositId, dto));
    }

    @GetMapping("/{agreementId}/deposits")
    public ResponseEntity<List<AdvanceDepositDTO>> getDepositsByAgreement(@PathVariable Long agreementId) {
        return ResponseEntity.ok(agreementService.getDepositsByAgreement(agreementId));
    }
}
