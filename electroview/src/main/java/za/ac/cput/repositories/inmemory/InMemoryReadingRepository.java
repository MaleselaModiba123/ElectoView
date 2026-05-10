package za.ac.cput.repositories.inmemory;

import za.ac.cput.domain.Reading;
import za.ac.cput.repositories.IReadingRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryReadingRepository implements IReadingRepository {

    private final Map<String, Reading> storage = new ConcurrentHashMap<>();

    @Override
    public Reading save(Reading entity) {
        if (entity == null || entity.getReadingId() == null)
            throw new IllegalArgumentException("Reading and ID cannot be null.");
        storage.put(entity.getReadingId(), entity);
        return entity;
    }

    @Override
    public Optional<Reading> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Reading> findAll() {
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
    public List<Reading> findByMeterId(String meterId) {
        return storage.values().stream()
                .filter(r -> meterId.equals(r.getMeterId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reading> findByMeterIdAndTimestampBetween(String meterId, LocalDateTime from, LocalDateTime to) {
        return storage.values().stream()
                .filter(r -> meterId.equals(r.getMeterId()))
                .filter(r -> !r.getRecordedAt().isBefore(from)
                          && !r.getRecordedAt().isAfter(to))
                .collect(Collectors.toList());
    }

    @Override
    public List<Reading> findAnomalous() {
        return storage.values().stream()
                .filter(Reading::isAnomaly)
                .collect(Collectors.toList());
    }

    @Override
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }
}