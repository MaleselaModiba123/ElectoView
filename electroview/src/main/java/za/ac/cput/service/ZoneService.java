package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.Enums.ThresholdType;
import za.ac.cput.domain.*;
import za.ac.cput.Enums.*;
import za.ac.cput.repository.ZoneRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public Zone createZone(String name, String description,
                           String location, double capacityKwh) {
        if (zoneRepository.existsByName(name))
            throw new IllegalArgumentException("Zone already exists: " + name);

        Zone zone = Zone.builder()
                .name(name)
                .description(description)
                .location(location)
                .capacityKwh(capacityKwh)
                .build();

        return zoneRepository.save(zone);
    }

    public Zone findById(String id) {
        return zoneRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found: " + id));
    }

    public List<Zone> findAll() {
        return zoneRepository.findAll();
    }

    public List<Zone> findActiveZones() {
        return zoneRepository.findByStatus(ZoneStatus.NORMAL);
    }

    public Zone activate(String id) {
        Zone zone = findById(id);
        zone.activate();
        return zoneRepository.save(zone);
    }

    public Zone deactivate(String id) {
        Zone zone = findById(id);
        zone.deactivate();
        return zoneRepository.save(zone);
    }

    public Zone updateThreshold(String id, ThresholdType type, double value) {
        Zone zone = findById(id);
        zone.updateThreshold(type, value);
        return zoneRepository.save(zone);
    }

    public Zone updateStatus(String id) {
        Zone zone = findById(id);
        zone.setStatus(zone.computeStatus());
        return zoneRepository.save(zone);
    }
}