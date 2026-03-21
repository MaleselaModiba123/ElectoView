# SRD.md - ElectroView: Electircity Usage Analytics dashboard

---
## 1. Functional Requirements

---
 
### FR-01: User Authentication and Role-Based Access
 
**Statement:** The system shall authenticate users via email and password, issue a JWT upon successful login, and enforce role-based access control for all protected routes.
 
**Stakeholders:** IT Admin, all roles  
**Acceptance Criteria:**
- Login succeeds within 2 seconds for valid credentials.
- Three roles are enforced: Administrator, Analyst, Consumer.
- An unauthenticated request to any protected endpoint returns HTTP 401.
- A Consumer attempting to access an admin endpoint receives HTTP 403.
- JWT tokens expire after 8 hours; refresh tokens are valid for 7 days.
 
---
 
### FR-02: Meter Reading Ingestion via REST API
 
**Statement:** The system shall accept meter consumption readings submitted as JSON payloads to a REST API endpoint, validate the payload, and persist the reading to the database.
 
**Stakeholders:** Electrcity Disribution Manager, Electricty Network Analyst, Technician  
**Acceptance Criteria:**
- Endpoint accepts POST `/api/meters/readings` with fields: `meterId`, `kwhConsumed`, `recordedAt`.
- Invalid payloads (missing fields, negative kWh values, invalid timestamps) return HTTP 400 with a descriptive error.
- Valid readings are persisted within 500ms of receipt.
- The system handles batch submissions of up to 500 readings in a single request.
 
---
 
### FR-03: Real-Time Zone Consumption Dashboard
 
**Statement:** The system shall display a live overview dashboard showing current consumption (kWh), load percentage, and status (Normal / High Load / Anomaly) for each distribution zone.
 
**Stakeholders:** Eelctrcity Distribution Manager, Municipal Executive  
**Acceptance Criteria:**
- Dashboard displays all active zones with consumption figures updated every 5 minutes.
- Each zone row shows: zone name, total kWh consumed today, load as a percentage of zone capacity, and a colour-coded status indicator.
- Zones with load above 90% are automatically flagged as "High Load."
- Dashboard loads within 2 seconds on a standard broadband connection.
 
---
 
### FR-04: Historical Consumption Trend Visualisation
 
**Statement:** The system shall render line charts showing consumption trends for selected zones over user-selectable time ranges (24 hours, 7 days, 30 days).
 
**Stakeholders:** Electricity Network Analyst, Electrcity Distribution Manager  
**Acceptance Criteria:**
- The user can select one or more zones to overlay on the same chart.
- Time range selector supports: 24h (hourly), 7d (daily), 30d (weekly aggregates).
- Chart renders within 2 seconds of filter selection.
- Y-axis units are labelled in kWh; X-axis labels reflect the selected time range.
 
---
 
### FR-05: Anomaly Detection and Alerting
 
**Statement:** The system shall automatically detect meter readings that exceed a configurable per-zone threshold and generate an anomaly record, an in-app alert notification, and an email to the responsible administrator.
 
**Stakeholders:** Electrcity Distribution Manager, Technician  
**Acceptance Criteria:**
- Each zone has a configurable threshold (kWh per reading interval); default is 120% of the zone's 30-day rolling average.
- An anomaly record is created within 10 seconds of the offending reading being ingested.
- The in-app notification appears in the admin notification panel without requiring a page refresh.
- An alert email is sent to the designated administrator within 2 minutes of detection.
- The anomaly log records: meter ID, zone, reading value, threshold at time of detection, and timestamp.
 
---
 
### FR-06: Anomaly Management and Resolution Workflow
 
**Statement:** The system shall provide administrators and meter technicians with the ability to view the anomaly log, update anomaly status, and record resolution notes.
 
**Stakeholders:** Electrcity Distribution Manager, Technician  
**Acceptance Criteria:**
- Anomaly log is filterable by: zone, status (Open / In Progress / Resolved), and date range.
- Each anomaly record can be updated with a status change and a free-text resolution note.
- Status transitions are logged with a timestamp and the user who made the change.
- Resolved anomalies are retained in the log for at least 12 months.
 
---
 
### FR-07: Consumer Personal Usage View
 
**Statement:** The system shall provide consumers with a personalised dashboard showing their meter's consumption history, a comparison to the previous billing period, and the ability to set a monthly usage budget with threshold alerts.
 
**Stakeholders:** Consumer  
**Acceptance Criteria:**
- Consumer dashboard is scoped exclusively to their own meter — no other meter data is accessible.
- Consumption history displays daily breakdowns for the current and previous billing month.
- The previous-period comparison shows the percentage change in total kWh consumed.
- Consumer can set a monthly kWh budget; the system sends an in-app alert when 80% and 100% of the budget is reached.
 
