---
name: setup-agent-memory-and-context
description: |
  Reusable prompt to bootstrap MEMORY.md, root AGENTS.md, and per-agent context files.
  Use when: initializing or migrating a repository to the multi-agent memory pattern.
---

# Setup Agent Memory and Context

*** ROLE ***
You are a repository infrastructure expert. Your task is to create or migrate a project's AI-agent context layer.

*** CONTEXT ***
The repository uses a multi-agent pattern where:
- `AGENTS.md` (root) is the single source of truth for project context.
- `MEMORY.md` (root) is shared long-term memory for cross-session recovery.
- Each LLM CLI has its own folder (e.g., `.claude/`, `.cursor/`, `.github/`) containing:
  - A minimal agent-specific context file (e.g., `CLAUDE.md`, `AGENTS.md`, `copilot-instructions.md`, `.mdc` rules).
  - `memory/agent-local-memory.md` for private session notes.

*** INSTRUCTIONS ***
Determine if this is GREENFIELD (no agent files exist) or BROWNFIELD (some agent files exist but are out of pattern). Then execute exactly:

## Step 1 — Root AGENTS.md
If `AGENTS.md` does NOT exist at repository root, create it by analyzing the codebase and filling:
- Project name, purpose, tech stack, base package.
- Module map (if multi-module).
- Build/test commands.
- Coverage gates (if any).
- Key design conventions (immutability, null-safety, no frameworks, etc.).
- Security considerations.
- `## Memory Model` section explaining `MEMORY.md` and `agent-local-memory.md`.
- `## Key Files for Context` listing critical files.

If `AGENTS.md` ALREADY exists:
- Add YAML frontmatter if missing:
  ```yaml
  ---
  name: <project-name>
  description: |
    <one-line purpose>
    Use when: any AI agent starts a session in this repository.
  ---
  ```
- Ensure it contains a `## Memory Model` section.
- Ensure it contains a `## Core Reminders` section with:
  - `Keep the core framework-agnostic and declaration-agnostic.`
  - `This is <build-system> multi-module core library; live implementation is in <module-pattern>/src and the related <build-files>.` (adapt to the actual build system).

## Step 2 — Root MEMORY.md
Create `MEMORY.md` at repository root with:
```yaml
---
name: project-memory
description: |
  Shared long-term memory for cross-session context recovery.
  Use when: any agent starts a session or finishes a task.
---
```

Then the body:
- `## Active` table: Agent | Topic | Status.
- `## Completed` list: date | agent | brief summary.
- `## Rules` explaining update cadence and scope.

## Step 3 — Per-Agent Memory
For EVERY known agent folder present or expected (`.agents`, `.claude`, `.codebuddy`, `.codex`, `.cursor`, `.gemini`, `.junie`, `.kimi`, `.opencode`, `.qwen`, `.vibe`, `.github`):
1. Ensure `<folder>/memory/` exists.
2. Create `<folder>/memory/agent-local-memory.md` with YAML frontmatter:
   ```yaml
   ---
   name: agent-local-memory
   description: |
     Private scratchpad for the current agent session.
     Use when: this specific agent needs to persist session notes.
   ---
   ```
3. Body: `## Current Session` and `## Notes` sections.

## Step 4 — Per-Agent Context Files
Create or UPDATE the agent-specific context file for each detected folder. Rules per CLI:

| Folder | File | Format Rules |
|--------|------|--------------|
| `.claude` | `CLAUDE.md` | Markdown. YAML frontmatter. Point to root `AGENTS.md`. Point to `MEMORY.md`. Add the two Core Reminders lines. |
| `.codebuddy` | `CODEBUDDY.md` | Same as above. |
| `.codex` | `AGENTS.md` | Same as above. |
| `.cursor` | `rules/01-core-rules.mdc` | **MANDATORY**: `.mdc` file inside `rules/` folder. YAML frontmatter with `alwaysApply: true`. Same bullet content. **DO NOT create `.cursor/CURSOR.md`**. |
| `.gemini` | `GEMINI.md` | Markdown. YAML frontmatter. Same structure. |
| `.github` | `copilot-instructions.md` | Markdown. YAML frontmatter. Same structure. |
| `.junie` | `AGENTS.md` | Markdown. YAML frontmatter. Same structure. Mention `commands/` if present. |
| `.kimi` | `AGENTS.md` | Markdown. YAML frontmatter. Same structure. Mention `skills/` if present. |
| `.opencode` | `AGENTS.md` | Markdown. YAML frontmatter. Same structure. Mention `command/` if present. |
| `.qwen` | `QWEN.md` | Markdown. YAML frontmatter. Same structure. Mention `commands/` if present. |
| `.vibe` | `AGENTS.md` | Markdown. YAML frontmatter. Same structure. Mention `prompts/` if present. |

### Content contract for every agent context file
- YAML frontmatter with `name`, `description`, `Use when`.
- Bullet: `Read AGENTS.md (root) first for full project context.`
- Bullet: `Read MEMORY.md at session start; update at task start/finish.`
- Bullet: `Write private details to memory/agent-local-memory.md.`
- Bullet: `Keep the core framework-agnostic and declaration-agnostic.` (adapt if needed)
- Bullet: `This is <build-system> multi-module core library; live implementation is in <module-pattern>/src and the related <build-files>.` (adapt to actual project)
- Add ONE bullet about the CLI-specific folder (skills, commands, prompts) ONLY if that folder exists.

## Step 5 — Validation
Before finishing, verify:
- [ ] `AGENTS.md` exists at root and has YAML frontmatter.
- [ ] `MEMORY.md` exists at root and has YAML frontmatter.
- [ ] No agent folder has a redundant full copy of root `AGENTS.md` content.
- [ ] `.cursor` uses `.mdc` inside `rules/`, not `.md` at root.
- [ ] `.agents/` does NOT have its own `AGENTS.md` (it is shared skills only).
- [ ] Every `agent-local-memory.md` has YAML frontmatter.

*** CONSTRAINTS ***
- NEVER duplicate the full root `AGENTS.md` into an agent folder.
- NEVER create `.agents/AGENTS.md`.
- NEVER create `.cursor/CURSOR.md` or `.cursor/*.md` at the folder root; use `.cursor/rules/*.mdc` instead.
- Keep each agent file under 15 lines of actual content (YAML + bullets only).
- Write all AI-facing docs in English.
