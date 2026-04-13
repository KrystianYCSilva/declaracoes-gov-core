---
name: development-workflow
description: |
  Standard workflow for code or documentation changes in declaracoes-gov-core.
  Use when: implementing, testing, or synchronizing changes in the core modules.
---

# Development Workflow

## Trigger

Use this workflow for code, test, or documentation changes in `declaracoes-gov-core`.

## Prerequisites

- Read `.context/standards/architectural-rules.md`.
- Identify the affected child module before editing.
- Confirm the current behavior in the relevant Portuguese human docs and in the live code.

## Standard Procedure

1. Confirm whether the change belongs in `domain`, `format`, `xml`, `crypto`, or `core-bom`.
2. Make the smallest change inside that existing boundary.
3. Update tests in the same child module when behavior changes.
4. Synchronize Portuguese human docs when the public contract, validator policy, or architecture changes.
5. Synchronize AI docs (`AGENTS.md`, tool shims, `.context/`) after the human docs are correct.
6. Use the existing Maven validation command for the touched scope.

## Validation Commands

- Focused module example: `mvn -q -pl declaracoes-gov-core-domain test`
- Full reactor validation: `mvn -q verify`

## Required Sync Targets

| Change Type | Human Docs | AI Docs |
| --- | --- | --- |
| Validator policy or public document model | `README.md`, `docs/02-DESIGN.md`, `docs/03-PLANO-TESTES.md`, `docs/05-MATRIZ-VALIDADORES.md` | `_meta/project-overview.md`, `_meta/key-decisions.md`, `standards/architectural-rules.md`, `standards/testing-strategy.md` |
| Format, parser, or JSON helper behavior | `README.md`, `ARCHITECTURE.md`, `docs/02-DESIGN.md` | `_meta/tech-stack.md`, `_meta/codebase-map.md`, `patterns/architecture-patterns.md` |
| XML or crypto behavior | `README.md`, `ARCHITECTURE.md`, `docs/02-DESIGN.md`, `docs/03-PLANO-TESTES.md` | `_meta/codebase-map.md`, `_meta/key-decisions.md`, `standards/architectural-rules.md`, `standards/testing-strategy.md` |
| Documentation-only sync | Confirm the existing Portuguese docs are already the source of truth | `AGENTS.md`, tool shims, and `.context/` as needed |

## Notes

- Do not expand this repository into transport or declaration-specific territory as part of a routine feature change.
- Do not describe provisional validator behavior as official in either human or AI docs.
- Documentation-only changes should remove stale references instead of inventing placeholder files.
