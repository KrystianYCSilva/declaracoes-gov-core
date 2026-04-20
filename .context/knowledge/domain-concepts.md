---
description: |
  Deep domain knowledge for Brazilian fiscal identifiers, validation algorithms, and government tables.
  Use when: implementing or modifying validators, value objects, or tables.
---

# Domain Concepts — Brazilian Fiscal Identifiers

## Fiscal Value Objects

| Class | Type | Validation Level | Algorithm |
|-------|------|------------------|-----------|
| `Cnpj` | Company ID | OFFICIAL | Modulo 11 (14 digits, positions 13-14) |
| `Cpf` | Person ID | OFFICIAL | Modulo 11 (11 digits, positions 10-11) |
| `Nis` | Social ID | OFFICIAL | Modulo 11 (11 digits) |
| `Caepf` | CAE for PF | STRUCTURAL | Length + character set |
| `Cno` | Construction ID | STRUCTURAL | Length + character set |
| `Cei` | Employer ID | STRUCTURAL | Length + character set |
| `CodigoMunicipio` | IBGE Code | OFFICIAL | 7-digit IBGE code table |
| `PeriodoApuracao` | Tax Period | STRUCTURAL | YYYY-MM format |
| `Recibo` | Government Receipt | STRUCTURAL | Length + prefix rules |
| `Vigencia` | Validity Range | STRUCTURAL | Start <= End, valid dates |

## Validation Algorithms

### Modulo 11 (CPF, CNPJ, NIS)

**Process**:
1. Extract digits only.
2. Multiply each digit by weights (descending from a base).
3. Sum products.
4. Divide sum by 11.
5. If remainder < 2, check digit is 0; else 11 - remainder.
6. Repeat for second check digit including the first check digit in the calculation.

**CPF weights**: first digit `[10,9,8,7,6,5,4,3,2]`; second digit `[11,10,9,8,7,6,5,4,3,2]`.

**CNPJ weights**: first digit `[5,4,3,2,9,8,7,6,5,4,3,2]`; second digit `[6,5,4,3,2,9,8,7,6,5,4,3,2]`.

### Known Invalid Patterns

- CPF: `00000000000` through `99999999999` (all same digit) — must be rejected even if Modulo 11 passes.
- CNPJ: `00000000000000` through `99999999999999` (all same digit) — must be rejected.

## Government Tables

### Uf (Federative Units)
Enum of 27 Brazilian states. Stable, rarely changes.

### TipoInscricao (Registration Type)
- `1` — CPF
- `2` — CNPJ
- `3` — CAEPF
- `4` — CNO

### TipoAmbiente (Environment)
- `1` — Producao
- `2` — Homologacao

## XMLDSIG Profile

**Required for Brazilian government declarations**:
- Signature method: `http://www.w3.org/2001/04/xmldsig-more#rsa-sha256`
- Digest method: `http://www.w3.org/2001/04/xmlenc#sha256`
- Canonicalization: `http://www.w3.org/TR/2001/REC-xml-c14n-20010315` (inclusive)
- Transform: `http://www.w3.org/2000/09/xmldsig#enveloped-signature`

**Target Selection**:
- Explicit via `XmlSignatureOptions` (element local name + ID attribute).
- Never heuristic (e.g., "first child of root").

## Layout Metadata

- `LayoutVersion` — version of a government layout.
- `RecordDefinition` — metadata for a record type (fixed or delimited).
- `FieldDefinition` — metadata for a field within a record (position, length, type, padding).

## Normalization Rules

| Identifier | Normalization |
|------------|---------------|
| CPF | Remove all non-digits; result is 11 digits |
| CNPJ | Remove all non-digits; result is 14 digits |
| NIS | Remove all non-digits; result is 11 digits |
| PeriodoApuracao | YYYY-MM; pad month with leading zero |
