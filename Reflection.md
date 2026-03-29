# Reflection.md
## ElectroView: Electricity Usage Analytics Dashboard

---
 
## 1. Challenge 1: Data Access Breadth vs. Privacy and Security
 
The **Finance Department** needs detailed, meter-level consumption data to reconcile finance and resolve disputes. The **IT Administrator** and **Electrcity Distribution Manager**, on the other hand, are concerned about unnecessary exposure of consumer personal information.
 
This created a genuine conflict: giving finance staff broad data access speeds up their work, but it also increases the attack surface and risks non-compliance with POPIA (Protection of Personal Information Act), which governs how South African organisations handle personal data.
 
The resolution was **scoped RBAC exports**: finance staff can export consumption data (kWh values, meter IDs, timestamps) but the export schema explicitly excludes personal identifiers like names, addresses, and account numbers (reflected in FR-08's CSV schema). This satisfies the finance team's operational need while respecting the privacy boundary. It required careful thinking about what finance staff actually need to do their job versus what they might incidentally be exposed to.
 
This challenge reinforced that requirements are not simply a list of features — they encode policy decisions that have legal and ethical implications.
 
---

## 2. Challenge 2: Analytical Depth vs. Data Retention Cost
 
The **Electrcity Network Analyst** stakeholder has a strong interest in long data retention windows. Trend analysis becomes significantly more powerful with multiple years of historical data. It enables seasonal comparisons, multi-year growth analysis, and infrastructure investment modelling.
 
However, retaining full-resolution meter readings (potentially hundreds of thousands of records per day across all meters) indefinitely would result in unsustainable database growth, increasing storage costs and degrading query performance over time.
 
The resolution is a **tiered retention policy** (referenced in the stakeholder conflict table in `STAKEHOLDERS.md`): full-resolution readings are retained for 12 months, after which they are aggregated into daily summaries that are retained for 5 years. This preserves the analytical value that analysts care about most — long-term trends — while dramatically reducing storage requirements. The trade-off is that analysts lose per-hour granularity for periods older than a year. This is a deliberate, documented decision rather than an oversight.
 
---

## 3. Challenge 3: Simplicity for Consumers vs. Feature Depth for Analysts
 
Designing a single system that serves both the **Consumer** (non-technical, wants simplicity) and the **Electrcity Network Analyst** (technical, wants data depth and flexibility) creates an interface design challenge.
 
If the system is built for the analyst, it will overwhelm the consumer with options, filters, and terminology they do not understand. If it is built for the consumer, it will frustrate analysts who need raw control over their queries and visualisations.
 
The solution is a **role-differentiated UI**: consumers see a simplified, opinionated view of their own data (pre-selected chart type, plain-language labels, no filter controls beyond the billing period selector), while analysts and administrators see a full-featured dashboard with zone selectors, time range controls, overlay options, and export capabilities. Both experiences are served by the same underlying API — the differentiation is in the frontend rendering logic. This approach is reflected in FR-07 (consumer view) and FR-04/FR-08 (analyst capabilities) as separate, distinct requirements rather than a single unified one.

---

## 4. Challenges with translating requirements to use cases and test

The process of translating the user requirments into use cases and test cases created challenges that were different from when the user requirments were created. Requirements engineering is primarily about understanding and documenting what a system must do, use case modeling and test case development demand a more precise, executable understanding of how the system behaves — and that precision has a way of exposing ambiguities that seemed resolved at the requirements level.

The most persistent challenge was deciding on the appropriate level of granularity for use cases. When drafting the use cases, I would model every system interaction as its own use case, which quickly produced a diagram too dense to be useful. Login, token refresh, session expiry, and role validation were all initially separated. These were collapsed into a single UC-01 (Login and Authenticate) with alternative flows handling the error paths. The principle that guided this consolidation was to ask whether a use case represented a goal that a user consciously pursues users log in with the goal of accessing the system, but they do not consciously pursue "token refresh" as a goal. Applying this consistently reduced the use case count to a manageable set that remained meaningful to both technical and non-technical readers.

The second challenge came from the automated actor the System / Scheduler responsible for anomaly detection (UC-04). I wanted to model anomaly detection as part of UC-02 (View Zone Dashboard), since that is where its results are visible. However, this conflated two distinct processes: the background detection logic, which runs continuously regardless of whether any user is viewing the dashboard, and the dashboard display, which is a user-initiated act. Separating them as distinct use cases better reflected the system's actual architecture and made the test case for UC-04 (TC-005 and TC-006) cleaner to write, since the trigger is an API event rather than a UI interaction.

The test cases created their own challenge: the need to make requirements falsifiable. Several requirements from the SRD were well-intentioned but not directly testable as written. For example, the original NFR-U02 stated that a new user should be able to complete three core tasks "within 10 minutes of first login." This is difficult to validate through automated testing as it requires a usability study with real participants and a defined task completion protocol.

Security test cases challenges were, deciding what to test without simulating a genuine attack. Testing that rate limiting triggers at 10 requests per minute (TC-NFR-S02) is a straightforward boundary test. However, other NFRs, such as AES-256 column-level encryption (NFR-SEC04) and bcrypt hashing strength (NFR-SEC02), cannot be verified purely through black-box API testing. Validating them requires inspection of the codebase or database schema. This raised an important insight about the nature of security testing: not all security requirements are verifiable through runtime behaviour; some require static analysis or code review as part of the testing strategy.