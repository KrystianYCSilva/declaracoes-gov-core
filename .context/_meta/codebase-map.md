---
name: codebase-map
description: |
  Package structure and key classes for declaracoes-gov-core.
  Use when: navigating the codebase or planning changes.
---

# Codebase Map

## Package Structure

```
br.uem.npd.govcore/
├── certificado/              # Certificate management (mTLS)
│   ├── model/               # Certificate models
│   │   ├── CertificadoInfo
│   │   ├── TipoCertificado
│   │   └── CertificadoException
│   ├── loader/              # Certificate loaders
│   │   ├── CertificadoLoader
│   │   ├── P12CertificadoLoader
│   │   └── A3CertificadoLoader
│   ├── validator/           # Certificate validators
│   │   ├── CertificadoValidator
│   │   └── ICPBrasilValidator
│   └── mtls/                # mTLS connection factory
│       ├── MtlsConnectionFactory
│       └── MtlsHttpClientBuilder
├── assinatura/               # XML digital signature
│   ├── model/
│   │   ├── SigningConfig
│   │   ├── AssinaturaInfo
│   │   └── AssinaturaException
│   ├── service/
│   │   ├── XmlSigner
│   │   └── EnvelopedXmlSigner
│   └── util/
│       ├── Canonicalizer
│       └── XmlNamespaceCleaner
├── documento/                # Document validators
│   ├── model/
│   │   ├── CNPJ
│   │   ├── CPF
│   │   ├── InscricaoEstadual
│   │   └── UF
│   ├── validator/
│   │   ├── CNPJValidator
│   │   ├── CPFValidator
│   │   └── IEValidator
│   └── util/
│       └── DocumentoFormatter
├── json/                     # JSON utilities
│   ├── mapper/
│   │   ├── JsonMapper
│   │   └── CoreJsonMapper
│   └── config/
│       └── JsonConfig
├── exception/                # Common exceptions
│   └── DeclaracoesCoreException
└── util/                     # General utilities
    └── StringUtils
```

## Key Classes by Domain

### Certificate Management (`certificado`)

| Class | Responsibility | Thread Safe |
|-------|----------------|-------------|
| `CertificadoManager` | Interface for certificate operations | Yes (implementations) |
| `P12CertificadoManager` | A1 certificate (.p12/.pfx) management | Yes |
| `A3CertificadoManager` | A3 certificate (token/HSM) management | Yes |
| `CertificadoLoader` | Factory for loading certificates | Yes |
| `ICPBrasilValidator` | Validates ICP-Brasil certificate chain | Yes |
| `MtlsConnectionFactory` | Creates SSLContext with client cert | Yes |

### XML Signature (`assinatura`)

| Class | Responsibility | Thread Safe |
|-------|----------------|-------------|
| `XmlSigner` | Interface for XML signing | Yes |
| `EnvelopedXmlSigner` | eSocial/EFD-Reinf compliant signer | Yes |
| `SigningConfig` | Configuration for signing operations | Yes (immutable) |
| `AssinaturaInfo` | Result of signature verification | Yes (immutable) |
| `Canonicalizer` | XML canonicalization (C14N) | Yes |
| `XmlNamespaceCleaner` | Removes xsi/xsd namespaces | Yes |

### Document Validators (`documento`)

| Class | Responsibility | Thread Safe |
|-------|----------------|-------------|
| `CNPJ` | CNPJ value object (numeric + alphanumeric) | Yes (immutable) |
| `CPF` | CPF value object | Yes (immutable) |
| `InscricaoEstadual` | IE value object with UF | Yes (immutable) |
| `CNPJValidator` | Validates CNPJ format and check digits | Yes |
| `CPFValidator` | Validates CPF format and check digits | Yes |
| `IEValidator` | Validates IE per state rules | Yes |
| `DocumentoFormatter` | Formats/unformats documents | Yes |

### JSON Utilities (`json`)

| Class | Responsibility | Thread Safe |
|-------|----------------|-------------|
| `JsonMapper` | Interface for JSON operations | Yes |
| `CoreJsonMapper` | Jackson-based implementation | Yes |
| `BigDecimalSerializer` | Precise decimal serialization | Yes |

## Extension Points (SPI)

| Interface | Purpose | Usage |
|-----------|---------|-------|
| `CertificadoLoader` | Custom certificate loading | Implement for new cert types |
| `CertificadoValidator` | Custom validation logic | Implement for custom CA chains |
| `XmlSigner` | Custom signature implementation | Rarely needed |
| `JsonMapper` | Custom JSON handling | Implement for other libraries |

## Public API Surface

### Entry Points for Consumers

1. **Certificate Loading**:
   ```java
   CertificadoManager cert = P12CertificadoLoader.load(Path.of("cert.p12"), password);
   SSLContext sslContext = MtlsConnectionFactory.createSslContext(cert);
   ```

2. **XML Signing**:
   ```java
   XmlSigner signer = new EnvelopedXmlSigner();
   String signedXml = signer.sign(xml, SigningConfig.builder()...build());
   ```

3. **Document Validation**:
   ```java
   CNPJ cnpj = CNPJ.of("12345678000195");
   boolean valid = cnpj.isValid();
   ```

4. **JSON**:
   ```java
   JsonMapper mapper = new CoreJsonMapper();
   String json = mapper.toJson(object);
   ```

## Package Dependencies

```
certificado
  ↓ (uses for logging)
slf4j

assinatura
  ↓ (uses certificate for signing)
certificado
  ↓ (uses for logging)
slf4j

documento
  ↓ (none, pure logic)
json
  ↓ (optional, uses Jackson if available)
jackson
```

## Configuration Files

| File | Purpose | Location |
|------|---------|----------|
| `pom.xml` | Maven build config | Root |
| `jacoco.exec` | Coverage data | `target/` |
