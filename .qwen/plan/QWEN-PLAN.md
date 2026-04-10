# QWEN-PLAN: Declaracoes Gov Core

## Objetivo Central

Criar uma biblioteca Java **agnostica, thread-safe e eficiente** que extrai componentes compartilhados por TODAS as declaracoes governamentais brasileiras (eSocial, EFD-Reinf, MIT, DCTFWeb, DEFIS, PGDAS-D, PGMEI), eliminando duplicacao de codigo e resolvendo o problema de ter 7+ copias do mesmo codigo de certificado, assinatura, validadores e utilitarios.

### Princípios Fundamentais

1. **Agnostica**: Sem dependencia de declaracao especifica — nenhum import de `br.uem.npd.esocial.*` ou `br.uem.npd.reinf.*`
2. **Thread-Safe**: Todos os componentes devem ser seguros para uso concorrente
3. **Eficiente**: Zero overhead desnecessario, lazy loading onde apropriado
4. **Lib Pura**: Sem Bean Validation, sem annotations de framework — apenas Java puro
5. **Java 8+**: Compatibilidade minima com Java 8

---

## Analise Profunda: O Que E Comum?

### 🔍 Declaracoes Analisadas

| Declaracao | Protocolo | Certificado | Assinatura XML | Validadores | Ambiente |
|------------|-----------|-------------|----------------|-------------|----------|
| **eSocial** | SOAP/WSDL | A1/A3 ICP-Brasil | XMLDSIG RSA-SHA256 | CNPJ, CPF, NIS, CEI, CAEPF, CNO | Producao/Restrita |
| **EFD-Reinf** | REST | A1/A3 ICP-Brasil | XMLDSIG RSA-SHA256 | CNPJ, CPF, CAEPF, CNO | Producao/Restrita |
| **MIT** | REST (Integra Contador) | A1/A3 ICP-Brasil + OAuth2 | Nao (mTLS basta) | CNPJ | Producao/Restrita |
| **DCTFWeb** | REST (Integra Contador) | A1/A3 ICP-Brasil + OAuth2 | Nao (mTLS basta) | CNPJ | Producao/Restrita |
| **DEFIS** | SOAP + REST | A1/A3 ICP-Brasil | Opcional | CNPJ, CPF | Producao/Restrita |
| **PGDAS-D** | SOAP + REST | A1/A3 ICP-Brasil | Opcional | CNPJ, CPF | Producao/Restrita |
| **PGMEI** | REST | A1/A3 ICP-Brasil ou Gov.br | Nao | CNPJ MEI | Producao/Restrita |

### ✅ Componentes 100% Compartilhados

| Componente | Usado Por | Status Atual | Acao |
|------------|-----------|--------------|------|
| **CertificateProvider (interface)** | eSocial, Reinf, MIT, DCTFWeb, DEFIS, PGDAS, PGMEI | Copiado em cada transmissor | **EXTRAIR PARA CORE** |
| **Pkcs12CertificateProvider** | TODAS | Copiado em cada transmissor | **EXTRAIR PARA CORE** |
| **Pkcs11CertificateProvider** | TODAS | Copiado em cada transmissor | **EXTRAIR PARA CORE** |
| **SslContextFactory** | TODAS | Copiado em cada transmissor | **EXTRAIR PARA CORE** |
| **XmlSigner (interface)** | eSocial, Reinf, DEFIS, PGDAS | Copiado | **EXTRAIR PARA CORE** |
| **XmlDsigSigner** | eSocial, Reinf | Copiado | **EXTRAIR PARA CORE** |
| **Validadores CNPJ/CPF** | TODAS | Regex basico em cada | **EXTRAIR + IMPLEMENTAR MODULO 11** |
| **TipoInscricao (enum)** | TODAS | Duplicado em cada leiaute | **EXTRAIR PARA CORE** |
| **TipoAmbiente (enum)** | TODAS | Duplicado em cada leiaute | **EXTRAIR PARA CORE** |
| **XmlDocuments (util)** | eSocial, Reinf | Copiado | **EXTRAIR PARA CORE** |
| **XmlDates (util)** | eSocial, Reinf | Copiado | **EXTRAIR PARA CORE** |
| **ProxyConfig** | eSocial, Reinf, MIT, DCTFWeb | Copiado | **EXTRAIR PARA CORE** |
| **Hierarquia de Excecoes** | TODAS | Padrao similar | **EXTRAIR PARA CORE** |

### ❌ Componentes ESPECIFICOS (NAO vão para o core)

| Componente | Motivo |
|------------|--------|
| SOAP Layer (SoapTransport, SoapEnvelopeBuilder, etc.) | Exclusivo do eSocial |
| EsocialClient / ReinfClient | Cada declaracao tem seu proprio cliente |
| Model classes de request/response | Especificas de cada declaracao |
| Endpoint catalogs | URLs e operacoes diferentes |
| EsocialVersion / ReinfVersion | Cada declaracao tem suas proprias versoes |
| ClasspathScanner | Especifico para descoberta de pacotes JAXB |

---

## Arquitetura Proposta

### 1. Estrutura de Diretorios

