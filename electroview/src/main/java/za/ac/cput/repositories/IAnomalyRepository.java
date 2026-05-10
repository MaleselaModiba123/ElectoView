package za.ac.cput.repositories;

import za.ac.cput.domain.Anomaly;
import za.ac.cput.Enums.*;

import java.util.List;

public interface IAnomalyRepository extends Repository<Anomaly, String> {
    List<Anomaly> findByMeterId(String meterId);

    List<Anomaly> findByStatus(AnomalyStatus status);

    List<Anomaly>findByAssignedTo(String userId);

     long countByStatus(AnomalyStatus status);
}
