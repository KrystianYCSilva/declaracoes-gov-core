---
name: declaracoes-gov-core
description: |
  Shared Java 11 foundation library for the declaracoes-* workspace.
  Use when: any AI agent starts a session in this repository.
---

# declaracoes-gov-core

## What This Project Is

Shared Java 11 foundation library for the `declaracoes-*` workspace. It provides immutable fiscal identifiers, a public validator confidence policy, text/number/date/JSON formatting utilities, secure XML parsing and XMLDSIG signing, certificate/SSLContext abstractions, and a neutral HTTP transport SPI for the Brazilian government declarations ecosystem.

- **GroupId:** `br.com.contabilizei.obrigacoes`
- **ArtifactId:** `declaracoes-gov-core-parent`
- **Version:** `1.1.0-SNAPSHOT`
- **Packaging:** `pom` (Maven reactor parent)
- **Base package:** `br.com.contabilizei.obrigacoes.govcore`

## Tech Stack

- **Java 11** (`maven.compiler.release` `11`)
- **Maven 3.x** multi-module reactor
- **JUnit 4.13.2** for unit tests
- **Mockito 4.11.0** for mocking
- **JaCoCo 0.8.11** for coverage gating
- **BouncyCastle** `bcpkix-jdk18on` `1.78.1` (test scope)
- **Apache Santuario xmlsec** `3.0.3` (restricted to the `xml` module)
- **Jackson** `2.16.1` (`jackson-databind`, `jackson-datatype-jsr310` — optional, restricted to the `format` module)
- **maven-compiler-plugin** `3.12.1`
- **maven-jar-plugin** `3.4.2` (used in `crypto` to publish a `test-jar`)

## Module Map

| Module | Type | Dependencies | Key Contents |
|--------|------|--------------|--------------|
| `declaracoes-gov-core-bom` | `pom` | none | Internal BOM for version alignment of all core modules. No source code. JaCoCo skipped. |
| `declaracoes-gov-core-domain` | `jar` | JDK only | Immutable value objects (`Cnpj`, `Cpf`, `Nis`, `Caepf`, `Cno`, `Cei`, `CodigoMunicipio`, `PeriodoApuracao`, `Vigencia`, `Recibo`), enums/tables (`Uf`, `TipoInscricao`, `TipoAmbiente`), layout metadata (`LayoutVersion`, `RecordDefinition`, `FieldDefinition`, ...), exception hierarchy (`GovCoreException`, `InvalidDocumentException`, `GovSecurityException`, ...), and validator catalog (`GovValidators`, `GovValidationCatalog`, `ValidationLevel`, `Modulo11`). |
| `declaracoes-gov-core-format` | `jar` | `domain` + optional Jackson | `GovTextNormalizer`, `GovNumberFormats`, `GovCompetenceFormats`, `XmlDates`, `GovJsonFactory`, delimited/fixed-length parsers and serializers. |
| `declaracoes-gov-core-crypto` | `jar` | `domain` | `CertificateProvider`, `AbstractKeyStoreProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder`. Publishes a `test-jar` for reuse by `xml` tests. |
| `declaracoes-gov-core-xml` | `jar` | `domain`, `crypto`, `xmlsec`, `crypto` `test-jar` (tests) | `XmlDocuments` (secure DOM parsing), `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions`. |
| `declaracoes-gov-core-transport` | `jar` | `crypto`, Apache HttpClient 5, WireMock (tests) | `HttpRequest`, `HttpResponse`, `RestTransport`, `RetryPolicy`, `ProxyConfig`, `TransportException`, `ApacheHttpClientRestTransport`. |

### Dependency Rules

- `domain` must remain independent (JDK only).
- `format` may depend on `domain`.
- `crypto` may depend on `domain`.
- `xml` may depend on `domain` and `crypto`.
- `transport` may depend on `crypto` and its own isolated HTTP stack.
- Heavy dependencies (Jackson, xmlsec) must not leak outside their owning module.

## Build and Test Commands

### Full reactor validation (compilation + tests + JaCoCo gates)

```bash
mvn -q verify
```

### Run a single module

```bash
cd declaracoes-gov-core-domain && mvn -q verify
```

### Skip JaCoCo (not recommended for CI)

```bash
mvn verify -Djacoco.skip=true
```

## Code Style Guidelines

### Language & Documentation

- **Javadoc and inline comments** are written primarily in **Portuguese** (Brazilian government domain vocabulary).
- **AI-facing documentation** (this file, `.context/`) is written in **English**.

### Design Conventions

- **Immutability:** All value objects are `final` with private constructors and static factory methods (e.g., `Cnpj.of("...")`).
- **Statelessness:** Validators and utilities are stateless.
- **No frameworks:** The core is declaration-agnostic and framework-agnostic. **No Spring, no Jakarta EE, no Bean Validation, no Lombok.**
- **Serializable:** Value objects declare `private static final long serialVersionUID = 1L` when implementing `Serializable`.
- **Explicit APIs:** Prefer explicit options objects over heuristics (e.g., `XmlSignatureOptions`).
- **Null safety:** Factory methods fail-fast with domain-specific exceptions (`InvalidDocumentException`) rather than returning `null`.

### Package Organization

```
br.com.contabilizei.obrigacoes.govcore
├── model              # Fiscal value objects (Cnpj, Cpf, Nis, ...)
├── model/layout       # Layout metadata (RecordDefinition, FieldDefinition, ...)
├── table              # Stable enums (Uf, TipoInscricao, TipoAmbiente)
├── validator          # GovValidators, GovValidationCatalog, Modulo11, concrete validators
├── exception          # GovCoreException and derivatives
├── util               # Formatting, JSON, XML date helpers
├── format/parser      # DelimitedParser, FixedLengthParser, serializers
├── crypto             # CertificateProvider, Pkcs12Provider, Pkcs11Provider, SslContextBuilder
└── signature          # XmlSigner, XmlDsigSigner, XmlSignatureOptions
```