```
declaracoes-gov-core/
├── pom.xml                          # Build Maven, Java 8
├── README.md                        # Visao geral e exemplos
├── ARCHITECTURE.md                  # Decisoes de design
├── CHANGELOG.md                     # Historico (v1.0.0)
├── CONTRIBUTING.md                  # Guia de contribuicao
├── ONBOARDING.md                    # Quick start
├── LICENSE (Apache 2.0)
├── NOTICE
├── .qwen/
│   └── plan/
│       └── QWEN-PLAN.md             # Este arquivo
├── docs/
│   ├── 01-REQUISITOS.md
│   ├── 02-DESIGN.md
│   └── 03-PLANO-TESTES.md
├── src/main/java/br/uem/npd/govcore/
│   ├── crypto/
│   │   ├── CertificateProvider.java         # Interface (extraida do eSocial)
│   │   ├── AbstractKeyStoreProvider.java    # Base abstrata (extraida)
│   │   ├── Pkcs12Provider.java              # Certificado A1 (renomeado, extraido)
│   │   ├── Pkcs11Provider.java              # Certificado A3 (renomeado, extraido)
│   │   └── SslContextBuilder.java           # SSLContext para mTLS (renomeado, extraido)
│   ├── signature/
│   │   ├── XmlSigner.java                   # Interface (extraida)
│   │   └── XmlDsigSigner.java               # XMLDSIG RSA-SHA256 (extraido)
│   ├── validator/
│   │   ├── CnpjValidator.java               # CNPJ com modulo 11 (NOVO - aritmetico)
│   │   ├── CpfValidator.java                # CPF com modulo 11 (NOVO - aritmetico)
│   │   ├── CnpjAlefValidator.java           # CNPJ alfanumerico (NOVO - pos 2024)
│   │   ├── InscricaoValidator.java          # Interface para validadores de inscricao
│   │   └── GovValidators.java               # Facade estatico (agrupa todos)
│   ├── model/
│   │   ├── Cnpj.java                        # Value object CNPJ (NOVO)
│   │   ├── Cpf.java                         # Value object CPF (NOVO)
│   │   ├── Inscricao.java                   # Value object generico (NOVO)
│   │   └── PeriodoApuracao.java             # Mes/Ano reutilizavel (NOVO)
│   ├── table/
│   │   ├── TipoInscricao.java               # Enum compartilhado (extraido)
│   │   └── TipoAmbiente.java                # Enum compartilhado (extraido)
│   ├── config/
│   │   └── ProxyConfig.java                 # Proxy HTTP (extraido)
│   ├── xml/
│   │   ├── XmlDocuments.java                # DOM utils (extraido)
│   │   └── XmlDates.java                    # Date conversions (extraido)
│   └── exception/
│       ├── GovDeclarationException.java     # Excecao base (renomeada)
│       ├── CertificateException.java        # Certificado (extraida)
│       ├── SignatureException.java          # Assinatura (extraida)
│       └── ValidationException.java         # Validacao (NOVA)
└── src/test/java/br/uem/npd/govcore/
    ├── crypto/
    │   ├── Pkcs12ProviderTest.java
    │   ├── Pkcs11ProviderTest.java
    │   └── SslContextBuilderTest.java
    ├── signature/
    │   └── XmlDsigSignerTest.java
    ├── validator/
    │   ├── CnpjValidatorTest.java           # Casos reais de CNPJ
    │   ├── CpfValidatorTest.java            # Casos reais de CPF
    │   ├── CnpjAlefValidatorTest.java       # CNPJ alfanumerico
    │   └── GovValidatorsTest.java           # Testes de facade
    ├── model/
    │   ├── CnpjTest.java
    │   ├── CpfTest.java
    │   └── PeriodoApuracaoTest.java
    ├── xml/
    │   ├── XmlDocumentsTest.java
    │   └── XmlDatesTest.java
    └── exception/
        └── ExceptionsTest.java
```

### 2. Pacote: `crypto/` - Certificado Digital

#### CertificateProvider.java (Interface)

```java
package br.uem.npd.govcore.crypto;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Contrato agnostico para provedores de certificado digital ICP-Brasil.
 * <p>
 * Usado por: eSocial, EFD-Reinf, MIT, DCTFWeb, DEFIS, PGDAS-D, PGMEI.
 * Suporta certificados A1 (PKCS12) e A3 (PKCS11).
 */
public interface CertificateProvider {

    /**
     * KeyStore carregado (PKCS12 ou PKCS11).
     */
    KeyStore getKeyStore();

    /**
     * Senha do certificado (PIN para A3, senha do arquivo para A1).
     * Retorna copia defensiva para seguranca.
     */
    char[] getKeyPassword();

    /**
     * Alias da chave privada no KeyStore.
     */
    String getKeyAlias();

    /**
     * Chave privada para operacoes criptograficas (assinatura, mTLS).
     */
    PrivateKey getPrivateKey();

    /**
     * Certificado X509 (primeiro da cadeia).
     */
    X509Certificate getCertificate();

    /**
     * Cadeia completa de certificados X509.
     * Retorna copia defensiva.
     */
    X509Certificate[] getCertificateChain();
}
```

#### AbstractKeyStoreProvider.java (Base Abstrata)

