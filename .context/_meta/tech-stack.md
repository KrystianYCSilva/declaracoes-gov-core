---
name: tech-stack
description: |
  Technology stack and runtime dependencies for declaracoes-gov-core.
  Use when: onboarding, debugging, or planning changes.
---

# Technology Stack

## Core Platform

| Component | Version | Purpose |
|-----------|---------|---------|
| Java | 8+ | Source and target compatibility |
| Maven | 3.9+ | Build and dependency management |

## Required Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Apache XML Security (xmlsec) | 3.0.3 | XML digital signature implementation |
| SLF4J API | 2.0.9 | Logging facade |
| JSR-305 annotations | 3.0.2 | Nullability annotations (@Nonnull, @Nullable) |

## Optional Dependencies

| Library | Version | Purpose | Scope |
|---------|---------|---------|-------|
| Jackson Databind | 2.16.1 | JSON serialization | provided |
| Jackson JSR310 | 2.16.1 | Java 8 date/time support | provided |
| Apache HttpClient 5 | 5.3 | HTTP client with mTLS | provided |
| Apache HttpCore 5 | 5.2.4 | HTTP core components | provided |
| Caffeine | 3.1.8 | High-performance caching | provided |

## Test Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| JUnit | 4.13.2 | Unit testing framework |
| Mockito | 4.11.0 | Mocking framework |
| BouncyCastle (bcpkix-jdk15on) | 1.70 | Test certificate and crypto utilities |

## Build Tools

| Plugin | Purpose |
|--------|---------|
| Maven Compiler | Java compilation (source/target 8) |
| Maven Surefire | Test execution |
| JaCoCo | Code coverage reporting and enforcement (0.8.11) |
| Maven Source | Source JAR generation |
| Maven Javadoc | Javadoc JAR generation |

## Cryptographic Standards

### XML Signature
- Algorithm: RSA-SHA256
- Digest: SHA-256
- Canonicalization: C14N (inclusive)
- Transform: Enveloped Signature
- Provider: Apache XML Security

### Certificate Standards
- Authority: ICP-Brasil
- Types: A1 (file), A3 (hardware)
- Formats: PKCS#12 (.p12/.pfx), PKCS#11 (HSM/token)
- Key size: 2048+ bits RSA

### Validation Algorithms
- CNPJ: Modulo 11 (numeric + alphanumeric ASCII-48)
- CPF: Modulo 11
- IE: State-specific algorithms

## Thread Safety Guarantees

| Component | Thread Safety | Mechanism |
|-----------|---------------|-----------|
| CertificadoManager | Yes | Immutable state |
| XmlSigner | Yes | Stateless, thread-safe factories |
| CNPJ/CPF validators | Yes | Stateless |
| JsonMapper | Yes | ObjectMapper is thread-safe after config |
| MtlsConnectionFactory | Yes | Stateless |

## Performance Targets

| Operation | Target |
|-----------|--------|
| Certificate loading (A1) | < 100ms |
| Certificate loading (A3) | < 500ms |
| XML signature | < 10ms |
| CNPJ validation | < 1ms |
| JSON serialization (small) | < 5ms |

## Compatibility Matrix

| Consumer | Min Version | Compatibility |
|----------|-------------|---------------|
| declaracoes-esocial-* | 1.0.0 | Full |
| declaracoes-efd-reinf-* | 1.0.0 | Full |
| declaracoes-serpro-* | 1.0.0 | Full |
| Java 8 runtime | - | Full |
| Java 11+ runtime | - | Full |
| Java 17+ runtime | - | Full |
