package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Anomaly;
import za.ac.cput.Enums.AnomalyStatus;
import za.ac.cput.service.AnomalyService;

import java.util.List;

@RestController
@RequestMapping("/api/anomalies")
@RequiredArgsConstructor
public class AnomalyController {

    private final AnomalyService anomalyService;

    @GetMapping
    public ResponseEntity<List<Anomaly>> getAll() {
        return ResponseEntity.ok(anomalyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anomaly> getById(@PathVariable String id) {
        return ResponseEntity.ok(anomalyService.findById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Anomaly>> getByStatus(
            @PathVariable AnomalyStatus status) {
        return ResponseEntity.ok(anomalyService.findByStatus(status));
    }

    @GetMapping("/meter/{meterId}")
    public ResponseEntity<List<Anomaly>> getByMeter(
            @PathVariable String meterId) {
        return ResponseEntity.ok(anomalyService.findByMeter(meterId));
    }

    @GetMapping("/count/open")
    public ResponseEntity<Long> countOpen() {
        return ResponseEntity.ok(anomalyService.countOpen());
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<Anomaly> assign(
            @PathVariable String id,
            @RequestParam String userId) {
        return ResponseEntity.ok(anomalyService.assign(id, userId));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<Anomaly> resolve(
            @PathVariable String id,
            @RequestParam String notes,
            @RequestParam String userId) {
        return ResponseEntity.ok(anomalyService.resolve(id, notes, userId));
    }

    @PatchMapping("/{id}/escalate")
    public ResponseEntity<Anomaly> escalate(@PathVariable String id) {
        return ResponseEntity.ok(anomalyService.escalate(id));
    }

    @PatchMapping("/{id}/auto-resolve")
    public ResponseEntity<Anomaly> autoResolve(@PathVariable String id) {
        return ResponseEntity.ok(anomalyService.autoResolve(id));
    }
}