```java
package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.CertificateException;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * Implementacao base para provedores de certificado baseados em KeyStore.
 * <p>
 * Responsabilidades:
 * <ul>
 *   <li>Descoberta automatica de alias (ou uso de alias preferido)</li>
 *   <li>Validacao de chave privada e cadeia X509</li>
 *   <li>Copias defensivas de senha e cadeia</li>
 * </ul>
 */
abstract class AbstractKeyStoreProvider implements CertificateProvider {

    private final KeyStore keyStore;
    private final char[] keyPassword;
    private final String keyAlias;
    private final PrivateKey privateKey;
    private final X509Certificate certificate;
    private final X509Certificate[] certificateChain;

    protected AbstractKeyStoreProvider(KeyStore keyStore, char[] keyPassword, String preferredAlias) {
        this.keyStore = keyStore;
        this.keyPassword = keyPassword == null ? new char[0] : Arrays.copyOf(keyPassword, keyPassword.length);
        try {
            this.keyAlias = discoverAlias(keyStore, preferredAlias);
            Key key = keyStore.getKey(this.keyAlias, this.keyPassword);
            if (!(key instanceof PrivateKey)) {
                throw new CertificateException("Alias informado nao contem chave privada: " + this.keyAlias);
            }
            this.privateKey = (PrivateKey) key;
            Certificate[] chain = keyStore.getCertificateChain(this.keyAlias);
            if (chain == null || chain.length == 0) {
                throw new CertificateException("Nao foi encontrada cadeia X509 para o alias: " + this.keyAlias);
            }
            this.certificateChain = new X509Certificate[chain.length];
            for (int i = 0; i < chain.length; i++) {
                if (!(chain[i] instanceof X509Certificate)) {
                    throw new CertificateException("A cadeia do certificado deve ser X509");
                }
                this.certificateChain[i] = (X509Certificate) chain[i];
            }
            this.certificate = this.certificateChain[0];
        } catch (CertificateException e) {
            throw e;
        } catch (Exception e) {
            throw new CertificateException("Falha ao ler material criptografico do KeyStore", e);
        }
    }

    private String discoverAlias(KeyStore keyStore, String preferredAlias) throws KeyStoreException {
        if (preferredAlias != null) {
            if (!keyStore.containsAlias(preferredAlias)) {
                throw new CertificateException("Alias nao encontrado no KeyStore: " + preferredAlias);
            }
            if (!keyStore.isKeyEntry(preferredAlias)) {
                throw new CertificateException("Alias nao possui chave privada: " + preferredAlias);
            }
            return preferredAlias;
        }
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (keyStore.isKeyEntry(alias)) {
                return alias;
            }
        }
        throw new CertificateException("Nenhum alias com chave privada foi encontrado no KeyStore");
    }

    @Override
    public KeyStore getKeyStore() {
        return keyStore;
    }

    @Override
    public char[] getKeyPassword() {
        return Arrays.copyOf(keyPassword, keyPassword.length);
    }

    @Override
    public String getKeyAlias() {
        return keyAlias;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public X509Certificate getCertificate() {
        return certificate;
    }

    @Override
    public X509Certificate[] getCertificateChain() {
        return Arrays.copyOf(certificateChain, certificateChain.length);
    }
}
```

#### Pkcs12Provider.java (Certificado A1)

```java
package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.CertificateException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;

/**
 * Provedor para certificado digital A1 (arquivo PKCS12).
 * <p>
 * Uso tipico:
 * <pre>
 * CertificateProvider provider = new Pkcs12Provider(
 *     Paths.get("certificado-a1.p12"),
 *     "senha".toCharArray()
 * );
 * </pre>
 *
 * @see <a href="https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/certificado-digital">Certificado Digital ICP-Brasil</a>
 */
public final class Pkcs12Provider extends AbstractKeyStoreProvider {

    /**
     * Cria provider com auto-descoberta de alias.
     *
     * @param path     Caminho para arquivo PKCS12 (.p12 ou .pfx)
     * @param password Senha do certificado
     */
    public Pkcs12Provider(Path path, char[] password) {
        this(path, password, null);
    }

    /**
     * Cria provider com alias especifico.
     *
     * @param path     Caminho para arquivo PKCS12
     * @param password Senha do certificado
     * @param alias    Alias da chave (null para auto-descobrir)
     */
    public Pkcs12Provider(Path path, char[] password, String alias) {
        super(load(path, password), password, alias);
    }

    private static KeyStore load(Path path, char[] password) {
        if (path == null) {
            throw new IllegalArgumentException("path nao pode ser nulo");
        }
        if (!Files.exists(path)) {
            throw new CertificateException("Arquivo PKCS12 nao encontrado: " + path);
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(inputStream, password);
            return keyStore;
        } catch (Exception e) {
            throw new CertificateException("Falha ao carregar arquivo PKCS12", e);
        }
    }
}
```

#### Pkcs11Provider.java (Certificado A3)

