---
name: architectural-rules
description: |
  Tier T0 rules for declaracoes-gov-core.
  Use when: proposing, reviewing, or implementing any change in this repository.
---

# Architectural Rules

> Tier: T0 - ABSOLUTE. Every change must comply with these rules.

## `AR-001` Keep the Core Declaration-Agnostic

Rule:

- Keep `declaracoes-gov-core` focused on transversal domain, formatting, XML, crypto, and BOM concerns.
- Do not add declaration-specific payload DTOs, schema bundles, or workflow-specific business logic here.
- Do not introduce transport concerns such as SOAP clients, REST clients, OAuth2 negotiation, or endpoint orchestration.

## `AR-002` Keep the Core Framework-Agnostic

Rule:

- Do not add Spring, Jakarta EE, or other framework dependencies to the current core modules.
- Public APIs must remain reusable from plain Java consumers.

## `AR-003` Preserve the Current Module Boundaries

Rule:

- `domain` owns fiscal value objects, validator policy, layout metadata, tables, and shared exceptions.
- `format` owns text, number, date, JSON, and record parsing/serialization helpers.
- `xml` owns secure DOM utilities and XMLDSIG signing support.
- `crypto` owns certificate-provider abstractions and `SSLContext` creation.
- `core-bom` stays POM-only and manages versions for the core modules.

## `AR-004` Keep the Java 8 Baseline

Rule:

- Source and target compatibility remain at Java 8.
- Do not introduce Java 9+ language or library features into maintained code.

## `AR-005` Preserve the Published Validator Confidence Model

Rule:

- `OFFICIAL` validators may back fail-fast defaults.
- `PROVISIONAL` validators must stay explicitly marked and opt-in.
- `STRUCTURAL` support must remain limited to normalization, length, and basic form.
- Keep the current document behavior aligned with `docs/05-MATRIZ-VALIDADORES.md`:
  - `Cnpj` supports numeric and alphanumeric validation.
  - `Cpf` and `Nis` default to structural checks, with provisional algorithms exposed explicitly.
  - `Caepf`, `Cno`, and `Cei` stay structural in the current baseline.

## `AR-006` Keep XML Signing Behavior Explicit

Rule:

- XML signing must keep the current XMLDSIG envelope shape: RSA-SHA256 signature, SHA-256 digest, inclusive canonicalization, and enveloped transform.
- Do not silently change target selection behavior; use `XmlSignatureOptions` when the signed element or ID attribute needs to be specified.
- Preserve secure DOM parsing and XXE protections in `XmlDocuments`.

## `AR-007` Keep Certificate Handling Isolated and Safe

Rule:

- Certificate access must stay behind `CertificateProvider` implementations.
- Preserve the current A1 (`Pkcs12Provider`) and A3 (`Pkcs11Provider`) support paths.
- Never commit real certificates, private keys, PINs, or token configuration secrets.
- Keep `SslContextBuilder` on TLSv1.2 and current JCA/JCE-based flows unless the live implementation changes first.

## `AR-008` Keep Build and Coverage Gates Accurate

Rule:

- `mvn -q verify` at the reactor root is the validation gate.
- Parent JaCoCo defaults remain `90%` line / `90%` branch.
- `declaracoes-gov-core-crypto` may keep its documented `85%` line override.
- Do not add fake exclusions or stale coverage numbers to AI docs.

## `AR-009` Keep Context in Sync

Rule:

- The live source of truth is the root `pom.xml`, the child-module `pom.xml` files, and `declaracoes-gov-core-*/src`.
- Human docs remain in Portuguese.
- AI docs remain in English.
- Synchronization order: implementation -> Portuguese human docs -> AI docs.
