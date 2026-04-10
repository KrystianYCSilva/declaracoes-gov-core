---
name: cli-basics
description: |
  Reference for Claude Code CLI invocation shapes, project instruction surfaces, and /init behavior.
  Use when: onboarding to Claude Code CLI or explaining headless vs interactive modes.
---

# Claude Code CLI basics

Use this file for the current Claude Code operator model.

## Invocation shapes that matter

- `claude` starts an interactive session.
- `claude -p "<prompt>" --output-format text` runs headless.
- `claude --agent <name>` selects a configured agent.
- `claude agents` lists configured agents.

## Project instruction surfaces

- Claude officially supports `CLAUDE.md`.
- The docs allow `CLAUDE.md` in the project root and under `.claude/`.
- This repository uses `.claude/CLAUDE.md` as the primary Claude memory file and keeps the root `AGENTS.md` as the shared multi-CLI index.

## `/init`

- `/init` bootstraps project memory.
- In this repository, keep `/init` additive:
  - preserve the root `AGENTS.md`
  - update `.claude/CLAUDE.md`
  - do not create a new root `CLAUDE.md`
