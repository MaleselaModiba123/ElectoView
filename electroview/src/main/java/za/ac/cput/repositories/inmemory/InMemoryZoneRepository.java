package za.ac.cput.repositories.inmemory;

import za.ac.cput.Enums.*;
import za.ac.cput.domain.*;
import za.ac.cput.repositories.IZoneRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryZoneRepository implements IZoneRepository {
    private final Map<String, Zone> storage = new ConcurrentHashMap<>();

    @Override
    public Zone save(Zone entity) {
        if (entity == null || entity.getZoneId() == null)
            throw new IllegalArgumentException("Zone and ID cannot be null.");
        storage.put(entity.getZoneId(), entity);
        return entity;
    }

    @Override
    public Optional<Zone> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Zone> findAll() {
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
    public List<Zone> findByStatus(ZoneStatus status) {
        return storage.values().stream()
                .filter(z -> z.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return storage.values().stream()
                .anyMatch(z -> z.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }
    
}
