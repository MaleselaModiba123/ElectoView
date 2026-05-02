package za.ac.cput.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import za.ac.cput.Enums.*;
import za.ac.cput.domain.Reading;
import za.ac.cput.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meters")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Meter {

    @Id
    @UuidGenerator
    @Column(updatable = false, nullable = false)
    private String meterId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterStatus status = MeterStatus.REGISTERED;

    @Column(updatable = false)
    private LocalDateTime installedAt = LocalDateTime.now();

    private LocalDateTime lastReadingAt;

    @Column(nullable = false)
    private int consecutiveRejectedReadings = 0;

    @Column(nullable = false)
    private String zoneId;

    private String consumerId;

    @OneToMany(mappedBy = "meterId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reading> readings = new ArrayList<>();

    private static final int FAULT_SUSPECTED_THRESHOLD = 3;

    @Builder
    public Meter(String serialNumber, String zoneId) {
        this.serialNumber = serialNumber;
        this.zoneId = zoneId;
        this.status = MeterStatus.REGISTERED;
        this.installedAt = LocalDateTime.now();
        this.consecutiveRejectedReadings = 0;
    }

    public void activate()     { this.status = MeterStatus.ACTIVE; }
    public void deactivate()   { this.status = MeterStatus.OFFLINE; }
    public void decommission() { this.status = MeterStatus.DECOMMISSIONED; }
    public void flagOffline()  { this.status = MeterStatus.OFFLINE; }

    public void addReading(Reading reading) {
        readings.add(reading);
        if (reading.getStatus() == ReadingStatus.REJECTED) {
            consecutiveRejectedReadings++;
            if (consecutiveRejectedReadings >= FAULT_SUSPECTED_THRESHOLD)
                this.status = MeterStatus.FAULT_SUSPECTED;
        } else {
            consecutiveRejectedReadings = 0;
            this.lastReadingAt = LocalDateTime.now();
        }
    }

    public Reading getLatestReading() {
        return readings.isEmpty() ? null : readings.get(readings.size() - 1);
    }

    public boolean isOnline()         { return status == MeterStatus.ACTIVE; }
    public boolean isFaultSuspected() { return status == MeterStatus.FAULT_SUSPECTED; }

    public void assignConsumer(String consumerId) {
        this.consumerId = consumerId;
    }
}