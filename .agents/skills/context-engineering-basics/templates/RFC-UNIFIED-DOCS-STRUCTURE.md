---
name: RFC-UNIFIED-DOCS-STRUCTURE
description: |
  RFC template for the unified docs/ directory structure standard for human documentation.
  Use when: proposing documentation organization standards.
---

# RFC: Unified docs/ Structure for Human Documentation

> **Version:** 1.0.0
> **Date:** 2026-01-16
> **Status:** APPROVED
> **Based on:** RFC-001, RFC-002, RFC-CONTEXT-ENGINEERING

---

## 1. Executive Summary

This RFC defines the standardized `docs/` directory layout for documentation aimed at **humans** (developers, analysts, stakeholders).

**Core principle:**
- `.context/` -> For AIs (active context, just-in-time loading)
- `docs/` -> For Humans (archival record, governance, traceability)

---

## 2. Directory Structure

### 2.1. Basic Level (Minimum Required)

```
docs/
├── README.md               # Index and navigation
├── cards/                  # Work units (Cards)
│   └── CARD-001-xxx.md
└── decisions/              # ADRs (Architecture Decision Records)
    └── ADR-001-xxx.md
```

### 2.2. Full Level (Critical Projects)

```
docs/
├── README.md               # Index and navigation
├── cards/                  # Work Units
│   ├── CARD-001-xxx.md
│   └── _TEMPLATE.md
├── decisions/              # ADRs
│   ├── ADR-001-xxx.md
│   └── _TEMPLATE.md
├── requirements/           # System Specifications
│   ├── README.md
│   ├── technical/          # TRs (Constraints, Stack)
│   │   └── TR-001-xxx.md
│   ├── quality/            # QRs (NFRs: Performance, Security)
│   │   └── QR-001-xxx.md
│   └── business/           # BRs + Use Cases
│       └── BR-001-xxx.md
├── diagrams/               # Visual Representations
│   ├── README.md
│   ├── class/              # Static structure
│   ├── sequence/           # Dynamic behavior
│   ├── c4/                 # C4 Model
│   └── other/
├── requests-for-comments/  # RFCs (Change Proposals)
│   ├── RFC-001-xxx.md
│   └── _TEMPLATE.md
├── research/               # Business/Technical Research
│   ├── README.md
│   └── RES-001-xxx.md
├── tech-debt/              # Technical Debt Registry
│   ├── TD-001-xxx.md
│   └── _TEMPLATE.md
├── plan/                   # Implementation Plans
│   ├── PLAN-001-xxx.md
│   └── _TEMPLATE.md
└── ARCHITECTURE.md         # System Overview (Mental Map)
```

---

## 3. Definitions and Purposes

| Directory | Purpose | Volatility | Example |
|-----------|---------|------------|---------|
| `cards/` | Where work is planned | High (task -> done -> archive) | CARD-001-crud-company.md |
| `decisions/` | History of "why" | Low (immutable) | ADR-001-choose-postgresql.md |
| `requirements/` | Current system truth | Medium (updated) | BR-001-tax-calculation.md |
| `diagrams/` | Visualizations | Medium | sequence-checkout.mmd |
| `requests-for-comments/` | Change proposals | High (proposed -> accepted/rejected) | RFC-001-new-architecture.md |
| `research/` | Documented research | Low | RES-001-esocial-2024.md |
| `tech-debt/` | Technical debt | Medium | TD-001-missing-service-tests.md |
| `plan/` | Implementation plans | High | PLAN-001-java17-migration.md |

---

## 4. Templates

### 4.1. Card (CARD-XXX.md)

```markdown
# CARD-XXX: [Title]

## 1. Description & User Story
"As a [Persona], I want [Action], so that [Value]."

## 2. Business Rules (BR)
* [BR01] The calculation must consider X...

## 3. Technical Rules (TR)
* [TR01] Must use Java 17 Records
* [TR02] (Tier 0) Lombok forbidden

## 4. Quality Requirements (QR)
* [QR01] Coverage >= 90%
* [QR02] Response time < 500ms

## 5. Acceptance Criteria
- [ ] Unit test covering scenario X
- [ ] Endpoint returns 200 for success case
- [ ] Documentation updated

## 6. References
- ADR-001: [Link]
- CARD-YYY: [Dependency]

## 7. Metadata
| Field | Value |
|-------|-------|
| **Status** | Backlog / In Progress / Done |
| **Priority** | High / Medium / Low |
| **Estimate** | [X]h |
| **Assignee** | [Name] |
```

