---
description: |
  Flow v0.x — CLI-native workflow reference.
  Applies to: flow versions 0.x, or any environment where the 3-prompt pack (.github/prompts/flow.*.prompt.md) is NOT present.
  Load when: `flow -v` reports a 0.x version, OR when operating without the .github/prompts wrapper.
---

# Flow v0.x — CLI-Native Workflow Reference

> ⚠️ **Verify your version first — before following this guide:**
> ```bash
> flow -v
> flow --help
> flow agent --help
> flow agent workflow --help
> flow agent tasks --help
> ```
> If your version starts with `1.` **and** `.github/prompts/flow.*.prompt.md` files exist, load `flow-v1.x.md` instead.

## Operating Model

In v0.x (or when operating without the `.github/prompts/` wrapper), agents and humans interact with flow by calling CLI commands directly. There is no prompt abstraction layer — you drive the full lifecycle through `flow` subcommands.

```
flow init  →  flow agent workflow implement  →  flow agent tasks  →  flow merge
```

## Project Bootstrap

```bash
flow init my-project
flow init --here
flow init --non-interactive --ai claude,codex
```

Key flags:
- `--ai <agents>`: valid values: `claude`, `codex`, `copilot`, `copilot-cli`, `gemini`, `opencode`
- `--script sh|ps`: helper script style
- `--non-interactive` / `--yes`: skip prompts for headless runs
- `--here`: initialize current directory

Creates runtime scaffolding under `.flowflow/` and artifact space under `flow-specs/`.

## Work Package Workflow (preferred agent path)

Use the workflow wrapper — it creates or reuses the workspace, moves the WP lane safely, and prints the exact next commands.

```bash
flow agent tasks list-tasks
flow agent tasks list-tasks --lane planned
flow agent workflow implement WP01 --agent claude
flow agent workflow implement --agent gemini
```

What this path does:
1. Finds the WP in `flow-specs/<feature>/tasks/`
2. Creates or reuses `.worktrees/<feature>-WP##/`
3. Moves the WP from `planned` to `doing`
4. Prints the implementation prompt and safe completion steps

When implementation is done:

```bash
cd .worktrees/010-example-WP01
git add <implementation-files>
git commit -m "feat(WP01): implement scope"
flow agent tasks mark-status T001 T002 --status done
flow agent tasks move-task WP01 --to for_review --note "Ready for review"
```

If blocked:

```bash
flow agent tasks add-history WP01 --note "Blocked: <reason>"
```

## Review a Work Package

```bash
flow agent workflow review WP01 --agent codex
flow agent workflow review --agent gemini
```

Approve or request changes:

```bash
flow agent tasks move-task WP01 --to done --note "Review passed"
flow agent tasks move-task WP01 --to planned --review-feedback-file feedback.md
```

Lane rules:
- `for_review → done` = approved
- `for_review → planned` = changes requested
- Review does NOT move a WP back to `doing`

## Manual Worktree Creation

Use when you need workspace creation only (no workflow prompt wrapper):

```bash
flow build WP01
flow build WP02 --base WP01
flow build WP06 --force
flow build WP01 --feature 010-example
flow build WP01 --json
```

## Autonomous Orchestration

```bash
flow build --auto
flow build --auto --feature 020-payments
flow build --status
flow build --resume
flow build --pause
flow build --abort --cleanup
flow build --skip WP03
```

Override agents intentionally:

```bash
flow build --auto --impl-agent claude --review-agent codex
```

Runtime agent IDs: `claude-code`, `codex`, `copilot`, `gemini`, `opencode`
(`claude` is alias for `claude-code`)

## Merge

Run after WPs are approved. Merge is a separate step — do not collapse it into orchestration.

```bash
flow merge --feature 010-example
flow merge --feature 010-example --target main
flow merge --dry-run --feature 010-example
flow merge --resume
flow merge --abort
```

Strategies: `merge` (default), `squash`, `rebase` (manual intervention may be required with worktrees).

## Context Commands

```bash
flow context info
flow context list
flow context cleanup --dry-run

# Rewrite agent context from plan.md:
flow agent context update-context --agent-type claude
flow agent context update-context --feature 020-payments --agent-type gemini
```

## Multi-Repo Workspace

```bash
flow workspace
flow workspace list
flow workspace add ../shared-lib
flow workspace remove shared-lib
```

Creates `.flowflow/workspace.yaml`, `.flowflow/config.yaml`, and `flow-specs/` at workspace root.

## Worktree Model

```text
repo-root/
├── flow-specs/010-example/tasks/WP01-*.md   # authoritative WP state (planning branch)
└── .worktrees/010-example-WP01/             # implementation workspace (feature branch)
```

- Implementation code belongs in the worktree branch.
- Task state and lane changes belong in the planning branch.
- Do not treat the worktree copy of `flow-specs/` as authoritative.

## Lane Model

WP lanes live in frontmatter only. Valid values: `planned` → `doing` → `for_review` → `done`

- Never move WP files between folders; change the frontmatter field instead.
- Use `flow agent tasks move-task` for lane transitions.

## Do Not Use

- `flow orchestrate` — deprecated; use `flow build --auto`
- Internal or non-public entrypoints
- `copilot-cli` as a runtime orchestration agent
- `build-direct` / `test-direct` outside intentionally non-worktree cycles
