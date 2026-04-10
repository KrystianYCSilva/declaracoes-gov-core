---
name: sources
description: |
  Official Claude Code documentation references. Use when: onboarding to Claude Code, explaining commands, skills, agents, or permissions.
Last verified: 2025-07-09
---
# Sources

Official Claude Code references, trimmed to the pages that matter most for daily work.

Last verified: 2025-07-09

## Core Docs

- Overview: `https://docs.anthropic.com/en/docs/claude-code/overview`
- Commands (slash): `https://docs.anthropic.com/en/docs/claude-code/commands`
- Skills system: `https://docs.anthropic.com/en/docs/claude-code/skills`
- Memory and `CLAUDE.md`: `https://docs.anthropic.com/en/docs/claude-code/memory`
- Subagents: `https://docs.anthropic.com/en/docs/claude-code/sub-agents`
- Permissions: `https://docs.anthropic.com/en/docs/claude-code/permissions`
- Hooks: `https://docs.anthropic.com/en/docs/claude-code/hooks`
- Plugins: `https://docs.anthropic.com/en/docs/claude-code/plugins`

## Open Standards and Adjacent Specs

- Agent Skills specification: `https://agentskills.io/specification`
- Anthropic docs home: `https://docs.anthropic.com/en/home`

## High-Value Notes

- `CLAUDE.md` is Claude Code's native persistent project memory surface.
- `/init` bootstraps or enriches project memory rather than acting like a generic repo scanner.
- Skills are invocable via `/skill-name` and follow the Agent Skills open standard; built-in commands (`/init`, `/compact`) are separate.
- When behavior feels surprising, check permissions and memory before assuming model failure.
