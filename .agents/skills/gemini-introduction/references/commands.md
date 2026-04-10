---
name: commands
description: |
  Reference for Gemini CLI terminal commands, headless invocation, authentication, and GEMINI.md @-include syntax.
  Use when: onboarding to Gemini CLI or explaining interactive vs headless operation.
---

# Gemini CLI command surface

Use this file for current Gemini CLI operation, not Gemini API behavior.

## Authentication

```sh
gemini auth          # authenticate with Google account
```

## Terminal commands that matter

- `gemini` starts an interactive session.
- `gemini -p "<prompt>"` runs headless (non-interactive, prints to stdout).
- `gemini -p "<prompt>" --output-format json` runs headless with JSON output.
- `gemini mcp` manages MCP servers.

## Session management commands

- `/chat save <name>` -- save the current session under a label.
- `/chat list` -- list all saved sessions.
- `/chat resume <name>` -- resume a previously saved session by label.

Session management lets you switch between tasks without losing context.
Use short, descriptive names when saving sessions.

## Model and context commands

- `/model` -- display or switch the active model.
- `/context` -- display the current context window contents and token usage.

These commands are useful for inspecting what the model sees and for
selecting a different model variant during an interactive session.

## File and directory context inclusion

Use `@` references to pull files or directories into context:

- `@path/to/file.java` -- include a single file.
- `@path/to/directory` -- include all files under a directory.

`@` references work both in `GEMINI.md` (loaded at startup) and in
interactive prompts (loaded on demand). They expand to the literal file
contents, so keep the referenced material within the model's context
limit.

## GEMINI.md includes

`GEMINI.md` supports `@path/to/file` to include another file's content at load time:

```markdown
@../AGENTS.md

# Gemini-Specific Notes
...
```

## Repository context

- This repository keeps Gemini memory in `.gemini/GEMINI.md`.
- Gemini custom commands live in `.gemini/commands/` as `.toml` files.
- Use `.gemini/commands/speckit.orchestrator.toml` as the Gemini-native Spec Kit entrypoint.

## `/init` rule here

- Preserve the root `AGENTS.md`.
- Update `.gemini/GEMINI.md`.
- Do not create a new root `GEMINI.md`.
