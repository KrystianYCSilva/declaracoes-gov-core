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

## Step 5 — `.context/` Directory (RFC Full Level)
Create the AI context directory with tiered depth that does not fit in `AGENTS.md`.

### Required structure
```
.context/
├── README.md                        # Navigation hub + Tier system
├── ai-assistant-guide.md            # Full AI protocol (bootstrap, request routing, Definition of Done)
├── _meta/                           # T2 — Project identity
│   ├── project-overview.md          # Scope, module map, boundaries, consumers
│   ├── tech-stack.md                # Exact dependency/plugin versions and constraints
│   └── key-decisions.md             # Consolidated ADRs (minimum 3)
├── standards/                       # T0-T1 — Rules and norms
│   ├── architectural-rules.md       # T0 absolute rules with CORRECT/FORBIDDEN examples
│   ├── code-quality.md              # T1 conventions, package organization, naming
│   └── testing-strategy.md          # T1 test framework, coverage gates, patterns
├── patterns/                        # T1 — Blueprints
│   └── architecture.md              # Design patterns with code examples
├── knowledge/                       # T3 — Deep domain knowledge
│   └── domain-concepts.md           # Domain algorithms, tables, normalization rules
└── workflows/                       # T2 — Operational guides
    └── development-workflows.md     # Build, test, publish, troubleshooting
```

### Metadata contract
Every `.context/` file MUST have YAML frontmatter:
```yaml
---
description: |
  <one-line purpose>
  Use when: <trigger for loading this file>
---
```

### Tier system (mandatory)
| Tier | Kind | Authority | Directory |
|------|------|-----------|-----------|
| T0 | Enforcement | ABSOLUTE | `standards/architectural-rules.md` |
| T1 | Standards | NORMATIVE | `standards/`, `patterns/` |
| T2 | Context | INFORMATIVE | `_meta/`, `workflows/` |
| T3 | Examples | ILLUSTRATIVE | `knowledge/` |

## Step 6 — CI/CD Pipeline
Create GitHub Actions workflow and PR governance.

### 6.1 — Workflow `.github/workflows/ci.yml`
Requirements:
- Trigger on `push` to `main`/`master`/`develop` and on all `pull_request`.
- JDK 8 (Temurin) with Maven cache.
- Run `mvn -B verify` as the gate.
- Upload surefire reports and JaCoCo HTML reports as artifacts.
- (Optional but recommended) Post coverage summary comment on PRs.

### 6.2 — PR Template `.github/pull_request_template.md`
Must include checklists for:
- T0 compliance (architectural rules).
- Testing & coverage (`mvn verify`, unit tests, JaCoCo gates).
- Documentation sync (Javadoc, AI docs, `MEMORY.md`).

### 6.3 — Branch Protection Note
Document that the team must enable in GitHub Settings:
- Require status checks to pass before merging.
- Select the CI workflow check (`Build & Verify`).

## Step 7 — Spec Kit Constitution (if `.specify/` exists)
If the repository contains a `.specify/` directory:
1. Run `/speckit.constitution`.
2. Pass the following information so the constitution understands the project structure and memory governance:
   - Project name, purpose, and base package.
   - Module map and dependency rules.
   - Build system and validation gate command.
   - Memory model: `MEMORY.md` (shared) + `agent-local-memory.md` (private).
   - Agent folder structure and CLI-specific rules (e.g., `.cursor/rules/*.mdc`).
   - Tier system and T0 rules summary.
   - Definition of Done (coverage gates, test conventions).
3. Ensure the constitution output is stored under `.specify/` and referenced from `AGENTS.md` if relevant.

## Step 8 — Validation
Before finishing, verify:
- [ ] `AGENTS.md` exists at root and has YAML frontmatter.
- [ ] `MEMORY.md` exists at root and has YAML frontmatter.
- [ ] No agent folder has a redundant full copy of root `AGENTS.md` content.
- [ ] `.cursor` uses `.mdc` inside `rules/`, not `.md` at root.
- [ ] `.agents/` does NOT have its own `AGENTS.md` (it is shared skills only).
- [ ] Every `agent-local-memory.md` has YAML frontmatter.
- [ ] `.context/` exists with all required subdirectories (`_meta/`, `standards/`, `patterns/`, `knowledge/`, `workflows/`).
- [ ] Every `.context/` file has YAML frontmatter with `description` and `Use when`.
- [ ] `.github/workflows/ci.yml` exists and runs `mvn -B verify` (or project-specific gate).
- [ ] `.github/pull_request_template.md` exists with T0 and coverage checklists.
- [ ] If `.specify/` exists, `/speckit.constitution` was executed and outputs are persisted.

*** CONSTRAINTS ***
- NEVER duplicate the full root `AGENTS.md` into an agent folder.
- NEVER create `.agents/AGENTS.md`.
- NEVER create `.cursor/CURSOR.md` or `.cursor/*.md` at the folder root; use `.cursor/rules/*.mdc` instead.
- Keep each agent file under 15 lines of actual content (YAML + bullets only).
- Write all AI-facing docs in English.
