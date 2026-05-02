package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Anomaly;
import za.ac.cput.Enums.AnomalyStatus;

import java.util.List;

@Repository
public interface AnomalyRepository extends JpaRepository<Anomaly, String> {

    List<Anomaly> findByMeterId(String meterId);

    List<Anomaly> findByStatus(AnomalyStatus status);

    List<Anomaly> findByMeterIdAndStatus(String meterId, AnomalyStatus status);

    List<Anomaly> findByAssignedTo(String userId);

    long countByStatus(AnomalyStatus status);
}