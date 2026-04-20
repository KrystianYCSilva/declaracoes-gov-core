# Implementation Plan: Core Transport Module

**Branch**: `feature/002-core-transport` | **Date**: 2026-04-20 | **Spec**: [spec.md](spec.md)
**Input**: Feature specification from `/specs/002-core-transport/spec.md`

## Summary

Create a new `declaracoes-gov-core-transport` Maven module providing a declaration-agnostic HTTP transport SPI and a default Apache HttpClient 5 implementation. The module will serve as the unified network foundation for all future declaration transmitters (eSocial, Reinf, Serpro), replacing ad-hoc HTTP client code with a composition-first, testable contract.

## Technical Context

**Language/Version**: Java 8 (Strict Baseline — source/target 1.8, no `module-info`)
**Primary Dependencies**: Maven, JUnit 4.13.2, Mockito, JaCoCo, Apache HttpClient 5.2.x (Java 8 compatible), BouncyCastle (via `core-crypto`)
**Storage**: N/A (Core library — no persistence)
**Testing**: JUnit 4 + Mockito for unit tests; embedded Jetty/Wiremock for integration tests; JaCoCo 90% line/branch for SPI and policy classes
**Target Platform**: JVM 8+
**Project Type**: Core Library Module (Declaration & Framework Agnostic)
**Performance Goals**: Low-allocation request/response wrappers; reusable `CloseableHttpClient` lifecycle
**Constraints**: No Java 9+ APIs, no Spring/Jakarta dependencies, no declaration-specific logic, no OAuth2/SOAP protocols
**Scale/Scope**: Single Maven module inside `declaracoes-gov-core` reactor; consumed by transmitter modules in separate repositories

## Constitution Check

*GATE: Passed before Phase 0 research. Re-checked after Phase 1 design.*

- [x] **Agnosticism**: The module defines only neutral HTTP abstractions; no declaration logic or framework coupling. (Principle I)
- [x] **Memory Protocol**: Root `MEMORY.md` and `.kimi/memory/agent-local-memory.md` will be updated at each workflow transition. (Principle II)
- [x] **Java 8 Baseline**: Apache HttpClient 5.2.x is Java 8 compatible; no `java.time.Duration` or `var` usage. (Constraint)
- [x] **Coverage Plan**: SPI + retry policies targeted at 90%; `ApacheHttpClientRestTransport` excluded from gate with integration-test justification. (Principle III)
- [x] **Delegation**: `speckit.tasks` and implementation may delegate to sub-agents for contract generation or test scaffolding. (Principle IV)
- [x] **Security**: mTLS via `core-crypto` `CertificateProvider`; no hardcoded credentials or certificate material. (Principle V)

## Project Structure

### Documentation (this feature)

```text
specs/002-core-transport/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command)
```

### Source Code (repository root)

```text
declaracoes-gov-core-transport/
├── pom.xml                              # Module POM (child of reactor)
└── src/
    ├── main/java/br/uem/npd/govcore/transport/
    │   ├── RestTransport.java           # SPI: core transport contract
    │   ├── HttpRequest.java             # Neutral request value object
    │   ├── HttpResponse.java            # Neutral response value object
    │   ├── ProxyConfig.java             # Immutable proxy configuration
    │   ├── TransportException.java      # Checked transport failure
    │   ├── RetryPolicy.java             # SPI: retry strategy
    │   ├── ExponentialBackoff.java      # Default RetryPolicy impl
    │   └── apache/
    │       └── ApacheHttpClientRestTransport.java   # Default RestTransport impl
    └── test/java/br/uem/npd/govcore/transport/
        ├── HttpRequestTest.java
        ├── HttpResponseTest.java
        ├── ProxyConfigTest.java
        ├── TransportExceptionTest.java
        ├── ExponentialBackoffTest.java
        ├── MockRestTransportTest.java   # SC-003: mockability < 50 LOC
        └── apache/
            ├── ApacheHttpClientRestTransportIT.java   # Embedded server tests
            └── ProxyRoutingIT.java                    # Proxy integration tests
```

**Structure Decision**: Single Maven module following the existing `declaracoes-gov-core-*` conventions. Package `br.uem.npd.govcore.transport` keeps it flat and discoverable. `apache` sub-package isolates the HttpClient 5 dependency, making the SPI boundary explicit.

## Complexity Tracking

No Constitution Check violations required. The transport module is justified under **ADR-008** (neutral SPI exception to AR-001) and documented in the spec alignment section.

## Phase 0: Research

See [research.md](research.md) for detailed findings. Key takeaways:
- Apache HttpClient 5.2.x is the latest Java 8-compatible major line; HC 5.3+ requires Java 11.
- `CloseableHttpClient` should be built once per `ApacheHttpClientRestTransport` instance and closed on `RestTransport.close()`.
- `core-crypto` already exposes `CertificateProvider` and `SslContextBuilder` abstractions sufficient for mTLS wiring.
- Existing transmitter precedent (`reinf-transmissor`) already excludes the Apache adapter from JaCoCo; this pattern is replicated.

## Phase 1: Design

### Data Model
See [data-model.md](data-model.md) for entity definitions, state diagrams, and package dependencies.

### Contracts
See [contracts/](contracts/) for:
- `RestTransport.java` — interface contract
- `RetryPolicy.java` — strategy contract
- `ApacheHttpClientRestTransport.java` — adapter contract (integration boundary)

### Quickstart
See [quickstart.md](quickstart.md) for consumer usage examples, Maven dependency snippet, and mock configuration.
