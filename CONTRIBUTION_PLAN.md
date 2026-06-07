# CONTRIBUTION_PLAN.md

## Contribution Plan — Assignment 15

**Author:** Malesela Modiba ([@MaleselaModiba123](https://github.com/MaleselaModiba123))
**Home repository:** [ElectoView – Electricity Usage Analytics Dashboard](https://github.com/MaleselaModiba123/ElectoView)

---

## Overview

This document outlines my strategy for contributing to classmates' repositories as part of cross-project collaboration. My goal is to submit at least **3 high-quality pull requests that get merged**, including at least **one addressing a feature-request** for the bonus.

---

## Selection Criteria

Each candidate repository was screened against the following checklist so my contributions would be feasible, welcome, and mergeable:

- [x] Has a clear `CONTRIBUTING.md` (or equivalent guidelines).
- [x] Has open, **labelled** issues (`good-first-issue`, `help-wanted`, `feature-request`).
- [x] Has a working CI pipeline so I can verify changes pass before review.
- [x] Enforces branch protection requiring green CI (so merges are quality-gated).
- [x] Open work matches small, achievable tasks (services, endpoints, tests).

---

## Selected Projects

| # | Project | Repository | Owner | Why I chose it |
|---|---------|-----------|-------|----------------|
| 1 | SmartLibraryManagementSystem | [`ThatoMabilo/SmartLibraryManagementSystem`](https://github.com/ThatoMabilo/SmartLibraryManagementSystem) | Thato Mabilo | Clear `CONTRIBUTING.md`, labelled good-first-issues, CI passing |
| 2 | bello-beauty-academy | [`MaleselaModiba123/bello-beauty-academy`](https://github.com/MaleselaModiba123/bello-beauty-academy) | Aaniquah | Well-documented, issues I have the skills to tackle |
| 3 | HospitalPatientMonitoringSystem | [`MaleselaModiba123/hospitalpatientmonitoringsystem`](https://github.com/MaleselaModiba123/hospitalpatientmonitoringsystem) | Mbasa | Active repo with a feature-request I can attempt |

---

## Selected Issues

| Project | Issue | Type | My planned approach |
|---------|-------|------|---------------------|
| **1 — SmartLibraryManagementSystem** | [#20 — Implement BookService, MemberService and LoanService with business logic](https://github.com/ThatoMabilo/SmartLibraryManagementSystem/issues/20) | `good-first-issue` | Create a `RepositoryFactory` class that returns the correct repository implementation based on a storage-type string. This decouples the rest of the application from knowing which storage backend is in use, making it easy to switch from in-memory to a database in future without changing anything else. |
| **2 — bello-beauty-academy** | [#107 — Add `GET /api/students/active` endpoint](https://github.com/MaleselaModiba123/bello-beauty-academy/issues/107) | `good-first-issue` | Add a new endpoint `GET /api/students/active` that returns only students with at least one active enrollment, reusing the existing `findActiveStudents()` method on `StudentRepository`. |
| **3 — HospitalPatientMonitoringSystem** | [#33 — Write integration test for AlertService threshold](https://github.com/MaleselaModiba123/hospitalpatientmonitoringsystem/issues/33) | `feature-request` *(bonus)* | Write an integration test that verifies `AlertService` raises an alert when a reading crosses the configured threshold and raises none when readings stay below it, exercising the service end-to-end rather than mocking it. <!-- REFINE if you have a more specific plan --> |

---

## Strategy

My approach is staged to maximise the number of merged PRs:

1. **Start with documentation and test fixes.** These are low-risk for maintainers to approve, so they get merged fastest. This banks guaranteed points early.

2. **Comment on each issue before starting** to claim it and avoid duplicating another contributor's work, following each project's `CONTRIBUTING.md` guidance.

3. **Keep each PR small and focused** — one issue per PR — so reviews are quick and the change is easy to verify.

4. **Ensure CI passes** by running the project's test suite locally before pushing, since these repos enforce branch protection requiring green CI.

5. **Attempt one feature-request last** for the bonus, once I'm familiar with the contribution workflow from the easier PRs.

6. **Respond quickly to review feedback** to keep PRs moving toward merge.

---

## Risk Management

| Risk | Mitigation |
|------|-----------|
| Someone else claims my issue first | Comment to claim immediately; have backup issues identified per repo. |
| Maintainer is unresponsive | Three independent projects so a slow reviewer on one never blocks overall progress. |
| PR rejected / changes requested | Keep changes small and reversible; respond to feedback within 24h. |
| CI failing on the target repo | Reproduce each project's pipeline locally before pushing; never request review on a red build. |
