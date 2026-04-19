# State Diagrams — ElectroView: Electricity Usage Analytics Dashboard

---

## 1. Object 1: User Account

```mermaid
stateDiagram-v2
    [*] --> Pending : Admin creates account
 
    Pending --> Active : Admin activates / welcome email sent
    Pending --> Cancelled : Admin cancels before activation
 
    Active --> Locked : 5 consecutive failed login attempts
    Active --> Inactive : Admin deactivates account
    Active --> Active : Successful login [JWT issued]
 
    Locked --> Active : Admin unlocks account
    Locked --> Inactive : Admin deactivates while locked
 
    Inactive --> Active : Admin reactivates account
    Inactive --> [*] : Account permanently deleted [data retained]
```
### Explanation
 
**Key states:** A User Account begins in `Pending` when first created by an administrator and transitions to `Active` once the admin confirms activation and a welcome email is dispatched. The `Locked` state is entered automatically after 5 consecutive failed login attempts — a security guard condition enforced without human intervention. `Inactive` accounts exist when an admin deactivates a user; the account cannot be logged into but all historical data is retained.
 
**Key transitions:** The transition from `Active` to `Locked` has an implicit guard: `[failedAttempts >= 5]`. The transition from `Inactive` back to `Active` requires explicit admin action, preventing accidental reactivation.
 
**Mapped requirements:** FR-01 (authentication and RBAC), FR-09 (user management), NFR-SEC03 (rate limiting and lockout). Maps to US-008 and US-012.
 
---

## 2. Object 2: Meter Reading

```mermaid
stateDiagram-v2
    [*] --> Received : POST /api/meters/readings
 
    Received --> Validating : System begins validation
 
    Validating --> Rejected : [payload invalid — missing fields / negative kWh]
    Validating --> Persisted : [payload valid]
 
    Persisted --> FlaggedAnomaly : [kWh exceeds zone threshold]
    Persisted --> Normal : [kWh within zone threshold]
 
    FlaggedAnomaly --> [*] : Anomaly record created, alert dispatched
    Normal --> [*] : Reading stored, no further action
    Rejected --> [*] : HTTP 400 returned to sender
```

### Explanation
 
**Key states:** Every meter reading submitted to the ingestion API passes through `Validating` before it can be persisted. The guard condition `[payload invalid]` causes immediate rejection with an HTTP 400 response. Once persisted, a second guard `[kWh exceeds zone threshold]` determines whether the reading moves to `FlaggedAnomaly`, triggering the anomaly detection pipeline, or `Normal`, where it is simply stored.
 
**Key transitions:** The branching at `Persisted` is the system's core anomaly detection decision point. Both terminal paths end the reading's active lifecycle — it becomes a historical record.
 
**Mapped requirements:** FR-02 (meter reading ingestion), FR-05 (anomaly detection). Maps to US-002, T-013, T-014.
 
---

## 3. Object 3: Anomaly

```mermaid
stateDiagram-v2
    [*] --> Open : Anomaly detected [reading exceeds threshold]
 
    Open --> InProgress : Technician / Manager assigns themselves
    Open --> AutoResolved : [subsequent readings return to normal AND no action taken within 24h]
 
    InProgress --> Resolved : Technician logs resolution notes and closes
    InProgress --> Escalated : [no resolution within 48 hours]
    InProgress --> Open : Technician unassigns (returned to queue)
 
    Escalated --> InProgress : Senior engineer picks up
    Escalated --> Resolved : Senior engineer resolves
 
    AutoResolved --> [*] : Logged as auto-resolved, retained in history
    Resolved --> [*] : Retained in anomaly log for 12 months
```

### Explanation
 
**Key states:** An anomaly is created in `Open` state the moment a flagged reading is detected. `InProgress` represents active field or remote investigation. `Escalated` is a guard-conditioned transition that fires if no resolution occurs within 48 hours, ensuring anomalies cannot be silently abandoned. `AutoResolved` handles the case where consumption normalises before a technician intervenes.
 
**Key transitions:** The transition from `InProgress` to `Escalated` has the guard `[resolutionTime > 48h]`. Resolution requires explicit note entry — the system does not permit a status change to `Resolved` without a non-empty resolution note field.
 
