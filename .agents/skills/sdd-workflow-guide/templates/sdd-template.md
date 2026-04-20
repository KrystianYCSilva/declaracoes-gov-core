---
description: |
  Fill-in-the-blanks SDD template. Copy and customize for each design.
  Use when: creating a new Software Design Document.
---

# Software Design Document: [Feature/System Name]

**Author:** ________________
**Date:** YYYY-MM-DD
**Status:** Draft | In Review | Approved | Superseded
**Reviewers:** ________________

---

## 1. Overview

> One paragraph describing what problem this design solves and for whom.

## 2. Goals and Non-Goals

### Goals
- [ ] Goal 1: ________________
- [ ] Goal 2: ________________
- [ ] Goal 3: ________________

### Non-Goals (explicitly out of scope)
- ________________
- ________________

## 3. Design

### 3.1 Architecture Overview

> High-level diagram or description of components and their relationships.

```
[Component A] ──HTTP──> [Component B] ──SQL──> [Database]
                              │
                              └──Event──> [Component C]
```

### 3.2 Data Model

| Entity | Key Attributes | Relationships |
|--------|---------------|---------------|
| | | |
| | | |

### 3.3 API Design

| Endpoint | Method | Input | Output | Notes |
|----------|--------|-------|--------|-------|
| | | | | |
| | | | | |

### 3.4 Key Algorithms/Logic

> Describe any non-trivial business logic or algorithms.

## 4. Alternatives Considered

### Alternative A: ________________
- **Pros:** ________________
- **Cons:** ________________
- **Why rejected:** ________________

### Alternative B: ________________
- **Pros:** ________________
- **Cons:** ________________
- **Why rejected:** ________________

## 5. Dependencies

| Dependency | Type | Risk if Unavailable |
|-----------|------|-------------------|
| | Library/Service/Team | |
| | | |

## 6. Risks and Mitigations

| Risk | Impact | Likelihood | Mitigation |
|------|--------|-----------|------------|
| | High/Med/Low | High/Med/Low | |
| | | | |

## 7. Test Strategy

| Test Type | What | How | Coverage Target |
|-----------|------|-----|----------------|
| Unit | Business logic | JUnit/Jest/pytest | 80% |
| Integration | DB/API interaction | H2/MockMvc | Key paths |
| E2E | Critical user flows | Selenium/Cypress | Smoke |

## 8. Rollout Plan

- [ ] Feature flag: ________________
- [ ] Deployment strategy: Big Bang / Rolling / Canary
- [ ] Monitoring: ________________
- [ ] Rollback plan: ________________
- [ ] Success criteria: ________________
