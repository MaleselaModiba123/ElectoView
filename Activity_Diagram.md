# Activity Diagrams — ElectroView: Electricity Usage Analytics Dashboard

---

## 1. Workflow 1: User Login and Authentication

```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph User
        A[Navigate to login page]
        B[Enter email and password]
        C[Submit login form]
    end
 
    subgraph System
        D{Account exists?}
        E{Account Active?}
        F{Credentials valid?}
        G[Increment failed attempt counter]
        H{Failed attempts >= 5?}
        I[Lock account]
        J[Issue JWT with role claims]
        K[Log login event to audit trail]
        L[Redirect to role-based dashboard]
        M[Return HTTP 401]
        N[Return HTTP 403 — account locked]
    end
 
    C --> D
    D -- No --> M --> stop1([End])
    D -- Yes --> E
    E -- No --> N --> stop2([End])
    E -- Yes --> F
    F -- No --> G --> H
    H -- Yes --> I --> N
    H -- No --> M
    F -- Yes --> J --> K --> L --> stop3([End])
```

### Explanation
 
The parallel fork after anomaly record creation (I) shows that the in-app notification (J) and email dispatch (K) happen concurrently, neither blocks the other, and both must complete before the HTTP 201 response is returned to the sender. This parallelism ensures the Electrcity Distribution Manager receives the fastest possible alert while also guaranteeing the email channel is attempted.
 
**Stakeholder concern addressed:** Electrcity Distribution Manager's requirement for proactive, real-time anomaly alerting without manual monitoring. Meter Technician's need for timely fault notification.
 
**Mapped requirements:** FR-02, FR-05. Maps to US-002, UC-04, T-013, T-014, T-015.
 
---

## 3. Workflow 3: Anomaly Resolution

```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph Technician["Technician"]
        A[Log in and navigate to Anomaly Log]
        B[Filter by zone and status — Open]
        C[Select anomaly record]
        D[Update status to In Progress]
        E[Conduct field or remote investigation]
        F[Enter resolution notes]
        G[Set status to Resolved]
    end
 
    subgraph System
        H[Display anomaly list]
        I[Load anomaly detail view]
        J{Resolution notes empty?}
        K[Show validation error — notes required]
        L[Persist status change and notes]
        M[Append audit log entry with user ID and timestamp]
        N[Remove anomaly from Open queue]
    end
 
    subgraph EscalationCheck["Background Scheduler"]
        O{Anomaly unresolved > 48h?}
        P[Escalate anomaly status]
        Q[Notify senior engineer]
    end
 
    A --> H --> B --> C --> I --> D --> E --> F --> J
    J -- Yes --> K --> F
    J -- No --> G --> L --> M --> N --> stop1([End])
 
    D --> O
    O -- Yes --> P --> Q --> stop2([End])
    O -- No --> stop3([End])
```
### Explanation
 
Two parallel flows exist after status is set to In Progress: the technician's resolution workflow and the background scheduler's 48-hour escalation check. These run independently, the scheduler does not wait for the technician, and the technician's resolution will supersede escalation if it completes first. The guard on resolution notes (J) prevents incomplete closure, enforcing data quality in the anomaly log.
 
**Stakeholder concern addressed:** Technician's need for structured, trackable fault resolution. Electrcity Distribution Manager's need for assurance that anomalies cannot be silently abandoned.
 
**Mapped requirements:** FR-06. Maps to US-007, UC-05.
 
---

## 4. Workflow 4: Consumer Views Personal Usage Dashboard
 
