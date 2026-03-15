package com.bdbl.rms.service;

import com.bdbl.rms.entity.Arrear;
import com.bdbl.rms.entity.Building;
import com.bdbl.rms.entity.LeaseAgreement;
import com.bdbl.rms.entity.RentBill;
import com.bdbl.rms.repository.ArrearRepository;
import com.bdbl.rms.repository.BuildingRepository;
import com.bdbl.rms.repository.LeaseAgreementRepository;
import com.bdbl.rms.repository.RentBillRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final RentBillRepository rentBillRepository;
    private final BuildingRepository buildingRepository;
    private final LeaseAgreementRepository leaseAgreementRepository;
    private final ArrearRepository arrearRepository;

    public DashboardService(RentBillRepository rentBillRepository,
            BuildingRepository buildingRepository,
            LeaseAgreementRepository leaseAgreementRepository,
            ArrearRepository arrearRepository) {
        this.rentBillRepository = rentBillRepository;
        this.buildingRepository = buildingRepository;
        this.leaseAgreementRepository = leaseAgreementRepository;
        this.arrearRepository = arrearRepository;
    }

    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // 1. Total Monthly Revenue (Current Month)
        LocalDate now = LocalDate.now();
        String currentMonthStr = String.format("%02d", now.getMonthValue());
        List<RentBill> currentBills = rentBillRepository.findByBillingYearAndBillingMonth(now.getYear(),
                currentMonthStr);
        BigDecimal totalRevenue = currentBills.stream()
                .map(RentBill::getTotalPayable)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        metrics.put("totalMonthlyRevenue", totalRevenue);

        // 2. Occupancy Rate
        List<Building> allBuildings = buildingRepository.findAll();
        BigDecimal totalCapacity = allBuildings.stream()
                .map(Building::getTotalAreaSft)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<LeaseAgreement> activeAgreements = leaseAgreementRepository.findByStatus("ACTIVE");
        BigDecimal totalOccupied = activeAgreements.stream()
                .map(LeaseAgreement::getAgreementAreaSft)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal occupancyRate = BigDecimal.ZERO;
        if (totalCapacity.compareTo(BigDecimal.ZERO) > 0) {
            occupancyRate = totalOccupied.multiply(new BigDecimal("100"))
                    .divide(totalCapacity, 1, RoundingMode.HALF_UP);
        }
        metrics.put("occupancyRate", occupancyRate);

        // 3. Outstanding Arrears
        List<Arrear> arrears = arrearRepository.findAll();
        BigDecimal totalArrears = arrears.stream()
                .filter(a -> !"PAID".equals(a.getStatus()))
                .map(Arrear::getOutstandingAmount)
                .filter(val -> val != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        metrics.put("outstandingArrears", totalArrears);

        // 4. Getting Some recent expiring agreements
        List<LeaseAgreement> expiringAgreements = activeAgreements.stream()
                .filter(a -> a.getEndDate() != null && a.getEndDate().isBefore(now.plusMonths(3)))
                .sorted((a1, a2) -> a1.getEndDate().compareTo(a2.getEndDate()))
                .limit(5)
                .collect(Collectors.toList());
        metrics.put("expiringAgreements", expiringAgreements);

        return metrics;
    }
}
