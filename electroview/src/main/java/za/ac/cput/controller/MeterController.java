package za.ac.cput.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.service.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
@Tag(name = "Meters", description = "Smart meter registration and lifecycle management operations")
public class MeterController {

    private final MeterService meterService;

    @Operation(summary = "Register a new meter",
               description = "Registers a new smart meter to a zone. "
                           + "Fails if a meter with the same serial number already exists.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Meter registered successfully"),
            @ApiResponse(responseCode = "400", description = "Serial number already registered")
    })
    @PostMapping
    public ResponseEntity<Meter> register(
            @RequestParam String serialNumber,
            @RequestParam String zoneId) {
        Meter meter = meterService.registerMeter(serialNumber, zoneId);
        return new ResponseEntity<>(meter, HttpStatus.CREATED);
    }

    @Operation(summary = "Get meter by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meter found"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Meter> getById(@PathVariable String id) {
        return ResponseEntity.ok(meterService.findById(id));
    }

    @Operation(summary = "Get all meters")
    @ApiResponse(responseCode = "200", description = "List of all meters returned")
    @GetMapping
    public ResponseEntity<List<Meter>> getAll() {
        return ResponseEntity.ok(meterService.findAll());
    }

    @Operation(summary = "Get all meters in a zone",
               description = "Returns every meter assigned to the specified zone ID.")
    @ApiResponse(responseCode = "200", description = "List of meters in the zone returned")
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<Meter>> getByZone(@PathVariable String zoneId) {
        return ResponseEntity.ok(meterService.findByZone(zoneId));
    }

    @Operation(summary = "Get meters by status",
               description = "Filters meters by lifecycle status "
                           + "(REGISTERED, ACTIVE, OFFLINE, FAULT_SUSPECTED, "
                           + "FAULT_CONFIRMED, UNDER_MAINTENANCE, DECOMMISSIONED).")
    @ApiResponse(responseCode = "200", description = "List of matching meters returned")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Meter>> getByStatus(
            @PathVariable MeterStatus status) {
        return ResponseEntity.ok(meterService.findByStatus(status));
    }

    @Operation(summary = "Activate a meter",
               description = "Sets the meter status to ACTIVE so it can accept readings.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meter activated"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Meter> activate(@PathVariable String id) {
        return ResponseEntity.ok(meterService.activate(id));
    }

    @Operation(summary = "Deactivate a meter",
               description = "Sets the meter status to OFFLINE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meter deactivated"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Meter> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(meterService.deactivate(id));
    }

    @Operation(summary = "Decommission a meter",
               description = "Permanently retires the meter. Historical readings "
                           + "are retained under the old meter ID for audit purposes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Meter decommissioned"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    @PatchMapping("/{id}/decommission")
    public ResponseEntity<Meter> decommission(@PathVariable String id) {
        return ResponseEntity.ok(meterService.decommission(id));
    }

    @Operation(summary = "Assign a consumer to a meter",
               description = "Links a residential consumer account to the meter, "
                           + "enabling that consumer to view their own usage data.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consumer assigned to meter"),
            @ApiResponse(responseCode = "404", description = "Meter not found")
    })
    @PatchMapping("/{id}/consumer")
    public ResponseEntity<Meter> assignConsumer(
            @PathVariable String id,
            @RequestParam String consumerId) {
        return ResponseEntity.ok(meterService.assignConsumer(id, consumerId));
    }
}