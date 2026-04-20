---
name: sources
description: |
  Spec Kit workflow references. Use when: creating specs, plans, and tasks.
Last verified: 2025-07-09
---
# Sources

References for the Spec Kit workflow and spec-driven development.

## Project-Local Artifacts

- Repository governance and agent instructions: `AGENTS.md` (repository root)
- Architecture documentation: `docs/DOCUMENTO-ARQUITETURA.md`
- Release notes: `docs/RELEASE-v0.1.md`
- Implementation cascade: `docs/CASCATA-IMPLEMENTACAO.md`

## Spec Kit Runtime Agents (repository-local)

These are the skill agents that implement Spec Kit stages.
Each is a SKILL.md under `.agents/skills/`:

| Agent | Stage |
|-------|-------|
| `speckit.specify` | Create or update the feature specification |
| `speckit.clarify` | Surface underspecified areas in the spec |
| `speckit.plan` | Generate the implementation plan from the spec |
| `speckit.tasks` | Generate ordered tasks from the plan |
| `speckit.implement` | Execute tasks from tasks.md |
| `speckit.analyze` | Cross-artifact consistency check |
| `speckit.checklist` | Generate a feature-specific checklist |
| `speckit.constitution` | Create or update the project constitution |

## Upstream Spec Kit

Spec Kit is the upstream framework that owns the spec/plan/tasks artifact flow.
The public project and documentation, if available, should be consulted before
modifying the upstream artifact templates or bootstrap behavior.

## Spec-Driven Development Methodology

- Continuous Delivery — Jez Humble and David Farley:
  `https://continuousdelivery.com/`
- Specification by Example — Gojko Adzic:
  `https://gojko.net/books/specification-by-example/`
- BDD in Action — John Ferguson Smart:
  `https://www.manning.com/books/bdd-in-action-second-edition`

## Usage Notes

- Consult `AGENTS.md` for active backend configuration and governance contract.
- Use the agent table above to route to the correct skill for each Spec Kit stage.
- External SDD resources provide methodology context; the local skill agents own execution.
