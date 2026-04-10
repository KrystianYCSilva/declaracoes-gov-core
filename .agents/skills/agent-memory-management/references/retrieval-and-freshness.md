---
description: |
  Reference for retrieval quality, freshness, and token-budget failure modes in agent memory systems.
  Use when: diagnosing poor recall, stale memory, ranking problems, or context-window overload.
---

# Retrieval and Freshness

## Common Failure Modes

- stale facts outrank current ones
- relevant items are buried in long context
- duplicate or conflicting memories confuse the agent
- retrieval returns broad but shallow context

## Control Levers

1. timestamps and recency weighting
2. authority weighting
3. deduplication
4. narrower retrieval scopes
5. post-retrieval summarization

## When to Prefer Structured State Over Retrieval

Prefer explicit state when:

- the information is authoritative
- exactness matters
- conflicts must be resolved deterministically
- the data drives workflow transitions or governance
