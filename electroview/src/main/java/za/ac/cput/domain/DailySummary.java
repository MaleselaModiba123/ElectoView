package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_summaries")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailySummary {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String summaryId;

    @NotBlank
    @Column(nullable = false)
    private String zoneId;

    @NotNull
    @Column(nullable = false)
    private LocalDate summaryDate;

    private double totalKwh;
    private double avgKwh;
    private double peakKwh;
    private int meterCount;

    private LocalDateTime computedAt = LocalDateTime.now();

    private static final long STALE_MINUTES = 30;

    @Builder
    public DailySummary(String zoneId, LocalDate summaryDate) {
        this.zoneId = zoneId;
        this.summaryDate = summaryDate;
        this.computedAt = LocalDateTime.now();
    }

    public void compute(double totalKwh, double avgKwh,
                        double peakKwh, int meterCount) {
        this.totalKwh = totalKwh;
        this.avgKwh = avgKwh;
        this.peakKwh = peakKwh;
        this.meterCount = meterCount;
        this.computedAt = LocalDateTime.now();
    }

    public boolean isStale() {
        return computedAt.plusMinutes(STALE_MINUTES)
                         .isBefore(LocalDateTime.now());
    }
}