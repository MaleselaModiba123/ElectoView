package za.ac.cput.repositories;

import za.ac.cput.domain.DailySummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDailySummaryRepository extends Repository<DailySummary, String> {

    List<DailySummary> findByZoneId(String zoneId);

    Optional<DailySummary> findByZoneIdAndSummaryDate(String zoneId, LocalDate date);

    List<DailySummary> findByZoneIdAndSummaryDateBetween(
            String zoneId,
            LocalDate from,
            LocalDate to
    );
}