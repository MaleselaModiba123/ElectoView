# Kanban explanation — ElectroView: Electricity Usage Analytics Dashboard

---

## 1. What is a Kanban Board?
 
A Kanban board is a visual project management tool that represents the stages of a workflow as a sequence of columns, with each unit of work such as a task, user story, or issue are represented as a card that moves from left to right across those columns as it progresses toward completion. The term "Kanban" originates from the Japanese word for "signboard" and was first applied to software development as part of Agile and Lean methodologies.
 
The defining characteristics of a Kanban board are:
 
**Visual workflow representation.** The board makes the state of all work visible at a glance. Rather than reading through a list or a spreadsheet, a team can see in seconds which tasks are waiting, which are being actively worked on, which are under review, and which are done. This transparency is particularly valuable for identifying where work is accumulating or stalling.
 
**Work-in-Progress (WIP) limits.** A well-configured Kanban board constrains how many cards can occupy any given column simultaneously. This prevents the common failure mode of a team starting many tasks in parallel and completing none of them, a phenomenon sometimes called "multi-tasking illusion." By enforcing WIP limits, the board forces the team to finish existing work before pulling in new work.
 
**Continuous flow.** Unlike Scrum, which organises work into fixed-length sprints with defined start and end points, pure Kanban operates as a continuous flow, cards enter the board when ready and exit when done, without waiting for a sprint boundary. The ElectroView board operates as a hybrid: it uses sprint iterations from the Team Planning template to maintain Scrum-style delivery cadence while using Kanban's column-based visualisation for day-to-day task tracking.
 
**Adaptability.** Because the board reflects the real state of work at any moment, it allows the team to respond to changing priorities, newly discovered blockers, or updated requirements without waiting for the next sprint planning ceremony.
 
---

## 2. ElectroView Kanban Board Design
 
### 2.1 Column Structure
 
The ElectroView board extends the Team Planning template's four default columns with two custom additions, giving seven columns in total:
 
| Column | Purpose | WIP Limit |
|---|---|---|
| **Backlog** | All user stories not yet assigned to the current sprint. Contains Should-have and Could-have stories from the product backlog in `Agile_Planning.md`. | No limit |
| **Ready** | Stories and tasks that have been refined, estimated, and are ready to be pulled into active development in the current sprint. A card moves here once its acceptance criteria are confirmed. | 6 cards |
| **In Progress** | Tasks actively being developed. A card enters this column when a developer begins work and a linked branch is created. | 4 cards |
| **Testing** *(custom)* | Tasks that have been developed and are undergoing functional or integration testing against the test cases in `TestCases.md`. A card moves here when development is complete and the developer has self-reviewed their work. | 3 cards |
| **Blocked** *(custom)* | Tasks that cannot proceed due to an external dependency, unresolved decision, or technical blocker. Cards here are labelled with the reason for the block and an estimated resolution date. | No limit (high visibility) |
| **Done** | Tasks whose pull request has been merged, all acceptance criteria have been met, and the Definition of Done has been satisfied. | No limit |
 
### 2.2 Justification for Custom Columns
 
**Testing column.** The test cases defined in `TestCases.md` represent a formal validation step that must occur before any task is submitted for peer review. Without a dedicated Testing column, testing is either absorbed invisibly into "In Progress" (making it invisible to progress tracking) or skipped entirely under time pressure. Making it a named column ensures it is treated as a mandatory stage in the workflow rather than an optional afterthought.
 
**Blocked column.** In a solo development context, blockers are common: a required API key may not be available, a design decision may be unresolved, or a dependency between tasks may mean Task B cannot start until Task A is done. Rather than leaving blocked cards sitting in "In Progress" and inflating the apparent WIP count, moving them to a dedicated Blocked column makes the obstruction visible and separable from active work. It also provides a clear signal of what needs to be resolved to unblock the sprint.
 
### 2.3 Issue Labels
 
Each GitHub Issue linked to the board is labelled to enable filtering and visual differentiation:
 
| Label | Colour | Applied To |
|---|---|---|
| `must-have` | Pink | US-001, US-002, US-005, US-007, US-008, US-012 |
| `should-have` | Green | US-006, US-009, US-010 |
| `could-have` | Purple | US-011 |
| `feature` | Blue | All user story issues |

### 2.4 Sprint 1 Task Assignment on the Board
 
All 22 tasks from the Sprint 1 backlog in `Agile_Planning.md` are created as GitHub Issues, linked to their parent user story issue, and assigned to the `Sprint 1 — MVP Foundation` milestone. At sprint start, all Sprint 1 tasks are placed in the **Ready** column. As work begins, tasks are pulled into **In Progress** subject to the WIP limit of 4.
 
### 2.5 WIP Limit Enforcement
 
WIP limits are enforced manually in GitHub Projects (GitHub does not natively enforce column limits). The limits are documented in the board description and in this document. At each working session, before pulling a new card into In Progress, the developer checks that the current In Progress count is below 4. If it is not, the existing card must be moved forward (to Testing, Blocked, or In Review) before a new one is pulled.
 
---

## 3. How the Board Supports Agile Principles
 
**Continuous delivery.** The board makes the path from Backlog to Done visible and unambiguous. Each column represents one clearly defined stage, and the Definition of Done ensures that "Done" means genuinely shippable, not merely coded.
 
**Adaptability.** If a new requirement or bug surfaces mid-sprint, a new card can be added to Backlog or Ready without disrupting the structure of the board. The Blocked column allows unexpected obstacles to be surfaced without masking true progress.
 
**Transparency.** Any stakeholder, in ElectroView's case, the lecturer or a hypothetical product owner, can inspect the board at any time and immediately understand what is being worked on, what is waiting, and what has been completed.
 
**Focus.** The WIP limit on In Progress (4 cards) enforces the Agile principle of completing work before starting new work, reducing context-switching and improving the quality of each individual deliverable.
 
---