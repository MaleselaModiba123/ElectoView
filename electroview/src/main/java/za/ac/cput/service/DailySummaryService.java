package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.DailySummaryRepository;
import za.ac.cput.repository.ReadingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
public class DailySummaryService {

    private final DailySummaryRepository dailySummaryRepository;
    private final ReadingRepository      readingRepository;

    public DailySummary computeForZone(String zoneId,
                                        LocalDate date,
                                        List<String> meterIds) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.plusDays(1).atStartOfDay();

        // Collect all readings for the zone on the given date
        List<Double> allKwh = meterIds.stream()
                .flatMap(mid -> readingRepository
                        .findByMeterIdAndRecordedAtBetween(mid, start, end)
                        .stream())
                .map(Reading::getKwhConsumed)
                .toList();

        double total = allKwh.stream().mapToDouble(Double::doubleValue).sum();
        OptionalDouble avg = allKwh.stream().mapToDouble(Double::doubleValue).average();
        OptionalDouble peak = allKwh.stream().mapToDouble(Double::doubleValue).max();

        // Upsert — update if exists, create if not
        DailySummary summary = dailySummaryRepository
                .findByZoneIdAndSummaryDate(zoneId, date)
                .orElse(DailySummary.builder()
                        .zoneId(zoneId)
                        .summaryDate(date)
                        .build());

        summary.compute(
                total,
                avg.orElse(0.0),
                peak.orElse(0.0),
                meterIds.size()
        );

        return dailySummaryRepository.save(summary);
    }

    public List<DailySummary> findByZone(String zoneId) {
        return dailySummaryRepository.findByZoneId(zoneId);
    }

    public List<DailySummary> findByZoneBetween(String zoneId,
                                                  LocalDate from,
                                                  LocalDate to) {
        return dailySummaryRepository
                .findByZoneIdAndSummaryDateBetween(zoneId, from, to);
    }
}