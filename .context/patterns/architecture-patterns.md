---
name: architecture-patterns
description: |
  Recurring implementation patterns already present in declaracoes-gov-core.
  Use when: designing changes so they match the current codebase.
---

# Architecture Patterns

## 1. Immutable Fiscal Value Objects

- **Purpose**: Keep document identifiers and core domain values valid at construction time and safe to share across threads.
- **Where used now**: `Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, `Recibo`, `Vigencia`.
- **Key classes**: `br.uem.npd.govcore.model.*`.
- **Constraints**: Constructor access stays private; callers use factory methods such as `of(...)`.
- **When not to use it**: Do not force provisional algorithms into the default constructor path.

## 2. Validator Facade plus Confidence Catalog

- **Purpose**: Expose simple validation entry points while publishing the confidence level of each supported document type.
- **Where used now**: `GovValidators`, `GovValidationCatalog`, `ValidationMetadata`, `ValidationLevel`.
- **Key classes**: `NumericCnpjValidator`, `AlphanumericCnpjValidator`, `CpfValidator`, `NisValidator`, `Modulo11`.
- **Constraints**: `OFFICIAL`, `PROVISIONAL`, and `STRUCTURAL` remain part of the public contract.
- **When not to use it**: Do not flatten all document checks into a single undifferentiated boolean API.

## 3. Layout Definition plus Parser/Serializer Pairing

- **Purpose**: Reuse shared record metadata to parse or serialize fixed-width and delimited fiscal layouts.
- **Where used now**: `RecordDefinition`, `FieldDefinition`, `DelimitedParser`, `DelimitedSerializer`, `FixedLengthParser`, `FixedLengthSerializer`.
- **Constraints**: Parsing behavior follows the field metadata rather than declaration-specific heuristics.
- **When not to use it**: Do not push transport- or schema-specific payload logic into these generic helpers.

## 4. Configurable XMLDSIG Signing Boundary

- **Purpose**: Keep XML signing reusable across declaration families while letting callers choose the target element and ID attribute explicitly.
- **Where used now**: `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions`, `XmlDocuments`.
- **Constraints**: Secure DOM parsing and current XMLDSIG algorithms remain intact.
- **When not to use it**: Do not hardcode declaration-specific target-selection rules into the signer.

## 5. KeyStore-Backed Certificate Providers

- **Purpose**: Hide JCA / JCE KeyStore handling behind a stable contract that works for both A1 and A3 scenarios.
- **Where used now**: `CertificateProvider`, `AbstractKeyStoreProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder`.
- **Constraints**: Alias discovery, defensive copies, and TLS setup stay inside the crypto module.
- **When not to use it**: Do not bypass the provider abstraction by passing raw keystores through unrelated modules.

## 6. Specialized Government JSON Factory

- **Purpose**: Centralize the JSON defaults already required by the workspace's REST integrations.
- **Where used now**: `GovJsonFactory`, `GovNumberFormats`.
- **Constraints**: BigDecimal serialization remains plain-string based, dates remain Java 8/Jackson aware, and empty fields stay omitted.
- **When not to use it**: Do not document `GovJsonFactory` as a generic `ObjectMapper` preset for every application outside the current workspace needs.
