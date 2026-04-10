---
description: |
  Reference for prioritizing test effort by risk, impact, and defect cost.
  Use when: deciding where to spend validation time, balancing depth against delivery pressure,
  or justifying why some paths need stronger evidence than others.
---

# Risk-Based Testing

## Risk Matrix Approach

Assess each feature or change along two axes and multiply to get a risk score:

| | Low Impact | Medium Impact | High Impact | Critical Impact |
|---|---|---|---|---|
| **High Likelihood** | Medium | High | Critical | Critical |
| **Medium Likelihood** | Low | Medium | High | Critical |
| **Low Likelihood** | Low | Low | Medium | High |

Inputs to the matrix:

- **Impact**: consequence if the defect reaches production (data loss, revenue loss, security breach, user trust).
- **Likelihood**: probability the area contains a defect (new code, complex logic, history of bugs, external dependency).

## Risk Classification

| Level | Definition | Example |
|-------|-----------|---------|
| **Critical** | Could cause data loss, security breach, or regulatory violation | Auth bypass, payment double-charge |
| **High** | Significant user impact or SLA breach | Core workflow failure, API contract break |
| **Medium** | Degraded experience but workaround exists | Slow search, minor UI glitch on edge case |
| **Low** | Cosmetic or rarely triggered | Tooltip typo, date format in admin panel |

## Test Prioritization by Risk Level

1. **Critical**: Full path coverage, negative tests, boundary tests, load tests. Automated regression mandatory.
2. **High**: Happy path + key negative paths. Automated regression strongly recommended.
3. **Medium**: Happy path + one representative negative case. Automated where cost-effective.
4. **Low**: Happy path only. Manual spot-check acceptable.

## Coverage Allocation Strategy

Allocate test effort proportionally to risk:

- ~40% of effort on Critical areas.
- ~30% on High areas.
- ~20% on Medium areas.
- ~10% on Low areas.

Reassess allocation each release cycle as risk profiles shift (new features raise risk; stabilized features lower it).

## Typical High-Risk Areas

- Auth and permissions
- Payments and data integrity
- Database migrations
- Public API compatibility
- Concurrency and distributed state
- Third-party integration boundaries
