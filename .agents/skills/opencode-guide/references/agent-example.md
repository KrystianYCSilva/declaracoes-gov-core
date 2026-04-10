---
name: agent-example
description: |
  Reference examples for OpenCode agent configuration including mode, tools, and repository conventions.
  Use when: creating or explaining OpenCode project agents in .opencode/agents/.
---

# OpenCode agents

## Official model

- Project agents live in `.opencode/agents/*.md`.
- Frontmatter commonly includes `description`, `mode`, `tools`, `permission`, and model settings.
- `mode: subagent` is the normal delegated-worker shape.
- `mode: primary` is the primary-agent shape.

## Repository convention

- `.opencode/AGENTS.md` is the OpenCode instruction file here.
- `.opencode/agents/speckit-orchestrator.md` is the OpenCode subagent for Spec Kit routing.
- `.opencode/command/speckit.orchestrator.md` is the matching command entrypoint.

## Guidance

- Keep agent prompts short and role-specific.
- Put shared repository rules in `.opencode/AGENTS.md`, not inside every subagent.
