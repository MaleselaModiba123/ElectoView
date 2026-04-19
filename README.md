# ElectroView - Electricity Usage Analytics Dashboard

ElectroView is a real-time data analytics dashboard application for monitoring and analysing electricity consumption across residential and municipal infrastructure. The system takes in meter readings, visualises usage patterns, detects anomalies, and generates reports to support informed decisions by utility providers and end-users.

Once completed, ElectroView will:
- Live electricity consumption monitoring per zone/household
- Historical trend analysis and forecasting
- Anomaly and fault detection alerts
- Usage reports exportable for billing and auditing
- Role-based access for administrators, analysts, and consumers

---
 
## Project Documents

[Specification](./Specification.md) Full system specification including domain, problem statement, scope, functional and non-functional requirements, and use cases.

[Architecture](./Architecture.md) C4 architectural diagrams (Context, Container, Component, Code) with Mermaid source.

[Stakeholders](./Stakeholders.md) Stakeholder analysis: 7 stakeholders with roles, key concerns, pain points, success metrics, and conflict mapping.

[System Requirments Document](./SRD.md) System Requirements Document: 12 functional requirements with acceptance criteria + 14 non-functional requirements across 6 quality categories.

[Use Cases](./UseCases.md) UML use case diagram (Mermaid), actor descriptions, 8 detailed use case specifications with flows and alternative flow.

[Test Cases](./TestCases.md) Functional test cases + non-functional test cases 

[Agile Panning](./Agile_Planning.md) 12 user stories (INVEST-compliant), MoSCoW-prioritised product backlog, Sprint 1 plan with 22 tasks

[REFLECTION](./Reflection.md) Reflection on challenges faced in the documentation of the project.

[template_analysis](./template_analysis.md) Comparison of 4 GitHub project templates with justification for selecting Team Planning.

[kanban_explanation.](./kanban_explanation.md) Kanban board definition, 7-column design rationale, WIP limits, and README section for the board.

[State_Diagrams](./State_Diagrams.md) State transition diagrams for 8 objects: User Account, Meter Reading, Anomaly, Consumption Report, Zone, Smart Meter, Consumer Budget, User Session.

[Activity_Diagrams](./Activity_Diagram.md) Activity diagrams for 8 workflows: Login, Meter Ingestion, Anomaly Resolution, Consumer Dashboard, Report Export, Threshold Config, User Management, Executive KPI

## Project Board
 
ElectroView uses a GitHub Project (Team Planning template) as its Agile Kanban board.
The board tracks all user stories and sprint tasks across seven columns:
 
Backlog → Ready → In Progress → Testing → Blocked → Done
 
Two custom columns were added beyond the default template:
- **Testing** — ensures all tasks pass the test cases defined in TEST_CASES.md
  before entering code review, making QA a visible and mandatory workflow stage.
- **Blocked** — surfaces tasks that cannot proceed due to dependencies or unresolved
  decisions, separating them from active work to keep WIP counts accurate.
 
WIP limits: In Progress (max 4), Testing (max 3).
All Sprint 1 issues are linked to the milestone: Sprint 1 — MVP Foundation.

![alt text](<Screenshot (83).png>)

![alt text](<Screenshot (84).png>)

---
 
## Tech Stack (Planned)
 
- **Frontend:** React + TypeScript, Chart.js / Recharts
- **Backend:** Node.js / Express REST API
- **Database:** PostgreSQL (time-series meter data) + Redis (caching)
- **Auth:** JWT-based role authentication
- **Deployment:** Docker + GitHub Actions CI/CD

