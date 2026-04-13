---
name: ai-assistant-guide
description: |
  Bootstrap and operating guide for AI assistants working in declaracoes-gov-core.
  Use when: deciding what to load, how to classify a request, and what done means here.
---

# AI Assistant Guide

## Bootstrap Sequence

1. Load `README.md` in `.context/`.
2. Load `_meta/project-overview.md` and `_meta/tech-stack.md`.
3. Load `standards/architectural-rules.md`.
4. Load only the task-specific files required for the current change.

## Request Classification

| Request Type | Minimum Files To Load |
| --- | --- |
| Validator or document model change | `standards/architectural-rules.md`, `_meta/codebase-map.md`, `_meta/key-decisions.md`, `standards/testing-strategy.md` |
| Format, parser, or `GovJsonFactory` change | `standards/architectural-rules.md`, `_meta/tech-stack.md`, `_meta/codebase-map.md`, `patterns/architecture-patterns.md` |
| XML signing or DOM utility change | `standards/architectural-rules.md`, `_meta/codebase-map.md`, `_meta/key-decisions.md`, `standards/testing-strategy.md` |
| PKCS11, PKCS12, or `SSLContext` change | `standards/architectural-rules.md`, `_meta/tech-stack.md`, `_meta/key-decisions.md`, `standards/testing-strategy.md` |
| Documentation or context sync task | `README.md`, `workflows/development-workflow.md` |

## Operating Rules

- This repository root is a Maven aggregator; the live implementation is in the child modules.
- Keep the core limited to transversal domain, formatting, XML, crypto, and BOM concerns.
- Do not add declaration-specific schemas, transport clients, OAuth2 flows, or framework wiring here.
- Use the actual package tree `br.uem.npd.govcore.*`; do not invent alternate package names in docs or code.
- Keep the validator confidence model aligned with `docs/05-MATRIZ-VALIDADORES.md`.
- Update Portuguese human docs first when behavior, policy, or architecture changes; then update AI docs.

## Definition of Done

- The change respects `standards/architectural-rules.md`.
- Behavior changes update or add tests in the same child module.
- `mvn -q test` stays green for the touched scope, and `mvn -q verify` remains the release-level gate.
- The parent coverage defaults stay true (`90%` line / `90%` branch, with the documented `crypto` line exception at `85%`).
- Public API or validator policy changes keep `docs/02-DESIGN.md`, `docs/03-PLANO-TESTES.md`, and `docs/05-MATRIZ-VALIDADORES.md` synchronized where applicable.
- No secrets, certificate material, or token configuration are added to Git.

## Research Method

1. Read the relevant Portuguese human document.
2. Confirm the current implementation in the child-module source and `pom.xml`.
3. Resolve conflicts in favor of the real code.
4. Compress the stable truth into `.context/` without inventing new behavior.

## Sync Triggers

Update `.context/` whenever any of the following changes:

- validator confidence level or document constructor behavior
- formatting or parser behavior in `declaracoes-gov-core-format`
- XML signing defaults or target-selection rules in `declaracoes-gov-core-xml`
- certificate-provider or `SSLContext` behavior in `declaracoes-gov-core-crypto`
- module boundaries, build gates, or coverage thresholds
- public API additions, deprecations, or removals
