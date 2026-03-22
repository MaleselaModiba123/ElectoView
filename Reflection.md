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