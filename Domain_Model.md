# Domain Modeling - ElectroView: Electircity Usage Analytics dashboard

---

## 1. Domain Entities

### Entity 1: User

| Property | Detail |
|---|---|
| **Description** | Represents any person who interacts with the ElectroView system. Users are differentiated by role, which determines what data they can access and what actions they can perform. |
| **Attributes** | `userId: UUID`, `name: String`, `email: String`, `passwordHash: String`, `role: Role`, `status: AccountStatus`, `createdAt: DateTime`, `lastLoginAt: DateTime`, `failedLoginAttempts: Integer` |
| **Methods** | `login()`, `logout()`, `resetPassword()`, `updateProfile()`, `isActive(): Boolean`, `isLocked(): Boolean` |
| **Relationships** | Has many `Session` objects; has one optional `Meter` (if Consumer role); creates and updates `AuditLog` entries; generates `Report` requests |
 
**Business Rules:**
- A user must have exactly one role from: Administrator, Analyst, Consumer, Technician, Executive.
- An account is automatically locked after 5 consecutive failed login attempts.
- A deactivated user's data is retained indefinitely but the account cannot be logged into.
- Only Administrators can create, update, or deactivate other user accounts.
---

### Entity 2: Meter
 
| Property | Detail |
|---|---|
| **Description** | Represents a physical smart electricity meter installed at a consumer's premises. A meter belongs to exactly one Zone and may be linked to one Consumer user. It is the primary source of raw consumption data in the system. |
| **Attributes** | `meterId: UUID`, `serialNumber: String`, `status: MeterStatus`, `installedAt: DateTime`, `zoneId: UUID`, `consumerId: UUID` (nullable), `lastReadingAt: DateTime`, `consecutiveRejectedReadings: Integer` |
| **Methods** | `activate()`, `deactivate()`, `decommission()`, `getLatestReading(): Reading`, `isOnline(): Boolean`, `isFaultSuspected(): Boolean` |
| **Relationships** | Belongs to one `Zone`; optionally linked to one Consumer `User`; generates many `Reading` objects; associated with many `Anomaly` objects |
 
**Business Rules:**
- A meter must belong to exactly one zone at any point in time.
- A meter is flagged offline if no reading is received within three consecutive expected polling intervals.
- A meter is flagged as FaultSuspected after three consecutive rejected readings.
- A decommissioned meter's ID and historical readings are retained for audit purposes.
---
### Entity 3: Zone
 
| Property | Detail |
|---|---|
| **Description** | Represents a geographic electricity distribution area (e.g., Bellville, Khayelitsha) managed by the municipality. A zone groups meters together and defines the thresholds used for anomaly detection and load monitoring. |
| **Attributes** | `zoneId: UUID`, `name: String`, `description: String`, `location: String`, `capacityKwh: Float`, `thresholdType: ThresholdType`, `thresholdValue: Float`, `status: ZoneStatus`, `createdAt: DateTime` |
| **Methods** | `activate()`, `deactivate()`, `updateThreshold()`, `getCurrentLoad(): Float`, `getLoadPercentage(): Float`, `computeStatus(): ZoneStatus` |
| **Relationships** | Contains many `Meter` objects (composition, meters cannot exist without a zone); has many `DailySummary` records; has many `Anomaly` records |
 
**Business Rules:**
- A zone must have at least one active meter to be considered Active.
- The threshold can be set as an absolute kWh value or as a percentage of the 30-day rolling average, not both simultaneously.
- Zone status (Normal / HighLoad / AnomalyAlert) is computed automatically from live readings and is never manually assigned.
- A zone's previous threshold values are stored in the audit history whenever a change is made.
---

### Entity 4: Reading
 
| Property | Detail |
|---|---|
| **Description** | Represents a single electricity consumption data point submitted by a meter at a specific point in time. It is the atomic unit of data in the ElectroView system and the trigger for anomaly detection. |
| **Attributes** | `readingId: UUID`, `meterId: UUID`, `kwhConsumed: Float`, `recordedAt: DateTime`, `receivedAt: DateTime`, `status: ReadingStatus`, `isAnomaly: Boolean` |
| **Methods** | `validate(): Boolean`, `persist()`, `checkAgainstThreshold(): Boolean`, `flag()` |
| **Relationships** | Belongs to one `Meter`; may produce one `Anomaly`; contributes to `DailySummary` aggregations |
 
**Business Rules:**
- A reading must have a positive kWh value; zero or negative values are rejected as invalid.
- A reading must include a valid meter ID that exists in the system and is in Active status.
- A reading that exceeds the zone threshold triggers anomaly creation within 10 seconds.
- Duplicate readings (same meter ID and timestamp) are detected and rejected without failing the batch.
---

### Entity 5: Anomaly
 
| Property | Detail |
|---|---|
| **Description** | Represents a detected electricity consumption irregularity associated with a specific meter reading that exceeded the configured zone threshold. An anomaly has a defined lifecycle managed by technicians and administrators. |
| **Attributes** | `anomalyId: UUID`, `meterId: UUID`, `readingId: UUID`, `thresholdAtTime: Float`, `actualValue: Float`, `status: AnomalyStatus`, `detectedAt: DateTime`, `assignedTo: UUID` (nullable), `resolvedAt: DateTime` (nullable), `resolutionNotes: String` (nullable) |
| **Methods** | `assign()`, `escalate()`, `resolve()`, `autoResolve()`, `isOverdue(): Boolean`, `getAgeInHours(): Integer` |
| **Relationships** | Associated with one `Reading`; associated with one `Meter`; linked to one resolving `User` (nullable) |
 
**Business Rules:**
- An anomaly cannot be set to Resolved status without a non-empty resolution note.
- An anomaly unresolved for more than 48 hours is automatically escalated.
- Resolved anomalies are retained in the log for a minimum of 12 months.
- An anomaly's threshold value at time of detection is stored immutably, subsequent threshold changes do not alter the recorded value.
---

### Entity 7: DailySummary
 
| Property | Detail |
|---|---|
| **Description** | Represents a pre-aggregated daily consumption record for a zone, computed from raw meter readings by a background job. Daily summaries enable fast query performance for trend charts and executive KPI views without scanning millions of raw reading records. |
| **Attributes** | `summaryId: UUID`, `zoneId: UUID`, `summaryDate: Date`, `totalKwh: Float`, `avgKwh: Float`, `peakKwh: Float`, `meterCount: Integer`, `computedAt: DateTime` |
| **Methods** | `compute()`, `recompute()`, `isStale(): Boolean` |
| **Relationships** | Belongs to one `Zone`; derived from many `Reading` objects (indirect — via meter and zone) |
 
**Business Rules:**
- A DailySummary is computed once per zone per calendar day by a scheduled background job.
- If new readings are ingested for a past date, the corresponding summary is recomputed.
- Summaries older than 12 months are downsampled to weekly aggregates to manage storage.
- Summaries are the primary data source for trend charts, raw readings are queried only for meter-level detail views.
---

## 3. Entity Relationship
 
```
User ──── has many ────► Session
User ──── (Consumer) linked to ──► Meter
User ──── requests ──────────────► Report
User ──── resolves ──────────────► Anomaly
 
Zone ◄─── contains (composition) ── Meter
Zone ──── has many ──────────────► DailySummary
Zone ──── has many ──────────────► Anomaly (via Meter)
 
Meter ──── generates many ──────► Reading
Meter ──── associated with ─────► Anomaly
 
Reading ──── may produce one ───► Anomaly
Reading ──── contributes to ────► DailySummary (via zone aggregation)
```
 
---