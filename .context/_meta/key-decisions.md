---
name: key-decisions
description: |
  Stable design decisions extracted from the current core implementation and human design docs.
  Use when: evaluating scope changes or documenting architecture-impacting work.
---

# Key Decisions

## ADR-CORE-001: Keep the Foundation Split by Module

- **Decision**: Keep `domain`, `format`, `xml`, `crypto`, and `core-bom` as separate Maven modules.
- **Context**: The design docs explicitly treat these concerns as distinct and the current reactor already reflects that split.
- **Consequences**: Consumers can depend on only the layers they need, and heavy concerns such as XML or crypto do not leak into the lighter modules.
- **Status**: Active.

## ADR-CORE-002: Core Stops Before Transport

- **Decision**: Keep SOAP, REST, OAuth2, HTTP client orchestration, and declaration-specific delivery logic outside `declaracoes-gov-core`.
- **Context**: Human design docs place transport in the transmissor modules and reserve the core for transversal technical utilities.
- **Consequences**: The core stays reusable across eSocial, EFD-Reinf, SERPRO, and REST-domain libraries without framework or channel coupling.
- **Status**: Active.

## ADR-CORE-003: Publish Validator Confidence Explicitly

- **Decision**: Keep validator support classified as `OFFICIAL`, `PROVISIONAL`, or `STRUCTURAL` through `ValidationLevel`, `ValidationMetadata`, and `GovValidationCatalog`.
- **Context**: `docs/05-MATRIZ-VALIDADORES.md` and the current code treat validator confidence as a public contract.
- **Consequences**: Only official rules can drive fail-fast defaults; provisional rules must stay clearly marked and structural rules must not be overstated.
- **Status**: Active.

## ADR-CORE-004: Keep Provisional Algorithms Opt-In

- **Decision**: `Cpf` and `Nis` keep structural validation by default and expose provisional algorithms only through explicit factory methods.
- **Context**: The current value objects implement `of(...)` and `ofProvisionallyValidated(...)` as separate entry points.
- **Consequences**: Consumers can adopt provisional algorithms deliberately without the core presenting them as normative defaults.
- **Status**: Active.

## ADR-CORE-005: Share Modulo11 and Normalization Infrastructure

- **Decision**: Keep shared checksum and normalization logic in the common validator layer instead of duplicating it per document type.
- **Context**: `Modulo11`, `GovValidators`, and the validator classes centralize common logic for CNPJ, CPF, and NIS-related checks.
- **Consequences**: Auditability improves and future validator changes stay localized.
- **Status**: Active.

## ADR-CORE-006: Make XML Signing Target Selection Explicit

- **Decision**: Use `XmlSignatureOptions` to control target element lookup, ID attribute selection, and fallback-to-root behavior.
- **Context**: `XmlDsigSigner` and its tests rely on explicit options rather than hidden layout-specific heuristics.
- **Consequences**: XML signing stays declaration-agnostic and predictable across different payload shapes.
- **Status**: Active.

## ADR-CORE-007: Reuse Mature Libraries Where They Add Real Value

- **Decision**: Keep using Jackson for JSON support, XMLDSIG APIs plus `xmlsec` for XML support, and JCA/JCE primitives for certificate handling instead of building custom replacements.
- **Context**: The current POMs and implementation already depend on these libraries and APIs.
- **Consequences**: Maintenance stays focused on Brazilian fiscal behavior rather than generic infrastructure reinvention.
- **Status**: Active.
