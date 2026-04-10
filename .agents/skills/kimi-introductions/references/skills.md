---
name: skills
description: |
  Reference for Kimi CLI skills, discovery paths, and the distinction between skills and custom agents.
  Use when: creating Kimi skills or explaining when skills vs custom agents are the right choice.
---

# Kimi skills

## What to keep clear

- Skills are reusable knowledge/workflow packages.
- They are not the same as custom agents.
- Agents define role and tool boundaries; skills define reusable procedures and references.

## Skill discovery paths

Kimi discovers skills from:
- `.kimi/skills/<name>/SKILL.md` (project-local, recommended)
- `.agents/skills/<name>/SKILL.md` (shared cross-CLI location)

The `SKILL.md` frontmatter must have `name` and `description` (no extra fields per Agent Skills spec).

## Repository guidance

- Keep skills focused and procedural.
- Keep the main Kimi project guidance in `.kimi/AGENTS.md`.
- Use skills for reusable workflows; use custom agents only when role/tool boundaries matter.
