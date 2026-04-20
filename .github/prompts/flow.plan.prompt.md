---
description: Executar planejamento de implementação e gerar artefatos de design.
---

## ⚠️ Verificar versão do flow antes de tudo

```bash
flow -v
flow --help
```

Este prompt foi escrito para o workflow v1.x (3-prompt). Se a versão instalada for 0.x, consulte `.agents/skills/flow-fundamentals/references/flow-v0.x.md` antes de prosseguir.

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

# /flow.plan - Create Implementation Plan

{INCLUDE: shared/user-input.md}

## Goal

Generate planning artifacts (`plan.md` and related design docs) from a clarified specification.

## Location

Run from the planning repository root (main repo). Do not run from WP worktrees.

## Mode Selection

### Plan-Only Mode (--plan-only flag)

If `$ARGUMENTS` contains `--plan-only`:
1. Strip `--plan-only` from arguments.
2. Run ONLY the planning phase (generate plan.md + design artifacts).
3. Do NOT generate tasks or WP prompts.
4. After completion, instruct user to re-run `/flow.plan` without `--plan-only` when ready to generate tasks.

### Default Mode: Plan + Tasks (combined)

By default, `/flow.plan` generates BOTH plan.md AND tasks.md + WP prompts in a single session. This reduces message overhead by combining two discovery phases into one.

## Discovery Loop (mandatory)

### Inherited Context

Before asking questions, load `meta.json` from FEATURE_DIR and check:
- `complexity_level`: calibrate question depth.
- `discovery_mode`: if `"express"` or `"external"`, skip discovery entirely — proceed using spec.md as sole context. If `"external"`, also load the original source document from `discovery_source.path` in meta.json as supplementary planning context.

### Question Delivery: Batch Mode

**IMPORTANT: Do NOT ask questions one at a time.** Present ALL questions (planning + task-slicing) in a **SINGLE message** as a numbered list.

Combined confidence requires clarity on:
- architecture direction
- key technical constraints
- integration boundaries
- testing/validation expectations
- unresolved decisions and risk ownership
- intended scope for this iteration (MVP vs full)
- sequencing constraints and dependency-critical components

Scale the NUMBER of questions in the batch by `complexity_level` from meta.json (fall back to local assessment if missing):
- trivial/test: 0-2 questions
- simple: 2-3 questions
- complex: 4-6 questions
- critical: 6+ questions

User intent signals:
- If user says "use defaults", "keep it simple", "just testing", or equivalent, skip batch and proceed with sensible defaults.

### Follow-up Protocol

After the user responds:
- If confidence is sufficient for BOTH planning and task decomposition: proceed.
- If not: ask up to 2 follow-up questions in ONE additional message.
- Maximum: 2 message exchanges for ALL discovery (planning + tasks combined).

Rules:
- If confidence is not sufficient after batch, ask follow-up and end with `WAITING_FOR_PLANNING_INPUT`.
- Keep an internal planning notes table; do not render it.
- Summarize engineering alignment AND decomposition strategy, get confirmation before generation.

## Feature Detection (explicit)

Detect feature slug before setup:
1. Branch context (`###-slug` or `###-slug-WP##` -> strip `-WP##`).
2. Current path context (if inside `flow-specs/<feature>/`).
3. If still ambiguous, ask user and do not guess.

Always pass explicit `--feature <feature-slug>`.

## Execution Steps

1. Run setup command with explicit feature slug:

```bash
flow agent feature setup-plan --feature <feature-slug> --json
```

2. Parse JSON and capture:
- `plan_file` (absolute)
- `feature_dir` (absolute)

3. Load context:
- required: `spec.md`
- optional: `.flowflow/memory/constitution.md`
- optional: `meta.json` — if `prd_url`, `prd_id`, or `prd_content` fields exist, use the PRD as **primary requirements context** alongside spec.md. The "Source PRD" section in spec.md is the canonical reference; planning decisions must align with the PRD requirements stated there.

