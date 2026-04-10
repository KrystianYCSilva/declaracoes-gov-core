---
name: cognitive-architectures
description: |
  Guide cognitive architecture choices for language agents, from memory organization to decision loops and tool use.
  Use when: designing autonomous agents, choosing reasoning loops, or comparing architectural patterns for agent systems.
---

# Cognitive Architectures

Use this skill when the question is architectural: how the agent should think, remember, and act as a system.

## How to Decompose an Agent Architecture

Start with four concerns:

1. perception
2. memory
3. decision loop
4. action space

If one of these concerns is implicit, the architecture is probably under-specified.

Use `references/history.md` for the lineage from symbolic to LLM-era systems.
Use `references/decision-loops.md` when the hard part is choosing the control loop.
Use `references/research-map.md` when the design debate needs source-backed terminology instead of intuition.

## How to Choose a Decision Pattern

Pick the loop that matches the environment:

- simple request/response for bounded tasks
- plan-act-review loops for governed delivery
- richer agent loops only when the system truly needs them

Use fancy loops only when they improve observably on the baseline.

If the tradeoff is unclear, compare loops against stop conditions, observability, and human override points instead of aesthetics.
If the proposal becomes academic name-dropping, map the paper back to a concrete failure or capability before adopting it.

## How to Keep the Architecture Grounded

Check:

1. memory freshness
2. tool boundaries
3. stop conditions
4. escalation paths
5. human override points

For evaluation questions, read `references/evaluation-checkpoints.md`.
For source-backed framing, read `references/research-map.md`.

## How to Review an Agent Architecture

An architecture should be rejected if it:

- hides state ownership
- lacks stop conditions
- assumes unlimited context
- gives powerful actions without corresponding controls

Load `references/sources.md` when the discussion needs canonical URLs, paper citations, or specific links for any referenced paper or framework.
