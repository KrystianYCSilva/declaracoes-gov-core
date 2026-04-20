---
name: sdd-workflow-guide
description: |
  Guide Software Design Document creation and review with structured templates and architecture decision records.
  Use when: writing technical design documents, creating architecture decisions, documenting system design, or reviewing SDD artifacts before implementation.
activation: Manual
estimated_tokens: 860
---

# SDD Workflow Guide

A Software Design Document (SDD) bridges requirements and implementation. It answers "HOW will we build it?" before code is written, preventing costly rework.

## How to Decide When an SDD is Needed

| Change Type | SDD Required? | Why |
|-------------|---------------|-----|
| New system/service | ✅ Yes (full SDD) | Architecture decisions have long-term impact |
| New feature (cross-cutting) | ✅ Yes (lightweight) | Affects multiple components |
| New feature (isolated) | ⚠️ Maybe (ADR only) | One component, clear scope |
| Bug fix | ❌ No | Implementation is clear |
| Refactoring | ❌ No (unless architectural) | Behavior doesn't change |

## How to Structure an SDD

```
1. Overview         — What problem are we solving? (1 paragraph)
2. Goals/Non-Goals  — What's in scope and explicitly NOT in scope
3. Design           — Architecture, components, data model, APIs
4. Alternatives     — What other approaches were considered and why rejected
5. Dependencies     — What this design depends on (services, libraries, teams)
6. Risks            — What could go wrong, mitigation plan
7. Test Strategy    — How will we validate the design works
8. Rollout Plan     — How will we deploy (feature flags, gradual, big bang)
```

Load `templates/sdd-template.md` for a fill-in-the-blanks SDD template.

## How to Write Architecture Decision Records (ADRs)

An ADR documents ONE decision. Short, focused, permanent.

```markdown
# ADR-001: Use H2 for Test Database

## Status: Accepted

## Context
Our production database is DB2. We need a test database that:
- Runs in-memory for speed
- Supports DB2 SQL dialect
- Requires zero installation

## Decision
Use H2 2.2.224 with MODE=DB2 for all integration tests.

## Consequences
- Tests run in ~5 seconds (vs 30+ with real DB2)
- Some DB2-specific features may not be supported by H2
- We accept minor compatibility gaps for speed
```

Load `templates/adr-template.md` for the ADR template.

## How to Review an SDD

Before approving an SDD, check:

1. **Completeness:** All 8 sections are addressed (even if brief)
2. **Alternatives:** At least 2 alternatives considered (avoids tunnel vision)
3. **Testability:** Design is testable without production dependencies
4. **Reversibility:** Can we undo this decision if it's wrong?
5. **Complexity budget:** Does this add justified complexity or unnecessary complexity?

Load `references/common-traps.md` for SDD review anti-patterns.

## How to Use SDDs with LLM Agents

An SDD is excellent context for LLM agents. When the SDD exists:

1. Include the SDD (or its summary) in the agent's context
2. The agent implements ACCORDING TO the design, not improvising
3. Review agent output against the SDD — deviations must be justified
4. Update the SDD if the implementation reveals a better approach

**Without an SDD:** Agent invents the architecture → inconsistent, often over-engineered.
**With an SDD:** Agent follows the blueprint → consistent, reviewable, traceable.