**Mapped requirements:** FR-05 (anomaly detection), FR-06 (anomaly resolution workflow). Maps to US-002, US-007, T-015.
 
---

## 4. Object 4: Consumtion Report

```mermaid
stateDiagram-v2
    [*] --> Requested : User submits report parameters
 
    Requested --> Validating : System checks date range and zone filters
 
    Validating --> Rejected : [date range invalid OR no data found for parameters]
    Validating --> Queued : [parameters valid AND record count > 500 OR days > 90]
    Validating --> Generating : [parameters valid AND within inline threshold]
 
    Queued --> Generating : Background job picks up request
    Generating --> Ready : Report file created (PDF or CSV)
    Generating --> Failed : [generation error — DB timeout or file system error]
 
    Ready --> Downloaded : User downloads file
    Ready --> Expired : [24 hours elapsed without download]
 
    Downloaded --> [*] : File removed from server after download
    Expired --> [*] : File purged automatically
    Failed --> [*] : User notified, error logged
    Rejected --> [*] : HTTP 400 returned with reason
```

### Explanation
 
**Key states:** The `Queued` state handles the large-report scenario, reports covering more than 500 meters or 90 days are processed asynchronously so they do not block the API. `Ready` is a time-bounded state: files that are not downloaded within 24 hours move to `Expired` and are purged from the server to manage storage.
 
**Key transitions:** The branching at `Validating` is governed by two sequential guards: first whether the parameters are valid, then whether the report size falls within the inline processing threshold.
 
**Mapped requirements:** FR-08 (report generation), NFR-P02 (report performance). Maps to US-004, T-020.
 
---

## 5. Object 5: Zone
 
```mermaid
stateDiagram-v2
    [*] --> Configured : Admin creates zone with name, capacity, threshold
 
    Configured --> Active : At least one meter registered to zone
 
    Active --> HighLoad : [current load > 90% of zone capacity]
    Active --> Normal : [load <= 90%]
 
    HighLoad --> Normal : [load drops back below 90%]
    HighLoad --> AnomalyAlert : [load > 100% OR individual meter exceeds threshold]
 
    AnomalyAlert --> HighLoad : [anomaly resolved, load still > 90%]
    AnomalyAlert --> Normal : [anomaly resolved, load returns to normal]
 
    Normal --> Inactive : Admin deactivates zone (no active meters)
    HighLoad --> Inactive : Admin deactivates zone
    AnomalyAlert --> Inactive : Admin deactivates zone
 
    Inactive --> Active : Admin reactivates zone
    Inactive --> [*] : Zone permanently removed [historical data retained]
```
 
### Explanation
 
**Key states:** A Zone's operational states;  `Normal`, `HighLoad`, and `AnomalyAlert` correspond directly to the colour-coded status indicators on the Zone Dashboard. These states are computed continuously from incoming meter readings and are not manually set. `Inactive` is the only manually-assigned state.
 
**Key transitions:** The transition from `Active` to `HighLoad` is triggered automatically when aggregated consumption crosses 90% of the zone's configured capacity. This threshold is configurable per zone (FR-10), so the guard condition references the stored threshold value rather than a hardcoded number.
 
**Mapped requirements:** FR-03 (zone dashboard), FR-10 (threshold configuration). Maps to US-001, US-002.
 
---
## 6. Object 6: Smart Meter
 
```mermaid
stateDiagram-v2
    [*] --> Registered : Admin registers meter (serial, zone, consumer)
 
    Registered --> Active : First reading received from meter
 
    Active --> Transmitting : Reading submission in progress
    Transmitting --> Active : Reading accepted [HTTP 201]
    Transmitting --> FaultSuspected : [3 consecutive rejected readings]
 
    Active --> Offline : [no reading received within expected polling interval × 3]
    Offline --> Active : Reading received after gap
    Offline --> FaultConfirmed : [offline for > 24 hours]
 
    FaultSuspected --> Active : Technician inspects — no fault found
    FaultSuspected --> FaultConfirmed : Technician confirms fault
 
    FaultConfirmed --> UnderMaintenance : Technician assigned for repair/replacement
    UnderMaintenance --> Active : Repair complete, readings resume
    UnderMaintenance --> Decommissioned : Meter replaced — old serial retired
 
    Decommissioned --> [*] : Historical data retained under old meter ID
```
 
