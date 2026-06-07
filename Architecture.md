# Architecture — ElectroView: Electricity Usage Analytics Dashboard

## 1. Project Title - ElectroView

## 2. Domain
 
**Domain: Electricity Distribution & Management**
The electrcity distribution and management domain encompasses the generation, distribution, metering, monitoring, and billing of electrical power to residential, commercial, and municipal consumers. In South Africa, electricity distribution is handled by entities such as Eskom and local municipalities which manage large-scale infrastructure including substations, distribution networks, and smart metering systems.
 
This domain is increasingly driven by digital transformation — smart meters, IoT sensors, and SCADA systems generate vast volumes of consumption data that require analytics platforms to derive actionable insights, detect waste or faults, and enable demand-side management.

---
 
## 3. Problem Statement

The problem is that many municipalities in South Africa are struggling to provide uninterrupted electricity to their residents due to infrastructure challenges such as energy dissipation during transmission and distribution, theft and vandalism, meter tampering, and illegal connections.

**ElectroView** addresses these problems by providing a centralised, web-based analytics dashboard that aggregates meter data, visualises consumption trends, detects anomalies, and generates exportable reports enabling both utility managers and consumers to make data-driven decisions.

---

## 4. C4 Architecture Diagrams
 
The C4 model describes software architecture at four levels of abstraction:
- **Level 1 — System Context:** Who uses the system and what external systems does it interact with?
- **Level 2 — Container:** What are the deployable units (apps, databases, services)?
- **Level 3 — Component:** What are the major components inside each container?
- **Level 4 — Code:** Key class/data model detail for the most critical component.
 
---

## 4.1 Level 1 — System Context Diagram
 
> Shows ElectroView in the context of its users and external systems.
 
```mermaid
graph TB
    admin["Administrator<br/>(manages users, zones, meters)"]
    analyst["Grid Analyst<br/>(monitors consumption, resolves anomalies)"]
    consumer["Consumer<br/>(views own usage)"]
    technician["Technician<br/>(resolves meter faults)"]

    electroview["ElectroView System<br/>Electricity Usage Analytics Dashboard"]

    meters["Smart Meters<br/>(send consumption readings)"]
    mysql["MySQL Database<br/>(stores all data)"]

    admin --> electroview
    analyst --> electroview
    consumer --> electroview
    technician --> electroview
    meters --> electroview
    electroview --> mysql
```

The system serves four human actor types plus automated smart meter input,
and persists everything to a MySQL database.
 
---
 
## 4.2 Level 2 — Container Diagram
 
> Breaks down ElectroView into its deployable containers and their interactions.
 
```mermaid
graph TB
    subgraph client["Client Layer"]
        swagger["Swagger UI<br/>(API documentation & testing)"]
        future["Future React Frontend<br/>(planned)"]
    end

    subgraph app["ElectroView Spring Boot Application"]
        api["REST API<br/>(Spring MVC Controllers)"]
        services["Service Layer<br/>(Business Logic)"]
        repos["Repository Layer<br/>(Spring Data JPA)"]
        patterns["Creational Patterns<br/>(Factories, Builder, Singleton)"]
    end

    db["MySQL Database"]

    swagger --> api
    future -.-> api
    api --> services
    services --> repos
    services --> patterns
    repos --> db
```

The application is a single deployable Spring Boot JAR. Clients communicate
over HTTP/JSON. The creational patterns support the service layer (e.g.,
the NotificationFactory and ReportExporter).
 
---
 
## 4.3 Level 3 — Component Diagram (Backend REST API)
 
> Breaks down the internal components of the Backend REST API container.
 
```mermaid
graph TB
    subgraph controllers["Controller Layer"]
        uc["UserController"]
        zc["ZoneController"]
        mc["MeterController"]
        rc["ReadingController"]
        ac["AnomalyController"]
        rpc["ReportController"]
        dc["DashboardController"]
    end

    subgraph svc["Service Layer"]
        us["UserService"]
        zs["ZoneService"]
        ms["MeterService"]
        rs["ReadingService"]
        as["AnomalyService"]
        rps["ReportService"]
        dss["DailySummaryService"]
    end

    subgraph repo["Repository Layer"]
        ur["UserRepository"]
        zr["ZoneRepository"]
        mr["MeterRepository"]
        rr["ReadingRepository"]
        ar["AnomalyRepository"]
        rpr["ReportRepository"]
        dsr["DailySummaryRepository"]
    end

    eh["GlobalExceptionHandler<br/>(@RestControllerAdvice)"]

    uc --> us
    zc --> zs
    mc --> ms
    rc --> rs
    ac --> as
    rpc --> rps
    dc --> dss
    dc --> ms

    us --> ur
    zs --> zr
    ms --> mr
    rs --> rr
    rs --> as
    as --> ar
    rps --> rpr
    dss --> dsr
    dss --> rr

    controllers -.exceptions.-> eh
```

