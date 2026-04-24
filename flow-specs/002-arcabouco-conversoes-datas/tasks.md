# Tasks: Arcabouço de Conversões de Datas

**Feature**: `002-arcabouco-conversoes-datas`  
**Target branch**: `master-ctbz`  
**Spec**: `spec.md` | **Plan**: `plan.md`

## Work Packages

| WP | Title | Subtasks | Deps | Status |
|----|-------|----------|------|--------|
| WP01 | GovTimeConstants + JavaTimeConversions (domain) | T001-T004 | — | planned |
| WP02 | LegacyDateConverter (domain) | T005-T007 | WP01 | planned |
| WP03 | GovDateParser + GovDateFormats (domain + format) | T008-T012 | WP01 | planned |
| WP04 | Expansão DateExtensions.kt (kotlin) | T013-T016 | WP02, WP03 | planned |
| WP05 | TemporalValueTypes.kt (kotlin) | T017-T019 | WP04 | planned |
| WP06 | DateStringTypes.kt (kotlin) | T020-T023 | WP04 | planned |

## Dependency Graph

```
WP01 → WP02 → WP04 → WP05
             ↗            ↘
WP01 → WP03 → WP04 → WP06
```

WP05 and WP06 can run in parallel after WP04.

## Implementation Order

1. `flow build WP01` (no deps)
2. `flow build WP02 --base WP01` and `flow build WP03 --base WP01` (parallel)
3. `flow build WP04 --base WP02,WP03`
4. `flow build WP05 --base WP04` and `flow build WP06 --base WP04` (parallel)

## Validation Gate

`mvn -q verify` at reactor root must pass all tests and JaCoCo 90%/90% gates.
