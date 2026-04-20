---
description: |
  Starter AGENTS.md template for greenfield projects.
  Use when: creating a new project and need to configure AI agent behavior.
  Instructions: Copy this into your project root as AGENTS.md and customize.
---

# AGENTS.md — [PROJECT NAME]

## Stack

- Language: Java 17 / Node 20 / Python 3.11 (pick one, delete others)
- Build: Maven 3.9 / npm / pip+venv
- Framework: Spring Boot 3.2 / Express / FastAPI
- Test: JUnit 5 + Mockito / Jest / pytest
- Coverage: JaCoCo 0.8.12 / Jest --coverage / pytest-cov
- Test DB: H2 2.2.224 / SQLite / testcontainers
- CI: GitHub Actions

## Conventions

- Package/module structure: `model/`, `dao/`, `service/`, `controller/`
- Naming: classes PascalCase, methods camelCase, constants UPPER_SNAKE
- Error handling: throw custom exceptions, never return null for collections
- Logging: SLF4J (Java) / winston (Node) / logging (Python) — no System.out

## Rules

1. Every new class must have a corresponding test in the same commit
2. Read existing code before creating new classes — follow the established pattern
3. No abstract base classes until 3+ concrete implementations share >70% code
4. No competing frameworks — use ONLY the stack listed above
5. `mvn verify` / `npm test` / `pytest` must pass before any commit
6. Coverage gate: 80% line coverage — do not lower this threshold

## Reference Implementation

Read `src/main/java/com/.../model/[FirstEntity].java` as the pattern to follow.
All subsequent entities, DAOs, and services must follow this structure.

## What NOT to Do

- Do not create DTOs that mirror entities with fewer than 3 field differences
- Do not add dependencies not listed in the stack above without explicit approval
- Do not write tests that test framework behavior (only test YOUR code)
- Do not create utility classes with only static methods — use the existing ones first
