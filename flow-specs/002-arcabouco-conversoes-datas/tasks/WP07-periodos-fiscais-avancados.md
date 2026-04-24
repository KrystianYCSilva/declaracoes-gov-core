---
work_package_id: WP07
title: Períodos Fiscais Avançados (Trimestre, Semestre, CompetenciaRange)
lane: planned
dependencies: [WP04, WP05]
created_at: '2026-04-24T00:00:00.000000+00:00'
subtasks:
- T025
- T026
- T027
- T028
- T029
addendum: true
addendum_reason: "Gap analysis identificou ausência de conceitos fiscais de Trimestre, Semestre e iteração idiomática de ranges de competência — não cobertos no escopo original da spec 002."
---

# WP07 — Períodos Fiscais Avançados (Adendo à Spec 002)

> **ADENDO**: Este WP é um complemento ao escopo original da spec 002, identificado em gap analysis posterior.
> Não modifica nenhum dos WP01–WP06. Depende de WP04 (DateExtensions.kt).
> Agent responsável: Claude (não Copilot).

## Objetivo

Adicionar ao módulo `declaracoes-gov-core-kotlin` suporte idiomático a conceitos fiscais de granularidade temporal acima do mês:

1. `Trimestre` — value class para 1T-4T (IRPJ/CSLL usa apuração trimestral)
2. `Semestre` — value class para 1S-2S (alguns regimes usam semestres)
3. `CompetenciaRange` — iteração idiomática sobre um intervalo fechado de `YearMonth`
4. `Int.plusPeriodos(n)` / `Int.minusPeriodos(n)` — navegação de N meses em formato AAAAMM
5. `ExercicioFiscal` — value class para ano fiscal (Jan–Dez; base para IRPF e IRPJ)

---

## T025 — `Trimestre` value class

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/FiscalPeriodTypes.kt` ← NOVO arquivo

### API

```kotlin
/**
 * Trimestre fiscal. Valores válidos: 1, 2, 3, 4.
 * Utilizado em IRPJ/CSLL (apuração trimestral) e DCTF.
 */
@JvmInline
value class Trimestre(val value: Int) {
    init {
        require(value in 1..4) { "Trimestre deve ser 1, 2, 3 ou 4: $value" }
    }

    /** Retorna o número do trimestre: 1..4 */
    val numero: Int get() = value

    /** Meses deste trimestre (ex: 1T → [jan, fev, mar]) */
    fun competencias(ano: Int): List<YearMonth>

    /** Primeiro mês do trimestre */
    fun primeiraMes(ano: Int): YearMonth

    /** Último mês do trimestre */
    fun ultimoMes(ano: Int): YearMonth

    /** Trimestre seguinte (1→2, 4→1 do próximo ano — retorna par Trimestre+ano) */
    fun proximo(ano: Int): Pair<Trimestre, Int>

    /** Trimestre anterior */
    fun anterior(ano: Int): Pair<Trimestre, Int>

    override fun toString(): String  // ex: "1T" ou "1º Trimestre"

    companion object {
        /** Retorna o trimestre do YearMonth fornecido */
        fun of(yearMonth: YearMonth): Trimestre
        /** Retorna o trimestre do mês do ano fornecido (mês base-1) */
        fun of(mes: Int): Trimestre
    }
}

// Extensão em YearMonth
val YearMonth.trimestre: Trimestre
```

### Testes obrigatórios

```kotlin
@Test fun trimestreDeveSerEntre1e4()
@Test fun trimestreMenor1LancaIllegalArgument()
@Test fun trimestreMaior4LancaIllegalArgument()
@Test fun competenciasDoTrimestre1Tem3Meses()
@Test fun competenciasDoTrimestre1SaoJanFevMar()
@Test fun competenciasDoTrimestre4SaoOutNovDez()
@Test fun yaerMonth_abril_eSegundoTrimestre()
@Test fun yaerMonth_dezembro_eQuartoTrimestre()
@Test fun proximoTrimestre_4_retorna_1_doAnoSeguinte()
```

---

## T026 — `Semestre` value class

**Arquivo:** mesmo `FiscalPeriodTypes.kt`

### API

```kotlin
/**
 * Semestre fiscal. Valores válidos: 1, 2.
 * Utilizado em IRPJ lucro presumido (opção semestral) e ISS municipal.
 */
@JvmInline
value class Semestre(val value: Int) {
    init {
        require(value in 1..2) { "Semestre deve ser 1 ou 2: $value" }
    }

    fun competencias(ano: Int): List<YearMonth>   // 6 meses
    fun primeiraMes(ano: Int): YearMonth
    fun ultimoMes(ano: Int): YearMonth
    fun proximo(ano: Int): Pair<Semestre, Int>
    fun anterior(ano: Int): Pair<Semestre, Int>

    override fun toString(): String  // "1S" ou "1º Semestre"

    companion object {
        fun of(yearMonth: YearMonth): Semestre
        fun of(mes: Int): Semestre
    }
}

val YearMonth.semestre: Semestre
```

---

## T027 — `CompetenciaRange` — iteração idiomática de YearMonth

**Arquivo:** mesmo `FiscalPeriodTypes.kt`

### API

```kotlin
/**
 * Intervalo fechado de competências [inicio, fim].
 * Permite iteração idiomática: `(jan2024..dez2024).forEach { ym -> ... }`
 */
