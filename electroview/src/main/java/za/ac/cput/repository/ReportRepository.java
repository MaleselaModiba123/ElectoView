package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.domain.Report;
import za.ac.cput.Enums.ReportStatus;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    List<Report> findByRequestedBy(String userId);

    List<Report> findByStatus(ReportStatus status);

    List<Report> findByZoneId(String zoneId);
}