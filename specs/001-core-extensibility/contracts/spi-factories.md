# SPI Contracts: Factories

## SslContextFactory
**Module**: `declaracoes-gov-core-crypto`
**Package**: `br.uem.npd.govcore.crypto`

```java
public interface SslContextFactory {
    SSLContext create(CertificateProvider certProvider, KeyStore trustStore);
}
```

## GovJsonFactoryInterface
**Module**: `declaracoes-gov-core-format`
**Package**: `br.uem.npd.govcore.util`

```java
public interface GovJsonFactoryInterface {
    ObjectMapper createMapper();
}
```

## Integration Policy
- If no custom factory is provided to the Builders, they MUST default to the existing `createMapper()` and `createSslContext()` logic.
