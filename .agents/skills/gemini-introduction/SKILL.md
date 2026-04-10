---
name: gemini-introduction
description: |
  Guide Gemini CLI usage, extension points, and JIT reference loading.
  Use when: onboarding to Gemini CLI, clarifying commands, agents, skills, prompts,
  or deciding which Gemini reference to load for the current task.
---

# Gemini Introduction

Use this skill as a router for the Gemini CLI knowledge already bundled in this directory.

## How to Navigate This Skill

- `references/commands.md`: install, auth, session commands, slash commands, and context handling
- `references/extensions-and-agents.md`: skills, commands, hooks, and extension patterns
- `references/sources.md`: official Gemini CLI, Google, and Agent Skills links
- `scripts/hello_agent.js`: small deterministic helper example for script-backed skills

## How to Model Gemini CLI

- keep `.gemini/GEMINI.md` as the Gemini project memory file in this repository
- use commands, extensions, hooks, and skills as the official Gemini extension surfaces
- treat `.gemini/agents/` as repository-local compatibility material, not as an official Gemini subagent surface
- keep scripts for deterministic or repetitive operations that should not be rewritten every time

## How to Answer Common Requests

- getting started or command usage -> `references/commands.md`
- creating a skill, command, or extension -> `references/extensions-and-agents.md`
- validating whether a capability is official -> `references/sources.md`
- showing a minimal helper-script pattern -> `scripts/hello_agent.js`

## How to Stay Accurate

- distinguish Gemini CLI behavior from generic Gemini API behavior
- load only the reference needed for the current question
- if a command, flag, or capability may have changed, confirm it against `references/sources.md`
