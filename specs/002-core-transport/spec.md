# Feature Specification: Core Transport Module

**Feature Branch**: `feature/002-core-transport`
**Created**: 2026-04-20
**Status**: Recalibrated
**Input**: User description: "Extração prioritária para novo módulo declaracoes-gov-core-transport baseado em SPI e Apache HttpClient 5"
**Alignment**: ADR-008 (Neutral Transport Module in Core Reactor), AR-001 exception.

---

## Alignment with Architecture

This spec implements **ADR-008**: a `declaracoes-gov-core-transport` module is permitted in the core reactor **only** because it is:
- **Declaration-agnostic**: no government endpoints, no SOAP/REST contracts, no OAuth2.
- **Composition-first**: SPI interfaces + default Apache HttpClient 5 implementation.
- **Boundary-respecting**: heavy dependency (`httpclient5`) confined to `core-transport`; no leak to `domain`, `format`, `xml`, or `crypto`.

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Neutral HTTP Transport SPI (Priority: P1)

As a developer creating a new declaration transmitter, I need a neutral HTTP transport SPI so that I can send payloads and receive responses without writing boilerplate HTTP client code or depending on specific frameworks like Spring.

**Why this priority**: Establishes the foundational contract for all future network operations.
**Independent Test**: Can be tested by creating a mock implementation of the SPI and verifying that a request payload is properly handed off to it.

**Acceptance Scenarios**:
1. **Given** a configured `RestTransport` interface, **When** I dispatch an `HttpRequest`, **Then** I receive a standardized `HttpResponse` regardless of the underlying HTTP client.
2. **Given** a mock `RestTransport`, **When** injected into a consumer, **Then** the consumer compiles and executes without referencing Apache HttpClient classes.

---

### User Story 2 — Apache HttpClient 5 Default Implementation (Priority: P2)

As a developer, I need a robust default implementation of the transport SPI using Apache HttpClient 5, supporting mTLS, proxies, and timeouts, so that I can securely communicate with government web services out-of-the-box.

**Why this priority**: Provides the immediate value and fulfills the core requirement for secure communication.
**Independent Test**: Can be tested via an embedded HTTP server (Wiremock or Jetty) to verify that the implementation correctly applies timeouts, proxy settings, and mTLS certificates.

**Acceptance Scenarios**:
1. **Given** a `ProxyConfig` and mTLS certificates, **When** I initialize the default Apache-based transport, **Then** it successfully routes traffic through the proxy and presents the client certificate.
2. **Given** a configured timeout, **When** the server delays the response, **Then** the client throws a standard `TransportException`.
3. **Given** a custom `User-Agent` header, **When** a request is executed, **Then** the header is present in the outgoing HTTP request.

---

### User Story 3 — Composable Retry Policy (Priority: P3)

As a developer dealing with unstable government endpoints, I need a composable retry mechanism (e.g., Exponential Backoff) so that transient network failures are handled gracefully without manual intervention.

**Why this priority**: Essential for reliability in production environments.
**Independent Test**: Can be tested using a mock transport that fails N times before succeeding, verifying the retry policy executes the correct number of attempts with expected delays.

**Acceptance Scenarios**:
1. **Given** a retry policy configured for 3 attempts with exponential backoff, **When** the server returns 503 Service Unavailable, **Then** the transport retries the request up to 3 times and returns the first non-retryable response or the final retryable response after exhaustion.
2. **Given** no retry policy, **When** the server returns 503, **Then** the transport returns that first response immediately.

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST define SPI interfaces: `RestTransport`, `HttpRequest`, `HttpResponse`, `ProxyConfig`, `TransportException`, `RetryPolicy`.
- **FR-002**: System MUST provide a default implementation of `RestTransport` utilizing Apache HttpClient 5 (`ApacheHttpClientRestTransport`).
- **FR-003**: The default implementation MUST support mTLS (via `CertificateProvider` from `core-crypto`), HTTP proxies, configurable connection/read timeouts, and custom User-Agent headers.
- **FR-004**: System MUST provide an `ExponentialBackoff` implementation of `RetryPolicy`.
- **FR-005**: System MUST NOT include any protocol-specific logic (e.g., SOAP envelopes, OAuth2 token management) or domain-specific endpoints.

