---
description: |
  Common traps in AI agent memory design with validated fixes.
  Use when: debugging memory-related failures in multi-agent or long-running agent systems.
---

# Common Memory Traps

## Trap 1: Unbounded Memory Growth

**Symptom:** Agent slows down progressively. Context window fills up. Responses become generic or contradictory.

**Cause:** Every observation, decision, and intermediate result is stored as "memory" without eviction policy.

**Fix:**
- Set hard limits: working memory ≤ 20 items, episodic ≤ 100, semantic unlimited but indexed
- Implement TTL (time-to-live) for working memory: items older than N turns are summarized or evicted
- Use tiered storage: hot (context window) → warm (session file) → cold (database/vector store)

**Validation:** After N turns, measure context utilization. If >80% is memory, you have unbounded growth.

---

## Trap 2: Memory Poisoning

**Symptom:** Agent makes confidently wrong decisions because it "remembers" incorrect information from earlier.

**Cause:** Early hallucination stored as durable memory. Later decisions reference this fabricated "fact" as truth.

**Fix:**
- Never store LLM output directly as memory without validation
- Separate "observations" (raw) from "conclusions" (validated)
- Add provenance: every memory item must link to its source (user input, tool output, or inference)
- Allow memory correction: human can flag items as invalid

---

## Trap 3: Ghost Context in Multi-Agent Systems

**Symptom:** Agent B acts on information that Agent A had in context but never explicitly shared.

**Cause:** Agents share a session file or memory store, but the sharing protocol is undefined. Agent B reads Agent A's working notes as if they were shared decisions.

**Fix:**
- Define explicit memory scopes:
  - **Private:** only this agent reads/writes (working memory, scratchpad)
  - **Shared:** all agents can read, designated writer(s) only (truth table)
  - **Handoff:** one-time message from agent A to agent B (consumed on read)
- Never share an agent's raw context window — only curated handoff artifacts

---

## Trap 4: Stale Memory Overrides Fresh Context

**Symptom:** Agent insists on using a pattern/version/approach that was correct 5 sessions ago but has since changed.

**Cause:** Durable memory (stored in files) has higher perceived authority than fresh context (current user message). Agent treats memory as more authoritative than reality.

**Fix:**
- Timestamp all memory items
- Fresh context (current turn) always overrides stored memory
- Implement explicit staleness check: "Is this memory from the current project state?"
- For code-related memory: re-validate by reading the actual file, not the remembered version

**Evidence:** In prior sessions, CLI-generated templates were created in session 3 but by session 8 they were outdated. The agent still tried to use them until explicitly told to re-read the source.

---

## Trap 5: Context Window as Poor Man's Memory

**Symptom:** Agent works well for the first 30 minutes, then starts losing track of what it was doing. Instructions from early in the session are forgotten.

**Cause:** Using the context window as the only memory mechanism. When it fills up, old information is evicted and the agent loses its operational knowledge.

**Fix:**
- Externalize durable state: use files (AGENTS.md, plan.md) for information that must survive context eviction
- Use structured artifacts (SQL todos, checklists) for tracking progress
- Summarize completed phases — don't keep raw transcripts in context
- The formula: AGENTS.md (durable) + context (transient) + tools (infinite)
