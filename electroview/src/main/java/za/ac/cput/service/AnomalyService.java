package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Anomaly;
import za.ac.cput.Enums.AnomalyStatus;
import za.ac.cput.repository.AnomalyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnomalyService {

    private final AnomalyRepository anomalyRepository;

    public Anomaly createAnomaly(String meterId, String readingId,
                                  double threshold, double actualValue) {
        Anomaly anomaly = Anomaly.builder()
                .meterId(meterId)
                .readingId(readingId)
                .thresholdAtTime(threshold)
                .actualValue(actualValue)
                .build();

        return anomalyRepository.save(anomaly);
    }

    public Anomaly findById(String id) {
        return anomalyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Anomaly not found: " + id));
    }

    public List<Anomaly> findAll() {
        return anomalyRepository.findAll();
    }

    public List<Anomaly> findByStatus(AnomalyStatus status) {
        return anomalyRepository.findByStatus(status);
    }

    public List<Anomaly> findByMeter(String meterId) {
        return anomalyRepository.findByMeterId(meterId);
    }

    public Anomaly assign(String id, String userId) {
        Anomaly anomaly = findById(id);
        anomaly.assign(userId);
        return anomalyRepository.save(anomaly);
    }

    public Anomaly resolve(String id, String notes, String userId) {
        Anomaly anomaly = findById(id);
        anomaly.resolve(notes, userId);
        return anomalyRepository.save(anomaly);
    }

    public Anomaly escalate(String id) {
        Anomaly anomaly = findById(id);
        anomaly.escalate();
        return anomalyRepository.save(anomaly);
    }

    public Anomaly autoResolve(String id) {
        Anomaly anomaly = findById(id);
        anomaly.autoResolve();
        return anomalyRepository.save(anomaly);
    }

    public long countOpen() {
        return anomalyRepository.countByStatus(AnomalyStatus.OPEN);
    }
}