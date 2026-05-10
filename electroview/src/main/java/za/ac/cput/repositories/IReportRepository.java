package za.ac.cput.repositories;

import za.ac.cput.domain.Report;
import za.ac.cput.Enums.ReportStatus;

import java.util.List;

public interface IReportRepository extends Repository<Report, String> {

    List<Report> findByRequestedBy(String userId);

    List<Report> findByStatus(ReportStatus status);

    List<Report> findByZoneId(String zoneId);
}