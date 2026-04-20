---
name: waterfall-workflow-guide
description: |
  Guide Waterfall methodology for projects requiring sequential phase gates, formal documentation, and regulatory compliance.
  Use when: working on projects with strict phase gates (government, defense, regulated industries), sequential delivery requirements, or formal documentation mandates.
activation: Manual
estimated_tokens: 1090
---

# Waterfall Workflow Guide

Waterfall is appropriate when requirements are stable, documentation is mandatory, and phase gates are contractual. This skill adapts it for LLM-assisted development.

## When to Use Waterfall

| Condition | Waterfall | Agile |
|-----------|-----------|-------|
| Requirements are fixed and well-understood | ✅ | — |
| Regulatory/contractual documentation required | ✅ | — |
| Requirements will evolve during development | — | ✅ |
| Customer needs frequent demos | — | ✅ |
| Hardware/firmware dependencies | ✅ | — |
| Government/defense procurement | ✅ | — |

## The Phases

```
1. Requirements  → What to build (frozen before design starts)
2. Design        → How to build it (SDD, architecture, data model)
3. Implementation → Build it (code, following the design)
4. Testing       → Verify it works (against requirements)
5. Deployment    → Ship it (with rollback plan)
6. Maintenance   → Support it (bug fixes, minor enhancements)
```

Each phase produces artifacts. The next phase does NOT start until the current phase's artifacts are approved.

## How to Execute Each Phase with LLM Agents

### Phase 1: Requirements
- Use `requirements-engineering` skill for elicitation and specification
- **Deliverable:** Requirements Specification Document (load template)
- **Gate:** All requirements reviewed and approved by stakeholder
- **LLM role:** Draft requirements from meetings/notes, structure them, identify gaps

### Phase 2: Design
- Use `sdd-workflow-guide` skill for SDD creation
- **Deliverable:** Software Design Document + ADRs
- **Gate:** Design reviewed and approved by tech lead / architect
- **LLM role:** Draft SDD, generate data model, propose API design, list alternatives

### Phase 3: Implementation
- Use `tdd-workflow-guide` for test-first coding
- Use `brownfield-refactoring` or `greenfield-development` for project setup
- **Deliverable:** Working code + tests meeting coverage threshold
- **Gate:** Code compiles, tests pass, coverage met, code reviewed
- **LLM role:** Write code following the SDD, generate tests, fix compilation errors

### Phase 4: Testing
- Use `quality-assurance` skill for test strategy
- **Deliverable:** Test report, coverage report, defect list
- **Gate:** All P0/P1 defects resolved, coverage threshold met
- **LLM role:** Generate additional tests, analyze coverage gaps, fix defects

### Phase 5: Deployment
- **Deliverable:** Deployment guide, rollback procedure, release notes
- **Gate:** Deployment tested in staging, rollback verified
- **LLM role:** Draft deployment documentation, create rollback scripts

### Phase 6: Maintenance
- **Deliverable:** Bug fixes with regression tests
- **Gate:** Fix verified, no regressions
- **LLM role:** Diagnose bugs, write fixes with characterization tests

Load `templates/phase-gate-checklist.md` for the full phase gate checklist.
Load `references/common-traps.md` for waterfall-specific pitfalls.

## How to Track Phase Artifacts

| Phase | Artifact | Format | Approved By |
|-------|----------|--------|-------------|
| Requirements | Requirements Spec | Markdown/PDF | Stakeholder |
| Design | SDD + ADRs | Markdown | Tech Lead |
| Implementation | Code + Tests | Repository | Code Review |
| Testing | Test Report | JaCoCo/Jest report | QA |
| Deployment | Release Notes | Markdown | Release Manager |
| Maintenance | Change Log | Markdown | Team Lead |

## Adapting Waterfall for Reality

Pure waterfall is rare. Most real projects use a "modified waterfall" with:

1. **Requirements baseline + controlled changes** — requirements freeze exists but change control allows updates
2. **Design iteration within phase** — design can iterate before the gate, not after
3. **Continuous integration during implementation** — CI/CD within the implementation phase
4. **Regression testing on every change** — not just at the end
