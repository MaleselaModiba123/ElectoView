package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Reading;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReadingRepository extends JpaRepository<Reading, String> {

    List<Reading> findByMeterId(String meterId);

    List<Reading> findByMeterIdAndRecordedAtBetween(
            String meterId,
            LocalDateTime from,
            LocalDateTime to
    );

    List<Reading> findByAnomalyTrue();

    List<Reading> findByMeterIdOrderByRecordedAtDesc(String meterId);
}