---
 
### FR-08: Report Generation with Export
 
**Statement:** The system shall enable analysts and billing staff to generate consumption reports filtered by date range, zone, and meter, and export them in PDF and CSV formats.
 
**Stakeholders:** Electrcity Network Analyst, Finance Department Staff  
**Acceptance Criteria:**
- Report parameters: start date, end date (mandatory); zone (optional); meter ID (optional).
- Generated reports include: total kWh per meter, daily averages, peak consumption, and any anomalies in the period.
- CSV export conforms to a schema compatible with SAP-based billing systems (column headers: `meter_id`, `zone`, `date`, `kwh_consumed`, `anomaly_flag`).
- PDF report is generated within 10 seconds for a 30-day date range covering up to 500 meters.
- Report files are available for download for 24 hours after generation.
 
---
 
### FR-09: User and Meter Management (Admin)
 
**Statement:** The system shall provide administrators with a management interface to create, update, deactivate, and assign roles to user accounts, and to register, update, and deactivate meter records.
 
**Stakeholders:** IT Admin, System Administrator  
**Acceptance Criteria:**
- Administrators can create a new user account with role assignment in under 3 clicks.
- Deactivated users cannot log in; their historical data is retained.
- Meter records include: serial number, zone assignment, installation date, and active status.
- All user and meter changes are recorded in an audit log with timestamp and acting administrator.
 
---
 
### FR-10: Zone Threshold Configuration
 
**Statement:** The system shall allow administrators to configure per-zone anomaly detection thresholds, either as an absolute kWh value or as a percentage of the rolling average.
 
**Stakeholders:** Electrcity Distribution Manager, IT Admin  
**Acceptance Criteria:**
- Threshold configuration is accessible from the zone management interface.
- Administrators can select threshold type: Absolute (fixed kWh) or Relative (% of 30-day average).
- Changes to thresholds take effect immediately for subsequent readings.
- Previous threshold values are stored in an audit history per zone.
 
---
 
### FR-11: Executive KPI Summary Dashboard
 
**Statement:** The system shall provide a read-only executive summary view displaying high-level KPIs: total grid consumption today, week-on-week trend, number of active anomalies, and overall infrastructure utilisation.
 
**Stakeholders:** Municipal Executive  
**Acceptance Criteria:**
- KPI summary is accessible to the Administrator role and a dedicated Executive role.
- All KPIs are refreshed on page load with data no older than 24 hours.
- The view is print-friendly and exportable as a single-page PDF.
- No raw meter data or individual consumer data is visible in this view.
 
---
 
### FR-12: Bulk Historical Data Import
 
**Statement:** The system shall accept bulk uploads of historical meter readings via CSV file to enable back-population of the database for trend analysis.
 
**Stakeholders:** Electricity Network Analyst, IT Admin  
**Acceptance Criteria:**
- CSV format is validated on upload; rows with invalid data are rejected with per-row error messages.
- Duplicate readings (same meter ID + timestamp) are detected and skipped without failing the entire import.
- Import progress is displayed in real time; the user is notified on completion with a summary (rows imported / rows skipped / rows failed).
- Maximum supported file size: 50 MB per upload.
 
---

## 2. Non-Functional Requirements
 
---
 
### 2.1 Usability
 
**NFR-U01:** The dashboard interface shall comply with WCAG 2.1 Level AA accessibility standards, including sufficient colour contrast (minimum 4.5:1 ratio for normal text), keyboard navigability, and screen-reader-compatible ARIA labels on all interactive elements.  
*Stakeholder:* All users  
*Rationale:* Municipal systems must be accessible to staff with visual impairments or assistive technology needs.
 
**NFR-U02:** A new user with no prior training shall be able to complete the three core tasks — view zone consumption, generate a report, and resolve an anomaly — within 10 minutes of first login, guided only by the in-system UI.  
*Stakeholder:* Technician, Finance Staff  
*Rationale:* Field technicians and Finance staff should not require formal training sessions.
 
**NFR-U03:** The consumer dashboard shall present all consumption data using plain-language labels and avoid technical jargon. All chart axes shall include units. Tooltips shall explain what each metric means in one sentence.  
*Stakeholder:* Consumer  
*Rationale:* Consumers are non-technical end users who must interpret the system without specialist knowledge.
 
---
 
### 2.2 Deployability
 
**NFR-D01:** The complete system (frontend, backend API, PostgreSQL, Redis) shall be deployable on any Linux server (Ubuntu 22.04 LTS or later) using a single `docker-compose up` command, requiring no manual environment configuration beyond providing a `.env` file.  
*Stakeholder:* IT Admin  
*Rationale:* Reduces deployment complexity and enables repeatable, environment-consistent deployments.
 
