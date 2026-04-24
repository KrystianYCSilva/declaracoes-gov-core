# Tasks: Arcabouço Financeiro e Monetário

**Feature**: `004-arcabouco-financeiro-monetario`
**Target branch**: `develop`
**Spec**: `spec.md` | **Plan**: `plan.md`

## Work Packages

| WP | Title | Subtasks | Deps | Status |
|----|-------|----------|------|--------|
| WP01 | GovNumberConstants + Enums de Domínio (domain) | T001-T003 | — | planned |
| WP02 | GovCurrencyFormats (format) | T004-T005 | WP01 | planned |
| WP03 | FinancialTypes.kt — ValorMonetario, Aliquota, Percentual, BaseCalculo (kotlin) | T006-T008 | WP02 | planned |
| WP04 | Expansões: NumberExtensions, TextExtensions, CollectionExtensions, ValidationExtensions (kotlin) | T009-T013 | WP03 | planned |

## Dependency Graph

```
WP01 → WP02 → WP03 → WP04
```

## Implementation Order

1. `flow build WP01` (no deps)
2. `flow build WP02 --base WP01`
3. `flow build WP03 --base WP02`
4. `flow build WP04 --base WP03`

## Validation Gate

`mvn -q verify` at reactor root must pass all tests and JaCoCo 90%/90% gates.
