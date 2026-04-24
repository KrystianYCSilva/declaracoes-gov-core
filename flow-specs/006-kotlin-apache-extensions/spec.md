# Feature Specification: Kotlin Apache Commons Extensions Module

**Feature Branch**: `feature/006-kotlin-apache-extensions`
**Created**: 2026-04-24
**Status**: Planned

## Source Document

**Tipo:** `generic-discovery`
**Origem:** Gap analysis via Claude Opus — análise de padrões recorrentes no ecossistema declaracoes-*
**Data:** 2026-04-24

### Resumo da análise

O JDK oferece APIs limitadas para processamento de texto fiscal avançado. Em especial:

| Lacuna | Impacto |
|--------|---------|
| Sem escape XML nativo robusto | Strings com `&`, `<`, `>` quebram XML de web services (eSocial, Reinf, NF-e) |
| Sem fuzzy matching | Reconciliação de razões sociais entre sistemas exige 15+ linhas manuais |
| Sem convenção booleana SPED | `"S"/"N"` e `1/0` convertidos de forma diferente em cada serviço |
| Sem estatísticas financeiras | Dashboards contábeis reimplementam média, mediana, desvio padrão |
| Sem encoding centralizado | `MessageDigest` verboso; `Base64` Java 8 limitado |
| `String.stripAccents()` incompleto | NFD do JDK falha em alguns caracteres especiais do português |

---

## Módulo Maven

**Artefato:** `declaracoes-gov-core-kotlin-apache`
**Dependência:** `declaracoes-gov-core-kotlin` + Apache Commons libs
**Pacote raiz:** `br.com.contabilizei.obrigacoes.govcore.apache`
**Natureza:** Módulo **opcional**

```
declaracoes-gov-core-kotlin
        ↓
declaracoes-gov-core-kotlin-apache
        + org.apache.commons:commons-lang3:3.14.0
        + org.apache.commons:commons-text:1.12.0
        + org.apache.commons:commons-math3:3.6.1
        + commons-codec:commons-codec:1.17.0
```

Estrutura de pacotes:
```
br.com.contabilizei.obrigacoes.govcore.apache
├── text/
│   ├── ApacheTextExtensions.kt
│   ├── ApacheEscapeExtensions.kt
│   └── ApacheSimilarityExtensions.kt
├── number/
│   └── ApacheConversionExtensions.kt
├── math/
│   └── FiscalStatisticsExtensions.kt
└── codec/
    └── ApacheCodecExtensions.kt
```

---

## Cenários de Uso

| # | Ator | Cenário | Resultado esperado |
|---|------|---------|-------------------|
| U1 | Dev Kotlin | Prepara string para XML eSocial | `"Empresa & Filhos <Ltda>".escapeXml10()` → `"Empresa &amp; Filhos &lt;Ltda&gt;"` |
| U2 | Dev Kotlin | Remove acentos para TXT SPED | `"Açúcar".stripAccents()` → `"Acucar"` (robusto, sem falsos negativos) |
| U3 | Dev Kotlin | Formata nome próprio para documento | `"MARIA DA SILVA".capitalizeWords()` → `"Maria Da Silva"` |
| U4 | Dev Kotlin | Reconcilia razão social | `"CONTABILIZEI TECNOLOGIA".bestMatchIn(candidatos, 0.85)` → melhor correspondência |
| U5 | Dev Kotlin | Converte boolean para SPED | `true.toStringSimNao()` → `"S"` / `false.toStringSimNao()` → `"N"` |
| U6 | Dev Kotlin | Parse boolean de campo SPED | `"S".toBooleanSimNao()` → `true` / `"N"` → `false` |
| U7 | Dev Kotlin | Converte flag numérico SPED | `1.toBooleanSped()` → `true` / `0.toBooleanSped()` → `false` |
| U8 | Dev Kotlin | Calcula estatísticas de faturamento | `faturamentos.estatistica()` → `EstatisticaFiscal(mean, median, stdDev, ...)` |
| U9 | Dev Kotlin | Calcula hash de arquivo SPED | `conteudo.sha256Hex()` → string hex de 64 chars |
| U10 | Dev Kotlin | Encode URL para parâmetro de portal | `cnpj.urlEncode()` → `"12.345.678%2F0001-90"` |
| U11 | Dev Kotlin | Verifica se string é parseable como número | `"1.234,56".isParsable()` → true |
| U12 | Dev Kotlin | Calcula média móvel de receitas | `receitas.simpleMovingAverage(3)` → lista suavizada |
| U13 | Dev Kotlin | Abrevia descrição para campo fixo | `"Prestação de serviços contábeis tributários".abbreviate(30)` → truncado com `...` |
| U14 | Dev Kotlin | Escapa CSV para exportação | `"Empresa, SA".escapeCsv()` → `'"Empresa, SA"'` |

