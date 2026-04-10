---
name: architectural-rules
description: |
  Tier T0 rules that define the non-negotiable architecture and security boundaries of declaracoes-gov-core.
  Use when: proposing, reviewing, or implementing any change in this repository.
---

# Architectural Rules

> Tier: T0 - ABSOLUTE. Every change must comply with these rules.

## `AR-001` Keep Library Agnostic

Rule:
- This is a library, not an application.
- Do not introduce framework-specific dependencies (Spring, Jakarta EE, Quarkus, Micronaut, etc.) in core modules.
- Keep the public API framework-agnostic.
- Integration with frameworks should happen in separate adapter modules or be left to consumers.

Rationale:
- This library is used by multiple applications with different technology stacks.
- Framework dependencies create version conflicts and classpath hell.

## `AR-002` Thread Safety

Rule:
- All public classes must be thread-safe.
- Prefer immutable objects.
- Document thread-safety guarantees in class-level Javadoc with `@apiNote`.
- Use `ReadWriteLock` for shared mutable state that requires high concurrency.
- Use `Atomic*` classes for simple counters/flags.
- ObjectMapper (Jackson) and XMLSignatureFactory are thread-safe after configuration - cache and reuse.

Example:
```java
/**
 * Thread-safe certificate manager for ICP-Brasil certificates.
 * 
 * @apiNote This class is thread-safe. All methods can be called concurrently
 *          from multiple threads without external synchronization.
 */
public final class P12CertificadoManager implements CertificadoManager {
    private final X509Certificate certificate; // immutable
    private final PrivateKey privateKey;       // immutable
    // ...
}
```

## `AR-003` Java 8 Compatibility

Rule:
- Source and target compatibility must remain at Java 8.
- Do not use Java 9+ features:
  - `var` keyword
  - New `Optional` methods (`ifPresentOrElse`, `or`, `stream`)
  - New `Stream` collectors
  - Private interface methods
  - New `Map`/`List`/`Set` factory methods
- Use explicit types instead of `var`.
- Use Guava or Apache Commons for pre-Java 9 utilities if needed.

## `AR-004` Dependency Minimalism

Rule:
- Keep external dependencies to a minimum.
- Required dependencies: Apache XML Security, SLF4J API.
- Optional dependencies: Jackson, Apache HttpClient, Caffeine - mark with `<optional>true</optional>`.
- Provide SPI (Service Provider Interface) for extensibility rather than direct integration.
- Avoid transitive dependency conflicts - use `<exclusions>` when necessary.

Dependency categories:
- **Required**: `xmlsec`, `slf4j-api`
- **Optional**: `jackson-*`, `httpclient5`, `caffeine`
- **Test only**: `junit-jupiter`, `mockito`, `assertj`

## `AR-005` No Secrets in Git

Rule:
- Never commit certificates, keys, or credentials.
- Never embed test credentials in source code.
- Use test keystores only for unit tests with dummy credentials.
- Real certificates for integration tests must be loaded from environment variables or files outside the repository.
- `.gitignore` must include: `*.p12`, `*.pfx`, `*.pem`, `secret/`, `keystore/`, `certs/`.

## `AR-006` Preserve Public API Stability

Rule:
- Do not break backward compatibility in public APIs without major version bump.
- Use `@Deprecated` with clear migration path and Javadoc `@deprecated` tag before removal.
- Semantic versioning: MAJOR.MINOR.PATCH
  - MAJOR: breaking changes
  - MINOR: new features, backward compatible
  - PATCH: bug fixes, backward compatible
- Keep deprecated methods for at least one minor version.

## `AR-007` Document Public APIs

Rule:
- All public classes and methods must have Javadoc.
- Include thread-safety notes in class-level Javadoc with `@apiNote`.
- Document preconditions, postconditions, and exceptions thrown.
- Document parameter and return value contracts (nullability, ranges, formats).

## `AR-008` Certificate Security

Rule:
- Support both A1 (file-based .p12/.pfx) and A3 (hardware token PKCS#11) certificates.
- Validate ICP-Brasil certificate chain.
- Check certificate revocation (CRL/OCSP) where applicable.
- Never log certificate private keys or passwords.
- Clear password arrays immediately after use (`Arrays.fill(password, '0')`).

## `AR-009` XML Signature Compliance

Rule:
- XML signature must comply with eSocial/EFD-Reinf specifications.
- Required algorithms:
  - Signature: RSA-SHA256 (`http://www.w3.org/2001/04/xmldsig-more#rsa-sha256`)
  - Digest: SHA-256 (`http://www.w3.org/2001/04/xmlenc#sha256`)
  - Canonicalization: C14N (`http://www.w3.org/TR/2001/REC-xml-c14n-20010315`)
  - Transform: Enveloped (`http://www.w3.org/2000/09/xmldsig#enveloped-signature`)
- Include only EndCertOnly (user certificate only, not full chain).
- Remove `xmlns:xsi` and `xmlns:xsd` before signing (eSocial requirement).

## `AR-010` Document Validator Accuracy

Rule:
- CNPJ validator must implement both numeric (current) and alphanumeric (2026+) formats.
- CPF validator must reject all-identical digits (111.111.111-11 is invalid).
- IE validators must implement state-specific rules per Receita Federal specification.
- Document validators must fail fast with clear error messages.

## `AR-011` JSON Thread Safety

Rule:
- Jackson ObjectMapper is thread-safe after configuration - configure once, reuse.
- Provide default configuration in factory/builder pattern.
- Allow consumers to customize without breaking thread safety.
- Use `CopyOnWriteArrayList` for dynamic configuration if needed.

## `AR-012` Keep Context In Sync

Rule:
- If code, runtime behavior, tests, or public APIs change, update the affected Portuguese human docs in `docs/` and the affected English AI docs in `.context/`.
- Do not leave `.context/` describing behavior that is no longer true in `src/`.
- Synchronization order: `src/` → `docs/` → `.context/`.
