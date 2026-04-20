---
description: |
  Phase gate checklist for waterfall projects. Copy and fill per project.
  Use when: verifying that a waterfall phase is complete before proceeding.
---

# Phase Gate Checklist

## Phase 1 → Phase 2 Gate (Requirements → Design)

- [ ] All functional requirements documented with unique IDs
- [ ] Acceptance criteria defined for each requirement (Given/When/Then)
- [ ] Non-functional requirements specified (performance, security, scalability)
- [ ] Out-of-scope items explicitly listed
- [ ] Stakeholder sign-off obtained
- [ ] Requirements baseline frozen (change control active)

**Gate decision:** ✅ Proceed to Design / ❌ Return to Requirements

---

## Phase 2 → Phase 3 Gate (Design → Implementation)

- [ ] SDD completed with all 8 sections
- [ ] At least 2 alternatives considered and documented
- [ ] Data model defined (entities, relationships, constraints)
- [ ] API contracts defined (endpoints, request/response formats)
- [ ] Test strategy defined (what types of tests, coverage targets)
- [ ] ADRs written for all significant technology decisions
- [ ] Tech lead / architect approval obtained

**Gate decision:** ✅ Proceed to Implementation / ❌ Return to Design

---

## Phase 3 → Phase 4 Gate (Implementation → Testing)

- [ ] All code compiles: `mvn compile` / `npm run build` / `go build`
- [ ] Unit tests pass: `mvn test` / `npm test` / `pytest`
- [ ] Coverage threshold met: ≥ 80% (greenfield) / ≥ 70% (brownfield)
- [ ] Code reviewed by at least 1 peer
- [ ] No P0/P1 known defects
- [ ] No hardcoded credentials or secrets
- [ ] Documentation updated (README, API docs if applicable)

**Gate decision:** ✅ Proceed to Testing / ❌ Return to Implementation

---

## Phase 4 → Phase 5 Gate (Testing → Deployment)

- [ ] All acceptance criteria validated (requirement ↔ test traceability)
- [ ] Integration tests pass
- [ ] E2E tests pass for critical paths
- [ ] Performance within SLA (if applicable)
- [ ] Security scan clean (OWASP/npm audit/safety)
- [ ] All P0/P1 defects resolved
- [ ] QA sign-off obtained

**Gate decision:** ✅ Proceed to Deployment / ❌ Return to Implementation/Testing

---

## Phase 5 → Phase 6 Gate (Deployment → Maintenance)

- [ ] Deployed to production successfully
- [ ] Smoke tests pass in production
- [ ] Monitoring/alerting active
- [ ] Rollback procedure verified
- [ ] Release notes published
- [ ] Stakeholder notified

**Gate decision:** ✅ Enter Maintenance / ❌ Rollback
