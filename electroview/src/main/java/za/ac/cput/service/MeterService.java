package za.ac.cput.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Meter;
import za.ac.cput.Enums.MeterStatus;
import za.ac.cput.repository.MeterRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeterService {

    private final MeterRepository meterRepository;

    public Meter registerMeter(String serialNumber, String zoneId) {
        if (meterRepository.existsBySerialNumber(serialNumber))
            throw new IllegalArgumentException("Meter already registered: " + serialNumber);

        Meter meter = Meter.builder()
                .serialNumber(serialNumber)
                .zoneId(zoneId)
                .build();

        return meterRepository.save(meter);
    }

    public Meter findById(String id) {
        return meterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Meter not found: " + id));
    }

    public List<Meter> findAll() {
        return meterRepository.findAll();
    }

    public List<Meter> findByZone(String zoneId) {
        return meterRepository.findByZoneId(zoneId);
    }

    public List<Meter> findByStatus(MeterStatus status) {
        return meterRepository.findByStatus(status);
    }

    public Meter activate(String id) {
        Meter meter = findById(id);
        meter.activate();
        return meterRepository.save(meter);
    }

    public Meter deactivate(String id) {
        Meter meter = findById(id);
        meter.deactivate();
        return meterRepository.save(meter);
    }

    public Meter decommission(String id) {
        Meter meter = findById(id);
        meter.decommission();
        return meterRepository.save(meter);
    }

    public Meter assignConsumer(String meterId, String consumerId) {
        Meter meter = findById(meterId);
        meter.assignConsumer(consumerId);
        return meterRepository.save(meter);
    }
}