---

## Requisitos Funcionais

### FR-001 — `ApacheTextExtensions.kt` (pacote `text`) — Apache Commons Lang3

```kotlin
// StringUtils wrappers — complementam as extensões do módulo kotlin core
fun String?.isBlankOrNull(): Boolean                             // StringUtils.isBlank
fun String?.isNotBlankOrNull(): Boolean
fun String?.defaultIfBlank(default: String): String             // StringUtils.defaultIfBlank
fun String?.stripAccents(): String?                             // StringUtils.stripAccents — robusto
fun String?.abbreviate(maxWidth: Int, abbrevMarker: String = "..."): String?
fun String.capitalizeFirst(): String                            // StringUtils.capitalize
fun String.uncapitalizeFirst(): String
fun String.swapCase(): String
fun String.reverseText(): String                                // avoids collision with Kotlin's reversed()
fun String.center(size: Int, padChar: Char = ' '): String
fun String.substringBetween(open: String, close: String): String?
fun String.countOccurrences(sub: String): Int                   // StringUtils.countMatches

// WordUtils via Commons Text
fun String.capitalizeWords(): String     // "MARIA DA SILVA" → "Maria Da Silva"
fun String.initials(): String            // "Maria Da Silva" → "MDS"
fun String.wrap(wrapLength: Int): String
```

---

### FR-002 — `ApacheEscapeExtensions.kt` (pacote `text`) — Apache Commons Text

```kotlin
// XML — CRÍTICO para web services governamentais
fun String.escapeXml10(): String         // StringEscapeUtils.escapeXml10
fun String.escapeXml11(): String         // StringEscapeUtils.escapeXml11
fun String.unescapeXml(): String

// HTML (relatórios, portais)
fun String.escapeHtml4(): String
fun String.unescapeHtml4(): String

// CSV (exportações fiscais)
fun String.escapeCsv(): String
fun String.unescapeCsv(): String

// Java (logging de dados fiscais)
fun String.escapeJava(): String

// Sanitização específica para o domínio
/** escapeXml10 + stripAccents + toUpperCase */
fun String.sanitizeForSped(): String
/** escapeXml10 com regras específicas eSocial (substitui control chars por espaço) */
fun String.sanitizeForEsocial(): String
```

---

### FR-003 — `ApacheSimilarityExtensions.kt` (pacote `text`) — Apache Commons Text

```kotlin
// Métricas de similaridade
fun String.jaroWinklerSimilarity(other: String): Double    // JaroWinklerSimilarity
fun String.levenshteinDistance(other: String): Int         // LevenshteinDistance
fun String.longestCommonSubsequence(other: String): CharSequence
fun String.fuzzyScore(query: String): Int                  // FuzzyScore (pt-BR Locale implícito)

// Decisões baseadas em similaridade
fun String.isSimilarTo(other: String, threshold: Double = 0.85): Boolean
fun String.bestMatchIn(candidates: List<String>, threshold: Double = 0.85): String?
/** Retorna o melhor match com seu score, ou null se abaixo do threshold */
fun String.bestMatchWithScoreIn(candidates: List<String>, threshold: Double = 0.0): Pair<String, Double>?
/** Ordena candidatos por similaridade decrescente */
fun String.rankCandidates(candidates: List<String>): List<Pair<String, Double>>
```

---

### FR-004 — `ApacheConversionExtensions.kt` (pacote `number`) — Apache Commons Lang3

