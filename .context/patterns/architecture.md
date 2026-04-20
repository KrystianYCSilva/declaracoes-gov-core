---
description: |
  T1 design blueprints and structural patterns for declaracoes-gov-core.
  Use when: designing new features or understanding how components interact.
---

# Architecture Patterns — declaracoes-gov-core

## Pattern: Immutable Value Object

**Applicability**: All fiscal identifiers and domain values.

**Structure**:
```java
public final class Cnpj implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String value;

    private Cnpj(String value) { this.value = value; }

    public static Cnpj of(String raw) {
        if (raw == null) throw new InvalidDocumentException("CNPJ cannot be null");
        String normalized = GovTextNormalizer.digitsOnly(raw);
        GovValidators.cnpj().validate(normalized);
        return new Cnpj(normalized);
    }

    // equals, hashCode, toString
}
```

**Rules**:
- Private constructor.
- Static factory with validation.
- No setters.
- `Serializable` with `serialVersionUID = 1L`.

## Pattern: Validator Confidence Tier

**Applicability**: Document validation (CPF, CNPJ, NIS, etc.).

**Structure**:
```java
public enum ValidationLevel { OFFICIAL, PROVISIONAL, STRUCTURAL }

// OFFICIAL — default, fail-fast
Cpf cpf = Cpf.of("12345678909");

// PROVISIONAL — explicit opt-in
Cpf cpf = Cpf.ofProvisionallyValidated("12345678909");

// STRUCTURAL — basic form only
String normalized = GovValidators.structural().normalize("123.456.789-09");
```

## Pattern: Explicit Options Object

**Applicability**: Complex operations with multiple parameters (XML signing).

**Structure**:
```java
XmlSignatureOptions options = XmlSignatureOptions.builder()
    .targetElementLocalName("evento")
    .targetIdAttribute("Id")
    .build();
```

**Rules**:
- Never use heuristics to select targets.
- All configurable aspects are explicit in the options object.

## Pattern: Certificate Provider Abstraction

**Applicability**: A1 (file) and A3 (hardware) certificate access.

**Structure**:
```java
CertificateProvider provider = new Pkcs12Provider(path, password);
// or
CertificateProvider provider = new Pkcs11Provider(libraryPath, slot, pin);
```

**Rules**:
- Consumer code depends on `CertificateProvider` interface only.
- Never commit real certificates or keys.
- Runtime behavior depends on consumer environment and native drivers.

## Pattern: Module-Local Heavy Dependencies

**Applicability**: Jackson (format), xmlsec (xml).

**Rules**:
- Heavy dependencies are declared in the owning module only.
- They must not leak to `domain` or other modules.
- Consumers import the BOM and pick only the modules they need.

## Pattern: Test-Jar Sharing

**Applicability**: `crypto` → `xml` test fixtures.

**Rules**:
- `crypto` publishes a `test-jar` via `maven-jar-plugin`.
- `xml` tests declare a `test` scope dependency on the crypto test-jar.
- Keep test fixtures stateless and reusable.
