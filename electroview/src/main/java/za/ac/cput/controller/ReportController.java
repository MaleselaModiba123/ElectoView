package za.ac.cput.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reports", description = "Endpoints for requesting, retrieving, and managing consumption reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(
            summary = "Request a new report",
            description = "Submits a new report generation request. Reports with a short date range are generated inline; longer ranges are queued."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Report created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters or date range")
    })
    @PostMapping
    public ResponseEntity<Report> requestReport(
            @Parameter(description = "ID of the user requesting the report") @RequestParam String requestedBy,
            @Parameter(description = "Start date of the report period (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date of the report period (yyyy-MM-dd)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Output format of the report (e.g. PDF, CSV)") @RequestParam ReportFormat format,
            @Parameter(description = "Optional zone ID to filter the report by zone") @RequestParam(required = false) String zoneId,
            @Parameter(description = "Optional meter ID to filter the report by meter") @RequestParam(required = false) String meterId) {
        Report report = reportService.requestReport(
                requestedBy, startDate, endDate,
                format, zoneId, meterId);
        return new ResponseEntity<>(report, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get a report by ID",
            description = "Returns a single report matching the provided ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Report found"),
            @ApiResponse(responseCode = "404", description = "Report not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Report> getById(
            @Parameter(description = "The unique ID of the report") @PathVariable String id) {
        return ResponseEntity.ok(reportService.findById(id));
    }

    @Operation(
            summary = "Get reports by user",
            description = "Returns all reports requested by the specified user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Report>> getByUser(
            @Parameter(description = "The unique ID of the user") @PathVariable String userId) {
        return ResponseEntity.ok(reportService.findByUser(userId));
    }

    @Operation(
            summary = "Get reports by status",
            description = "Returns all reports matching the given status (e.g. PENDING, READY, EXPIRED)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reports retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status value")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Report>> getByStatus(
            @Parameter(description = "The report status to filter by") @PathVariable ReportStatus status) {
        return ResponseEntity.ok(reportService.findByStatus(status));
    }

    @Operation(
            summary = "Purge an expired report",
            description = "Marks an expired report as purged and removes its generated file from storage."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Report purged successfully"),
            @ApiResponse(responseCode = "404", description = "Report not found"),
            @ApiResponse(responseCode = "409", description = "Report is not expired and cannot be purged")
    })
    @PatchMapping("/{id}/purge")
    public ResponseEntity<Report> purge(
            @Parameter(description = "The unique ID of the report to purge") @PathVariable String id) {
        return ResponseEntity.ok(reportService.purgeExpired(id));
    }
}