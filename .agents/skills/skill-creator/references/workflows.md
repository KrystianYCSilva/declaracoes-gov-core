---
name: skill-creator-reference-workflows
description: |
  Workflow reference for designing reusable skill packages with progressive disclosure.
  Use when: applying the skill-creator skill and you need deeper guidance on turning examples
  into scripts, references, assets, and validation checkpoints.
---

# Workflow Patterns

## Sequential Workflow Pattern

Use this when the task has a stable order of operations:

1. identify inputs and constraints
2. choose scripts or tools
3. perform the transformation
4. validate the result
5. document any required follow-up

## Conditional Workflow Pattern

Use this when the first decision changes the rest of the procedure:

1. define the branching question
2. provide the safe choices
3. point each branch to the matching reference or script

## Resource Planning Questions

Before finalizing a skill, ask:

1. what must stay in `SKILL.md` to make the first move?
2. what detail should move to `references/`?
3. what repetitive execution belongs in `scripts/`?
4. what output artifact belongs in `assets/`?
