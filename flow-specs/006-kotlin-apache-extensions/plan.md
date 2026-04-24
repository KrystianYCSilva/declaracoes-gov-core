# Plano de Implementação: Kotlin Apache Commons Extensions Module

**Branch**: `feature/006-kotlin-apache-extensions` | **Spec**: `flow-specs/006-kotlin-apache-extensions/spec.md`
**Target**: `develop`

---

## Project Conventions

| Categoria | Valor |
|-----------|-------|
| Linguagem | Kotlin 1.8.22 (apenas Kotlin — zero arquivos Java) |
| Build | Maven 3.x — novo módulo `declaracoes-gov-core-kotlin-apache` |
| Test framework | JUnit 4.13.2 + `kotlin-test-junit` |
| Coverage | JaCoCo ≥90% linha + ≥90% branch |
| Dependências externas | commons-lang3:3.14.0, commons-text:1.12.0, commons-math3:3.6.1, commons-codec:1.17.0 |
| Arquitetura | Módulo opcional; nunca depende de `kotlin-guava` |

---

## Sumário

Criação do novo módulo Maven `declaracoes-gov-core-kotlin-apache` com wrappers idiomáticos Kotlin sobre Apache Commons. **7 arquivos de produção, 7 de teste**. O módulo é opcional.

---

## Contexto Técnico

- **Dependência do módulo**: `declaracoes-gov-core-kotlin` (compile) + 4 libs Apache Commons (compile)
- **Sem sobreposição com ZipExtensions.kt**: `encodeBase64Apache` tem nome distinto de `compressToBase64`
- **Sem colisão com Kotlin stdlib**: `stripAccents`, `abbreviate`, etc. não existem na stdlib; onde há risco, usar prefixo (ex: `reverseText`, `capitalizeFirst`)
- **ReDoS**: Nenhuma regex criada neste módulo — usar apenas APIs Apache que já protegem contra isso
- **POM pai**: Adicionar `<module>declaracoes-gov-core-kotlin-apache</module>` e properties de versão Apache Commons
- **BOM**: Adicionar `declaracoes-gov-core-kotlin-apache` ao BOM

---

## Constitution Check

| Princípio | Status | Observação |
|-----------|--------|-----------|
| I. Framework Agnostic | ✅ | Apache Commons são utility libraries, não frameworks |
| III. Test-Driven Integrity | ✅ | JaCoCo 90% |
| NFR-003 ReDoS | ✅ | Zero regex nova criada; usar apenas APIs Apache |

---

## Estrutura do Novo Módulo

```
declaracoes-gov-core-kotlin-apache/
├── pom.xml
└── src/
    ├── main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/
    │   ├── text/
    │   │   ├── ApacheTextExtensions.kt        ← NOVO
    │   │   ├── ApacheEscapeExtensions.kt      ← NOVO
    │   │   └── ApacheSimilarityExtensions.kt  ← NOVO
    │   ├── number/
    │   │   └── ApacheConversionExtensions.kt  ← NOVO
    │   ├── math/
    │   │   └── FiscalStatisticsExtensions.kt  ← NOVO
    │   └── codec/
    │       └── ApacheCodecExtensions.kt       ← NOVO
    └── test/kotlin/...
        └── (espelho dos arquivos main)
```

---

## Decisões Arquiteturais

| Decisão | Escolha | Justificativa |
|---------|---------|--------------|
| `escapeXml10` vs `escapeXml11` | Ambos expostos | eSocial/Reinf usam XML 1.0; SPED-Fiscal usa 1.0; manter ambos por completude |
| `toBooleanSimNao` neste módulo vs core | Neste módulo (`ApacheConversionExtensions`) | Usa `BooleanUtils` do Apache; se 006 não for adicionado, WP04 da spec 003 fornece versão pura Kotlin |
| `EstatisticaFiscal` | `data class` no mesmo arquivo | Não merece módulo próprio; agrupado com as extensões de estatística |
| `sha256Apache` vs `sha256Guava` | Prefixo por biblioteca | Evita conflito se consumidor usar ambos os módulos |
| `simpleMovingAverage` retorna `List` | Mesma cardinalidade que input | Posições iniciais sem janela completa usam média parcial |

---

## Análise de Paralelismo dos WPs

```
WP01 (pom.xml do módulo + setup)
  └── WP02 (ApacheTextExtensions + ApacheEscapeExtensions)  ─► paralelo com WP03/WP04
  └── WP03 (ApacheSimilarityExtensions)                     ─► paralelo com WP02/WP04
  └── WP04 (ApacheConversionExtensions)                     ─► paralelo com WP02/WP03
  └── WP05 (FiscalStatisticsExtensions + ApacheCodecExtensions) ─► após WP01, independente
```

WP02, WP03, WP04 e WP05 são todos independentes entre si após WP01.

---

## Rastreabilidade de Requisitos

| FR | Arquivo | WP |
|----|---------|-----|
| FR-001 | `ApacheTextExtensions.kt` | WP02 |
| FR-002 | `ApacheEscapeExtensions.kt` | WP02 |
| FR-003 | `ApacheSimilarityExtensions.kt` | WP03 |
| FR-004 | `ApacheConversionExtensions.kt` | WP04 |
| FR-005 | `FiscalStatisticsExtensions.kt` | WP05 |
| FR-006 | `ApacheCodecExtensions.kt` | WP05 |