**PRD alignment rule**: When a PRD exists in spec.md, all plan artifacts (component names, module names, section headings, architecture decisions) must use the PRD's terminology and domain language. The plan must be traceable back to the PRD — a reader should be able to map every plan section to its originating PRD requirement.

4. **Project Conventions Auto-Detection** (mandatory):

Scan the repository to detect the actual tooling and conventions in use. Do NOT assume defaults — detect from files present in the repo.

**Detection rules** (check in order, stop at first match per category):

| Category | Files to check | What to extract |
|----------|---------------|-----------------|
| **Language/Runtime** | File extensions in `src/`, `lib/`, `app/`; `pyproject.toml`, `package.json`, `Cargo.toml`, `go.mod`, `*.csproj`, `Gemfile`, `composer.json`, `build.gradle`, `pom.xml` | Primary and secondary languages |
| **Package manager** | `uv.lock` → uv; `poetry.lock` → poetry; `Pipfile.lock` → pipenv; `requirements.txt` → pip; `pnpm-lock.yaml` → pnpm; `yarn.lock` → yarn; `bun.lockb` → bun; `package-lock.json` → npm; `Cargo.lock` → cargo; `go.sum` → go mod; `Gemfile.lock` → bundler; `composer.lock` → composer | Package manager + lockfile |
| **Linter** | `ruff.toml` or `[tool.ruff]` in pyproject.toml → ruff; `.flake8`/`[flake8]` → flake8; `.pylintrc` → pylint; `.eslintrc*`/`eslint.config.*` → eslint; `biome.json` → biome; `.golangci.yml` → golangci-lint; `clippy.toml` → clippy; `.rubocop.yml` → rubocop; `phpstan.neon` → phpstan | Linter + config location |
| **Formatter** | `[tool.ruff.format]` in pyproject.toml or `ruff.toml` → ruff format; `[tool.black]`/`pyproject.toml` with black → black; `.prettierrc*` → prettier; `biome.json` → biome; `rustfmt.toml` → rustfmt; `.editorconfig` → editorconfig | Formatter + config location |
| **Test runner** | `[tool.pytest]`/`pytest.ini`/`conftest.py` → pytest; `jest.config.*` → jest; `vitest.config.*` → vitest; `.mocharc.*` → mocha; `karma.conf.*` → karma; `phpunit.xml` → phpunit; `_test.go` files → go test; `*_spec.rb` → rspec; `*_test.rb` → minitest | Test runner + config |
| **Pre-commit hooks** | `.pre-commit-config.yaml` → parse repos and hooks list | All configured hooks |
| **CI/CD** | `.github/workflows/*.yml`, `.gitlab-ci.yml`, `Jenkinsfile`, `.circleci/config.yml`, `bitbucket-pipelines.yml` | Lint/test/build commands used in CI |
| **Build system** | `Makefile`, `Taskfile.yml`, `justfile`, `Rakefile`, `nx.json`, `turbo.json` | Task runner + common targets |
| **Monorepo** | `nx.json`, `turbo.json`, `pnpm-workspace.yaml`, `lerna.json`, `Cargo.toml [workspace]` | Monorepo tool + workspace structure |

**How to detect**: Use `ls`, `cat`, and `grep` on the repo root. Keep it fast — only check files that exist, do not recurse deeply.

**Output**: Include a `## Project Conventions` section at the TOP of `plan.md` with the detected conventions. Format:

```markdown
## Project Conventions

> Auto-detected from repository. All work packages MUST follow these conventions.

- **Language(s)**: Python 3.11+
- **Package manager**: uv (pyproject.toml)
- **Linter**: ruff (`ruff.toml`)
- **Formatter**: ruff format
- **Test runner**: pytest (`conftest.py` at root)
- **Pre-commit hooks**: ruff check, ruff format, pytest (from `.pre-commit-config.yaml`)
- **CI**: GitHub Actions — runs `uv run ruff check`, `uv run pytest` (from `.github/workflows/ci.yml`)
- **Build system**: Makefile (targets: lint, test, build)
```

Only include categories where something was detected. If nothing is found for a category, omit it.

