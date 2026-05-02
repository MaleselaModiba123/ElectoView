package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.DailySummary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, String> {

    List<DailySummary> findByZoneId(String zoneId);

    Optional<DailySummary> findByZoneIdAndSummaryDate(String zoneId, LocalDate date);

    List<DailySummary> findByZoneIdAndSummaryDateBetween(
            String zoneId,
            LocalDate from,
            LocalDate to
    );
}