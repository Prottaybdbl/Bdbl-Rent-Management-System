package com.bdbl.rms.controller;

import com.bdbl.rms.dto.*;
import com.bdbl.rms.service.BillingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    // ==========================================
    // RENT BILL ENDPOINTS
    // ==========================================

    @PostMapping("/rent-bills")
    public ResponseEntity<RentBillDTO> generateRentBill(@Valid @RequestBody RentBillDTO dto) {
        RentBillDTO created = billingService.generateRentBill(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/rent-bills/{id}")
    public ResponseEntity<RentBillDTO> updateRentBill(@PathVariable Long id, @Valid @RequestBody RentBillDTO dto) {
        RentBillDTO updated = billingService.updateRentBill(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/rent-bills/{id}")
    public ResponseEntity<RentBillDTO> getRentBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getRentBillById(id));
    }

    @GetMapping("/rent-bills/number/{billNumber}")
    public ResponseEntity<RentBillDTO> getRentBillByNumber(@PathVariable String billNumber) {
        return ResponseEntity.ok(billingService.getRentBillByNumber(billNumber));
    }

    @GetMapping("/rent-bills/tenant/{tenantId}")
    public ResponseEntity<List<RentBillDTO>> getRentBillsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(billingService.getRentBillsByTenant(tenantId));
    }

    @GetMapping("/rent-bills/agreement/{agreementId}")
    public ResponseEntity<List<RentBillDTO>> getRentBillsByAgreement(@PathVariable Long agreementId) {
        return ResponseEntity.ok(billingService.getRentBillsByAgreement(agreementId));
    }

    // ==========================================
    // PAYMENT ENDPOINTS
    // ==========================================

    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> processPayment(@Valid @RequestBody PaymentDTO dto) {
        PaymentDTO processed = billingService.processPayment(dto);
        return new ResponseEntity<>(processed, HttpStatus.CREATED);
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getPaymentById(id));
    }

    @GetMapping("/payments/tenant/{tenantId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(billingService.getPaymentsByTenant(tenantId));
    }

    @GetMapping("/payments/bill/{billId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByBill(@PathVariable Long billId) {
        return ResponseEntity.ok(billingService.getPaymentsByBill(billId));
    }

    // ==========================================
    // ARREAR ENDPOINTS
    // ==========================================

    @PostMapping("/arrears")
    public ResponseEntity<ArrearDTO> createArrear(@Valid @RequestBody ArrearDTO dto) {
        ArrearDTO created = billingService.createArrear(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/arrears/{id}")
    public ResponseEntity<ArrearDTO> updateArrear(@PathVariable Long id, @Valid @RequestBody ArrearDTO dto) {
        ArrearDTO updated = billingService.updateArrear(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/arrears/tenant/{tenantId}")
    public ResponseEntity<List<ArrearDTO>> getArrearsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(billingService.getArrearsByTenant(tenantId));
    }

    @GetMapping("/arrears/agreement/{agreementId}")
    public ResponseEntity<List<ArrearDTO>> getArrearsByAgreement(@PathVariable Long agreementId) {
        return ResponseEntity.ok(billingService.getArrearsByAgreement(agreementId));
    }

    // ==========================================
    // RENT WAIVER ENDPOINTS
    // ==========================================

    @PostMapping("/waivers")
    public ResponseEntity<RentWaiverDTO> applyWaiver(@Valid @RequestBody RentWaiverDTO dto) {
        RentWaiverDTO applied = billingService.applyWaiver(dto);
        return new ResponseEntity<>(applied, HttpStatus.CREATED);
    }

    @PutMapping("/waivers/{id}/approve")
    public ResponseEntity<RentWaiverDTO> approveWaiver(@PathVariable Long id, @RequestParam Long approvedById) {
        RentWaiverDTO approved = billingService.approveWaiver(id, approvedById);
        return ResponseEntity.ok(approved);
    }

    @GetMapping("/waivers/tenant/{tenantId}")
    public ResponseEntity<List<RentWaiverDTO>> getWaiversByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(billingService.getWaiversByTenant(tenantId));
    }

    // ==========================================
    // UTILITY BILL ENDPOINTS
    // ==========================================

    @PostMapping("/utility-bills")
    public ResponseEntity<UtilityBillDTO> generateUtilityBill(@Valid @RequestBody UtilityBillDTO dto) {
        UtilityBillDTO created = billingService.generateUtilityBill(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/utility-bills/{id}")
    public ResponseEntity<UtilityBillDTO> getUtilityBillById(@PathVariable Long id) {
        return ResponseEntity.ok(billingService.getUtilityBillById(id));
    }

    @GetMapping("/utility-bills/tenant/{tenantId}")
    public ResponseEntity<List<UtilityBillDTO>> getUtilityBillsByTenant(@PathVariable Long tenantId) {
        return ResponseEntity.ok(billingService.getUtilityBillsByTenant(tenantId));
    }
}
