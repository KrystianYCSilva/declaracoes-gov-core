# Feature Specification: Core Audit and Composition Boundaries

**Feature Branch**: `feature/001-core-extensibility`
**Created**: 2026-04-20
**Status**: Recalibrated
**Input**: User description: "Auditoria e ajustes do core atual para garantir composição e extensibilidade sem quebrar defaults"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Audit Existing Core Defaults (Priority: P1)

As a maintainer of the core library, I need to validate whether the current `domain`, `format`, `crypto`, and `xml` modules are already sufficient for release in their declared scope, so that we do not introduce unnecessary abstractions or fake extension points.

**Why this priority**: The core must remain stable, agnostic, and official-first before any new extensibility hook is added.
**Independent Test**: Review the public APIs and confirm the documentation references only classes and methods that actually exist in the codebase.

**Acceptance Scenarios**:
1. **Given** the current core modules, **When** the audit is complete, **Then** the documentation reflects only the APIs that already exist.
2. **Given** the current public defaults (`GovValidators`, `Nis`, `GovJsonFactory`, `SslContextBuilder`), **When** existing tests are executed, **Then** behavior remains unchanged.

---

### User Story 2 - Preserve Composition at Consumer Boundary (Priority: P2)

As a developer consuming the library, I need clear guidance for composing custom validation, JSON, and TLS flows outside the core defaults, so that I can support edge cases without forcing new internal registries or SPI layers into the shared foundation.

**Why this priority**: Composition remains mandatory, but it must be introduced where real reuse exists rather than inside every helper by default.
**Independent Test**: Demonstrate custom validation, JSON configuration, and TLS setup using only existing public APIs plus consumer-side wrappers/services.

**Acceptance Scenarios**:
1. **Given** a custom validation rule for NIS, **When** a consumer wraps `Nis`/`GovValidators` in its own service, **Then** the custom rule can be applied without modifying the core library.
2. **Given** a consumer-specific JSON requirement, **When** the consumer needs its own mapper, **Then** it can build or copy an `ObjectMapper` without changing `GovJsonFactory`.
3. **Given** a consumer-specific TLS requirement, **When** the default `SslContextBuilder` is insufficient, **Then** the consumer can build its own `SSLContext` externally.

---

### User Story 3 - Contain Deprecated Domain Contracts (Priority: P3)

As a maintainer of the domain model, I need to ensure no new validation or utility flows are introduced around the deprecated `IdentificadorEmpregador`, so that backward compatibility is preserved without spreading a legacy contract into new shared abstractions.

**Why this priority**: The deprecated type can remain for compatibility, but it should not drive new API design.
**Independent Test**: Static analysis confirms the deprecated interface remains contained to existing compatibility implementations only.

**Acceptance Scenarios**:
1. **Given** the current domain module, **When** the audit runs, **Then** no new validators, registries, or helpers depend on `IdentificadorEmpregador`.
2. **Given** legacy consumers of `Cnpj` and `Cpf`, **When** they continue using the current APIs, **Then** backward compatibility is preserved.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST preserve the current public behavior of `GovValidators`, `Nis`, `GovJsonFactory`, `SslContextBuilder`, and the existing XML utilities.
- **FR-002**: Feature documentation MUST NOT advertise registries, factories, setters, or SPIs that do not exist in the codebase.
- **FR-003**: Composition for consumer-specific rules MUST be supported at the integration boundary, using existing core APIs plus consumer-side services/wrappers/builders.
- **FR-004**: System MUST NOT introduce new dependencies on `IdentificadorEmpregador` outside the existing compatibility types that already implement it.
- **FR-005**: Internal extension points such as `ValidatorRegistry`, `SslContextFactory`, and mutable `GovJsonFactory` hooks are explicitly deferred until multi-project reuse justifies them.

### Key Entities

- **Core Defaults**: `GovValidators`, `Nis`, `GovJsonFactory`, `SslContextBuilder`, XML helpers, and value objects already published by the core.
- **Consumer Composition Layer**: Custom services, wrappers, factories, or builders created outside the core to adapt the defaults to a specific declaration flow.
- **Deprecated Compatibility Types**: Existing domain classes that still implement `IdentificadorEmpregador` for legacy interoperability.

### Non-Functional Requirements

- **NFR-001**: Implementation and documentation MUST remain strictly Java 8 compliant.
- **NFR-002**: Unit/Integration tests MUST continue satisfying the project-wide 90% JaCoCo gate.
- **NFR-003**: The core MUST remain declaration-agnostic and framework-agnostic.
- **NFR-004**: The audit MUST prefer YAGNI over speculative abstraction.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: `mvn verify` passes without changing the existing public behavior of the current core modules.
- **SC-002**: The `001-core-extensibility` documentation contains only APIs that actually exist in the codebase.
- **SC-003**: Static analysis shows no new dependencies on `IdentificadorEmpregador`; only the legacy compatibility implementations remain.
- **SC-004**: Consumer-facing examples show composition using current public APIs rather than nonexistent internal registries or SPIs.

## Assumptions

- The existing defaults in `domain`, `format`, `crypto`, and `xml` are already sufficient for the current release scope.
- Not every opinionated helper in the core needs an internal SPI; some extension points belong in consuming modules instead.
- If future reuse across multiple projects proves the need, dedicated extensibility hooks can be introduced later in a separate feature.
