package com.bdbl.rms.controller;

import com.bdbl.rms.service.BillingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    @Autowired
    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public String viewBilling(Model model) {
        model.addAttribute("bills", billingService.getAllRentBills());
        model.addAttribute("currentMonth", LocalDate.now().getMonthValue());
        model.addAttribute("currentYear", LocalDate.now().getYear());
        return "billing";
    }

    @PostMapping("/generate")
    public String generateBills(@RequestParam int year, @RequestParam String month, RedirectAttributes redirectAttributes) {
        try {
            billingService.generateMonthlyRentBills(year, month);
            redirectAttributes.addFlashAttribute("successMessage", "Rent bills generated successfully for " + month + "/" + year);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error generating bills: " + e.getMessage());
        }
        return "redirect:/billing";
    }
}
