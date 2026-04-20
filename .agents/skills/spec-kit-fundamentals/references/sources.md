---
name: sources
description: |
  Spec Kit workflow references and spec-driven development methodology. Use when: creating specs, plans, and tasks or understanding the artifact flow.
Last verified: 2026-04-15
---
# Sources

References for the Spec Kit workflow and spec-driven development.

## Project-Local Artifacts

- Repository context and agent instructions: `AGENTS.md` (repository root)
- Architecture documentation: `docs/` directory (project-specific)

## Spec Kit Skills (loaded from `.agents/skills/`)

Each skill implements one stage of the Spec Kit workflow:

| Skill | Stage |
|-------|-------|
| `speckit-specify` | Create or update the feature specification |
| `speckit-clarify` | Surface underspecified areas in the spec |
| `speckit-plan` | Generate the implementation plan from the spec |
| `speckit-tasks` | Generate ordered tasks from the plan |
| `speckit-implement` | Execute tasks from tasks.md |
| `speckit-analyze` | Cross-artifact consistency check |
| `speckit-checklist` | Generate a feature-specific checklist |
| `speckit-taskstoissues` | Convert tasks into GitHub issues |

## Spec-Driven Development Methodology

- Continuous Delivery — Jez Humble and David Farley:
  `https://continuousdelivery.com/`
- Specification by Example — Gojko Adzic:
  `https://gojko.net/books/specification-by-example/`
- BDD in Action — John Ferguson Smart:
  `https://www.manning.com/books/bdd-in-action-second-edition`

## Usage Notes

- Consult `AGENTS.md` for active backend configuration and conventions.
- Use the skill table above to route to the correct skill for each Spec Kit stage.
- Governance enforcement belongs to CI/CD pipelines — Spec Kit generates artifacts, it does not enforce them.