```java
package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.CertificateException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

/**
 * Provedor para certificado digital A3 (token/smartcard PKCS11).
 * <p>
 * Requer arquivo de configuracao PKCS11 no formato:
 * <pre>
 * name=TokenName
 * library=/caminho/para/dll/ou/so
 * </pre>
 *
 * <h3>Exemplo de uso:</h3>
 * <pre>
 * CertificateProvider provider = new Pkcs11Provider(
 *     Paths.get("pkcs11.cfg"),
 *     "123456".toCharArray()  // PIN do token
 * );
 * </pre>
 *
 * @see <a href="https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/certificado-digital/certificado-a3">Certificado A3 ICP-Brasil</a>
 */
public final class Pkcs11Provider extends AbstractKeyStoreProvider {

    public Pkcs11Provider(Path configurationFile, char[] pin) {
        this(configurationFile, pin, null);
    }

    public Pkcs11Provider(Path configurationFile, char[] pin, String alias) {
        this(loadProvider(configurationFile), pin, alias);
    }

    Pkcs11Provider(Provider provider, char[] pin, String alias) {
        super(loadKeyStore(provider, pin), pin, alias);
    }

    private static Provider loadProvider(Path configurationFile) {
        if (configurationFile == null) {
            throw new IllegalArgumentException("configurationFile nao pode ser nulo");
        }
        if (!Files.exists(configurationFile)) {
            throw new CertificateException("Arquivo de configuracao PKCS11 nao encontrado: " + configurationFile);
        }
        try {
            Class<?> providerClass = Class.forName("sun.security.pkcs11.SunPKCS11");
            try (InputStream inputStream = Files.newInputStream(configurationFile)) {
                try {
                    java.lang.reflect.Constructor<?> constructor = providerClass.getConstructor(InputStream.class);
                    Provider provider = (Provider) constructor.newInstance(inputStream);
                    Security.addProvider(provider);
                    return provider;
                } catch (NoSuchMethodException e) {
                    Provider baseProvider = Security.getProvider("SunPKCS11");
                    if (baseProvider == null) {
                        throw new CertificateException("Provider SunPKCS11 nao esta disponivel na JVM. Use JDK 11+ ou adicione o modulo jdk.crypto.cryptoki");
                    }
                    java.lang.reflect.Method configure = Provider.class.getMethod("configure", String.class);
                    Provider configured = (Provider) configure.invoke(baseProvider, configurationFile.toAbsolutePath().toString());
                    Security.addProvider(configured);
                    return configured;
                }
            }
        } catch (CertificateException e) {
            throw e;
        } catch (Exception e) {
            throw new CertificateException("Falha ao inicializar provider PKCS11", e);
        }
    }

    private static KeyStore loadKeyStore(Provider provider, char[] pin) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS11", provider);
            keyStore.load(null, pin);
            return keyStore;
        } catch (Exception e) {
            throw new CertificateException("Falha ao carregar KeyStore PKCS11", e);
        }
    }
}
```

#### SslContextBuilder.java (mTLS)

```java
package br.uem.npd.govcore.crypto;

import br.uem.npd.govcore.exception.CertificateException;
import org.apache.hc.core5.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

/**
 * Factory thread-safe para SSLContext com mTLS (autenticacao mutua).
 * <p>
 * Usado por todos os transmissores para configurar HTTPS com certificado ICP-Brasil.
 * <p>
 * Ciphers suportados:
 * <ul>
 *   <li>TLS_RSA_WITH_AES_128_GCM_SHA256</li>
 *   <li>TLS_RSA_WITH_AES_256_GCM_SHA384</li>
 *   <li>TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256</li>
 *   <li>TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384</li>
 * </ul>
 */
public final class SslContextBuilder {

    public static final String[] SUPPORTED_CIPHERS = {
            "TLS_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_RSA_WITH_AES_256_GCM_SHA384",
            "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"
    };

    /**
     * Cria SSLContext para mTLS.
     *
     * @param certificateProvider Provedor de certificado (obrigatorio)
     * @param trustStore          TrustStore opcional (null = truststore default da JVM)
     * @return SSLContext configurado para mTLS
     */
    public SSLContext build(CertificateProvider certificateProvider, KeyStore trustStore) {
        if (certificateProvider == null) {
            throw new IllegalArgumentException("certificateProvider nao pode ser nulo");
        }
        try {
            if (trustStore == null) {
                return SSLContexts.custom()
                        .loadKeyMaterial(certificateProvider.getKeyStore(), certificateProvider.getKeyPassword())
                        .loadTrustMaterial((KeyStore) null, null)
                        .build();
            }
            return SSLContexts.custom()
                    .loadKeyMaterial(certificateProvider.getKeyStore(), certificateProvider.getKeyPassword())
                    .loadTrustMaterial(trustStore, null)
                    .build();
        } catch (Exception e) {
            throw new CertificateException("Falha ao criar SSLContext para mTLS", e);
        }
    }

    /**
     * Cria SSLContext com truststore default da JVM.
     */
    public SSLContext build(CertificateProvider certificateProvider) {
        return build(certificateProvider, null);
    }
}
```

### 3. Pacote: `signature/` - Assinatura XML

#### XmlSigner.java (Interface)

```java
package br.uem.npd.govcore.signature;

/**
 * Contrato agnostico para assinatura XML.
 * <p>
 * Usado por: eSocial, EFD-Reinf, e possivelmente DEFIS/PGDAS-D.
 */
public interface XmlSigner {

    /**
     * Assina XML com XMLDSIG enveloped.
     * Se XML ja possui assinatura, retorna sem re-assinar.
     *
     * @param xml XML original (sem assinatura)
     * @return XML assinado com XMLDSIG
     */
    String sign(String xml);
}
```

#### XmlDsigSigner.java

