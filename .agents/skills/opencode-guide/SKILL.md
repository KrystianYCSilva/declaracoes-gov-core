---
name: opencode-guide
description: |
  Guide OpenCode usage, configuration, agents, skills, commands, and official references with low token overhead.
  Use when: onboarding to OpenCode, clarifying `/init`, project config,
  custom agents, custom commands, or selecting the right OpenCode reference or script.
---

# OpenCode Guide

Use this skill as the entry point for OpenCode, not as a single giant handbook.

## How to Navigate This Skill

- `references/config-example.md`: `opencode.json` structure and config patterns
- `references/agent-example.md`: custom agent shape and frontmatter
- `references/command-example.md`: reusable command shape and placeholders
- `references/skill-example.md`: skill shape and examples
- `references/sources.md`: official OpenCode and Agent Skills links
- `scripts/`: scaffolding helpers for project init, skills, agents, and commands

## How to Model OpenCode

- keep `.opencode/AGENTS.md` as the OpenCode instruction file in this repository
- keep the root `AGENTS.md` as the shared cross-agent index
- use `.opencode/agents/` for specialized agents
- use `.opencode/command/` for reusable prompt commands
- use skills for deeper procedural knowledge loaded on demand
- keep project config in `opencode.json`

## How to Answer Common Requests

- configure OpenCode -> `references/config-example.md`
- create an agent -> `references/agent-example.md`
- create a command -> `references/command-example.md`
- create a skill -> `references/skill-example.md`
- validate what is official or current -> `references/sources.md`

## How to Stay Accurate

- separate official OpenCode behavior from repository-local wrappers
- use scripts only when they match the exact workflow being requested
- if a command or config behavior may have changed, confirm it against `references/sources.md`
