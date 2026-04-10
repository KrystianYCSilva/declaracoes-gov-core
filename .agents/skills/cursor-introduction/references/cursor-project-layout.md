---
name: cursor-project-layout
description: |
  Reference for Cursor project layout covering .cursor/rules, .cursor/commands, and Agent Skills directory conventions.
  Use when: setting up a Cursor project structure or explaining Cursor's official customization surfaces.
---

# Cursor project layout

## Official surfaces that matter

- `.cursor/commands/` for reusable slash commands (Markdown, `.md`)
- `.cursor/rules/` for project rules (`.mdc` files, replaces legacy `.cursorrules`)
- Agent Skills under `.agents/skills/` or `.cursor/skills/`

## Rules format (`.cursor/rules/*.mdc`)

```yaml
---
description: "Brief description of when this rule applies"
globs: ["**/*.ts", "**/*.tsx"]   # omit for description-only activation
alwaysApply: false               # true = loads in every chat
---
# Rule Title

- Actionable guideline one
- Actionable guideline two
```

**Activation modes:**
- `alwaysApply: true` — loads in every chat (use sparingly for universal conventions)
- `globs:` — loads only when matching files are in context
- description-only — intelligent inclusion based on request content
- no frontmatter — loaded only via explicit `@rule-name` mention

Legacy `.cursorrules` in project root is deprecated; `.mdc` takes precedence.

## Repository convention

- `.cursor/commands/speckit.orchestrator.md` is the Cursor entrypoint for Spec Kit routing.
- The root `AGENTS.md` stays the shared cross-agent index.
- Keep Cursor-specific workflow assets inside `.cursor/`.
