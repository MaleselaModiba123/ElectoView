package za.ac.cput.repositories;

import za.ac.cput.domain.Reading;

import java.time.LocalDateTime;
import java.util.List;

public interface IReadingRepository extends Repository<Reading, String> {
    List<Reading> findByMeterId(String meterId);
    List<Reading>findByMeterIdAndTimestampBetween(String meterId, LocalDateTime startTime, LocalDateTime endTime);
    List<Reading>findAnomalous();
    
}
