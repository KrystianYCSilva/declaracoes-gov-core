---
description: |
  Reference for selecting working, episodic, semantic, and procedural memory tiers for AI agents.
  Use when: deciding which memory mechanism fits the problem, comparing buffer versus retrieval approaches,
  or mapping architectural memory concepts to concrete implementation choices.
---

# Memory Tier Selection

## Working Memory

Use for the active task and short-lived reasoning state.
Typical forms:

- bounded message window
- rolling summary
- local scratchpad or task board

## Episodic Memory

Use for recalling prior events, outcomes, or sessions.
Typical forms:

- retrieved summaries
- event logs
- vectorized episodes or issue histories

## Semantic Memory

Use for relatively stable facts, rules, and domain knowledge.
Typical forms:

- curated docs
- knowledge graphs
- versioned policies or configuration artifacts

## Procedural Memory

Use for reusable "how to" behavior.
Typical forms:

- skills
- tools
- scripts
- workflow contracts

## Selection Questions

1. how often does the information change?
2. does the agent need exact recall or approximate recall?
3. who is allowed to write it?
4. does the next decision require continuity, institutional memory, or both?
