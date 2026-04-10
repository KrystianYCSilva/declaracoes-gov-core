---
description: |
  Reference for shared versus private memory in multi-agent systems.
  Use when: multiple agents need coordination, ownership boundaries, or explicit handoff artifacts.
---

# Multi-Agent Memory

## Shared Truth

Reserve shared memory for:

- authoritative workflow state
- approved planning artifacts
- durable continuity items that multiple roles need

## Private Working Context

Keep private:

- local scratch reasoning
- transient draft decisions
- role-specific analysis not yet accepted into shared truth

## Handoff Artifacts

Use explicit handoffs when ownership changes.
Good handoffs explain:

1. current goal
2. current safe state
3. next legal action
4. unresolved risks
