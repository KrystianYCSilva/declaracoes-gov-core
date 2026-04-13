---
name: testing-strategy
description: |
  Testing strategy and coverage facts for declaracoes-gov-core.
  Use when: changing behavior, coverage, or test scope in the core modules.
---

# Testing Strategy

## Test Scope by Module

| Module | Main Test Focus |
| --- | --- |
| `declaracoes-gov-core-domain` | Value objects, validator confidence catalog, checksum helpers, exceptions, and tables |
| `declaracoes-gov-core-format` | Text/number/date helpers, government JSON mapper defaults, fixed/delimited parser and serializer behavior |
| `declaracoes-gov-core-xml` | Secure DOM parsing, signature options, XMLDSIG signing behavior |
| `declaracoes-gov-core-crypto` | PKCS12 / PKCS11 providers, alias discovery, certificate extraction, and `SslContextBuilder` |
| `declaracoes-gov-core-bom` | No source or tests; JaCoCo skipped because it is POM-only |

## Tooling Actually Used

- JUnit `4.13.2`
- Mockito `4.11.0` when mocking is needed
- BouncyCastle `1.70` in test scope
- Standard `org.junit.Assert` assertions already used throughout the current repository tests

## Locations and Naming

- Each child module keeps tests under `src/test/java`, mirroring the package layout of `src/main/java`.
- Test classes use the repository's current `*Test.java` convention.
- There is no generated source tree in this repository today, so generated-code coverage exclusions are not part of the current testing strategy.

## Coverage Gates from the Live Build

- Parent defaults in the root `pom.xml`:
  - line coverage: `90%`
  - branch coverage: `90%`
- `declaracoes-gov-core-crypto` overrides only the line threshold to `85%`.
- `declaracoes-gov-core-bom` sets `jacoco.skip=true` because it is a BOM module.

## Execution Commands

- Full reactor tests: `mvn -q test`
- Release-level validation: `mvn -q verify`
- Focused module run example: `mvn -q -pl declaracoes-gov-core-xml test`

## Repository-Specific Constraints

- PKCS11 / A3 tests exercise failure handling and provider initialization without depending on a real token in the repository.
- XML negative tests may emit secure-parser diagnostics to stderr while still representing expected failure cases.
- Validator-policy changes must keep `docs/03-PLANO-TESTES.md` and `docs/05-MATRIZ-VALIDADORES.md` synchronized.

## Debugging Failed Tests

1. Re-run the affected module or class with Maven.
2. Confirm whether the failure is in `domain`, `format`, `xml`, or `crypto`; do not debug from the wrong module.
3. Check validator-policy changes against `GovValidationCatalog` and the published matrix before changing expected outcomes.
4. For PKCS11 failures, verify that the test still targets error handling rather than real hardware behavior.