class CompetenciaRange(
    override val start: YearMonth,
    override val endInclusive: YearMonth
) : ClosedRange<YearMonth>, Iterable<YearMonth> {

    override fun iterator(): Iterator<YearMonth>  // itera mês a mês

    fun toList(): List<YearMonth>
    fun monthCount(): Int
    fun contains(periodo: Int): Boolean  // AAAAMM format
    fun isEmpty(): Boolean

    /** Retorna os YearMonth do range que NÃO estão na coleção fornecida */
    fun gaps(covered: Collection<YearMonth>): List<YearMonth>
}

// Operador rangeTo em YearMonth
operator fun YearMonth.rangeTo(other: YearMonth): CompetenciaRange

// Extensões de uso
fun CompetenciaRange.forEachCompetencia(action: (YearMonth) -> Unit)
fun <R> CompetenciaRange.mapCompetencias(transform: (YearMonth) -> R): List<R>

// Usando Int AAAAMM
infix fun Int.atePeriodo(outro: Int): CompetenciaRange
```

### Testes obrigatórios

```kotlin
@Test fun rangeDeJanADezTem12Meses()
@Test fun rangeIteraEmOrdemCrescente()
@Test fun rangeContemYearMonth()
@Test fun rangeContemPeriodoAAAMM()
@Test fun gapsRetornaCompetenciasFaltantes()
@Test fun rangeVazioQuandoInicioAposF im()
@Test fun atePeriodoFunciona_202401_atePeriodo_202412()
```

---

## T028 — `plusPeriodos(n)` / `minusPeriodos(n)` em Int (AAAAMM)

**Arquivo:** expansão de `DateExtensions.kt` existente (após linha dos métodos `periodoSeguinte`/`periodoAnterior`)

### API

```kotlin
/**
 * Avança N meses a partir deste período AAAAMM.
 * Ex: 202412.plusPeriodos(2) → 202502
 */
fun Int.plusPeriodos(n: Int): Int =
    toYearMonth().plusMonths(n.toLong()).toPeriodo()

/**
 * Recua N meses a partir deste período AAAAMM.
 * Ex: 202502.minusPeriodos(2) → 202412
 */
fun Int.minusPeriodos(n: Int): Int =
    toYearMonth().minusMonths(n.toLong()).toPeriodo()

/**
 * Retorna lista com todos os períodos de [this] até [outro] (inclusive), crescente.
 * Ex: 202401.periodosAte(202406) → [202401, 202402, 202403, 202404, 202405, 202406]
 */
fun Int.periodosAte(outro: Int): List<Int>
```

### Testes obrigatórios

```kotlin
@Test fun plusPeriodos_avancaViradaDeAno()    // 202412.plusPeriodos(1) == 202501
@Test fun plusPeriodos_avancaMultiplosMeses() // 202401.plusPeriodos(13) == 202502
@Test fun minusPeriodos_recuaViradaDeAno()    // 202501.minusPeriodos(1) == 202412
@Test fun periodosAte_listaCompleta()          // 202401.periodosAte(202406).size == 6
@Test fun periodosAte_mesmoMes()               // 202401.periodosAte(202401) == [202401]
```

---

## T029 — `ExercicioFiscal` value class

**Arquivo:** mesmo `FiscalPeriodTypes.kt`

### API

```kotlin
/**
 * Exercício fiscal (ano civil Jan–Dez).
 * Base para IRPJ (ano-base) e IRPF (ano de referência = ano-base + 1).
 */
@JvmInline
value class ExercicioFiscal(val ano: Int) {
    init {
        require(ano in 1900..2100) { "Ano do exercício fiscal inválido: $ano" }
    }

    /** 12 competências do exercício: YearMonth.of(ano, 1)..YearMonth.of(ano, 12) */
    fun competencias(): CompetenciaRange

    /** Primeiro período: 202401 */
    fun primeiroPeriodo(): Int

    /** Último período: 202412 */
    fun ultimoPeriodo(): Int

    /** Ano de referência IRPF (exercício de apuração = ano + 1) */
    val anoReferenciaIrpf: Int get() = ano + 1

    override fun toString(): String  // "2024"

    companion object {
        fun atual(): ExercicioFiscal
        fun of(yearMonth: YearMonth): ExercicioFiscal  // extrai o ano
    }
}

val YearMonth.exercicioFiscal: ExercicioFiscal
```

---

## Atualização de `tasks.md` da spec 002

Adicionar linha à tabela de Work Packages:

```
| WP07 | Períodos Fiscais Avançados (Trimestre, Semestre, CompetenciaRange) | T025-T029 | WP04, WP05 | planned |
```

Adicionar ao grafo de dependências:
```
WP05 → WP07
WP04 → WP07
```

## Checklist de Validação

- [ ] `Trimestre(5)` lança `IllegalArgumentException`
- [ ] `YearMonth.of(2024, 4).trimestre.value` == 2
- [ ] `(YearMonth.of(2024, 1).rangeTo(YearMonth.of(2024, 12))).toList().size` == 12
- [ ] `202412.plusPeriodos(1)` == 202501
- [ ] `ExercicioFiscal(2024).competencias().monthCount()` == 12
- [ ] `ExercicioFiscal(2024).anoReferenciaIrpf` == 2025
- [ ] `mvn -q verify` no módulo `kotlin` passa sem erros

## Review Feedback

TBD

