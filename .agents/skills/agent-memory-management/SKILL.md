---
name: agent-memory-management
description: |
  Guide architecting memory systems for AI agents from short-context buffers to retrieval-backed and multi-agent state models.
  Use when: designing stateful agents, solving context limit issues, or choosing how durable memory should be represented.
---

# Agent Memory Management

Memory design is a system decision, not a prompt trick.
Keep this file thin, and load deeper references only for the memory problem in front of you.
The right shape is usually a combination of bounded working context, a small durable truth surface,
and optional retrieval for non-authoritative recall.

## How to Choose a Memory Tier

Match the memory mechanism to the job:

- short window or summary for local conversational continuity
- retrieval or episodic storage for sparse recall
- structured state for durable operational truth
- role-scoped memory for multi-agent systems

Avoid claiming "infinite memory" when retrieval quality and staleness are still unresolved.

Read:

- `references/memory-tier-selection.md` when deciding between buffers, summaries, retrieval, or durable state
- `references/retrieval-and-freshness.md` when the problem is staleness, ranking, or "lost in the middle"
- `references/research-map.md` when the design needs source-driven terminology or academic grounding

## How to Prevent Memory Pollution

Control:

1. who can write
2. what qualifies as durable
3. how stale entries are handled
4. how retrieval is filtered

If everything becomes memory, nothing remains useful.

Use `references/retrieval-and-freshness.md` when the failure smells like recall quality rather than storage quantity.
Use `references/research-map.md` when the discussion starts hand-waving around "long context" without naming failure modes.

## How to Handle Multi-Agent State

Separate:

1. shared truth
2. private working context
3. message or handoff artifacts

Global mutable memory without ownership rules becomes a source of drift and race conditions.

Use `references/multi-agent-memory.md` when the design crosses agent boundaries or ownership layers.
If the system mixes private reasoning with shared truth, stop and define write ownership before adding more memory.

## How to Evaluate a Memory Design

Ask:

1. does it improve the next decision?
2. can it be audited?
3. can stale entries be corrected?
4. does it reduce, rather than increase, hallucination risk?

If the answer depends on theory or literature rather than local intuition, read the relevant reference first instead of stretching the SKILL body.

Load `references/sources.md` when the discussion needs canonical URLs, paper citations, or specific links for any referenced paper or framework.
