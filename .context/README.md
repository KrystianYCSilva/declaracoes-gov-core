---
name: context-hub
description: |
  Navigation hub for the AI-facing context of declaracoes-gov-core.
  Use when: loading the minimum authoritative context before changing code or AI docs.
---

# .context Hub

## Quick Start

1. Load `standards/architectural-rules.md`.
2. Load `_meta/project-overview.md` and `_meta/tech-stack.md`.
3. Load `_meta/codebase-map.md`.
4. Load only the task-specific files needed for the current request.

## Authority Model

| Tier | Kind | Authority | Typical Files |
| --- | --- | --- | --- |
| T0 | Enforcement | Absolute | `standards/architectural-rules.md` |
| T1 | Standards and patterns | Normative | `standards/`, `patterns/` |
| T2 | Project context and workflows | Informative | `_meta/`, `workflows/` |
| T3 | Examples | Illustrative | None currently maintained |

Conflict resolution:

- If T0 conflicts with anything else, T0 wins.
- If AI docs and Portuguese human docs drift from the code, confirm the truth in `declaracoes-gov-core-*/src` and the relevant `pom.xml`, then update human docs, then `.context/`.
- Never describe provisional validator behavior as official.

## Source of Truth

- The root `pom.xml` defines the reactor, shared test dependencies, and default coverage gates.
- Child-module `pom.xml` files and `declaracoes-gov-core-*/src` define the live implementation.
- Human docs in the module root and `docs/` remain in Portuguese.
- `.context/` is the compressed English AI layer.

## Task Routing

| Task | Load Next |
| --- | --- |
| Validator or value-object change | `_meta/codebase-map.md`, `_meta/key-decisions.md`, `standards/testing-strategy.md` |
| Format/parser/JSON change | `_meta/tech-stack.md`, `_meta/codebase-map.md`, `patterns/architecture-patterns.md` |
| XML signing or DOM utility change | `standards/architectural-rules.md`, `_meta/codebase-map.md`, `_meta/key-decisions.md` |
| PKCS11, PKCS12, or `SSLContext` change | `standards/architectural-rules.md`, `_meta/tech-stack.md`, `_meta/key-decisions.md` |
| Documentation or sync task | `ai-assistant-guide.md`, `workflows/development-workflow.md` |

## Directory Map

- `_meta/`: project scope, technology, codebase map, and stable design decisions.
- `standards/`: normative rules for architecture, code quality, and testing.
- `patterns/`: recurring implementation patterns that already exist in the codebase.
- `workflows/`: repeatable maintenance flow for code or documentation changes.
- Optional `examples/` and `troubleshooting/` folders are not used in this module today.

## Human Docs Mapping

| Human Doc | AI Summary Files |
| --- | --- |
| `README.md` | `_meta/project-overview.md`, `_meta/tech-stack.md` |
| `ARCHITECTURE.md`, `docs/02-DESIGN.md` | `_meta/codebase-map.md`, `_meta/key-decisions.md`, `patterns/architecture-patterns.md` |
| `docs/03-PLANO-TESTES.md` | `standards/testing-strategy.md` |
| `docs/05-MATRIZ-VALIDADORES.md` | `_meta/project-overview.md`, `_meta/key-decisions.md`, `standards/architectural-rules.md` |