### Explanation
 
**Key states:** The meter lifecycle captures both software-visible states (`Transmitting`, `Offline`) and physical states (`FaultConfirmed`, `UnderMaintenance`, `Decommissioned`). The `FaultSuspected` state acts as a buffer between transient errors and confirmed hardware failures, preventing unnecessary field dispatches.
 
**Key transitions:** The transition to `Offline` is time-based, if the polling interval (e.g., every 15 minutes) passes three times without a reading, the meter is flagged offline. `FaultConfirmed` can be reached either through prolonged offline status or through technician inspection following rejected readings.
 
**Mapped requirements:** FR-02 (meter ingestion), FR-06 (anomaly resolution), FR-09 (meter management). Maps to US-007, US-008.
 
---
 
## 7. Object 7: Consumer Usage Budget
 
```mermaid
stateDiagram-v2
    [*] --> NotSet : Consumer account exists, no budget configured
 
    NotSet --> Active : Consumer sets monthly kWh budget
 
    Active --> Tracking : System begins comparing daily consumption to budget
    Tracking --> WarningIssued : [cumulative kWh >= 80% of budget]
    Tracking --> Active : New billing period starts [budget resets]
 
    WarningIssued --> LimitReached : [cumulative kWh >= 100% of budget]
    WarningIssued --> Active : New billing period starts [budget resets]
 
    LimitReached --> Active : New billing period starts [budget resets]
    LimitReached --> Exceeded : [consumption continues past 100%]
 
    Active --> NotSet : Consumer removes budget
    Exceeded --> Active : New billing period starts [budget resets]
```
 
### Explanation
 
**Key states:** The budget object has a progressive alert lifecycle, `WarningIssued` at 80% and `LimitReached` at 100%, giving consumers two notification touchpoints before they exceed their target. `Exceeded` captures the state where consumption continues past the budget limit within the same billing period, distinguishing it from `LimitReached` which is the exact threshold crossing.
 
**Key transitions:** All transitions back to `Active` at the start of a new billing period are system-triggered (automated monthly reset). The transition from `NotSet` to `Active` is the only consumer-initiated transition.
 
**Mapped requirements:** FR-07 (consumer usage view and budget). Maps to US-006.
 
---
 
## 8. Object 8: User Session
 
```mermaid
stateDiagram-v2
    [*] --> Initiated : User submits login credentials
 
    Initiated --> Authenticated : [credentials valid AND account Active]
    Initiated --> Rejected : [credentials invalid OR account Locked/Inactive]
 
    Authenticated --> Active : JWT issued and stored in browser
    Active --> Expired : [JWT lifetime > 8 hours with no refresh]
    Active --> Refreshed : [user requests token refresh within 7-day window]
    Active --> Terminated : User logs out explicitly
 
    Refreshed --> Active : New JWT issued
    Expired --> [*] : User redirected to login page
    Terminated --> [*] : JWT invalidated server-side
    Rejected --> [*] : HTTP 401 returned, failed attempt counter incremented
```
 
### Explanation
 
**Key states:** The session object is distinct from the User Account, a single account can have multiple sessions (e.g., browser and mobile). `Active` represents a valid, unexpired JWT in use. The `Refreshed` state handles the sliding window renewal, allowing long-running sessions to persist without forcing re-login every 8 hours as long as the user remains active within the 7-day refresh window.
 
**Key transitions:** The guard on `Authenticated` checks both credential validity and account status, a valid password on a `Locked` account still results in `Rejected`. Explicit logout (`Terminated`) invalidates the JWT server-side via a blocklist, preventing token reuse after logout.
 
**Mapped requirements:** FR-01 (authentication and RBAC), NFR-SEC01 (TLS), NFR-SEC02 (password hashing). Maps to US-012, T-001 through T-004.
 
---