---
name: key-decisions
description: |
  Architectural decisions and their rationale for declaracoes-gov-core.
  Use when: evaluating changes, onboarding, or reviewing design choices.
---

# Key Architectural Decisions

## ADR-001: Java 8 Compatibility

**Decision**: Maintain Java 8 as minimum version (source/target).

**Context**: 
- Many enterprise clients still on Java 8
- Need maximum compatibility across consumer libraries

**Consequences**:
- (+) Widest possible adoption
- (+) No runtime compatibility issues
- (-) Cannot use modern Java features (var, new Optional methods)
- (-) Some dependencies require older versions

**Status**: Active, non-negotiable for 1.x.

---

## ADR-002: Framework Agnosticism

**Decision**: Zero framework dependencies in core (no Spring, Jakarta EE, etc.).

**Context**:
- Library is shared across multiple applications
- Different consumers use different frameworks
- Framework dependencies cause version conflicts

**Consequences**:
- (+) No classpath conflicts
- (+) Can be used in any Java application
- (+) Smaller footprint
- (-) Must implement some utilities manually
- (-) No dependency injection in core

**Status**: Active, T0 rule.

---

## ADR-003: Immutable Value Objects

**Decision**: Use immutable objects for all domain models (CNPJ, CPF, certificates).

**Context**:
- Thread safety requirement
- Prevent accidental mutation
- Functional programming benefits

**Consequences**:
- (+) Thread-safe by design
- (+) Predictable behavior
- (+) Cache-friendly
- (-) More boilerplate (mitigated by Lombok if needed)
- (-) Object creation overhead (acceptable)

**Status**: Active, applies to all public models.

---

## ADR-004: Optional Dependencies Pattern

**Decision**: Mark non-essential dependencies as `<optional>true</optional>`.

**Context**:
- Jackson is used only by some consumers
- HttpClient only needed for mTLS HTTP (not all use cases)
- Want to minimize transitive dependencies

**Consequences**:
- (+) Consumers control their dependency tree
- (+) No forced dependencies
- (-) Consumers must explicitly add optional deps if needed
- (-) Must document which deps are needed for which features

**Status**: Active. Required: xmlsec, slf4j. Optional: jackson, httpclient5, caffeine.

---

## ADR-005: CNPJ Alphanumeric Support

**Decision**: Support CNPJ alphanumeric format (July 2026) from day one.

**Context**:
- Receita Federal introducing alphanum CNPJ in 2026
- Better to support early than migrate later
- ASCII-48 algorithm documented

**Consequences**:
- (+) Future-proof
- (+) No breaking change later
- (-) Slightly more complex validation
- (-) Need to maintain dual-mode support

**Status**: Active. CNPJ class handles both formats automatically.

---

## ADR-006: Apache XML Security over JDK Default

**Decision**: Use Apache XML Security (Santuario) instead of JDK's built-in XML signature.

**Context**:
- JDK XML signature has compatibility issues
- Apache implementation more widely tested
- Better support for ICP-Brasil requirements

**Consequences**:
- (+) Better compatibility
- (+) More control over algorithms
- (+) Consistent behavior across JDK versions
- (-) Additional dependency

**Status**: Active.

---

## ADR-007: SPI Pattern for Extensibility

**Decision**: Use Service Provider Interface (SPI) for extension points.

**Context**:
- Need to allow custom certificate loaders
- Want to support different validation strategies
- Avoid tight coupling to implementations

**Consequences**:
- (+) Clean extension mechanism
- (+) No reflection needed
- (+) Standard Java pattern
- (-) Slightly more complex to implement

**Status**: Active. Interfaces: CertificadoLoader, CertificadoValidator, XmlSigner, JsonMapper.

---

## ADR-008: SLF4J over java.util.logging

**Decision**: Use SLF4J as logging facade.

**Context**:
- Standard for Java libraries
- Consumers choose implementation (Logback, Log4j2, etc.)
- Better performance than JUL

**Consequences**:
- (+) Consumer flexibility
- (+) Rich ecosystem
- (-) One more dependency (small, stable)

**Status**: Active.

---

## ADR-009: No Lombok in Public API

**Decision**: Do not use Lombok in public API classes (ok in internal/test).

**Context**:
- Lombok requires annotation processor in consumers
- Some organizations restrict Lombok usage
- Public API should have minimal dependencies

**Consequences**:
- (+) No consumer dependency on Lombok
- (+) Public API is "plain Java"
- (-) More boilerplate in public classes
- (-) Manual equals/hashCode/toString

**Status**: Active. May use Lombok in internal package or tests.

---

## ADR-010: BigDecimal for Monetary Values

**Decision**: Use BigDecimal for all monetary/tax values, never double/float.

**Context**:
- Financial calculations require precision
- IEEE 754 floating point has rounding errors
- Tax calculations must be exact

**Consequences**:
- (+) Exact decimal arithmetic
- (+) No rounding surprises
- (-) More verbose code
- (-) Performance impact (acceptable)

**Status**: Active, T0 rule.

---

## Rejected Alternatives

### Spring Boot Starter (REJECTED)
- Rejected: Would force Spring dependency on all consumers
- Alternative: Keep agnostic, consumers add Spring if needed

### Java 11 as Minimum (REJECTED)
- Rejected: Too many clients still on Java 8
- Alternative: Java 8 with best practices

### Custom JSON Implementation (REJECTED)
- Rejected: Jackson is standard, well-tested
- Alternative: Jackson with optional scope

### Synchronous-only Certificate Loading (REJECTED)
- Rejected: A3 certificates may block on PIN entry
- Alternative: Provide async variants where needed

---

## Pending Decisions

### Cache Implementation
- Options: Caffeine (optional dep), custom simple cache, or no cache
- Factors: Performance needs, memory constraints
- Status: Under evaluation

### OSGi Support
- Options: Add OSGi metadata, provide fragment, or no support
- Factors: Consumer requirements
- Status: Not currently needed

### Module System (JPMS)
- Options: Add module-info.java or stay classpath-only
- Factors: Consumer adoption of modules
- Status: Deferred to 2.x