```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph Consumer
        A[Log in with consumer credentials]
        B[Land on personal dashboard]
        C[View daily bar chart — current period]
        D[View previous period comparison]
        E[Hover over data point to see exact kWh]
    end
 
    subgraph System
        F{Meter linked to account?}
        G[Display — No meter registered message]
        H[Retrieve current billing period readings]
        I[Retrieve previous billing period readings]
        J{Previous period data exists?}
        K[Compute daily kWh totals]
        L[Compute percentage change between periods]
        M[Render bar chart with overlay]
        N[Render bar chart — current period only]
        O[Display — No previous period data message]
        P{Budget set?}
        Q[Display budget progress indicator]
    end
 
    A --> F
    F -- No --> G --> stop1([End])
    F -- Yes --> H
    H --> I --> J
    J -- No --> K --> N --> O --> P
    J -- Yes --> K --> L --> M --> P
    P -- Yes --> Q --> B --> C --> D --> E --> stop2([End])
    P -- No --> B --> C --> stop3([End])
```
 
### Explanation
 
The parallel retrieval of current (H) and previous (I) period data runs as sequential steps here but could be parallelised at the API level for performance. The decision node (J) handles the new-consumer edge case where no previous finance period exists, ensuring the UI degrades gracefully. The budget indicator branch (P) shows how US-006 extends this workflow without modifying its core flow.
 
**Stakeholder concern addressed:** Residential Consumer's need for clear, understandable personal usage data and finance period comparison without technical knowledge.
 
**Mapped requirements:** FR-07. Maps to US-005, US-006, UC-06.
 
---

## 5. Workflow 5: Report Generation and Export
 
```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph Analyst["Analyst / Finance Staff"]
        A[Navigate to Reports section]
        B[Set date range, zone, meter filters]
        C[Select export format — PDF or CSV]
        D[Click Generate Report]
        H[Receive background job notification]
        I[Download file from notification link]
        J[Download file immediately]
    end
 
    subgraph System
        E{Parameters valid?}
        F[Return error — adjust filters]
        G{Record count > 500 OR days > 90?}
        K[Queue background job]
        L[Generate report inline]
        M[Notify user — report processing]
        N[Background job generates file]
        O[Send in-app notification — report ready]
        P[Store file for 24 hours]
        Q{24h elapsed without download?}
        R[Purge file from server]
    end
 
    A --> B --> C --> D --> E
    E -- No --> F --> B
    E -- Yes --> G
    G -- Yes --> K --> M --> N --> O --> P --> H --> I --> Q
    G -- No --> L --> P --> J --> Q
    Q -- Yes --> R --> stop1([End])
    Q -- No --> stop2([End])
```
 
### Explanation
 
The key branching point is the record count guard (G), which routes large reports through an asynchronous background job to avoid blocking the API. Both paths converge at file storage (P), after which the 24-hour expiry lifecycle begins. This satisfies both the performance NFR (inline reports ≤ 10 seconds) and the scalability requirement for large dataset handling.
 
**Stakeholder concern addressed:** Electrcity Network Analyst's need for fast, clean exports. Finance Staff's need for CSV reports compatible with existing financial systems.
 
**Mapped requirements:** FR-08, NFR-P02. Maps to US-004, UC-07.
 
---

## 6. Workflow 6: Zone Threshold Configuration
 
```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph Electrcity DistributionManager["Electrcity Distribution Manager / Admin"]
        A[Navigate to Zone Management]
        B[Select zone to configure]
        C[Choose threshold type — Absolute or Relative]
        D[Enter threshold value]
        E[Save configuration]
    end
 
    subgraph System
        F[Load zone detail view]
        G{Threshold type?}
        H[Accept fixed kWh value input]
        I[Accept percentage of 30-day rolling average input]
        J{Value valid? positive number, within range}
        K[Show validation error]
        L[Persist new threshold to database]
        M[Store previous threshold in audit history]
        N[Apply new threshold to all subsequent readings for this zone]
        O[Display confirmation message]
    end
 
    A --> F --> B --> C --> G
    G -- Absolute --> H --> D --> J
    G -- Relative --> I --> D --> J
    J -- No --> K --> D
    J -- Yes --> E --> L
    L --> M
    L --> N
    M --> O --> stop1([End])
    N --> O
```
 
### Explanation
 
