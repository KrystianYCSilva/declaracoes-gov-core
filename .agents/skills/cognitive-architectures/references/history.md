---
name: history
description: |
  Historical reference for the evolution of cognitive architectures from symbolic systems to language-agent frameworks.
  Use when: grounding an architecture discussion in prior paradigms, comparing LLM-era agents to classical systems,
  or explaining why certain modules still matter.
---

# History of Cognitive Architectures

## Era 1: Symbolic Architectures (1980s - 2000s)

- SOAR: rule-based system focused on problem-solving and learning through chunking.
- ACT-R: modeled cognition with distinct modules such as declarative memory and production rules.
- Key lesson: explicit modularity is powerful, but brittleness in open environments is high.

## Era 2: Connectionist and Hybrid Approaches (2000s - 2010s)

- hybrid systems explored combinations of learning and explicit control
- the main lesson was that data-driven adaptation helps, but does not remove the need for clear control structure

## Era 3: LLM-Based Agents (2020s - Present)

- CoALA: gives a useful decomposition for language agents
- ReAct-style systems: interleave reasoning and action
- Generative Agents: emphasize episodic continuity and reflection
- Key lesson: language is a strong coordination interface, but it does not erase the need for memory and control boundaries

## How to Use This History

Use the older architectures to name modules and control concerns.
Use the newer agent literature to understand what LLM-native systems make easier.
Do not confuse historical inspiration with a requirement to reproduce every mechanism in production.
