---
name: architecture-patterns
description: |
  Architectural and design patterns used in declaracoes-gov-core.
  Use when: designing new features or understanding existing code.
---

# Architecture Patterns

## 1. Value Object Pattern

### Purpose

Immutable objects that represent values without identity. Two value objects are equal if their contents are equal.

### Implementation

```java
/**
 * CNPJ value object supporting both numeric and alphanumeric formats.
 * 
 * @apiNote This class is immutable and thread-safe.
 */
public final class CNPJ implements Serializable {
    
    private final String valor;          // Unformatted, stored as-is
    private final boolean alfanumerico;  // True if contains letters
    
    private CNPJ(String valor) {
        this.valor = unformat(valor);
        this.alfanumerico = detectaAlfanumerico(this.valor);
        validate();
    }
    
    public static CNPJ of(String valor) {
        return new CNPJ(valor);
    }
    
    public static CNPJ fromFormatted(String formatado) {
        return new CNPJ(unformat(formatado));
    }
    
    // No setters - immutable
    
    public String getValor() {
        return valor;
    }
    
    public String format() {
        if (alfanumerico) {
            return formatAlphanumeric(valor);
        }
        return formatNumeric(valor);
    }
    
    public boolean isAlfanumerico() {
        return alfanumerico;
    }
    
    public boolean isValid() {
        return CNPJValidator.isValid(this);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CNPJ)) return false;
        CNPJ cnpj = (CNPJ) o;
        return valor.equals(cnpj.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return format();
    }
}
```

### Benefits

- Thread-safe by design
- Predictable behavior
- Cache-friendly
- No defensive copies needed

## 2. Builder Pattern

### Purpose

Construct complex objects step by step, especially when many optional parameters exist.

### Implementation

```java
/**
 * Configuration for XML signing operations.
 */
public final class SigningConfig {
    
    private final X509Certificate certificate;
    private final PrivateKey privateKey;
    private final String referenceUri;
    private final List<String> transforms;
    private final String digestMethod;
    private final String signatureMethod;
    
    private SigningConfig(Builder builder) {
        this.certificate = builder.certificate;
        this.privateKey = builder.privateKey;
        this.referenceUri = builder.referenceUri;
        this.transforms = Collections.unmodifiableList(new ArrayList<>(builder.transforms));
        this.digestMethod = builder.digestMethod;
        this.signatureMethod = builder.signatureMethod;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static final class Builder {
        private X509Certificate certificate;
        private PrivateKey privateKey;
        private String referenceUri = "";  // Default: empty
        private List<String> transforms = Arrays.asList(
            Transform.ENVELOPED,
            CanonicalizationMethod.INCLUSIVE
        );
        private String digestMethod = DigestMethod.SHA256;
        private String signatureMethod = XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256;
        
        private Builder() {}
        
        public Builder certificate(X509Certificate certificate) {
            this.certificate = Objects.requireNonNull(certificate);
            return this;
        }
        
        public Builder privateKey(PrivateKey privateKey) {
            this.privateKey = Objects.requireNonNull(privateKey);
            return this;
        }
        
        public Builder referenceUri(String referenceUri) {
            this.referenceUri = referenceUri;
            return this;
        }
        
        public Builder transforms(List<String> transforms) {
            this.transforms = new ArrayList<>(transforms);
            return this;
        }
        
        public SigningConfig build() {
            Objects.requireNonNull(certificate, "Certificate is required");
            Objects.requireNonNull(privateKey, "Private key is required");
            return new SigningConfig(this);
        }
    }
    
    // Getters (no setters - immutable)
}
```

### Benefits

- Readable construction code
- Immutable result
- Optional parameters with defaults
- Validation at build time

## 3. SPI (Service Provider Interface)

### Purpose

Allow consumers to extend or replace functionality without modifying core code.

### Implementation

```java
/**
 * SPI for certificate loading strategies.
 * 
 * Implementations are discovered via ServiceLoader.
 */
public interface CertificadoLoader {
    
    /**
     * Returns true if this loader can handle the given source.
     */
    boolean supports(String sourceType);
    
    /**
     * Loads a certificate from the specified source.
     * 
     * @param source the certificate source (path, token name, etc.)
     * @param password the password or PIN
     * @return the certificate manager
     * @throws CertificadoException if loading fails
     */
    CertificadoManager load(String source, char[] password);
}

// Implementation
public class P12CertificadoLoader implements CertificadoLoader {
    
    @Override
    public boolean supports(String sourceType) {
        return "P12".equalsIgnoreCase(sourceType) || 
               "PFX".equalsIgnoreCase(sourceType);
    }
    
    @Override
    public CertificadoManager load(String source, char[] password) {
        // Implementation
    }
}

// Registration: META-INF/services/br.gov.receita.declaracoes.core.certificado.loader.CertificadoLoader
// Contents: br.gov.receita.declaracoes.core.certificado.loader.P12CertificadoLoader

// Usage
ServiceLoader<CertificadoLoader> loaders = ServiceLoader.load(CertificadoLoader.class);
for (CertificadoLoader loader : loaders) {
    if (loader.supports(sourceType)) {
        return loader.load(source, password);
    }
}
```

