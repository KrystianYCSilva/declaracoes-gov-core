---
name: claude-introduction
description: |
  Guide Claude Code usage, extension points, and repository integration with a lean JIT reference model.
  Use when: onboarding to Claude Code, explaining `/init`, skills, agents, permissions,
  bundled commands, or locating the right official Claude reference.
---

# Claude Introduction

Use this skill as an entry point, not as a full manual.

## How to Navigate This Skill

- `references/cli-basics.md`: installation, auth, session startup, and core commands
- `references/skills-system.md`: skill structure and authoring patterns
- `references/agents-system.md`: custom agents and delegation
- `references/bundled-skills.md`: bundled commands and built-in workflows
- `references/permissions.md`: approval model and safety controls
- `references/best-practices.md`: prompting and daily workflow patterns
- `references/troubleshooting.md`: failure modes and debugging
- `references/sources.md`: official Claude Code and Agent Skills links

## How to Model Claude Code

- keep `.claude/CLAUDE.md` as the preferred Claude memory file in this repository
- keep the root `AGENTS.md` as the shared cross-agent index
- use skills for reusable task workflows
- use agents for bounded specialized work
- prefer official built-ins before inventing local conventions

## How to Answer Common Requests

- install or first run -> `references/cli-basics.md`
- create or fix a skill -> `references/skills-system.md`
- create or use an agent -> `references/agents-system.md`
- permissions or safety -> `references/permissions.md`
- built-in workflow question -> `references/bundled-skills.md`
- weird behavior or failures -> `references/troubleshooting.md`

## How to Stay Accurate

- separate official Claude behavior from repository-local conventions
- load only the reference needed for the current question
- if a command or capability may have changed, confirm it against `references/sources.md`
