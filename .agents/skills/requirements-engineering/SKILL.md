---
name: requirements-engineering
description: |
  Guide requirements engineering from elicitation through specification, validation, and traceability.
  Use when: defining clear, testable requirements, separating business intent from implementation detail,
  or controlling scope change across engineering delivery.
---

# Requirements Engineering

This skill focuses on requirement quality, not implementation style and not release governance.
Use it before coding and before QA planning hardens the release strategy.

## How to Elicit the Real Need

Capture:

1. who needs the capability
2. what outcome they need
3. what is explicitly out of scope
4. what exists today
5. how success will be observed

If the problem statement already smells like a solution, peel it back to the underlying need first.

## How to Write Testable Requirements

Requirements should be:

- specific
- observable
- bounded
- implementation-agnostic
- traceable to business intent

Separate:

- functional requirements
- quality attributes
- constraints
- acceptance criteria

For deeper examples, read `references/acceptance-and-quality-attributes.md`.

## How to Maintain Traceability

Trace every important requirement to:

1. source or stakeholder intent
2. card/epic or spec artifact
3. acceptance or test evidence
4. change decisions over time

Use `references/elicitation-and-traceability.md` when the project needs a stronger traceability matrix.

## How to Control Requirement Change

Change requests should answer:

1. what changed
2. why it changed
3. what downstream artifacts must move
4. whether scope or risk grew

If a requirement change invalidates the current card or technical bundle, reopen planning instead of patching around the drift.

Use `references/change-control.md` for the minimum change record structure.

## How to Stay Accurate

When asserting standard terminology, elicitation techniques, or traceability vocabulary,
use `references/sources.md` for canonical methodology anchors (IEEE 29148, BABOK, SWEBOK,
BDD/Gherkin) before making claims about requirements engineering best practice.
