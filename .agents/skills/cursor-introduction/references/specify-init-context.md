---
name: specify-init-context
description: |
  Reference for bootstrapping Spec Kit in Cursor projects using specify init with the cursor-agent backend.
  Use when: initializing Cursor with the Spec Kit workflow or explaining the Cursor Spec Kit entrypoint.
---

# Spec Kit bootstrap for Cursor

## Bootstrap rule

- Use `specify init . --ai=cursor-agent --script <ps|sh>` when the user explicitly wants Cursor bootstrap.
- Use `ps` on Windows and `sh` on Linux or macOS.

## Repository convention after bootstrap

- Keep the root `AGENTS.md` as the shared cross-agent index.
- Keep Cursor workflow files under `.cursor/`.
- Use `.cursor/commands/speckit.orchestrator.md` as the Cursor routing entrypoint.
