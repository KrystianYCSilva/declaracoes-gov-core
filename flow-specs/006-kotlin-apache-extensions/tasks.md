# Tasks: Kotlin Apache Commons Extensions Module

**Feature**: `006-kotlin-apache-extensions`
**Target branch**: `develop`
**Spec**: `spec.md` | **Plan**: `plan.md`

## Work Packages

| WP | Title | Subtasks | Deps | Status |
|----|-------|----------|------|--------|
| WP01 | Setup do módulo Maven (pom.xml, build config, BOM) | T001-T002 | — | planned |
| WP02 | Text + Escape (ApacheTextExtensions, ApacheEscapeExtensions) | T003-T005 | WP01 | planned |
| WP03 | Similarity (ApacheSimilarityExtensions) | T006-T007 | WP01 | planned |
| WP04 | Conversion — Booleanos SPED e NumberUtils (ApacheConversionExtensions) | T008-T010 | WP01 | planned |
| WP05 | Statistics + Codec (FiscalStatisticsExtensions, ApacheCodecExtensions) | T011-T014 | WP01 | planned |

## Dependency Graph

```
WP01 → WP02
WP01 → WP03
WP01 → WP04
WP01 → WP05
```

WP02, WP03, WP04 e WP05 são todos independentes entre si — podem rodar em paralelo após WP01.

## Implementation Order

1. `flow build WP01`
2. `flow build WP02`, `flow build WP03`, `flow build WP04`, `flow build WP05` (todos em paralelo após WP01)

## Validation Gate

`mvn -q verify` at reactor root (incluindo novo módulo) must pass 90%/90% JaCoCo.

## Nota sobre POM Raiz

WP01 deve incluir:
- Criação de `declaracoes-gov-core-kotlin-apache/pom.xml`
- Adição de `<module>declaracoes-gov-core-kotlin-apache</module>` no `pom.xml` raiz
- Properties: `commons-lang3.version=3.14.0`, `commons-text.version=1.12.0`, `commons-math3.version=3.6.1`, `commons-codec.version=1.17.0`
- Adição do artefato no BOM

## Nota sobre Colisões de Nomenclatura

- `stripAccents` — não conflita com stdlib Kotlin
- `reverseText` — usar em vez de `reverse()` (conflito com `CharSequence.reversed()` da stdlib)
- `capitalizeFirst` — usar em vez de `capitalize()` (deprecated na stdlib Kotlin 1.5+)
- `toBooleanSimNao` — também disponível no WP04 da spec 003 sem Apache; se ambos forem merged, garantir que WP04/003 não duplique a implementação ou que as versões coexistam com assinaturas distintas
