---
name: agents
description: |
  Reference for Qwen Code subagent structure, storage locations (.qwen/agents/), and frontmatter fields.
  Use when: creating Qwen Code subagents or explaining the /agents command.
---

# Qwen Code subagents

## Official model

- Project subagents live in `.qwen/agents/*.md`.
- User-level subagents live in `~/.qwen/agents/`.
- Files use YAML frontmatter plus a Markdown body.
- `/agents create` and `/agents manage` are the native management entrypoints.

## Key fields

- `name`
- `description`
- `tools`

Keep descriptions specific enough for delegation and keep prompts bounded.

## Repository convention

- `.qwen/QWEN.md` is the main project context file here.
- `.qwen/agents/speckit-orchestrator.md` is the Qwen-specific Spec Kit router.
- `.qwen/commands/speckit.orchestrator.md` is the matching Qwen command entrypoint.
