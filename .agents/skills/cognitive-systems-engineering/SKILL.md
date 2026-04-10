---
name: cognitive-systems-engineering
description: |
  Guide engineering cognitive systems for AI agents with explicit memory, decision, and multi-agent coordination structures.
  Use when: translating cognitive architecture ideas into implementable system designs or operating models.
---

# Cognitive Systems Engineering

This skill is the implementation-facing companion to higher-level cognitive architecture thinking.
Load deeper references when the discussion shifts from design taste to system mechanics.

## How to Engineer a Memory Stack

Define:

1. working memory
2. durable knowledge
3. episodic or retrieval memory
4. procedural memory or skill layer

Each tier should have a clear write policy and retrieval purpose.

Use `references/memory-patterns.md` for concrete memory-shape options and boundary tradeoffs.
Use `references/operating-model.md` when memory design must plug into ownership, review, or recovery rules.

## How to Engineer a Decision Cycle

Choose the cycle based on observability and control needs.
For engineering systems, explicit observe-orient-decide-act boundaries often beat vague "agent autonomy".

If the team cannot tell which phase failed, the cycle is too implicit.

Use `references/architectures.md` when mapping theory to implementation primitives.
Use `references/operating-model.md` when the real question is who owns each phase rather than which theory sounds best.

## How to Engineer Multi-Agent Coordination

Choose between:

- orchestrator-workers
- pipeline
- blackboard
- review or debate patterns

The pattern should match dependency structure, not aesthetic preference.

If the design starts to hide shared-state risks, read `references/warnings.md` before finalizing it.
Use `references/operating-model.md` when the coordination pattern must become an auditable workflow rather than only a conceptual diagram.

## How to Verify the Design

Check whether the design names:

1. state ownership
2. escalation boundaries
3. failure handling
4. synchronization rules
5. closure conditions

Use `references/glossary.md` when terminology starts drifting across disciplines or teams.
Use `references/operating-model.md` when verification must include escalation, retries, or closure.
Load `references/sources.md` when the discussion needs canonical URLs, paper citations, or specific links for any referenced paper or framework.
