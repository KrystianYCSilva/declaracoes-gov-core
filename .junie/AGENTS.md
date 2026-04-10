# Junie Instructions

Use the root `AGENTS.md` as the shared repository index.

## Baseline

- Project: `declaracoes-gov-core`
- Java 8+ Maven library
- Thread-safe, framework-agnostic

## Key Points

- Java 8 compatible
- No framework dependencies
- Thread-safe public APIs
- Immutable value objects

## Build

```bash
mvn -q verify
```

## Project Structure

- `certificado/`: Certificate management
- `assinatura/`: XML signature
- `documento/`: Document validators
- `json/`: JSON utilities
