---
description: Criar ou atualizar a especificação da feature a partir de uma descrição em linguagem natural.
---

## ⚠️ Verificar versão do flow antes de tudo

```bash
flow -v
flow --help
```

Este prompt é o ponto de entrada do workflow v1.x (3-prompt): `/flow.refine` → `/flow.plan` → `/flow.build`. Se a versão instalada for 0.x, consulte `.agents/skills/flow-fundamentals/references/flow-v0.x.md` antes de prosseguir.

## PLANNING MODE — NO IMPLEMENTATION ALLOWED

**You are a PLANNING AGENT. Your role is analysis, design, and documentation ONLY.**

ABSOLUTE RULES (violations will corrupt the workflow):
1. DO NOT create, edit, or delete any source code files
2. DO NOT create worktrees or implementation workspaces
3. DO NOT run build commands, compilers, or package installers
4. DO NOT write implementation code in any form (not even "examples" or "scaffolds")
5. DO NOT proceed to the next workflow phase — STOP when this phase's artifact is complete

PERMITTED ACTIONS:
- Read and analyze existing code for planning purposes
- Create/update markdown planning documents (spec.md, plan.md, tasks.md)
- Run read-only commands (git status, git log, git diff, ls, cat)
- Ask the user clarifying questions
- Commit planning artifacts (markdown files in flow-specs/ only)

If you find yourself about to write implementation code, STOP and re-read these rules.

## Response Language Policy

- Always respond in the same language as the user's latest message.
- If the user writes in Portuguese, respond in Portuguese (pt-BR).
- Keep CLI commands, paths, and code snippets unchanged unless translation is explicitly requested.

# /flow.refine - Create Feature Specification

{INCLUDE: shared/user-input.md}

## Goal

Produce a complete, implementation-ready `spec.md` in `flow-specs/<feature>/` by running discovery until confidence is sufficient.

## Location

Run from the planning repository root (main repo). Do not run from WP worktrees.

## External Document Detection (auto — runs before discovery)

Before starting the discovery loop, check if `$ARGUMENTS` contains or references a structured discovery document. If detected, this section **replaces the entire refine workflow** and also generates plan + task artifacts in a single session.

### Step 1 — Detect

1. **File path**: If `$ARGUMENTS` contains a file path (absolute path or `~/...`), read the file. If the file doesn't exist, fall through to normal discovery below.
2. **Pasted content**: If `$ARGUMENTS` itself contains structured content (not just a brief description), check it directly.
3. **Structure detection rules** (check in order, stop at first match):
   - `# Diagnosis:` header with sections like "Root Cause", "Evidence", "Impact" → type: `"diagnosis"`
   - RFC or ADR headers/structure → type: `"rfc"` or `"adr"`
   - Clear problem + evidence + code-references structure → type: `"generic-discovery"`
4. **If nothing detected**: Fall through to the normal Discovery Loop below — continue as if this section didn't exist.

### Step 2 — Minimal Confirmation (1 message only)

