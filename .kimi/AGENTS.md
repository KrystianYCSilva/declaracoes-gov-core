# Kimi Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific Kimi guidance.

## Baseline

- Project: `declaracoes-gov-core`
- Java 8+ Maven library
- Thread-safe, framework-agnostic
- Domain: Brazilian tax declarations (certificates, XML signature, validators)

## Guardrails

- Keep secrets out of Git (no .p12, .pfx, .pem in repo).
- Keep public APIs stable - use @Deprecated before removal.
- Maintain Java 8 compatibility - no `var`, no Java 9+ Optional methods.
- Keep `mvn verify` green at all times.
- All public classes must be thread-safe - document in Javadoc.
- No Spring/Jakarta EE dependencies in core.
- Use BigDecimal for all monetary values - never double/float.

## Main Components

- **certificado/**: A1/A3 certificate loading, ICP-Brasil validation, mTLS factory
- **assinatura/**: XML digital signature (RSA-SHA256, C14N, Enveloped)
- **documento/**: CNPJ (numeric + alphanumeric), CPF, IE validators
- **json/**: Thread-safe Jackson wrapper

## XML Signature Requirements

When implementing XML signing:
- Algorithm: RSA-SHA256 (`http://www.w3.org/2001/04/xmldsig-more#rsa-sha256`)
- Digest: SHA-256 (`http://www.w3.org/2001/04/xmlenc#sha256`)
- Canonicalization: C14N (`http://www.w3.org/TR/2001/REC-xml-c14n-20010315`)
- Transform: Enveloped (`http://www.w3.org/2000/09/xmldsig#enveloped-signature`)
- Remove xmlns:xsi and xmlns:xsd before signing (eSocial requirement)

## CNPJ Alphanumeric Support

The CNPJ validator must support both formats:
- Current: 14 digits (XX.XXX.XXX/XXXX-XX)
- 2026+: 14 alphanumeric (using ASCII-48 for DV calculation)
- Auto-detect format based on content

## Useful Commands

```bash
# Compile only
mvn -q -DskipTests compile

# Run tests
mvn -q test

# Full validation (tests, checkstyle, pmd, coverage)
mvn -q verify

# Package for local install
mvn -q -DskipTests package

# Install to local repo
mvn -q -DskipTests install

# Check code style
mvn -q checkstyle:check

# Run PMD
mvn -q pmd:check

# Generate coverage report
mvn -q jacoco:report
```

## Testing Requirements

- Minimum 80% line coverage, 75% branch coverage
- All public methods must have tests
- Thread-safety tests for concurrent classes
- Use test certificates (dummy) - never real ones

## Spec Kit

- Shared contract: `docs/SPECKIT-AGENTS.md` (if exists)
- Implementation plans: `.kimi/plan/`
- This project uses Kimi as primary development agent
