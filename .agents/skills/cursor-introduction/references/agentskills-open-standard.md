---
name: agentskills-open-standard
description: |
  Reference for the Agent Skills open standard as applied in Cursor, with guidance on concise SKILL.md and JIT reference loading.
  Use when: creating Cursor skills or distinguishing skills from rules and commands.
---

# Agent Skills open standard

## What Agent Skills is

Agent Skills is a portable, CLI-agnostic skill format designed to work
across multiple coding-agent tools. A skill packages domain knowledge,
workflows, or reusable guidance into a self-contained directory that any
compatible CLI can discover and load on demand.

## Key spec elements

### SKILL.md entry point

Every skill directory contains a `SKILL.md` file with YAML frontmatter:

```yaml
---
name: my-skill
description: |
  Brief purpose statement.
  Use when: trigger conditions.
---
```

The frontmatter provides `name` and `description` fields so the agent can
decide when to load the skill without reading its full contents.

### references/ directory

Heavy detail, glossaries, examples, and source lists go into `references/`.
Each reference file carries its own frontmatter with a `name` and
`description` so the agent can load only the slice it needs.

### JIT loading

Skills are designed for just-in-time (JIT) loading. The agent reads
`SKILL.md` first. Only when the current task matches a reference does
the agent pull that reference into context, keeping the token budget small.

## How Cursor discovers skills

Cursor looks for skills in two locations:

- `.cursor/skills/` -- Cursor-native skill directory.
- `.agents/skills/` -- shared cross-agent skill directory.

Both paths follow the same Agent Skills layout. Placing skills in
`.agents/skills/` makes them visible to Cursor and every other CLI that
supports the standard.

## Skills vs rules

| Aspect   | `.cursor/rules/` rules       | Agent Skills                  |
|----------|------------------------------|-------------------------------|
| Loading  | Always-on policy injection   | On-demand, JIT loaded         |
| Scope    | Entire project or glob match | Activated by task relevance   |
| Purpose  | Enforce constraints          | Supply domain knowledge       |
| Format   | Markdown with frontmatter    | SKILL.md + references/        |

Rules are best for invariants the agent must always obey (style, security).
Skills are best for domain expertise the agent needs only for specific tasks.