```kotlin
// NumberUtils — parse tolerante a null/inválido
fun String?.toIntOrDefault(defaultValue: Int = 0): Int
fun String?.toLongOrDefault(defaultValue: Long = 0L): Long
fun String?.toDoubleOrDefault(defaultValue: Double = 0.0): Double
fun String?.toBigDecimalOrDefault(defaultValue: BigDecimal = BigDecimal.ZERO): BigDecimal
fun String?.isNumber(): Boolean      // NumberUtils.isCreatable
fun String?.isParsable(): Boolean    // NumberUtils.isParsable

// BOOLEANOS SPED — convenção "S"/"N" e 1/0
// Estas funções resolvem um gap CRÍTICO: cada serviço reimplementa a mesma lógica
/** true → "S", false/null → "N" */
fun Boolean?.toStringSimNao(): String
/** null → null, true → "S", false → "N" */
fun Boolean?.toStringSimNaoOrNull(): String?
/** "S"/"SIM"/"1"/"TRUE" (case-insensitive) → true; qualquer outro → false */
fun String?.toBooleanSimNao(): Boolean
/** null/blank → null */
fun String?.toBooleanSimNaoOrNull(): Boolean?
/** SPED numérico: 1 → true, 0 → false, outros → false */
fun Int.toBooleanSped(): Boolean
/** true → 1, false → 0 */
fun Boolean.toIntSped(): Int
```

---

### FR-005 — `FiscalStatisticsExtensions.kt` (pacote `math`) — Apache Commons Math3

```kotlin
data class EstatisticaFiscal(
    val count: Int,
    val sum: BigDecimal,
    val mean: BigDecimal,
    val min: BigDecimal,
    val max: BigDecimal,
    val median: BigDecimal,
    val stdDev: BigDecimal,
    val variance: BigDecimal
)

// Estatísticas básicas — null-safe, retornam ZERO para lista vazia
fun List<BigDecimal>.mean(scale: Int = 2): BigDecimal
fun List<BigDecimal>.median(): BigDecimal
fun List<BigDecimal>.standardDeviation(scale: Int = 4): BigDecimal
fun List<BigDecimal>.variance(scale: Int = 4): BigDecimal
fun List<BigDecimal>.percentile(p: Double): BigDecimal  // ex: percentile(0.75) = 3o quartil

// Análise de tendência
fun List<BigDecimal>.simpleMovingAverage(window: Int): List<BigDecimal>
/** Variação percentual: (atual - anterior) / anterior * 100 */
fun BigDecimal.growthRate(previous: BigDecimal, scale: Int = 2): BigDecimal?  // null se previous == 0

// Sumário completo
fun List<BigDecimal>.estatistica(): EstatisticaFiscal
```

---

### FR-006 — `ApacheCodecExtensions.kt` (pacote `codec`) — Apache Commons Codec

```kotlin
// Hex encoding (fingerprints de certificados, verificação de integridade)
fun ByteArray.encodeHex(): String       // Hex.encodeHexString
fun String.decodeHex(): ByteArray       // Hex.decodeHex
fun String.encodeHexString(): String    // alias semântico para ByteArray

// Digest (verificação de integridade SPED, hash de arquivos XML)
fun ByteArray.md5Apache(): ByteArray    // DigestUtils.md5 — prefixo para não conflitar com Kotlin
fun ByteArray.sha256Apache(): ByteArray
fun String.md5Hex(): String            // DigestUtils.md5Hex
fun String.sha256Hex(): String         // DigestUtils.sha256Hex
fun String.sha512Hex(): String

// Base64 (complementa ZipExtensions.kt existente)
fun String.encodeBase64Apache(): String  // Base64.encodeBase64String
fun ByteArray.encodeBase64Apache(): String
fun String.decodeBase64Apache(): ByteArray

// URL encoding (parâmetros de portais governamentais)
fun String.urlEncode(charset: String = "UTF-8"): String   // URLCodec.encode
fun String.urlDecode(charset: String = "UTF-8"): String
```

---

## Critérios de Sucesso

