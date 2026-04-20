# Project Overview

## Name

`declaracoes-gov-core`

## Purpose

Shared Java 8 foundation library for the `declaracoes-*` workspace: fiscal document identifiers, validator policy, formatting, XML handling, certificate providers, and the internal core BOM.

## Modules

- **core-domain** — immutable fiscal value objects (`Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, `Recibo`), validator confidence model, layout metadata, tables, exceptions
- **core-format** — text, number, date, JSON helpers; government-oriented `ObjectMapper` factory
- **core-xml** — secure DOM parsing, configurable XMLDSIG signing
- **core-crypto** — A1/A3 certificate-provider abstractions, `SSLContext` creation
- **core-bom** — internal version alignment (POM-only)

## In Scope

- Shared domain identifiers and validator policy
- Formatting helpers and layout parser/serializer utilities
- XML parsing and digital signature support
- PKCS12/PKCS11 certificate access and mTLS SSLContext

## Out of Scope

- Declaration-specific schemas or payload DTOs
- SOAP, REST, OAuth2, or HTTP clients
- Tax calculation or business rules

## Consumers

- `declaracoes-gov-bom`, eSocial, EFD-Reinf, SERPRO, and REST-domain modules

## Baseline

- Version: `1.1.0-SNAPSHOT`
- Packaging: `pom` (reactor parent)
- Base package: `br.uem.npd.govcore`
