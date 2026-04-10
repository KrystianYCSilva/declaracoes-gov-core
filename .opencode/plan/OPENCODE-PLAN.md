# OPENCODE-PLAN: declaracoes-gov-core

## 1. Context & Motivation

O **declaracoes-gov-core** é a biblioteca de núcleo compartilhado que extrai funcionalidades comuns a todas as declarações governamentais brasileiras. Seguindo o padrão do eSocial, esta lib será agnóstica e não terá dependência de nenhum layout específico.

### Padrões Identificados no eSocial Transmitter

| Componente | Reusabilidade | Destino no Core |
|------------|---------------|-----------------|
| `CertificateProvider` | 100% | `br.gov.core.security` |
| `SslContextFactory` | 100% | `br.gov.core.security` |
| `XmlSigner` | 100% | `br.gov.core.signature` |
| `XmlDsigSigner` | 100% | `br.gov.core.signature` |
| `Validators` | 100% | `br.gov.core.validation` |
| `XmlDocuments` | 100% | `br.gov.core.util` |
| `XmlDates` | 100% | `br.gov.core.util` |
| `TipoAmbiente` | 100% | `br.gov.core.domain` |
| `TipoInscricao` | 100% | `br.gov.core.domain` |

### Dependências Externas (Extraídas)

- **JAXB API/Runtime**: Para marshalling XML
- **Apache Santuario**: Para XMLDSIG
- **Jackson**: Para JSON (comum a todas APIs REST)
- **Java 8+**: Target mínimo

---

## 2. Goals

1. **Segurança Mútua (mTLS)**: Carregamento de certificados A1 (PKCS12) e A3 (PKCS11)
2. **Assinatura Digital**: XMLDSIG conforme ICP-Brasil
3. **Validadores**: CNPJ, CPF, NIS, CEI, CNO, recibos, protocolos
4. **Utilitários**: XML parsing, datas, JSON genérico
5. **Domínio Comum**: Enumerações compartilhadas (Ambiente, TipoInscricao)
6. **Thread-Safe**: Toda a biblioteca deve ser thread-safe

---

## 3. Structure

```
declaracoes-gov-core/
├── src/
│   ├── main/
│   │   └── java/br/gov/core/
│   │       ├── CoreVersion.java                    # Version tracking
│   │       ├── CoreException.java               # Base exception
│   │       ├── security/
│   │       │   ├── CertificateProvider.java    # Interface
│   │       │   ├── AbstractKeyStoreProvider.java # Base class
│   │       │   ├── Pkcs12CertificateProvider.java  # A1
│   │       │   ├── Pkcs11CertificateProvider.java  # A3
│   │       │   ├── KeyStoreFactory.java        # Factory
│   │       │   └── SslContextFactory.java      # mTLS setup
│   │       ├── signature/
│   │       │   ├── XmlSigner.java              # Interface
│   │       │   ├── XmlDsigSigner.java         # Implementation
│   │       │   └── SignatureConfig.java        # Config
│   │       ├── validation/
│   │       │   ├── Validators.java            # Entry point
│   │       │   ├── CnpjValidator.java         # CNPJ + Alfanumérico
│   │       │   ├── CpfValidator.java         # CPF
│   │       │   ├── NisValidator.java         # PIS/PASEP
│   │       │   ├── CeiValidator.java         # CEI
│   │       │   ├── CnoValidator.java         # CNO
│   │       │   └── DocumentValidator.java    # Genérico
│   │       ├── domain/
│   │       │   ├── Environment.java           # PROD/RESTRITA
│   │       │   ├── TaxIdType.java            # CNPJ/CPF/CAEPF/CNO/CEI/CGC
│   │       │   └── PeriodoApuracao.java     # AAAAMM helper
│   │       ├── util/
│   │       │   ├── XmlHelper.java            # XML manipulation
│   │       │   ├── XmlDates.java            # Date conversions
│   │       │   ├── JsonHelper.java          # JSON utilities
│   │       │   └── StringHelper.java       # String utils
│   │       └── exception/
│   │           ├── CoreException.java        # Base
│   │           ├── CertificateException.java  # Cert errors
│   │           └── SignatureException.java    # Signature errors
│   └── test/
│       └── java/br/gov/core/
├── pom.xml
├── README.md
├── ARCHITECTURE.md
├── CHANGELOG.md
└── OPENCODE-PLAN.md
```

