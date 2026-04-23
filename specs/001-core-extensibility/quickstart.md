# Quickstart: Compose Around the Existing Core

## Custom Validation Without Changing the Core

```java
public final class MeuNisService {

    public Nis parse(String raw) {
        Nis nis = Nis.ofProvisionallyValidated(raw);

        if (!nis.getUnformatted().startsWith("1")) {
            throw new InvalidDocumentException("Regra local: NIS deve iniciar com 1");
        }

        return nis;
    }
}
```

The core keeps the official/default validation behavior. Consumer-specific rules are applied in an external service instead of replacing `GovValidators` or `Nis` internals.

## Custom JSON Mapper Without Mutating `GovJsonFactory`

```java
ObjectMapper mapper = GovJsonFactory.getMapper().copy();
mapper.findAndRegisterModules();
mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
```

Use the core mapper as a safe baseline and compose the consumer-specific mapper outside the shared singleton.

## Custom TLS Without Introducing a Core SPI

```java
SSLContext defaultContext = SslContextBuilder.build(certificateProvider, trustStore);

SSLContext customContext = SSLContext.getInstance("TLSv1.2");
customContext.init(keyManagers, trustManagers, null);
```

The core helper covers the shared default path. If an integration needs specialized TLS behavior, it should own that composition in the consuming module.
