package za.ac.cput.repositories.inmemory;

import za.ac.cput.Enums.ReportStatus;
import za.ac.cput.domain.Report;
import za.ac.cput.repositories.IReportRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryReportRepository implements IReportRepository {

    private final Map<String, Report> storage = new ConcurrentHashMap<>();

    @Override
    public Report save(Report entity) {
        if (entity == null || entity.getReportId() == null)
            throw new IllegalArgumentException("Report and ID cannot be null.");
        storage.put(entity.getReportId(), entity);
        return entity;
    }

    @Override
    public Optional<Report> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Report> findAll() {
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
    public List<Report> findByRequestedBy(String userId) {
        return storage.values().stream()
                .filter(r -> userId.equals(r.getRequestedBy()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByStatus(ReportStatus status) {
        return storage.values().stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> findByZoneId(String zoneId) {
        return storage.values().stream()
                .filter(r -> zoneId.equals(r.getZoneId()))
                .collect(Collectors.toList());
    }
}