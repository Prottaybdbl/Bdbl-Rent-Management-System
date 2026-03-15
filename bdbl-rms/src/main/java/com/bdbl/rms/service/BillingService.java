package com.bdbl.rms.service;

import com.bdbl.rms.dto.*;

import java.util.List;

public interface BillingService {

    // === Rent Bill Operations ===
    RentBillDTO generateRentBill(RentBillDTO dto);

    RentBillDTO updateRentBill(Long id, RentBillDTO dto);

    RentBillDTO getRentBillById(Long id);

    RentBillDTO getRentBillByNumber(String billNumber);

    List<RentBillDTO> getRentBillsByTenant(Long tenantId);

    List<RentBillDTO> getRentBillsByAgreement(Long agreementId);

    List<RentBillDTO> getAllRentBills();

    void generateMonthlyRentBills(int year, String month);

    // === Payment Operations ===
    PaymentDTO processPayment(PaymentDTO dto);

    PaymentDTO recordPayment(PaymentDTO dto);

    PaymentDTO getPaymentById(Long id);

    List<PaymentDTO> getPaymentsByTenant(Long tenantId);

    List<PaymentDTO> getPaymentsByBill(Long billId);

    List<PaymentDTO> getAllPayments();

    // === Arrear Operations ===
    ArrearDTO createArrear(ArrearDTO dto);

    ArrearDTO updateArrear(Long id, ArrearDTO dto);

    List<ArrearDTO> getArrearsByTenant(Long tenantId);

    List<ArrearDTO> getArrearsByAgreement(Long agreementId);

    // === Rent Waiver Operations ===
    RentWaiverDTO applyWaiver(RentWaiverDTO dto);

    RentWaiverDTO approveWaiver(Long waiverId, Long approvedById);

    List<RentWaiverDTO> getWaiversByTenant(Long tenantId);

    // === Utility Bill Operations ===
    UtilityBillDTO generateUtilityBill(UtilityBillDTO dto);

    UtilityBillDTO getUtilityBillById(Long id);

    List<UtilityBillDTO> getUtilityBillsByTenant(Long tenantId);
}
