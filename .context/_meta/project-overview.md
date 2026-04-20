---
description: |
  Project identity, scope, module map, and boundary definitions for declaracoes-gov-core.
  Use when: an agent needs to understand what the project is and what it is not.
---

# Project Overview — declaracoes-gov-core

## Identity

| Field | Value |
|-------|-------|
| **Name** | `declaracoes-gov-core` |
| **GroupId** | `br.com.contabilizei.obrigacoes` |
| **ArtifactId** | `declaracoes-gov-core-parent` |
| **Version** | `1.1.0-SNAPSHOT` |
| **Packaging** | `pom` (Maven reactor parent) |
| **Base Package** | `br.com.contabilizei.obrigacoes.govcore` |

## Purpose

Shared Java 8 foundation library for the `declaracoes-*` workspace. Provides:
- Immutable fiscal identifiers (`Cnpj`, `Cpf`, `Nis`, `Caepf`, `Cno`, `Cei`, `Recibo`, `PeriodoApuracao`, `CodigoMunicipio`).
- Validator confidence policy (`OFFICIAL`, `PROVISIONAL`, `STRUCTURAL`).
- Text, number, date, and JSON formatting utilities.
- Secure XML parsing and XMLDSIG signing.
- Certificate provider abstractions (PKCS#12 / PKCS#11) and `SSLContext` builders.
- Internal BOM for version alignment across the workspace.

## Module Map

| Module | Artifact | Role | Dependencies |
|--------|----------|------|--------------|
| `declaracoes-gov-core-domain` | `core-domain` | Immutable fiscal value objects, validator catalog, layout metadata, tables, exceptions | JDK only |
| `declaracoes-gov-core-format` | `core-format` | Text/number/date normalizers, `GovJsonFactory`, record parsers/serializers | `core-domain` + optional Jackson |
| `declaracoes-gov-core-xml` | `core-xml` | Secure DOM parsing (`XmlDocuments`), XMLDSIG signing (`XmlDsigSigner`) | `core-domain`, `core-crypto`, `xmlsec` |
| `declaracoes-gov-core-crypto` | `core-crypto` | `CertificateProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder` | `core-domain` |
| `declaracoes-gov-core-bom` | `core-bom` | Internal BOM (`dependencyManagement`) | none (POM-only) |

### Dependency Graph
```
domain ← format
   ↑
domain ← crypto → xml
   ↑___________↑
```
- `domain` must remain JDK-only.
- Heavy deps (Jackson, xmlsec) must not leak outside their owning module.

## In Scope

- Shared domain identifiers and validator policy.
- Formatting helpers and layout parser/serializer utilities.
- XML parsing and digital signature support.
- PKCS12/PKCS11 certificate access and mTLS SSLContext.

## Out of Scope (Never Add)

- Declaration-specific schemas or payload DTOs (e.g., eSocial, EFD-Reinf).
- SOAP, REST, OAuth2, or HTTP clients.
- Tax calculation or declaration-specific business rules.
- Code generated from official government XSDs/Schemas.
- Endpoint catalogues, retry policies, or rate-limiting.

## Consumers

- `declaracoes-gov-bom`
- eSocial transmitters
- EFD-Reinf transmitters
- SERPRO integration modules
- REST-domain modules

## Baseline

- **Java**: 8 (source/target `1.8`). No Java 9+ features.
- **Build**: Maven 3.x multi-module reactor.
- **Test**: JUnit 4.13.2 + Mockito 4.11.0.
- **Coverage**: JaCoCo 0.8.11 with gates.
