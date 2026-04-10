---
description: |
  Reference for requirement baseline changes and controlled scope evolution.
  Use when: requirements change mid-cycle, scope increases, or delivery artifacts must be re-baselined.
---

# Change Control

## Change Request Workflow

1. **Submit**: Author files a change request describing the proposed change, its motivation, and affected scope.
2. **Classify**: Triage assigns severity (critical, major, minor) and type (corrective, adaptive, perfective).
3. **Analyze Impact**: Analyst maps the change to affected requirements, design artifacts, code modules, and tests.
4. **Review**: Change control board (or designated reviewer) evaluates cost, risk, and alignment with project goals.
5. **Decide**: Approve, defer, or reject with documented rationale.
6. **Implement**: Approved change is scheduled into a sprint or release cycle.
7. **Verify**: QA confirms the change is implemented correctly and no regressions introduced.
8. **Close**: Update the change log and re-baseline affected artifacts.

## Impact Analysis Checklist

Before approving a change, verify:

- [ ] Which requirements (BR, TR, QR) are affected?
- [ ] Which design documents or ADRs need updating?
- [ ] Which source modules or packages are touched?
- [ ] Which tests must be added, modified, or removed?
- [ ] Are there downstream API consumers affected?
- [ ] Does the change affect database schema or migration scripts?
- [ ] Does the change introduce new dependencies or library upgrades?
- [ ] Is the deployment procedure affected (new config, new env vars)?
- [ ] What is the estimated effort (hours/story points)?
- [ ] What is the risk if the change is deferred?

## Approval Criteria

A change should be approved when:

- The business value or risk reduction justifies the effort.
- The impact analysis is complete and reviewed.
- Resources and timeline are available to absorb the change.
- No blocker-level side effects are identified.
- The requestor and implementor agree on acceptance criteria.

A change should be deferred when cost exceeds current capacity but the need is acknowledged for a future cycle.

## Version Control of Requirements

- Maintain a baseline version for each release milestone (e.g., v0.1 baseline, v0.2 baseline).
- When a requirement changes, increment its version and record the delta (what changed, why, who approved).
- Keep a change log table in the requirements document or a dedicated CHANGELOG section.
- Tag requirement documents in version control at each baseline to enable diffing.
- Cross-reference the change request ID in commit messages for traceability.

## Minimum Change Record

Record:

1. Change summary
2. Reason
3. Affected artifacts
4. Risk and scope impact
5. Approval decision
