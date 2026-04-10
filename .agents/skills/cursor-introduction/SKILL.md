---
name: cursor-introduction
description: |
  Guide Cursor IDE and Cursor Agent workflows, skills, rules, and project commands.
  Use when: onboarding to Cursor, clarifying `cursor-agent`, Agent Skills,
  `.cursor/commands`, or the boundary between rules, skills, and prompts.
---

# Cursor Introduction

Use this skill as the map for Cursor-specific workflows and local Cursor assets.

## How to Navigate This Skill

- `references/cursor-agents-cli.md`: `cursor-agent` install, CLI usage, and automation
- `references/cursor-project-layout.md`: `.cursor/` layout, rules, skills, and commands
- `references/slash-commands-and-prompts.md`: reusable commands and prompt surfaces
- `references/agentskills-open-standard.md`: Agent Skills structure and progressive disclosure
- `references/specify-init-context.md`: local Spec Kit bootstrap context for Cursor
- `references/sources.md`: official Cursor and Agent Skills links

## How to Model Cursor

- use rules for always-on project guidance
- use skills for task-specific reusable workflows
- use `.cursor/commands/` for reusable prompts invoked from the product
- treat `cursor-agent` as the terminal automation surface for Cursor behavior

## How to Answer Common Requests

- CLI or terminal automation -> `references/cursor-agents-cli.md`
- project layout or file placement -> `references/cursor-project-layout.md`
- commands or prompts -> `references/slash-commands-and-prompts.md`
- skill authoring or validation -> `references/agentskills-open-standard.md`
- Spec Kit bootstrap in this repo -> `references/specify-init-context.md`

## How to Stay Accurate

- separate official Cursor behavior from the open Agent Skills standard
- separate Cursor behavior from repository-local Spec Kit overlays
- if a capability may have changed, confirm it against `references/sources.md`