### Key Entities

| Entity | Role |
|--------|------|
| `HttpRequest` | Neutral request representation (method, URI, headers, body, contentType). |
| `HttpResponse` | Neutral response representation (statusCode, reasonPhrase, headers, body). |
| `ProxyConfig` | Proxy host, port, optional credentials. Immutable builder. |
| `RestTransport` | Core SPI for executing requests. Extends `Closeable`. |
| `RetryPolicy` | Strategy interface for retry decisions. |
| `TransportException` | Checked exception for transport failures. |

### Non-Functional Requirements

- **NFR-001**: Implementation MUST be strictly Java 8 compliant (carefully managing Apache HttpClient 5 dependencies to avoid Java 9+ transitives).
- **NFR-002**: Unit/Integration tests MUST achieve 90% JaCoCo line and branch coverage **for the SPI and policy classes**. The default `ApacheHttpClientRestTransport` class is a pure I/O boundary adapter and MAY be excluded from the gate, provided it is covered by integration tests with an embedded HTTP server.
- **NFR-003**: All workflow steps MUST sync state to `MEMORY.md` and agent-local memory.
- **NFR-004**: Code MUST remain declaration-agnostic and framework-agnostic.
- **NFR-005**: The module MUST depend only on `core-crypto` (for `CertificateProvider`) and `core-domain` (for exceptions if needed). No dependency on `core-format` or `core-xml`.

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: The `core-transport` module compiles and passes tests on a strict Java 8 JDK without `module-info` or Java 9+ bytecode errors.
- **SC-002**: JaCoCo reports at least 90% line and branch coverage for the module **or** 90% for all non-I/O-boundary classes with documented integration-test coverage for `ApacheHttpClientRestTransport`.
- **SC-003**: The SPI can be implemented by a simple mock class in under 50 lines of code, proving its decoupling from Apache HttpClient.
- **SC-004**: Integration tests demonstrate successful proxy routing using the default implementation (mTLS via embedded server if feasible; otherwise mocked at TLS socket layer).
- **SC-005**: The module is added to the parent reactor and to `declaracoes-gov-core-bom` without breaking the green build.

---

## Test Strategy

### Unit Tests (SPI + Policies)
- Mock `RestTransport` verifying request/response contracts.
- `ExponentialBackoff` delay calculations.
- `ProxyConfig` builder validation.
- `TransportException` hierarchy.

### Integration Tests (Default Implementation)
- Embedded Jetty or Wiremock server responding to HTTP requests.
- Timeout verification via delayed response.
- Proxy verification via mock proxy server.
- mTLS: use `core-crypto` test-jar fixtures to configure a test keystore/truststore.

### Coverage Exclusion
```xml
<!-- In core-transport pom.xml -->
<exclude>br/uem/npd/govcore/transport/ApacheHttpClientRestTransport*</exclude>
```
Justification: Pure I/O boundary adapter. Coverage guaranteed by integration tests.

---

## Assumptions

- Apache HttpClient 5.2+ (compatible with Java 8) will be used.
- Payload serialization (JSON/XML) is handled outside of this transport module; the transport deals strictly with `String` or `byte[]` body.
- Consumers (`reinf-transmissor`, `esocial-transmissor`, `serpro-transmissor`) will migrate to `core-transport` in a separate phase, not in this spec.

---

## Out of Scope

- Government endpoint catalogues.
- OAuth2, SAML, or any authentication protocol.
- SOAP envelope handling.
- Request/response DTOs for specific declarations.
- Migration of existing transmitters (esocial/reinf/serpro) to use core-transport.
