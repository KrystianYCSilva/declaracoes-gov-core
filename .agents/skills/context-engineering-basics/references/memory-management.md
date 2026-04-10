---
name: memory-management
description: |
  Reference for memory consolidation tiers (hot/warm/cold) and persistence strategies.
  Use when: an agent needs multi-tier memory or a session requires deciding what to persist and what to discard.
  For multi-agent context strategies (blackboard, context isolation, handoff), see multi-agent-workflows.md.
---

# Memory Management

## Memory Consolidation Tiers

1.  **Working Memory (Hot)**: Last 5-10 messages. High fidelity.
2.  **Session Memory (Warm)**: Summary of the current conversation + extracted entities.
3.  **Episodic Memory (Cold)**: Vector database of past conversations.

## Persistence Strategies

- **Append-only logs**: Each session appends a summary block to a durable file (e.g., `MEMORY.md`). Suitable for audit trails.
- **Rolling summary**: Overwrite the previous summary with a compressed version that retains key facts. Suitable for long-running projects where history length must stay bounded.
- **Entity extraction**: After each session, extract named entities (files changed, decisions made, blockers) into structured fields rather than free-form prose.

## When to Promote Memory

- Promote from Hot to Warm when the conversation exceeds the context window budget.
- Promote from Warm to Cold when the session ends or a new session begins.
- Prune Cold memory periodically by relevance score or staleness date.

