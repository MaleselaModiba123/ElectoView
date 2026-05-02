package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.*;
import za.ac.cput.service.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/readings")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;

    @PostMapping
    public ResponseEntity<Reading> ingest(
            @RequestParam String meterId,
            @RequestParam double kwhConsumed,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime recordedAt) {
        Reading reading = readingService.ingestReading(
                meterId, kwhConsumed, recordedAt);
        return new ResponseEntity<>(reading, HttpStatus.CREATED);
    }

    @GetMapping("/meter/{meterId}")
    public ResponseEntity<List<Reading>> getByMeter(
            @PathVariable String meterId) {
        return ResponseEntity.ok(readingService.findByMeter(meterId));
    }

    @GetMapping("/meter/{meterId}/range")
    public ResponseEntity<List<Reading>> getByMeterAndRange(
            @PathVariable String meterId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime from,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime to) {
        return ResponseEntity.ok(
                readingService.findByMeterBetween(meterId, from, to));
    }

    @GetMapping("/anomalous")
    public ResponseEntity<List<Reading>> getAnomalous() {
        return ResponseEntity.ok(readingService.findAllAnomalousReadings());
    }
}