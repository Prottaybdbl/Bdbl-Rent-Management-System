package com.bdbl.rms.service;

import com.bdbl.rms.dto.*;
import com.bdbl.rms.entity.*;
import com.bdbl.rms.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceImpl.class);

    private final RentBillRepository rentBillRepository;
    private final PaymentRepository paymentRepository;
    private final ArrearRepository arrearRepository;
    private final RentWaiverRepository rentWaiverRepository;
    private final UtilityBillRepository utilityBillRepository;
    private final TenantRepository tenantRepository;
    private final LeaseAgreementRepository leaseAgreementRepository;
    private final UserRepository userRepository;
    private final BuildingRepository buildingRepository;
    private final AdvanceDepositRepository advanceDepositRepository;

    public BillingServiceImpl(
            RentBillRepository rentBillRepository,
            PaymentRepository paymentRepository,
            ArrearRepository arrearRepository,
            RentWaiverRepository rentWaiverRepository,
            UtilityBillRepository utilityBillRepository,
            TenantRepository tenantRepository,
            LeaseAgreementRepository leaseAgreementRepository,
            UserRepository userRepository,
            BuildingRepository buildingRepository,
            AdvanceDepositRepository advanceDepositRepository) {
        this.rentBillRepository = rentBillRepository;
        this.paymentRepository = paymentRepository;
        this.arrearRepository = arrearRepository;
        this.rentWaiverRepository = rentWaiverRepository;
        this.utilityBillRepository = utilityBillRepository;
        this.tenantRepository = tenantRepository;
        this.leaseAgreementRepository = leaseAgreementRepository;
        this.userRepository = userRepository;
        this.buildingRepository = buildingRepository;
        this.advanceDepositRepository = advanceDepositRepository;
    }

    // ==========================================
    // RENT BILL OPERATIONS
    // ==========================================

    @Override
    public RentBillDTO generateRentBill(RentBillDTO dto) {
        if (rentBillRepository.existsByBillNumber(dto.getBillNumber())) {
            throw new IllegalArgumentException("Bill number already exists: " + dto.getBillNumber());
        }

        Tenant tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found with ID: " + dto.getTenantId()));

        LeaseAgreement agreement = leaseAgreementRepository.findById(dto.getAgreementId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Agreement not found with ID: " + dto.getAgreementId()));

        User generatedBy = null;
        if (dto.getGeneratedById() != null) {
            generatedBy = userRepository.findById(dto.getGeneratedById())
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getGeneratedById()));
        }

        RentBill bill = RentBill.builder()
                .billNumber(dto.getBillNumber())
                .tenant(tenant)
                .agreement(agreement)
                .billingMonthDate(dto.getBillingMonthDate())
                .billingYear(dto.getBillingYear())
                .billingMonth(dto.getBillingMonth())
                .billingDays(dto.getBillingDays() != null ? dto.getBillingDays() : 30)
                .totalDaysInMonth(dto.getTotalDaysInMonth() != null ? dto.getTotalDaysInMonth() : 30)
                .baseRent(dto.getBaseRent())
                .serviceCharge(dto.getServiceCharge() != null ? dto.getServiceCharge() : BigDecimal.ZERO)
                .parkingCharge(dto.getParkingCharge() != null ? dto.getParkingCharge() : BigDecimal.ZERO)
                .penaltyAmount(dto.getPenaltyAmount() != null ? dto.getPenaltyAmount() : BigDecimal.ZERO)
                .taxAdjustment(dto.getTaxAdjustment() != null ? dto.getTaxAdjustment() : BigDecimal.ZERO)
                .waiverAmount(dto.getWaiverAmount() != null ? dto.getWaiverAmount() : BigDecimal.ZERO)
                .totalPayable(dto.getTotalPayable())
                .paidAmount(BigDecimal.ZERO)
                .outstandingAmount(dto.getTotalPayable())
                .dueDate(dto.getDueDate())
                .status("GENERATED")
                .generatedBy(generatedBy)
                .build();

        RentBill savedBill = rentBillRepository.save(bill);
        return mapToRentBillDTO(savedBill);
    }

    @Override
    public RentBillDTO updateRentBill(Long id, RentBillDTO dto) {
        RentBill bill = rentBillRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rent bill not found: " + id));

        bill.setBaseRent(dto.getBaseRent());
        bill.setServiceCharge(dto.getServiceCharge());
        bill.setParkingCharge(dto.getParkingCharge());
        bill.setPenaltyAmount(dto.getPenaltyAmount());
        bill.setTaxAdjustment(dto.getTaxAdjustment());
        bill.setWavierAmount(dto.getWaiverAmount());
        bill.setTotalPayable(dto.getTotalPayable());
        bill.setDueDate(dto.getDueDate());
        bill.setStatus(dto.getStatus());

        // Let entity's PreUpdate calculate outstanding
        RentBill updatedBill = rentBillRepository.save(bill);
        return mapToRentBillDTO(updatedBill);
    }

    @Override
    public RentBillDTO getRentBillById(Long id) {
        return mapToRentBillDTO(rentBillRepository.findById(id).orElseThrow());
    }

    @Override
    public RentBillDTO getRentBillByNumber(String billNumber) {
        return mapToRentBillDTO(rentBillRepository.findByBillNumber(billNumber).orElseThrow());
    }

    @Override
    public List<RentBillDTO> getRentBillsByTenant(Long tenantId) {
        return rentBillRepository.findByTenantId(tenantId).stream().map(this::mapToRentBillDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentBillDTO> getRentBillsByAgreement(Long agreementId) {
        return rentBillRepository.findByAgreementId(agreementId).stream().map(this::mapToRentBillDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RentBillDTO> getAllRentBills() {
        return rentBillRepository.findAll().stream().map(this::mapToRentBillDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void generateMonthlyRentBills(int year, String month) {
        log.info("Generating all rent bills for {}/{}", month, year);
        
        List<LeaseAgreement> activeAgreements = leaseAgreementRepository.findByStatus("ACTIVE");
        
        for (LeaseAgreement agreement : activeAgreements) {
            // Check if bill already exists
            if (rentBillRepository.existsByAgreementIdAndBillingYearAndBillingMonth(agreement.getId(), year, month)) {
                log.warn("Bill already exists for agreement {} and period {}/{}", agreement.getAgreementNumber(), month, year);
                continue;
            }

            RentBill bill = RentBill.builder()
                    .billNumber(String.format("RB-%d-%s-%04d", year, month, agreement.getId()))
                    .tenant(agreement.getTenant())
                    .agreement(agreement)
                    .billingYear(year)
                    .billingMonth(month)
                    .billingDays(30)
                    .totalDaysInMonth(30)
                    .baseRent(agreement.getTotalMonthlyRent()) // Using calculated total rent from agreement
                    .serviceCharge(agreement.getServiceChargeAmount())
                    .totalPayable(agreement.getTotalMonthlyRent().add(agreement.getServiceChargeAmount() != null ? agreement.getServiceChargeAmount() : BigDecimal.ZERO))
                    .paidAmount(BigDecimal.ZERO)
                    .outstandingAmount(agreement.getTotalMonthlyRent().add(agreement.getServiceChargeAmount() != null ? agreement.getServiceChargeAmount() : BigDecimal.ZERO))
                    .dueDate(java.time.LocalDate.of(year, Integer.parseInt(month), 10))
                    .status("UNPAID")
                    .build();
            
            rentBillRepository.save(bill);
        }
    }

    // ==========================================
    // PAYMENT OPERATIONS
    // ==========================================

    @Override
    public PaymentDTO processPayment(PaymentDTO dto) {
        // Existing logic...
        return recordPayment(dto);
    }

    @Override
    public PaymentDTO recordPayment(PaymentDTO dto) {
        log.info("Recording payment of {} for tenant ID: {}", dto.getAmount(), dto.getTenantId());
        
        RentBill bill = rentBillRepository.findById(dto.getRentBillId())
                .orElseThrow(() -> new IllegalArgumentException("Bill not found: " + dto.getRentBillId()));

        String paymentNumber = "PAY-" + System.currentTimeMillis();

        Payment payment = Payment.builder()
                .paymentNumber(paymentNumber)
                .tenant(bill.getTenant())
                .rentBill(bill)
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .transactionRef(dto.getTransactionRef())
                .paymentDate(dto.getPaymentDate())
                .status("COMPLETED")
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update bill status
        bill.setPaidAmount(bill.getPaidAmount().add(dto.getAmount()));
        if (bill.getOutstandingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            bill.setStatus("PAID");
        } else {
            bill.setStatus("PARTIALLY_PAID");
        }
        rentBillRepository.save(bill);

        return mapToPaymentDTO(savedPayment);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        return mapToPaymentDTO(paymentRepository.findById(id).orElseThrow());
    }

    @Override
    public List<PaymentDTO> getPaymentsByTenant(Long tenantId) {
        return paymentRepository.findByTenantId(tenantId).stream().map(this::mapToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getPaymentsByBill(Long billId) {
        return paymentRepository.findByRentBillId(billId).stream().map(this::mapToPaymentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::mapToPaymentDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // ARREAR OPERATIONS
    // ==========================================

    @Override
    public ArrearDTO createArrear(ArrearDTO dto) {
        Tenant tenant = tenantRepository.findById(dto.getTenantId()).orElseThrow();
        LeaseAgreement agreement = leaseAgreementRepository.findById(dto.getAgreementId()).orElseThrow();

        Arrear arrear = Arrear.builder()
                .tenant(tenant)
                .agreement(agreement)
                .financialYear(dto.getFinancialYear())
                .principalAmount(dto.getPrincipalAmount())
                .penaltyAmount(dto.getPenaltyAmount() != null ? dto.getPenaltyAmount() : BigDecimal.ZERO)
                .totalAmount(dto.getTotalAmount())
                .paidAmount(BigDecimal.ZERO)
                .outstandingAmount(dto.getTotalAmount())
                .status("UNPAID")
                .build();

        Arrear savedArrear = arrearRepository.save(arrear);
        return mapToArrearDTO(savedArrear);
    }

    @Override
    public ArrearDTO updateArrear(Long id, ArrearDTO dto) {
        Arrear arrear = arrearRepository.findById(id).orElseThrow();
        arrear.setPrincipalAmount(dto.getPrincipalAmount());
        arrear.setPenaltyAmount(dto.getPenaltyAmount());
        arrear.setTotalAmount(dto.getTotalAmount());
        arrear.setStatus(dto.getStatus());
        Arrear updated = arrearRepository.save(arrear);
        // calculating outstanding is in PreUpdate
        return mapToArrearDTO(updated);
    }

    @Override
    public List<ArrearDTO> getArrearsByTenant(Long tenantId) {
        return arrearRepository.findByTenantId(tenantId).stream().map(this::mapToArrearDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArrearDTO> getArrearsByAgreement(Long agreementId) {
        return arrearRepository.findByAgreementId(agreementId).stream().map(this::mapToArrearDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // RENT WAIVER OPERATIONS
    // ==========================================

    @Override
    public RentWaiverDTO applyWaiver(RentWaiverDTO dto) {
        RentBill bill = rentBillRepository.findById(dto.getRentBillId()).orElseThrow();
        User createdBy = dto.getCreatedById() != null ? userRepository.findById(dto.getCreatedById()).orElse(null)
                : null;

        RentWaiver waiver = RentWaiver.builder()
                .rentBill(bill)
                .waiverAmount(dto.getWaiverAmount())
                .reason(dto.getReason())
                .approvalDocumentPath(dto.getApprovalDocumentPath())
                .status("PENDING")
                .createdBy(createdBy)
                .build();

        return mapToRentWaiverDTO(rentWaiverRepository.save(waiver));
    }

    @Override
    public RentWaiverDTO approveWaiver(Long waiverId, Long approvedById) {
        RentWaiver waiver = rentWaiverRepository.findById(waiverId).orElseThrow();
        User user = userRepository.findById(approvedById).orElseThrow();

        waiver.setStatus("APPROVED");
        waiver.setApprovedBy(user.getUsername());

        // apply waiver to bill
        RentBill bill = waiver.getRentBill();
        bill.setWavierAmount(bill.getWaiverAmount().add(waiver.getWaiverAmount()));
        bill.setTotalPayable(bill.getTotalPayable().subtract(waiver.getWaiverAmount()));
        rentBillRepository.save(bill);

        return mapToRentWaiverDTO(rentWaiverRepository.save(waiver));
    }

    @Override
    public List<RentWaiverDTO> getWaiversByTenant(Long tenantId) {
        // Find bills by tenant, then find waivers for those bills
        // Actually earlier we saw RentWaiverRepository doesn't have findByTenantId out
        // of the box because it links to bill.
        // Wait, RentWaiver Repository had findByTenantId() generated by me! Not
        // standard. Let me change finding logic.
        return rentWaiverRepository.findAll().stream()
                .filter(w -> w.getRentBill().getTenant().getId().equals(tenantId))
                .map(this::mapToRentWaiverDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // UTILITY BILL OPERATIONS
    // ==========================================

    @Override
    public UtilityBillDTO generateUtilityBill(UtilityBillDTO dto) {
        Tenant tenant = tenantRepository.findById(dto.getTenantId()).orElseThrow();
        Building building = buildingRepository.findById(dto.getBuildingId()).orElseThrow();
        User createdBy = dto.getCreatedById() != null ? userRepository.findById(dto.getCreatedById()).orElse(null)
                : null;

        UtilityBill bill = UtilityBill.builder()
                .billNumber(dto.getBillNumber())
                .tenant(tenant)
                .building(building)
                .utilityType(dto.getUtilityType())
                .billingMonthDate(dto.getBillingMonthDate())
                .billingYear(dto.getBillingYear())
                .billingMonth(dto.getBillingMonth())
                .amount(dto.getAmount())
                .dueDate(dto.getDueDate())
                .documentPath(dto.getDocumentPath())
                .status("UNPAID")
                .createdBy(createdBy)
                .build();

        return mapToUtilityBillDTO(utilityBillRepository.save(bill));
    }

    @Override
    public UtilityBillDTO getUtilityBillById(Long id) {
        return mapToUtilityBillDTO(utilityBillRepository.findById(id).orElseThrow());
    }

    @Override
    public List<UtilityBillDTO> getUtilityBillsByTenant(Long tenantId) {
        return utilityBillRepository.findByTenantId(tenantId).stream().map(this::mapToUtilityBillDTO)
                .collect(Collectors.toList());
    }

    // ==========================================
    // MAPPING METHODS
    // ==========================================

    private RentBillDTO mapToRentBillDTO(RentBill entity) {
        RentBillDTO d = new RentBillDTO();
        d.setId(entity.getId());
        d.setBillNumber(entity.getBillNumber());
        d.setAgreementId(entity.getAgreement().getId());
        d.setAgreementNumber(entity.getAgreement().getAgreementNumber());
        d.setTenantId(entity.getTenant().getId());
        d.setTenantCompanyName(entity.getTenant().getCompanyName());
        d.setBillingMonthDate(entity.getBillingMonthDate());
        d.setBillingYear(entity.getBillingYear());
        d.setBillingMonth(entity.getBillingMonth());
        d.setBillingDays(entity.getBillingDays());
        d.setTotalDaysInMonth(entity.getTotalDaysInMonth());
        d.setBaseRent(entity.getBaseRent());
        d.setServiceCharge(entity.getServiceCharge());
        d.setParkingCharge(entity.getParkingCharge());
        d.setPenaltyAmount(entity.getPenaltyAmount());
        d.setTaxAdjustment(entity.getTaxAdjustment());
        d.setWaiverAmount(entity.getWaiverAmount());
        d.setTotalPayable(entity.getTotalPayable());
        d.setPaidAmount(entity.getPaidAmount());
        d.setOutstandingAmount(entity.getOutstandingAmount());
        d.setDueDate(entity.getDueDate());
        d.setStatus(entity.getStatus());
        d.setGeneratedAt(entity.getGeneratedAt());
        if (entity.getGeneratedBy() != null)
            d.setGeneratedById(entity.getGeneratedBy().getId());
        return d;
    }

    private PaymentDTO mapToPaymentDTO(Payment entity) {
        PaymentDTO d = new PaymentDTO();
        d.setId(entity.getId());
        d.setPaymentNumber(entity.getPaymentNumber());
        d.setTenantId(entity.getTenant().getId());
        d.setTenantCompanyName(entity.getTenant().getCompanyName());
        if (entity.getRentBill() != null) {
            d.setRentBillId(entity.getRentBill().getId());
            d.setRentBillNumber(entity.getRentBill().getBillNumber());
        }
        if (entity.getArrear() != null)
            d.setArrearId(entity.getArrear().getId());
        if (entity.getAdvanceDeposit() != null)
            d.setAdvanceDepositId(entity.getAdvanceDeposit().getId());
        d.setAmount(entity.getAmount());
        d.setPaymentMethod(entity.getPaymentMethod());
        d.setChequeNumber(entity.getChequeNumber());
        d.setChequeDate(entity.getChequeDate());
        d.setBankName(entity.getBankName());
        d.setTransactionRef(entity.getTransactionRef());
        d.setPaymentDate(entity.getPaymentDate());
        d.setDocumentPath(entity.getDocumentPath());
        d.setNotes(entity.getNotes());
        d.setStatus(entity.getStatus());
        d.setCreatedAt(entity.getCreatedAt());
        if (entity.getCreatedBy() != null)
            d.setCreatedById(entity.getCreatedBy().getId());
        return d;
    }

    private ArrearDTO mapToArrearDTO(Arrear entity) {
        ArrearDTO d = new ArrearDTO();
        d.setId(entity.getId());
        d.setTenantId(entity.getTenant().getId());
        d.setTenantCompanyName(entity.getTenant().getCompanyName());
        d.setAgreementId(entity.getAgreement().getId());
        d.setAgreementNumber(entity.getAgreement().getAgreementNumber());
        d.setFinancialYear(entity.getFinancialYear());
        d.setPrincipalAmount(entity.getPrincipalAmount());
        d.setPenaltyAmount(entity.getPenaltyAmount());
        d.setTotalAmount(entity.getTotalAmount());
        d.setPaidAmount(entity.getPaidAmount());
        d.setOutstandingAmount(entity.getOutstandingAmount());
        d.setStatus(entity.getStatus());
        d.setCreatedAt(entity.getCreatedAt());
        d.setUpdatedAt(entity.getUpdatedAt());
        return d;
    }

    private RentWaiverDTO mapToRentWaiverDTO(RentWaiver entity) {
        RentWaiverDTO d = new RentWaiverDTO();
        d.setId(entity.getId());
        if (entity.getRentBill() != null) {
            d.setRentBillId(entity.getRentBill().getId());
            d.setRentBillNumber(entity.getRentBill().getBillNumber());
        }
        d.setWaiverAmount(entity.getWaiverAmount());
        d.setReason(entity.getReason());
        d.setApprovalDocumentPath(entity.getApprovalDocumentPath());
        d.setApprovedBy(entity.getApprovedBy());
        d.setStatus(entity.getStatus());
        d.setCreatedAt(entity.getCreatedAt());
        if (entity.getCreatedBy() != null)
            d.setCreatedById(entity.getCreatedBy().getId());
        return d;
    }

    private UtilityBillDTO mapToUtilityBillDTO(UtilityBill entity) {
        UtilityBillDTO d = new UtilityBillDTO();
        d.setId(entity.getId());
        d.setBillNumber(entity.getBillNumber());
        d.setTenantId(entity.getTenant().getId());
        d.setTenantCompanyName(entity.getTenant().getCompanyName());
        d.setBuildingId(entity.getBuilding().getId());
        d.setBuildingName(entity.getBuilding().getName());
        d.setUtilityType(entity.getUtilityType());
        d.setBillingMonthDate(entity.getBillingMonthDate());
        d.setBillingYear(entity.getBillingYear());
        d.setBillingMonth(entity.getBillingMonth());
        d.setAmount(entity.getAmount());
        d.setDueDate(entity.getDueDate());
        d.setDocumentPath(entity.getDocumentPath());
        d.setStatus(entity.getStatus());
        d.setCreatedAt(entity.getCreatedAt());
        if (entity.getCreatedBy() != null)
            d.setCreatedById(entity.getCreatedBy().getId());
        return d;
    }
}