### 4.2. ADR (ADR-XXX.md)

```markdown
# ADR-XXX: [Decision Title]

**Status:** Proposed | Accepted | Rejected | Superseded by ADR-YYY
**Date:** YYYY-MM-DD
**Authors:** [Names]

## Context
[What problem or need motivated this decision?]

## Decision
[What was decided?]

## Consequences
### Positive
- ...
### Negative
- ...

## Alternatives Considered
### Alternative 1: [Name]
**Description:** ...
**Why rejected:** ...
```

### 4.3. RFC (RFC-XXX.md)

```markdown
# RFC-XXX: [Proposal Title]

| Field | Value |
|-------|-------|
| **Status** | Proposed / Under Review / Approved / Rejected |
| **Created** | YYYY-MM-DD |
| **Review Deadline** | YYYY-MM-DD |
| **Author(s)** | [Names] |

## Executive Summary
[3-5 lines summarizing the proposal]

## Problem Statement
[Detailed problem description]

## Proposed Solution
[Solution description]

## Impact
[How it affects the system/team]

## Success Criteria
[How to measure success]
```

### 4.4. Tech Debt (TD-XXX.md)

```markdown
# TD-XXX: [Debt Title]

**Status:** Identified | In Resolution | Resolved
**Severity:** Critical | High | Medium | Low
**Date Identified:** YYYY-MM-DD

## Description
[What the technical debt is]

## Impact
[How it affects the system]

## Root Cause
[Why it exists]

## Proposed Solution
[How to resolve it]

## Estimated Effort
[Time/cost to resolve]
```

---

## 5. docs/README.md (Index)

```markdown
# docs/ - Project Documentation

> **AI Notice**: This directory is an archival record for humans.
> For active context, consult `/.context/` first.

## Structure
| Directory | Content |
|-----------|---------|
| `cards/` | Work units (tasks) |
| `decisions/` | ADRs (architectural decisions) |
| `requirements/` | Specifications (BR, TR, QR) |
| `diagrams/` | Visualizations (Mermaid, PlantUML) |
| `requests-for-comments/` | Change proposals |
| `research/` | Documented research |
| `tech-debt/` | Technical debt |
| `plan/` | Implementation plans |

## Naming Conventions
| Type | Format | Example |
|------|--------|---------|
| Card | `CARD-XXX-description.md` | CARD-001-crud-company.md |
| ADR | `ADR-XXX-description.md` | ADR-001-choose-postgresql.md |
| RFC | `RFC-XXX-description.md` | RFC-001-new-architecture.md |
| Research | `RES-XXX-description.md` | RES-001-esocial-2024.md |
| Debt | `TD-XXX-description.md` | TD-001-missing-tests.md |
| Plan | `PLAN-XXX-description.md` | PLAN-001-java17-migration.md |
```

---

## 6. Conformance Checklist

### Basic Level

- [ ] docs/README.md created
- [ ] docs/cards/ created with at least 1 card or template
- [ ] docs/decisions/ created with at least 1 ADR or template

### Full Level (additional)

- [ ] docs/ARCHITECTURE.md created
- [ ] docs/requirements/ structured (technical/, quality/, business/)
- [ ] docs/diagrams/ structured
- [ ] docs/requests-for-comments/ created
- [ ] docs/research/ created with README.md
- [ ] docs/tech-debt/ created
- [ ] docs/plan/ created with template
- [ ] Templates (_TEMPLATE.md) in each directory

---

## 7. Traceability

### Traceability Matrix

```
BR (Business Rule) <-> CARD <-> ADR <-> Code
        |                |
       TR              Test
        |
       QR
```

### Example

```
BR-001 (IRRF Calculation)
  -> CARD-015 (Implement calculation)
       -> ADR-003 (Library choice)
            -> src/main/java/.../IrrfCalculator.java
                 -> IrrfCalculatorTest.java
```

---

## 8. References

- RFC-001: AI-Assisted Software Development Lifecycle
- RFC-002: Standardized Documentation Structure
- RFC-CONTEXT-ENGINEERING: Context Engineering for AIs

---

**Version:** 1.0.0
**Last Updated:** 2026-01-16