Note the cross-service dependency: `ReadingService` calls `AnomalyService`
when a reading breaches its zone threshold — this is the anomaly detection
pipeline. The `GlobalExceptionHandler` intercepts exceptions thrown by any
controller and converts them into clean HTTP error responses.
 
---
 
## 4.4 Level 4 — Code (Persistence Abstraction)
 
> Key data model for the most critical persistence layer — the PostgreSQL database schema.
 
```mermaid
erDiagram
    USERS {
        uuid id PK
        string email
        string password_hash
        string role
        boolean is_active
        timestamp created_at
    }
 
    ZONES {
        uuid id PK
        string name
        string description
        string location
        float threshold_kwh
        timestamp created_at
    }
 
    METERS {
        uuid id PK
        uuid zone_id FK
        uuid consumer_id FK
        string meter_serial
        boolean is_active
        timestamp installed_at
    }
 
    READINGS {
        uuid id PK
        uuid meter_id FK
        float kwh_consumed
        timestamp recorded_at
    }
 
    ANOMALIES {
        uuid id PK
        uuid meter_id FK
        uuid reading_id FK
        float threshold_at_time
        float actual_value
        string status
        timestamp detected_at
        timestamp resolved_at
    }
 
    DAILY_SUMMARIES {
        uuid id PK
        uuid zone_id FK
        date summary_date
        float total_kwh
        float avg_kwh
        float peak_kwh
    }
 
    USERS ||--o{ METERS : "assigned to"
    ZONES ||--o{ METERS : "contains"
    METERS ||--o{ READINGS : "generates"
    READINGS ||--o| ANOMALIES : "may trigger"
    METERS ||--o{ ANOMALIES : "associated with"
    ZONES ||--o{ DAILY_SUMMARIES : "aggregated into"
```
 
---
 
## 5. End-to-End System Flow
 
The following sequence illustrates the full data flow from meter reading to dashboard display and anomaly alert.
 
```mermaid
sequenceDiagram
    participant Simulator as Meter Simulator
    participant API as REST API
    participant DB as PostgreSQL
    participant Detector as Anomaly Service
    participant Email as Email Service
    participant Cache as Redis
    participant UI as React Dashboard
 
    Simulator->>API: POST /api/meters/reading {meterId, kWh, timestamp}
    API->>DB: INSERT INTO readings
    API->>Detector: checkAnomaly(reading)
    Detector->>DB: SELECT zone threshold
    alt Reading exceeds threshold
        Detector->>DB: INSERT INTO anomalies
        Detector->>Email: sendAlert(adminEmail, anomalyDetails)
    end
    API-->>Simulator: 201 Created
 
    UI->>API: GET /api/dashboard/zone/:id
    API->>Cache: GET zone_summary:zoneId
    alt Cache miss
        API->>DB: SELECT aggregated readings
        API->>Cache: SET zone_summary:zoneId
    end
    API-->>UI: Return consumption summary JSON
    UI->>UI: Render charts and KPIs
```
 
---
 
## 6. Deployment Architecture
 
```mermaid
graph TD
    subgraph Client
        Browser["Browser (React SPA)"]
    end
 
    subgraph Cloud["Cloud / VPS Deployment (Docker Compose)"]
        Nginx["Nginx Reverse Proxy"]
        API["Node.js API Container"]
        Jobs["Job Runner Container"]
        PG["PostgreSQL Container"]
        Redis["Redis Container"]
        Reports["Report Engine (Module in API)"]
    end
 
    subgraph External
        Email["SendGrid / Email API"]
        CDN["CDN (Static Assets)"]
    end
 
    Browser --> CDN
    Browser --> Nginx
    Nginx --> API
    API --> PG
    API --> Redis
    API --> Reports
    Jobs --> PG
    Jobs --> Email
    API --> Email
```
