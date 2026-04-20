---
description: |
  Flow v1.x — simplified 3-prompt workflow reference.
  Applies to: flow versions 1.x where `.github/prompts/flow.*.prompt.md` are present.
  Load when: `flow -v` reports a 1.x version AND the 3-prompt pack exists in the repo.
---

# Flow v1.x — Simplified 3-Prompt Workflow Reference

> ⚠️ **Verify your version and available surfaces before following this guide:**
> ```bash
> flow -v
> flow --help
> ```
> Then confirm `.github/prompts/flow.refine.prompt.md`, `flow.plan.prompt.md`, and `flow.build.prompt.md` exist.
> If they are absent or `flow -v` reports 0.x, load `flow-v0.x.md` instead.

## Operating Model

In v1.x, the primary human-facing interface is **three prompts**. Agents and users interact with the workflow lifecycle through prompt invocations rather than raw CLI commands. The prompts internally call the same `flow agent *` CLI commands, but that detail is hidden from the caller.

```
/flow.refine  →  /flow.plan  →  /flow.build
```

| Prompt | Role | Primary Output |
|--------|------|----------------|
| `/flow.refine` | Speccing — turns a natural-language description or external document into a structured spec | `flow-specs/<feature>/spec.md` |
| `/flow.plan` | Planning — generates implementation plan, tasks breakdown, and WP prompt files | `plan.md`, `tasks.md`, `tasks/WP##-*.md` |
| `/flow.build` | Orchestration — drives WP implementation through subagents on a single feature branch | Committed code + updated WP lanes |

## Prompt Files Location

```
.github/prompts/
├── flow.refine.prompt.md
├── flow.plan.prompt.md
└── flow.build.prompt.md
```

These are GitHub Copilot slash-command prompts. They are **part of the flow prompt pack** — do not edit them in isolation without reviewing the cross-prompt contract (see below).

## Typical Session Flow

### 1. Specify a feature

From natural language:
```
/flow.refine My feature description here
```

From an external document (diagnosis, RFC, ADR, etc.):
```
/flow.refine ~/path/to/diagnosis.md
```

Output: `flow-specs/<feature>/spec.md` + `meta.json`.

### 2. Generate the implementation plan

```
/flow.plan
```

Use `--plan-only` to generate only `plan.md` (skip task/WP generation):
```
/flow.plan --plan-only
```

Output: `plan.md`, `tasks.md`, `tasks/WP##-*.md`.

### 3. Implement

```
/flow.build 002
```

Pass the feature number. The build prompt:
1. Resolves the feature slug from `flow-specs/`
2. Creates/switches to the implementation branch (named after the slug)
3. Commits planning artifacts
4. Iterates all WPs, delegating each to a `@flow-wp-implementer` subagent
5. Updates WP lanes as work progresses

Output: committed code on the feature branch with all WPs moved to `done`.

## Planning Artifacts Layout

```
flow-specs/
└── 002-my-feature/
    ├── meta.json         ← feature metadata, discovery mode, complexity level
    ├── spec.md           ← from /flow.refine
    ├── plan.md           ← from /flow.plan (includes Project Conventions section)
    ├── tasks.md          ← WP overview and dependency graph
    └── tasks/
        ├── WP01-*.md     ← per-WP implementation prompts
        └── WP02-*.md
```

## Cross-Prompt Contract (invariants — do NOT break)

The three prompts share these assumptions. Breaking any of them will corrupt the workflow:

| Invariant | Detail |
|-----------|--------|
| **Artifact paths** | `flow-specs/<feature>/spec.md`, `plan.md`, `tasks.md`, `tasks/WP##-*.md` — exact names and locations |
| **meta.json fields** | `target_branch`, `complexity_level`, `discovery_mode`, `implementation_path`, `prd_url/prd_id/prd_content` |
| **Handoff: refine → plan** | `plan` reads `spec.md` + `meta.json` produced by `refine`; if `spec.md` is absent, `plan` will fail |
| **Handoff: plan → build** | `build` reads `tasks.md` + `tasks/WP##-*.md` produced by `plan`; must exist before orchestration starts |
| **Lane model** | WP lanes in frontmatter: `planned` → `doing` → `for_review` → `done`; build updates these via `flow agent tasks move-task` |
| **Implementation path** | `meta.json#implementation_path`: `"full"` (all WPs) or `"direct"` (WP01 only); build checks this before iterating |

