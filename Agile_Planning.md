# Agile Planning — ElectroView: Electricity Usage Analytics Dashboard

---

## 1. User Stories

| Story ID | User Story | Acceptance Criteria | Priority |
|---|---|---|---|
| US-001 | As an **electrcity distribution manager**, I want to view a live dashboard of all distribution zones so that I can immediately identify zones with high load or active anomalies. | All active zones are displayed with kWh consumed today, load %, and a colour-coded status (Normal / High Load / Anomaly). Dashboard renders within 2 seconds. Data is no older than 5 minutes. | High |
| US-002 | As an **electrcity distribution manager**, I want the system to automatically detect anomalous meter readings so that I am alerted immediately without having to monitor raw data manually. | An anomaly record is created within 10 seconds of a threshold-exceeding reading. An in-app notification appears in my panel without a page refresh. An alert email is sent within 2 minutes. | High |
| US-003 | As an **Electricty network analyst**, I want to view historical consumption trend charts for selected zones over 24h, 7d, or 30d so that I can identify usage patterns and plan infrastructure accordingly. | Chart renders within 2 seconds of filter selection. I can overlay multiple zones on the same chart. Y-axis is labelled in kWh; X-axis reflects the selected time range. | High |
| US-004 | As an **Electricty network analyst**, I want to generate a consumption report filtered by date range and zone and export it as a CSV or PDF so that I can share findings with the finance department and management. | Report generates within 10 seconds for up to 30 days / 500 meters. CSV columns match the finance schema (`meter_id`, `zone`, `date`, `kwh_consumed`, `anomaly_flag`). File is available for download for 24 hours. | High |
| US-005 | As a **consumer**, I want to view my daily electricity consumption for the current and previous finance period so that I can understand my usage habits and compare costs. | Dashboard shows daily bar chart for current and previous finance month. Percentage change indicator is displayed. Data is scoped exclusively to my own meter — no other meter data is visible. | High |
| US-006 | As a **consumer**, I want to set a monthly kWh usage budget and receive an alert when I reach 80% and 100% of it so that I can manage my electricity costs proactively. | Budget can be set and updated from the consumer dashboard. In-app alert fires when 80% of budget is reached. Second alert fires at 100%. Alerts are visible without a page refresh. | Medium |
| US-007 | As a **technician**, I want to view a filtered list of open anomalies and update their status with resolution notes so that field investigations are tracked systematically. | Anomaly log is filterable by zone and status. I can change status from Open → In Progress → Resolved. Resolution notes are saved with my user ID and a timestamp. Resolved anomalies remain in the log for at least 12 months. | High |
| US-008 | As an **IT administrator**, I want to create, update, and deactivate user accounts with assigned roles so that access to the system is properly controlled and auditable. | New user account is created in under 3 clicks. Deactivated users cannot log in but their data is retained. All changes are logged in the audit trail with timestamp and my admin ID. | High |
| US-009 | As an **IT administrator**, I want to deploy the entire system using a single `docker-compose up` command so that setup is repeatable and does not depend on manual environment configuration. | Full system (API, frontend, PostgreSQL, Redis) is running after one command given a valid `.env` file. No additional manual steps are required. Deployment completes in under 10 minutes on a clean Ubuntu 22.04 server. | Medium |
| US-010 | As a **Electricty network analyst**, I want to upload a CSV file of historical meter readings so that past data can be back-populated into the system for long-term trend analysis. | CSV is validated on upload. Invalid rows are rejected with per-row error messages. Valid rows are imported without failing the batch. A summary of imported / skipped / failed rows is shown on completion. Maximum file size of 50 MB is enforced. | Medium |
| US-011 | As a **municipal executive**, I want to access a read-only KPI summary dashboard showing total Electricty network consumption, week-on-week trend, active anomaly count, and infrastructure utilisation so that I can make strategic decisions without requiring analyst support. | All KPIs are refreshed on page load with data no older than 24 hours. The view is print-friendly and exportable as a single-page PDF. No raw meter or consumer data is visible in this view. | Medium |
| US-012 | As an **IT administrator**, I want all user passwords stored as bcrypt hashes and all API traffic served over TLS so that the system meets baseline security standards and protects user credentials. | Passwords are never logged or stored in plaintext. TLS 1.2 or higher is enforced; HTTP requests redirect to HTTPS. Login endpoint rate-limited to 10 attempts per IP per minute. | High |

---

## 2. Product Backlog

