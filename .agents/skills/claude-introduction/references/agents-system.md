---
name: agents-system
description: |
  Reference for Claude Code custom agents — file format, location, and distinction from skills.
  Use when: creating or explaining custom Claude agents or distinguishing agents from skills.
---

# Claude Code agents

## Official model

- Custom Claude agents live in `.claude/agents/*.md`.
- They use YAML frontmatter plus a Markdown body.
- Common frontmatter fields include `name`, `description`, `tools`, `model`, and `skills`.

## What to avoid

- Do not invent built-in agent names unless the current docs or CLI output confirm them.
- Do not confuse a skill with an agent:
  - skills are reusable task workflows
  - agents are specialized workers with their own prompt and tool scope

## Repository convention

- `.claude/CLAUDE.md` is the main Claude memory file here.
- `.claude/agents/speckit-orchestrator.md` is the Claude-specific Spec Kit router.
- The root `AGENTS.md` remains the shared cross-agent index.
