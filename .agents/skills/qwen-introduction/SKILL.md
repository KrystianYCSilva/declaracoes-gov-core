---
name: qwen-introduction
description: |
  Guide Qwen Code usage, headless workflows, agents, skills, configuration, and official references.
  Use when: onboarding to Qwen Code, clarifying `/init`, headless mode, agents,
  skills, configuration, or deciding which Qwen reference to load next.
---

# Qwen Introduction

Use this skill as a router for Qwen Code workflows and its bundled references.

## How to Navigate This Skill

- `references/commands.md`: install, commands, slash commands, and headless usage
- `references/agents.md`: SubAgents and agent authoring
- `references/skills.md`: skill discovery, layout, and usage
- `references/configuration.md`: settings, auth, ignore files, and providers
- `references/templates.md`: reusable templates and scaffolds
- `references/examples.md`: examples and practical prompt shapes
- `references/sources.md`: official Qwen and Agent Skills links

## How to Model Qwen Code

- use `/init` and `.qwen/QWEN.md` to establish project context in this repository
- use headless mode for automation and pipelines
- use agents for isolated specialized work
- use skills for reusable domain workflows and references

## How to Answer Common Requests

- command or headless question -> `references/commands.md`
- SubAgent question -> `references/agents.md`
- skill question -> `references/skills.md`
- settings or provider question -> `references/configuration.md`
- example-driven question -> `references/examples.md`

## How to Stay Accurate

- distinguish Qwen Code behavior from generic Qwen model capabilities
- prefer the official links in `references/sources.md` for current behavior
- load only the reference file needed for the current task
