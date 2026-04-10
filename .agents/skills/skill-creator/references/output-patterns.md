---
name: skill-creator-reference-output-patterns
description: |
  Output-shape reference for skill packages that need deterministic or semi-structured responses.
  Use when: applying the skill-creator skill and you need examples of strict, flexible,
  or review-oriented output contracts.
---

# Output Patterns

Use these patterns when a skill needs predictable outputs.

## Strict Output Pattern

Use this for fragile or machine-consumed outputs:

- exact section order
- exact field names
- explicit "output only" instruction
- explicit forbidden extras

## Flexible Output Pattern

Use this when structure matters but local adaptation is acceptable:

- required top-level sections
- optional subsections
- room for context-specific detail

## Review Output Pattern

Use this for audits and code reviews:

1. findings first
2. severity labels
3. file references
4. change summary only after findings
