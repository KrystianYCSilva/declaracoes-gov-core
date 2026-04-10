---
name: commands
description: |
  Reference for Qwen Code command surface including invocation forms, /init behavior, and project context loading.
  Use when: onboarding to Qwen Code or explaining available slash commands and context file conventions.
---

# Qwen Code command surface

## Commands that matter

- `qwen "<prompt>"` runs a one-shot prompt.
- `qwen -i "<prompt>"` starts interactive mode after the initial prompt.
- `qwen -p "<prompt>" -o text` still works, but `-p` is deprecated in current help output.
- `/init` bootstraps project context.
- `/agents` manages project subagents.
- `/skills` manages skills.
- `/extensions` manages extensions.

## Project context

- Qwen loads `QWEN.md` files based on `context.fileName`.
- The current docs describe searching the current directory, parents, and subdirectories.
- This repository uses `.qwen/QWEN.md` as the preferred Qwen context file.

## `/init` rule here

- Keep the root `AGENTS.md` as the shared cross-agent index.
- Update `.qwen/QWEN.md`.
- Do not create a new root `QWEN.md`.
