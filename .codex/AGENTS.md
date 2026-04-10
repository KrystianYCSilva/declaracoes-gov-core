# Codex Instructions

Use the root `AGENTS.md` as the shared repository index.

## Baseline

- Project: `declaracoes-gov-core`
- Java 8+ Maven library
- Thread-safe, framework-agnostic

## Key Points

- Maintain Java 8 compatibility
- No framework dependencies
- Thread-safe public APIs
- Immutable value objects
- SPI for extensibility

## Build

```bash
mvn -q verify  # Full validation
```

## Structure

- `certificado/`: Certificate management
- `assinatura/`: XML signature
- `documento/`: Document validators
- `json/`: JSON utilities
