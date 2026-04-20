---
description: |
  Pre-release readiness checklist. Copy and fill before each release decision.
  Use when: deciding whether a build is safe to ship.
---

# Release Readiness Checklist

## Gate 1: Code Quality
- [ ] All tests pass (`mvn verify` / `npm test` / `pytest`)
- [ ] Coverage gate met (80% greenfield / 70% brownfield)
- [ ] No new compiler warnings introduced
- [ ] Static analysis clean (SonarQube / ESLint / ruff)
- [ ] No `TODO` or `FIXME` in changed files without linked issues

## Gate 2: Functional Completeness
- [ ] All acceptance criteria from the requirement are covered by tests
- [ ] Happy path tested end-to-end
- [ ] Error paths tested (invalid input, missing data, timeouts)
- [ ] Edge cases documented and tested (empty lists, nulls, max values)
- [ ] No known P0/P1 bugs open for this release

## Gate 3: Non-Functional Requirements
- [ ] Performance: response time within SLA for critical endpoints
- [ ] Security: no new vulnerabilities (OWASP dependency check / npm audit)
- [ ] Logging: key operations produce traceable log entries
- [ ] Error handling: no stack traces exposed to end users

## Gate 4: Operational Readiness
- [ ] Deployment procedure documented and tested
- [ ] Rollback procedure documented and tested
- [ ] Environment configuration verified (staging → production parity)
- [ ] Database migration scripts tested (if applicable)
- [ ] Monitoring/alerting configured for key metrics

## Gate 5: Human Verification
- [ ] Code review completed by at least 1 peer
- [ ] QA sign-off (manual testing of critical paths if automated E2E is insufficient)
- [ ] Stakeholder demo/approval (for user-facing changes)
- [ ] Release notes written

## Decision

| Criterion | Status | Notes |
|-----------|--------|-------|
| All gates passed | ✅/❌ | |
| Known risks accepted | ✅/❌ | |
| Rollback plan verified | ✅/❌ | |

**Release decision:** GO / NO-GO

**Decided by:** ________________  **Date:** ________________
