# Phase 0 Research: Core Transport Module

**Date**: 2026-04-20 | **Spec**: [spec.md](spec.md)

## Apache HttpClient 5 Java 8 Compatibility

- **Version**: Apache HttpClient 5.2.6 (latest 5.2.x at time of writing).
- **Baseline**: HC 5.x core compiles to Java 8; HC 5.3+ bumped requirement to Java 11.
- **Maven coordinates**: `org.apache.httpcomponents.client5:httpclient5:5.2.6`
- **Transitive caution**: `httpclient5` brings `httpcore5` and `slf4j-api`. No Java 9+ modules are introduced.

## Existing Patterns in the Ecosystem

### Reinf Transmissor (`reinf/declaracoes-efd-reinf-transmissor`)
- Uses Apache HttpClient 4.x directly inside transmitter classes.
- JaCoCo exclusion already applied to the I/O adapter class.
- Certificate loading is manual and duplicated.

### eSocial & Serpro Transmitters
- Similar ad-hoc HttpClient usage; no shared abstraction.
- Migration to `core-transport` is **out of scope** for this spec, but the SPI is designed to accommodate them.

## mTLS Integration with core-crypto

- `core-crypto` provides:
  - `CertificateProvider` — abstraction over keystore/truststore sources.
  - `SslContextBuilder` — utility to build `SSLContext` from provider inputs.
- `ApacheHttpClientRestTransport` will accept a `CertificateProvider` in its builder/factory and wire it into `SSLConnectionSocketFactory` via HttpClient 5's `TlsSocketStrategy`.

## Retry & Backoff Strategies

- Apache HttpClient 5 has a `HttpRequestRetryStrategy`, but it is client-internal and not mockable for unit tests.
- Decision: implement a **transport-level** `RetryPolicy` SPI that wraps `RestTransport.execute()`, keeping the SPI independent of HttpClient internals.
- `ExponentialBackoff` formula: `delay = baseDelay * (2 ^ attempt) + jitter`, capped at `maxDelay`.

## Embedded Server Options for Integration Tests

| Option | Pros | Cons |
|--------|------|------|
| **Wiremock** | Dedicated to HTTP mocking; proxy/mTLS support | Extra dependency (test scope) |
| **Jetty 9.x** | Java 8 compatible; already used in some government stacks | More boilerplate |
| **Mockito + `HttpClient` mock** | No server startup; fast | Cannot test real socket behavior (proxy, TLS) |

**Decision**: Use **Wiremock** (test scope) for integration tests. It supports proxy verification, delay simulation, and header inspection out of the box.

## Coverage Exclusion Precedent

- Existing `reinf-transmissor/pom.xml`:
  ```xml
  <exclude>br/uem/.../ApacheHttpClient*</exclude>
  ```
- Justification documented: "Pure I/O boundary adapter with integration-test coverage."
- This precedent is adopted verbatim for `core-transport`.
