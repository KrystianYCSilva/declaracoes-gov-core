# declaracoes-gov-core

## What This Project Is
Shared Java 8 foundation library for the `declaracoes-*` workspace: fiscal identifiers, validator policy, formatting, XML signing, and certificate handling.

## Tech Stack
Java 8, Maven multi-module, JUnit 4.13.2, JaCoCo, BouncyCastle, Apache Santuario xmlsec

## Conventions
- Declaration-agnostic and framework-agnostic — no Spring, no SOAP/REST clients
- Module boundaries: domain, format, xml, crypto, core-bom
- Validator confidence model: OFFICIAL (fail-fast), PROVISIONAL (opt-in), STRUCTURAL (basic)
- Human docs in Portuguese, AI docs in English
- JaCoCo: 90% line / 90% branch (crypto: 85% line exception)

## How to Build and Test
```bash
mvn -q verify
```

## Key Directories
- `declaracoes-gov-core-domain/src/` — fiscal value objects, validators, tables
- `declaracoes-gov-core-format/src/` — text, number, date, JSON helpers
- `declaracoes-gov-core-xml/src/` — XMLDSIG signing, secure DOM
- `declaracoes-gov-core-crypto/src/` — certificate providers, SSLContext
- `declaracoes-gov-core-bom/` — internal version alignment (POM-only)
- `.context/` — AI context (rules.md, tech.md, project.md)
