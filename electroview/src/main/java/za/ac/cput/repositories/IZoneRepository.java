package za.ac.cput.repositories;

import za.ac.cput.domain.Zone;
import za.ac.cput.Enums.ZoneStatus;

import java.util.List;


public interface IZoneRepository extends Repository<Zone, String> {
    List<Zone>findByStatus(ZoneStatus status);

    boolean existsByName(String name);
    
}
