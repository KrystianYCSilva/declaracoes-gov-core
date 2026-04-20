---
description: |
  Full AI protocol for working with the declaracoes-gov-core repository.
  Use when: an AI agent needs the complete bootstrap sequence and operational rules.
---

# AI Assistant Guide — declaracoes-gov-core

## Bootstrap Sequence

1. **Load AGENTS.md** (root) for project kernel.
2. **Load MEMORY.md** (root) for current state and active work.
3. **Load .context/README.md** for context navigation.
4. **Load standards/architectural-rules.md** (T0) — absolute constraints.
5. **Load task-specific context** from `_meta/`, `standards/`, `patterns/`, or `knowledge/`.

## Request Classification

| Request Type | Primary Context Files |
|--------------|----------------------|
| Code change / Refactor | `standards/architectural-rules.md`, `standards/code-quality.md`, `patterns/architecture.md` |
| New validator / Domain object | `knowledge/domain-concepts.md`, `standards/architectural-rules.md` |
| Test addition / Fix | `standards/testing-strategy.md`, `standards/architectural-rules.md` |
| Build / CI issue | `_meta/tech-stack.md`, `workflows/development-workflows.md` |
| Dependency upgrade | `_meta/tech-stack.md`, `standards/architectural-rules.md` (AR-003, AR-004) |
| Security review | `standards/architectural-rules.md` (AR-006, AR-007) |

## Tier System

- **T0 (ABSOLUTE)**: `standards/architectural-rules.md` — never violate.
- **T1 (NORMATIVE)**: `standards/code-quality.md`, `standards/testing-strategy.md`, `patterns/architecture.md` — follow unless T0 overrides.
- **T2 (INFORMATIVE)**: `_meta/`, `workflows/` — context for decision-making.
- **T3 (ILLUSTRATIVE)**: `knowledge/` — deep reference, not prescriptive.

## Definition of Done

| Metric | Minimum |
|--------|---------|
| Line Coverage | >= 90% (crypto: 85%) |
| Branch Coverage | >= 90% (all modules) |
| 1:1 Convention | 1 test class : 1 implementation class |
| T0 Compliance | No AR violation introduced |
| Documentation | AI docs updated if behavior changes |
| Memory | `MEMORY.md` updated if task state changes |

## Research Methodology

1. Check `knowledge/domain-concepts.md` before inventing validation logic.
2. Check `standards/architectural-rules.md` before adding dependencies.
3. Check `_meta/tech-stack.md` before changing versions.
4. Prefer official government sources over third-party summaries for validation rules.
5. When in doubt, cite the rule or concept ID and ask for clarification.

## Available Agents / CLI Surfaces

| CLI | Folder | Entry File | Notes |
|-----|--------|------------|-------|
| Claude | `.claude/` | `CLAUDE.md` | — |
| CodeBuddy | `.codebuddy/` | `CODEBUDDY.md` | — |
| Codex | `.codex/` | `AGENTS.md` | Subagent-friendly |
| Cursor | `.cursor/` | `rules/01-core-rules.mdc` | `.mdc` format, `alwaysApply: true` |
| Gemini | `.gemini/` | `GEMINI.md` | — |
| GitHub Copilot | `.github/` | `copilot-instructions.md` | — |
| Junie | `.junie/` | `AGENTS.md` | — |
| Kimi | `.kimi/` | `AGENTS.md` | Skills in `.kimi/skills/` |
| OpenCode | `.opencode/` | `AGENTS.md` | Commands in `.opencode/command/` |
| Qwen | `.qwen/` | `QWEN.md` | Commands in `.qwen/commands/` |
| Vibe | `.vibe/` | `AGENTS.md` | Prompts in `.vibe/prompts/` |

## Language Rules

- **Javadoc / inline comments**: Portuguese (Brazilian government vocabulary).
- **AI-facing docs / .context/**: English.
- **Commit messages / code**: English.
