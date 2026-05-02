package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.service.*;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DailySummaryService dailySummaryService;
    private final MeterService        meterService;

    @PostMapping("/zones/{zoneId}/summarise")
    public ResponseEntity<DailySummary> summarise(
            @PathVariable String zoneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<String> meterIds = meterService.findByZone(zoneId)
                .stream()
                .map(m -> m.getMeterId())
                .toList();

        DailySummary summary = dailySummaryService
                .computeForZone(zoneId, date, meterIds);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/zones/{zoneId}/summaries")
    public ResponseEntity<List<DailySummary>> getSummaries(
            @PathVariable String zoneId) {
        return ResponseEntity.ok(dailySummaryService.findByZone(zoneId));
    }

    @GetMapping("/zones/{zoneId}/summaries/range")
    public ResponseEntity<List<DailySummary>> getSummariesInRange(
            @PathVariable String zoneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {
        return ResponseEntity.ok(
                dailySummaryService.findByZoneBetween(zoneId, from, to));
    }
}