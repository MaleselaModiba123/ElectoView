# SPECIFICATION — ElectroView: Electricity Usage Analytics Dashboard

## 1. Project Title - ElectroView

## 2. Domain
 
**Domain: Electricity Distribution & Management**
The electrcity distribution and management domain encompasses the generation, distribution, metering, monitoring, and billing of electrical power to residential, commercial, and municipal consumers. In South Africa, electricity distribution is handled by entities such as Eskom and local municipalities which manage large-scale infrastructure including substations, distribution networks, and smart metering systems.
 
This domain is increasingly driven by digital transformation — smart meters, IoT sensors, and SCADA systems generate vast volumes of consumption data that require analytics platforms to derive actionable insights, detect waste or faults, and enable demand-side management.

---
 
## 3. Problem Statement

The problem is that many municipalities in South Africa are struggling to provide uninterrupted electricity to their residents due to infrastructure challenges such as energy dissipation during transmission and distribution, theft and vandalism, meter tampering, and illegal connections.

**ElectroView** addresses these problems by providing a centralised, web-based analytics dashboard that aggregates meter data, visualises consumption trends, detects anomalies, and generates exportable reports enabling both utility managers and consumers to make data-driven decisions.

---
 
## 4. Individual Scope (Feasibility Justification)

| In Scope | Out of Scope |
|---|---|
| Dashboard UI with real-time and historical charts | Physical IoT hardware integration |
| Simulated/mock meter data ingestion via REST API | Actual SCADA or smart meter protocol integration (e.g., DLMS/COSEM) |
| User authentication (Admin, Analyst, Consumer roles) | Full billing/payment processing module |
| Anomaly detection using threshold rules | Machine learning-based predictive forecasting |
| PDF/CSV report export | Multi-language localisation |
| Zone-level and household-level consumption views | Native mobile application |

The system will use a simulated data generator to populate the database with realistic electricity readings, allowing full demonstration of all dashboard features without requiring live infrastructure.

---
 
## 5. Functional Requirements
 
### 5.1 User Management
- The system shall allow users to register and log in using email and password.
- The system shall support three roles: **Administrator**, **Analyst**, and **Consumer**.
- Administrators shall be able to create, update, and deactivate user accounts.
 
### 5.2 Data Ingestion
- The system shall accept meter reading submissions via a REST API endpoint.
- The system shall store readings with timestamps, meter ID, zone ID, and consumption value (kWh).
- The system shall support bulk ingestion of historical data via CSV upload.
 
### 5.3 Dashboard & Visualisation
- The dashboard shall display a real-time consumption overview for all active zones.
- The system shall render line charts showing hourly, daily, and monthly consumption trends.
- The system shall display a heatmap of consumption intensity by zone.
- The dashboard will show current peak demand and average consumption KPIs.
 
### 5.4 Anomaly Detection
- The system shall flag meter readings that exceed a configurable threshold.
- Anomalies shall trigger an in-app alert notification for administrators.
- The system shall maintain an anomaly log accessible to Analysts and Administrators.
 
### 5.5 Reporting
- Users shall be able to generate usage reports filtered by date range, zone, and meter.
- Reports shall be exportable in PDF and CSV formats.
 
---

## 6. Non-Functional Requirements
 
| Category | Requirement |
|---|---|
| Performance | Dashboard shall load within 2 seconds under normal load |
| Scalability | API shall handle at least 500 concurrent meter data submissions |
| Security | All API endpoints shall require JWT authentication |
| Availability | System shall target 99.5% uptime |
| Usability | UI shall be responsive and accessible on desktop and tablet |
| Maintainability | Codebase shall follow modular architecture with documented components |

---
 
## 7. Use Cases
 
### UC-01: View Zone Consumption Dashboard
- **Actor:** Analyst, Administrator
- **Precondition:** User is logged in
- **Flow:** User selects a zone from the map/list → System retrieves and displays real-time and historical charts for that zone
- **Postcondition:** Consumption data is displayed
 
### UC-02: Detect and Alert Anomaly
- **Actor:** System (automated), Administrator
- **Flow:** System receives meter reading → Compares against threshold → If exceeded, creates anomaly record → Sends in-app alert to Admin
- **Postcondition:** Anomaly is logged and Admin is notified
 
### UC-03: Generate Usage Report
- **Actor:** Analyst
- **Flow:** Analyst selects date range and zone → System aggregates data → Analyst downloads PDF/CSV
- **Postcondition:** Report file is generated and downloaded
 
### UC-04: Consumer Views Personal Usage
- **Actor:** Consumer
- **Flow:** Consumer logs in → Dashboard displays their meter's consumption history → Consumer can compare current vs previous billing period
- **Postcondition:** Consumer has visibility into their usage
 
---