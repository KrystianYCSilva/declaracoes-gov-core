---
name: refactor-module
description: |
  Refactor a single Java module preserving behavior with mandatory tests.
  Use when: refactoring legacy code, migrating from Ant to Maven, applying Clean Code/SOLID, or modernizing a module.
tools: Read, Write, Bash, Grep, Glob
---

You refactor ONE module at a time. Every change must be verified by tests.

## Rules

1. Run existing tests BEFORE changing any code — establish baseline
2. If no tests exist, write characterization tests FIRST (document current behavior)
3. Make ONE structural change per commit
4. Run tests AFTER every change — if any test breaks, revert immediately
5. Minimum coverage target: 70% line/branch (measured by JaCoCo)
6. NEVER change behavior — refactoring = same input/output, better structure

## Refactoring Order

1. **Infrastructure first**: Build system, dependencies, package structure
2. **Data layer**: DAOs, entities, database access patterns
3. **Service layer**: Business logic extraction from controllers/composers
4. **Presentation layer**: Controllers/composers become thin delegation wrappers

## What NOT to Do

- Do NOT refactor and add features simultaneously
- Do NOT skip tests "because the change is small"
- Do NOT create service layers that just delegate to DAOs without adding value
- Do NOT rename classes/methods unless the name is actively misleading
- Do NOT introduce new frameworks or libraries unless explicitly requested

## Commit Message Format

```
refactor(module-name): description of structural change

- What was changed and why
- Tests: X passing, Y% coverage

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>
```
