---
name: slash-commands-and-prompts
description: |
  Reference for Cursor command file format (.cursor/commands), required frontmatter, and agent handoff configuration.
  Use when: creating Cursor slash commands or configuring agent routing and handoffs.
---

# Cursor commands and prompts

## Command file format (`.cursor/commands/<name>.md`)

```markdown
---
description: "What this command does (shown in autocomplete)"
handoffs:
  - label: "Button Label"
    agent: speckit.specify
    prompt: "Prompt to send to the target agent"
    send: true
---

## User Input

```text
$ARGUMENTS
```

Command body here. Use $ARGUMENTS for user-supplied input.
Reference context with @filename.
```

**Required:** `description` in frontmatter (enables autocomplete).
**Optional:** `handoffs` for routing to specialized agents with one-click buttons.
**Arguments:** `$ARGUMENTS` captures text typed after the slash command name.
**Locations:** `.cursor/commands/` (project) or `~/.cursor/commands` (global).

## Practical model

- Keep commands short, role-specific, and outcome-driven.
- Use `handoffs` when a command should offer next-step choices.
- Prefer repo commands over long copy-pasted prompts.

## Repository convention

- The Spec Kit router lives in `.cursor/commands/speckit.orchestrator.md`.
- Use the shared workflow contract in `docs/SPECKIT-AGENTS.md` for cross-CLI bootstrap rules.
