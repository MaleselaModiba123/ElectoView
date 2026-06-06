# ElectroView Roadmap

This document outlines planned features and improvements for ElectroView.
Contributions toward any of these are welcome, see [Contributing.md](./Contributing.md).

---

## Near-Term (Next Release)

- [ ] **Redis caching layer** — Cache aggregated zone summaries to reduce
      database load on the dashboard (currently every request hits MySQL).
- [ ] **JWT authentication** — Replace the currently-disabled Spring Security
      with full JWT-based authentication and role-based access control.
- [ ] **Email alert dispatch** — Wire up real email sending (SendGrid) for
      anomaly alerts, replacing the current in-app-only notifications.
- [ ] **Bulk CSV import endpoint** — Allow analysts to back-populate
      historical meter readings via CSV upload.

---

## Mid-Term

- [ ] **Real-time dashboard updates** — Replace 5-minute polling with
      WebSocket push for live consumption monitoring.
- [ ] **PDF report generation** — Implement actual PDF rendering for the
      report export feature (currently a placeholder).
- [ ] **FileSystem repository implementation** — Complete the JSON file-based
      repository stub (currently throws UnsupportedOperationException).
- [ ] **Anomaly auto-escalation scheduler** — Background job that
      automatically escalates anomalies unresolved after 48 hours.

---

## Long-Term

- [ ] **React frontend** — Build the consumer and admin dashboards in
      React + TypeScript with Chart.js visualisations.
- [ ] **Machine learning anomaly detection** — Replace threshold-based
      detection with a trained model for predictive fault detection.
- [ ] **Multi-tenancy** — Support multiple municipalities on a single
      deployment with isolated data.
- [ ] **Mobile app** — Native mobile application for field technicians to
      resolve anomalies on-site.
- [ ] **Docker Compose deployment** — One-command deployment of the full
      stack (API, MySQL, Redis) via Docker Compose.

---

## Completed

- [x] Domain model with 7 entities
- [x] All 6 creational design patterns
- [x] Generic repository layer with in-memory implementation
- [x] Service layer with business logic
- [x] REST API with Swagger documentation
- [x] CI/CD pipeline with GitHub Actions
- [x] Branch protection and PR workflow