---
description: |
  Consolidated Architecture Decision Records (ADRs) for declaracoes-gov-core.
  Use when: evaluating changes that challenge established design choices.
---

# Key Decisions — declaracoes-gov-core

## ADR-001: Java 11 Baseline

**Status**: Accepted  
**Date**: 2026-04  
**Context**: The `v1.0.x` line is frozen on Java 8 for maintenance only, while `v1.1.x` becomes the active `javax` line for forward evolution.  
**Decision**: Lock the active branch baseline to Java 11 using `maven.compiler.release=11`. Prohibit Java 12+ language features and APIs in the `v1.1.x` line.  
**Consequences**: + Aligns the core with the next supported runtime of the ecosystem. + Keeps the `v1` series on `javax` while unlocking newer dependency lines. − Java 8 compatibility moves to the maintenance branch only.

## ADR-002: Framework-Agnostic Core

**Status**: Accepted  
**Date**: 2024-01  
**Context**: Core must be usable by Spring, Jakarta EE, and plain Java consumers alike.  
**Decision**: No Spring, no Jakarta EE, no Bean Validation, no Lombok, no injection frameworks.  
**Consequences**: + Maximum reusability. − More boilerplate (explicit constructors, no Lombok `@Value`).

## ADR-003: Validator Confidence Model

**Status**: Accepted  
**Date**: 2024-02  
**Context**: Market sells heuristic CPF/CNPJ checks as "official". Need transparent confidence levels.  
**Decision**: Three-tier model:
- `OFFICIAL` — fail-fast, backed by catalogued government source.
- `PROVISIONAL` — algorithm known but source not fully catalogued; opt-in only.
- `STRUCTURAL` — normalization, length, basic form.
**Consequences**: + Prevents misleading validation claims. − Slightly more complex API (explicit opt-in methods).

## ADR-004: Module Isolation with Internal BOM

**Status**: Accepted  
**Date**: 2024-01  
**Context**: Heavy dependencies (Jackson, xmlsec) must not leak to consumers that only need domain objects.  
**Decision**: Six-module reactor with `core-bom` for version alignment. `domain` stays JDK-only, and `core-transport` isolates the neutral HTTP infrastructure.  
**Consequences**: + Clean dependency graph. − More modules to publish and version.

## ADR-005: XMLDSIG Defaults (RSA-SHA256)

**Status**: Accepted  
**Date**: 2024-03  
**Context**: Brazilian government systems require specific signature profiles.  
**Decision**: Fix defaults to RSA-SHA256, SHA-256 digest, inclusive canonicalization, enveloped transform. Use `XmlSignatureOptions` for explicit target selection.  
**Consequences**: + Predictable signing behavior. − Less flexibility (by design).

## ADR-006: AI Context in English, Human Docs in Portuguese

**Status**: Accepted  
**Date**: 2025-04  
**Context**: Developers speak Portuguese; AI agents and international collaborators need English.  
**Decision**: Javadoc and inline comments in Portuguese. `.context/`, `AGENTS.md`, and AI-facing docs in English.  
**Consequences**: + Bilingual clarity. − Need to sync two doc streams.

## ADR-008: Neutral Transport Module in Core Reactor

**Status**: Accepted  
**Date**: 2026-04-20  
**Context**: AR-001 prohibits "transport concerns" in the core repository. However, 3 transmitter projects (`esocial`, `reinf`, `serpro`) all duplicate similar HTTP transport infrastructure (Apache HttpClient 5, proxy config, mTLS, retry logic). Extracting a commons module aligns with the DRY principle, but risks violating the declaration-agnostic boundary.

**Decision**: Permit a `declaracoes-gov-core-transport` module in the same reactor, with strict boundaries:
- SPI-first: `RestTransport`, `HttpRequest`, `HttpResponse`, `ProxyConfig`, `RetryPolicy`, `TransportException`.
- Default implementation: Apache HttpClient 5.3.x for the Java 11 line.
- No declaration-specific endpoints, no OAuth2, no SOAP envelopes, no government contracts.
- Heavy deps (HttpClient 5) confined to `core-transport` only; must not leak to other core modules.

**Consequences**: + Eliminates duplication across transmitters. + Consumers can inject custom transport implementations. − Slightly blurs the "no transport" line; requires discipline to keep SPI neutral.

## ADR-007: Multi-Agent Memory Pattern

**Status**: Accepted  
**Date**: 2026-04-20  
**Context**: Multiple LLM CLIs (Claude, Cursor, Gemini, Kimi, etc.) operate on this repo. Need shared state without interference.  
**Decision**: `MEMORY.md` (root) for shared cross-session state + `memory/agent-local-memory.md` per agent for private notes. Root `AGENTS.md` is the single source of truth.  
**Consequences**: + Context recovery across sessions and agents. − Requires discipline to update MEMORY.md at task boundaries.
