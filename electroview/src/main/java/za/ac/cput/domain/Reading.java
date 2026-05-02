package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import za.ac.cput.Enums.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "readings")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Reading {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String readingId;

    @NotBlank
    @Column(nullable = false)
    private String meterId;

    @Positive
    @Column(nullable = false)
    private double kwhConsumed;

    @Column(nullable = false)
    private LocalDateTime recordedAt;

    @Column(updatable = false)
    private LocalDateTime receivedAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReadingStatus status = ReadingStatus.RECEIVED;

    @Column(nullable = false)
    private boolean anomaly = false;

    @Builder
    public Reading(String meterId, double kwhConsumed, LocalDateTime recordedAt) {
        this.meterId = meterId;
        this.kwhConsumed = kwhConsumed;
        this.recordedAt = recordedAt;
        this.receivedAt = LocalDateTime.now();
        this.status = ReadingStatus.RECEIVED;
        this.anomaly = false;
    }

    public boolean validate() {
        this.status = ReadingStatus.VALIDATING;
        if (kwhConsumed <= 0 || meterId == null || meterId.isBlank()) {
            this.status = ReadingStatus.REJECTED;
            return false;
        }
        this.status = ReadingStatus.PERSISTED;
        return true;
    }

    public boolean checkAgainstThreshold(double threshold) {
        return kwhConsumed > threshold;
    }

    public void flag()   { this.anomaly = true; }
    public void reject() { this.status = ReadingStatus.REJECTED; }
}