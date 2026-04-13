---
name: codebase-map
description: |
  Module map and important entry points for declaracoes-gov-core.
  Use when: navigating the repository or deciding where a change belongs.
---

# Codebase Map

## Reactor Layout

- Root `pom.xml`: modules, Java 8 baseline, shared test dependencies, and JaCoCo defaults.
- `declaracoes-gov-core-domain/`: public domain types, validator policy, tables, and exceptions.
- `declaracoes-gov-core-format/`: normalization, formatting, JSON factory, and record parsing/serialization helpers.
- `declaracoes-gov-core-xml/`: secure DOM utilities and XMLDSIG signing support.
- `declaracoes-gov-core-crypto/`: certificate providers and `SSLContext` creation.
- `declaracoes-gov-core-bom/`: internal BOM for the core modules.

## Module Details

### `declaracoes-gov-core-domain`

- `br.uem.npd.govcore.model`
  - `Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, `Recibo`, `Vigencia`, `CodigoMunicipio`, `Caepf`, `Cei`, `Cno`
- `br.uem.npd.govcore.model.layout`
  - `FieldDefinition`, `RecordDefinition`, `FieldType`, `LayoutVersion`, `NormativeSource`, `ValidityWindow`, `Constraint`
- `br.uem.npd.govcore.validator`
  - `GovValidators`, `GovValidationCatalog`, `ValidationMetadata`, `ValidationLevel`, `NumericCnpjValidator`, `AlphanumericCnpjValidator`, `CpfValidator`, `NisValidator`, `Modulo11`
- `br.uem.npd.govcore.table`
  - `TipoInscricao`, `TipoAmbiente`, `Uf`
- `br.uem.npd.govcore.exception`
  - `GovCoreException`, `GovSecurityException`, `GovSignatureException`, `GovCommunicationException`, `InvalidDocumentException`

### `declaracoes-gov-core-format`

- `br.uem.npd.govcore.util`
  - `GovTextNormalizer`, `GovNumberFormats`, `GovCompetenceFormats`, `GovJsonFactory`, `XmlDates`
- `br.uem.npd.govcore.format.parser`
  - `DelimitedParser`, `DelimitedSerializer`, `FixedLengthParser`, `FixedLengthSerializer`

### `declaracoes-gov-core-xml`

- `br.uem.npd.govcore.signature`
  - `XmlSigner`, `XmlDsigSigner`, `XmlSignatureOptions`
- `br.uem.npd.govcore.util`
  - `XmlDocuments`

### `declaracoes-gov-core-crypto`

- `br.uem.npd.govcore.crypto`
  - `CertificateProvider`, `AbstractKeyStoreProvider`, `Pkcs12Provider`, `Pkcs11Provider`, `SslContextBuilder`

## Main Flows

- `Cnpj.of(...)` -> `GovValidators.stripCnpjIfValid(...)` -> numeric or alphanumeric validator chain.
- `Cpf.of(...)` / `Nis.of(...)` -> structural validation; `ofProvisionallyValidated(...)` -> explicit provisional algorithm path.
- `GovJsonFactory.getMapper()` -> Jackson mapper configured for non-empty payloads, ISO dates, and plain-string `BigDecimal` serialization.
- `XmlDsigSigner.sign(...)` -> `XmlDocuments.parse(...)` -> target resolution through `XmlSignatureOptions` -> XMLDSIG signature creation.
- `SslContextBuilder.build(...)` -> client key material exposed by a `CertificateProvider`.

## Boundaries

- There is no root `src/`; all maintained code lives under the child modules.
- No generated-code directory is present in the current repository state.
- Transport, OAuth2, SOAP/REST clients, and declaration-specific payloads stay outside this module.
- Tests mirror the package layout inside each child module under `src/test/java`.
