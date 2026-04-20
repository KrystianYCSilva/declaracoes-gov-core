---
title: Core Transport Module
created: 2026-04-20
tags: [transport, http, apache-httpclient, spi]
---

# Core Transport Module (`declaracoes-gov-core-transport`)

## Overview

The `declaracoes-gov-core-transport` module provides a **declaration-agnostic HTTP transport SPI** and a default Apache HttpClient 5 implementation. It is the unified network foundation for all declaration transmitters (eSocial, Reinf, Serpro).

## Architecture Decision

Permitted inside the core reactor under **ADR-008** (exception to AR-001). See `.context/standards/architectural-rules.md`.

## Key Contracts

| Contract | Location | Purpose |
|----------|----------|---------|
| `RestTransport` | `transport/` | SPI for executing HTTP requests |
| `RetryPolicy` | `transport/` | Strategy for retry decisions |
| `HttpRequest` / `HttpResponse` | `transport/` | Neutral request/response value objects |
| `ProxyConfig` | `transport/` | Immutable proxy configuration |
| `TransportException` | `transport/` | Checked exception hierarchy |
| `ApacheHttpClientRestTransport` | `transport/apache/` | Default I/O boundary adapter |

## Dependencies

- `core-crypto` — `CertificateProvider` and `SslContextBuilder` for mTLS
- `core-domain` — (indirect, via crypto) exceptions if needed
- **No dependency** on `core-format` or `core-xml`

## JaCoCo Strategy

- **90% line/branch** for SPI and policy classes
- `ApacheHttpClientRestTransport` excluded from gate (pure I/O boundary adapter)
- Integration tests with embedded Wiremock server provide coverage justification

## Usage

```java
RestTransport transport = ApacheHttpClientRestTransport.builder()
    .connectTimeoutMillis(5_000)
    .readTimeoutMillis(15_000)
    .retryPolicy(new ExponentialBackoff())
    .build();
```

## Future Work

Migration of existing transmitters (`reinf`, `esocial`, `serpro`) to `core-transport` is planned for a later phase.
