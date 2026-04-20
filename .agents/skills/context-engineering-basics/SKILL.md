---
name: context-engineering-basics
description: |
  Guide context engineering with metadata, JIT loading, compression, and multi-agent boundary control.
  Use when: designing AI context structures, reducing token waste, or deciding what information should be loaded for a task.
activation: Auto
estimated_tokens: 550
---

# Context Engineering Basics

Context engineering is the discipline of giving the model the right information at the right time, not all information all the time.

## How to Design a Context Hierarchy

Organize information by authority and task relevance.
Keep normative rules separate from descriptive context and separate both from human-facing narrative.

For structure patterns, read `references/context-structure.md`.

Load `templates/RFC-UNIFIED-CONTEXT-STRUCTURE.md` when designing a `.context/` directory layout.
Load `templates/RFC-UNIFIED-DOCS-STRUCTURE.md` when designing a `docs/` directory layout.

## How to Load Context JIT

Load:

1. the minimum canonical layer required to act
2. task-specific context next
3. deep references only when a trigger demands them

Avoid brute-force loading entire repositories or document trees.

## How to Reduce Token Waste

Prefer:

- short metadata that improves discovery
- summaries over repeated history
- structured tables over repetitive prose
- targeted references over monolithic documents

For deeper optimization ideas, read `references/optimization-techniques.md`.

## How to Keep Multi-Agent Context Clean

When multiple agents are involved:

1. separate shared truth from private working memory
2. control who may write durable state
3. keep handoff artifacts explicit
4. avoid leaking irrelevant context across roles

For memory patterns, read `references/memory-management.md`.
For multi-agent workflow patterns and router or chain architectures, read `references/multi-agent-workflows.md`.

Load `references/sources.md` when the discussion needs canonical URLs, paper citations, or specific links for any referenced concept.
Load `references/common-traps.md` for the 5 most common context failures (overload paradox, stale context, JIT→eager, no enforcement, multi-agent leakage).

