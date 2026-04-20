# Feature Specification: Core Extensibility and Composition

**Feature Branch**: `feature/001-core-extensibility`
**Created**: 2026-04-20
**Status**: Draft
**Input**: User description: "Auditoria e ajustes do core atual para garantir composição e extensibilidade sem quebrar defaults"

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Custom Validator Chain (Priority: P1)

As a developer integrating a specific fiscal layout, I need to compose a custom chain of validators using a ValidatorRegistry without modifying the default GovValidators, so that I can apply domain-specific rules on top of standard validation.

**Why this priority**: Opening the validation chain is the primary rigidity point identified in the audit.
**Independent Test**: Can be fully tested by instantiating a custom registry with a mocked validator and asserting it executes without affecting the static `GovValidators` behavior.

**Acceptance Scenarios**:
1. **Given** a new ValidatorRegistry instance, **When** I register a custom NisValidator, **Then** the registry uses my custom validator for NIS checks.
2. **Given** existing code calling `GovValidators.isNisStructureValid()`, **When** executed, **Then** it still uses the default validation logic (backward compatibility).

---

### User Story 2 - Decouple from Deprecated IdentificadorEmpregador (Priority: P2)

As a developer maintaining the domain models, I need to ensure new integrations do not rely on the deprecated `IdentificadorEmpregador` interface, so that we can safely phase it out in the future without breaking new flows.

**Why this priority**: Prevents technical debt accumulation in new features.
**Independent Test**: Can be tested by verifying that core domain objects (Cnpj, Cpf, etc.) can be used in validation flows without requiring casts or references to `IdentificadorEmpregador`.

**Acceptance Scenarios**:
1. **Given** a validation flow requiring an employer identifier, **When** passing a `Cnpj` object, **Then** the validation succeeds via a generic or composable contract, not the deprecated interface.

---

### User Story 3 - Extensible Format and Crypto Helpers (Priority: P3)

As a developer consuming the core library, I need to inject custom configurations into JSON serialization and SSL contexts via interfaces/factories, so that I am not locked into the opinionated utility defaults.

**Why this priority**: Improves flexibility for edge cases (e.g., custom trust stores or specific date formats).
**Independent Test**: Can be tested by injecting a custom `CertificateProvider` into a TLS setup flow and verifying it is called.

**Acceptance Scenarios**:
1. **Given** a requirement for a custom SSL context, **When** I provide a custom implementation of the new SSL factory interface, **Then** the crypto layer uses my custom context.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a `ValidatorRegistry` or `ValidatorComposer` that allows registering and executing validation chains.
- **FR-002**: `GovValidators` and `Nis` MUST be refactored to use the registry internally while preserving their existing public static signatures.
- **FR-003**: System MUST expose interfaces or factories for JSON serialization (`GovJsonFactory` equivalents) and SSL context building (`SslContextBuilder` equivalents) to allow dependency injection.
- **FR-004**: System MUST NOT alter the behavior of existing public methods (Minor or Patch release scope).

### Key Entities

- **ValidatorRegistry**: A composable registry that holds and executes a chain of validators for domain objects.
- **Domain Value Objects**: Cnpj, Cpf, Nis, etc. (existing).

### Non-Functional Requirements

- **NFR-001**: Implementation MUST be strictly Java 8 compliant.
- **NFR-002**: Unit/Integration tests MUST maintain project-wide 90% JaCoCo coverage.
- **NFR-003**: All workflow steps MUST sync state to `MEMORY.md` and agent-local memory.
- **NFR-004**: Code MUST remain declaration-agnostic and framework-agnostic.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of existing unit tests pass without modification (ensuring absolute backward compatibility).
- **SC-002**: The new `ValidatorRegistry` can be instantiated and executed independently from static contexts.
- **SC-003**: JaCoCo reports at least 90% line and branch coverage for the new registry and factory classes.
- **SC-004**: Static analysis shows 0 new dependencies on `IdentificadorEmpregador`.

## Assumptions

- The existing `GovValidators` rules are correct and only the composition mechanism needs changing.
- Consumers of the library rely heavily on the static methods, hence the strict backward compatibility requirement.
