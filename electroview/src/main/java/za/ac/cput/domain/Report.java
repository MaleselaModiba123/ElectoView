package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import za.ac.cput.Enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Report {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String reportId;

    @NotBlank
    @Column(nullable = false)
    private String requestedBy;

    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;

    private String zoneId;
    private String meterId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportFormat format;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.REQUESTED;

    @Column(updatable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();

    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    private String filePath;

    private static final long EXPIRY_HOURS = 24;
    private static final int LARGE_REPORT_DAY_THRESHOLD = 90;

    @Builder
    public Report(String requestedBy, LocalDate startDate,
                  LocalDate endDate, ReportFormat format) {
        this.requestedBy = requestedBy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.format = format;
        this.status = ReportStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

    public boolean validate() {
        return startDate != null && endDate != null
               && !startDate.isAfter(endDate);
    }

    public boolean requiresBackgroundProcessing() {
        long days = endDate.toEpochDay() - startDate.toEpochDay();
        return days > LARGE_REPORT_DAY_THRESHOLD;
    }

    public void markQueued()     { this.status = ReportStatus.QUEUED; }
    public void markGenerating() { this.status = ReportStatus.GENERATING; }
    public void markFailed()     { this.status = ReportStatus.FAILED; }

    public void markReady(String filePath) {
        this.status = ReportStatus.READY;
        this.generatedAt = LocalDateTime.now();
        this.expiresAt = generatedAt.plusHours(EXPIRY_HOURS);
        this.filePath = filePath;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public void purge() {
        this.status = ReportStatus.EXPIRED;
        this.filePath = null;
    }
}