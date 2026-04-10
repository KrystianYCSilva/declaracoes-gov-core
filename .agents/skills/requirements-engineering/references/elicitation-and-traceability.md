---
description: |
  Requirements engineering reference for elicitation structures and traceability mechanics.
  Use when: building discovery questions, linking requirements to delivery artifacts,
  or strengthening auditability across cards, specs, and tests.
---

# Elicitation and Traceability

## Elicitation Prompts

Ask:

1. Actor and goal
2. Trigger and expected behavior
3. Exception and abuse cases
4. Dependency and integration boundaries
5. Success signal

## Elicitation Technique Selection Guide

Choose the technique based on stakeholder availability and requirement maturity:

| Technique | Best For | Output |
|-----------|----------|--------|
| **Interviews** | Exploring unknowns with domain experts | Free-form notes, recorded insights |
| **Workshops** | Aligning multiple stakeholders on scope | Prioritized feature list, user story map |
| **Document Analysis** | Extracting rules from existing specs, regulations, or legacy systems | Structured requirement list |
| **Prototyping** | Validating UI/UX assumptions early | Clickable mock, feedback log |
| **Observation** | Understanding real workflows vs. described workflows | Process map, pain-point list |
| **Questionnaires** | Gathering input from many stakeholders asynchronously | Quantitative preference data |
| **Use Case Modeling** | Defining system-actor interactions precisely | Use case diagrams, step-by-step scenarios |

### Selection Heuristic

- Start with **document analysis** if prior specs exist.
- Use **interviews** for high-uncertainty domains.
- Use **workshops** when cross-team alignment is the bottleneck.
- Use **prototyping** when stakeholders struggle to articulate needs verbally.

## Traceability Targets

Link each important requirement to:

- Source request
- Epic or card
- Spec section
- Test evidence
- Change decision when it evolves

## Traceability Matrix Format

Use a table that maps each requirement to its upstream source and downstream artifacts:

| Req ID | Source | Card | Design Artifact | Test Case | Status |
|--------|--------|------|-----------------|-----------|--------|
| BR-001 | Stakeholder interview 2026-01-10 | CARD-015 | ADR-003 | BrCalculationTest | Verified |
| TR-001 | Architecture decision | CARD-002 | tech-stack.md | IntegrationTest | Verified |
| QR-001 | SLA agreement | CARD-020 | testing-strategy.md | PerformanceTest | Pending |

## Bidirectional Trace Links

Traceability must work in both directions:

- **Forward trace** (requirement -> implementation -> test): Confirms every requirement is implemented and tested.
- **Backward trace** (test -> implementation -> requirement): Confirms every test validates a known requirement (no orphan tests testing nothing meaningful).

### Maintaining Bidirectional Links

1. Each requirement document references the cards and tests that satisfy it.
2. Each test file or test class references the requirement ID it validates (e.g., in a comment or annotation).
3. Each card references the requirements it addresses and the tests that verify completion.
4. During review, check both directions: no requirement without a test, no test without a requirement.

### Coverage Gap Detection

- Walk the matrix column by column. An empty "Test Case" cell means the requirement lacks verification.
- Walk backward from test inventory. A test not linked to any Req ID may be redundant or may indicate an undocumented requirement.