| Story ID | User Story (Summary) | MoSCoW | Story Points | Dependencies | Justification |
|---|---|---|---|---|---|
| US-001 | View live zone dashboard | Must-have | 5 | US-012 | Core operational capability for the Utility Manager — the primary daily user of the system. Without this, the system delivers no monitoring value. |
| US-002 | Automatic anomaly detection and alerting | Must-have | 5 | US-001, US-009 (threshold config) | Addresses the most critical pain point: reactive fault management. A key differentiator of the system over manual monitoring. |
| US-003 | View historical consumption trend charts | Must-have | 3 | US-001 | Required for Grid Analyst's core workflow. Trend data is the foundation of all planning and reporting activities. |
| US-004 | Generate and export consumption report | Must-have | 5 | US-003 | Finance reconciliation depends on this. Directly satisfies the Finance Staff success metric of reducing dispute resolution from 5 days to 1. |
| US-005 | Consumer views personal usage dashboard | Must-have | 3 | US-012 | Consumer-facing transparency is a primary system goal. Provides value independent of backend analytics features. |
| US-012 | Secure auth, password hashing, TLS enforcement | Must-have | 3 | None | Security is a prerequisite for all other stories. No story involving user data can be accepted without this foundation in place. |
| US-008 | Admin manages user accounts and roles | Must-have | 3 | US-012 | All role-differentiated features depend on user accounts existing with correct roles. Required before any actor-specific story can be tested end-to-end. |
| US-007 | Meter technician manages anomaly resolution | Must-have | 3 | US-002 | Anomaly detection (US-002) has no operational value without a resolution workflow. Together they form a complete fault management loop. |
| US-006 | Consumer sets personal usage budget and alerts | Should-have | 2 | US-005 | Adds meaningful value to the consumer experience but is not essential for the initial release. Depends on the personal dashboard (US-005) being delivered first. |
| US-010 | Bulk CSV import of historical meter data | Should-have | 3 | US-008 | Important for analysts to perform long-term trend analysis from day one of deployment, but the system delivers value without it initially. |
| US-009 | Single-command Docker deployment | Should-have | 2 | None | Significant operational value for IT Admin, but the system can be deployed manually in the short term. Prioritised for the second sprint. |
| US-011 | Executive KPI summary dashboard | Could-have | 3 | US-001, US-003 | Valuable for stakeholder buy-in and strategic reporting, but executives can be served by analyst-generated reports in the short term. |

---
## Sprint Plan

### 3.1 Sprint Goal
**"Deliver a working, secure, role-authenticated foundation with live zone monitoring, automated anomaly detection, and a consumer usage view, forming the minimum viable product of ElectroView."**
 
This sprint establishes the core value of the system: electricity consumption is ingested, visualised in real time for utility managers, flagged automatically when anomalous, and made accessible to residential consumers. By the end of Sprint 1, a demonstrable, end-to-end working system exists that satisfies the most critical stakeholder needs identified in Assignment 4.

### 3.2 Selected Stories for Sprint 1
 
| Story ID | Summary |
|---|---|
| US-012 | Secure auth, password hashing, TLS |
| US-008 | Admin manages user accounts and roles |
| US-001 | View live zone dashboard |
| US-002 | Automatic anomaly detection and alerting |
| US-005 | Consumer views personal usage dashboard |
| **Total** | |

## 3.2 Sprint Backlog

| Task ID | Story ID | Task Description | Estimated Hours | Status |
|---|---|---|---|---|
| T-001 | US-012 | Configure TLS certificate and enforce HTTPS redirect in Nginx | 2 | To Do |
| T-002 | US-012 | Implement bcrypt password hashing in the auth service (cost factor 12) | 3 | To Do |
| T-003 | US-012 | Implement JWT issuance and validation middleware with role claims | 4 | To Do |
| T-004 | US-012 | Implement login rate limiting (10 attempts/min/IP) and account lockout after 5 failures | 3 | To Do |
| T-005 | US-008 | Build POST `/api/admin/users` endpoint — create user with role assignment | 4 | To Do |
| T-006 | US-008 | Build PATCH `/api/admin/users/:id` endpoint — update role and deactivate | 3 | To Do |
| T-007 | US-008 | Build audit log table and middleware to record all user/meter changes | 4 | To Do |
| T-008 | US-008 | Build User Management UI page (list, create, deactivate) | 5 | To Do |
| T-009 | US-001 | Build GET `/api/dashboard/zones` endpoint returning aggregated zone summaries | 5 | To Do |
| T-010 | US-001 | Implement Redis caching layer for zone summary queries (5-minute TTL) | 3 | To Do |
| T-011 | US-001 | Build Zone Dashboard UI — zone cards with kWh, load bar, status indicator | 6 | To Do |
| T-012 | US-001 | Implement 5-minute auto-refresh on Zone Dashboard (polling) | 2 | To Do |
| T-013 | US-002 | Build anomaly detection service — compare reading to zone threshold | 4 | To Do |
| T-014 | US-002 | Build POST `/api/meters/readings` ingestion endpoint with validation | 4 | To Do |
| T-015 | US-002 | Implement anomaly record creation and in-app notification push | 4 | To Do |
| T-016 | US-002 | Integrate email alert dispatch (SendGrid) on anomaly creation | 3 | To Do |
| T-017 | US-005 | Build GET `/api/consumers/me/readings` endpoint scoped to authenticated consumer's meter | 4 | To Do |
| T-018 | US-005 | Build Consumer Dashboard UI — daily bar chart, period comparison, % change indicator | 6 | To Do |
| T-019 | US-005 | Enforce consumer data isolation — reject requests to other meters with HTTP 403 | 2 | To Do |
| T-020 | All | Write integration tests for all Sprint 1 API endpoints (target: 70% coverage) | 6 | To Do |
| T-021 | All | Set up Docker Compose file with all services (API, frontend, PostgreSQL, Redis) | 4 | To Do |
| T-022 | All | Seed database with simulated meter data for Zone A–E (Cape Town zones) | 2 | To Do |

---
