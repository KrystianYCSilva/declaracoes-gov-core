# Implementation Plan: Core Extensibility and Composition

**Branch**: `001-core-extensibility` | **Date**: 2026-04-20 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `specs/001-core-extensibility/spec.md`

## Summary

Audit and adjust the current core modules (`domain`, `format`, `crypto`, `xml`) to introduce extensibility via composition and SPI patterns. The primary focus is breaking the rigidity of `GovValidators` and `Nis` by introducing a `ValidatorRegistry`, while ensuring absolute backward compatibility for existing static consumers.

## Technical Context

**Language/Version**: Java 8 (Strict Baseline)
**Primary Dependencies**: Maven, JUnit 4, Mockito, BouncyCastle, Jackson
**Storage**: N/A (Core library)
**Testing**: JUnit 4 + JaCoCo (90% coverage target)
**Target Platform**: JVM 8+
**Project Type**: Core Library (Declaration & Framework Agnostic)
**Performance Goals**: High-performance normalization and validation
**Constraints**: No Java 9+ features, No high-level frameworks (Spring/Jakarta)
**Scale/Scope**: Shared foundation for all declaracoes-* applications

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- [x] **Agnosticism**: The registry is purely domain-driven and does not depend on specific layouts or frameworks. (Principle I)
- [x] **Memory Protocol**: Workflow includes updating `MEMORY.md` and local memory files. (Principle II)
- [x] **Java 8 Baseline**: All proposed changes (registries, factories) use standard Java 8 APIs. (Constraint)
- [x] **Coverage Plan**: New classes and refactored logic will be covered by 90%+ unit tests. (Principle III)
- [x] **Delegation**: N/A (Handled directly in this plan)
- [x] **Security**: Existing XML/Crypto protections are maintained; new factories only open configuration. (Principle V)

## Project Structure

### Documentation (this feature)

```text
specs/001-core-extensibility/
├── plan.md              # This file
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
└── tasks.md             # Phase 2 output
```

### Source Code (repository root)

```text
declaracoes-gov-core-domain/
├── src/main/java/br/uem/npd/govcore/validator/
│   ├── ValidatorRegistry.java (NEW)
│   ├── GovValidators.java (Refactored)
│   └── ...
├── src/main/java/br/uem/npd/govcore/model/
│   ├── Nis.java (Refactored)
│   └── ...

declaracoes-gov-core-format/
├── src/main/java/br/uem/npd/govcore/util/
│   ├── GovJsonFactory.java (Refactored/SPI)
│   └── ...

declaracoes-gov-core-crypto/
├── src/main/java/br/uem/npd/govcore/crypto/
│   ├── SslContextFactory.java (NEW/SPI)
│   ├── SslContextBuilder.java (Refactored)
│   └── ...
```

**Structure Decision**: Standard Maven multi-module structure preserved. New SPI/Registry classes added to relevant modules.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| N/A | No violations detected | |
