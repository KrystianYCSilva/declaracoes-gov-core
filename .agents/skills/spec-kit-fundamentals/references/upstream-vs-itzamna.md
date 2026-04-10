---
description: |
  Reference for the boundary between upstream Spec Kit workflow and Itzamna governance.
  Use when: deciding which layer owns a behavior, comparing local conventions to upstream templates,
  or explaining why Spec Kit is a backend and not the whole operating model.
---

# Upstream Versus Itzamna

## Upstream Spec Kit Owns

- `spec-template.md`
- `plan-template.md`
- `tasks-template.md`
- the sequential spec-driven workflow

## Itzamna Owns

- bootstrap and routing
- macro state and cards
- source-of-truth precedence
- closure and context updates

## Non-Negotiable Rule

`backend=spec-kit-native` means Spec Kit owns technical artifact generation for the current cycle.
It does not make Spec Kit the sovereign source of governance for the repository.
