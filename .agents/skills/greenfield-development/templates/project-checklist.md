---
description: |
  Pre-flight checklist before starting any greenfield project.
  Use when: bootstrapping a new project to ensure nothing is missed.
---

# Greenfield Project Checklist

## Phase 0: Foundation (before writing ANY business code)

- [ ] Build tool configured (pom.xml / package.json / go.mod)
- [ ] Test framework added as dependency
- [ ] Coverage plugin configured with gate (80% line for greenfield)
- [ ] Test database configured (H2 / SQLite / testcontainers)
- [ ] `src/test/` directory exists with at least one passing test
- [ ] `mvn test` / `npm test` / `pytest` runs green
- [ ] AGENTS.md created (< 150 lines, stack + conventions + example pointer)
- [ ] .gitignore configured (target/, node_modules/, __pycache__/, .env)
- [ ] CI pipeline created (GitHub Actions / GitLab CI) with test + coverage gate

## Phase 1: Vertical Slice (one complete feature, end-to-end)

- [ ] 1 entity/model created with proper annotations/types
- [ ] 1 DAO/repository with basic CRUD
- [ ] 1 service with business logic (even if trivial)
- [ ] 1 controller/endpoint (if web project)
- [ ] Tests for ALL of the above (entity, DAO, service, controller)
- [ ] `mvn verify` / `npm test -- --coverage` passes with gate

## Phase 2: Expand (replicate the slice)

- [ ] Additional entities follow the SAME pattern as the first
- [ ] Each new entity has tests created BEFORE or WITH the implementation
- [ ] Coverage gate still passes after each addition
- [ ] No abstract base classes unless 3+ concrete classes share >70% code

## Phase 3: Production Readiness

- [ ] Logging configured (SLF4J / winston / logging module)
- [ ] Error handling standardized (global exception handler)
- [ ] Environment config externalized (env vars or config files, NOT hardcoded)
- [ ] README.md with build + run + test instructions
- [ ] Docker/container support (if deployment requires it)

## Anti-Pattern Checklist (things that should NOT exist)

- [ ] NO abstract base classes without 3+ concrete implementations
- [ ] NO DTOs that mirror entities 1:1 with fewer than 3 field differences
- [ ] NO competing frameworks (e.g., JUnit 4 + JUnit 5 in same project)
- [ ] NO `System.out.println` (use logger)
- [ ] NO `e.printStackTrace()` (use logger.error)
- [ ] NO TODO comments without a linked issue/ticket
