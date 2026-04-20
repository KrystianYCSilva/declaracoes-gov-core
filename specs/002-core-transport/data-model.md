# Phase 1 Data Model: Core Transport Module

**Date**: 2026-04-20 | **Spec**: [spec.md](spec.md)

## Entity Definitions

### HttpRequest
Neutral, immutable representation of an HTTP request.

| Field | Type | Constraints |
|-------|------|-------------|
| `method` | `String` | GET, POST, PUT, DELETE, etc. |
| `uri` | `String` | Non-null, absolute URI |
| `headers` | `Map<String, List<String>>` | Case-insensitive key map |
| `body` | `Optional<byte[]>` | Empty for GET/DELETE |
| `contentType` | `Optional<String>` | e.g., `application/xml` |

**Mutability**: Immutable. Builder pattern provided.

### HttpResponse
Neutral, immutable representation of an HTTP response.

| Field | Type | Constraints |
|-------|------|-------------|
| `statusCode` | `int` | HTTP status (200, 404, 503, etc.) |
| `reasonPhrase` | `Optional<String>` | e.g., "OK", "Service Unavailable" |
| `headers` | `Map<String, List<String>>` | Case-insensitive key map |
| `body` | `Optional<byte[]>` | Raw response body |

**Mutability**: Immutable. No builder — constructed only by `RestTransport` implementations.

### ProxyConfig
Immutable proxy configuration with optional authentication.

| Field | Type | Constraints |
|-------|------|-------------|
| `host` | `String` | Non-null, non-empty |
| `port` | `int` | 1–65535 |
| `username` | `Optional<String>` | Present only if password is present |
| `password` | `Optional<String>` | Present only if username is present |

**Mutability**: Immutable. Builder pattern with validation (both or neither of user/pass).

### TransportException
Checked exception hierarchy for transport failures.

| Property | Type | Description |
|----------|------|-------------|
| `message` | `String` | Human-readable failure reason |
| `cause` | `Optional<Throwable>` | Underlying exception (IOException, etc.) |

**Hierarchy**:
- `TransportException` (checked)
  - `TransportTimeoutException` — connection or read timeout
  - `TransportSecurityException` — mTLS / SSL handshake failure

### RetryPolicy
Strategy interface for retry decisions.

| Method | Return | Parameters | Description |
|--------|--------|------------|-------------|
| `shouldRetry` | `boolean` | `HttpRequest`, `HttpResponse`, `attemptCount` | Whether to retry |
| `delayMillis` | `long` | `attemptCount` | Delay before next attempt |
| `maxAttempts` | `int` | — | Maximum number of attempts |

### ExponentialBackoff
Default `RetryPolicy` implementation.

| Parameter | Type | Default |
|-----------|------|---------|
| `baseDelayMillis` | `long` | 1000 |
| `maxDelayMillis` | `long` | 30000 |
| `maxAttempts` | `int` | 3 |
| `retryableStatusCodes` | `Set<Integer>` | { 503, 504, 502 } |

**Formula**: `delay = min(baseDelay * 2^(attempt-1), maxDelay)`

## State Diagram

```
[Consumer]
   |
   v
[HttpRequest.Builder] --build()--> [HttpRequest (immutable)]
   |
   v
[RestTransport.execute()] --success--> [HttpResponse (immutable)]
   |                                    |
   |--failure--> [RetryPolicy.shouldRetry?]
                  |--yes--> [delay] --> [retry execute]
                  |--no--> [TransportException]
```

## Package Dependencies

```
br.uem.npd.govcore.transport
├── RestTransport (SPI)
├── HttpRequest
├── HttpResponse
├── ProxyConfig
├── TransportException
├── RetryPolicy (SPI)
├── ExponentialBackoff
└── apache
    └── ApacheHttpClientRestTransport (depends on HttpClient 5)
        └── wires CertificateProvider from core-crypto
```

**Dependency Rules**:
- `transport` package must NOT reference `apache` sub-package.
- `apache` package may reference `transport` and `core-crypto`.
- No dependency on `core-format` or `core-xml`.