**Critical**: This section acts as a constraint for all downstream WP prompts. When the plan says "Linter: ruff", no WP should use flake8. When it says "Package manager: uv", no WP should use pip install.

5. Produce artifacts:
- `plan.md` (mandatory) — must include the `## Project Conventions` section from step 4
- additional artifacts as needed by scope:
- `research.md` (if unknowns require research)
- `data-model.md` (if entities/data are involved)
- `contracts/*` (if API/interface work exists)
- `quickstart.md` (validation scenarios)

6. Clarification gate:
- Resolve blocking architectural ambiguities before completion.
- Deferred decisions are allowed only if user explicitly defers; record assumptions, owners, and risks.

7. Constitution check:
- If constitution exists, validate alignment and surface conflicts.
- If absent, note it and continue.
- Re-check after design artifacts are drafted.

8. Update agent context after artifacts are drafted:

```bash
flow agent context update-context --feature <feature-slug>
```

9. Completing step:

```bash
flow telemetry emit --feature "<feature-slug>" --event plan_completed || true
```

10. Stop after planning artifacts are complete.

## Task Generation Phase (default mode only)

**Skip this entire section if `--plan-only` was specified.** If plan-only, jump to Output Contract below.

### ⚠️ CRITICAL: Subagent Delegation

After plan.md and design artifacts are written, **you MUST delegate WP generation to a subagent**. Do NOT generate WPs yourself in this session — this prevents context exhaustion that causes truncated or missing WPs.

**Use the Agent tool** to spawn a subagent with the following prompt. Replace `<feature-slug>` and `<FEATURE_DIR>` with the actual values:

