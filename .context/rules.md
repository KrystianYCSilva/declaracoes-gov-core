# Architectural Rules

> Every change must comply with these rules.

## AR-001 — Keep the Core Declaration-Agnostic

No declaration-specific payload DTOs, schema bundles, or transport concerns (SOAP, REST, OAuth2).

## AR-002 — Keep the Core Framework-Agnostic

No Spring, Jakarta EE, or other framework dependencies. Public APIs must remain reusable from plain Java.

## AR-003 — Preserve Module Boundaries

- `domain` — fiscal value objects, validator policy, layout metadata, tables, exceptions
- `format` — text, number, date, JSON, record parsing/serialization helpers
- `xml` — secure DOM utilities and XMLDSIG signing
- `crypto` — certificate-provider abstractions and SSLContext creation
- `core-bom` — POM-only version management

## AR-004 — Keep the Java 8 Baseline

Source and target at Java 8. No Java 9+ language or library features.

## AR-005 — Preserve the Validator Confidence Model

`OFFICIAL` validators back fail-fast defaults. `PROVISIONAL` must be opt-in. `STRUCTURAL` stays limited to normalization, length, and basic form.

## AR-006 — Keep XML Signing Explicit

XMLDSIG envelope: RSA-SHA256, SHA-256 digest, inclusive canonicalization, enveloped transform. Use `XmlSignatureOptions` for target selection. Preserve XXE protections.

## AR-007 — Keep Certificate Handling Isolated

Certificate access behind `CertificateProvider`. Preserve A1 (Pkcs12) and A3 (Pkcs11) paths. Never commit real certificates or keys.

## AR-008 — Build and Coverage Gates

`mvn -q verify` is the validation gate. JaCoCo: 90% line / 90% branch (crypto: 85% line exception).

## AR-009 — Keep Context in Sync

Source of truth: `pom.xml` and `src/`. Human docs in Portuguese. AI docs in English. Sync order: implementation → Portuguese docs → AI docs.
