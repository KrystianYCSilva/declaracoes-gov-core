# Tasks: Core Audit and Composition Boundaries

**Input**: Design documents from `specs/001-core-extensibility/`
**Prerequisites**: plan.md, spec.md

## Phase 1: Audit the Existing Core Surface

- [x] T001 Verify Java 8 baseline and JaCoCo 90% gates in the root `pom.xml`
- [x] T002 Audit the public APIs of `GovValidators`, `Nis`, `GovJsonFactory`, `SslContextBuilder`, and the XML helpers
- [x] T003 Compare the feature artifacts with the shipped code and identify nonexistent APIs or stale promises

---

## Phase 2: Recalibrate Documentation to the Real Release Scope

- [x] T004 Rewrite `spec.md` to reflect the current release-ready defaults and the decision to defer speculative internal SPIs
- [x] T005 Rewrite `plan.md` to preserve module boundaries and consumer-side composition
- [x] T006 Rewrite `quickstart.md` so it demonstrates only existing public APIs
- [x] T007 Rewrite `research.md`, `data-model.md`, and `contracts/spi-factories.md` to record deferred internals instead of nonexistent implementations

---

## Phase 3: Guardrails Around Deprecated Contracts

- [x] T008 Verify no new validators or helpers were added around `IdentificadorEmpregador`
- [x] T009 Record that existing compatibility implementations remain (`Cnpj`, `Cpf`) and no new abstractions should depend on the deprecated interface

---

## Phase 4: Validation

- [x] T010 Run `mvn verify` to ensure the recalibrated feature still reflects a green build
- [x] T011 Review `specs/001-core-extensibility` for consistency after the recalibration

## Notes

- `ValidatorRegistry`, `SslContextFactory`, `DefaultSslContextFactory`, and mutable `GovJsonFactory` hooks were removed from this feature scope because they are not shipped and are not required for the current release.
- If reuse across multiple declarations later proves the need for those extension points, they should be introduced in a new dedicated feature with matching code, tests, and adoption plan.
