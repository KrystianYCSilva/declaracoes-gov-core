# Claude Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific Claude guidance.

## Baseline

- Project: `declaracoes-gov-core`
- Java 8+ Maven library
- Thread-safe, framework-agnostic
- Domain: Brazilian tax declarations (certificates, XML signature, validators)

## Guardrails

- Keep secrets out of Git.
- Keep public APIs stable.
- Maintain Java 8 compatibility.
- Keep `mvn verify` green.
- All public classes must be thread-safe.
- No Spring/Jakarta EE dependencies.

## Architecture Focus

This is a foundational library for Brazilian tax declaration systems. Code quality and API stability are critical.

### Key Design Principles

1. **Immutability**: All domain objects (CNPJ, CPF, certificates) are immutable
2. **Thread Safety**: All public APIs are thread-safe without external synchronization
3. **Zero Framework Dependencies**: Pure Java + small, focused libraries only
4. **SPI Pattern**: Extension through interfaces, not inheritance

### Package Responsibilities

- `certificado`: Certificate loading (A1/A3), validation, mTLS
- `assinatura`: XML digital signature per eSocial/EFD-Reinf specs
- `documento`: Document validators (CNPJ alphanumeric-aware, CPF, IE)
- `json`: JSON utilities (Jackson wrapper, optional dependency)

## Claude-Specific Guidance

When generating code:
- Prefer explicit types over `var` (Java 8 compatibility)
- Use `final` for fields and parameters where possible
- Include comprehensive Javadoc with `@apiNote` for thread-safety
- Use Builder pattern for objects with many optional fields
- Use factory methods (`of()`, `from()`) for value objects

Example:
```java
/**
 * Creates a CNPJ instance from unformatted string.
 * 
 * @param value 14-digit or alphanumeric CNPJ string
 * @return new CNPJ instance
 * @throws DocumentoException if format is invalid
 * @apiNote This method is thread-safe. The returned CNPJ is immutable.
 */
public static CNPJ of(String value) {
    // implementation
}
```

## Testing Expectations

- Every public method must have unit tests
- Thread-safety tests for concurrent classes
- Mutation testing for validators (property-based if possible)
- Use AssertJ for fluent assertions

## Security Considerations

- Never use real certificates in tests
- Never log sensitive data (passwords, private keys)
- Clear sensitive arrays immediately after use
- Validate all inputs before processing
