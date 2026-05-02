package za.ac.cput.controller;

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
public class MeterController {

    private final MeterService meterService;

    @PostMapping
    public ResponseEntity<Meter> register(
            @RequestParam String serialNumber,
            @RequestParam String zoneId) {
        Meter meter = meterService.registerMeter(serialNumber, zoneId);
        return new ResponseEntity<>(meter, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meter> getById(@PathVariable String id) {
        return ResponseEntity.ok(meterService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<Meter>> getAll() {
        return ResponseEntity.ok(meterService.findAll());
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<Meter>> getByZone(@PathVariable String zoneId) {
        return ResponseEntity.ok(meterService.findByZone(zoneId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Meter>> getByStatus(
            @PathVariable MeterStatus status) {
        return ResponseEntity.ok(meterService.findByStatus(status));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Meter> activate(@PathVariable String id) {
        return ResponseEntity.ok(meterService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Meter> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(meterService.deactivate(id));
    }

    @PatchMapping("/{id}/decommission")
    public ResponseEntity<Meter> decommission(@PathVariable String id) {
        return ResponseEntity.ok(meterService.decommission(id));
    }

    @PatchMapping("/{id}/consumer")
    public ResponseEntity<Meter> assignConsumer(
            @PathVariable String id,
            @RequestParam String consumerId) {
        return ResponseEntity.ok(meterService.assignConsumer(id, consumerId));
    }
}