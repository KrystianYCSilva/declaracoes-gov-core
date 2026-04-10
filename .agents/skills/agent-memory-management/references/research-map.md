---
description: |
  Research and standards map for agent memory design.
  Use when: a memory decision needs academic grounding, stronger terminology, or source-driven tradeoff framing.
---

# Agent Memory Research Map

## Primary Anchors

- CoALA: useful vocabulary for working memory, long-term memory, action space, and decision modules in language agents.
- Retrieval-Augmented Generation for Knowledge-Intensive NLP Tasks: baseline framing for retrieval-backed memory rather than stuffing all facts into the prompt.
- Lost in the Middle: strong reminder that longer context does not mean better recall and that ranking and placement matter.
- Generative Agents: useful for episodic memory, reflection, and behavior continuity, but not a license to over-store everything.

## Operational Anchors

- Model Context Protocol documentation: useful when memory has to be exposed through tool contracts rather than hidden inside prompts.
- NIST Secure Software Development Framework: useful when durable memory affects operational truth, auditability, or release safety.

## How to Use These Anchors

Use academic sources to name the pattern and expose tradeoffs.
Use operational standards to decide who can write, how freshness is checked, and what qualifies as durable truth.

If the design decision changes workflow state or governance, structured state still wins over approximate retrieval.
