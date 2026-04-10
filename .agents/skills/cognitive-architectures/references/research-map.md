---
name: research-map
description: |
  Research map for cognitive architectures in the language-agent era.
  Use when: the discussion needs stronger academic grounding, comparison across paradigms,
  or a clearer bridge from named papers to architectural decisions.
---

# Cognitive Architecture Research Map

## Primary Anchors

- ACT-R: modular cognition, memory systems, and production-rule thinking
- SOAR: explicit control structures, operators, and chunking
- ReAct: interleaving reasoning and action rather than separating them artificially
- CoALA: a practical vocabulary for memory, action space, and decision modules in language agents
- Generative Agents: reflective memory and social simulation patterns

## What These Sources Are Good For

- classical architectures: naming modules and control concerns
- ReAct and similar agent papers: deciding when tool use and reasoning should be interleaved
- CoALA: decomposing modern language agents into concrete components without pretending the model is the whole system
- generative-agent work: episodic continuity and reflection, not governance or release workflow

## Practical Rule

Use named sources to clarify design vocabulary.
Do not import every academic mechanism into production if the environment only needs a simpler governed loop.
