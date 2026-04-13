---
name: code-quality
description: |
  Project-specific code quality rules for declaracoes-gov-core.
  Use when: touching maintained Java code in any child module.
---

# Code Quality Standards

## Naming and Package Continuity

- Keep the existing package root `br.uem.npd.govcore`.
- Reuse the established domain vocabulary already present in the codebase (`Cnpj`, `PeriodoApuracao`, `TipoInscricao`, `GovValidators`, `XmlDsigSigner`, `SslContextBuilder`).
- Do not invent alternate English package trees or alias packages in code or AI docs.

## Public API Patterns Already in Use

- Prefer immutable value objects with static factory methods such as `of(...)` or `ofProvisionallyValidated(...)`.
- Keep utility-only classes `final` with private constructors.
- Keep cross-module contracts narrow and interface-driven (`DocumentValidator`, `XmlSigner`, `CertificateProvider`).
- Favor explicit behavior over hidden magic, especially for validator confidence and XML signing target selection.

## Error and Null Handling

- Invalid fiscal-domain input should continue to fail through the repository's domain exceptions, especially `InvalidDocumentException` and the `Gov*Exception` hierarchy.
- Use `IllegalArgumentException` only when it matches the current API style for required arguments.
- Do not widen null-handling semantics in public APIs without updating the related human docs.

## Documentation Expectations

- When touching public APIs, preserve or improve the existing Javadoc instead of adding stale comments elsewhere.
- Keep validator-policy wording aligned with `docs/05-MATRIZ-VALIDADORES.md`.
- Keep source comments and AI docs synchronized with the actual implementation; do not rewrite unrelated files in a documentation-only task.

## What to Avoid

- Framework annotations or dependencies in the core modules.
- Transport logic in `domain`, `format`, `xml`, or `crypto`.
- Broad refactors that rename established public types without a documented compatibility plan.
- Documentation or examples that refer to classes not present in the current repository state.

## Review Checklist

- [ ] The change stays inside the correct module boundary.
- [ ] Public behavior matches the current validator confidence model.
- [ ] New or changed public APIs remain consistent with existing naming and exception patterns.
- [ ] Tests and docs were updated when the public contract changed.
