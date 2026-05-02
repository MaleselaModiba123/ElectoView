package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.MeterRepository;
import za.ac.cput.repository.ReadingRepository;
import za.ac.cput.repository.ZoneRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private final ReadingRepository readingRepository;
    private final MeterRepository   meterRepository;
    private final ZoneRepository    zoneRepository;
    private final AnomalyService    anomalyService;

    public Reading ingestReading(String meterId,
                                 double kwhConsumed,
                                 LocalDateTime recordedAt) {

        // 1. Build and validate the reading
        Reading reading = Reading.builder()
                .meterId(meterId)
                .kwhConsumed(kwhConsumed)
                .recordedAt(recordedAt)
                .build();

        if (!reading.validate()) {
            readingRepository.save(reading);
            throw new IllegalArgumentException(
                "Invalid reading for meter: " + meterId);
        }

        // 2. Update meter state
        Meter meter = meterRepository.findById(meterId)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Meter not found: " + meterId));
        meter.addReading(reading);
        meterRepository.save(meter);

        // 3. Check against zone threshold
        Zone zone = zoneRepository.findById(meter.getZoneId())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Zone not found: " + meter.getZoneId()));

        if (reading.checkAgainstThreshold(zone.getThresholdValue())) {
            reading.flag();
            anomalyService.createAnomaly(
                meterId,
                reading.getReadingId(),
                zone.getThresholdValue(),
                kwhConsumed
            );
        }

        return readingRepository.save(reading);
    }

    public List<Reading> findByMeter(String meterId) {
        return readingRepository.findByMeterId(meterId);
    }

    public List<Reading> findByMeterBetween(String meterId,
                                             LocalDateTime from,
                                             LocalDateTime to) {
        return readingRepository
                .findByMeterIdAndRecordedAtBetween(meterId, from, to);
    }

    public List<Reading> findAllAnomalousReadings() {
        return readingRepository.findByAnomalyTrue();
    }
}