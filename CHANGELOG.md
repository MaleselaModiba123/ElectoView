# CHANGELOG

## [1.0.0] — May 2026

### Added

#### Domain Model
- `User.java` — User entity with account lockout after 5 failed login attempts,
  role management, and password reset validation
- `Zone.java` — Distribution zone with load percentage computation and
  configurable anomaly detection thresholds
- `Meter.java` — Smart meter with consecutive rejection fault detection
  and reading history
- `Reading.java` — Meter reading with validation and threshold comparison
- `Anomaly.java` — Anomaly lifecycle management including assignment,
  escalation, resolution, and auto-resolution
- `Report.java` — Report request with inline vs background processing
  detection and 24-hour expiry lifecycle
- `DailySummary.java` — Pre-aggregated daily zone consumption summary

#### Enumerations
- `Role` — ADMINISTRATOR, ANALYST, CONSUMER, TECHNICIAN, EXECUTIVE
- `AccountStatus` — PENDING, ACTIVE, LOCKED, INACTIVE
- `MeterStatus` — REGISTERED, ACTIVE, OFFLINE, FAULT_SUSPECTED,
  FAULT_CONFIRMED, UNDER_MAINTENANCE, DECOMMISSIONED
- `ZoneStatus` — CONFIGURED, NORMAL, HIGH_LOAD, ANOMALY_ALERT, INACTIVE
- `ReadingStatus` — RECEIVED, VALIDATING, PERSISTED, REJECTED
- `AnomalyStatus` — OPEN, IN_PROGRESS, ESCALATED, RESOLVED, AUTO_RESOLVED
- `ReportFormat` — PDF, CSV
- `ReportStatus` — REQUESTED, QUEUED, GENERATING, READY, FAILED, EXPIRED
- `ThresholdType` — ABSOLUTE, RELATIVE

#### Repositories
- `UserRepository` — findByEmail, existsByEmail, findByRole, findByStatus
- `ZoneRepository` — findByStatus, existsByName
- `MeterRepository` — findByZoneId, findByStatus, findBySerialNumber,
  findByConsumerId, existsBySerialNumber
- `ReadingRepository` — findByMeterId, findByMeterIdAndRecordedAtBetween,
  findByAnomalyTrue
- `AnomalyRepository` — findByMeterId, findByStatus, countByStatus,
  findByAssignedTo
- `ReportRepository` — findByRequestedBy, findByStatus, findByZoneId
- `DailySummaryRepository` — findByZoneId, findByZoneIdAndSummaryDate,
  findByZoneIdAndSummaryDateBetween

#### Service Layer
- `UserService` — Account creation, login tracking, lockout, role changes
- `ZoneService` — Zone creation, threshold configuration, status updates
- `MeterService` — Meter registration, activation, consumer assignment
- `ReadingService` — Ingests readings, validates, triggers anomaly detection
- `AnomalyService` — Creates, assigns, resolves, and escalates anomalies
- `ReportService` — Inline and background report generation
- `DailySummaryService` — Aggregates zone readings into daily summaries

#### REST Controllers
- `UserController` — `/api/users/**`
- `ZoneController` — `/api/zones/**`
- `MeterController` — `/api/meters/**`
- `ReadingController` — `/api/readings/**`
- `AnomalyController` — `/api/anomalies/**`
- `ReportController` — `/api/reports/**`
- `DashboardController` — `/api/dashboard/**`

#### Creational Design Patterns
- **Simple Factory** — `NotificationFactory` creates typed notification
  objects centrally, eliminating scattered instantiation across the codebase
- **Factory Method** — `ReportExporter` abstract class with `PdfReportExporter`
  and `CsvReportExporter` subclasses delegating file creation
- **Abstract Factory** — `DashboardComponentFactory` interface with
  `AdminDashboardFactory` and `ConsumerDashboardFactory` producing
  role-specific component families
- **Builder** — `ReportRequest.Builder` constructs complex report parameters
  with mandatory validation and optional field chaining
- **Prototype** — `ZoneTemplateCache` stores and clones pre-configured
  RESIDENTIAL, INDUSTRIAL, and MIXED_USE zone templates
- **Singleton** — `DatabaseConnectionManager` with thread-safe
  double-checked locking and connection pool management

#### Tests
- `UserServiceTest` — 9 tests
- `ZoneServiceTest` — 7 tests
- `MeterServiceTest` — 6 tests
- `AnomalyServiceTest` — 9 tests
- `ReportServiceTest` — 6 tests
- `NotificationFactoryTest` — 9 tests
- `ReportExporterTest` — 8 tests
- `DashboardComponentFactoryTest` — 7 tests
- `ReportRequestBuilderTest` — 7 tests
- `ZonePrototypeTest` — 7 tests
- `DatabaseConnectionManagerTest` — 8 tests
- **Total: 83 tests — 0 failures**

### Technical Decisions
- Used `@Getter`, `@Setter`, `@NoArgsConstructor`, and `@Builder` from
  Lombok instead of `@Data` to avoid constructor conflicts with JPA
- Used `@AfterEach` instead of `@BeforeEach` in singleton tests to prevent
  IllegalStateException on first test run
- Used `thenAnswer(invocation -> invocation.getArgument(0))` in Mockito
  tests where the service mutates the object before saving
- MySQL configured with `ddl-auto=update` so Hibernate manages the schema
  automatically during development
- Spring Security disabled in `application.properties` during development
  to allow unrestricted API access while building the core features

## [1.2.0] — 17 May 2026 — Service Layer and REST API

### Added

#### Service Layer
- Business logic encapsulated in `UserService`, `ZoneService`, `MeterService`
  (plus 4 more), each using the repository layer for persistence
- Input validation and business rule enforcement (duplicate email check,
  duplicate serial number check, threshold positivity check)

#### REST API
- Full CRUD REST endpoints for Users, Zones, and Meters (plus 4 more entities)
- Consistent HTTP status codes: 201 Created, 200 OK, 400 Bad Request, 404 Not Found
- `GlobalExceptionHandler` converts `IllegalArgumentException` into clean
  400 Bad Request responses across the entire API

#### API Documentation
- Integrated SpringDoc OpenAPI 2.3.0 / Swagger UI
- `OpenApiConfig` defines API metadata (title, description, version, contact)
- All three core controllers annotated with `@Tag`, `@Operation`, and
  `@ApiResponses` documenting every endpoint and its possible error responses
- Swagger UI available at `/swagger-ui.html`

#### Integration Tests
- `UserControllerIntegrationTest` — 5 tests (full HTTP stack, real H2 DB)
- `ZoneControllerIntegrationTest` — 5 tests
- `MeterControllerIntegrationTest` — 6 tests
- Tests run against an in-memory H2 database, isolated from production MySQL
- Apache HttpClient 5 wired in for PATCH method support in TestRestTemplate

### Fixed
- `createdAt` / timestamp fields no longer null on insert — set explicitly
  in entity constructors so `nullable = false` constraints are satisfied
- 500 Internal Server Error on duplicate-email creation now correctly
  returns 400 Bad Request via the global exception handler

### Technical Decisions
- Used `@SpringBootTest(webEnvironment = RANDOM_PORT)` with `@LocalServerPort`
  so integration tests hit the real running server on a random free port
- Separate `src/test/resources/application.properties` points tests at H2
  so the production MySQL database is never touched during testing