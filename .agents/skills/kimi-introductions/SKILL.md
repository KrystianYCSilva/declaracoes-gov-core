---
name: kimi-introductions
description: |
  Guide Kimi Code CLI usage, modes, agents, skills, tools, and MCP with a JIT reference layout.
  Use when: onboarding to Kimi CLI, clarifying commands, operation modes, skills,
  custom agents, or locating the right official Kimi source.
---

# Kimi Introduction

Use this skill as the router for Kimi CLI guidance and bundled references.

## How to Navigate This Skill

- `references/installation.md`: install, login, first-run setup
- `references/cli-usage.md`: CLI commands, operational modes, and tool surfaces
- `references/agents.md`: built-in agents, subagents, and custom agents
- `references/skills.md`: skills and flow skills
- `references/extensibility.md`: MCP configuration and usage examples
- `references/sources.md`: official Kimi and Agent Skills links

## How to Model Kimi CLI

- keep `.kimi/AGENTS.md` as the Kimi-specific project guidance file in this repository
- keep the root `AGENTS.md` as the shared cross-agent index
- use modes to control autonomy and risk before changing files
- use skills for reusable workflows and domain knowledge
- use custom agents when a toolset or role needs tighter boundaries

## How to Answer Common Requests

- install or configure Kimi -> `references/installation.md`
- command, mode, or tool question -> `references/cli-usage.md`
- custom agent or subagent question -> `references/agents.md`
- skill or flow-skill question -> `references/skills.md`
- MCP, extensibility, or usage examples -> `references/extensibility.md`

## How to Stay Accurate

- separate current CLI behavior from generic model behavior
- prefer the official sources in `references/sources.md` for version-sensitive details
- load only the reference file needed for the current ask
