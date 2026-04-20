---
description: |
  Detailed state machine definitions for flow orchestration and merge subsystems.
  Use when: debugging state transitions, building automation around flow states, or diagnosing stuck WPs.
---

# Flow State Machines — Detailed Reference

## Orchestration State Machine

### OrchestrationStatus (run-level)

| State | Description | Transitions To |
|-------|-------------|----------------|
| `pending` | Run created, not started | `running` |
| `running` | Actively processing WPs | `paused`, `completed`, `failed` |
| `paused` | Waiting for manual intervention | `running` (via `--resume`) |
| `completed` | All WPs reached terminal state | — (terminal) |
| `failed` | Unrecoverable error | — (terminal) |

### WPStatus (work-package-level)

| State | Description | Transitions To |
|-------|-------------|----------------|
| `pending` | Initial state, dependencies not checked | `ready` |
| `ready` | Dependencies satisfied, can be picked up | `implementation` |
| `implementation` | Agent is working on the WP | `review`, `failed` |
| `review` | Implementation done, awaiting review | `completed`, `rework` |
| `rework` | Review rejected, needs changes | `implementation` |
| `completed` | Review approved | — (terminal) |
| `failed` | Max retries exceeded | — (terminal) |

### Rework Loop

The `review → rework → implementation → review` cycle repeats up to the configured max retries. After exhausting retries, the WP transitions to `failed`. The orchestrator then applies the configured `FallbackStrategy`:

- `next_in_list` — Reassign to next available agent (resets retry counter)
- `same_agent` — Retry with same agent (does not reset)
- `fail` — Mark WP as permanently failed, continue with remaining WPs

## Merge State Machine

### Merge Lifecycle

```
NO_STATE → IN_PROGRESS → [CONFLICT] → RESOLVED → COMPLETED
                                     ↘ ABORTED
```

### MergeState Fields

| Field | Type | Description |
|-------|------|-------------|
| `feature_slug` | string | Feature identifier (e.g., `010-auth-refactor`) |
| `target_branch` | string | Merge target (e.g., `main`, `develop`) |
| `wp_order` | string[] | Ordered WP IDs to merge |
| `completed_wps` | string[] | Successfully merged WPs |
| `current_wp` | string\|null | WP being merged right now |
| `has_pending_conflicts` | bool | Unresolved git conflicts exist |
| `strategy` | string | `merge`, `squash`, or `rebase` |
| `started_at` | ISO string | When merge began |
| `updated_at` | ISO string | Last state change |

### Conflict Resolution Flow

1. `flow merge --feature 010` starts sequential WP merge
2. Conflict detected on WP03 → state saved with `has_pending_conflicts: true`
3. Developer resolves conflicts manually in the worktree
4. `flow merge --resume` picks up from WP03, validates resolution, continues to WP04+
5. All WPs merged → state cleared

### State File Location

- **Merge state**: `.flowflow/merge-state.json`
- **Orchestration state**: `.flowflow/orchestration-state.json`
- **Orchestration backup**: `.flowflow/orchestration-state.json.bak`

Both are JSON files safe to inspect manually. Do not edit while orchestration/merge is running.
