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

[System Requirments Document.md](./SRD.md) System Requirements Document: 12 functional requirements with acceptance criteria + 14 non-functional requirements across 6 quality categories.

[Use Cases](./UseCases.md) UML use case diagram (Mermaid), actor descriptions, 8 detailed use case specifications with flows and alternative flow.

[Test Cases](./TestCases.md) Functional test cases + non-functional test cases 

[Agile Panning](./Agile_Planning.md) 12 user stories (INVEST-compliant), MoSCoW-prioritised product backlog, Sprint 1 plan with 22 tasks

[REFLECTION.md](./Reflection.md) Reflection on challenges faced in the documentation of the project.


---
 
## Tech Stack (Planned)
 
- **Frontend:** React + TypeScript, Chart.js / Recharts
- **Backend:** Node.js / Express REST API
- **Database:** PostgreSQL (time-series meter data) + Redis (caching)
- **Auth:** JWT-based role authentication
- **Deployment:** Docker + GitHub Actions CI/CD

