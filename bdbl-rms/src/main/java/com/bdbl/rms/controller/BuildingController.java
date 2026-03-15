package com.bdbl.rms.controller;

import com.bdbl.rms.dto.BuildingDTO;
import com.bdbl.rms.dto.FloorDTO;
import com.bdbl.rms.dto.ParkingSpaceDTO;
import com.bdbl.rms.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    // ==========================================
    // BUILDING ENDPOINTS
    // ==========================================

    @PostMapping
    public ResponseEntity<BuildingDTO> createBuilding(@RequestBody BuildingDTO buildingDTO) {
        return new ResponseEntity<>(buildingService.createBuilding(buildingDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BuildingDTO> updateBuilding(@PathVariable Long id, @RequestBody BuildingDTO buildingDTO) {
        return ResponseEntity.ok(buildingService.updateBuilding(id, buildingDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingDTO> getBuilding(@PathVariable Long id) {
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BuildingDTO>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // FLOOR ENDPOINTS
    // ==========================================

    @PostMapping("/{buildingId}/floors")
    public ResponseEntity<FloorDTO> addFloor(@PathVariable Long buildingId, @RequestBody FloorDTO floorDTO) {
        return new ResponseEntity<>(buildingService.addFloor(buildingId, floorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/floors/{floorId}")
    public ResponseEntity<FloorDTO> updateFloor(@PathVariable Long floorId, @RequestBody FloorDTO floorDTO) {
        return ResponseEntity.ok(buildingService.updateFloor(floorId, floorDTO));
    }

    @GetMapping("/{buildingId}/floors")
    public ResponseEntity<List<FloorDTO>> getFloorsByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(buildingService.getFloorsByBuildingId(buildingId));
    }

    @DeleteMapping("/floors/{floorId}")
    public ResponseEntity<Void> deleteFloor(@PathVariable Long floorId) {
        buildingService.deleteFloor(floorId);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // PARKING SPACE ENDPOINTS
    // ==========================================

    @PostMapping("/{buildingId}/parking")
    public ResponseEntity<ParkingSpaceDTO> addParkingSpace(@PathVariable Long buildingId,
            @RequestBody ParkingSpaceDTO parkingDTO) {
        return new ResponseEntity<>(buildingService.addParkingSpace(buildingId, parkingDTO), HttpStatus.CREATED);
    }

    @PutMapping("/parking/{parkingId}")
    public ResponseEntity<ParkingSpaceDTO> updateParkingSpace(@PathVariable Long parkingId,
            @RequestBody ParkingSpaceDTO parkingDTO) {
        return ResponseEntity.ok(buildingService.updateParkingSpace(parkingId, parkingDTO));
    }

    @GetMapping("/{buildingId}/parking")
    public ResponseEntity<List<ParkingSpaceDTO>> getParkingSpacesByBuilding(@PathVariable Long buildingId) {
        return ResponseEntity.ok(buildingService.getParkingSpacesByBuildingId(buildingId));
    }

    @DeleteMapping("/parking/{parkingId}")
    public ResponseEntity<Void> deleteParkingSpace(@PathVariable Long parkingId) {
        buildingService.deleteParkingSpace(parkingId);
        return ResponseEntity.noContent().build();
    }
}
