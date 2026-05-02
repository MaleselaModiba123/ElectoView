package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import za.ac.cput.Enums.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "zones")
@NoArgsConstructor
public class Zone{
    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String zoneId;

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @NotBlank
    @Column(nullable = false)
    private String location;

    @Positive
    @Column(nullable = false)
    private double capacityKwh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThresholdType thresholdType = ThresholdType.RELATIVE;

    @Column(nullable = false)
    private double thresholdValue = 120.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ZoneStatus status = ZoneStatus.CONFIGURED;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "zoneId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Meter> meters = new ArrayList<>();

    @Builder
    public Zone(String name, String description, String location, double capacityKwh, ThresholdType thresholdType, double thresholdValue) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.capacityKwh = capacityKwh;
        this.thresholdType = thresholdType;
        this.thresholdValue = thresholdValue;
        this.thresholdValue = 120.0;
        this.status = ZoneStatus.CONFIGURED;
        this.createdAt = LocalDateTime.now();
    }
    public void activate()   { this.status = ZoneStatus.NORMAL; }
    public void deactivate() { this.status = ZoneStatus.INACTIVE; }

    public void updateThreshold(ThresholdType type, double value) {
        if (value <= 0)
            throw new IllegalArgumentException("Threshold value must be positive.");
        this.thresholdType = type;
        this.thresholdValue = value;
    }

    public double getCurrentLoad() {
        return meters.stream().mapToDouble(m -> {
            Reading r = m.getLatestReading();
            return r != null ? r.getKwhConsumed() : 0.0;
        }).sum();
    }
    public double getLoadPercentage() {
        return capacityKwh > 0 ? (getCurrentLoad() / capacityKwh) * 100.0 : 0.0;
    }

    public ZoneStatus computeStatus() {
        double pct = getLoadPercentage();
        if (pct > 100.0) return ZoneStatus.ANOMALY_ALERT;
        if (pct > 90.0)  return ZoneStatus.HIGH_LOAD;
        return ZoneStatus.NORMAL;
    }

    public void addMeter(Meter meter) {
        this.meters.add(meter);
    }
}