## Testing Instructions

### Test Framework

- **JUnit 4** (`@Test`, `@Test(expected = ...)`, `Assert.*`).
- **Mockito** for collaborator mocking.
- Test classes are named `*Test.java` and live in `src/test/java` mirroring the main package structure.

### Coverage Gates (JaCoCo)

| Module | Line Minimum | Branch Minimum |
|--------|-------------|----------------|
| `domain`, `format`, `xml`, `transport` | 90% | 90% |
| `crypto` | 85% | 90% |
| `bom` | skipped | skipped |

### Notable Testing Caveats

- **PKCS#11 / A3 tests** are limited to what can be simulated without real hardware.
- **Negative XML parser tests** may emit messages to `stderr` by design; this does not indicate a build failure.
- The `crypto` module publishes a `test-jar` consumed by `xml` tests for shared certificate test support.

## Security Considerations

### Certificate and Key Handling

- **Never commit real certificates or private keys.**
- `.gitignore` explicitly blocks: `*.pem`, `*.p12`, `*.pfx`, `keystore/`, `truststore/`, `certs/`, `secret/`, `.env*`.
- `Pkcs11Provider` and `Pkcs12Provider` abstract A3 and A1 certificate access; runtime behavior depends on the consumer's environment and native drivers.

### XML Security

- `XmlDocuments` parses XML securely with **XXE protections** enabled.
- XMLDSIG defaults: **RSA-SHA256**, **SHA-256 digest**, **inclusive canonicalization**, **enveloped transform**.
- Signature target selection is explicit via `XmlSignatureOptions` (element local name + ID attribute), never heuristic.

### Validation Confidence Model

The core publishes three validation levels to prevent market heuristics from being sold as official validation:

- `OFFICIAL` — fail-fast strong validation backed by a catalogued government source (e.g., CNPJ).
- `PROVISIONAL` — algorithm available but primary source not yet fully catalogued; must be **opt-in** (e.g., `Cpf.ofProvisionallyValidated(...)`).
- `STRUCTURAL` — normalization, length, and basic form only (e.g., CAEPF, CNO, CEI).

## Deployment and Consumption

### Internal BOM Import

Consumers should import the BOM and then pick only the modules they need:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>br.com.contabilizei.obrigacoes</groupId>
            <artifactId>declaracoes-gov-core-bom</artifactId>
            <version>1.1.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### Publication Order

1. Align versions in parent and all child POMs.
2. Run `mvn verify`.
3. Publish concrete JARs (`domain`, `format`, `crypto`, `xml`, `transport`).
4. Publish `declaracoes-gov-core-bom` last.

## What Is Out of Scope

The following must **never** be added to this repository:

- HTTP/SOAP/REST clients or transport logic (except a neutral, composition-first transport SPI as `core-transport`; see AR-001 exception and ADR-008)
- OAuth2, token handling, queues, polling, or delivery orchestration
- Declaration-specific business rules (e.g., eSocial, EFD-Reinf payloads)
- Code generated from official government XSDs/Schemas
- Endpoint catalogues, retry policies, or rate-limiting

## Core Reminders

- Keep the core framework-agnostic and declaration-agnostic.
- This is Maven multi-module core library; live implementation is in `declaracoes-gov-core-*/src` and the related `pom.xml` files.

## Memory Model

- `MEMORY.md` — shared long-term memory. Read at session start; update at task start/finish.
  - `Active`: current topic + owning agent + status (one line).
  - `Completed`: finished work with brief summary and date (one line).
- `<agent-dir>/memory/agent-local-memory.md` — private scratchpad for the agent currently working.
- Rules: never delete another agent's active row; keep entries minimal; this file is for cross-session recovery, not live session context.

## Key Files for Context

- `pom.xml` — Parent reactor: versions, plugins, JaCoCo gates, dependency management.
- `README.md` — Human-oriented quick start and scope (Portuguese).
- `docs/01-REQUISITOS.md` — Functional and non-functional requirements.
- `docs/02-DESIGN.md` — Package map and architectural decisions.
- `docs/03-PLANO-TESTES.md` — Test plan and gate definitions.
- `docs/04-IMPLANTACAO.md` — Deployment and consumption guide.
- `docs/05-MATRIZ-VALIDADORES.md` — Validator confidence matrix (source of truth for `GovValidationCatalog`).
- `.context/README.md` — AI context navigation hub (Tier system, load order).
- `.context/ai-assistant-guide.md` — Full AI protocol (bootstrap, request routing, Definition of Done).
- `.context/standards/architectural-rules.md` — T0 enforceable rules (AR-001 through AR-009).
- `.context/standards/code-quality.md` — T1 design conventions and package organization.
- `.context/standards/testing-strategy.md` — T1 test framework, coverage gates, patterns.
- `.context/_meta/project-overview.md` — T2 project identity, scope, module map, boundaries.
- `.context/_meta/tech-stack.md` — T2 exact dependency/plugin versions and constraints.
- `.context/_meta/key-decisions.md` — T2 consolidated ADRs (Java 11 baseline, framework-agnostic, confidence model, etc.).
- `.context/patterns/architecture.md` — T1 design blueprints (value objects, validator tiers, options objects).
- `.context/knowledge/domain-concepts.md` — T3 Brazilian fiscal identifiers, Modulo 11, XMLDSIG profile.
- `.context/workflows/development-workflows.md` — T2 build, test, publish, and troubleshooting flows.
