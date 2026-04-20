---
name: quality-assurance
description: |
  Guide QA practices for risk-based validation, release readiness, and defect prevention in software delivery.
  Use when: setting release criteria, designing risk-based test strategy, or checking whether a change is safe to ship.
activation: Auto
estimated_tokens: 580
---

# Quality Assurance

This skill focuses on release confidence and validation strategy.
It is not a substitute for requirements discovery and it is not general architecture guidance.

## How to Build a Risk-Based Test Strategy

Prioritize testing by:

1. user impact
2. change complexity
3. security exposure
4. data or financial risk
5. likelihood of regression

Not every change deserves the same test depth.
Critical paths deserve stronger evidence.

## How to Define Release Readiness

A release is ready only when:

1. scope is understood
2. required tests passed
3. unresolved issues are classified and accepted consciously
4. rollback and observability expectations are known

Use `references/release-readiness.md` when the decision is contentious.

## How to Prevent Defects Systemically

Defect prevention is more than testing.
Check:

- unclear requirements
- weak review criteria
- missing environment parity
- poor observability
- insecure defaults

Use `references/sre-and-secure-sdlc.md` when operational quality and secure SDLC concerns dominate.

## How to Report QA Findings

Report by risk and decision impact:

1. what failed or remains uncertain
2. why it matters
3. what evidence exists
4. what must happen before release

Use `references/risk-based-testing.md` for a deeper prioritization lens.
Use `references/common-traps.md` for the 5 most common QA failures with AI-generated code.
Use `templates/test-case-template.md` for Given-When-Then test structure and naming conventions.
Use `templates/risk-assessment-matrix.md` for a fillable risk/impact scoring template.
Use `templates/release-readiness-checklist.md` for a pre-release gate checklist.

## How to Stay Accurate

When invoking testing standards, quality attribute taxonomies, or security testing frameworks,
use `references/sources.md` for the canonical methodology references (ISO 29119, ISO 25010,
OWASP, ISTQB, SRE practices) before asserting standard compliance or vocabulary.
