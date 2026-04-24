# Plano de Implementação: Arcabouço Financeiro e Monetário

**Branch**: `feature/004-arcabouco-financeiro-monetario` | **Spec**: `flow-specs/004-arcabouco-financeiro-monetario/spec.md`
**Target**: `develop`

---

## Project Conventions

| Categoria | Valor |
|-----------|-------|
| Linguagem/Runtime | Java 8 (`maven.compiler.source=1.8`) + Kotlin 1.8.22 |
| Build | Maven 3.x multi-module reactor |
| Test framework | JUnit 4.13.2 + `kotlin-test-junit` |
| Coverage | JaCoCo 0.8.11 — `domain`/`format`/`kotlin`: ≥90% linha + ≥90% branch |
| Arquitetura | 100% agnóstico (sem Spring, Jakarta EE, Lombok) |
| CI | `mvn -q verify` no reactor root |

---

## Sumário

Implementação do arcabouço financeiro-monetário em 3 módulos existentes (`domain`, `format`, `kotlin`): constantes fiscais (`GovNumberConstants`), formatação BRL/SPED (`GovCurrencyFormats`), enums de domínio (`RegimeTributario`, `SituacaoCadastral`), value classes financeiras (`ValorMonetario`, `Aliquota`, `Percentual`, `BaseCalculo`), extensões de coleção fiscal e validação composta.

**9 arquivos novos / 4 expansões** → 9 arquivos de produção + 9 arquivos de teste.

---

## Contexto Técnico

- **Linguagem/Versão**: Java 8 (JDK-only em `domain`; `NumberFormat(Locale)` em `format`) + Kotlin 1.8.22
- **Dependências primárias**: JDK 8 (`java.math`, `java.text`, `java.util`) — zero dependências externas adicionais
- **Testing**: JUnit 4 (`@Test`, `Assert.*`); Kotlin: `kotlin-test-junit`
- **Constraints**: AR-003 (`domain` JDK-only), AR-004 (Java 8 baseline), AR-002 (sem frameworks)
- **Retrocompatibilidade total** com `GovBigDecimalConstants`, `GovNumberUtils`, `GovNumberFormats`, `NumberExtensions.kt`, `CollectionExtensions.kt`, `TextExtensions.kt`, `ValidationExtensions.kt` (partes existentes)
- **Dependência cruzada**: `fillGaps` usa `YearMonth.rangeTo` — se WP04 da spec 002 não estiver merged, implementar `CompetenciaRange` localmente ou desvincular `fillGaps` da spec 002

---

## Constitution Check

| Princípio | Status | Observação |
|-----------|--------|-----------|
| I. Framework Agnostic | ✅ | JDK-only em domain; java.text em format |
| II. Multi-Agent Shared Memory | ✅ | MEMORY.md atualizado |
| III. Test-Driven Integrity (90%) | ✅ | JaCoCo gates mantidos |
| V. Secure XML & Crypto | ✅ | Nenhum impacto |

---

## Estrutura do Projeto

```
declaracoes-gov-core-domain/
└── src/
    ├── main/java/.../govcore/
    │   ├── util/GovNumberConstants.java     ← NOVO
    │   └── table/
    │       ├── RegimeTributario.java         ← NOVO
    │       └── SituacaoCadastral.java        ← NOVO
    └── test/java/.../govcore/
        ├── util/GovNumberConstantsTest.java  ← NOVO
        └── table/
            ├── RegimeTributarioTest.java     ← NOVO
            └── SituacaoCadastralTest.java    ← NOVO

declaracoes-gov-core-format/
└── src/
    ├── main/java/.../govcore/util/
    │   └── GovCurrencyFormats.java           ← NOVO
    └── test/java/.../govcore/util/
        └── GovCurrencyFormatsTest.java       ← NOVO

declaracoes-gov-core-kotlin/
└── src/
    ├── main/kotlin/.../govcore/ext/
    │   ├── FinancialTypes.kt                 ← NOVO
    │   ├── NumberExtensions.kt               ← EXPANDIR
    │   ├── TextExtensions.kt                 ← EXPANDIR (constantes)
    │   └── CollectionExtensions.kt           ← EXPANDIR
    │   └── ValidationExtensions.kt           ← EXPANDIR
    └── test/kotlin/.../govcore/ext/
        ├── FinancialTypesTest.kt             ← NOVO
        ├── NumberExtensionsTest.kt           ← EXPANDIR
        ├── TextExtensionsTest.kt             ← EXPANDIR
        └── CollectionExtensionsTest.kt       ← EXPANDIR
        └── ValidationExtensionsTest.kt       ← EXPANDIR
```

---

## Decisões Arquiteturais

| Decisão | Escolha | Justificativa |
|---------|---------|--------------|
| `GovNumberConstants` vs modificar `GovBigDecimalConstants` | Classe nova complementar | Retrocompatibilidade; separação de constantes numéricas gerais vs constantes fiscais |
| `formatBrl` usa `java.text.NumberFormat` | Instância criada internamente por chamada | Thread-safe via lock implícito; JDK 8 disponível |
| `ValorMonetario` scale enforcement | No `companion.of()` via `setScale(2, HALF_EVEN)` | Construtor direto aceita scale ≤ 2 para flexibilidade (ex: `BigDecimal("1.50")` já tem scale 2) |
| Enums no pacote `table` | Junto com `Uf`, `TipoInscricao` existentes | Coesão — todos são tabelas de domínio fiscal |
| `validacaoComposta` DSL | Classe builder sem `object` | Permite estado acumulado; compatível com Java 8 |
| `fillGaps` com `LinkedHashMap` | Mantém ordem por período | Relatórios esperam ordem temporal |

---

## Análise de Paralelismo dos WPs

```
WP01 (GovNumberConstants + Enums)
  ├── WP02 (GovCurrencyFormats) — usa SCALE_MONETARIO de WP01
  └── (independente de WP02)
        └── WP03 (FinancialTypes.kt) — usa GovCurrencyFormats de WP02
              └── WP04 (Extensões: Number, Text, Collection, Validation) — usa FinancialTypes de WP03
```

WP01 e WP02 podem rodar em sequência rápida. WP03 e WP04 são sequenciais após WP02.

---

## Estratégia de Testes

1. **Arredondamento ABNT**: testar explicitamente `BigDecimal("2.5").setScale(0, HALF_EVEN)` == 2 (não 3)
2. **Null-safety**: todos os métodos Java e Kotlin com entrada null
3. **Formatação locale**: garantir que `formatBrl` não é afetado pela `Locale` padrão da JVM
4. **Value class operators**: testar `+`, `-`, `*`, `compareTo` com casos de borda (zero, negativo, muito grande)
5. **Validação composta**: pelo menos 3 validações com 1-2 falhas — verificar que TODAS são acumuladas

---

## Rastreabilidade de Requisitos

| RF | Entidade | WP |
|----|----------|----|
| RF-001 | `GovNumberConstants` | WP01 |
| RF-002 | `GovCurrencyFormats` | WP02 |
| RF-003 | `RegimeTributario`, `SituacaoCadastral` | WP01 |
| RF-004 | `FinancialTypes.kt` | WP03 |
| RF-005 | `NumberExtensions.kt` (expansão) | WP04 |
| RF-006 | `TextExtensions.kt` (constantes) | WP04 |
| RF-007 | `CollectionExtensions.kt` (expansão) | WP04 |
| RF-008 | `ValidationExtensions.kt` (expansão) | WP04 |
