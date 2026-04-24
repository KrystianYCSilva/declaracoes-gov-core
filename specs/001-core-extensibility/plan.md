# Implementation Plan: Core Audit and Composition Boundaries

**Branch**: `001-core-extensibility` | **Date**: 2026-04-20 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `specs/001-core-extensibility/spec.md`

## Summary

Audit the current `domain`, `format`, `crypto`, and `xml` modules to confirm they are complete for the release scope they already declare. Keep the current official-first defaults intact, remove stale documentation that promised nonexistent registries/SPIs, and document composition at the consumer boundary instead of forcing new internal hooks into the core.

## Technical Context

**Language/Version**: Java 8 (strict baseline)
**Primary Dependencies**: Maven, JUnit 4, Jackson, BouncyCastle
**Storage**: N/A
**Testing**: JUnit 4 + JaCoCo 90% gate
**Target Platform**: JVM 8+
**Project Type**: Core Library
**Performance Goals**: Preserve existing lightweight validation and utility behavior
**Constraints**: No Java 9+ APIs, no framework coupling, no speculative internal SPI
**Scale/Scope**: Documentation alignment plus audit guardrails for the current core surface

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **Agnosticism**: No declaration- or framework-specific abstraction is added.
- [x] **Java 8 Baseline**: No new APIs exceed the supported baseline.
- [x] **Coverage Plan**: Existing build and test gates remain unchanged.
- [x] **Security**: XML and crypto defaults remain intact; no weaker path is introduced.
- [x] **YAGNI**: Internal registries and mutable SPIs are deferred until real reuse justifies them.

## Project Structure

### Documentation (this feature)

```text
specs/001-core-extensibility/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
└── tasks.md
```

### Source Code (repository root)

```text
declaracoes-gov-core-domain/
├── src/main/java/br/uem/npd/govcore/validator/GovValidators.java
└── src/main/java/br/uem/npd/govcore/model/Nis.java

declaracoes-gov-core-format/
└── src/main/java/br/uem/npd/govcore/util/GovJsonFactory.java

declaracoes-gov-core-crypto/
└── src/main/java/br/uem/npd/govcore/crypto/SslContextBuilder.java

declaracoes-gov-core-xml/
└── src/main/java/br/uem/npd/govcore/util/XmlDocuments.java
```

**Structure Decision**: Preserve the current module boundaries and API surface. This feature updates the audit narrative and removes stale promises of internal registries/factories that are not part of the shipped code.

## Execution Strategy

1. Audit the public APIs that already exist in `domain`, `format`, `crypto`, and `xml`.
2. Confirm which extension points belong inside the core and which belong in consuming modules.
3. Recalibrate the spec artifacts to describe the current release-ready behavior.
4. Keep `IdentificadorEmpregador` contained to backward-compatible model types only.
5. Validate the result with `mvn verify`.

## Complexity Tracking

| Topic | Decision | Rationale |
|-------|----------|-----------|
| ValidatorRegistry | Deferred | No shipped code or proven multi-project need justifies introducing it now. |
| JSON factory SPI | Deferred | Current helper is sufficient as an opinionated default; consumers can own custom mappers externally. |
| SSL factory SPI | Deferred | Current builder covers the shared default path; specialized TLS should remain consumer-owned until reuse appears. |
