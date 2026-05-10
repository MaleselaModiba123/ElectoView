package za.ac.cput.repositories.inmemory;

import za.ac.cput.Enums.*;
import za.ac.cput.domain.*;
import za.ac.cput.repositories.IAnomalyRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryAnomalyRepository implements IAnomalyRepository {

    private final Map<String, Anomaly> storage = new ConcurrentHashMap<>();

    @Override
    public Anomaly save(Anomaly entity) {
        if (entity == null || entity.getAnomalyId() == null)
            throw new IllegalArgumentException("Anomaly and ID cannot be null.");
        storage.put(entity.getAnomalyId(), entity);
        return entity;
    }

    @Override
    public Optional<Anomaly> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Anomaly> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    @Override
    public long count() {
        return storage.size();
    }

    @Override
    public List<Anomaly> findByMeterId(String meterId) {
        return storage.values().stream()
                .filter(a -> meterId.equals(a.getMeterId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> findByStatus(AnomalyStatus status) {
        return storage.values().stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Anomaly> findByAssignedTo(String userId) {
        return storage.values().stream()
                .filter(a -> userId.equals(a.getAssignedTo()))
                .collect(Collectors.toList());
    }

    @Override
    public long countByStatus(AnomalyStatus status) {
        return storage.values().stream()
                .filter(a -> a.getStatus() == status)
                .count();
    }

    @Override
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }
}