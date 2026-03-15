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
C4Context
    title System Context — ElectroSense
 
    Person(admin, "Administrator", "Utility manager who monitors zones, manages users, and responds to anomalies")
    Person(analyst, "Analyst", "Reviews consumption trends and generates reports")
    Person(consumer, "Consumer", "Views their personal electricity usage data")
 
    System(electroview, "ElectroView", "Web-based analytics dashboard for electricity usage monitoring, anomaly detection, and reporting")
 
    System_Ext(meterSim, "Meter Data Simulator", "Simulates smart meter readings submitted via REST API (dev/demo environment)")
    System_Ext(emailService, "Email Notification Service", "Sends anomaly alert emails to administrators (e.g., SendGrid)")
    System_Ext(authProvider, "Authentication Service", "JWT-based auth; future integration with municipal LDAP/SSO")
 
    Rel(admin, electroview, "Manages zones, users, anomalies", "HTTPS/Browser")
    Rel(analyst, electroview, "Views dashboards and exports reports", "HTTPS/Browser")
    Rel(consumer, electroview, "Views personal usage data", "HTTPS/Browser")
    Rel(meterSim, electroview, "Submits meter readings", "REST API / HTTPS")
    Rel(electroview, emailService, "Sends anomaly alert emails", "SMTP/API")
    Rel(electroview, authProvider, "Validates user tokens", "HTTPS")
```
 
---
 
## 4.2 Level 2 — Container Diagram
 
> Breaks down ElectroView into its deployable containers and their interactions.
 
```mermaid
C4Container
    title Container Diagram — ElectroView
 
    Person(admin, "Administrator")
    Person(analyst, "Analyst")
    Person(consumer, "Consumer")
 
    System_Boundary(electroview, "ElectroView System") {
 
        Container(webApp, "React Web Application", "React + TypeScript", "Single-page application providing dashboards, charts, anomaly alerts, and report generation UI. Served via CDN.")
 
        Container(apiServer, "Backend REST API", "Node.js + Express", "Handles all business logic: data ingestion, authentication, anomaly detection, report generation. Exposes REST endpoints.")
 
        Container(db, "Primary Database", "PostgreSQL", "Stores users, zones, meters, time-series consumption readings, anomaly logs, and reports.")
 
        Container(cache, "Cache Layer", "Redis", "Caches aggregated consumption summaries and session tokens to reduce DB load on dashboard queries.")
 
        Container(jobRunner, "Background Job Runner", "Node.js + node-cron", "Scheduled jobs: runs anomaly detection checks, aggregates hourly/daily summaries, cleans old data.")
 
        Container(reportEngine, "Report Generator", "Node.js + PDFKit / csv-writer", "Generates PDF and CSV reports on demand from aggregated data.")
    }
 
    System_Ext(meterSim, "Meter Data Simulator")
    System_Ext(emailService, "Email Notification Service")
 
    Rel(admin, webApp, "Uses", "HTTPS")
    Rel(analyst, webApp, "Uses", "HTTPS")
    Rel(consumer, webApp, "Uses", "HTTPS")
 
    Rel(webApp, apiServer, "Calls API", "REST/JSON over HTTPS")
    Rel(apiServer, db, "Reads/Writes", "SQL over TCP")
    Rel(apiServer, cache, "Reads/Writes cached data", "Redis protocol")
    Rel(apiServer, reportEngine, "Requests report generation", "Internal function call")
    Rel(jobRunner, db, "Reads/Writes aggregated summaries", "SQL")
    Rel(jobRunner, emailService, "Sends anomaly alert emails", "SMTP/API")
    Rel(meterSim, apiServer, "POSTs meter readings", "REST/JSON")
    Rel(reportEngine, db, "Queries data for reports", "SQL")
```
 
---
 
## 4.3 Level 3 — Component Diagram (Backend REST API)
 
> Breaks down the internal components of the Backend REST API container.
 
```mermaid
C4Component
    title Component Diagram — Backend REST API (Node.js/Express)
 
    Container_Boundary(apiServer, "Backend REST API") {
 
        Component(authController, "Auth Controller", "Express Router", "Handles user login, registration, JWT token issuance and validation. Exposes /api/auth/* routes.")
 
        Component(meterController, "Meter Data Controller", "Express Router", "Accepts incoming meter readings from simulators/devices. Validates and persists readings. Exposes /api/meters/* routes.")
 
        Component(dashboardController, "Dashboard Controller", "Express Router", "Serves aggregated consumption summaries for zones and households. Exposes /api/dashboard/* routes.")
 
        Component(anomalyController, "Anomaly Controller", "Express Router", "Returns anomaly logs and allows admins to update anomaly status. Exposes /api/anomalies/* routes.")
 
        Component(reportController, "Report Controller", "Express Router", "Accepts report requests with filters, triggers report engine, returns file download. Exposes /api/reports/* routes.")
 
        Component(anomalyDetector, "Anomaly Detection Service", "Service Module", "Compares incoming readings against per-zone thresholds. Creates anomaly records and triggers email notifications if threshold is breached.")
 
        Component(aggregationService, "Data Aggregation Service", "Service Module", "Computes hourly, daily, monthly consumption sums and averages from raw readings. Used by dashboard and report controllers.")
 
        Component(authMiddleware, "Auth Middleware", "Express Middleware", "Validates JWT tokens on protected routes. Enforces role-based access control (Admin, Analyst, Consumer).")
    }
 
    ContainerDb(db, "PostgreSQL Database")
    ContainerDb(cache, "Redis Cache")
    System_Ext(emailService, "Email Notification Service")
    Container(reportEngine, "Report Generator")
 
    Rel(meterController, anomalyDetector, "Triggers anomaly check after each reading")
    Rel(anomalyDetector, db, "Writes anomaly records")
    Rel(anomalyDetector, emailService, "Sends alert emails")
 
    Rel(dashboardController, aggregationService, "Requests consumption summaries")
    Rel(aggregationService, cache, "Reads/Writes aggregated data")
    Rel(aggregationService, db, "Queries raw readings")
 
    Rel(meterController, db, "Writes meter readings")
    Rel(authController, db, "Reads/Writes user records")
    Rel(reportController, reportEngine, "Requests PDF/CSV generation")
    Rel(anomalyController, db, "Reads anomaly logs")
 
    Rel(authMiddleware, authController, "Validates tokens")
```
 
---
 
## 4.4 Level 4 — Code / Data Model (Core Entities)
 
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