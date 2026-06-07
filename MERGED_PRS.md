# MERGED_PRS.md

## Pull Requests — Submitted & Merged

---

## Summary Table

| # | Project | PR Title | Type | Issue | PR Link | CI | Status |
|---|---------|----------|------|-------|---------|----|--------|
| 1 | `ThatoMabilo/SmartLibraryManagementSystem` | Implement service layer with RepositoryFactory | good-first-issue | [#20](https://github.com/ThatoMabilo/SmartLibraryManagementSystem/issues/20) | <!-- PR URL --> | <!-- ✅ --> | 🟡 Planned |
| 2 | `MaleselaModiba123/bello-beauty-academy` | Add `GET /api/students/active` endpoint | good-first-issue | [#107](https://github.com/MaleselaModiba123/bello-beauty-academy/issues/107) | <!-- PR URL --> | <!-- ✅ --> | 🟡 Planned |
| 3 | `MaleselaModiba123/hospitalpatientmonitoringsystem` | Add integration test for AlertService threshold | feature-request | [#33](https://github.com/MaleselaModiba123/hospitalpatientmonitoringsystem/issues/33) | <!-- PR URL --> | <!-- ✅ --> | 🟡 Planned |

**Merged PRs counted toward grade:** target 3 × +10 = **30 / 30** (capped)
**Bonus eligible:** PR #3 is a feature-request → **+5** when merged.

---

## PR #1 — Implement service layer with RepositoryFactory
- **Repository:** [`ThatoMabilo/SmartLibraryManagementSystem`](https://github.com/ThatoMabilo/SmartLibraryManagementSystem)
- **Owner:** Thato Mabilo
- **Issue addressed:** [#20 — Implement BookService, MemberService and LoanService with business logic](https://github.com/ThatoMabilo/SmartLibraryManagementSystem/issues/20)
- **PR link:** https://github.com/ThatoMabilo/SmartLibraryManagementSystem/pull/37
- **Type:** good-first-issue
- **Status:**  Mergred 

**Summary of changes**
- Implemented `BookService`, `MemberService`, and `LoanService` with their business logic.
- Added a `RepositoryFactory` class that returns the correct repository implementation based on a storage-type string, decoupling the application from the storage backend so it can switch from in-memory to a database later without further changes.
- Added unit tests covering the new service logic.

**Commit message used**
```
Feat: Add service layer (Book/Member/Loan) backed by RepositoryFactory
```

**CI evidence**
![CI passing for PR #1](<!Screenshot 2026-06-07 at 21.23.15.png -->)

---

## PR #2 — Add `GET /api/students/active` endpoint
- **Repository:** [`MaleselaModiba123/bello-beauty-academy`](https://github.com/MaleselaModiba123/bello-beauty-academy)
- **Owner:** Aaniquah
- **Issue addressed:** [#107 — Add `GET /api/students/active` endpoint](https://github.com/MaleselaModiba123/bello-beauty-academy/issues/107)
- **PR link:** https://github.com/Aaniquah222641495/bello-beauty-academy/pull/114
- **Type:** good-first-issue
- **Status:** 🟡 Sent

**Summary of changes**
- Added a new endpoint `GET /api/students/active` that returns only students with at least one active enrollment.
- Reused the existing `findActiveStudents()` method on `StudentRepository` rather than duplicating query logic.
- Added a test verifying only active-enrolled students are returned.

**Commit message used**
```
Feat: Add GET /api/students/active endpoint for active-enrolled students
```

**CI evidence**
![CI passing for PR #2](<!-- -->)

---

## PR #3 — Add integration test for AlertService threshold *(feature request — bonus)*
- **Repository:** [`MaleselaModiba123/hospitalpatientmonitoringsystem`](https://github.com/MaleselaModiba123/hospitalpatientmonitoringsystem)
- **Owner:** Mbasa
- **Issue addressed:** [#33 — Write integration test for AlertService threshold](https://github.com/MaleselaModiba123/hospitalpatientmonitoringsystem/issues/33)
- **PR link:** (https://github.com/Mbasa6/HospitalPatientMonitoringSystem/pull/38)
- **Type:** feature-request *(eligible for +5 bonus)*
- **Status:** 🟡 Sent

**Summary of changes**
- Added an integration test that verifies `AlertService` raises an alert when a reading crosses the configured threshold and raises none when readings stay below it.
- Exercised the service end-to-end rather than mocking it, increasing confidence in the alerting path.
- Discussed the test approach in the issue thread before coding to align with the maintainer.

**CI evidence**
![CI passing for PR #3](<!-- screenshots/pr3-ci.png -->)

---

## Screenshots Index

| Screenshot | File |
|-----------|------|
| PR #1 CI passing | `Screenshot 2026-06-07 at 21.23.15.png` |
| PR #2 CI passing | `screenshots/pr2-ci.png` |
| PR #3 CI passing | `screenshots/pr3-ci.png` |

> Place the screenshot image files in a `screenshots/` folder next to this document and confirm the relative paths above resolve.
