package za.ac.cput.repositories;

import za.ac.cput.domain.Meter;
import za.ac.cput.Enums.MeterStatus;

import java.util.List;
import java.util.Optional;

public interface IMeterRepository extends Repository<Meter, String> {
    List<Meter> findByStatus(MeterStatus status);
    Optional<Meter> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
    List<Meter> findByZoneId(String zoneId);
    List<Meter>findByCustomerId(String customerId);
}
