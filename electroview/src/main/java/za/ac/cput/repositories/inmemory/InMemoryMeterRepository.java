package za.ac.cput.repositories.inmemory;

import za.ac.cput.Enums.*;
import za.ac.cput.domain.*;
import za.ac.cput.repositories.IMeterRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryMeterRepository implements IMeterRepository {

    private final Map<String, Meter> storage = new ConcurrentHashMap<>();

    @Override
    public Meter save(Meter entity) {
        if (entity == null || entity.getMeterId() == null)
            throw new IllegalArgumentException("Meter and ID cannot be null.");
        storage.put(entity.getMeterId(), entity);
        return entity;
    }

    @Override
    public Optional<Meter> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Meter> findAll() {
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
    public List<Meter> findByZoneId(String zoneId) {
        return storage.values().stream()
                .filter(m -> zoneId.equals(m.getZoneId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meter> findByStatus(MeterStatus status) {
        return storage.values().stream()
                .filter(m -> m.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Meter> findBySerialNumber(String serialNumber) {
        return storage.values().stream()
                .filter(m -> serialNumber.equals(m.getSerialNumber()))
                .findFirst();
    }

    @Override
    public List<Meter> findByCustomerId(String consumerId) {
        return storage.values().stream()
                .filter(m -> consumerId.equals(m.getConsumerId()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBySerialNumber(String serialNumber) {
        return storage.values().stream()
                .anyMatch(m -> serialNumber.equals(m.getSerialNumber()));
    }

    @Override
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }
}