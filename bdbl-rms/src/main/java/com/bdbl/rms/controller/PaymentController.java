package com.bdbl.rms.controller;

import com.bdbl.rms.dto.PaymentDTO;
import com.bdbl.rms.service.BillingService;
import com.bdbl.rms.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentController {

    private final BillingService billingService;
    private final DashboardService dashboardService;

    @Autowired
    public PaymentController(BillingService billingService, DashboardService dashboardService) {
        this.billingService = billingService;
        this.dashboardService = dashboardService;
    }

    @GetMapping("/payments")
    public String viewPayments(Model model) {
        model.addAttribute("payments", billingService.getAllPayments());
        model.addAttribute("pendingBills", billingService.getAllRentBills().stream()
                .filter(b -> !"PAID".equals(b.getStatus())).toList());
        model.addAttribute("newPayment", new PaymentDTO());
        return "payments";
    }

    @PostMapping("/payments/save")
    public String savePayment(@ModelAttribute PaymentDTO paymentDTO, RedirectAttributes redirectAttributes) {
        try {
            billingService.recordPayment(paymentDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Payment recorded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error recording payment: " + e.getMessage());
        }
        return "redirect:/payments";
    }

    @GetMapping("/reports")
    public String viewReports(Model model) {
        model.addAttribute("metrics", dashboardService.getDashboardMetrics());
        return "reports";
    }
}
