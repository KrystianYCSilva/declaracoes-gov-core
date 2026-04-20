---
name: flow-fundamentals
description: |
  Operate the flow SDD CLI through its public human-facing workflow: feature planning in `flow-specs/`, isolated WP worktrees, agent-safe lane transitions, autonomous `flow build --auto`, and resumable `flow merge`.
  Use when: initializing a flow project, implementing or reviewing work packages, resuming orchestration, merging a feature, or coordinating a multi-repo workspace.
---

# Flow Fundamentals

## Step 0 — Verify Version Before Anything Else

**Always run this before giving any flow guidance or invoking any flow command:**

```bash
flow -v
flow --help
```

Then route to the correct reference:

| Condition | Load |
|-----------|------|
| `flow -v` reports `0.x` | `references/flow-v0.x.md` |
| `flow -v` reports `1.x` **and** `.github/prompts/flow.*.prompt.md` files exist | `references/flow-v1.x.md` |
| `flow -v` reports `1.x` but `.github/prompts/flow.*.prompt.md` are absent | `references/flow-v0.x.md` |

Do not assume a version. Do not skip this check. The CLI surface, branch model, and agent delegation pattern differ between versions.

## How to Treat Flow as an External Prompt and Agent Pack

`flow` is an external project with its own prompts, agent instructions, and runtime mirrors.
Do not pretend that Itzamna core templates own or regenerate that Flow prompt pack.

When `itzamna init` or `itzamna update` detects Flow in the repository, it installs `.itzamna/protocols/flow-prompt-agent-hardening.md`.
Load that protocol before editing Flow-owned prompt or agent files such as `.github/prompts/flow.*`, Flow-specific entries under `.github/agents/`, or runtime mirror files under `.flowflow/`.

Use the protocol to harden the existing Flow surfaces in place while preserving repository-local notes that do not conflict with the verified public CLI contract.

## Invariant Rules (apply to all versions)

These rules hold regardless of version. Violating them corrupts state:

- Do not call `flow orchestrate` — deprecated; use `flow build --auto`
- Do not invent unsupported agent IDs in runtime guidance
- Do not treat `copilot-cli` as a runtime orchestration agent
- WP lane lives in frontmatter only (`planned` → `doing` → `for_review` → `done`)
- Do not move WP files between folders; update the `lane` frontmatter field through `flow agent tasks move-task`
- Do not edit WP status in a worktree copy of `flow-specs/`; the planning-branch copy is authoritative
- Merge is always a separate step after implementation — do not collapse it into the WP review loop
- Use `build-direct` and `test-direct` only inside intentionally non-worktree cycles

## Reference Index

| File | When to load |
|------|-------------|
| `references/flow-v0.x.md` | v0.x CLI-native workflow (direct commands: `flow agent workflow`, `flow build --auto`, `flow merge`, etc.) |
| `references/flow-v1.x.md` | v1.x 3-prompt workflow (`/flow.refine`, `/flow.plan`, `/flow.build` as primary interface) |
| `references/flow-state-machines.md` | Orchestration and merge state machines — load when debugging stuck WPs, state transitions, or merge persistence |