---

## 4. Security Module (br.gov.core.security)

### 4.1 CertificateProvider Interface

```java
public interface CertificateProvider extends Closeable {
    KeyStore getKeyStore();
    char[] getKeyPassword();
    String getKeyAlias();
    PrivateKey getPrivateKey();
    X509Certificate getCertificate();
    X509Certificate[] getCertificateChain();
    
    static Pkcs12CertificateProvider fromFile(Path file, char[] password) { ... }
    static Pkcs11CertificateProvider fromToken(String config) { ... }
}
```

### 4.2 Pkcs12CertificateProvider (Certificados A1)

```java
public class Pkcs12CertificateProvider implements CertificateProvider {
    private final Path file;
    private final char[] password;
    private final KeyStore keyStore;
    
    public Pkcs12CertificateProvider(Path file, char[] password) {
        this.file = file;
        this.password = password.clone();  // Defensive copy
        this.keyStore = loadKeyStore();
    }
    
    private KeyStore loadKeyStore() {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        try (InputStream in = Files.newInputStream(file)) {
            ks.load(in, password);
        }
        return ks;
    }
}
```

### 4.3 Pkcs11CertificateProvider (Certificados A3)

```java
public class Pkcs11CertificateProvider implements CertificateProvider {
    private static final String PKCS11_PREFIX = "SunPKCS11-";
    
    public Pkcs11CertificateProvider(String pkcs11Config) {
        Provider p = getPkcs11Provider();
        this.keyStore = KeyStore.getInstance("PKCS11", p);
        this.keyStore.load(null, pin);
    }
    
    private Provider getPkcs11Provider() {
        // Handle JDK version differences (SunPKCS11 constructor)
        // JDK 9+: Uses Provider configure method
        // JDK 8: Uses constructor with config string
    }
}
```

### 4.4 SslContextFactory

```java
public final class SslContextFactory {
    private static final String[] ENABLED_CIPHERS = {
        "TLS_RSA_WITH_AES_128_GCM_SHA256",
        "TLS_RSA_WITH_AES_256_GCM_SHA384",
        "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
        "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"
    };
    
    public SSLContext createSslContext(
            CertificateProvider certProvider,
            KeyStore trustStore
    ) {
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("NewKeyStore");
        kmf.init(certProvider.getKeyStore(), certProvider.getKeyPassword());
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(trustStore);
        
        SSLContext ssl = SSLContext.getInstance("TLSv1.2");
        ssl.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        return ssl;
    }
    
    public static SSLContext createDefault() {
        // Use system default trust store
    }
}
```

---

## 5. Signature Module (br.gov.core.signature)

### 5.1 XmlSigner Interface

```java
public interface XmlSigner {
    String sign(String xml);
    String sign(Element element);
    boolean isSigned(String xml);
}
```

### 5.2 XmlDsigSigner Implementation

