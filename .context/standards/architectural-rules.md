---
description: |
  T0 (Absolute) architectural rules for declaracoes-gov-core.
  Use when: generating, reviewing, or refactoring code. These rules are non-negotiable.
---

# Architectural Rules — T0 (ABSOLUTE)

> **Tier**: T0. ALWAYS follow these rules. If a change conflicts with T0, the change is invalid.

## AR-001 — Keep the Core Declaration-Agnostic

**Rule**: No declaration-specific payload DTOs, schema bundles, or transport concerns (SOAP, REST, OAuth2, HTTP clients) **in the core modules** (`domain`, `format`, `xml`, `crypto`).

> **Exception** (ADR-008): A **neutral transport SPI** (`core-transport`) is permitted as a separate module in the same reactor, provided it is:
> - Declaration-agnostic (no government endpoints, no SOAP/REST contracts, no OAuth2).
> - Composition-first (SPI + default Apache HttpClient 5 implementation).
> - Consumed by declaration-specific transmitters via dependency injection, never the other way around.

// CORRECT
```java
public final class Cnpj implements Serializable { /* fiscal identifier */ }
```

// FORBIDDEN
```java
public class EsocialEnvioLoteRequest { /* payload DTO for a specific declaration */ }
```

## AR-002 — Keep the Core Framework-Agnostic

**Rule**: No Spring, Jakarta EE, Bean Validation, Lombok, or any injection framework. Public APIs must work from plain Java.

// CORRECT
```java
public final class Cpf {
    private final String value;
    private Cpf(String value) { this.value = value; }
    public static Cpf of(String value) { /* validation */ return new Cpf(value); }
}
```

// FORBIDDEN
```java
@Data // Lombok
@Entity // JPA
public class Cpf { }
```

## AR-003 — Preserve Module Boundaries

**Rule**: `domain` is JDK-only. Heavy deps (Jackson, xmlsec) are confined to their owning modules and must not leak.

| Module | Allowed Dependencies |
|--------|---------------------|
| `domain` | JDK only |
| `format` | `domain`, Jackson (optional) |
| `xml` | `domain`, `crypto`, `xmlsec` |
| `crypto` | `domain`, BouncyCastle (test scope) |
| `transport` | `crypto`, HttpClient 5, WireMock (test scope) |
| `bom` | none |

## AR-004 — Keep the Java 11 Baseline

**Rule**: Compile with `maven.compiler.release=11`. No Java 12+ language features or APIs in the `v1.1.x` line.

## AR-005 — Preserve the Validator Confidence Model

**Rule**: Three levels with strict semantics.
- `OFFICIAL` — fail-fast defaults; backed by catalogued government source.
- `PROVISIONAL` — must be explicitly opt-in (e.g., `Cpf.ofProvisionallyValidated(...)`).
- `STRUCTURAL` — normalization, length, basic form only.

## AR-006 — Keep XML Signing Explicit

**Rule**: XMLDSIG envelope is fixed:
- Algorithm: RSA-SHA256
- Digest: SHA-256
- Canonicalization: inclusive
- Transform: enveloped

Use `XmlSignatureOptions` for target selection. Never use heuristics. Preserve XXE protections in `XmlDocuments`.

## AR-007 — Keep Certificate Handling Isolated

**Rule**: Certificate access behind `CertificateProvider`. Preserve A1 (`Pkcs12Provider`) and A3 (`Pkcs11Provider`) paths. Never commit real certificates or private keys. `.gitignore` blocks: `*.pem`, `*.p12`, `*.pfx`, `keystore/`, `truststore/`, `certs/`, `secret/`, `.env*`.

## AR-008 — Build and Coverage Gates

**Rule**: `mvn -q verify` is the validation gate. JaCoCo thresholds:
- `domain`, `format`, `xml`, `transport`: 90% line / 90% branch
- `crypto`: 85% line / 90% branch

## AR-009 — Keep Context in Sync

**Rule**: Source of truth is `pom.xml` and `src/`. Human docs in Portuguese. AI docs in English. Sync order: implementation → Portuguese docs → AI docs.
