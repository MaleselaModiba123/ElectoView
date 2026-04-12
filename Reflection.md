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

---

# 5. Challenges in Agile Planning

Agile methodology is designed for teams. Its ceremonies, sprint planning, daily standups, retrospectives assume multiple people with distinct roles: a Product Owner who holds the vision, a Scrum Master who facilitates the process, and a development team that executes. Working through this assignment as a solo practitioner meant collapsing all of those roles into one person, and the friction that created was instructive in ways that reading about Agile rarely is.

The first and most persistent problem was in prioritisation. The MoSCoW method asks you to be decisive about what is truly essential versus what would merely be nice to have. In a real project with real stakeholders, this decision is made through negotiation. A finance staff member pushes for the report export feature, a consumer advocate pushes for the budget alert, and the Product Owner mediates. Doing this alone meant having those arguments with myself. There were moments where I genuinely could not determine whether the bulk CSV import (US-010) was a "Must-have" or a "Should-have." On one hand, analysts cannot perform meaningful long-term trend analysis without historical data. On the other hand, the system generates new data from day one of deployment, and trend analysis grows in value over time. I resolved this by returning to the stakeholder success metrics in STAKEHOLDERS.md specifically, whether the feature was required to meet any success metric in the first release window. It was not, so it became a "Should-have." But this kind of deliberate reasoning is something a Product Owner in a real team would do collaboratively, with someone to push back. Doing it in isolation required inventing a second perspective and arguing against my own instincts, which is more cognitively demanding than it sounds.

Effort estimation presented a different kind of problem. Story point estimation in Scrum is a team activity precisely because individual estimates are unreliable planning poker exists to surface disagreement and force calibration. Estimating alone risks anchoring on the first number that comes to mind. I noticed this happening when estimating T-011 (Zone Dashboard UI).

Sprint scope selection also created internal tension. The pull toward including more stories in Sprint 1 was strong, partly ambition, partly a reluctance to defer things I found technically interesting, like the executive KPI dashboard (US-011). Scrum's discipline around sprint capacity exists precisely to counter this. With a 2-week sprint and a solo developer, committing to 19 story points across 5 stories and 83 estimated hours is already ambitious. Including US-011 or US-003 would have produced an overloaded sprint that could not realistically be completed, which defeats the purpose of sprint planning. The resistance I felt when moving those stories to Sprint 2 was real but recognising that resistance as the same instinct that causes scope creep in professional projects made it easier to manage.

The lesson of this assignment is that Agile's structure is not bureaucratic overhead, it is a system of constraints that protects development teams from their own ambitions and from the unrealistic demands of stakeholders. Simulating those constraints alone, without the natural resistance that a real team and real stakeholders provide, required significant deliberate effort. That effort itself was the learning.

---

# 6. Reflection of Kanban board creation

The process of selecting a GitHub project template and customising a Kanban board for ElectroView raised questions I had not anticipated, particularly around the gap between what project management tools offer by default and what a real development workflow actually requires.

The template selection itself was more consequential than it initially appeared. GitHub's templates look similar on the surface but they are all just columns with cards and the differences in their assumptions about workflow reveal fundamentally different philosophies. The Basic Kanban assumes that "To Do, In Progress, Done" captures everything worth tracking, which is true for simple personal task lists but collapses important distinctions in a software project. The distinction between a task that is done-but-not-reviewed and a task that is genuinely done-and-merged matters enormously in practice: a feature that exists on a development branch but has not been reviewed and merged to main cannot be demonstrated to a stakeholder and cannot be built upon by other tasks. Collapsing that into a single "Done" column makes a project look further along than it is.

This insight sharpened during the Automated Kanban evaluation. The appeal of automation is real, having a card move automatically from In Progress to Done when a pull request is merged reduces the manual overhead of board maintenance. But for ElectroView, the automation would have skipped the Testing stage entirely: a PR could be opened, merged, and the card marked Done without any validation against the test cases defined in `TestCases.md`. Automation that bypasses a mandatory quality step is worse than no automation, because it creates a false sense of progress. This was the clearest lesson of the template analysis: automation is only as good as the workflow it encodes. If the workflow has gaps, automation makes those gaps invisible rather than fixing them.

The customisation decisions of adding the Testing and Blocked columns were straightforward in their rationale but required resisting the pull toward simplicity. Both additions increase the board's complexity, and there is always a legitimate argument that simpler boards are more likely to be maintained consistently. A board with six columns that is kept up to date is more valuable than a board with three columns that nobody moves cards on. The counter-argument, which ultimately prevailed, is that the Testing and Blocked stages represent real states that tasks enter and exit in this project. Hiding them in a simpler column structure does not eliminate them, it just makes them invisible. Making them visible creates the accountability to manage them.

Comparing GitHub Projects to other tools clarifies what GitHub does and does not do well. Trello is more flexible visually and easier for non-developers to interact with, but it has no native integration with code repositories, a card in Trello has no awareness of whether its associated pull request has been merged. This disconnect between the task board and the codebase is a meaningful weakness for software projects. Jira, on the other hand, offers deep Agile support, velocity tracking, burndown charts, sprint reports, and configurable WIP limit enforcement, that GitHub Projects simply does not provide. For a professional team running multiple squads with formal sprint reporting requirements, Jira's depth is worth its complexity and cost. For a student project or a small team already working in GitHub, the overhead of maintaining a separate Jira instance is difficult to justify. GitHub Projects occupies a useful middle ground: tightly integrated with the repository, free, and sufficient for straightforward Agile workflows, but without the analytical depth of a dedicated project management platform.