```java
public class XmlDsigSigner implements XmlSigner {
    private final CertificateProvider certificateProvider;
    private final SignatureConfig config;
    
    public XmlDsigSigner(CertificateProvider provider) {
        this(provider, SignatureConfig.DEFAULT);
    }
    
    @Override
    public String sign(String xml) {
        Document doc = XmlHelper.parse(xml);
        sign(doc.getDocumentElement());
        return XmlHelper.toString(doc);
    }
    
    private void sign(Element element) {
        // 1. Create SignedInfo
        SignedInfo si = new SignedInfo(
            Canonicalizer.ALGO_ID_C14N_INCLUSIVE,
            SignatureMethod.RSA_SHA256,
            DigestMethod.SHA256
        );
        
        // 2. Add reference
        String id = element.getAttribute("id");
        si.addReference("#" + id);
        
        // 3. Sign
        Signature sig = Signature.getInstance("RSA_SHA256", "SunJSSE");
        sig.initSign(certificateProvider.getPrivateKey());
        sig.update(si.getSignedInfo());
        sig.sign();
        
        // 4. Add KeyInfo with certificate
        KeyInfo ki = new KeyInfo();
        ki.add(certificateProvider.getCertificate());
        
        // 5. Build final XML
        // ...
    }
}
```

### 5.3 SignatureConfig

```java
public static class SignatureConfig {
    public static final SignatureConfig DEFAULT = builder().build();
    
    private final String canonicalization;
    private final String signatureMethod;
    private final String digestMethod;
    private final String keyInfoProvider;
    
    public static Builder builder() { return new Builder(); }
}
```

---

## 6. Validation Module (br.gov.core.validation)

### 6.1 CNPJ Validator (Inclui Alfanumérico 2026+)

```java
public final class CnpjValidator {
    
    public static boolean isValid(String cnpj) {
        if (cnpj == null) return false;
        String digits = extractDigits(cnpj);
        if (digits.length() != 14) return false;
        if (isAllSameDigits(digits)) return false;
        
        return checkDv(digits, 12) && checkDv(digits, 13);
    }
    
    private static int checkDv(String digits, int position) {
        int[] weights = getWeights(position);
        int sum = 0;
        
        for (int i = 0; i < position; i++) {
            int value = getDigitValue(digits.charAt(i));
            sum += value * weights[i];
        }
        
        int remainder = sum % 11;
        int expectedDv = (remainder < 2) ? 0 : 11 - remainder;
        int actualDv = Character.getNumericValue(digits.charAt(position));
        
        return expectedDv == actualDv;
    }
    
    private static int getDigitValue(char c) {
        if (Character.isDigit(c)) {
            return Character.getNumericValue(c);
        }
        // Alfanumérico: ASCII - 48
        return c - 48; // 'A' = 65-48=17
    }
    
    private static int[] getWeights(int position) {
        // CNPJ numérico: 2-9
        // CNPJ alfanumérico: igual, mas valores das letras são ASCII-48
        int[] weights = new int[position];
        int weight = position == 12 ? 5 : 6;
        for (int i = position - 1; i >= 0; i--) {
            weights[i] = weight;
            weight = (weight == 2) ? 9 : weight - 1;
        }
        return weights;
    }
}
```

### 6.2 CPF Validator (Módulo 11)

```java
public final class CpfValidator {
    
    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        String digits = extractDigits(cpf);
        if (digits.length() != 11) return false;
        if (isAllSameDigits(digits)) return false;
        
        return checkDv(digits, 9) && checkDv(digits, 10);
    }
    
    private static boolean checkDv(String digits, int position) {
        int[] weights = new int[position];
        for (int i = 0; i < position; i++) {
            weights[i] = position + 1 - i; // 10,9,8... ou 11,10,9...
        }
        
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        
        int remainder = sum % 11;
        int expectedDv = (remainder < 2) ? 0 : 11 - remainder;
        int actualDv = digits.charAt(position) - '0';
        
        return expectedDv == actualDv;
    }
}
```

### 6.3 Validators Entry Point

