---
description: |
  Risk assessment matrix template. Copy and fill for each feature/release.
  Use when: prioritizing test effort based on risk.
---

# Risk Assessment Matrix

## How to Use

1. List each component or feature being changed
2. Rate Impact (1-5) and Likelihood (1-5)
3. Risk Score = Impact × Likelihood
4. Prioritize testing by risk score (highest first)

## Impact Scale

| Score | Level | Description |
|-------|-------|-------------|
| 5 | Critical | Data loss, security breach, financial impact, system down |
| 4 | High | Major feature broken, significant user impact |
| 3 | Medium | Feature degraded, workaround exists |
| 2 | Low | Minor inconvenience, cosmetic issue |
| 1 | Negligible | No user-visible impact |

## Likelihood Scale

| Score | Level | Description |
|-------|-------|-------------|
| 5 | Almost certain | Complex change, no tests, high coupling |
| 4 | Likely | Significant change, partial test coverage |
| 3 | Possible | Moderate change, good test coverage |
| 2 | Unlikely | Small change, well-tested area |
| 1 | Rare | Trivial change, comprehensive tests |

## Template

| Component | Change Description | Impact (1-5) | Likelihood (1-5) | Risk Score | Test Strategy |
|-----------|-------------------|--------------|-------------------|------------|---------------|
| Auth Module | Password reset flow | 5 | 3 | 15 | E2E + security review |
| User List | Pagination fix | 2 | 2 | 4 | Unit test only |
| Report Gen | New export format | 3 | 4 | 12 | Integration + manual |
| DB Schema | Add column | 4 | 3 | 12 | Migration test + rollback |

## Test Depth by Risk Score

| Risk Score | Test Depth | Minimum Coverage |
|------------|-----------|------------------|
| 15-25 | Full: Unit + Integration + E2E + Security + Performance | 90% of changed code |
| 8-14 | Standard: Unit + Integration + key E2E paths | 80% of changed code |
| 4-7 | Basic: Unit + smoke test | 70% of changed code |
| 1-3 | Minimal: Unit tests only | 60% of changed code |
