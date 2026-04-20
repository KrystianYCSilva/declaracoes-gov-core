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

## Completed
<!-- date | agent | brief summary (one line per item) -->
| 2026-04-20 | Gemini | Feature 001 (core-extensibility) merged to develop |
| 2026-04-20 | Kimi | Feature 002 (core-transport) merged to develop — BUILD SUCCESS |

## Rules
- Read this file at session start. Update at task start and finish.
- One line per entry. Details go to `<agent-dir>/memory/agent-local-memory.md`.
- This is cross-session recovery, not live session memory. Do not overwrite other agents' active rows.
