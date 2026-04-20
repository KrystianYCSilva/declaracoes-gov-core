---
name: prompt-engineering-advanced
description: |
  Guide advanced prompt design with structure, decomposition, and evaluation-focused reasoning patterns.
  Use when: crafting high-stakes prompts, shaping multi-step analysis, or improving reliability without bloating context.
activation: Manual
estimated_tokens: 560
---

# Advanced Prompt Engineering

The goal is not to make prompts longer.
The goal is to make the task boundary, evidence, and expected output sharper.

## How to Structure a Strong Prompt

A strong prompt usually makes these explicit:

1. role or evaluation posture
2. task objective
3. relevant context
4. output contract
5. constraints or exclusions

For copyable patterns, read `references/prompt-templates.md`.

## How to Use Advanced Reasoning Patterns Carefully

Use decomposition only when the task needs it.
Prefer:

- step decomposition for planning
- option comparison for tradeoffs
- self-check prompts for review and verification

Do not force verbose reasoning traces when a shorter structured answer is enough.

For the pattern catalog, read `references/techniques-catalog.md`.
For deep worked examples of CoT, ToT, and ReAct, read `references/reasoning-frameworks.md`.

## How to Reduce Context Waste

Prefer:

1. delimiters
2. explicit source lists
3. thin summaries plus JIT expansion
4. structured small inputs instead of giant blobs

If the model can infer a basic concept already, spend tokens on the non-obvious part instead.

## How to Evaluate Prompt Quality

Check whether the prompt:

1. reduces ambiguity
2. names the real deliverable
3. separates facts from instructions
4. avoids accidental overreach
5. is easy to iterate after failure

For common structural pitfalls, read `references/warnings.md`.
For terminology clarification, read `references/glossary.md`.
Load `references/sources.md` when the discussion needs canonical URLs or paper citations for any referenced technique.
Load `references/prompt-failure-patterns.md` for the 6 most common prompt failures with fixes (kitchen sink, implicit constraints, ask-vs-execute, zero-shot, contradictions, temperature).
Load `templates/prompt-audit-checklist.md` for a 5-point quality gate to score prompts before use.
