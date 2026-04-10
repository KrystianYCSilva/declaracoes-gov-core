---
name: warnings
description: |
  Warning reference for failure modes in engineered cognitive systems.
  Use when: reviewing autonomous loops, diagnosing drift, or checking whether a multi-agent design is safe enough to ship.
---

# Warnings and Common Pitfalls

## Infinite Loops

- Error: the agent retries the same failed action without learning.
- Fix: cap retries and escalate after a small bounded number of attempts.

## Context Pollution

- Error: working memory is flooded with too much retrieved material.
- Fix: narrow retrieval, re-rank results, and keep top-k small.

## Hallucination of Capability

- Error: the agent claims it can do something it has no tool or authority to do.
- Fix: ground actions in explicit tool contracts and permission boundaries.

## Memory Drift

- Error: long-term memory accumulates conflicting or stale information.
- Fix: add consolidation, freshness checks, and ownership rules.

## Shared-State Ambiguity

- Error: multiple agents can update the same durable state with no ownership rule.
- Fix: introduce explicit write ownership and handoff artifacts before scaling the workflow.

## No Closure Phase

- Error: the system treats "task complete" as equivalent to "state is consistent again".
- Fix: require an explicit closure or reanchor phase that updates durable truth and resets the workflow safely.
