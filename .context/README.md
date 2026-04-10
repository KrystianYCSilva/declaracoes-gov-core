---
name: context-hub
description: |
  Navigation hub for the AI-facing context of declaracoes-gov-core.
  Use when: loading the minimum authoritative context before changing code, tests, or documentation.
---

# .context Hub

## Quick Start

1. Load `standards/architectural-rules.md` first.
2. Load `_meta/project-overview.md` and `_meta/codebase-map.md`.
3. Load only the task-specific files listed below.

## Authority Model

| Tier | Kind | Authority | Typical Files |
| --- | --- | --- | --- |
| T0 | Enforcement | Absolute | `standards/architectural-rules.md` |
| T1 | Standards and patterns | Normative | `standards/`, `patterns/` |
| T2 | Project context | Informative | `_meta/` |
| T3 | Examples | Illustrative | `examples/` |

Conflict resolution:
- If T0 conflicts with anything else, T0 wins.
- If T1 conflicts with T2 or T3, T1 wins.
- If T2 conflicts with T3, T2 wins.
- If code and docs disagree, confirm the current behavior in `src/` and then update both `docs/` and `.context/`.

## Source Of Truth

- `src/` is authoritative for implementation details and current runtime behavior.
- `docs/` is the human-facing record and remains in Portuguese.
- `.context/` is the compressed English AI-facing layer and must mirror the current code and human docs.

## Task Routing

| Task | Load Next |
| --- | --- |
| Certificate loading/mTLS changes | `_meta/key-decisions.md`, `standards/architectural-rules.md`, `patterns/architecture-patterns.md` |
| XML signature changes | `_meta/key-decisions.md`, `standards/architectural-rules.md`, `workflows/development-workflow.md` |
| CNPJ/CPF/IE validator changes | `_meta/codebase-map.md`, `patterns/architecture-patterns.md`, `standards/testing-strategy.md` |
| JSON utilities changes | `_meta/tech-stack.md`, `patterns/architecture-patterns.md` |
| Test changes or regression fix | `standards/testing-strategy.md`, `patterns/testing-and-tdd.md`, `workflows/testing-and-validation-workflow.md` |
| Review or QA | `_meta/codebase-map.md`, `workflows/review-qa-and-release-workflow.md` |

## Directory Map

- `_meta/`: project summary, technology, decisions, and package map.
- `standards/`: absolute and normative rules that every change must follow.
- `patterns/`: the real architectural and design patterns used by this codebase.
- `workflows/`: execution playbooks for development, testing, review, QA, and synchronization.
- `troubleshooting/`: common failure modes and their likely causes.
- `examples/`: compact examples that show how to apply project principles correctly.

## Human Docs Mapping

| Human Doc | AI Summary Files |
| --- | --- |
| `docs/PROJECT-OVERVIEW.md` | `_meta/project-overview.md`, `_meta/tech-stack.md` |
| `docs/ARCHITECTURE.md` | `_meta/tech-stack.md`, `_meta/codebase-map.md`, `patterns/architecture-patterns.md` |
| `docs/KEY-DECISIONS.md` | `_meta/key-decisions.md`, `standards/architectural-rules.md` |
| `docs/TESTING.md` | `standards/testing-strategy.md`, `patterns/testing-and-tdd.md` |
