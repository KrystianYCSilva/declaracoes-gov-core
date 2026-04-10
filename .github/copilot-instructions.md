# Copilot Instructions

This is a Java library project for Brazilian federal tax declarations.

## Key Points

- Java 8+ compatibility required (no `var`, no Java 9+ features)
- Thread-safe public APIs with comprehensive Javadoc
- Framework-agnostic (no Spring/Jakarta dependencies in core)
- Use JAXB for XML, Jackson for JSON (optional dependency)
- Minimum 80% line coverage, 75% branch coverage
- All public APIs must have Javadoc with thread-safety notes

## Code Style

- Follow Java conventions
- Use immutable objects where possible
- Document thread-safety in class-level Javadoc with `@apiNote`
- Use BigDecimal for all monetary values - never double/float
- Prefer explicit types over `var` (Java 8 compatibility)

## Project Structure

```
br.gov.receita.declaracoes.core/
├── certificado/    # Certificate management (A1/A3, mTLS)
├── assinatura/     # XML digital signature
├── documento/      # Validators (CNPJ, CPF, IE)
├── json/           # JSON utilities
└── exception/      # Common exceptions
```

## XML Signature Requirements

When implementing XML signing:
- Algorithm: RSA-SHA256
- Digest: SHA-256
- Canonicalization: C14N
- Transform: Enveloped Signature
- Library: Apache XML Security

## Certificate Management

- Support A1 (.p12/.pfx files) and A3 (hardware tokens)
- Validate ICP-Brasil certificate chain
- Never log private keys or passwords
