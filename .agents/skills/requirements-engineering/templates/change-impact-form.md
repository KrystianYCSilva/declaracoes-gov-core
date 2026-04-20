---
description: |
  Change impact assessment form. Fill before approving any requirement change.
  Use when: a requirement changes after initial planning and you need to assess downstream effects.
---

# Change Impact Assessment

## Change Request

| Field | Value |
|-------|-------|
| Change ID | CHG-___ |
| Date | YYYY-MM-DD |
| Requested by | |
| Affected requirement | REQ-___ |

## What Changed

**Before:**
> [Original requirement text]

**After:**
> [New requirement text]

**Reason for change:**
> [Why this change is needed]

## Impact Analysis

### Code Impact
- [ ] New code required (estimate: ___ files, ___ hours)
- [ ] Existing code must change (list affected files)
- [ ] No code impact

### Test Impact
- [ ] New tests required (estimate: ___ test cases)
- [ ] Existing tests must change (list affected test IDs)
- [ ] Existing tests invalidated (must be deleted/rewritten)
- [ ] No test impact

### Data Impact
- [ ] Database schema change required
- [ ] Data migration required
- [ ] Existing data affected
- [ ] No data impact

### Integration Impact
- [ ] API contract changes (breaking: yes/no)
- [ ] Downstream consumers affected (list them)
- [ ] External dependencies affected
- [ ] No integration impact

### Schedule Impact
- [ ] Can be absorbed in current sprint/iteration
- [ ] Requires additional sprint/iteration
- [ ] Blocks other work (list blocked items)

## Risk Assessment

| Risk | Impact (1-5) | Likelihood (1-5) | Mitigation |
|------|-------------|-------------------|------------|
| | | | |

## Decision

- [ ] **Approved** — proceed with change
- [ ] **Deferred** — revisit in next planning cycle
- [ ] **Rejected** — keep original requirement

**Decided by:** ________________  **Date:** ________________