```java
package br.uem.npd.govcore.signature;

import br.uem.npd.govcore.crypto.CertificateProvider;
import br.uem.npd.govcore.exception.SignatureException;
import br.uem.npd.govcore.xml.XmlDocuments;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.util.Collections;

/**
 * Assinador XML usando XMLDSIG enveloped com RSA-SHA256.
 * <p>
 * Algoritmos utilizados:
 * <ul>
 *   <li>Canonicalizacao: INCLUSIVE</li>
 *   <li>Assinatura: RSA-SHA256</li>
 *   <li>Digest: SHA-256</li>
 *   <li>Transform: ENVELOPED</li>
 *   <li>KeyInfo: X509Data com certificado</li>
 *   <li>Namespace prefix: ds</li>
 * </ul>
 * <p>
 * Usado por: eSocial (eventos), EFD-Reinf (eventos).
 * <p>
 * Thread-safe: SIM (sem estado mutavel por chamada).
 */
public final class XmlDsigSigner implements XmlSigner {

    private final CertificateProvider certificateProvider;

    public XmlDsigSigner(CertificateProvider certificateProvider) {
        if (certificateProvider == null) {
            throw new IllegalArgumentException("certificateProvider nao pode ser nulo");
        }
        this.certificateProvider = certificateProvider;
    }

    @Override
    public String sign(String xml) {
        if (xml == null || xml.trim().isEmpty()) {
            throw new IllegalArgumentException("xml nao pode ser vazio");
        }
        try {
            Document document = XmlDocuments.parse(xml);
            Element root = document.getDocumentElement();
            if (root == null) {
                throw new SignatureException("XML sem elemento raiz");
            }
            if (XmlDocuments.hasSignature(root)) {
                return xml;
            }

            XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
            Element target = XmlDocuments.findFirstElementWithAttribute(root, "Id");
            Reference reference = createReference(signatureFactory, root, target);

            SignedInfo signedInfo = signatureFactory.newSignedInfo(
                    signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                    signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA256, null),
                    Collections.singletonList(reference)
            );

            KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();
            X509Data x509Data = keyInfoFactory.newX509Data(Collections.singletonList(certificateProvider.getCertificate()));
            KeyInfo keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));

            DOMSignContext signContext = new DOMSignContext(certificateProvider.getPrivateKey(), target == null ? root : target);
            signContext.setDefaultNamespacePrefix("ds");

            XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, keyInfo);
            signature.sign(signContext);
            return XmlDocuments.toString(document);
        } catch (SignatureException e) {
            throw e;
        } catch (Exception e) {
            throw new SignatureException("Falha ao assinar XML", e);
        }
    }

    private Reference createReference(XMLSignatureFactory signatureFactory, Element root, Element target) throws Exception {
        Transform transform = signatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null);
        if (target != null) {
            target.setIdAttribute("Id", true);
            return signatureFactory.newReference(
                    "#" + target.getAttribute("Id"),
                    signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                    Collections.singletonList(transform),
                    null,
                    null
            );
        }
        return signatureFactory.newReference(
                "",
                signatureFactory.newDigestMethod(DigestMethod.SHA256, null),
                Collections.singletonList(transform),
                null,
                null
        );
    }
}
```

### 4. Pacote: `validator/` - Validadores Gov

#### CnpjValidator.java (Modulo 11 - ARITMETICO)

```java
package br.uem.npd.govcore.validator;

import java.util.Objects;

/**
 * Validador aritmetico de CNPJ (modulo 11).
 * <p>
 * Implementa o algoritmo oficial da Receita Federal para validacao
 * dos dois digitos verificadores do CNPJ.
 * <p>
 * Suporta formatos:
 * <ul>
 *   <li>14 digitos (ex: 12345678000199)</li>
 *   <li>Formatado (ex: 12.345.678/0001-99) - normalizado automaticamente</li>
 * </ul>
 *
 * @see <a href="https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastro/perguntas-e-respostas/cnpj-alfanumerico">CNPJ Alfanumerico - Serpro 2024</a>
 */
public final class CnpjValidator implements InscricaoValidator {

    private static final CnpjValidator INSTANCE = new CnpjValidator();

    private CnpjValidator() {}

    public static CnpjValidator getInstance() {
        return INSTANCE;
    }

    /**
     * Valida CNPJ pelo algoritmo modulo 11.
     *
     * @param cnpj CNPJ (14 digitos ou formatado)
     * @return true se valido
     */
    public boolean isValid(String cnpj) {
        if (cnpj == null) return false;
        String digits = normalize(cnpj);
        if (digits.length() != 14) return false;
        if (!digits.matches("\\d{14}")) return false;
        return calculateDigit(digits.substring(0, 12), 1) == Character.getNumericValue(digits.charAt(12))
            && calculateDigit(digits.substring(0, 13), 1) == Character.getNumericValue(digits.charAt(13));
    }

    public void validate(String cnpj) {
        Objects.requireNonNull(cnpj, "CNPJ nao pode ser nulo");
        String digits = normalize(cnpj);
        if (!isValid(cnpj)) {
            throw new IllegalArgumentException("CNPJ invalido: " + cnpj);
        }
    }

    String normalize(String cnpj) {
        return cnpj.replaceAll("[^\\d]", "");
    }

    int calculateDigit(String base, int weightStart) {
        int sum = 0;
        int weight = weightStart;
        for (int i = base.length() - 1; i >= 0; i--) {
            sum += Character.getNumericValue(base.charAt(i)) * weight;
            weight = (weight == 9) ? 2 : weight + 1;
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
```

#### CpfValidator.java (Modulo 11 - ARITMETICO)

```java
package br.uem.npd.govcore.validator;

import java.util.Objects;

/**
 * Validador aritmetico de CPF (modulo 11).
 * <p>
 * Implementa o algoritmo oficial da Receita Federal para validacao
 * dos dois digitos verificadores do CPF.
 * <p>
 * Suporta formatos:
 * <ul>
 *   <li>11 digitos (ex: 12345678901)</li>
 *   <li>Formatado (ex: 123.456.789-01) - normalizado automaticamente</li>
 * </ul>
 */
public final class CpfValidator implements InscricaoValidator {

    private static final CpfValidator INSTANCE = new CpfValidator();

    private CpfValidator() {}

    public static CpfValidator getInstance() {
        return INSTANCE;
    }

    public boolean isValid(String cpf) {
        if (cpf == null) return false;
        String digits = normalize(cpf);
        if (digits.length() != 11) return false;
        if (!digits.matches("\\d{11}")) return false;
        // Rejeita CPFs com digitos repetidos (ex: 111.111.111-11)
        if (digits.matches("(\\d)\\1{10}")) return false;
        return calculateDigit(digits.substring(0, 9), 10) == Character.getNumericValue(digits.charAt(9))
            && calculateDigit(digits.substring(0, 10), 11) == Character.getNumericValue(digits.charAt(10));
    }

    public void validate(String cpf) {
        Objects.requireNonNull(cpf, "CPF nao pode ser nulo");
        if (!isValid(cpf)) {
            throw new IllegalArgumentException("CPF invalido: " + cpf);
        }
    }

    String normalize(String cpf) {
        return cpf.replaceAll("[^\\d]", "");
    }

    int calculateDigit(String base, int weight) {
        int sum = 0;
        for (int i = 0; i < base.length(); i++) {
            sum += Character.getNumericValue(base.charAt(i)) * (weight - i);
        }
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }
}
```