**NFR-D02:** The system shall support deployment on both on-premise servers and major cloud platforms (AWS, Azure, GCP) without code changes. All environment-specific values shall be injected via environment variables.  
*Stakeholder:* IT Admin, Municipal Executive  
*Rationale:* South African municipalities vary in their infrastructure maturity; some manage on-premise, others use cloud.
 
**NFR-D03:** The React frontend shall be deployable as a static bundle served via any CDN or web server (Nginx, Apache) without a Node.js runtime on the serving host.  
*Stakeholder:* IT Admin  
*Rationale:* Decouples frontend delivery from backend infrastructure and simplifies scaling.
 
---
 
### 2.3 Maintainability
 
**NFR-M01:** The codebase shall achieve a minimum test coverage of 70% for all backend service modules (anomaly detection, aggregation, report generation), measured by a CI-integrated test runner (Jest).  
*Stakeholder:* IT Admin  
*Rationale:* Ensures regressions are caught before deployment and reduces maintenance burden over time.
 
**NFR-M02:** All REST API endpoints shall be documented in an OpenAPI 3.0 specification (`/docs/api`), accessible via a Swagger UI at `/api/docs` in development mode.  
*Stakeholder:* IT Admin, Electricty Network Analyst (future integrations)  
*Rationale:* Enables third-party integration and reduces onboarding time for new developers.
 
**NFR-M03:** The system shall use database migrations (via a tool such as Flyway or Knex migrations) for all schema changes. No manual SQL shall be required to update the schema during upgrades.  
*Stakeholder:* IT Admin  
*Rationale:* Ensures safe, versioned, and reversible database evolution in production environments.
 
---

### 2.4 Scalability
 
**NFR-S01:** The backend API shall support a minimum of 1,000 concurrent users during peak hours without degrading response times beyond the performance thresholds defined in NFR-P01 and NFR-P02.  
*Stakeholder:* Electrcity Distribution Manager, IT Admin  
*Rationale:* A large municipality may have thousands of consumers and hundreds of staff simultaneously active during morning peak periods.
 
**NFR-S02:** The meter data ingestion endpoint shall handle a sustained throughput of 500 meter reading submissions per second without data loss or HTTP 5xx errors.  
*Stakeholder:* Electrcity Distribution Manager, Electrcity Network Analyst  
*Rationale:* A large-scale smart meter rollout (e.g., 50,000 meters on 1-minute polling) generates high-frequency write loads.
 
**NFR-S03:** The system architecture shall support horizontal scaling of the API layer via containerised instances behind a load balancer, with no shared in-process state (all session state managed via Redis).  
*Stakeholder:* IT Admin  
*Rationale:* Enables the system to scale out under load without requiring a redesign of the application layer.
 
---
 
### 2.5 Security
 
**NFR-SEC01:** All data in transit between the browser, API, and database shall be encrypted using TLS 1.2 or higher. HTTP requests shall be automatically redirected to HTTPS.  
*Stakeholder:* IT Admin, all users  
*Rationale:* Consumption data and user credentials must not be transmitted in plaintext.
 
**NFR-SEC02:** All passwords shall be hashed using bcrypt with a minimum cost factor of 12 before storage. Plaintext passwords shall never be logged or persisted.  
*Stakeholder:* IT Admin  
*Rationale:* Protects user credentials in the event of a database breach.
 
**NFR-SEC03:** The system shall implement rate limiting on the authentication endpoint of no more than 10 login attempts per IP address per minute. Accounts shall be temporarily locked after 5 consecutive failed attempts.  
*Stakeholder:* IT Admin  
*Rationale:* Mitigates brute-force credential attacks against the login endpoint.
 
**NFR-SEC04:** All sensitive data at rest in the PostgreSQL database (including personal consumer information: email, address) shall be encrypted using AES-256 at the column level.  
*Stakeholder:* IT Admin, Consumer  
*Rationale:* Complies with POPIA (Protection of Personal Information Act) requirements applicable to South African organisations storing personal data.
 
---
 
### 2.6 Performance
 
**NFR-P01:** The zone dashboard overview (FR-03) shall load and render completely within 2 seconds under normal load (up to 200 concurrent users), measured from the time the HTTP request is issued to full page render in the browser.  
*Stakeholder:* Electrcity Distribution Manager, Municipal Executive
 
**NFR-P02:** Report generation (FR-08) for a 30-day period covering up to 500 meters shall complete within 10 seconds. For reports covering more than 500 meters or more than 90 days, a background job shall be used and the user notified on completion.  
*Stakeholder:* Electrcity Network Analyst, Finance Staff
 
**NFR-P03:** The anomaly detection check (FR-05) shall complete and persist the anomaly record within 10 seconds of the triggering meter reading being ingested, under a sustained load of 100 readings per second.  
*Stakeholder:* Electrcity Distribution Manager, Technician
 
---