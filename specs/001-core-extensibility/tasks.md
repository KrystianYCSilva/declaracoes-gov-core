# Tasks: Core Extensibility and Composition

**Input**: Design documents from `specs/001-core-extensibility/`
**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [x] T001 Initialize agent-local-memory.md in .gemini/memory/
- [x] T002 Update root MEMORY.md with active task status for 001-core-extensibility
- [x] T003 [P] Verify Java 8 baseline and JaCoCo 90% gates in root pom.xml

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [x] T004 Create `ValidatorRegistry` singleton in `declaracoes-gov-core-domain` (src/main/java/br/uem/npd/govcore/validator/ValidatorRegistry.java)
- [x] T005 Implement base `Validator` interface (or reuse existing) for the registry (src/main/java/br/uem/npd/govcore/validator/Validator.java)
- [x] T006 [P] Create `SslContextFactory` SPI in `declaracoes-gov-core-crypto` (src/main/java/br/uem/npd/govcore/crypto/SslContextFactory.java)
- [x] T007 [P] Create `GovJsonFactoryInterface` SPI/Interface in `declaracoes-gov-core-format` (src/main/java/br/uem/npd/govcore/util/GovJsonFactoryInterface.java)

---

## Phase 3: User Story 1 - Custom Validator Chain (Priority: P1) 🎯 MVP

**Goal**: Allow composing custom validator chains via the registry while maintaining static compatibility.

**Independent Test**: Instantiate `ValidatorRegistry`, register a custom NIS validator, and verify it is used. Verify `GovValidators` still works as before.

- [x] T008 [P] [US1] Create unit test for `ValidatorRegistry` (src/test/java/br/uem/npd/govcore/validator/ValidatorRegistryTest.java)
- [x] T009 [P] [US1] Add integration test for custom chain validation in `ValidatorRegistryTest.java`
- [x] T010 [US1] Refactor `GovValidators` to delegate NIS validation to `ValidatorRegistry` (src/main/java/br/uem/npd/govcore/validator/GovValidators.java)
- [x] T011 [US1] Refactor `Nis` model to use `ValidatorRegistry` instead of direct instantiation (src/main/java/br/uem/npd/govcore/model/Nis.java)
- [x] T012 [US1] Verify backward compatibility with `GovValidatorsTest.java`

**Checkpoint**: User Story 1 complete - Validation is now extensible.

---

## Phase 4: User Story 2 - Decouple from Deprecated IdentificadorEmpregador (Priority: P2)

**Goal**: Ensure validation flows do not depend on the deprecated interface.

**Independent Test**: Execute a validation flow using a `Cnpj` object without referencing `IdentificadorEmpregador`.

- [x] T013 [P] [US2] Create unit tests verifying generic validation behavior without IdentificadorEmpregador (src/test/java/br/uem/npd/govcore/validator/GenericValidatorTest.java)
- [x] T014 [US2] Update `Validator` contracts to accept generic identifiers (src/main/java/br/uem/npd/govcore/validator/Validator.java)
- [x] T015 [US2] Refactor `CnpjValidator` and `CpfValidator` to use the updated contracts (src/main/java/br/uem/npd/govcore/validator/)
- [x] T016 [US2] Verify no new usages of `IdentificadorEmpregador` exist in the `domain` module using `grep -rn "IdentificadorEmpregador"`
- [x] T017 [US2] Update `ExceptionsTest.java` to ensure no regressions in validation error reporting

**Checkpoint**: User Story 2 complete - Technical debt related to `IdentificadorEmpregador` reduced.

---

## Phase 5: User Story 3 - Extensible Format and Crypto Helpers (Priority: P3)

**Goal**: Inject custom configurations into JSON and SSL factories.

**Independent Test**: Inject a custom SSL factory and verify its context is used by `SslContextBuilder`.

- [x] T018 [P] [US3] Add unit tests for `SslContextBuilder` with custom factories (src/test/java/br/uem/npd/govcore/crypto/SslContextBuilderTest.java)
- [x] T019 [P] [US3] Add unit tests for `GovJsonFactory` custom mapper (src/test/java/br/uem/npd/govcore/util/GovJsonFactoryTest.java)
- [x] T020 [US3] Implement default `SslContextFactory` in `declaracoes-gov-core-crypto` (src/main/java/br/uem/npd/govcore/crypto/DefaultSslContextFactory.java)
- [x] T021 [US3] Refactor `SslContextBuilder` to accept an optional `SslContextFactory` (src/main/java/br/uem/npd/govcore/crypto/SslContextBuilder.java)
- [x] T022 [US3] Refactor `GovJsonFactory` to allow providing a custom `ObjectMapper` via a new setter/factory (src/main/java/br/uem/npd/govcore/util/GovJsonFactory.java)

**Checkpoint**: User Story 3 complete - Core helpers are now fully extensible.

---

## Phase N: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [x] T023 Update root MEMORY.md to 'completed' status for 001-core-extensibility
- [x] T024 Sync final agent-local-memory.md state
- [x] T025 [P] Final documentation review of `docs/*.md` (Portuguese) and `GEMINI.md` (English)
- [x] T026 Run `mvn verify` to ensure all tests pass and JaCoCo 90% coverage is met

---

## Dependencies & Execution Order

- **Foundational (Phase 2)**: MUST complete before any User Story.
- **User Stories (Phase 3-5)**: Can proceed in parallel after Phase 2, but priority order (P1 -> P2 -> P3) is recommended for sequential delivery.
- **Polish (Final Phase)**: Depends on all user stories.

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Setup and Foundational phases.
2. Implement User Story 1 (ValidatorRegistry).
3. Validate and demo extensibility.
