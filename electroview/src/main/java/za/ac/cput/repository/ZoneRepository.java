package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Zone;
import za.ac.cput.Enums.ZoneStatus;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, String> {

    List<Zone> findByStatus(ZoneStatus status);

    boolean existsByName(String name);
}