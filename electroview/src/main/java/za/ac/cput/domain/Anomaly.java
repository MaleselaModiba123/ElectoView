package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import za.ac.cput.Enums.AnomalyStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "anomalies")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Anomaly {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String anomalyId;

    @NotBlank
    @Column(nullable = false)
    private String meterId;

    @NotBlank
    @Column(nullable = false)
    private String readingId;

    @Column(nullable = false)
    private double thresholdAtTime;

    @Column(nullable = false)
    private double actualValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AnomalyStatus status = AnomalyStatus.OPEN;

    @Column(updatable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();

    private String assignedTo;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;

    private static final int ESCALATION_HOURS = 48;

    @Builder
    public Anomaly(String meterId, String readingId,
                   double thresholdAtTime, double actualValue) {
        this.meterId = meterId;
        this.readingId = readingId;
        this.thresholdAtTime = thresholdAtTime;
        this.actualValue = actualValue;
        this.status = AnomalyStatus.OPEN;
        this.detectedAt = LocalDateTime.now();
    }

    public void assign(String userId) {
        this.assignedTo = userId;
        this.status = AnomalyStatus.IN_PROGRESS;
    }

    public void escalate() {
        this.status = AnomalyStatus.ESCALATED;
    }

    public void resolve(String notes, String userId) {
        if (notes == null || notes.isBlank())
            throw new IllegalArgumentException("Resolution notes cannot be empty.");
        this.resolutionNotes = notes;
        this.assignedTo = userId;
        this.status = AnomalyStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    public void autoResolve() {
        this.status = AnomalyStatus.AUTO_RESOLVED;
        this.resolvedAt = LocalDateTime.now();
        this.resolutionNotes = "Auto-resolved: consumption returned to normal.";
    }

    public boolean isOverdue() {
        return ChronoUnit.HOURS.between(detectedAt, LocalDateTime.now()) >= ESCALATION_HOURS;
    }

    public long getAgeInHours() {
        return ChronoUnit.HOURS.between(detectedAt, LocalDateTime.now());
    }
}