The parallel fork after persistence (L) shows that storing the audit history (M) and activating the new threshold for live processing (N) happen simultaneously. This is important: the audit log must capture the previous value before the new one takes effect. The threshold applies to all readings received after the save completes, readings already in the database are not retroactively re-evaluated.
 
**Stakeholder concern addressed:** Electrcity Distribution Manager's need to tune detection sensitivity per zone based on infrastructure characteristics and seasonal patterns.
 
**Mapped requirements:** FR-10. Maps to US-002 (detection depends on threshold), UC-09.
 
---

## 7. Workflow 7: User and Meter Account Management
 
```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph Admin["IT Administrator"]
        A[Navigate to User Management]
        B[Click Add User]
        C[Enter name, email, role, zone assignment]
        D[Submit form]
        J[Select existing user]
        K[Click Deactivate]
        L[Confirm deactivation]
    end
 
    subgraph System
        E{Email already registered?}
        F[Show error — duplicate email]
        G[Create user account — status Pending]
        H[Generate temporary password]
        I[Send welcome email with credentials]
        M{Active sessions exist for user?}
        N[Invalidate all active JWTs for user]
        O[Set account status to Inactive]
        P[Retain all historical data]
        Q[Write audit log entry]
        R[Display confirmation]
    end
 
    A --> B --> C --> D --> E
    E -- Yes --> F --> C
    E -- No --> G --> H --> I --> Q --> R --> stop1([End])
 
    A --> J --> K --> L --> M
    M -- Yes --> N --> O --> P --> Q --> R --> stop2([End])
    M -- No --> O --> P --> Q --> R
```
 
### Explanation
 
Two parallel sub-workflows are shown: account creation (top path) and deactivation (bottom path), both accessible from the same User Management screen. The deactivation path includes an active session check (M), if the user is currently logged in, their JWT must be invalidated immediately to enforce the deactivation in real time rather than waiting for token expiry.
 
**Stakeholder concern addressed:** IT Administrator's need for auditable, immediate access control management. Security requirement that deactivated users cannot continue using the system through still-valid tokens.
 
**Mapped requirements:** FR-09, NFR-SEC01. Maps to US-008, UC-08.
 
---

## 8. Workflow 8: Executive KPI Dashboard Access
 
```mermaid
flowchart TD
    start([Start]) --> A
 
    subgraph Executive
        A[Log in with Executive or Admin role]
        B[Navigate to KPI Summary view]
        E[View KPI summary]
        F[Click Export as PDF]
        G[Download single-page PDF]
    end
 
    subgraph System
        C{Role is Executive or Admin?}
        D[Return HTTP 403 — insufficient permissions]
        H[Retrieve total grid consumption — today]
        I[Retrieve week-on-week trend delta]
        J[Retrieve active anomaly count]
        K[Retrieve infrastructure utilisation %]
        L{Any KPI data older than 24h?}
        M[Trigger background aggregation refresh]
        N[Render KPI cards and trend indicators]
        O[Generate print-friendly PDF]
        P[Serve PDF for download]
    end
 
    A --> C
    C -- No --> D --> stop1([End])
    C -- Yes --> B
    B --> H
    B --> I
    B --> J
    B --> K
    H --> L
    I --> L
    J --> L
    K --> L
    L -- Yes --> M --> N
    L -- No --> N
    N --> E
    E --> F --> O --> P --> G --> stop2([End])
```
 
### Explanation
 
The four KPI data retrieval steps (H, I, J, K) run in parallel, they are independent database queries that do not depend on each other's results. This parallelism is important for meeting the dashboard load time NFR: sequential queries would approximately quadruple the response time compared to concurrent execution. The data freshness check (L) ensures executives never see stale data without being aware of it.
 
**Stakeholder concern addressed:** Municipal Executive's need for self-service strategic data without requiring analyst support. The 24-hour data freshness guard directly maps to the success metric defined in `STAKEHOLDERS.md`.
 
**Mapped requirements:** FR-11, NFR-P01. Maps to US-011, UC-10.
 
---