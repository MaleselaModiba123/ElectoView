package za.ac.cput.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Zones", description = "Electricity distribution zone management operations")
public class ZoneController {

    private final ZoneService zoneService;

    @Operation(summary = "Create a new zone",
               description = "Creates a new electricity distribution zone. "
                           + "Fails if a zone with the same name already exists.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Zone created successfully"),
            @ApiResponse(responseCode = "400", description = "Zone name already exists")
    })
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

    @Operation(summary = "Get zone by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zone found"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Zone> getById(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.findById(id));
    }

    @Operation(summary = "Get all zones")
    @ApiResponse(responseCode = "200", description = "List of all zones returned")
    @GetMapping
    public ResponseEntity<List<Zone>> getAll() {
        return ResponseEntity.ok(zoneService.findAll());
    }

    @Operation(summary = "Get all active zones",
               description = "Returns only zones with NORMAL status.")
    @ApiResponse(responseCode = "200", description = "List of active zones returned")
    @GetMapping("/active")
    public ResponseEntity<List<Zone>> getActive() {
        return ResponseEntity.ok(zoneService.findActiveZones());
    }

    @Operation(summary = "Activate a zone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zone activated"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Zone> activate(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.activate(id));
    }

    @Operation(summary = "Deactivate a zone")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zone deactivated"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Zone> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.deactivate(id));
    }

    @Operation(summary = "Update a zone's anomaly detection threshold",
               description = "Sets either an ABSOLUTE kWh threshold or a RELATIVE "
                           + "percentage of the rolling average. "
                           + "Throws 400 if the threshold value is not positive.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Threshold updated"),
            @ApiResponse(responseCode = "400", description = "Threshold value must be positive"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @PatchMapping("/{id}/threshold")
    public ResponseEntity<Zone> updateThreshold(
            @PathVariable String id,
            @RequestParam ThresholdType type,
            @RequestParam double value) {
        return ResponseEntity.ok(zoneService.updateThreshold(id, type, value));
    }

    @Operation(summary = "Refresh a zone's computed status",
               description = "Recomputes the zone status (NORMAL / HIGH_LOAD / "
                           + "ANOMALY_ALERT) based on current load percentage.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Zone status refreshed"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<Zone> refreshStatus(@PathVariable String id) {
        return ResponseEntity.ok(zoneService.updateStatus(id));
    }
}