```java
public final class Validators {
    
    public static void assertCnpj(String cnpj) {
        if (!CnpjValidator.isValid(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido: " + cnpj);
        }
    }
    
    public static void assertCpf(String cpf) {
        if (!CpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
    }
    
    public static void assertInscricao(String tipo, String numero) {
        switch (tipo) {
            case "1": assertCnpj(numero); break;
            case "2": assertCpf(numero); break;
            case "3": assertCaepf(numero); break;
            case "4": assertCno(numero); break;
            case "6": assertCei(numero); break;
            default: throw new IllegalArgumentException("Tipo inscrição inválido: " + tipo);
        }
    }
    
    public static boolean isValidInscricao(String tipo, String numero) {
        try {
            assertInscricao(tipo, numero);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

---

## 7. Domain Module (br.gov.core.domain)

### 7.1 Environment Enum

```java
public enum Environment {
    PRODUCAO(1, "Produção", "https://"),
    PRODUCAO_RESTRITA(2, "Produção Restrita", "https://pre-");
    
    private final int code;
    private final String description;
    private final String urlPrefix;
    
    public static Environment fromCode(int code) { ... }
    public static Environment fromCode(String code) { ... }
}
```

### 7.2 TaxIdType Enum

```java
public enum TaxIdType {
    CNPJ(1, "CNPJ", 14, "CNPJ"),
    CPF(2, "CPF", 11, "CPF"),
    CAEPF(3, "CAEPF", 11, "CAEPF"),
    CNO(4, "CNO", 14, "CNO"),
    CGC(5, "CGC", 14, "CGC"),
    CEI(6, "CEI", 12, "CEI");
    
    private final int code;
    private final String description;
    private final int length;
    private final String label;
    
    public static TaxIdType fromCode(int code) { ... }
    public static TaxIdType fromCode(String code) { ... }
    public static TaxIdType fromDigits(String digits) { ... }
}
```

### 7.3 PeriodoApuracao

```java
public final class PeriodoApuracao implements Comparable<PeriodoApuracao> {
    private final int ano;
    private final int mes;
    
    public PeriodoApuracao(int ano, int mes) { ... }
    public static PeriodoApuracao parse(String formatoAAAAMM) { ... }
    public static PeriodoApuracao current() { ... }
    public static PeriodoApuracao previous() { ... }
    public static PeriodoApuracao next() { ... }
    
    @Override
    public String toString() {
        return String.format("%04d%02d", ano, mes);
    }
    
    public String toDisplay() {
        return String.format("%02d/%04d", mes, ano);
    }
}
```

---

## 8. Util Module (br.gov.core.util)

### 8.1 XmlHelper

```java
public final class XmlHelper {
    
    public static Document parse(String xml) {
        return parse(xml, false);
    }
    
    public static Document parse(String xml, boolean namespaceAware) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        // ... security hardening
        return factory.newDocumentBuilder().parse(
            new InputSource(new StringReader(xml))
        );
    }
    
    public static String toString(Node node) {
        return toString(node, true);
    }
    
    public static String toString(Node node, boolean prettyPrint) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        if (prettyPrint) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        }
        
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }
    
    public static Element findElementById(Element root, String id) {
        // Recursive search with namespace awareness
    }
    
    public static boolean hasSignature(Element root) {
        return findElementById(root, "Signature") != null ||
               root.getElementsByTagNameNS("http://www.w3.org/2000/09/xmldsig#", "Signature").getLength() > 0;
    }
}
```

### 8.2 XmlDates

```java
public final class XmlDates {
    
    private static final DateTimeFormatter XML_DATE_TIME = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][XXX]");
    
    private static final DateTimeFormatter XML_DATE = 
        DateTimeFormatter.ISO_LOCAL_DATE;
    
    public static String toXmlDateTime(OffsetDateTime dateTime) {
        return dateTime.format(XML_DATE_TIME);
    }
    
    public static OffsetDateTime toOffsetDateTime(String xmlDateTime) {
        return OffsetDateTime.parse(xmlDateTime, XML_DATE_TIME);
    }
    
    public static String toXmlDate(LocalDate date) {
        return date.format(XML_DATE);
    }
    
    public static LocalDate toLocalDate(String xmlDate) {
        return LocalDate.parse(xmlDate, XML_DATE);
    }
}
```

### 8.3 JsonHelper

```java
public final class JsonHelper {
    
