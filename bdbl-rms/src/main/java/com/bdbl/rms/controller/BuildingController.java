package com.bdbl.rms.controller;

import com.bdbl.rms.dto.BuildingDTO;
import com.bdbl.rms.dto.FloorDTO;
import com.bdbl.rms.service.BuildingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    @Autowired
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping
    public String viewBuildings(Model model) {
        model.addAttribute("buildings", buildingService.getAllBuildings());
        model.addAttribute("newBuilding", new BuildingDTO());
        return "buildings";
    }

    @PostMapping("/save")
    public String saveBuilding(@ModelAttribute BuildingDTO buildingDTO, RedirectAttributes redirectAttributes) {
        try {
            buildingService.createBuilding(buildingDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Building created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating building: " + e.getMessage());
        }
        return "redirect:/buildings";
    }

    @GetMapping("/{id}")
    public String viewBuildingDetails(@PathVariable Long id, Model model) {
        BuildingDTO building = buildingService.getBuildingById(id);
        List<FloorDTO> floors = buildingService.getFloorsByBuildingId(id);
        model.addAttribute("building", building);
        model.addAttribute("floors", floors);
        model.addAttribute("newFloor", new FloorDTO());
        return "building-details";
    }

    @PostMapping("/{id}/floors/save")
    public String saveFloor(@PathVariable Long id, @ModelAttribute FloorDTO floorDTO, RedirectAttributes redirectAttributes) {
        try {
            buildingService.addFloor(id, floorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Floor added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding floor: " + e.getMessage());
        }
        return "redirect:/buildings/" + id;
    }
}