**When editing any flow prompt**: read all three prompts first and verify the change does not alter shared artifact names, `meta.json` field expectations, or lane transitions.

## Discovery Modes in `/flow.refine`

| Mode | Trigger | Behavior |
|------|---------|---------|
| `interactive` | No structured input detected | Asks discovery questions in batches |
| `external` | File path or structured document in `$ARGUMENTS` | Maps document to spec, skips discovery loop; generates spec + plan + tasks in one session |
| `express` | `meta.json` has `discovery_mode: "express"` | Skips questions entirely, proceeds with defaults |

## Complexity-Based Question Scaling

Both `/flow.refine` and `/flow.plan` scale their discovery question count by `complexity_level` in `meta.json`:
- `trivial` / `test`: 0–2 questions
- `simple`: 2–3 questions
- `complex`: 4–6 questions
- `critical`: 6+ questions

## `/flow.plan` Modes

| Mode | Command | Behavior |
|------|---------|---------|
| Default (plan + tasks) | `/flow.plan` | Generates `plan.md` **and** `tasks.md` + WP files in one session |
| Plan-only | `/flow.plan --plan-only` | Only generates `plan.md` and design artifacts; task generation deferred |

## WP Sizing Rules (enforced by `/flow.plan`)

- Target: 3–7 subtasks per WP; 200–500 lines per WP file
- Hard max: 10 subtasks; 700 lines — must split if exceeded
- Subtask prompt standard: 30–70 lines each (objective + concrete steps + files + validation checklist + edge cases)

## `/flow.build` Implementation Paths

`/flow.build` checks `meta.json#implementation_path`:
- `"full"` (default or absent): iterate all WPs in order, delegate each to a subagent
- `"direct"`: handle WP01 only (single-subagent path), skip full iteration

## Merge (same CLI as v0.x — unchanged)

After `/flow.build` completes, merge is a separate step:

```bash
flow merge --feature 010-example
flow merge --feature 010-example --target main
flow merge --dry-run --feature 010-example
flow merge --resume    # after manual conflict resolution
flow merge --abort
```

## What the Prompts Call Internally

The prompts orchestrate these CLI commands — agents do NOT call them directly when using the 3-prompt workflow:

| Prompt | Key CLI calls |
|--------|--------------|
| `/flow.refine` | `flow agent feature create-feature` |
| `/flow.plan` | `flow agent feature setup-plan`, `flow agent feature check-prerequisites`, `flow agent feature finalize-tasks`, `flow telemetry emit` |
| `/flow.build` | `flow agent tasks move-task`, `flow agent context update-context`, `git checkout`, `git commit` |

## Key Differences vs v0.x CLI-Native Mode

| Aspect | v0.x CLI-native | v1.x 3-prompt |
|--------|----------------|---------------|
| Entry points | Direct `flow` commands | `/flow.refine`, `/flow.plan`, `/flow.build` |
| Branch model | Worktrees per WP (`.worktrees/<feature>-WP##/`) | Single feature branch for all WPs |
| Spec creation | Manual or via `flow agent feature create-feature` | Automated via `/flow.refine` |
| WP generation | Manual or via `flow agent feature finalize-tasks` | Automated via `/flow.plan` subagent delegation |
| Agent delegation | `flow agent workflow implement/review` | `@flow-wp-implementer` via prompt subagent |
| Merge | `flow merge` (same) | `flow merge` (same) |

> The v1.x single-branch model is intentional — it simplifies conflict resolution when WPs are tightly coupled.
> If you need true parallel isolation via worktrees, use the v0.x CLI-native path instead.
