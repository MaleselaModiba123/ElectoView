# GitHub Template Analysis — ElectroView: Electricity Usage Analytics Dashboard

---

| Criteria | Kanban | Automated Kanban | Bug Triage | Team Planning |
|---|---|---|---|---|
| **Default Columns** | To Do, In Progress, Done | To Do, In Progress, Done | Needs Triage, High Priority, Low Priority, Closed | Backlog, Ready, In Progress, In Review, Done |
| **Number of Default Columns** | 3 | 3 | 4 | 5 |
| **Automation Features** | None, all card movement is manual | Issues auto-move to In Progress when a linked PR is opened; auto-move to Done when PR is merged or issue is closed | Issues auto-move to Needs Triage when created; auto-close moves to Closed | Limited automation; supports milestone and iteration tracking |
| **Issue Linking** | Manual, cards added individually | Full. Issues and PRs linked and tracked automatically | Full. Designed around GitHub Issues as the primary input | Full. Issues linked to iterations and milestones |
| **Sprint / Iteration Support** | No native sprint support | No native sprint support | No. Designed for bug management, not sprint cycles | Yes, built around iterations (sprints) with start/end dates |
| **WIP Limit Support** | No built-in WIP limits | No built-in WIP limits | No built-in WIP limits | No built-in WIP limits (must be enforced manually or via custom fields) |
| **Custom Fields** | No | No | No | Yes, supports Priority, Status, Iteration, and custom fields |
| **Suitability for Agile / Scrum** | Low,  minimal structure, no sprint tracking | Medium, automation reduces manual overhead but lacks sprint organisation | Low,  purpose-built for bug management, not feature delivery | High, designed for Agile teams with sprint planning, backlog, and review stages |
| **Best Suited For** | Small personal projects or simple task lists | Solo or small team projects needing automation without ceremony | Teams managing a high volume of bug reports | Agile teams running sprint-based development with defined roles |
| **Customisability** | High, simple base to extend | Medium, automation rules may conflict with added columns | Low, structure is tightly coupled to bug workflow | High, columns, fields, and iterations are all configurable |

---
 
## 3. Selected Template: Team Planning
 
### 3.1 Justification
 
The **Team Planning** template is the most appropriate choice for the ElectroView project for the following reasons:
 
**Alignment with Agile/Scrum workflow.** The ElectroView project has been planned using Scrum-based Agile methodology. The Team Planning template is the only template that natively supports iterations (sprints) with defined start and end dates, which directly mirrors the Sprint 1 plan documented in `Agile_Planning.md`. The five default columns including Backlog, Ready, In Progress, In Review, Done, map naturally onto the lifecycle of each task defined in the sprint backlog.
 
**Backlog management.** The product backlog created previously contains 12 user stories at various priority levels. The Team Planning template's dedicated Backlog column allows lower-priority stories (Should-have and Could-have) to sit in the backlog visibly without cluttering the active sprint columns. This separation is not possible in the Basic or Automated Kanban templates, which only offer a single "To Do" column.
 
**In Review stage.** The Definition of Done established in `Agile_Planning.md` requires that all completed work pass through a code review and merge process before being marked Done. The Team Planning template's "In Review" column makes this stage explicit and visible, whereas the Basic and Automated Kanban templates move directly from In Progress to Done with no intermediate review state.
 
**Custom fields for priority and iteration.** The Team Planning template supports custom fields, allowing each card to display its MoSCoW priority (Must-have, Should-have, Could-have) and its sprint iteration assignment. This enables the board to reflect the backlog structure from `Agile_Planning.md` without requiring separate documentation.
 
**Extensibility.** Two additional columns such as "Testing" and "Blocked" need to be added to the default template (see Section 4 of `kanban_explanation.md`). The Team Planning template's column structure is straightforward to extend, and its custom field support means these additions do not break existing automation.