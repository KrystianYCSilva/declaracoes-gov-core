# Tasks: Kotlin Guava Extensions Module

**Feature**: `005-kotlin-guava-extensions`
**Target branch**: `develop`
**Spec**: `spec.md` | **Plan**: `plan.md`

## Work Packages

| WP | Title | Subtasks | Deps | Status |
|----|-------|----------|------|--------|
| WP01 | Setup do módulo Maven (pom.xml, build config, BOM) | T001-T002 | — | planned |
| WP02 | ImmutableCollections + Multimap (GuavaCollectionExtensions, GuavaMultimapExtensions) | T003-T005 | WP01 | planned |
| WP03 | PeriodoRangeExtensions — RangeSet para períodos fiscais | T006-T008 | WP01 | planned |
| WP04 | FiscalTable + Cache + Hash (FiscalTableExtensions, CacheExtensions, HashExtensions) | T009-T012 | WP02, WP03 | planned |

## Dependency Graph

```
WP01 → WP02 → WP04
WP01 → WP03 → WP04
```

WP02 e WP03 podem rodar em paralelo após WP01.

## Implementation Order

1. `flow build WP01`
2. `flow build WP02 --base WP01` e `flow build WP03 --base WP01` (paralelo)
3. `flow build WP04 --base WP02,WP03`

## Validation Gate

`mvn -q verify` at reactor root (incluindo novo módulo) must pass 90%/90% JaCoCo.

## Nota sobre POM Raiz

WP01 deve incluir:
- Criação de `declaracoes-gov-core-kotlin-guava/pom.xml`
- Adição de `<module>declaracoes-gov-core-kotlin-guava</module>` no `pom.xml` raiz
- Adição de `guava.version=33.0.0-jre` nas properties do POM raiz
- Adição do artefato no BOM (`declaracoes-gov-core-bom/pom.xml`)
