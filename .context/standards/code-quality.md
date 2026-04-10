---
name: code-quality
description: |
  Code quality standards and style guidelines for declaracoes-gov-core.
  Tier T1 - normative standards that should be followed.
---

# Code Quality Standards

## General Principles

1. **Readability over cleverness**: Code is read more often than written
2. **Explicit over implicit**: Prefer clear, verbose code over terse shortcuts
3. **Fail fast**: Validate inputs early and throw meaningful exceptions
4. **Defensive programming**: Null-checks, bounds-checks, type safety

## Java Style Guide

### Naming Conventions

| Element | Convention | Example |
|---------|------------|---------|
| Classes | PascalCase | `CertificadoManager`, `XmlSigner` |
| Interfaces | PascalCase (adjective/noun) | `Validador`, `Carregavel` |
| Methods | camelCase (verb) | `carregarCertificado()`, `assinarXml()` |
| Variables | camelCase | `certificado`, `chavePrivada` |
| Constants | UPPER_SNAKE_CASE | `ALGORITMO_RSA_SHA256` |
| Packages | lowercase | `br.gov.receita.declaracoes.core` |
| Enums | PascalCase (constants UPPER) | `TipoCertificado { E_CPF, E_CNPJ }` |
| Generic types | Single uppercase | `T`, `E`, `K`, `V` |

### Code Formatting

- Indentation: 4 spaces (no tabs)
- Line length: 120 characters maximum
- Braces: Same line (K&R style)
- Empty lines: Between methods, before comments
- Imports: No wildcard imports, organized groups

```java
package br.gov.receita.declaracoes.core.certificado;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Objects;

import br.gov.receita.declaracoes.core.exception.CertificadoException;

/**
 * Manages ICP-Brasil certificates for mTLS and XML signing.
 */
public final class P12CertificadoManager implements CertificadoManager {
    
    private final X509Certificate certificate;
    private final PrivateKey privateKey;
    
    private P12CertificadoManager(X509Certificate certificate, PrivateKey privateKey) {
        this.certificate = Objects.requireNonNull(certificate, "Certificate is required");
        this.privateKey = Objects.requireNonNull(privateKey, "Private key is required");
    }
    
    @Override
    public X509Certificate getCertificate() {
        return certificate;
    }
    
    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
```

### Comments and Documentation

#### Javadoc Requirements

All public APIs must have Javadoc with:
- Brief description
- `@param` for each parameter
- `@return` description
- `@throws` for each checked exception
- `@apiNote` for thread-safety or usage notes

```java
/**
 * Creates a CNPJ instance from an unformatted string.
 * 
 * <p>Accepts both numeric (current) and alphanumeric (2026+) formats.
 * Formatting characters (dots, slash, dash) are removed automatically.
 * 
 * @param value the CNPJ string (14 characters, with or without formatting)
 * @return a new immutable CNPJ instance
 * @throws DocumentoException if the format is invalid or check digits don't match
 * @apiNote This method is thread-safe. The returned CNPJ is immutable.
 * @implNote This method auto-detects the format (numeric vs alphanumeric).
 */
public static CNPJ of(String value) {
    // implementation
}
```

#### Code Comments

- Explain "why", not "what" (the code shows what)
- Mark TODOs with issue numbers: `// TODO(#123): Fix edge case`
- Explain workarounds and hacks
- Don't comment obvious code

```java
// Good: Explains business logic
// CNPJ alfanumerico usa ASCII-48 no calculo (IN RFB 2.229/2024)
int valor = charToAscii48(caractere);

// Bad: Obvious comment
// Increment counter
counter++;
```

### Null Safety

- Use `Objects.requireNonNull()` for parameter validation
- Return empty collections, not null
- Use `Optional` for truly optional return values
- Document nullability in Javadoc

```java
// Good
public void process(CNPJ cnpj) {
    Objects.requireNonNull(cnpj, "CNPJ is required");
    // ...
}

// Good with Optional
public Optional<Certificado> findByCnpj(CNPJ cnpj) {
    // returns Optional.empty() if not found
}

// Bad
public void process(CNPJ cnpj) {
    if (cnpj == null) {
        throw new NullPointerException();
    }
}
```

### Exception Handling

- Use checked exceptions for recoverable errors
- Use unchecked exceptions for programming errors
- Wrap low-level exceptions with context
- Never swallow exceptions

```java
// Good: Checked exception with context
try {
    return keyStore.getEntry(alias, passwordProtection);
} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
    throw new CertificadoException("Failed to load certificate from " + path + ": " + e.getMessage(), e);
}

// Bad: Swallowing exception
try {
    process();
} catch (Exception e) {
    // ignored
}
```

## Design Patterns

### Preferred Patterns

1. **Value Object**: Immutable, identity-free objects (CNPJ, CPF)
2. **Builder**: For complex object construction (SigningConfig)
3. **Factory Method**: Static `of()`, `from()` methods
4. **SPI (Service Provider Interface)**: For extensibility
5. **Strategy**: For interchangeable algorithms

### Anti-Patterns to Avoid

1. **Singleton**: Use dependency injection or factory instead
2. **God Class**: Split large classes by responsibility
3. **Primitive Obsession**: Use value objects (CNPJ instead of String)
4. **Feature Envy**: Methods should be on the class with the data

## Testing Standards

### Test Naming

```java
@Test
void shouldValidateCorrectCnpj() { }

@Test
void shouldRejectCnpjWithInvalidCheckDigits() { }

@Test
void shouldThrowExceptionWhenCnpjIsNull() { }
```

### Test Structure (AAA)

```java
@Test
void shouldSignXmlWithValidCertificate() {
    // Arrange
    XmlSigner signer = new EnvelopedXmlSigner();
    String xml = loadTestXml();
    SigningConfig config = createTestConfig();
    
    // Act
    String signed = signer.sign(xml, config);
    
    // Assert
    assertThat(signed).contains("<Signature");
    assertThat(signer.verify(signed)).isTrue();
}
```

### Test Coverage Requirements

- **Line coverage**: ≥ 80%
- **Branch coverage**: ≥ 75%
- All public methods must have tests
- Edge cases must be tested
- Thread-safety requires concurrent tests

## Static Analysis

### Checkstyle Rules

- Maximum line length: 120
- Indentation: 4 spaces
- No trailing whitespace
- Proper Javadoc on public APIs
- No wildcard imports

### PMD Rules

- Avoid unused imports
- Avoid unused private methods/fields
- Avoid empty catch blocks
- Avoid deep nesting (max 3)
- Avoid overly complex methods

### Common PMD Suppressions

```java
// For intentionally empty methods
@SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")

// For performance-critical code
@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
```

## Code Review Checklist

Before submitting code:

- [ ] Code compiles without warnings
- [ ] All tests pass (`mvn test`)
- [ ] Coverage meets thresholds (`mvn jacoco:check`)
- [ ] Checkstyle passes (`mvn checkstyle:check`)
- [ ] PMD passes (`mvn pmd:check`)
- [ ] Javadoc is complete for public APIs
- [ ] Thread-safety is documented
- [ ] No secrets or credentials in code
- [ ] No Java 9+ features used
