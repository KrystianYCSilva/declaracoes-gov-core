# Qwen Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific Qwen guidance.

## Baseline

- Project: `declaracoes-gov-core`
- Java 8+ Maven library
- Thread-safe, framework-agnostic
- Domain: Brazilian tax declarations

## Guardrails

- Keep secrets out of Git.
- Keep public APIs stable.
- Maintain Java 8 compatibility.
- Keep `mvn verify` green.

## Focus Areas

### Certificate Management
- A1 certificates: P12/PFX file-based loading
- A3 certificates: PKCS#11 hardware token support
- ICP-Brasil chain validation
- mTLS SSLContext factory for HTTP clients

### XML Digital Signature
- eSocial/EFD-Reinf compliant
- RSA-SHA256, SHA-256, C14N, Enveloped
- Apache XML Security library
- Remove xsi/xsd namespaces before signing

### Document Validators
- CNPJ: Support numeric (current) and alphanumeric (2026+)
- CPF: Modulo 11 with rejection of identical digits
- IE: State-specific validation rules

## Code Style

- Explicit types, no `var`
- `final` fields preferred
- Immutable objects
- Builder pattern for complex construction
- Comprehensive Javadoc

## Testing

- JUnit 5 + Mockito
- 80% line, 75% branch coverage
- Thread-safety tests
- AssertJ assertions
