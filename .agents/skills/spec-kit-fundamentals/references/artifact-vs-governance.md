---
description: |
  Reference for the boundary between Spec Kit artifact flow and project governance.
  Use when: deciding which layer owns a behavior, or clarifying why Spec Kit
  generates artifacts but does not enforce them.
---

# Artifact Flow Versus Project Governance

## Spec Kit Owns (Artifact Flow)

- `spec-template.md`
- `plan-template.md`
- `tasks-template.md`
- the sequential spec-driven workflow

## Project Governance Owns (Enforcement)

- CI/CD pipeline gates
- code review and approval
- release readiness criteria
- AGENTS.md context and conventions

## Non-Negotiable Rule

`backend=spec-kit-native` means Spec Kit owns technical artifact generation for the current cycle.
It does not make Spec Kit the sovereign source of governance for the repository.
Governance enforcement belongs to CI/CD and automated tests — not to prompts or state machines.