| # | Critério | Verificação |
|---|---------|-------------|
| CS-01 | `"Empresa & Cia <Ltda>".escapeXml10()` == `"Empresa &amp; Cia &lt;Ltda&gt;"` | Teste unitário |
| CS-02 | `"Açúcar Cúbico".stripAccents()` == `"Acucar Cubico"` | Teste unitário |
| CS-03 | `"CONTABILIZEI TECNOLOGIA".jaroWinklerSimilarity("CONTABILIZEI TECNOLOGIA LTDA")` >= 0.90 | Teste unitário |
| CS-04 | `true.toStringSimNao()` == `"S"` e `false.toStringSimNao()` == `"N"` | Teste unitário |
| CS-05 | `"S".toBooleanSimNao()` == `true` e `"N".toBooleanSimNao()` == `false` | Teste unitário |
| CS-06 | `1.toBooleanSped()` == `true` e `0.toBooleanSped()` == `false` | Teste unitário |
| CS-07 | `listOf(bd("10"), bd("20"), bd("30")).mean()` == `BigDecimal("20.00")` | Teste unitário |
| CS-08 | `"test".sha256Hex().length` == 64 e é determinístico | Teste unitário |
| CS-09 | `"abc".escapeXml10().unescapeXml()` == `"abc"` (roundtrip) | Teste unitário |
| CS-10 | `mvn -q verify` com JaCoCo 90%/90% para `kotlin-apache` | Build CI |
| CS-11 | `"Empresa, SA".escapeCsv()` produz campo CSV válido (com aspas) | Teste unitário |

---

## Entidades-Chave

| Entidade | Arquivo | Pacote |
|---------|---------|--------|
| `ApacheTextExtensions` | `text/ApacheTextExtensions.kt` | `...govcore.apache.text` |
| `ApacheEscapeExtensions` | `text/ApacheEscapeExtensions.kt` | `...govcore.apache.text` |
| `ApacheSimilarityExtensions` | `text/ApacheSimilarityExtensions.kt` | `...govcore.apache.text` |
| `ApacheConversionExtensions` | `number/ApacheConversionExtensions.kt` | `...govcore.apache.number` |
| `EstatisticaFiscal` | `math/FiscalStatisticsExtensions.kt` | `...govcore.apache.math` |
| `FiscalStatisticsExtensions` | `math/FiscalStatisticsExtensions.kt` | `...govcore.apache.math` |
| `ApacheCodecExtensions` | `codec/ApacheCodecExtensions.kt` | `...govcore.apache.codec` |

---

## Dependências e Restrições

- **commons-lang3:3.14.0** — StringUtils, NumberUtils, StringEscapeUtils (até 3.x)
- **commons-text:1.12.0** — StringEscapeUtils (novo pacote), WordUtils, similarity
- **commons-math3:3.6.1** — DescriptiveStatistics, Percentile
- **commons-codec:1.17.0** — DigestUtils, Hex, Base64, URLCodec
- **Sem sobreposição com ZipExtensions.kt**: O `encodeBase64Apache` deve ter nome distinto do `compressToBase64` existente
- **Kotlin 1.8.22**: Nenhuma feature pós-1.8
- **Zero Java no módulo**: Apenas arquivos `.kt`
- **AR-002**: Sem frameworks
- **JaCoCo 90%**: Mesmos gates do restante do projeto
- **ReDoS**: Nenhuma regex criada neste módulo — usar apenas Apache Commons que já é linear
- **Nomenclatura**: Onde houver risco de colisão com Kotlin stdlib (ex: `reverse`, `capitalize`), usar sufixo ou nome alternativo descritivo

---

## Não-Objetivos

- Não substituir `GovTextNormalizer` Java existente — complementar com robustez Apache
- Não duplicar `ZipExtensions.kt` (GZIP + Base64 já existem)
- Não criar DSL para formatação SPED posicional (candidato a spec independente)
- `Commons IO` — sem caso de uso adicional ao `FileExtensions.kt` existente
- `Commons BeanUtils` — sem reflexão em tempo de execução
- `Commons Collections` — coberto pelo módulo kotlin-guava
