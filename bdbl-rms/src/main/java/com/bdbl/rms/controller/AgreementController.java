package com.bdbl.rms.controller;

import com.bdbl.rms.dto.BuildingDTO;
import com.bdbl.rms.dto.FloorDTO;
import com.bdbl.rms.dto.LeaseAgreementDTO;
import com.bdbl.rms.dto.TenantDTO;
import com.bdbl.rms.service.AgreementService;
import com.bdbl.rms.service.BuildingService;
import com.bdbl.rms.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/agreements")
public class AgreementController {

    private final AgreementService agreementService;
    private final BuildingService buildingService;
    private final TenantService tenantService;

    @Autowired
    public AgreementController(AgreementService agreementService, BuildingService buildingService, TenantService tenantService) {
        this.agreementService = agreementService;
        this.buildingService = buildingService;
        this.tenantService = tenantService;
    }

    @GetMapping
    public String viewAgreements(Model model) {
        model.addAttribute("agreements", agreementService.getAllAgreements());
        return "agreements";
    }

    @GetMapping("/new")
    public String createAgreement(Model model) {
        model.addAttribute("agreement", new LeaseAgreementDTO());
        model.addAttribute("tenants", tenantService.getAllTenants());
        model.addAttribute("buildings", buildingService.getAllBuildings());
        return "agreement-form";
    }

    @PostMapping("/save")
    public String saveAgreement(@ModelAttribute LeaseAgreementDTO agreementDTO, RedirectAttributes redirectAttributes) {
        try {
            agreementService.createAgreement(agreementDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Lease agreement created successfully!");
            return "redirect:/agreements";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating agreement: " + e.getMessage());
            return "redirect:/agreements/new";
        }
    }

    @GetMapping("/api/buildings/{id}/floors")
    @ResponseBody
    public List<FloorDTO> getFloors(@PathVariable Long id) {
        return buildingService.getFloorsByBuildingId(id);
    }
}
