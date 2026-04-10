---
name: decision-loops
description: |
  Reference for comparing decision-loop patterns used in language-agent architectures.
  Use when: choosing between request-response, planning loops, ReAct-like control,
  or other iterative reasoning and action patterns.
---

# Decision Loops

## Baseline Request-Response

Best when the task is bounded and tool use is minimal.
Failure mode: hidden ambiguity or no explicit recovery path.

## Plan-Act-Review

Best when work must stay auditable and stepwise.
Failure mode: too much ceremony for trivial tasks.

## ReAct-Like Loops

Best when the agent must interleave reasoning and external actions.
Failure mode: looping, shallow tool grounding, or poor stop conditions.

## Selection Questions

1. how observable must the loop be?
2. how expensive is a wrong action?
3. how often must a human intervene?
4. what is the clean stop condition?

## Research Anchors

- ReAct is a useful anchor when tool use and reasoning must be interleaved.
- OODA-style framing is useful when the environment changes fast and the system must re-orient repeatedly.
- governed engineering workflows often need a simpler plan-act-review loop rather than maximal autonomy.
