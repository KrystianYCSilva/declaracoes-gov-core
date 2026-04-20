---
description: |
  T1 testing strategy for declaracoes-gov-core. Framework, coverage gates, test patterns, and module-specific caveats.
  Use when: writing tests, reviewing coverage, or debugging test failures.
---

# Testing Strategy — T1 (NORMATIVE)

## Test Framework

- **JUnit 4** (`@Test`, `@Test(expected = ...)`, `Assert.*`).
- **Mockito** 4.11.0 for collaborator mocking.
- Test classes named `*Test.java` in `src/test/java`, mirroring main package structure.

## Coverage Gates (JaCoCo 0.8.11)

| Module | Line Minimum | Branch Minimum |
|--------|-------------|----------------|
| `domain`, `format`, `xml` | 90% | 90% |
| `crypto` | 85% | 90% |
| `bom` | skipped | skipped |

## Test Patterns

### Value Object Tests
- Valid construction (factory method returns instance).
- Invalid construction (throws `InvalidDocumentException` or derivative).
- Equality and hashCode contracts.
- Serialization round-trip (if `Serializable`).

### Validator Tests
- Official validators: test valid and invalid checksums.
- Structural validators: test normalization, length, character set.
- Edge cases: empty, null, whitespace-only.

### Parser/Serializer Tests
- Round-trip: input → parse → serialize → equals original.
- Delimiter escaping and fixed-length padding.
- Encoding boundaries (UTF-8).

### XML Security Tests
- XXE attack vectors must be rejected.
- Signature verification with known-good and tampered documents.
- Negative parser tests may emit to `stderr` by design (not a build failure).

### Crypto Tests
- PKCS#11 / A3 tests are limited to simulation without real hardware.
- `crypto` publishes a `test-jar` consumed by `xml` tests for shared certificate fixtures.

## Module-Specific Caveats

- **Crypto**: Some tests simulate hardware and may be limited. Do not expect full PKCS#11 coverage.
- **XML**: Negative parser tests may write to `stderr` by design.
- **BOM**: No tests; `jacoco.skip=true`.

## Running Tests

```bash
# Full reactor validation
mvn -q verify

# Single module
cd declaracoes-gov-core-domain && mvn -q verify

# Single test class
mvn -q test -pl declaracoes-gov-core-domain -Dtest=CnpjTest

# Skip JaCoCo (local only)
mvn verify -Djacoco.skip=true
```
