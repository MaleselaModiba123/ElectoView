package za.ac.cput.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.Report;
import za.ac.cput.Enums.ReportFormat;
import za.ac.cput.Enums.ReportStatus;
import za.ac.cput.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Report> requestReport(
            @RequestParam String requestedBy,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,
            @RequestParam ReportFormat format,
            @RequestParam(required = false) String zoneId,
            @RequestParam(required = false) String meterId) {
        Report report = reportService.requestReport(
                requestedBy, startDate, endDate,
                format, zoneId, meterId);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(@PathVariable String id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Report>> getByUser(
            @PathVariable String userId) {
        return ResponseEntity.ok(reportService.findByUser(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Report>> getByStatus(
            @PathVariable ReportStatus status) {
        return ResponseEntity.ok(reportService.findByStatus(status));
    }

    @PatchMapping("/{id}/purge")
    public ResponseEntity<Report> purge(@PathVariable String id) {
        return ResponseEntity.ok(reportService.purgeExpired(id));
    }
}