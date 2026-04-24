# Plano de Implementação: Kotlin Guava Extensions Module

**Branch**: `feature/005-kotlin-guava-extensions` | **Spec**: `flow-specs/005-kotlin-guava-extensions/spec.md`
**Target**: `develop`

---

## Project Conventions

| Categoria | Valor |
|-----------|-------|
| Linguagem | Kotlin 1.8.22 (apenas Kotlin — zero arquivos Java) |
| Build | Maven 3.x — novo módulo `declaracoes-gov-core-kotlin-guava` |
| Test framework | JUnit 4.13.2 + `kotlin-test-junit` |
| Coverage | JaCoCo ≥90% linha + ≥90% branch |
| Dependência externa | `com.google.guava:guava:33.0.0-jre` |
| Arquitetura | Módulo opcional; nunca depende de `kotlin-apache` |

---

## Sumário

Criação do novo módulo Maven `declaracoes-gov-core-kotlin-guava` com wrappers idiomáticos Kotlin sobre Google Guava. **6 arquivos de produção, 6 de teste**. O módulo é opcional — consumidores adicionam somente se já dependem de Guava.

---

## Contexto Técnico

- **Dependência do módulo**: `declaracoes-gov-core-kotlin` (compile) + `guava:33.0.0-jre` (compile)
- **Java 8 compat**: Guava 33.x `-jre` suporta Java 8; usar artefato `-jre` (não `-android`)
- **APIs proibidas**: Nunca usar APIs marcadas `@Beta` do Guava — podem mudar sem aviso
- **Kotlin**: `@JvmOverloads` onde necessário para interop Java; `@JvmStatic` em companions
- **POM pai**: Adicionar `<module>declaracoes-gov-core-kotlin-guava</module>` no `pom.xml` raiz; adicionar versão do Guava em `<dependencyManagement>`
- **BOM**: Adicionar `declaracoes-gov-core-kotlin-guava` ao BOM

---

## Constitution Check

| Princípio | Status | Observação |
|-----------|--------|-----------|
| I. Framework Agnostic | ✅ | Guava não é framework — é utility library |
| III. Test-Driven Integrity | ✅ | JaCoCo 90% |
| Módulo opcional | ✅ | Core kotlin não depende deste módulo |

---

## Estrutura do Novo Módulo

```
declaracoes-gov-core-kotlin-guava/
├── pom.xml
└── src/
    ├── main/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/
    │   ├── collections/
    │   │   ├── GuavaCollectionExtensions.kt   ← NOVO
    │   │   └── GuavaMultimapExtensions.kt     ← NOVO
    │   ├── ranges/
    │   │   └── PeriodoRangeExtensions.kt      ← NOVO
    │   ├── table/
    │   │   └── FiscalTableExtensions.kt       ← NOVO
    │   ├── cache/
    │   │   └── CacheExtensions.kt            ← NOVO
    │   └── hash/
    │       └── HashExtensions.kt             ← NOVO
    └── test/kotlin/br/com/contabilizei/obrigacoes/govcore/guava/
        ├── collections/
        │   ├── GuavaCollectionExtensionsTest.kt
        │   └── GuavaMultimapExtensionsTest.kt
        ├── ranges/
        │   └── PeriodoRangeExtensionsTest.kt
        ├── table/
        │   └── FiscalTableExtensionsTest.kt
        ├── cache/
        │   └── CacheExtensionsTest.kt
        └── hash/
            └── HashExtensionsTest.kt
```

---

## Decisões Arquiteturais

| Decisão | Escolha | Justificativa |
|---------|---------|--------------|
| `YearMonth.rangeTo` | Cria `Range<YearMonth>` Guava | `YearMonth` já implementa `Comparable` — Guava Range funciona diretamente |
| `ImmutableRangeSet` vs `RangeSet` | `ImmutableRangeSet` nas APIs públicas | Garante imutabilidade nas assinaturas; consumidor sabe que não pode mutar |
| `fiscalTable` DSL | Extension de `ImmutableTable.Builder` | Sintaxe mais fluente; consistent com o padrão Kotlin DSL |
| `buildLoadingCache` loader | Lambda `(K) -> V` | Kotlin-friendly vs `CacheLoader.from(Function)` do Java |
| Hash: Murmur3 | `Hashing.murmur3_128()` | Rápido, não-criptográfico — adequado para deduplicação de registros |

---

## Análise de Paralelismo dos WPs

```
WP01 (pom.xml do módulo + setup) ─────────────────────────────────►
  └── WP02 (GuavaCollectionExtensions + GuavaMultimapExtensions)  ─►  paralelo com WP03
  └── WP03 (PeriodoRangeExtensions)                               ─►  paralelo com WP02
  └── WP04 (FiscalTableExtensions + CacheExtensions + HashExtensions) ─► após WP02/WP03
```

WP02 e WP03 são independentes entre si e podem rodar em paralelo após WP01.

---

## Rastreabilidade de Requisitos

| FR | Arquivo | WP |
|----|---------|-----|
| FR-001 | `GuavaCollectionExtensions.kt` | WP02 |
| FR-002 | `PeriodoRangeExtensions.kt` | WP03 |
| FR-003 | `FiscalTableExtensions.kt` | WP04 |
| FR-004 | `GuavaMultimapExtensions.kt` | WP02 |
| FR-005 | `CacheExtensions.kt` | WP04 |
| FR-006 | `HashExtensions.kt` | WP04 |
