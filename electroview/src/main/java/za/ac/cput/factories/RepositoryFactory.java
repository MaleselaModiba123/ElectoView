package za.ac.cput.factories;

import za.ac.cput.repositories.inmemory.*;
import za.ac.cput.repositories.*;
import za.ac.cput.repositories.filesystem.FileSystemUserRepository;
import za.ac.cput.factories.StorageType;

public class RepositoryFactory {
    private RepositoryFactory() {
       
    }

    public static IUserRepository getUserRepository(StorageType type) {
    return switch (type) {
        case IN_MEMORY   -> new InMemoryUserRepository();
        case FILE_SYSTEM -> new FileSystemUserRepository("data/users.json");
        case DATABASE    -> throw new UnsupportedOperationException(
                "Use Spring's @Autowired UserRepository for JPA-backed storage.");
    };
}
    public static IZoneRepository getZoneRepository(StorageType type) {
        return switch (type) {
            case IN_MEMORY   -> new InMemoryZoneRepository();
            case FILE_SYSTEM -> throw new UnsupportedOperationException(
                    "FileSystemZoneRepository not yet implemented.");
            case DATABASE    -> throw new UnsupportedOperationException(
                    "Use Spring's @Autowired ZoneRepository for JPA-backed storage.");
        };
    }

    public static IMeterRepository getMeterRepository(StorageType type) {
        return switch (type) {
            case IN_MEMORY   -> new InMemoryMeterRepository();
            case FILE_SYSTEM -> throw new UnsupportedOperationException(
                    "FileSystemMeterRepository not yet implemented.");
            case DATABASE    -> throw new UnsupportedOperationException(
                    "Use Spring's @Autowired MeterRepository for JPA-backed storage.");
        };
    }
     public static IReadingRepository getReadingRepository(StorageType type) {
        return switch (type) {
            case IN_MEMORY   -> new InMemoryReadingRepository();
            case FILE_SYSTEM -> throw new UnsupportedOperationException(
                    "FileSystemReadingRepository not yet implemented.");
            case DATABASE    -> throw new UnsupportedOperationException(
                    "Use Spring's @Autowired ReadingRepository for JPA-backed storage.");
        };
    }

    public static IAnomalyRepository getAnomalyRepository(StorageType type) {
        return switch (type) {
            case IN_MEMORY   -> new InMemoryAnomalyRepository();
            case FILE_SYSTEM -> throw new UnsupportedOperationException(
                    "FileSystemAnomalyRepository not yet implemented.");
            case DATABASE    -> throw new UnsupportedOperationException(
                    "Use Spring's @Autowired AnomalyRepository for JPA-backed storage.");
        };
    }

    public static IReportRepository getReportRepository(StorageType type) {
        return switch (type) {
            case IN_MEMORY   -> new InMemoryReportRepository();
            case FILE_SYSTEM -> throw new UnsupportedOperationException(
                    "FileSystemReportRepository not yet implemented.");
            case DATABASE    -> throw new UnsupportedOperationException(
                    "Use Spring's @Autowired ReportRepository for JPA-backed storage.");
        };
    }

    public static IDailySummaryRepository getDailySummaryRepository(StorageType type) {
        return switch (type) {
            case IN_MEMORY   -> new InMemoryDailySummaryRepository();
            case FILE_SYSTEM -> throw new UnsupportedOperationException(
                    "FileSystemDailySummaryRepository not yet implemented.");
            case DATABASE    -> throw new UnsupportedOperationException(
                    "Use Spring's @Autowired DailySummaryRepository for JPA-backed storage.");
        };
    }
}
