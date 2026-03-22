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

[SPECIFICATION.md](./Specification.md) | Full system specification including domain, problem statement, scope, functional and non-functional requirements, and use cases.
[ARCHITECTURE.md](./Architecture.md) | C4 architectural diagrams (Context, Container, Component, Code) with Mermaid source.
[STAKEHOLDERS.md](./Stakeholders.md) | Stakeholder analysis: 7 stakeholders with roles, key concerns, pain points, success metrics, and conflict mapping
[SRD.md](./SRD.md) | System Requirements Document: 12 functional requirements with acceptance criteria + 14 non-functional requirements across 6 quality categories.
[REFLECTION.md](./Reflection.md) | Reflection on challenges in balancing competing stakeholder needs during requirements engineering.

---
 
## Tech Stack (Planned)
 
- **Frontend:** React + TypeScript, Chart.js / Recharts
- **Backend:** Node.js / Express REST API
- **Database:** PostgreSQL (time-series meter data) + Redis (caching)
- **Auth:** JWT-based role authentication
- **Deployment:** Docker + GitHub Actions CI/CD