```
You are generating work packages for feature "<feature-slug>".

## Step 1: Load Context

Read these files in order:
1. `<FEATURE_DIR>/spec.md` (required — the feature specification)
2. `<FEATURE_DIR>/plan.md` (required — the implementation plan with project conventions)
3. `<FEATURE_DIR>/meta.json` (optional — check for prd_url, prd_id, prd_content, complexity_level)
4. `.flowflow/memory/constitution.md` (optional — project standards)
5. Any additional artifacts: `<FEATURE_DIR>/research.md`, `<FEATURE_DIR>/data-model.md`, `<FEATURE_DIR>/contracts/`, `<FEATURE_DIR>/quickstart.md`

## Step 2: Run Prerequisites

```bash
flow agent feature check-prerequisites --json --paths-only --include-tasks
```

Parse JSON and use absolute FEATURE_DIR for every file operation.

## Step 3: PRD Alignment

If spec.md contains a "Source PRD" section or meta.json has prd_url/prd_id/prd_content:
- WP titles, subtask titles, and slugs MUST use the PRD's terminology. Do not rename PRD concepts.
- Each WP should map to one or more PRD requirements. Include a prd_requirements note in each WP prompt body.
- The PRD is the single source of truth for scope.

## Step 4: Build Subtask Inventory

Build internal subtask inventory (T001...) and dependencies.

## Step 5: Roll Up Into Work Packages

Use strict sizing rules:
- target: 3-7 subtasks per WP
- hard max: 10 subtasks per WP
- if a WP exceeds max or is too large, split it — 20 focused WPs are better than 5 overwhelming ones
- target prompt size per WP file: 200-500 lines
- hard max prompt size per WP file: 700 lines
- if prompt size is projected over 700 lines, split before writing files

Split by phase, component, user story, or work type. Merge only when WPs are <3 subtasks, address the same concern, and have no natural parallelization opportunity.

Sizing self-check before writing:
- ≤7 subtasks and ≤500 lines → good
- 8-10 subtasks or 500-700 lines → consider splitting
- >10 subtasks or >700 lines → must split

## Step 6: Write tasks.md

Write FEATURE_DIR/tasks.md using the bundled template contract:
- base structure from src/cli_flow/missions/software-dev/.flowflow/templates/tasks-template.md
- keep checklist-oriented WP sections and dependency visibility
- do not invent an incompatible format

## Step 7: Generate ALL WP Prompt Files

Generate per-WP prompt files in a flat directory only:
- FEATURE_DIR/tasks/WPxx-<slug>.md
- include frontmatter with at least: work_package_id, title, lane: "planned", subtasks, dependencies
- PRD reference: If spec.md contains a "Source PRD" section, include a prd_ref field in frontmatter and add a "## Source PRD" section at the top of each WP prompt body.
- multi-repo workspaces: When .flowflow/workspace.yaml exists, each WP MUST include a repo: field matching a key from workspace.yaml.
- optionally include deploy_group and deploy_mode for simultaneous-release governance
- do not create lane subdirectories — lane is tracked in frontmatter only

### Deploy Groups (Optional)

When WPs must be deployed atomically as a group, annotate them with deploy governance fields.

Frontmatter format (in tasks/WPxx-<slug>.md):
work_package_id, title, lane: "planned", dependencies, prd_ref, repo, deploy_group, deploy_mode, subtasks

Defaults (backward compatible):
- Omit both fields → deploy_mode: independent → WP releases as soon as its own implementation completes.

## Step 8: Prompt Detail Standard — THIS IS THE MOST CONSEQUENTIAL STEP

Each subtask must include: objective, concrete file/work guidance, validation checklist, and edge cases for high-risk paths. Thin prompts cause implementation failures and rework. Aim for 30-70 lines per subtask.

Too thin (causes agent confusion):
  T001: Add user authentication
  Steps: Create endpoint, add validation, test it.

Sufficient (enables correct first-pass implementation):
  T001: Implement POST /api/auth/login
  Purpose: Validate credentials and return a JWT token.
  Steps:
  1. Create handler in src/api/auth.py — route, request shape {email, password},
     response {token, user}, error codes 400/401/429.
  2. Hash comparison with bcrypt; use constant-time compare.
  3. Sign JWT with SECRET_KEY (HS256, 24h expiry).
  4. Rate-limit: 5 attempts/IP/15 min → 429 with Retry-After.
  Files: src/api/auth.py (~80 lines), tests/api/test_auth.py (~120 lines).
  Validation: [ ] 200+token on valid creds [ ] 401 on bad creds [ ] 429 after 5 attempts
  Edge cases: nonexistent account → 401 (same as wrong password); empty password → 400.

Invest the tokens here. Thorough prompts pay back 10x during implementation.

## Step 9: Dependency Detection

- parse/detect dependencies from tasks.md sequencing and explicit dependency statements
- include dependency-aware implement command guidance in WP prompts:
  - no dependencies: flow build WP01
  - with dependencies: flow build WP02 --base WP01

## Step 10: Finalize

```bash
flow agent feature finalize-tasks --json
```

Parse finalization output. Artifacts are validated but NOT committed — they will be committed on the implementation branch by `/flow.build`.

## CRITICAL COMPLETENESS CHECK

Before finalizing, verify:
- [ ] tasks.md exists and lists ALL WPs
- [ ] Every WP listed in tasks.md has a matching file in FEATURE_DIR/tasks/
- [ ] Every WP file has complete frontmatter (work_package_id, title, lane, subtasks, dependencies)
- [ ] Every subtask has objective, steps, files, validation checklist (30-70 lines each)
- [ ] No WP file is truncated or incomplete

Report: tasks.md path, WP count, subtask distribution, dependency highlights, finalization result.
```

**After spawning the subagent**, wait for it to complete and then report the combined results.

## Output Contract

### Default mode (plan + tasks):

Report:
- absolute `plan.md` path + design artifact paths
- absolute `tasks.md` path
- WP count and subtask distribution
- dependency highlights
- size validation summary (flag WPs over max)
- finalization result
- next command suggestion: `/flow.build`

### Plan-only mode:

Report absolute paths for generated plan artifacts and unresolved deferred decisions.
- Instruct user to re-run `/flow.plan` without `--plan-only` when ready to generate tasks.

## Error Handling

- If setup command fails or feature cannot be identified, report exact failure and stop.
- If prerequisite or finalize command fails, report exact failure and stop.
- Do not continue with blocking architectural ambiguity.
- Do not proceed when required docs are missing.
