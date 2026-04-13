---
name: project-overview
description: |
  High-level summary of declaracoes-gov-core: purpose, scope, domain, and consumers.
  Use when: onboarding, planning, or checking whether a change belongs in this repository.
---

# Project Overview

## Name

`declaracoes-gov-core`

## Purpose

Provide the shared Java 8 foundation used by the `declaracoes-*` workspace for fiscal document identifiers, validator policy, formatting, XML handling, certificate providers, and the internal core BOM.

## Key Capabilities

- Immutable fiscal value objects such as `Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, and `Recibo`.
- Published validator confidence metadata through `GovValidationCatalog` and `ValidationLevel`.
- Text, number, date, and JSON helpers in `declaracoes-gov-core-format`, including a government-oriented `ObjectMapper` factory.
- Secure DOM parsing plus configurable XMLDSIG signing through `XmlDocuments`, `XmlSigner`, `XmlDsigSigner`, and `XmlSignatureOptions`.
- A1 and A3 certificate-provider abstractions plus `SSLContext` creation in `declaracoes-gov-core-crypto`.
- Internal version alignment through `declaracoes-gov-core-bom`.

## In Scope

- Shared domain identifiers and validator policy.
- Formatting helpers and layout parser/serializer utilities.
- XML parsing and XML signature support.
- PKCS12 / PKCS11 certificate access and mTLS `SSLContext` creation.
- The internal BOM for the core modules.

## Out of Scope

- Declaration-specific schemas or payload DTOs.
- SOAP, REST, OAuth2, or HTTP client implementations.
- Tax calculation or business rules tied to a single declaration family.
- Generated sources; this repository currently contains only maintained code.

## Domain

Brazilian federal declaration support libraries used by eSocial, EFD-Reinf, SERPRO/Integra Contador, and related REST-domain modules.

## Key Consumers

- `declaracoes-gov-bom`
- eSocial leiautes, transmissor, and BOM modules
- EFD-Reinf leiautes, transmissor, and BOM modules
- SERPRO foundation and REST-domain leiautes/transmissor modules

## Baseline

- Version: `1.0.0`
- Status: Active workspace foundation line
- Packaging at the root: `pom` (reactor parent)
