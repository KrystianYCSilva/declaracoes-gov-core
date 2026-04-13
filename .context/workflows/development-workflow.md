---
name: development-workflow
description: |
  Step-by-step workflow for developing features in declaracoes-gov-core.
  Use when: implementing new features or fixing bugs.
---

# Development Workflow

## Pre-Development Checklist

Before starting work:

- [ ] Read `.context/standards/architectural-rules.md`
- [ ] Read `.context/_meta/codebase-map.md`
- [ ] Understand the domain (certificates, XML signature, or validators)
- [ ] Check for existing related issues or PRs

## Phase 1: Design

### 1.1 Define the Interface

Start with what the consumer will use:

```java
// How will users call this?
CertificadoManager cert = P12CertificadoLoader.load(path, password);
String signed = new EnvelopedXmlSigner().sign(xml, config);
boolean valid = CNPJ.of(cnpjString).isValid();
```

### 1.2 Identify Extension Points

- Will consumers need to customize behavior? → Use SPI
- Are there multiple algorithms? → Use Strategy
- Complex object with optional params? → Use Builder

### 1.3 Check Thread Safety Requirements

All public classes in this library must be thread-safe:

- [ ] Can the class be immutable? (preferred)
- [ ] If mutable, use `ReadWriteLock` or `Atomic*` fields
- [ ] Document thread-safety in class Javadoc with `@apiNote`

## Phase 2: Implementation

### 2.1 Create Value Objects First

```java
public final class CertificadoInfo {
    private final String subjectCN;
    private final String issuerCN;
    private final LocalDateTime validFrom;
    private final LocalDateTime validTo;
    
    private CertificadoInfo(Builder builder) {
        this.subjectCN = builder.subjectCN;
        // ...
    }
    
    // Getters only, no setters
    // equals, hashCode, toString
}
```

### 2.2 Implement Core Logic

```java
public final class P12CertificadoLoader {
    
    private P12CertificadoLoader() {
        // Utility class
    }
    
    public static CertificadoManager load(Path path, char[] password) {
        Objects.requireNonNull(path, "Path is required");
        Objects.requireNonNull(password, "Password is required");
        
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream is = Files.newInputStream(path)) {
                keyStore.load(is, password);
            }
            
            // Clear password immediately
            Arrays.fill(password, '0');
            
            // Extract certificate and key
            // ...
            
            return new P12CertificadoManager(certificate, privateKey);
        } catch (Exception e) {
            throw new CertificadoException("Failed to load P12: " + path, e);
        }
    }
}
```

### 2.3 Add Comprehensive Javadoc

```java
/**
 * Loads a certificate from a PCKS#12 (.p12/.pfx) file.
 * 
 * <p>This method supports both A1 certificates stored in files.
 * The password array is cleared immediately after use for security.
 * 
 * @param path the path to the P12 file
 * @param password the password for the P12 file (will be cleared)
 * @return a certificate manager for the loaded certificate
 * @throws CertificadoException if loading fails
 * @throws IllegalArgumentException if path or password is null
 * @apiNote This method is thread-safe. The password array is cleared 
 *          before the method returns.
 * @implNote Uses standard Java KeyStore with PKCS12 type.
 */
```

## Phase 3: Testing

### 3.1 Unit Tests

Create `src/test/java` mirror structure:

```java
@RunWith(MockitoJUnitRunner.class)
public class P12CertificadoLoaderTest {
    
    @Test
    public void shouldLoadValidP12() throws Exception {
        // Arrange
        Path testP12 = Paths.get("src/test/resources/certs/test.p12");
        char[] password = "test123".toCharArray();
        
        // Act
        CertificadoManager manager = P12CertificadoLoader.load(testP12, password);
        
        // Assert
        assertThat(manager.getCertificate()).isNotNull();
        assertThat(manager.getPrivateKey()).isNotNull();
    }
    
    @Test
    public void shouldThrowWhenPathIsNull() {
        assertThatThrownBy(() -> P12CertificadoLoader.load(null, "pass".toCharArray()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Path is required");
    }
    
    @Test
    public void shouldThrowWhenPasswordIsWrong() {
        Path testP12 = Paths.get("src/test/resources/certs/test.p12");
        
        assertThatThrownBy(() -> P12CertificadoLoader.load(testP12, "wrong".toCharArray()))
            .isInstanceOf(CertificadoException.class)
            .hasCauseInstanceOf(IOException.class);
    }
}
```

