---
name: project-memory
description: |
  Shared long-term memory for cross-session context recovery.
  Use when: any agent starts a session or finishes a task.
---

# Project Memory

## Active
<!-- agent | topic | status (one line) -->
| Agent | Topic | Status |
|-------|-------|--------|
| — | — | idle |
| 002-core-transport | Kimi | Reviewing spec and ADR-008 alignment |

## Completed
<!-- date | agent | brief summary (one line per item) -->

## Rules
- Read this file at session start. Update at task start and finish.
- One line per entry. Details go to `<agent-dir>/memory/agent-local-memory.md`.
- This is cross-session recovery, not live session memory. Do not overwrite other agents' active rows.
