---
name: codex-cli-basics
description: |
  Reference for Codex CLI basics including interactive TUI mode, headless invocation, and core capabilities.
  Use when: onboarding to Codex CLI or explaining interactive vs non-interactive operation modes.
---

# Codex CLI basics

## What Codex is

According to the official Codex overview, Codex is OpenAI's coding agent for
software development. It helps write code, understand unfamiliar codebases,
review code, debug problems, and automate engineering tasks.

## Core operating modes

### Interactive mode

Use:

```text
codex
codex "Explain this codebase to me"
```

In interactive mode, Codex opens a full-screen TUI that can:

- read the repository;
- edit files;
- run commands;
- accept prompts, snippets, and screenshots;
- show plans and diffs inline.

Good fit:

- exploratory work;
- iterative edits;
- reviewing plans before execution;
- repo onboarding.

### Resume mode

Use:

```text
codex resume
codex resume --last
codex resume --all
```

Codex stores transcripts locally and can reopen previous sessions so you do not
need to restate all context.

Good fit:

- continuing feature work;
- resuming a review thread;
- picking back up after compaction or interruption.

### Non-interactive mode

Use when you want Codex to run a task from the terminal without staying in the
full interactive UI.

Common pattern:

```text
codex exec "<task>"
```

Good fit:

- automation;
- CI-like local flows;
- one-shot tasks;
- scripted usage.

## Useful live session controls

Common built-in slash commands:

- `/model`
- `/fast`
- `/permissions`
- `/agent`
- `/status`
- `/diff`
- `/compact`
- `/copy`
- `/new`
- `/resume`
- `/init`
- `/mcp`

Use slash commands to control the session itself.
Use natural-language prompts to do the actual engineering task.

## Practical prompting tips

- State the goal first.
- State constraints next.
- Point to concrete files or folders.
- Say what not to change.
- Ask for a plan first when the change is risky.
- When the task is broad, split planning from implementation.

Example:

```text
Read src/ and explain the runtime flow. Do not edit files yet. Call out the
main entrypoints, data flow, and hotspots.
```

Example:

```text
Update the PowerShell path handling in .specify/scripts/powershell. Keep Bash
behavior unchanged. Show the plan before editing.
```