### 3.2 Thread-Safety Tests

For concurrent classes:

```java
class XmlSignerConcurrencyTest {
    
    @Test
    public void shouldHandleConcurrentSigning() throws InterruptedException {
        XmlSigner signer = new EnvelopedXmlSigner();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        AtomicInteger failures = new AtomicInteger(0);
        
        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                try {
                    String signed = signer.sign(xml, config);
                    if (!signer.verify(signed)) {
                        failures.incrementAndGet();
                    }
                } catch (Exception e) {
                    failures.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();
        
        assertThat(failures.get()).isZero();
    }
}
```

### 3.3 Coverage Check

```bash
mvn jacoco:report
cat target/site/jacoco/index.html
```

Ensure the module-specific JaCoCo gates configured in the POMs remain green. The default target is 90% line / 90% branch, with a documented crypto line exception.

## Phase 4: Quality Checks

### 4.1 Compile

```bash
mvn -q -DskipTests compile
```

No warnings should be present.

### 4.2 Full Verification

```bash
mvn -q verify
```

Must pass all checks.

## Phase 5: Documentation

### 5.1 Update .context

If changing behavior or adding features:

1. Update `.context/_meta/codebase-map.md` with new classes
2. Update `.context/_meta/key-decisions.md` with new ADRs
3. Update relevant pattern docs if introducing new patterns

### 5.2 Update docs/

Create/update human-facing docs in Portuguese:

- `docs/API.md` - Public API documentation
- `docs/EXAMPLES.md` - Usage examples
- `docs/CHANGELOG.md` - Version changes

## Phase 6: Review

### Self-Review Checklist

- [ ] Code follows style guide
- [ ] All public APIs have Javadoc
- [ ] Thread-safety documented
- [ ] Tests cover success and failure paths
- [ ] No Java 9+ features used
- [ ] No secrets in code
- [ ] `mvn verify` passes

### Submit PR

1. Commit with descriptive message:
   ```
   feat(certificado): Add A3 certificate support
   
   - Implement PKCS#11 loader for hardware tokens
   - Add slot and PIN configuration
   - Include thread-safety tests
   
   Fixes #123
   ```

2. Push branch
3. Create PR with description
4. Ensure CI passes

## Common Tasks

### Adding a New Validator

1. Create value object in `documento.model`
2. Create validator in `documento.validator`
3. Add formatter in `documento.util`
4. Write comprehensive tests
5. Document in `.context/`

### Adding a New Certificate Type

1. Implement `CertificadoManager` interface
2. Create loader implementing `CertificadoLoader` SPI
3. Register in `META-INF/services/`
4. Add integration tests with test certificate
5. Document password clearing behavior

### Modifying XML Signature

1. Check eSocial/EFD-Reinf specifications
2. Ensure algorithm compatibility
3. Test with real government endpoints if possible
4. Document any breaking changes

## Debugging Tips

### Certificate Loading Issues

```bash
# Enable SSL debug
java -Djavax.net.debug=ssl,handshake -jar app.jar

# Check certificate
keytool -v -list -keystore cert.p12 -storetype PKCS12
```

### XML Signature Issues

```bash
# Validate XML
xmllint --schema schema.xsd signed.xml --noout

# Check signature
xmlsec1 --verify --pubkey-pem cert.pem signed.xml
```

### Performance Issues

```bash
# Profile with JMH
mvn jmh:run

# Check for object allocation
java -XX:+PrintGCDetails -jar app.jar
```

## Emergency Fixes

For critical bugs:

1. Create hotfix branch from `main`
2. Fix with minimal changes
3. Add regression test
4. Fast-track review
5. Release immediately
