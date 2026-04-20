---
description: |
  Patterns for migrating projects and configurations between AI coding CLIs.
  Use when: switching CLIs, running multiple CLIs on the same project, or consolidating CLI configs.
---

# CLI Migration Guide

## Context File Translation

| Source CLI | Target CLI | How to Migrate |
|-----------|-----------|---------------|
| Claude (`CLAUDE.md`) | Copilot (`AGENTS.md`) | Rename file, keep content (format compatible) |
| Claude (`CLAUDE.md`) | OpenCode (`AGENTS.md`) | OpenCode reads CLAUDE.md natively (fallback) |
| Claude (`CLAUDE.md`) | Codex (`AGENTS.md`) | Rename file, keep content |
| Copilot (`AGENTS.md`) | Any CLI | Most CLIs read AGENTS.md natively or via fallback |
| Gemini (`GEMINI.md`) | Others | Manual translation — Gemini uses cascading hierarchy |
| Cursor (`.cursor/rules/`) | Others | Extract rules content into AGENTS.md format |

## Skill Migration

Use the compile_skills.py script to distribute skills across CLIs:

```bash
# Compile all skills to a specific CLI
python ~/.agents/scripts/compile_skills.py --clis claude gemini

# Compile specific skill to all CLIs
python ~/.agents/scripts/compile_skills.py --skills brownfield-refactoring

# Preview without writing
python ~/.agents/scripts/compile_skills.py --dry-run
```

## Variable Syntax Translation

| CLI Group | Syntax | Example |
|-----------|--------|---------|
| Copilot, Claude, Codex, Cursor, OpenCode | `$ARGUMENTS` | `$ARGUMENTS` |
| Gemini, Qwen | `{{args}}` | `{{args}}` |

The compile_skills.py script handles this translation automatically.

## Multi-CLI Project Setup

When running multiple CLIs on the same project:

1. **Single source of truth**: Keep `AGENTS.md` as the primary context file
2. **CLI-specific overrides**: Use each CLI's native file only for CLI-specific settings
3. **Shared skills**: Point all CLIs to `~/.agents/skills/` or compile with compile_skills.py
4. **Avoid conflicts**: Don't let multiple CLIs modify the same files simultaneously

## Common Migration Pitfalls

1. **Don't copy governance bloat** — migrating is a chance to simplify (40 lines max)
2. **Test commands first** — verify build/test commands work before configuring the new CLI
3. **Reset memory** — don't carry over stale memory/context from the old CLI
4. **Check variable syntax** — `$ARGUMENTS` vs `{{args}}` causes silent failures