#### GovValidators.java (Facade Estatico)

```java
package br.uem.npd.govcore.validator;

/**
 * Facade estatico para todos os validadores governamentais.
 * <p>
 * Uso tipico:
 * <pre>
 * GovValidators.validateCnpj("12345678000199");
 * GovValidators.validateCpf("12345678901");
 * boolean valido = GovValidators.isCnpjValid("12345678000199");
 * </pre>
 * <p>
 * Thread-safe: SIM (todos os validadores internos sao stateless).
 */
public final class GovValidators {

    private GovValidators() {}

    public static boolean isCnpjValid(String cnpj) {
        return CnpjValidator.getInstance().isValid(cnpj);
    }

    public static void validateCnpj(String cnpj) {
        CnpjValidator.getInstance().validate(cnpj);
    }

    public static boolean isCpfValid(String cpf) {
        return CpfValidator.getInstance().isValid(cpf);
    }

    public static void validateCpf(String cpf) {
        CpfValidator.getInstance().validate(cpf);
    }

    public static boolean isInscricaoValid(TipoInscricao tipo, String numero) {
        if (numero == null) return false;
        switch (tipo) {
            case CNPJ:
            case CGC:
                return isCnpjValid(numero);
            case CPF:
            case CAEPF:
                return isCpfValid(numero);
            case CNO:
                return numero.matches("\\d{12}");
            case CEI:
                return numero.matches("\\d{12}");
            default:
                return false;
        }
    }

    public static void validateInscricao(TipoInscricao tipo, String numero) {
        if (!isInscricaoValid(tipo, numero)) {
            throw new IllegalArgumentException("Inscricao invalida: tipo=" + tipo + ", numero=" + numero);
        }
    }
}
```

### 5. Pacote: `model/` - Value Objects

#### Cnpj.java (Value Object)

```java
package br.uem.npd.govcore.model;

import br.uem.npd.govcore.validator.CnpjValidator;

import java.io.Serializable;
import java.util.Objects;

/**
 * Value object para CNPJ com validacao automatica.
 * <p>
 * Imutavel, thread-safe, serializavel.
 */
public final class Cnpj implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String digits;  // Sempre 14 digitos

    private Cnpj(String digits) {
        this.digits = digits;
    }

    public static Cnpj of(String cnpj) {
        CnpjValidator.getInstance().validate(cnpj);
        return new Cnpj(normalize(cnpj));
    }

    public static Cnpj of(String cnpj, boolean skipValidation) {
        if (!skipValidation) {
            return of(cnpj);
        }
        return new Cnpj(normalize(cnpj));
    }

    private static String normalize(String cnpj) {
        if (cnpj == null) throw new IllegalArgumentException("CNPJ nao pode ser nulo");
        return cnpj.replaceAll("[^\\d]", "");
    }

    public String getDigits() {
        return digits;
    }

    public String getFormatted() {
        return String.format("%s.%s.%s/%s-%s",
            digits.substring(0, 2),
            digits.substring(2, 5),
            digits.substring(5, 8),
            digits.substring(8, 12),
            digits.substring(12));
    }

    public boolean isValid() {
        return CnpjValidator.getInstance().isValid(digits);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cnpj cnpj = (Cnpj) o;
        return digits.equals(cnpj.digits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digits);
    }

    @Override
    public String toString() {
        return digits;
    }
}
```

#### Cpf.java (Value Object)

Similar ao Cnpj, mas para CPF com 11 digitos.

#### PeriodoApuracao.java

```java
package br.uem.npd.govcore.model;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.Objects;

/**
 * Value object para periodo de apuracao fiscal.
 * <p>
 * Usado por: eSocial (perApur), EFD-Reinf, MIT, DCTFWeb, PGDAS-D, DEFIS.
 * Imutavel, thread-safe, serializavel.
 */
public final class PeriodoApuracao implements Serializable {

    private static final long serialVersionUID = 1L;

    private final YearMonth periodo;

    private PeriodoApuracao(YearMonth periodo) {
        this.periodo = periodo;
    }

    public static PeriodoApuracao of(int ano, int mes) {
        return new PeriodoApuracao(YearMonth.of(ano, mes));
    }

    public static PeriodoApuracao of(String periodo) {
        // Suporta "YYYY-MM", "YYYYMM", "MM/YYYY"
        String normalized = periodo.replaceAll("[^\\d]", "");
        if (normalized.length() == 6) {
            int ano = Integer.parseInt(normalized.substring(0, 4));
            int mes = Integer.parseInt(normalized.substring(4, 6));
            return of(ano, mes);
        }
        throw new IllegalArgumentException("Formato de periodo invalido: " + periodo);
    }

    public YearMonth getPeriodo() {
        return periodo;
    }

    public int getAno() {
        return periodo.getYear();
    }

    public int getMes() {
        return periodo.getMonthValue();
    }

    /**
     * Formato oficial para transmissao: YYYY-MM
     */
    public String toXmlFormat() {
        return periodo.toString();  // "YYYY-MM"
    }

    /**
     * Formato MM/YYYY
     */
    public String toDisplayFormat() {
        return String.format("%02d/%d", mes, ano);
    }

    public PeriodoApuracao mesSeguinte() {
        return new PeriodoApuracao(periodo.plusMonths(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodoApuracao that = (PeriodoApuracao) o;
        return periodo.equals(that.periodo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodo);
    }

    @Override
    public String toString() {
        return toXmlFormat();
    }
}
```

