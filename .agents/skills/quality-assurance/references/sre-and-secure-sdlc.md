---
description: |
  Reference for operational resilience and secure SDLC concerns that affect QA decisions.
  Use when: release quality depends on rollback, monitoring, abuse resistance, or secure delivery controls.
---

# SRE and Secure SDLC

## Observability Requirements

### Logging

- Structured JSON logs with correlation IDs on every request.
- Log levels used consistently: ERROR for failures requiring attention, WARN for degraded paths, INFO for key business events, DEBUG for development only.
- Sensitive data (tokens, passwords, PII) must never appear in logs.

### Metrics

- Expose RED metrics for every service: Rate (requests/sec), Errors (error rate), Duration (latency percentiles).
- Track saturation signals: thread pool usage, connection pool usage, heap utilization.
- Use labels/tags that allow filtering by endpoint, status code, and environment.

### Tracing

- Propagate distributed trace context (e.g., W3C Trace Context header) across all service boundaries.
- Capture spans for inbound HTTP, outbound HTTP, and database calls at minimum.
- Sample at a rate that balances cost with debuggability (100% in staging, 1-10% in production).

## Incident Response Basics

1. **Detect**: Alerts fire based on SLO burn-rate or error-rate thresholds.
2. **Triage**: On-call determines severity and blast radius.
3. **Mitigate**: Apply the fastest safe action (rollback, feature flag, scale-up).
4. **Communicate**: Update the status page and notify stakeholders.
5. **Resolve**: Fix root cause when mitigation is stable.
6. **Review**: Conduct a blameless post-incident review within 5 business days.

## SAST/DAST Integration Points

- **SAST (Static)**: Run on every pull request via CI. Block merge on critical or high findings. Tools: SonarQube, Semgrep, CodeQL.
- **DAST (Dynamic)**: Run nightly or on each staging deployment against live endpoints. Tools: OWASP ZAP, Burp Suite CI.
- **Dependency scanning**: Check transitive dependencies for known CVEs on every build. Tools: OWASP Dependency-Check, Snyk, Trivy.
- **Secret scanning**: Pre-commit hook and CI step to prevent committed credentials.

## Security Gates in CI/CD

| Gate | Stage | Blocks Deploy? |
|------|-------|---------------|
| Secret scan | Pre-commit / PR | Yes |
| SAST | PR build | Yes (critical/high) |
| Dependency CVE check | PR build | Yes (critical) |
| DAST | Staging deploy | Yes (critical) |
| License compliance | PR build | Advisory |
| Container image scan | Image build | Yes (critical) |

## Operational Quality Questions

1. How will failure be detected?
2. How will it be mitigated?
3. Can the team roll back or contain blast radius quickly?

## Secure Delivery Questions

1. What abuse path is plausible?
2. What control should catch it?
3. What evidence shows the control was exercised?
