# UseCases.md — ElectroView: Electircity Usage Analytics dashboard

---

```mermaid
graph TD
    %% Actors
    EDM([Electrcity Distribution Manager])
    ENA([Electrcity Network Analyst])
    C([Consumer])
    IT([IT Administrator])
    FS([Finance Staff])
    ET([Electrcity Technician])
    ME([Municipal Executive])
    SYS([System / Scheduler])
 
    %% Use Cases
    UC1[UC-01: Login and Authenticate]
    UC2[UC-02: View Zone Dashboard]
    UC3[UC-03: View Historical Trends]
    UC4[UC-04: Detect and Alert Anomaly]
    UC5[UC-05: Manage Anomaly Resolution]
    UC6[UC-06: View Personal Usage]
    UC7[UC-07: Generate and Export Report]
    UC8[UC-08: Manage Users and Meters]
    UC9[UC-09: Configure Zone Thresholds]
    UC10[UC-10: View Executive KPI Summary]
    UC11[UC-11: Import Bulk Meter Data]
    UC12[UC-12: Set Personal Usage Budget]
 
    %% Inclusions (dashed style via subgraph label)
    UC2 -->|includes| UC1
    UC3 -->|includes| UC1
    UC4 -->|includes| UC1
    UC5 -->|includes| UC1
    UC6 -->|includes| UC1
    UC7 -->|includes| UC1
    UC8 -->|includes| UC1
    UC9 -->|includes| UC1
    UC10 -->|includes| UC1
    UC11 -->|includes| UC1
    UC12 -->|includes| UC1
 
    UC4 -->|includes| UC9
 
    %% Actor associations
    EDM --- UC2
    EDM --- UC3
    EDM --- UC5
    EDM --- UC9
 
    ENA --- UC3
    ENA --- UC7
    ENA --- UC11
 
    C --- UC6
    C --- UC12
 
    IT --- UC8
    IT --- UC11
 
    BS --- UC7
 
    ET --- UC5
 
    ME --- UC10
 
    SYS --- UC4
```

## 2. Actor and Use Case Descriptions
 
### 2.1 Actors
 
| Actor | Type | Role |
|---|---|---|
| Electrcity Distribution Manager | Primary | Monitors zone-level consumption and load in real time; responds to operational alerts. Maps to FR-03, FR-05, FR-09, FR-10. |
| Electrcity Network Analyst | Primary | Analyses historical trends, produces consumption reports, and imports historical data. Maps to FR-04, FR-08, FR-12. |
| Consumer | Primary | Views their own meter's usage history and manages their personal budget alert. Maps to FR-07. |
| IT Administrator | Primary | Manages user accounts, meter records, and bulk data imports. Maps to FR-09, FR-12. |
| Finance Staff | Primary | Generates and exports consumption reports for billing reconciliation. Maps to FR-08. |
| Electrcity Technician | Primary | Reviews and resolves anomalies identified in the field. Maps to FR-06. |
| Municipal Executive | Primary | Views high-level KPI summaries for strategic reporting. Maps to FR-11. |
| System / Scheduler | Secondary | Automated actor; triggers anomaly detection after each meter reading ingestion. Maps to FR-05. |

---

### 2.2 Key Relationships
 
**Include relationships:** Every use case that requires an authenticated session includes UC-01 (Login and Authenticate). This avoids repeating authentication logic in each specification and reflects the system's JWT enforcement on all protected endpoints (FR-01). UC-04 (Detect and Alert Anomaly) additionally includes UC-09 (Configure Zone Thresholds) because anomaly detection cannot execute without a threshold value being present.
 
**Actor generalisations:** The Electrcity Distribution Manager and Electrcity Network Analyst share access to UC-03 (View Historical Trends) — both roles require trend data, though for different purposes (operational monitoring vs. analytical reporting). Rather than duplicating the use case, both actors are associated with the same use case, with role-specific filtering applied within the system.
 
