package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Meter;
import za.ac.cput.Enums.MeterStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, String> {

    List<Meter> findByZoneId(String zoneId);

    List<Meter> findByStatus(MeterStatus status);

    Optional<Meter> findBySerialNumber(String serialNumber);

    List<Meter> findByConsumerId(String consumerId);

    boolean existsBySerialNumber(String serialNumber);
}