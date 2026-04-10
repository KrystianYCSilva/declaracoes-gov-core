---
name: config-example
description: |
  Reference examples for OpenCode configuration covering instructions, commands directory, workspace root, and model settings.
  Use when: configuring an OpenCode project or explaining config.json high-value fields.
---

# OpenCode configuration

## High-value fields

- `instructions`: always-on instruction files
- `commands.directory`: location of custom commands
- `workspace.root`: workspace root
- model and permission settings as needed

## Repository convention

This repository uses:

```json
{
  "$schema": "https://raw.githubusercontent.com/opencode-ai/opencode/main/schema.json",
  "name": "project-name",
  "description": "Short project description",
  "instructions": [".opencode/AGENTS.md"],
  "commands": {
    "directory": ".opencode/command"
  },
  "workspace": {
    "root": "."
  }
}
```

## Provider and model configuration

API keys are set via environment variables (e.g., `ANTHROPIC_API_KEY`, `OPENAI_API_KEY`).
OpenCode auto-detects available providers from environment. No explicit config required
unless overriding defaults.

To set a default model:

```json
{
  "model": "anthropic/claude-sonnet-4-5"
}
```

## Guidance

- Keep OpenCode's primary instructions in `.opencode/AGENTS.md`.
- Keep the root `AGENTS.md` as the shared cross-agent index.
- Do not duplicate long policy text in both files.
