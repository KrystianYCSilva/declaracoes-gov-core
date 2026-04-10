---
name: ai-assistant-guide
description: |
  Bootstrap and operating guide for AI assistants working in declaracoes-gov-core.
  Use when: deciding what to load, how to classify a request, and what done means in this repository.
---

# AI Assistant Guide

## Bootstrap Sequence

1. Load `standards/architectural-rules.md`.
2. Load `_meta/project-overview.md` and `_meta/codebase-map.md`.
3. Load only the task-specific standard, pattern, workflow, or example files required for the current change.

## Request Classification

| Request Type | Minimum Files To Load |
| --- | --- |
| Certificate (A1/A3) or mTLS change | `standards/architectural-rules.md`, `_meta/key-decisions.md`, `_meta/codebase-map.md`, `standards/testing-strategy.md` |
| XML signature algorithm change | `standards/architectural-rules.md`, `_meta/key-decisions.md`, `patterns/architecture-patterns.md` |
| CNPJ/CPF/IE validator change | `standards/architectural-rules.md`, `_meta/codebase-map.md`, `patterns/architecture-patterns.md`, `standards/testing-strategy.md` |
| JSON mapper/serialization change | `standards/architectural-rules.md`, `_meta/tech-stack.md`, `patterns/architecture-patterns.md` |
| Test change or regression fix | `standards/testing-strategy.md`, `patterns/testing-and-tdd.md`, `workflows/testing-and-validation-workflow.md` |
| Review or QA request | `_meta/codebase-map.md`, `workflows/review-qa-and-release-workflow.md`, `troubleshooting/common-issues.md` |
| Performance optimization | `standards/architectural-rules.md`, `_meta/tech-stack.md`, `patterns/architecture-patterns.md` |

## Operating Rules

- This is a library project (not an application); maintain framework agnosticism.
- Never introduce Spring, Jakarta EE, or other framework dependencies in core modules.
- Prefer the existing package boundaries (`certificado`, `assinatura`, `documento`, `json`) and extension points over new abstractions.
- Never invent behavior that is not documented in `docs/` or implemented in `src/`.
- When architecture, behavior, or operational setup changes, update `docs/` first for humans and `.context/` second for AI compression.
- All certificate-related code must support both A1 (file-based) and A3 (hardware token) certificates.
- XML signature must comply with eSocial/EFD-Reinf specifications: RSA-SHA256, SHA-256 digest, C14N canonicalization, Enveloped transform.
- CNPJ validator must support both numeric (current) and alphanumeric (2026+) formats.

## Definition Of Done

- The change respects `standards/architectural-rules.md`.
- The affected tests are updated or added.
- `mvn -q test` stays green for behavior changes.
- `mvn -q verify` stays green before release-level completion.
- JaCoCo minimums remain at `80%` line and `75%` branch.
- No secrets are added to Git or embedded into the JAR.
- `docs/` and `.context/` are synchronized when behavior, architecture, tests, or environment requirements change.
- All public APIs have Javadoc with thread-safety notes.
- Java 8 compatibility is maintained (no Java 9+ features).

## Research Method

1. Read the relevant human doc in `docs/`.
2. Confirm the current implementation in `src/`.
3. Resolve conflicts in favor of the real code, then update documentation.
4. Summarize the stable truth in `.context/` without duplicating full documents.

## Sync Triggers

Update `.context/` whenever any of the following changes:

- certificate loading, validation, or mTLS behavior
- XML signature algorithms or transforms
- document validators (CNPJ, CPF, IE) logic
- JSON serialization/deserialization behavior
- package responsibilities, class responsibilities, or extension points
- test strategy, coverage thresholds, or release gates
- accepted architectural decisions or removed legacy artifacts
- public API changes (new methods, deprecations, removals)

## Special Considerations for Core Library

### Thread Safety
- All public classes must be thread-safe.
- Use immutable objects where possible.
- Document thread-safety guarantees in class-level Javadoc.
- Use `ReadWriteLock` for shared mutable state.

### Dependencies
- Keep external dependencies to minimum.
- Mark optional dependencies with `<optional>true</optional>` in pom.xml.
- Prefer SPI (Service Provider Interface) for extensibility.
- Avoid dependency version conflicts - use provided scope when integrating with other libs.

### Backward Compatibility
- Maintain semantic versioning (MAJOR.MINOR.PATCH).
- Do not break public APIs without major version bump.
- Use `@Deprecated` with clear migration path before removal.
- Keep deprecated methods for at least one minor version.