**Secondary actor (System / Scheduler):** UC-04 is initiated not by a human actor but by the system itself upon ingestion of each meter reading. This models the automated nature of anomaly detection, which operates continuously in the background without requiring manual user intervention.

---

### 2.3 Alignment with Stakeholder Concerns
 
| Stakeholder Concern | Addressed by Use Case |
|---|---|
| Electrcity Distribution Manager: real-time visibility into zone load | UC-02: View Zone Dashboard |
| Electrcity Distribution Manager: proactive fault detection | UC-04: Detect and Alert Anomaly |
| Electrcity Network Analyst: historical trend analysis | UC-03: View Historical Trends |
| Electrcity Network Analyst: report generation and export | UC-07: Generate and Export Report |
| Consumer: personal usage visibility | UC-06: View Personal Usage |
| Residential Consumer: budget management | UC-12: Set Personal Usage Budget |
| Finance Staff: meter-level data for reconciliation | UC-07: Generate and Export Report |
| Electrcity Technician: structured anomaly resolution | UC-05: Manage Anomaly Resolution |
| IT Admin: user and meter account management | UC-08: Manage Users and Meters |
| Municipal Executive: self-service KPI access | UC-10: View Executive KPI Summary |
 
---

## 3. Use Case Specifications
 
---
 
### UC-01: Login and Authenticate
 
**Actor:** All users  
**Related FR:** FR-01  
**Description:** A user provides their credentials to gain access to the system. Upon successful authentication, a JWT is issued and the user is directed to their role-appropriate dashboard.
 
**Preconditions:**
- The user has a registered account with an assigned role.
- The system is online and the authentication service is reachable.
 
**Postconditions:**
- A valid JWT is stored in the user's browser session.
- The user is redirected to their role-specific dashboard (e.g., Zone Dashboard for Electrcity Distribtion Manager, Personal Usage for Consumer).
- A login event is recorded in the audit log.
 
**Basic Flow:**
1. User navigates to the login page.
2. User enters email address and password.
3. System validates the credentials against the stored bcrypt hash.
4. System issues a JWT with the user's role embedded as a claim.
5. System redirects the user to their default dashboard.
 
**Alternative Flows:**
 
*AF-01A: Invalid credentials*
- At step 3, credentials do not match.
- System increments the failed attempt counter for that account.
- System displays: "Invalid email or password. Please try again."
- If 5 consecutive failures occur, the account is temporarily locked and the user is informed to contact an administrator.
 
*AF-01B: Account deactivated*
- At step 3, the account exists but is marked inactive.
- System displays: "Your account has been deactivated. Contact your administrator."
- No JWT is issued.
 
---
 
### UC-02: View Zone Dashboard
 
**Actor:** Electrcity Distribution Manager, Municipal Executive  
**Related FR:** FR-03  
**Description:** The user views a live overview of all active distribution zones, including current consumption (kWh), load percentage, and status indicator. The dashboard refreshes every 5 minutes.
 
**Preconditions:**
- User is authenticated (UC-01 completed).
- User has the Electrcity Distribution Manager or Administrator role.
- At least one active zone and one active meter exist in the system.
 
**Postconditions:**
- The user has a current view of all zone statuses.
- No state change occurs in the database as a result of viewing.
 
**Basic Flow:**
1. User navigates to the Zone Dashboard.
2. System queries the cache (Redis) for the latest aggregated zone summaries.
3. System renders a table/card view showing: zone name, total kWh consumed today, load as a percentage of capacity, and a colour-coded status (Normal / High Load / Anomaly).
4. Dashboard auto-refreshes every 5 minutes.
 
**Alternative Flows:**
 
*AF-02A: Cache miss*
- At step 2, no cached summary exists.
- System queries the PostgreSQL database directly for the latest readings.
- System populates the cache before rendering the response.
- Additional latency of up to 3 seconds may be observed; a loading indicator is shown.
 
*AF-02B: No active zones*
- At step 3, no active zones are found.
- System displays: "No active zones configured. Contact your administrator."
 
---
 
### UC-03: View Historical Trends
 