### Benefits

- Extension without modification
- Consumer flexibility
- Clean separation of concerns
- Standard Java pattern

## 4. Strategy Pattern

### Purpose

Define a family of algorithms, encapsulate each one, and make them interchangeable.

### Implementation

```java
/**
 * Strategy for validating Inscrição Estadual.
 */
public interface IEValidationStrategy {
    boolean isValid(String ie);
}

// Concrete strategies
public class IESaoPauloValidator implements IEValidationStrategy {
    @Override
    public boolean isValid(String ie) {
        // São Paulo specific algorithm
    }
}

public class IERioJaneiroValidator implements IEValidationStrategy {
    @Override
    public boolean isValid(String ie) {
        // Rio de Janeiro specific algorithm
    }
}

// Context
public class IEValidator {
    
    private static final Map<UF, IEValidationStrategy> STRATEGIES = Map.of(
        UF.SP, new IESaoPauloValidator(),
        UF.RJ, new IERioJaneiroValidator(),
        // ...
    );
    
    public boolean isValid(String ie, UF uf) {
        IEValidationStrategy strategy = STRATEGIES.get(uf);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown UF: " + uf);
        }
        return strategy.isValid(ie);
    }
}
```

### Benefits

- Easy to add new algorithms
- Eliminates conditional complexity
- Algorithms are testable in isolation

## 5. Factory Method Pattern

### Purpose

Create objects without specifying the exact class to create.

### Implementation

```java
public abstract class CertificadoManager {
    
    /**
     * Creates a certificate manager from a P12 file.
     */
    public static CertificadoManager fromP12(Path path, char[] password) {
        return new P12CertificadoManager(path, password);
    }
    
    /**
     * Creates a certificate manager from an A3 token.
     */
    public static CertificadoManager fromA3(int slot, String pin) {
        return new A3CertificadoManager(slot, pin);
    }
    
    // Abstract methods...
}

// Usage
CertificadoManager cert = CertificadoManager.fromP12(path, password);
```

### Benefits

- Simple creation API
- Hides implementation details
- Can add new types without changing client code

## 6. Template Method Pattern

### Purpose

Define the skeleton of an algorithm, letting subclasses override specific steps.

### Implementation

```java
public abstract class XmlSigner {
    
    /**
     * Template method defining the signing algorithm.
     */
    public final String sign(String xml, SigningConfig config) {
        // Step 1: Parse (common)
        Document doc = parseXml(xml);
        
        // Step 2: Prepare (customizable)
        Element elementToSign = prepareDocument(doc, config);
        
        // Step 3: Create signature (common)
        XMLSignature signature = createSignature(config);
        
        // Step 4: Apply transforms (customizable)
        applyTransforms(signature, config);
        
        // Step 5: Sign (common)
        signDocument(doc, signature, config);
        
        // Step 6: Return (common)
        return documentToString(doc);
    }
    
    protected abstract Element prepareDocument(Document doc, SigningConfig config);
    
    protected abstract void applyTransforms(XMLSignature signature, SigningConfig config);
    
    // Concrete common methods
    private Document parseXml(String xml) { /* ... */ }
    private XMLSignature createSignature(SigningConfig config) { /* ... */ }
    private void signDocument(Document doc, XMLSignature signature, SigningConfig config) { /* ... */ }
    private String documentToString(Document doc) { /* ... */ }
}
```

### Benefits

- Reuse common algorithm structure
- Customize specific steps
- Prevent algorithm modification

## Pattern Selection Guide

| Situation | Pattern |
|-----------|---------|
| Need immutable data carrier | Value Object |
| Complex object with optional params | Builder |
| Allow consumer extension | SPI |
| Multiple interchangeable algorithms | Strategy |
| Hide creation complexity | Factory Method |
| Algorithm with customizable steps | Template Method |
| Need to add behavior without changing class | Decorator |

## Anti-Patterns to Avoid

### God Class

```java
// Bad: One class does everything
public class Utils {
    public static String signXml(...) { }
    public static boolean validateCnpj(...) { }
    public static void sendHttp(...) { }
    public static String formatDate(...) { }
}

// Good: Separate by responsibility
XmlSigner.sign(...)
CNPJValidator.isValid(...)
HttpClient.send(...)
DateFormatter.format(...)
```

### Anemic Domain Model

```java
// Bad: Just data, no behavior
public class CNPJ {
    private String value;
    // getters and setters only
}

// Good: Rich domain model
public class CNPJ {
    private final String value;
    
    public boolean isValid() { }
    public String format() { }
    public String getRaiz() { }
}
```

### Primitive Obsession

```java
// Bad: Using String for domain concepts
public void process(String cnpj) { }

// Good: Using value objects
public void process(CNPJ cnpj) { }
```
