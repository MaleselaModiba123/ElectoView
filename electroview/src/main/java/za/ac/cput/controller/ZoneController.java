package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.Enums.ThresholdType;
import za.ac.cput.domain.Zone;
import za.ac.cput.service.ZoneService;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public ResponseEntity<Zone> createZone(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam double capacityKwh) {
        Zone zone = zoneService.createZone(name, description,
                                           location, capacityKwh);
        return new ResponseEntity<>(zone, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> getById(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Zone>> getAll() {
        return ResponseEntity.ok(zoneService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Zone>> getActive() {
        return ResponseEntity.ok(zoneService.findActiveZones());
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Zone> activate(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Zone> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.deactivate(id));
    }

    @PatchMapping("/{id}/threshold")
    public ResponseEntity<Zone> updateThreshold(
            @PathVariable String id,
            @RequestParam ThresholdType type,
            @RequestParam double value) {
        return ResponseEntity.ok(zoneService.updateThreshold(id, type, value));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Zone> refreshStatus(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.updateStatus(id));
    }
}