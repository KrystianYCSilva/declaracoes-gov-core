---
name: repo-local-conventions
description: |
  Reference for repository-local Codex conventions using the root AGENTS.md and .codex/AGENTS.md two-layer structure.
  Use when: setting up or explaining the Codex project instruction layout.
---

# Repository-local Codex conventions

This repository uses a two-layer Codex contract:

- root `AGENTS.md`: shared multi-CLI index
- `.codex/AGENTS.md`: Codex-specific guidance

## Local layout

```text
.codex/
  AGENTS.md
  agents/
```

## Practical meaning

- Keep shared project rules in the root index.
- Keep Codex-only details in `.codex/AGENTS.md`.
- Keep reusable delegation roles in `.codex/agents/`.
- Treat Spec Kit prompt files as repo workflow assets, not as proof of an official Codex prompt plugin system.
