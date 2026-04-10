---
name: cli-nuances
description: |
  High-signal comparison of the context, bootstrap, extension, and delegation surfaces of major coding-agent CLIs.
  Use when: routing a task to a specific CLI, comparing portability, or deciding where persistent instructions should live.
---

# CLI Nuances

Use this file to compare the surfaces that materially change day-to-day usage.

## What Actually Differs Across CLIs

The useful differences are not "model vibes." They are:

- where persistent instructions live
- how project bootstrap happens
- where reusable commands or workflows live
- whether specialists are native agents, open-standard skills, or repo-local rules
- how much of the tool is portable across vendors

## High-Signal Matrix

| Tool | Native project context | Bootstrap surface | Reusable workflow surface | Specialist/delegation surface | Operational nuance |
|------|------------------------|-------------------|---------------------------|-------------------------------|-------------------|
| Claude Code | `CLAUDE.md` | `/init` | `.claude/skills/`, plugins, bundled skills | built-in and custom subagents | Custom commands are now part of the skill system; built-in slash commands remain separate. |
| GitHub Copilot CLI | `.github/copilot-instructions.md` plus `AGENTS.md` | `copilot init` | `.github/instructions/*.instructions.md`, skills, custom agents | custom agents and agent-oriented CLI flows | Strongest when the repo already lives in GitHub workflows, reviews, and policy. |
| OpenCode | `AGENTS.md` | `/init` | `.opencode/command/`, `.opencode/skills/`, `.opencode/agents/` | agents and subagents | Best portability story: it natively recognizes `.agents/skills` and Claude-compatible layouts. |
| Gemini CLI | `GEMINI.md` plus layered `settings.json` | `/init` | `.gemini/commands/`, MCP, project config | tool-driven agent workflows | Strong headless and large-context workflow surface; separate CLI behavior from generic Gemini API knowledge. |
| Qwen Code | `QWEN.md` by default, configurable via `context.fileName` | `/init` | `.qwen/skills/`, `.qwen/agents/`, extensions | sub-agents, extensions, MCP | Most modular of the group; context filename and extension packaging are first-class. |
| Cursor | `.cursor/rules/` or root `AGENTS.md` | generate rules from chat; no single `/init` equivalent is the main pattern | project rules and reusable commands | agent workflows in the product/CLI | Cursor-native rules matter more than portable markdown when you need scope, attachment rules, or structured always-on behavior. |
| Kimi Code CLI | `AGENTS.md` | `/init` | session controls, shell mode, project context | agent mode with resumable sessions | Public docs are stronger on sessions and interaction than on a large public extension surface; do not over-assume skill parity with Claude/Qwen. |
| Codex CLI | `AGENTS.md` | `/init` | skills, slash commands, plugins | built-in and custom subagents | Approval modes and sandbox boundaries are part of the product shape, not an afterthought. |

## Portable Rules That Hold Up

- `AGENTS.md` is the most portable shared format, but it is not the native primary file for every tool.
- `/init` is not semantically identical across vendors; treat it as "bootstrap context" only after checking the tool's docs.
- If a team wants cross-tool reuse, keep canonical project guidance in `AGENTS.md` and add vendor-native overlays only where they unlock real value.
- If a tool already has a strong native rules system, use that for always-on behavior and use skills only for task-shaped knowledge.

## What Not To Assume

- Do not assume every tool has first-class open-standard skills.
- Do not assume a CLI is the primary surface just because the vendor also has an IDE product.
- Do not copy examples between tools without translating instruction hierarchy, permissions, and session model.

