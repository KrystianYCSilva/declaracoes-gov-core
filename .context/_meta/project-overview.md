---
name: project-overview
description: |
  High-level summary of declaracoes-gov-core: purpose, scope, domain, and key stakeholders.
  Use when: onboarding, planning, or writing human-facing docs.
---

# Project Overview

## Name

declaracoes-gov-core

## Purpose

Agnostic core library providing shared functionality for Brazilian federal tax declaration systems. Extracts and unifies common capabilities needed across eSocial, EFD-Reinf, DCTFWeb, PGDAS, DEFIS, PER/DCOMP, and MIT declarations.

## Key Capabilities

1. **Certificate Management (mTLS)**
   - Load A1 certificates (.p12/.pfx files)
   - Load A3 certificates (hardware tokens/smartcards via PKCS#11)
   - ICP-Brasil chain validation
   - mTLS connection factory for HTTP clients

2. **XML Digital Signature**
   - eSocial/EFD-Reinf compliant signing
   - RSA-SHA256 with SHA-256 digest
   - C14N canonicalization
   - Enveloped signature transform
   - Signature verification

3. **Document Validators**
   - CNPJ (numeric current + alphanumeric 2026+ support)
   - CPF
   - Inscrição Estadual (state-specific rules)
   - Formatters and utilities

4. **JSON Utilities**
   - Thread-safe Jackson wrapper
   - Java 8 date/time support
   - BigDecimal precision handling for monetary values

## Scope

### In Scope
- Certificate loading and validation (A1/A3)
- mTLS SSLContext factory
- XML signature (sign and verify)
- Document validators (CNPJ, CPF, IE)
- JSON serialization/deserialization utilities
- SPI interfaces for extensibility

### Out of Scope
- Specific declaration schemas (handled by leiautes libraries)
- HTTP client implementation (handled by transmissor libraries)
- Business logic for specific tax calculations
- UI or web frameworks
- Database persistence

## Domain

Brazilian Federal Revenue Service (Receita Federal do Brasil) tax declaration ecosystem:
- Previdenciário: eSocial, EFD-Reinf
- Fazendário: DCTFWeb, MIT, PGDAS, DEFIS, PER/DCOMP
- Parcelamentos: PERT, PARC, RELP

## Key Stakeholders

- **Primary Users**: Java developers building applications for Brazilian tax declarations
- **Maintainers**: NPD/UEM development team
- **Consumers**: 
  - declaracoes-esocial-leiautes/transmissor
  - declaracoes-efd-reinf-leiautes/transmissor
  - declaracoes-serpro-transmissor
  - declaracoes-dctfweb-leiautes
  - declaracoes-mit-leiautes
  - declaracoes-pgdas-leiautes
  - declaracoes-defis-leiautes
  - declaracoes-perdcomp-leiautes
  - declaracoes-parcelamento-leiautes

## Baseline

- Version: 1.0.0
- Status: Planning/Initial Development
- Target Release: Q2 2026

## Success Criteria

- Zero external framework dependencies in core
- 100% thread-safety for public APIs
- Java 8+ compatibility
- >80% code coverage
- <10ms overhead for certificate operations
- <5ms overhead for signature operations
