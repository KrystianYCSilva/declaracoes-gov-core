---
description: |
  T1 code quality standards for declaracoes-gov-core. Design conventions, package organization, and style rules.
  Use when: writing new code, reviewing PRs, or refactoring.
---

# Code Quality Standards — T1 (NORMATIVE)

## Design Conventions

### Immutability
- All value objects are `final` with private constructors.
- Use static factory methods (e.g., `Cnpj.of("...")`).
- Never expose mutable state.

### Statelessness
- Validators and utilities are stateless.
- No static mutable fields.

### Explicit APIs
- Prefer explicit options objects over heuristics.
- Example: `XmlSignatureOptions` instead of auto-detecting the target element.

### Null Safety
- Factory methods fail-fast with domain-specific exceptions (`InvalidDocumentException`).
- Never return `null` from public APIs.

### Serializable
- Value objects declare `private static final long serialVersionUID = 1L` when implementing `Serializable`.

## Package Organization

```
br.uem.npd.govcore
├── model              # Fiscal value objects (Cnpj, Cpf, Nis, ...)
├── model/layout       # Layout metadata (RecordDefinition, FieldDefinition, ...)
├── table              # Stable enums (Uf, TipoInscricao, TipoAmbiente)
├── validator          # GovValidators, GovValidationCatalog, Modulo11, concrete validators
├── exception          # GovCoreException and derivatives
├── util               # Formatting, JSON, XML date helpers
├── format/parser      # DelimitedParser, FixedLengthParser, serializers
├── crypto             # CertificateProvider, Pkcs12Provider, Pkcs11Provider, SslContextBuilder
└── signature          # XmlSigner, XmlDsigSigner, XmlSignatureOptions
```

## Naming Conventions

| Element | Pattern | Example |
|---------|---------|---------|
| Value object | Noun, immutable | `Cnpj`, `PeriodoApuracao` |
| Factory method | `of(...)`, `from(...)` | `Cnpj.of(raw)` |
| Validator | `*Validator` or method in `GovValidators` | `Modulo11` |
| Exception | `Gov*Exception` | `InvalidDocumentException` |
| Test class | `*Test.java` | `CnpjTest.java` |
| Test method | `should*When*` or `rejects*When*` | `shouldRejectInvalidCheckDigits` |

## Language Rules

- **Javadoc and inline comments**: Portuguese (Brazilian government domain vocabulary).
- **AI-facing documentation**: English.
- **Commit messages**: English.
- **Variable names**: Portuguese or English? Prefer Portuguese when matching domain terms (`cnpj`, `inscricao`, `vigencia`), English for generic concepts (`builder`, `parser`, `serializer`).
