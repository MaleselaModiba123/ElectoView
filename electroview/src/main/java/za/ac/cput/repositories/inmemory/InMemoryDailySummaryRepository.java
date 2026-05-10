package za.ac.cput.repositories.inmemory;

import za.ac.cput.domain.*;
import za.ac.cput.repositories.IDailySummaryRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryDailySummaryRepository implements IDailySummaryRepository {

    private final Map<String, DailySummary> storage = new ConcurrentHashMap<>();

    @Override
    public DailySummary save(DailySummary entity) {
        if (entity == null || entity.getSummaryId() == null)
            throw new IllegalArgumentException("Summary and ID cannot be null.");
        storage.put(entity.getSummaryId(), entity);
        return entity;
    }

    @Override
    public Optional<DailySummary> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<DailySummary> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean delete(String id) {
        return storage.remove(id) != null;
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
    public List<DailySummary> findByZoneId(String zoneId) {
        return storage.values().stream()
                .filter(s -> zoneId.equals(s.getZoneId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DailySummary> findByZoneIdAndSummaryDate(String zoneId, LocalDate date) {
        return storage.values().stream()
                .filter(s -> zoneId.equals(s.getZoneId())
                          && s.getSummaryDate().equals(date))
                .findFirst();
    }

    @Override
    public List<DailySummary> findByZoneIdAndSummaryDateBetween(
            String zoneId, LocalDate from, LocalDate to) {
        return storage.values().stream()
                .filter(s -> zoneId.equals(s.getZoneId()))
                .filter(s -> !s.getSummaryDate().isBefore(from)
                          && !s.getSummaryDate().isAfter(to))
                .collect(Collectors.toList());
    }
}