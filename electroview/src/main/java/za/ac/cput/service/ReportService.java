package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Report;
import za.ac.cput.Enums.ReportFormat;
import za.ac.cput.Enums.ReportStatus;
import za.ac.cput.repository.ReportRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public Report requestReport(String requestedBy, LocalDate startDate,
                                LocalDate endDate, ReportFormat format,
                                String zoneId, String meterId) {

        Report report = Report.builder()
                .requestedBy(requestedBy)
                .startDate(startDate)
                .endDate(endDate)
                .format(format)
                .build();

        report.setZoneId(zoneId);
        report.setMeterId(meterId);

        if (!report.validate())
            throw new IllegalArgumentException(
                "Invalid report parameters: check date range.");

        if (report.requiresBackgroundProcessing()) {
            report.markQueued();
        } else {
            report.markGenerating();
            // Simulate generation — replace with real logic later
            report.markReady("/reports/" + report.getReportId()
                             + "." + format.name().toLowerCase());
        }

        return reportRepository.save(report);
    }

    public Report findById(String id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Report not found: " + id));
    }

    public List<Report> findByUser(String userId) {
        return reportRepository.findByRequestedBy(userId);
    }

    public List<Report> findByStatus(ReportStatus status) {
        return reportRepository.findByStatus(status);
    }

    public Report purgeExpired(String id) {
        Report report = findById(id);
        if (report.isExpired()) {
            report.purge();
            return reportRepository.save(report);
        }
        return report;
    }
}