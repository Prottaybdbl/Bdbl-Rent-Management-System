package com.bdbl.rms.controller;

import com.bdbl.rms.dto.TenantDTO;
import com.bdbl.rms.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public String viewTenants(Model model) {
        model.addAttribute("tenants", tenantService.getAllTenants());
        model.addAttribute("newTenant", new TenantDTO());
        return "tenants";
    }

    @PostMapping("/save")
    public String saveTenant(@ModelAttribute TenantDTO tenantDTO, RedirectAttributes redirectAttributes) {
        try {
            tenantService.createTenant(tenantDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Tenant registered successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error registering tenant: " + e.getMessage());
        }
        return "redirect:/tenants";
    }
}