Ask the user in **ONE message** (in the user's language):
- **Target branch**: "Para qual branch essa feature vai quando estiver pronta?" / "Which branch does this feature go to when it's ready?"
- **Scope confirmation**: "Analisei [document title]. Entendi que o objetivo é: [1-2 sentence summary of what needs to be done]. Confirma ou quer ajustar o escopo?"

Wait for the user's response before proceeding.

### Step 3 — Create feature

Derive a slug from the document title (kebab-case, concise). Then:

```bash
flow agent feature create-feature "<slug>" --target-branch "<branch>" --json
```

Parse JSON output, capture `feature` (full slug with numeric prefix) and `feature_dir` (absolute path).

### Step 4 — Generate spec.md

Map the external document content to the spec template structure:

**Diagnosis document mapping:**
- Root Cause (§4) → Problem Statement + desired outcome (the fix)
- Impact (§2) + Affected Entities (§7) → Scope, key entities
- Faulty Code Path (§4.1) + Code References (§6) → Constraints (files/methods that need changing)
- Evidence (§5) → Acceptance Criteria (how to verify the fix)
- Timeline (§3) → Context/background
- What Went Wrong (§4.3) + Why It Wasn't Caught (§4.4) → Edge cases, risks

**RFC/ADR document mapping:**
- Decision/Proposal → Problem Statement
- Rationale → Functional Requirements
- Consequences/Trade-offs → Risks, success criteria

**Generic discovery mapping:**
- Best-effort extraction of problem, solution, scope, acceptance criteria

**Required sections in the generated spec.md:**
- `## Source Document` section at the top (analogous to `## Source PRD`) with the document path and a structured summary
- Standard spec sections: User Scenarios, Requirements, Success Criteria — all mapped from the document
- `## Appendix: Original Discovery Document` at the end containing the full original document verbatim

Update `meta.json` in `feature_dir` with:
- `friendly_name`: derived from document title
- `source_description`: 1-line summary
- `discovery_mode`: `"external"`
- `discovery_source`: `{"type": "<detected-type>", "path": "<absolute-path-or-null>", "date": "<date-from-doc-or-today>"}`
- `complexity_level`: assessed from document severity/impact (diagnosis with financial exposure → `"complex"` or `"critical"`)
- `prd_content`: summary extracted from document
- `prd_id`: short PRD/ticket identifier for commit prefixing (e.g., `"PRD-148407"`, `"PROJ-1234"`). Extract from document title, URL, or ticket reference. If no clear ID, set to `null`.
- `vcs`: `"git"`

### Step 5 — Auto-detect project conventions

Scan the repository to detect tooling and conventions (same detection as `/flow.plan`):

| Category | What to check |
|----------|--------------|
| Language/Runtime | File extensions in `src/`, `pyproject.toml`, `package.json`, `Cargo.toml`, `go.mod`, etc. |
| Package manager | `uv.lock`, `poetry.lock`, `pnpm-lock.yaml`, `yarn.lock`, `package-lock.json`, etc. |
| Linter | `ruff.toml`, `[tool.ruff]`, `.eslintrc*`, `biome.json`, etc. |
| Formatter | `[tool.ruff.format]`, `[tool.black]`, `.prettierrc*`, etc. |
| Test runner | `conftest.py`, `jest.config.*`, `vitest.config.*`, etc. |
| Pre-commit hooks | `.pre-commit-config.yaml` |
| CI/CD | `.github/workflows/*.yml`, `.gitlab-ci.yml`, etc. |
| Build system | `Makefile`, `Taskfile.yml`, `justfile`, etc. |

Use `ls`, `cat`, and `grep` on the repo root. Keep it fast.

### Step 6 — Generate plan.md

Using spec.md + original document + detected conventions, generate `plan.md` in `feature_dir`.

Follow the plan template structure:
- `## Project Conventions` section at the TOP with detected conventions
- `## Summary`: primary requirement + technical approach
- `## Technical Context`: language, dependencies, storage, testing, constraints
- `## Constitution Check`: if `.flowflow/memory/constitution.md` exists, validate alignment
- `## Project Structure`: directory layout
- `## Parallel Work Analysis`: dependency graph and work distribution

Also generate secondary artifacts in `feature_dir` as needed:
- `research.md` (if unknowns require research)
- `data-model.md` (if entities/data are involved)
- `contracts/` (if API/interface work exists)

### Step 7 — Generate tasks + WP prompt files (via subagent)

**⚠️ CRITICAL: Delegate WP generation to a subagent.** Do NOT generate WPs yourself — this prevents context exhaustion.

**Use the Agent tool** to spawn a subagent with the following prompt (replace placeholders):

```
You are generating work packages for feature "<feature-slug>".

## Step 1: Load Context

Read these files in order:
1. `<FEATURE_DIR>/spec.md` (required — the feature specification)
2. `<FEATURE_DIR>/plan.md` (required — the implementation plan with project conventions)
3. `<FEATURE_DIR>/meta.json` (required — check discovery_source for original document context)
4. `.flowflow/memory/constitution.md` (optional — project standards)

If meta.json has `discovery_source.path`, also read the original document for additional detail.

## Step 2: Run Prerequisites

```bash
flow agent feature check-prerequisites --json --paths-only --include-tasks
```

Parse JSON and use absolute FEATURE_DIR for every file operation.

## Step 3: Build Subtask Inventory

Build internal subtask inventory (T001...) and dependencies.

## Step 4: Roll Up Into Work Packages

Use strict sizing rules:
- target: 3-7 subtasks per WP
- hard max: 10 subtasks per WP
- if a WP exceeds max, split it — 20 focused WPs > 5 overwhelming ones
- target prompt size per WP file: 200-500 lines
- hard max: 700 lines

## Step 5: Write tasks.md

Write FEATURE_DIR/tasks.md using the tasks template contract from
src/cli_flow/missions/software-dev/.flowflow/templates/tasks-template.md.

## Step 6: Generate ALL WP Prompt Files

Generate per-WP files in FEATURE_DIR/tasks/WPxx-<slug>.md with:
- frontmatter: work_package_id, title, lane: "planned", subtasks, dependencies
- detailed subtask prompts (30-70 lines each): objective, steps, files, validation, edge cases

## Step 7: Dependency Detection

Parse dependencies from sequencing and explicit statements.
Include dependency-aware implement commands in WP prompts.

## Step 8: Finalize

```bash
flow agent feature finalize-tasks --json
```
```

After the subagent completes, verify all artifacts were created.

### Step 8 — Telemetry

```bash
flow telemetry emit --feature "<feature-slug>" --event spec_completed || true
flow telemetry emit --feature "<feature-slug>" --event plan_completed || true
```

### Step 9 — Report and STOP

Report:
- Feature ID/slug
- Absolute paths: `spec.md`, `plan.md`, `tasks.md`
- WP count and subtask distribution
- Dependency highlights
- Next command: `/flow.build <feature-number>`

### ⛔ MANDATORY STOP POINT (External Document Mode)

**This command is COMPLETE after generating all planning artifacts.**

**YOU MUST STOP HERE.**

Do NOT:
- ❌ Run `/flow.build` or create implementation branches
- ❌ Start implementing any code
- ❌ Create worktrees or workspaces

The user will run `/flow.build` when they are ready.

---

## Discovery Loop (mandatory — skipped when External Document Detection matches above)

### Express Mode (--express flag)

If `$ARGUMENTS` contains `--express`:
1. Strip `--express` from arguments to get the feature description.
2. Skip ALL discovery questions including PRD check.
3. Generate spec from description using sensible defaults.
4. Set `complexity_level: "trivial"` and `discovery_mode: "express"` in meta.json.
5. Set `implementation_path: "direct"` (express mode always uses the direct path).
6. Run `flow agent feature create-direct-wp --feature "<feature-slug>" --json` to auto-generate WP01.
7. Document assumptions clearly in spec.md's Assumptions section.
8. Report to the user:
   > ✅ WP01 generated. Run now: /flow.build
   > The agent will implement, test, merge and cleanup automatically.

### PRD / External Reference (mandatory first question)

Before any other discovery, ask the user for the external PRD or requirements document:

> "Do you have a PRD, Jira ticket, or external requirements document for this feature? If so, please paste the link or the content."

Rules:
- This is always the **first question** in the discovery loop, regardless of complexity.
- If the user provides a URL or ticket ID, record it immediately in your internal notes.
- If the user provides PRD content (pasted text), use it as **primary source of truth** for discovery — extract problem, actors, scope, acceptance criteria, and constraints from it before asking further questions.
- If the user says there is no PRD, proceed normally with discovery questions.
- When a PRD is provided, the remaining discovery questions should focus on **clarifying gaps or ambiguities in the PRD**, not re-asking what the PRD already answers.

### Question Delivery: Batch Mode

**IMPORTANT: Do NOT ask questions one at a time.** After the PRD check above, present ALL remaining discovery questions in a **SINGLE message** as a numbered list.

Format example:
> Based on your description, I have a few questions before writing the spec:
> 1. [Question about scope/boundaries]
> 2. [Question about actors/users]
> 3. [Question about constraints]
> Please answer all — brief answers are fine.

Confidence is sufficient only when these are clear:
- problem and desired outcome
- primary users/actors
- in-scope and out-of-scope behavior
- acceptance scenarios and success criteria
- key constraints/risks

Scale the NUMBER of questions in the batch by complexity:
- trivial/test features: 0-2 questions in batch
- simple features: 2-3 questions in batch
- complex features: 3-5 questions in batch
- critical/platform features: 5+ questions in batch

User intent signals:
- If user says "just testing", "use defaults", "keep it simple", or equivalent: skip batch entirely and proceed with sensible defaults.

### Follow-up Protocol

After the user responds to the batch:
- Score confidence against the rubric below.
- If confidence >= 90%: proceed to spec generation.
- If confidence < 90%: ask up to 2 targeted follow-up questions in ONE additional message.
- Maximum: 3 message exchanges total (PRD Q&A + batch Q&A + optional follow-up Q&A).
- If still < 90% after follow-up, summarize assumptions and ask confirmation to proceed.

Rules:
- Treat the ranges above as guidance, not a hard stop. The batch + follow-up should reach at least 90% confidence.
- Always stop when the user explicitly asks to stop, proceed, or accept defaults.
- If confidence is not sufficient after the batch response, ask up to 2 follow-up questions in a single message and end with `WAITING_FOR_DISCOVERY_INPUT`.
- Keep an internal notes table (`Question`, `Why`, `Current insight`), but do not display it.
- Summarize intent and get confirmation before creation.

90% confidence criteria (explicit rubric):
- Score confidence as `resolved_criteria / applicable_criteria`.
- Mark each criterion as Resolved, Open, or Deferred by user.
- Base criteria:
  - problem/outcome is explicit and testable
  - primary actors and permissions are clear
  - in-scope and out-of-scope boundaries are explicit
  - main user flow is clear end-to-end
  - edge/error/failure behavior is defined for high-risk paths
  - functional requirements are specific and verifiable
  - success criteria are measurable
  - key constraints/dependencies are explicit
  - assumptions and risks are captured with owners/next action
  - mission/title/slug alignment is unambiguous
- Applicable criteria exclude truly irrelevant items (for example, no permissions model for a single-user local test tool).
- 90% means at least 9 of 10 applicable criteria are Resolved (or equivalent ratio for fewer applicable criteria).
- Deferred-by-user criteria count as unresolved risk unless the user explicitly accepts the tradeoff to proceed.

## Mission Selection

Choose mission from intent:
- `software-dev`: building or changing software
- `research`: investigation/analysis deliverables

If mission confidence is low, ask one confirmation question.

## Title and Slug

Before creation, confirm a short friendly title and derive a slug from it.
- Title should be concise and specific.
- Slug must be kebab-case and used explicitly in create-feature command.

## Execution Steps

⚠️ **CRITICAL: NEVER create the feature folder manually with `mkdir` or any file tool.**
The folder MUST be created by `flow agent feature create-feature` — this is the only command that assigns the required `001-` numeric prefix. A folder without this prefix is invisible to all flow commands (kanban, build, merge, status).

1. **Ask the developer which branch to target** — **MANDATORY**: Ask in the same language the user is using. Portuguese: "Para qual branch essa feature vai quando estiver pronta?" / English: "Which branch does this feature go to when it's ready?" — **NEVER auto-detect or silently default to `master`/`main`**. Wait for explicit confirmation before proceeding.

2. When intent, title, mission, and **target branch** are confirmed, run:

```bash
flow agent feature create-feature "<slug>" --target-branch "<branch>" --json
```

3. Parse JSON output and capture:
- `feature` — full slug WITH numeric prefix, e.g. `001-iva-filtros-mascaras` ← use this everywhere
- `feature_dir` (absolute path) ← use this for ALL subsequent file operations, never construct it manually

4. Update `meta.json` in `feature_dir` with additional fields:
- `friendly_name`
- `source_description`
- `vcs` (default `git`)
- `prd_url` (URL or Jira ticket ID if provided; `null` if none)
- `prd_id` (short PRD/ticket identifier for commit prefixing, e.g., `"PRD-148407"`. Extract from `prd_url` — use the Jira ticket key, PRD number, or similar short ID. If `prd_url` is a full URL like `https://jira.example.com/browse/PRD-148407`, extract `"PRD-148407"`. If no clear ID, set to `null`)
- `prd_content` (summary of PRD content if pasted inline; omit if only URL was given or no PRD exists)
- `complexity_level` — assessed during discovery: `"trivial"` | `"simple"` | `"complex"` | `"critical"`
- `discovery_mode` — `"express"` if user passed --express or said "skip questions"/"just testing", otherwise `"standard"`

Note: `target_branch` and `mission` are already set by `create-feature`.

5. Write `spec.md` using discovery answers as source of truth:
- **PRD reference** (if provided): include a "Source PRD" section at the top of spec.md with the URL/ticket ID and a brief summary of what the PRD covers. If PRD content was pasted, include the key points extracted from it.
- **PRD-driven naming**: When a PRD is provided, use its terminology, domain language, and feature names as the basis for naming in spec.md (user stories, requirements, entities). Do not invent new names for concepts the PRD already names.
- user scenarios
- functional requirements (testable)
- success criteria (measurable)
- key entities (when relevant)
- assumptions and dependencies

Planning artifacts (spec.md, meta.json) are created by `create-feature` but **NOT committed**.
They will be committed as the first commit on the implementation branch when you run `/flow.build`.
**Do NOT run `git commit` manually** during planning.

> **Note:** `spec.md` must be written before `create-direct-wp` is called — the command extracts content from it.

### Direct Path Branch (trivial/simple features)

After writing `spec.md` and updating `meta.json` with `complexity_level`:

- If `complexity_level` is `"trivial"` or `"simple"`:
  1. Run:
     ```bash
     flow agent feature create-direct-wp --feature "<feature-slug>" --json
     ```
  2. Parse JSON response; capture `wp_file` path and `feature_slug`.
  3. `implementation_path: "direct"` is already written to `meta.json` by the command.
  4. Report to the user and stop:
     ```
     ✅ Feature `<complexity_level>` ready!

     WP01 generated at flow-specs/<slug>/tasks/WP01-<slug>.md

     👉 Run now: /flow.build
     The agent will implement, test, merge and cleanup automatically.
     ```

- If `complexity_level` is `"complex"` or `"critical"`:
  1. Write `"implementation_path": "full"` into `meta.json` directly (update the file).
  2. Continue with the remaining execution steps (validate, etc.).

6. Validate quality:
- no unresolved clarification markers
- no implementation leakage in requirements
- requirements are specific and verifiable

If validation fails, fix, then re-commit and revalidate before completion.

7. Completing step:

```bash
flow telemetry emit --feature "<feature-slug>" --event spec_completed || true
```

## Output Contract

Report the following after completion:
- Feature id/slug
- Absolute path to `spec.md`
- Mission selected
- `complexity_level` and `implementation_path`
- Next step:
  - `direct` → instruct user to run `/flow.build` (in chat)
  - `full` → `/flow.plan`

## Error Handling

- If creation command fails or JSON is invalid, report exact failure and stop.
- Never proceed with unresolved critical ambiguities.

---

## ⛔ MANDATORY STOP POINT

**This command is COMPLETE after the Direct Path or full path step resolves.**

**YOU MUST STOP HERE.**

Do NOT:
- ❌ Run `/flow.plan` or generate `plan.md`
- ❌ Generate work packages (for `direct` path, only WP01 exists — already created)
- ❌ Create any implementation code
- ❌ Proceed to any implementation phase
- ❌ Create worktrees or workspaces manually
- ❌ Create more than 1 work package for direct path features
- ❌ Run /flow.plan for direct path features

For `full` path: The user will run `/flow.plan` when they are ready.
For `direct` path: The user will run `/flow.build` when they are ready.

**Next suggested command**:
- `direct` path → `/flow.build` (run in chat)
- `full` path → `/flow.plan`
