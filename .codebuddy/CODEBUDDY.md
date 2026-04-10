# CodeBuddy Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific CodeBuddy guidance.

## Baseline

- Project: `declaracoes-gov-core`
- Java 8+ Maven library
- Thread-safe, framework-agnostic

## Quick Reference

### Build Commands
```bash
mvn -q compile        # Compile only
mvn -q test           # Run tests
mvn -q verify         # Full validation
mvn -q -DskipTests package  # Package JAR
```

### Key Constraints
- Java 8 compatible (no `var`, no new Optional methods)
- Thread-safe public APIs
- No Spring/Jakarta dependencies
- BigDecimal for money, never double/float

### Project Structure
```
certificado/    # Certificate loading & mTLS
assinatura/     # XML digital signature
documento/      # CNPJ/CPF/IE validators
json/           # JSON utilities
```

## CodeBuddy Focus

When generating code for this project:
1. Check Java 8 compatibility
2. Ensure thread-safety
3. Add Javadoc with @apiNote
4. Use Builder pattern where appropriate
5. Write corresponding tests

## Quality Gates
- `mvn verify` must pass
- 80% line coverage minimum
- Checkstyle and PMD clean