**Actor:** Electrcity Distribution Manager, Electrcity Network Analyst  
**Related FR:** FR-04  
**Description:** The user selects one or more zones and a time range to view overlaid line charts of historical electricity consumption, enabling pattern identification and comparative analysis.
 
**Preconditions:**
- User is authenticated with Electrcity Distribtion Manager or Analyst role.
- Historical readings exist in the database for the selected period.
 
**Postconditions:**
- The chart is rendered in the browser.
- No database state is modified.
 
**Basic Flow:**
1. User navigates to the Trends section.
2. User selects one or more zones from a multi-select dropdown.
3. User selects a time range: 24h (hourly), 7d (daily), or 30d (weekly).
4. System queries aggregated consumption data for the selected zones and period.
5. System renders overlaid line charts with labelled axes (kWh on Y, time on X).
6. User can hover over data points to view exact values in a tooltip.
 
**Alternative Flows:**
 
*AF-03A: No data for selected period*
- At step 4, no readings exist for the selected zone/period combination.
- System displays: "No data available for the selected filters. Try a different zone or date range."
 
*AF-03B: Chart render timeout*
- At step 5, the query takes longer than 5 seconds.
- System displays a loading indicator for up to 10 seconds, then shows an error message with a retry option.
 
---
 
### UC-04: Detect and Alert Anomaly
 
**Actor:** System / Scheduler (automated)  
**Related FR:** FR-05  
**Description:** Following each meter reading ingestion, the system automatically evaluates the reading against the configured zone threshold. If the threshold is exceeded, an anomaly record is created, an in-app notification is generated, and an alert email is dispatched to the responsible administrator.
 
**Preconditions:**
- A meter reading has been successfully ingested (FR-02).
- A threshold is configured for the meter's zone (UC-09).
- An administrator account is designated for the zone.
 
**Postconditions:**
- An anomaly record exists in the `anomalies` table with status "Open."
- An in-app notification is visible in the administrator's notification panel.
- An alert email has been dispatched to the designated administrator.
 
**Basic Flow:**
1. System receives a successfully ingested meter reading.
2. System retrieves the threshold configuration for the meter's zone.
3. System compares the reading value against the threshold.
4. Reading exceeds the threshold.
5. System creates an anomaly record (meter ID, reading ID, value, threshold, timestamp, status: Open).
6. System pushes an in-app notification to the designated administrator's session.
7. System dispatches an alert email via the configured email service.
 
**Alternative Flows:**
 
*AF-04A: Reading within threshold*
- At step 3, the reading does not exceed the threshold.
- No anomaly record is created. Processing ends.
 
*AF-04B: Email service unavailable*
- At step 7, the email service returns an error.
- Anomaly record and in-app notification are still created (steps 5–6 succeed).
- System logs the email failure and schedules a retry after 5 minutes (up to 3 retries).
 
---
 
### UC-05: Manage Anomaly Resolution
 
**Actor:** Electrcity Distribtion Manager, Electrcity Technician  
**Related FR:** FR-06  
**Description:** An authorised user reviews the anomaly log, updates the status of an anomaly (Open → In Progress → Resolved), and adds resolution notes following a physical investigation or corrective action.
 
**Preconditions:**
- User is authenticated with Electrcity Distribtion Manager or Electrcity Technician role.
- At least one anomaly record exists with status "Open" or "In Progress."
 
**Postconditions:**
- The anomaly record reflects the updated status and resolution notes.
- The status change is logged with timestamp and the acting user's ID.
 
**Basic Flow:**
1. User navigates to the Anomaly Log.
2. User filters the log by zone and/or status.
3. User selects an anomaly record to view its details.
4. User updates the status (e.g., from "Open" to "In Progress").
5. User enters resolution notes describing the investigation or action taken.
6. User saves the update.
7. System persists the status change and notes, and appends a log entry (user ID, timestamp, previous status, new status).
 
**Alternative Flows:**
 
*AF-05A: Anomaly already resolved*
- At step 3, the selected anomaly has status "Resolved."
- System displays the record in read-only mode with a note: "This anomaly has been resolved."
- No edits are permitted.
 
