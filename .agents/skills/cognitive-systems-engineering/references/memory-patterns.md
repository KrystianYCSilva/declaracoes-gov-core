---
name: memory-patterns
description: |
  Reference for concrete memory implementation patterns in cognitive systems engineering.
  Use when: choosing between short-term, episodic, semantic, and procedural memory structures,
  or mapping them into software components.
---

# Memory Patterns and Implementation

## Working Memory

Use for the active context window and local task state.
Typical implementation: bounded window, rolling summary, or local task board.

## Episodic Memory

Use for prior events, outcomes, and session continuity.
Typical implementation: event log, retrieved summaries, or vector-backed episode recall.

## Semantic Memory

Use for relatively stable facts, policies, and domain rules.
Typical implementation: versioned documentation, structured policy files, or durable knowledge stores.

## Procedural Memory

Use for "how to" behavior.
Typical implementation: skills, tools, scripts, and workflow contracts.

## Engineering Questions

1. which tier is authoritative?
2. which tier is best-effort retrieval?
3. who writes each tier?
4. how is stale content corrected?

## Practical Guardrail

If a workflow transition depends on the information, keep it in structured state rather than retrieval-only memory.
