---
name: testing-strategy
description: |
  Testing strategy, standards, and requirements for declaracoes-gov-core.
  Tier T1 - normative standards for testing.
---

# Testing Strategy

## Testing Pyramid

```
    /\
   /  \  Integration Tests (10%)
  /----\   - Certificate loading with real keystore
 /      \  - mTLS connection tests
/--------\
   /\
  /  \   Unit Tests (primary)
 /----\    - Business logic
/      \   - Validators
/--------\ - Signatures
  /\
 /  \    Contract/Static Tests (10%)
/----\     - JaCoCo coverage
/      \   - Contract/resource validation
/--------\ - Build verification
```

## Test Categories

### 1. Unit Tests (Primary)

**Scope**: Individual classes and methods in isolation.

**Tools**: JUnit 4.13.x and focused helper utilities already present in the repository

**Location**: `src/test/java` mirroring `src/main/java`

**Naming**: `*Test.java` or `Test*.java`

**Requirements**:
- Every public method must have tests
- Test both success and failure paths
- Test boundary conditions
- Mock external dependencies

```java
@RunWith(MockitoJUnitRunner.class)
public class CNPJValidatorTest {
    
    private final CNPJValidator validator = new CNPJValidator();
    
    @Test
    public void shouldValidateCorrectNumericCnpj() {
        assertTrue(validator.isValid("12345678000195"));
    }
    
    @Test
    public void shouldValidateCorrectAlphanumericCnpj() {
        assertTrue(validator.isValid("12ABC67801X295"));
    }
    
    @Test
    public void shouldRejectCnpjWithInvalidCheckDigits() {
        assertFalse(validator.isValid("12345678000100"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNullCnpj() {
        validator.validate(null);
    }
}
```

### 2. Integration Tests

**Scope**: Multiple components working together.

**Tools**: JUnit 4.13.x, real test keystores (dummy credentials)

**Location**: `src/test/java/**/integration/`

**Naming**: `*IntegrationTest.java`

**Requirements**:
- Use real XML Security implementation
- Load actual test keystores (not mocks)
- Test end-to-end flows

```java
class XmlSigningIntegrationTest {
    
    @Test
    void shouldSignAndVerifyXmlWithRealCertificate() throws Exception {
        // Load test certificate (dummy)
        CertificadoManager cert = P12CertificadoLoader.load(
            Paths.get("src/test/resources/certs/test.p12"),
            "test123".toCharArray()
        );
        
        XmlSigner signer = new EnvelopedXmlSigner();
        String xml = loadTestXml();
        
        String signed = signer.sign(xml, SigningConfig.builder()
            .certificate(cert.getCertificate())
            .privateKey(cert.getPrivateKey())
            .build());
        
        assertThat(signer.verify(signed)).isTrue();
    }
}
```

### 3. Thread-Safety Tests

**Scope**: Concurrent access to shared state.

**Tools**: JUnit 4.13.x and standard JDK concurrency primitives

**Location**: `src/test/java/**/concurrent/`

**Naming**: `*ConcurrencyTest.java`

**Requirements**:
- Test with multiple threads (≥ 10)
- Run multiple iterations (≥ 1000)
- Verify no race conditions

```java
class XmlSignerConcurrencyTest {
    
    @Test
    void shouldBeThreadSafe() throws InterruptedException {
        XmlSigner signer = new EnvelopedXmlSigner();
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(1000);
        
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                try {
                    String signed = signer.sign(xml, config);
                    assertThat(signer.verify(signed)).isTrue();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue();
        executor.shutdown();
    }
}
```

## Test Data Management

### Test Certificates

- Store in `src/test/resources/certs/`
- Use only test/dummy certificates
- Never commit real production certificates
- Document password in test class (not in resource files)

### Test XML Files

- Store in `src/test/resources/xml/`
- Include various formats:
  - Valid unsigned XML
  - Valid signed XML
  - Invalid XML (malformed)
  - XML with special characters

### Test Vectors

For validators, include known good/bad values:

```java
class CNPJTestVectors {
    static final String[] VALID_NUMERIC = {
        "12345678000195",
        "00000000000191"
    };
    
    static final String[] INVALID = {
        "11111111111111",  // All same digits
        "12345678000100",  // Wrong check digits
        "12345678",        // Too short
        null,              // Null
        "",                // Empty
        "abcdefghijklmn"   // Non-numeric (pre-2026)
    };
}
```

## Coverage Requirements

### Minimum Thresholds

| Metric | Minimum | Ideal |
|--------|---------|-------|
| Line Coverage | 90% | 95% |
| Branch Coverage | 90% | 95% |
| Method Coverage | 90% | 95% |
| Class Coverage | 90% | 100% |

### JaCoCo Configuration

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <configuration>
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>LINE</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.90</minimum>
                    </limit>
                    <limit>
                        <counter>BRANCH</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.90</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
    </configuration>
</plugin>
```

### Exclusions from Coverage

- Generated code (JAXB)
- Equals/hashCode/toString
- Private constructors (utility classes)
- Main methods (CLI entry points)

## Test Execution

### Maven Profiles

```bash
# Unit tests only (fast)
mvn test

# All tests including integration
mvn test -Pintegration-tests

# With coverage
mvn verify

# Skip tests (CI only)
mvn verify -DskipTests
```

### IDE Integration

- Run individual test: Click gutter icon in IDE
- Run package tests: Right-click package → Run Tests
- Debug test: Set breakpoint, run in debug mode

## Test Quality Guidelines

### Good Tests

```java
@Test
void shouldRejectCnpjWithAllSameDigits() {
    // Clear name describes expected behavior
    String allSame = "11111111111111";
    
    assertThat(validator.isValid(allSame))
        .as("CNPJ with all same digits should be invalid")
        .isFalse();
}
```

### Bad Tests

```java
@Test
void test1() {  // Bad: Non-descriptive name
    CNPJ c = new CNPJ("123");  // Bad: Short variable names
    assertEquals(true, c.isValid());  // Bad: Wrong order (expected, actual)
}
```

### Test Independence

- Each test must be independent
- No shared mutable state between tests
- Use `@BeforeEach` for setup, not shared fields

### Deterministic Tests

- Tests must produce same result every run
- No random data without seeded Random
- No dependencies on system time (use Clock)

## Property-Based Testing (Optional)

For validators, consider property-based tests:

```java
@Property
void shouldRejectAnyInvalidCnpj(@ForAll("invalidCnpjs") String cnpj) {
    assertThat(validator.isValid(cnpj)).isFalse();
}

@Provide
Arbitrary<String> invalidCnpjs() {
    return Arbitraries.of(
        "11111111111111",
        "00000000000000",
        "abcdefghijklmn"
    );
}
```

## Debugging Failed Tests

1. **Reproduce locally**: `mvn test -Dtest=ClassName#methodName`
2. **Add logging**: Use SLF4J with test scope
3. **Check thread-safety**: Run with `-Dconcurrency=10`
4. **Verify test data**: Check resources are loaded correctly

## CI/CD Integration

Tests run automatically on:
- Pull request creation
- Push to main branch
- Release tag creation

Failed tests block merge.
