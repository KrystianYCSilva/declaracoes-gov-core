---
description: |
  Navigation hub for the .context/ directory.
  Use when: any AI agent needs to discover which context files to load for a task.
---

# .context/ — AI Context Hub

> **Human Notice**: This directory is AI-facing context. For human docs, see `docs/`.

## Quick Start
1. Read this file.
2. Load `standards/architectural-rules.md` (T0 — absolute rules).
3. Load `_meta/tech-stack.md` (T2 — stack specifics).
4. Load `_meta/project-overview.md` (T2 — scope and boundaries).

## Tier System

| Tier | Kind | Authority | Directory |
|------|------|-----------|-----------|
| **T0** | Enforcement | ABSOLUTE | `standards/architectural-rules.md` |
| **T1** | Standards | NORMATIVE | `standards/`, `patterns/` |
| **T2** | Context | INFORMATIVE | `_meta/`, `workflows/` |
| **T3** | Examples | ILLUSTRATIVE | `knowledge/` |

### Conflict Resolution
- T0 wins over all tiers.
- T1 wins over T2 and T3.
- T2 wins over T3.
- Always cite the specific rule ID when enforcing T0.

## Structure

| Directory | Purpose | Tier |
|-----------|---------|------|
| `_meta/` | Project identity, tech stack, key decisions | T2 |
| `standards/` | Architectural rules, code quality, testing strategy | T0-T1 |
| `patterns/` | Design blueprints and structural conventions | T1 |
| `knowledge/` | Deep domain knowledge (Brazilian fiscal domain) | T3 |
| `workflows/` | Development and operational workflows | T2 |

## Agent Bootstrap Sequence

```
AGENTS.md (root) → .context/README.md → standards/architectural-rules.md (T0)
→ [task-specific context from _meta/, standards/, patterns/, knowledge/]
```

## Key Files
- `standards/architectural-rules.md` — AR-001 through AR-009 (T0).
- `standards/testing-strategy.md` — JaCoCo gates, test patterns, coverage rules.
- `_meta/tech-stack.md` — Exact dependency and plugin versions.
- `knowledge/domain-concepts.md` — Fiscal identifiers, validation algorithms, government tables.
