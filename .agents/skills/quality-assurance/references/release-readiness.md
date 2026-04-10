---
description: |
  Reference for release readiness gates, evidence expectations, and go/no-go framing.
  Use when: deciding whether a change can ship, preparing a release review,
  or clarifying what evidence is still missing.
---

# Release Readiness

## Release Criteria Checklist

1. All acceptance tests pass on the release candidate build.
2. Known defects are classified (blocker, deferrable, cosmetic) with none at blocker.
3. Performance benchmarks meet or exceed baseline thresholds.
4. Security scan (SAST/DAST) results reviewed; no critical findings open.
5. Database migration scripts tested against a production-like dataset.
6. Configuration differences between environments documented.
7. Release notes drafted covering changes, known issues, and upgrade steps.

## Environment Validation Steps

1. Deploy the release candidate to a staging environment that mirrors production.
2. Run the full regression suite against staging.
3. Verify external integrations (APIs, message queues, third-party services) respond correctly.
4. Confirm monitoring dashboards, alerts, and log aggregation are operational.
5. Validate TLS certificates, DNS, and network policies match production expectations.

## Rollback Plan Expectations

- Document the exact rollback procedure before deploying (restore artifact, revert migration, feature flag).
- Identify the rollback trigger: which metric or alert threshold means "roll back now."
- Estimate rollback time and confirm it is within the acceptable outage window.
- Test the rollback path at least once in staging before go-live.
- Assign a rollback owner who has authority to execute without further approval.

## Smoke Test Patterns

- **Health endpoint**: Confirm the application starts and `/health` or `/actuator/health` returns 200.
- **Auth round-trip**: Authenticate with a test credential and verify a protected endpoint returns the expected payload.
- **CRUD canary**: Create, read, update, and delete a single test record on the primary entity.
- **Integration ping**: Call each downstream dependency and verify a non-error response.
- **Latency gate**: Confirm P95 response time of the smoke requests stays below the agreed threshold.

## Go/No-Go Question

The release decision should answer:

- What is the residual risk?
- Is that risk understood and accepted by the stakeholders?
- What is the fallback if the change misbehaves?
