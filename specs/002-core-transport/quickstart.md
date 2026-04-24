# Quickstart: Core Transport Module

**Module**: `declaracoes-gov-core-transport` | **Spec**: [spec.md](spec.md)

## Maven Dependency

```xml
<dependency>
    <groupId>br.uem.npd.govcore</groupId>
    <artifactId>declaracoes-gov-core-transport</artifactId>
    <version>${declaracoes-gov-core.version}</version>
</dependency>
```

Ensure the BOM is imported for version alignment:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>br.uem.npd.govcore</groupId>
            <artifactId>declaracoes-gov-core-bom</artifactId>
            <version>${declaracoes-gov-core.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Basic Usage

```java
import br.uem.npd.govcore.transport.*;
import br.uem.npd.govcore.transport.apache.ApacheHttpClientRestTransport;

public class Example {
    public static void main(String[] args) throws Exception {
        RestTransport transport = ApacheHttpClientRestTransport.builder()
            .connectTimeoutMillis(5_000)
            .readTimeoutMillis(15_000)
            .userAgent("my-transmitter/2.0")
            .retryPolicy(new ExponentialBackoff())
            .build();

        HttpRequest request = HttpRequest.builder()
            .method("POST")
            .uri("https://example.gov.br/api")
            .header("Content-Type", "application/xml")
            .body("<payload/>".getBytes())
            .build();

        HttpResponse response = transport.execute(request);
        System.out.println("Status: " + response.statusCode());

        transport.close();
    }
}
```

When a `RetryPolicy` is configured, the transport retries retryable failures/responses and returns the first non-retryable response or the last retryable response after the configured attempts are exhausted.

## Mocking the SPI (Unit Tests)

```java
public class MockRestTransport implements RestTransport {
    private final Queue<HttpResponse> responses = new LinkedList<>();

    public void enqueue(HttpResponse response) {
        responses.add(response);
    }

    @Override
    public HttpResponse execute(HttpRequest request) {
        return responses.poll();
    }

    @Override
    public void close() {}
}
```

This mock is under 30 lines and proves the SPI is decoupled from Apache HttpClient.

## mTLS + Proxy Configuration

```java
CertificateProvider certs = ...; // from core-crypto
ProxyConfig proxy = ProxyConfig.builder()
    .host("proxy.corp.gov.br")
    .port(8080)
    .username("svc")
    .password("secret")
    .build();

RestTransport transport = ApacheHttpClientRestTransport.builder()
    .certificateProvider(certs)
    .proxyConfig(proxy)
    .build();
```

## Retry Policy Customization

```java
RetryPolicy custom = new ExponentialBackoff(
    500,   // base delay ms
    10_000, // max delay ms
    5,     // max attempts
    EnumSet.of(502, 503, 504)
);
```