### 6. Pacote: `table/` - Enums Compartilhados

#### TipoInscricao.java (Extraido)

```java
package br.uem.npd.govcore.table;

import java.util.Optional;

/**
 * Tabela oficial de tipos de inscricao das declaracoes governamentais.
 * <p>
 * Codigos padrao Receita Federal:
 * <ul>
 *   <li>1 = CNPJ</li>
 *   <li>2 = CPF</li>
 *   <li>3 = CAEPF</li>
 *   <li>4 = CNO</li>
 *   <li>5 = CGC</li>
 *   <li>6 = CEI</li>
 * </ul>
 * <p>
 * Usado por: eSocial, EFD-Reinf, MIT, DCTFWeb, DEFIS, PGDAS-D, PGMEI.
 */
public enum TipoInscricao {

    CNPJ(1, "CNPJ - Cadastro Nacional da Pessoa Juridica"),
    CPF(2, "CPF - Cadastro de Pessoa Fisica"),
    CAEPF(3, "CAEPF - Cadastro de Atividade Economica de Pessoa Fisica"),
    CNO(4, "CNO - Cadastro Nacional de Obra"),
    CGC(5, "CGC - Cadastro Geral de Contribuinte"),
    CEI(6, "CEI - Cadastro Especifico do INSS");

    private final int code;
    private final String description;

    TipoInscricao(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Optional<TipoInscricao> fromCode(int code) {
        for (TipoInscricao t : values()) {
            if (t.code == code) return Optional.of(t);
        }
        return Optional.empty();
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }
}
```

#### TipoAmbiente.java (Extraido)

```java
package br.uem.npd.govcore.table;

import java.util.Optional;

/**
 * Tipos de ambiente para transmissao de declaracoes governamentais.
 * <p>
 * Padrao Receita Federal:
 * <ul>
 *   <li>1 = Producao</li>
 *   <li>2 = Producao Restrita</li>
 * </ul>
 * <p>
 * Usado por: eSocial, EFD-Reinf, MIT, DCTFWeb, DEFIS, PGDAS-D, PGMEI.
 */
public enum TipoAmbiente {

    PRODUCAO(1, "Producao"),
    PRODUCAO_RESTRITA(2, "Producao Restrita");

    private final int code;
    private final String description;

    TipoAmbiente(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Optional<TipoAmbiente> fromCode(int code) {
        for (TipoAmbiente t : values()) {
            if (t.code == code) return Optional.of(t);
        }
        return Optional.empty();
    }

    public int getCode() { return code; }
    public String getDescription() { return description; }
}
```

### 7. Pacote: `exception/` - Hierarquia de Excecoes

```java
// GovDeclarationException.java - Excecao base
public class GovDeclarationException extends RuntimeException {
    public GovDeclarationException(String message) { super(message); }
    public GovDeclarationException(String message, Throwable cause) { super(message, cause); }
}

// CertificateException.java
public class CertificateException extends GovDeclarationException {
    public CertificateException(String message) { super(message); }
    public CertificateException(String message, Throwable cause) { super(message, cause); }
}

// SignatureException.java
public class SignatureException extends GovDeclarationException {
    public SignatureException(String message) { super(message); }
    public SignatureException(String message, Throwable cause) { super(message, cause); }
}

// ValidationException.java
public class ValidationException extends GovDeclarationException {
    private final String campo;
    public ValidationException(String campo, String message) {
        super(message);
        this.campo = campo;
    }
    public String getCampo() { return campo; }
}
```

### 8. Pacote: `xml/` - Utilitarios DOM

XmlDocuments.java e XmlDates.java sao **copiados e renomeados** (sem alteracoes funcionais) do eSocial transmissor.

---

## Build Configuration (pom.xml)