    private static final ObjectMapper MAPPER = createMapper();
    
    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
    
    public static String toJson(Object obj) {
        return MAPPER.writeValueAsString(obj);
    }
    
    public static <T> T fromJson(String json, Class<T> type) {
        return MAPPER.readValue(json, type);
    }
    
    public static <T> T fromJson(String json, TypeRef<T> type) {
        return MAPPER.readValue(json, new TypeReference<T>() {});
    }
    
    public static JsonNode parse(String json) {
        return MAPPER.readTree(json);
    }
}
```

---

## 9. Exception Hierarchy

```
CoreException (RuntimeException)
├── CertificateException
│   ├── KeyStoreException
│   ├── CertificateExpiredException
│   └── CertificateNotFoundException
└── SignatureException
    ├── SigningException
    └── VerificationException
```

---

## 10. Thread Safety

Todas as classes são **imutáveis** após construção, exceto:

1. **SslContextFactory**: Pode cachear contextos criados
2. **KeyStore**: Carregado lazily e cacheado
3. **ObjectMapper**: Jackson é thread-safe

```java
// Exemplo: XmlDsigSigner é stateless
public class XmlDsigSigner implements XmlSigner {
    private final CertificateProvider provider;
    private final SignatureConfig config;
    // Construtor apenas - sem estado mutável
    // Todos os métodos são stateless
}
```

---

## 11. Dependencies (pom.xml)

```xml
<dependencies>
    <!-- XML Processing -->
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.1</version>
    </dependency>
    <dependency>
        <groupId>org.glassfish.jaxb</groupId>
        <artifactId>jaxb-runtime</artifactId>
        <version>2.3.9</version>
    </dependency>
    
    <!-- XML Signature -->
    <dependency>
        <groupId>javax.xml.crypto</groupId>
        <artifactId>xmlsec</artifactId>
        <version>3.0.1</version>
    </dependency>
    
    <!-- JSON -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.4</version>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
        <version>2.15.4</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 12. API Preview

```java
// Certificado
CertificateProvider cert = Pkcs12CertificateProvider.fromFile(
    Paths.get("cert.pfx"), "senha".toCharArray()
);

// SSL Context
SslContextFactory sslFactory = new SslContextFactory();
SSLContext ssl = sslFactory.createSslContext(cert, null);

// Assinatura
XmlSigner signer = new XmlDsigSigner(cert);
String signedXml = signer.sign(eventoXml);

// Validação
Validators.assertCnpj("12.345.678/0001-99");
Validators.assertCpf("123.456.789-00");

// Utilitários
Document doc = XmlHelper.parse(xml);
String pretty = XmlHelper.toString(doc, true);

OffsetDateTime dt = XmlDates.toOffsetDateTime("2025-01-15T10:30:00");
String formatted = XmlDates.toXmlDateTime(dt);

// Domínio
Environment env = Environment.PRODUCAO_RESTRITA;
TaxIdType type = TaxIdType.CNPJ;
PeriodoApuracao periodo = PeriodoApuracao.parse("202501");
```

---

## 13. Implementation Phases

### Phase 1: Core Foundation
1. Criar pom.xml com todas dependências
2. Implementar exceptions hierarchy
3. Implementar domain enums

### Phase 2: Security Module
4. CertificateProvider interface
5. Pkcs12CertificateProvider
6. Pkcs11CertificateProvider
7. SslContextFactory

### Phase 3: Validation Module
8. CnpjValidator (com suporte alfanumérico)
9. CpfValidator
10. NisValidator, CeiValidator, CnoValidator
11. Validators facade

### Phase 4: Signature Module
12. XmlSigner interface
13. XmlDsigSigner implementation
14. SignatureConfig

### Phase 5: Util Module
15. XmlHelper
16. XmlDates
17. JsonHelper

### Phase 6: Testing
18. Testes unitários (>90% coverage)
19. Jacoco setup
