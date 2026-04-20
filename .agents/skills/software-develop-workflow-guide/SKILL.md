---
name: software-develop-workflow-guide
description: |
  Guide end-to-end software development workflow from idea to production, integrating all development methodologies and practices.
  Use when: planning a development workflow, choosing between methodologies, onboarding developers to a project process, or establishing team development practices.
activation: Auto
estimated_tokens: 1170
---

# Software Development Workflow Guide

This skill is the routing hub — it helps you choose the RIGHT workflow for your project and connects to specialized skills for each phase.

## How to Choose a Development Methodology

| Question | If YES → | If NO → |
|----------|----------|---------|
| Are requirements fixed before development starts? | Waterfall | Agile/Iterative |
| Is formal documentation contractually required? | Waterfall | Agile |
| Will requirements evolve during development? | Agile | Waterfall |
| Is this a new product with unknown market fit? | Agile + MVP | — |
| Is this a regulated industry (gov, defense, health)? | Waterfall + compliance | Agile |
| Is the team experienced with the technology? | Either | Agile (faster learning) |

## The Universal Workflow (methodology-agnostic)

Regardless of methodology, every software project goes through these stages:

```
UNDERSTAND → DESIGN → BUILD → VERIFY → SHIP → MAINTAIN
```

The difference between methodologies is:
- **Waterfall:** Each stage completes fully before the next starts
- **Agile:** All stages happen in short cycles (sprints), incrementally
- **Hybrid:** Requirements baseline + iterative implementation

## Stage-by-Stage Guide

### 1. UNDERSTAND (What are we building?)

**Skills to load:**
- `requirements-engineering` — elicitation, testable requirements, traceability
- `ddd-workflow-guide` — if the domain is complex, use domain discovery first

**Deliverables:**
- Requirements with acceptance criteria
- Domain model (if DDD) or feature list (if CRUD)
- Scope boundaries (in/out)

### 2. DESIGN (How will we build it?)

**Skills to load:**
- `sdd-workflow-guide` — for significant features, write an SDD
- `ddd-workflow-guide` — for aggregate/context design
- `greenfield-development` — for tech stack decisions

**Deliverables:**
- SDD or ADR (for significant decisions)
- Data model, API contract (for features with integrations)
- AGENTS.md (for AI-assisted implementation)

### 3. BUILD (Write the code)

**Skills to load:**
- `tdd-workflow-guide` — test-first discipline
- `greenfield-development` — if new project
- `brownfield-refactoring` — if legacy project
- `ddd-workflow-guide` — for domain layer structure

**Deliverables:**
- Working code + tests
- Coverage meets threshold
- Code follows project conventions (AGENTS.md)

### 4. VERIFY (Does it work? Is it good?)

**Skills to load:**
- `quality-assurance` — risk-based testing, release readiness
- `code-review-guide` — structured code review
- `tdd-workflow-guide` — test pyramid compliance

**Deliverables:**
- All tests pass
- Coverage gate met
- Code review approved
- No P0/P1 defects

### 5. SHIP (Deploy to production)

**Deliverables:**
- Deployment successful
- Smoke tests pass
- Monitoring active
- Release notes written

### 6. MAINTAIN (Keep it running)

**Skills to load:**
- `brownfield-refactoring` — for tech debt reduction
- `quality-assurance` — for regression prevention

**Deliverables:**
- Bug fixes with regression tests
- Tech debt reduction plan
- Performance monitoring

## How to Set Up a Project from Scratch

```
Day 1:  Choose methodology → Choose tech stack → Create repository
Day 2:  Write AGENTS.md → Set up CI/CD → Write first test (infrastructure)
Day 3:  Implement vertical slice (1 entity end-to-end with tests)
Day 4+: Replicate the slice for remaining features
```

Load `references/common-traps.md` for workflow-level anti-patterns.
Load `templates/project-kickoff-checklist.md` for a pre-development checklist.

## Related Skills (When to Load What)

| Task | Load Skill |
|------|-----------|
| Writing requirements | `requirements-engineering` |
| Designing architecture | `sdd-workflow-guide` |
| Modeling domain | `ddd-workflow-guide` |
| Starting new project | `greenfield-development` |
| Refactoring legacy | `brownfield-refactoring` |
| Writing tests first | `tdd-workflow-guide` |
| Reviewing code | `code-review-guide` |
| Release decision | `quality-assurance` |
| Writing docs | `software-documentation-guide` |
| Configuring AI agents | `coding-agent-tools` |
| Writing better prompts | `prompt-engineering-advanced` |
