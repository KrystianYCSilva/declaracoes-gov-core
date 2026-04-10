---
name: codex-introduction
description: |
  Guide Codex CLI usage, official customization surfaces, and repository-local integration.
  Use when: onboarding to Codex, explaining `AGENTS.md`, skills, subagents, `/init`,
  slash commands, or deciding between official Codex behavior and local conventions.
---

# Codex Introduction

Use this skill as the router for Codex-specific guidance in this repository.

## How to Navigate This Skill

- `references/codex-cli-basics.md`: install, auth, interactive mode, resume, and non-interactive mode
- `references/agents-and-subagents.md`: built-in agents, custom agents, and delegation
- `references/skills-agents-md-and-prompts.md`: `AGENTS.md`, skills, `/init`, and prompt-file caveats
- `references/repo-local-conventions.md`: how this repository uses `.codex/` and `.codex/prompts/`
- `references/prompting-patterns.md`: prompt shaping for Codex work
- `references/sources.md`: official OpenAI and Agent Skills links

## How to Model Codex

- keep the root `AGENTS.md` as the shared cross-agent index
- keep `.codex/AGENTS.md` as the Codex-specific deep guide in this repository
- use skills for reusable workflows and domain guidance
- use subagents for bounded parallel or specialized work
- treat prompt files as local assets unless the official docs say otherwise

## How to Answer Common Requests

- start or resume Codex -> `references/codex-cli-basics.md`
- create or use a subagent -> `references/agents-and-subagents.md`
- explain `/init`, skills, or `AGENTS.md` -> `references/skills-agents-md-and-prompts.md`
- explain local `.codex/` behavior -> `references/repo-local-conventions.md`
- improve a prompt -> `references/prompting-patterns.md`

## How to Stay Accurate

- distinguish official Codex behavior from repository-local prompt plumbing
- prefer the OpenAI docs in `references/sources.md` for version-sensitive details
- load only the minimum reference set needed for the current question