```xml
<groupId>br.uem.npd</groupId>
<artifactId>declaracoes-gov-core</artifactId>
<version>1.0.0</version>
<packaging>jar</packaging>

<name>Declaracoes Gov Core</name>
<description>Componentes compartilhados para declaracoes governamentais brasileiras</description>

<properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <httpclient5.version>5.3.1</httpclient5.version>
    <junit.version>4.13.2</junit.version>
    <jacoco.version>0.8.12</jacoco.version>
    <bc.version>1.78.1</bc.version>
</properties>

<dependencies>
    <!-- Apache HttpClient 5 (apenas para SslContextFactory - SSLContexts) -->
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
        <version>${httpclient5.version}</version>
    </dependency>

    <!-- JUnit para testes -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>

    <!-- Bouncy Castle para testes (geracao de certificados de teste) -->
    <dependency>
        <groupId>org.bouncycastle</groupId>
        <artifactId>bcpkix-jdk18on</artifactId>
        <version>${bc.version}</version>
        <scope>test</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>3.2.5</version>
        </plugin>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>${jacoco.version}</version>
            <executions>
                <execution>
                    <id>prepare-agent</id>
                    <goals><goal>prepare-agent</goal></goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals><goal>report</goal></goals>
                </execution>
                <execution>
                    <id>coverage-check</id>
                    <phase>verify</phase>
                    <goals><goal>check</goal></goals>
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
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

---

## Decisoes de Design

### DD-01: Renomear para Evitar Colisao de Pacote
- **Problema**: Copiar `Pkcs12CertificateProvider` manteria o mesmo nome de pacote em cada transmissor.
- **Decisao**: Renomear para `Pkcs12Provider` no pacote `br.uem.npd.govcore.crypto`.
- **Consequencia**: Nome unico, sem colisao com copias locais se houver.

### DD-02: Validadores Aritmeticos ao Inves de Regex
- **Problema**: Validadores atuais do eSocial usam apenas regex (ex: `Pattern.compile("\\d{14}")`).
- **Decisao**: Implementar algoritmo modulo 11 oficial para CNPJ e CPF.
- **Consequencia**: Validacao real, nao apenas format. Rejeita CNPJs/CPFs invalidos com digitos errados.

### DD-03: Value Objects Imutaveis para CNPJ/CPF
- **Problema**: Strings solitas sem validacao.
- **Decisao**: Criar `Cnpj` e `Cpf` como value objects com validacao automatica no factory.
- **Consequencia**: Type-safety, imutabilidade, thread-safety.

### DD-04: Sem Bean Validation
- **Problema**: Annotations `@NotNull`, `@Pattern` adicionam dependencia e acoplamento.
- **Decisao**: Usar validacao programatica pura (IllegalArgumentException).
- **Consequencia**: Lib 100% agnostica, sem dependencia de framework.

### DD-05: HttpClient5 Apenas para SSLContexts
- **Problema**: SslContextFactory usa `SSLContexts` do HttpClient5.
- **Decisao**: Manter dependencia compile-scope pois e necessaria para mTLS.
- **Consequencia**: Consumidores que nao usam mTLS podem excluir via `<exclusions>`.

### DD-06: Facade Estatico para Validadores
- **Problema**: Chamar `CnpjValidator.getInstance().validate()` e verboso.
- **Decisao**: Criar `GovValidators` com metodos estaticos para uso rapido.
- **Consequencia**: API dupla — simples (`GovValidators.validateCnpj()`) ou avancada (`CnpjValidator.getInstance()`).

---

## Fases de Implementacao

### Fase 1: Estrutura e Crypto (dias 1-2)
1. **Criar estrutura de diretorios**
2. **Configurar pom.xml**
3. **Extrair CertificateProvider** (renomear pacotes)
4. **Extrair Pkcs12Provider, Pkcs11Provider**
5. **Extrair SslContextBuilder**
6. **Testar com certificados de teste (Bouncy Castle)**

### Fase 2: Signature e XML (dias 3-4)
7. **Extrair XmlSigner, XmlDsigSigner**
8. **Extrair XmlDocuments, XmlDates**
9. **Testar assinatura XML com exemplos reais**

### Fase 3: Validadores e Models (dias 5-6)
10. **Implementar CnpjValidator (modulo 11)**
11. **Implementar CpfValidator (modulo 11)**
12. **Implementar GovValidators (facade)**
13. **Implementar Cnpj, Cpf, PeriodoApuracao (value objects)**
14. **Testar com CNPJs/CPFs reais (validos e invalidos)**

### Fase 4: Enums e Exceptions (dias 7-8)
15. **Extrair TipoInscricao, TipoAmbiente**
16. **Criar hierarquia de excecoes**
17. **Extrair ProxyConfig**
18. **Testar todas as classes**

### Fase 5: Documentacao e Finalizacao (dias 9-10)
19. **Escrever README.md, ARCHITECTURE.md, ONBOARDING.md**
20. **Escrever docs/01-REQUISITOS.md, 02-DESIGN.md, 03-PLANO-TESTES.md**
21. **Configurar JaCoCo (meta: 90%+)**
22. **`mvn clean test verify`**
23. **Gerar CHANGELOG.md, CONTRIBUTING.md**

---

## Thread-Safety Guarantee

| Componente | Thread-safe? | Garantia |
|------------|--------------|----------|
| `CertificateProvider` implementations | Sim | Sem estado mutavel (copias defensivas) |
| `SslContextBuilder` | Sim | Factory sem estado |
| `XmlDsigSigner` | Sim | Sem estado mutavel por chamada |
| `CnpjValidator`, `CpfValidator` | Sim | Singleton stateless |
| `GovValidators` | Sim | Metodos estaticos puros |
| `Cnpj`, `Cpf`, `PeriodoApuracao` | Sim | Imutaveis |
| `TipoInscricao`, `TipoAmbiente` | Sim | Enums |
| `XmlDocuments`, `XmlDates` | Sim | Metodos estaticos puros |
| Exceptions | Sim | Imutaveis |

---

## Referencias

- **Certificado Digital ICP-Brasil**: https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/certificado-digital
- **CNPJ Alfanumerico (2024+)**: https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/perguntas-e-respostas/cnpj/cnpj-alfanumerico
- **Algoritmo Modulo 11**: https://www.bosontreinamentos.com.br/logica-de-programacao/algoritmo-para-validacao-de-cpf-digitos-verificadores
- **XMLDSIG W3C**: https://www.w3.org/TR/xmldsig-core1/
- **Projeto de Referencia**: `declaracoes-esocial-transmissor/` (security/, signature/, util/, exception/)
- **Projeto de Referencia 2**: `declaracoes-esocial-leiautes/` (table/, validators/)

---

*Documento criado em: 2026-04-09*  
*Autor: Qwen Code*  
*Projeto: declaracoes-gov-core*