---
 
### UC-06: View Personal Usage
 
**Actor:** Consumer  
**Related FR:** FR-07  
**Description:** A consumer views their meter's consumption history as a daily breakdown for the current and previous billing periods, with a percentage comparison between the two periods.
 
**Preconditions:**
- User is authenticated with Consumer role.
- A meter is registered and associated with the consumer's account.
- At least one billing period of readings exists for the consumer's meter.
 
**Postconditions:**
- The consumer has viewed their consumption data.
- No database state is modified.
 
**Basic Flow:**
1. Consumer logs in and is directed to their personal dashboard.
2. System retrieves daily consumption totals for the current and previous billing month for the consumer's meter.
3. System renders a bar chart showing daily kWh for the current period, with the previous period overlaid for comparison.
4. System displays the total kWh for each period and the percentage change (e.g., "↑ 12% vs last month").
5. Consumer can hover over any bar to see the exact kWh value for that day.
 
**Alternative Flows:**
 
*AF-06A: No meter assigned*
- At step 2, no meter is linked to the consumer's account.
- System displays: "No meter is registered to your account. Please contact your municipality."
 
*AF-06B: Insufficient data for comparison*
- At step 2, only the current period has data (no previous period exists).
- System renders the current period chart and displays: "No previous period data available for comparison."
 
---
 
### UC-07: Generate and Export Report
 
**Actor:** Electrcity Network Analyst, Finance Department Staff  
**Related FR:** FR-08  
**Description:** An authorised user configures report parameters (date range, zone, meter), generates a consumption report, and downloads it in PDF or CSV format.
 
**Preconditions:**
- User is authenticated with Analyst or Finance Department Staff role.
- Consumption data exists in the database for the selected parameters.
 
**Postconditions:**
- A report file (PDF or CSV) is generated and available for download for 24 hours.
- The report generation event is recorded in the audit log.
 
**Basic Flow:**
1. User navigates to the Reports section.
2. User sets the start date and end date (mandatory).
3. User optionally selects a zone and/or a specific meter ID.
4. User selects export format: PDF or CSV.
5. User clicks "Generate Report."
6. System queries the database and aggregates consumption data for the selected parameters.
7. System generates the report file.
8. System presents a download link to the user.
9. User downloads the file.
 
**Alternative Flows:**
 
*AF-07A: No data for selected parameters*
- At step 6, no readings match the selected filters.
- System displays: "No data found for the selected parameters. Adjust your filters and try again."
- No file is generated.
 
*AF-07B: Large report (background processing)*
- At step 6, the query covers more than 500 meters or more than 90 days.
- System initiates a background job and informs the user: "Your report is being generated. You will be notified when it is ready."
- User receives an in-app notification with a download link when the job completes.
 
---
 
### UC-08: Manage Users and Meters
 
**Actor:** IT Administrator  
**Related FR:** FR-09  
**Description:** The administrator creates, updates, or deactivates user accounts and meter records, and assigns roles to users. All changes are recorded in the audit log.
 
**Preconditions:**
- User is authenticated with Administrator role.
 
**Postconditions:**
- The user account or meter record reflects the changes made.
- All changes are recorded in the audit log with timestamp and administrator ID.
 
**Basic Flow (Create User):**
1. Administrator navigates to the User Management section.
2. Administrator clicks "Add User."
3. Administrator enters: name, email, role, and assigned zone (if applicable).
4. System validates that the email is not already registered.
5. System creates the account, generates a temporary password, and sends a welcome email to the new user.
6. System records the creation event in the audit log.
 
**Alternative Flows:**
 
*AF-08A: Duplicate email*
- At step 4, the email address already exists in the system.
- System displays: "An account with this email already exists."
- No account is created.
 
*AF-08B: Deactivate User*
- Administrator selects an existing user and clicks "Deactivate."
- System sets the account status to inactive.
- Any active sessions for that user are invalidated immediately.
- Historical data associated with the account is retained.

---