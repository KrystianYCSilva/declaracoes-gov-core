# Feature Specification: Arcabouço Financeiro e Monetário

**Feature Branch**: `feature/004-arcabouco-financeiro-monetario`
**Created**: 2026-04-24
**Status**: Planned

## Source Document

**Tipo:** `generic-discovery`
**Origem:** Gap analysis via Claude Opus sobre código existente (7 módulos) e flow specs 002/003
**Data:** 2026-04-24

### Resumo da análise

O ecossistema `declaracoes-*` manipula valores monetários, alíquotas, percentuais e bases de cálculo em praticamente todos os serviços. Atualmente esses conceitos são representados como `BigDecimal` ou `String` sem semântica tipada, levando a:

| Padrão problemático | Ocorrência estimada | Risco |
|---------------------|---------------------|-------|
| `BigDecimal` usado tanto para valor quanto para alíquota | Ubíquo | Mistura de semânticas; multiplicação errada |
| `NumberFormat.getInstance(new Locale("pt","BR"))` inline | 30+ arquivos | Instância não reutilizável; formatação inconsistente |
| Arredondamento `HALF_UP` onde deveria ser `HALF_EVEN` (ABNT) | 15+ cálculos | Erro fiscal; não-conformidade com ABNT NBR 5891 |
| `BigDecimal.setScale(2, ROUND_HALF_UP)` sem constante nomeada | 50+ ocorrências | Magic numbers espalhados |
| `list.stream().reduce(BigDecimal.ZERO, BigDecimal::add)` | 20+ pontos | Boilerplate verboso; não trata null |
| Regime tributário como `String` literal | 10+ serviços | Sem validação; typos silenciosos |

---

## Problema

O ecossistema `declaracoes-*` não possui:
1. **Value classes tipadas** para valores monetários, alíquotas e percentuais — prevenindo erros de mistura de semânticas em tempo de compilação
2. **Formatação centralizada** para BRL (`R$ 1.234,56`) e SPED (`1234,56`) — cada serviço reimplementa com risco de inconsistência
3. **Constantes fiscais de arredondamento** (ABNT NBR 5891 = `HALF_EVEN`) — o padrão errado (`HALF_UP`) é frequentemente usado
4. **Enums de domínio** para regime tributário e situação cadastral — strings literais espalhadas sem validação
5. **Extensões de coleção** para agregação fiscal por período — boilerplate verboso e repetitivo
6. **Validação composta** que acumula múltiplos erros — só existe fail-fast no modelo atual

---

## Cenários de Uso

| # | Ator | Cenário | Resultado esperado |
|---|------|---------|-------------------|
| U1 | Dev Kotlin | Declara parâmetro como `ValorMonetario` em vez de `BigDecimal` | `fun calcularImposto(base: ValorMonetario, aliquota: Aliquota)` — compilador impede mistura |
| U2 | Dev Kotlin | Aplica alíquota sobre base de cálculo | `aliquota.aplicarSobre(base)` retorna `ValorMonetario` com escala 2 e `HALF_EVEN` |
| U3 | Dev Kotlin | Formata valor para exibição BRL | `valor.formatBrl()` → `"R$ 1.234,56"` |
| U4 | Dev Kotlin | Formata valor para campo SPED | `valor.toSpedDecimal(2)` → `"1234,56"` (vírgula, sem separador de milhar) |
| U5 | Dev Java | Usa constante de arredondamento fiscal | `GovNumberConstants.ROUNDING_FISCAL` == `RoundingMode.HALF_EVEN` |
| U6 | Dev Java | Formata `BigDecimal` para BRL | `GovCurrencyFormats.formatBrl(valor)` → `"1.234,56"` |
| U7 | Dev Java | Formata `BigDecimal` para campo SPED | `GovCurrencyFormats.toSpedDecimal(valor, 2)` → `"1234,56"` |
| U8 | Dev Kotlin | Cria percentual tipado | `val taxa = Percentual(BigDecimal("15.00"))` com `require(value >= 0)` implícito |
| U9 | Dev Kotlin | Soma lista de valores com null | `listOf(bd("100"), null, bd("200")).sumOrZero()` → `BigDecimal("300")` |
| U10 | Dev Kotlin | Soma acumulada para relatório | `listOf(bd("100"), bd("150"), bd("200")).cumulativeSum()` → `[100, 250, 450]` |
| U11 | Dev Kotlin | Formata percentual | `Percentual(BigDecimal("15.00")).formatPercentual()` → `"15,00%"` |
| U12 | Dev Kotlin | Identifica regime tributário | `RegimeTributario.SIMPLES_NACIONAL` — enum com código RFB integrado |
| U13 | Dev Kotlin | Agrupa eventos por período | `eventos.groupByPeriodo { it.competencia }` → `Map<YearMonth, List<Evento>>` |
| U14 | Dev Kotlin | Merge de resumos fiscais | `mapa1.mergeWith(mapa2, BigDecimal::add)` → mapa combinado |
| U15 | Dev Kotlin | Preenche meses sem movimento | `mapa.fillGaps(jan..dez, BigDecimal.ZERO)` → todos os meses presentes |
| U16 | Dev Kotlin | Acumula validações sem fail-fast | `validacaoComposta { validar("cnpj") { ... }; validar("periodo") { ... } }` → lista de falhas |
| U17 | Dev Kotlin | Converte alíquota para fator decimal | `Aliquota(BigDecimal("15.00")).toFator()` → `BigDecimal("0.15")` |
| U18 | Dev Kotlin | Aritmética entre `ValorMonetario` | `val total = valor1 + valor2` via operator overloading |
| U19 | Dev Java | Consulta situação cadastral por código | `SituacaoCadastral.fromCodigo(2)` → `Optional.of(ATIVA)` |
| U20 | Dev Kotlin | Cria `ValorMonetario` a partir de centavos | `ValorMonetario.of(12356L)` → `ValorMonetario(BigDecimal("123.56"))` |

---

## Requisitos Funcionais

### RF-001 — `GovNumberConstants` (módulo `domain`)

Classe `final` com construtor privado. **Complementa** `GovBigDecimalConstants` (não a substitui).
Todos os campos `public static final`.

```java
// Arredondamento (ABNT NBR 5891)
public static final RoundingMode ROUNDING_FISCAL    = RoundingMode.HALF_EVEN;
public static final RoundingMode ROUNDING_TRUNCATE  = RoundingMode.DOWN;
public static final RoundingMode ROUNDING_COMERCIAL = RoundingMode.HALF_UP;

// Escalas padrão por contexto fiscal
public static final int SCALE_MONETARIO      = 2;   // valores monetários em geral
public static final int SCALE_ALIQUOTA       = 4;   // alíquotas (ex: 4.5000%)
public static final int SCALE_PERCENTUAL     = 2;   // percentuais de exibição
public static final int SCALE_QUANTIDADE     = 4;   // quantidades fiscais (NF-e)
public static final int SCALE_PRECO_UNITARIO = 10;  // vUnitTrib NF-e (até 10 casas)

// Constantes BigDecimal (complemento a GovBigDecimalConstants: CEM, UM, DOIS, DEZ, ZERO_DECIMAL)
public static final BigDecimal TRES             = new BigDecimal("3");
public static final BigDecimal QUATRO           = new BigDecimal("4");
public static final BigDecimal CINCO            = new BigDecimal("5");
public static final BigDecimal SEIS             = new BigDecimal("6");
public static final BigDecimal SETE             = new BigDecimal("7");
public static final BigDecimal OITO             = new BigDecimal("8");
public static final BigDecimal NOVE             = new BigDecimal("9");
public static final BigDecimal ONZE             = new BigDecimal("11");
public static final BigDecimal DOZE             = new BigDecimal("12");
public static final BigDecimal QUINZE           = new BigDecimal("15");
public static final BigDecimal VINTE            = new BigDecimal("20");
public static final BigDecimal VINTE_CINCO      = new BigDecimal("25");
public static final BigDecimal TRINTA           = new BigDecimal("30");
public static final BigDecimal CINQUENTA        = new BigDecimal("50");
public static final BigDecimal MIL              = new BigDecimal("1000");
public static final BigDecimal MENOS_UM         = new BigDecimal("-1");

// Alíquotas federais nominais frequentes (convenience — para cálculos; não substitui tabela dinâmica)
public static final BigDecimal ALIQUOTA_IRPJ               = new BigDecimal("15.00");
public static final BigDecimal ALIQUOTA_IRPJ_ADICIONAL     = new BigDecimal("10.00");
public static final BigDecimal ALIQUOTA_CSLL_LP            = new BigDecimal("9.00");
public static final BigDecimal ALIQUOTA_CSLL_IF            = new BigDecimal("15.00");
public static final BigDecimal ALIQUOTA_PIS_CUMULATIVO     = new BigDecimal("0.65");
public static final BigDecimal ALIQUOTA_PIS_NAO_CUMULATIVO = new BigDecimal("1.65");
public static final BigDecimal ALIQUOTA_COFINS_CUMULATIVO     = new BigDecimal("3.00");
public static final BigDecimal ALIQUOTA_COFINS_NAO_CUMULATIVO = new BigDecimal("7.60");
public static final BigDecimal ALIQUOTA_INSS_PATRONAL      = new BigDecimal("20.00");
public static final BigDecimal ALIQUOTA_ISS_MINIMO         = new BigDecimal("2.00");
```

---

### RF-002 — `GovCurrencyFormats` (módulo `format`)

Classe `final`, construtor privado. Null-safe: retorna `null` para entrada `null`.
Usa `java.text.NumberFormat` com `new Locale("pt", "BR")` — instância criada internamente (não exposta).

```java
/**
 * Formata BigDecimal para padrão monetário brasileiro sem símbolo.
 * null → null. BigDecimal("1234.56") → "1.234,56"
 */
public static String formatBrl(BigDecimal value)

/**
 * Formata com prefixo "R$ ".
 * null → null. BigDecimal("1234.56") → "R$ 1.234,56"
 */
public static String formatBrlWithSymbol(BigDecimal value)

/**
 * Formata para campo numérico SPED/posicional.
 * Vírgula decimal, sem separador de milhar, escala fixa via setScale(scale, HALF_EVEN).
 * null → null. (BigDecimal("1234.5"), 2) → "1234,50"
 */
public static String toSpedDecimal(BigDecimal value, int scale)

/**
 * Formata como percentual brasileiro.
 * null → null. BigDecimal("15.5") → "15,50%"
 */
public static String formatPercentual(BigDecimal value)

/**
 * Parse de string BRL para BigDecimal.
 * Aceita: "1.234,56" | "R$ 1.234,56" | "1234,56"
 * null/blank/inválido → null
 */
public static BigDecimal parseBrl(String value)

/**
 * Parse de string SPED (vírgula decimal) para BigDecimal.
 * Aceita: "1234,56" | "0,00"
 * null/blank/inválido → null
 */
public static BigDecimal parseSpedDecimal(String value)
```

---

### RF-003 — Enums de Domínio (módulo `domain`, pacote `table`)

#### `RegimeTributario.java`

Códigos conforme SPED/eSocial/PGDAS.

```java
public enum RegimeTributario {
    SIMPLES_NACIONAL(1, "Simples Nacional"),
    SIMPLES_NACIONAL_EXCESSO(2, "Simples Nacional - Excesso de sublimite"),
    LUCRO_PRESUMIDO(3, "Lucro Presumido"),
    LUCRO_REAL(4, "Lucro Real"),
    LUCRO_ARBITRADO(5, "Lucro Arbitrado"),
    MEI(6, "Microempreendedor Individual"),
    IMUNE(7, "Imune"),
    ISENTA(8, "Isenta");

    private final int codigo;
    private final String descricao;

    RegimeTributario(int codigo, String descricao) { ... }
    public int getCodigo() { ... }
    public String getDescricao() { ... }

    /** Busca por código RFB. Retorna Optional.empty() se não encontrado. */
    public static Optional<RegimeTributario> fromCodigo(int codigo)
}
```

#### `SituacaoCadastral.java`

Códigos conforme Receita Federal do Brasil.

```java
public enum SituacaoCadastral {
    NULA(1, "Nula"),
    ATIVA(2, "Ativa"),
    SUSPENSA(3, "Suspensa"),
    INAPTA(4, "Inapta"),
    BAIXADA(8, "Baixada");

    private final int codigo;
    private final String descricao;

    SituacaoCadastral(int codigo, String descricao) { ... }
    public int getCodigo() { ... }
    public String getDescricao() { ... }

    public static Optional<SituacaoCadastral> fromCodigo(int codigo)
}
```

---

### RF-004 — Value Classes Financeiras: `FinancialTypes.kt` (módulo `kotlin`)

Novo arquivo no pacote `br.com.contabilizei.obrigacoes.govcore.ext`.

#### `ValorMonetario`

```kotlin
/**
 * Valor monetário em BRL com escala máxima de 2 casas decimais.
 * Todos os cálculos usam RoundingMode.HALF_EVEN (ABNT NBR 5891).
 */
@JvmInline
value class ValorMonetario(val value: BigDecimal) {
    init {
        require(value.scale() <= GovNumberConstants.SCALE_MONETARIO) {
            "ValorMonetario: máximo ${GovNumberConstants.SCALE_MONETARIO} casas decimais, encontrado: ${value.scale()}"
        }
    }

    operator fun plus(other: ValorMonetario): ValorMonetario
    operator fun minus(other: ValorMonetario): ValorMonetario
    operator fun times(factor: BigDecimal): ValorMonetario  // arredonda HALF_EVEN scale 2
    operator fun unaryMinus(): ValorMonetario
    operator fun compareTo(other: ValorMonetario): Int

    fun isZero(): Boolean
    fun isPositive(): Boolean
    fun isNegative(): Boolean
    fun abs(): ValorMonetario
    fun negate(): ValorMonetario
    fun max(other: ValorMonetario): ValorMonetario
    fun min(other: ValorMonetario): ValorMonetario

    /** "1.234,56" */
    fun formatBrl(): String
    /** "R$ 1.234,56" */
    fun formatBrlWithSymbol(): String
    /** "1234,56" para campos SPED */
    fun toSpedDecimal(): String

    override fun toString(): String  // delega a value.toPlainString()

    companion object {
        val ZERO: ValorMonetario get() = ValorMonetario(BigDecimal.ZERO.setScale(2))

        /** Aplica setScale(2, HALF_EVEN) antes de criar. */
        fun of(value: BigDecimal): ValorMonetario
        /** Parse de string decimal (ponto ou vírgula). */
        fun of(value: String): ValorMonetario
        /** Converte centavos inteiros: 12356L → ValorMonetario("123.56") */
        fun of(centavos: Long): ValorMonetario
        fun ofOrNull(value: BigDecimal?): ValorMonetario?
        fun ofOrNull(value: String?): ValorMonetario?
    }
}
```

#### `Aliquota`

```kotlin
/**
 * Alíquota tributária percentual entre 0 e 100.
 * Ex: Aliquota(BigDecimal("15.00")) representa 15%.
 */
@JvmInline
value class Aliquota(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO && value <= GovBigDecimalConstants.CEM) {
            "Alíquota deve estar entre 0 e 100: $value"
        }
    }

    /**
     * Aplica sobre base: base * (value / 100), escala 2, HALF_EVEN.
     * Ex: Aliquota("15").aplicarSobre(BigDecimal("1000")) → BigDecimal("150.00")
     */
    fun aplicarSobre(base: BigDecimal): BigDecimal
    fun aplicarSobre(base: ValorMonetario): ValorMonetario
    fun aplicarSobre(base: BaseCalculo): ValorMonetario

    /** 15.00 → BigDecimal("0.1500") com scale SCALE_ALIQUOTA */
    fun toFator(): BigDecimal

    /** "15,00%" */
    fun formatAliquota(): String

    override fun toString(): String

    companion object {
        val ZERO: Aliquota
        fun of(value: BigDecimal): Aliquota
        fun of(value: String): Aliquota
        fun ofOrNull(value: BigDecimal?): Aliquota?
        fun ofOrNull(value: String?): Aliquota?

        // Alíquotas federais nominais — convenience
        val IRPJ: Aliquota               // 15.00
        val IRPJ_ADICIONAL: Aliquota     // 10.00
        val CSLL_LP: Aliquota            // 9.00
        val CSLL_IF: Aliquota            // 15.00
        val PIS_CUMULATIVO: Aliquota     // 0.65
        val PIS_NAO_CUMULATIVO: Aliquota // 1.65
        val COFINS_CUMULATIVO: Aliquota     // 3.00
        val COFINS_NAO_CUMULATIVO: Aliquota // 7.60
    }
}
```

#### `Percentual`

```kotlin
/**
 * Percentual genérico (≥ 0). Distinto de Aliquota — sem contexto tributário específico.
 * Ex: desconto de 5%, crescimento de 120%.
 */
@JvmInline
value class Percentual(val value: BigDecimal) {
    init {
        require(value >= BigDecimal.ZERO) { "Percentual não pode ser negativo: $value" }
    }

    fun aplicarSobre(base: BigDecimal): BigDecimal
    fun toFator(): BigDecimal

    /** "15,00%" */
    fun formatPercentual(): String

    override fun toString(): String

    companion object {
        val ZERO: Percentual
        val CEM: Percentual
        fun of(value: BigDecimal): Percentual
        fun of(value: String): Percentual
        fun ofOrNull(value: BigDecimal?): Percentual?
    }
}
```

#### `BaseCalculo`

```kotlin
/**
 * Base de cálculo tributária. Semanticamente distinta de ValorMonetario —
 * representa o valor sobre o qual alíquotas são aplicadas.
 */
@JvmInline
value class BaseCalculo(val value: BigDecimal) {

    fun aplicar(aliquota: Aliquota): ValorMonetario
    fun isZero(): Boolean
    fun isPositive(): Boolean
    fun formatBrl(): String

    override fun toString(): String

    companion object {
        val ZERO: BaseCalculo
        fun of(value: BigDecimal): BaseCalculo
        fun ofOrNull(value: BigDecimal?): BaseCalculo?
    }
}
```

---

### RF-005 — Extensões Numéricas e Monetárias: expansão de `NumberExtensions.kt` (módulo `kotlin`)

Adicionar ao arquivo existente. Não modificar extensões existentes.

```kotlin
// ---------------------------------------------------------------------------
// Constante Kotlin para arredondamento fiscal
// ---------------------------------------------------------------------------
val BigDecimal.Companion.ROUNDING_FISCAL: RoundingMode get() = RoundingMode.HALF_EVEN

// ---------------------------------------------------------------------------
// Formatação monetária — delega a GovCurrencyFormats
// ---------------------------------------------------------------------------
/** "1.234,56" */
fun BigDecimal?.formatBrl(): String
/** "R$ 1.234,56" */
fun BigDecimal?.formatBrlWithSymbol(): String
/** "1234,56" com escala fixa */
fun BigDecimal?.toSpedDecimal(scale: Int = GovNumberConstants.SCALE_MONETARIO): String
/** "15,00%" */
fun BigDecimal?.formatPercentual(): String
/** Parse "1.234,56" ou "R$ 1.234,56" → BigDecimal? */
fun String?.parseBrl(): BigDecimal?
/** Parse "1234,56" → BigDecimal? */
fun String?.parseSpedDecimal(): BigDecimal?

// ---------------------------------------------------------------------------
// Conversões para value classes
// ---------------------------------------------------------------------------
fun BigDecimal.toValorMonetario(): ValorMonetario
fun BigDecimal.toAliquota(): Aliquota
fun BigDecimal.toPercentual(): Percentual
fun BigDecimal.toBaseCalculo(): BaseCalculo
fun BigDecimal?.toValorMonetarioOrZero(): ValorMonetario
fun String.toValorMonetario(): ValorMonetario
fun String.toAliquota(): Aliquota
/** 12356L → ValorMonetario("123.56") */
fun Long.centavosToValorMonetario(): ValorMonetario

// ---------------------------------------------------------------------------
// Cálculo fiscal idiomático
// ---------------------------------------------------------------------------
/** Calcula base * aliquota / 100 com HALF_EVEN, scale 2 */
infix fun BigDecimal.comAliquota(aliquota: BigDecimal): BigDecimal
fun BigDecimal.calcularImposto(aliquota: Aliquota): ValorMonetario

// ---------------------------------------------------------------------------
// Agregação de coleções (null-safe)
// ---------------------------------------------------------------------------
fun Iterable<BigDecimal?>.sumOrZero(): BigDecimal
fun List<BigDecimal>.cumulativeSum(): List<BigDecimal>
fun Iterable<BigDecimal?>.minOrZero(): BigDecimal
fun Iterable<BigDecimal?>.maxOrZero(): BigDecimal
fun Iterable<BigDecimal?>.averageOrZero(scale: Int = GovNumberConstants.SCALE_MONETARIO): BigDecimal
```

---

### RF-006 — Constantes Complementares: expansão de `TextExtensions.kt` e `NumberExtensions.kt`

Adicionar ao arquivo existente. Não modificar extensões existentes.

```kotlin
// TextExtensions.kt — novas constantes Char (complementam EMPTY, SPACE, PIPE, SLASH, DASH, COMMA, DOT)
val Char.Companion.SEMICOLON: Char get() = ';'
val Char.Companion.COLON: Char get() = ':'
val Char.Companion.AT: Char get() = '@'
val Char.Companion.HASH: Char get() = '#'
val Char.Companion.PERCENT: Char get() = '%'
val Char.Companion.AMPERSAND: Char get() = '&'
val Char.Companion.UNDERSCORE: Char get() = '_'
val Char.Companion.EQUALS: Char get() = '='
val Char.Companion.PLUS: Char get() = '+'
val Char.Companion.ASTERISK: Char get() = '*'
val Char.Companion.QUESTION: Char get() = '?'
val Char.Companion.BACKSLASH: Char get() = '\\'
val Char.Companion.OPEN_PAREN: Char get() = '('
val Char.Companion.CLOSE_PAREN: Char get() = ')'
val Char.Companion.SINGLE_QUOTE: Char get() = '\''
val Char.Companion.DOUBLE_QUOTE: Char get() = '"'
val Char.Companion.LINE_FEED: Char get() = '\n'
val Char.Companion.CARRIAGE_RETURN: Char get() = '\r'
val Char.Companion.LESS_THAN: Char get() = '<'
val Char.Companion.GREATER_THAN: Char get() = '>'

// TextExtensions.kt — constantes String (complementam String.EMPTY)
val String.Companion.PIPE: String by lazy { "|" }
val String.Companion.DASH: String by lazy { "-" }
val String.Companion.SLASH: String by lazy { "/" }
val String.Companion.COMMA: String by lazy { "," }
val String.Companion.DOT: String by lazy { "." }
val String.Companion.SEMICOLON: String by lazy { ";" }
val String.Companion.COLON: String by lazy { ":" }
val String.Companion.SPACE: String by lazy { " " }
val String.Companion.NEW_LINE: String by lazy { "\n" }
val String.Companion.CRLF: String by lazy { "\r\n" }

// NumberExtensions.kt — novas constantes Int (complementam ZERO, ONE)
val Int.Companion.TWO: Int get() = 2
val Int.Companion.THREE: Int get() = 3
val Int.Companion.FOUR: Int get() = 4
val Int.Companion.FIVE: Int get() = 5
val Int.Companion.TEN: Int get() = 10
val Int.Companion.TWELVE: Int get() = 12
val Int.Companion.HUNDRED: Int get() = 100
val Int.Companion.THOUSAND: Int get() = 1000

val Long.Companion.ZERO: Long get() = 0L
val Long.Companion.MINUS_ONE: Long get() = -1L

// NumberExtensions.kt — Regex companions adicionais (complementam NON_DIGITS, SPACE, etc.)
val Regex.Companion.DIGITS_ONLY: Regex by lazy { Regex("\\d+") }
val Regex.Companion.LETTERS_ONLY: Regex by lazy { Regex("[a-zA-Z]+") }
val Regex.Companion.LETTERS_AND_DIGITS: Regex by lazy { Regex("[a-zA-Z0-9]+") }
```

---

### RF-007 — Extensões de Coleção Fiscal: expansão de `CollectionExtensions.kt` (módulo `kotlin`)

```kotlin
// ---------------------------------------------------------------------------
// Agrupamento por contexto fiscal
// ---------------------------------------------------------------------------
fun <T> Iterable<T>.groupByPeriodo(keySelector: (T) -> YearMonth): Map<YearMonth, List<T>>
fun <T> Iterable<T>.groupByCnpj(keySelector: (T) -> Cnpj): Map<Cnpj, List<T>>

// ---------------------------------------------------------------------------
// Operações sobre mapas fiscais
// ---------------------------------------------------------------------------
/**
 * Merge de dois mapas aplicando função de combinação para chaves duplicadas.
 * Ex: resumoJan.mergeWith(resumoFev) { a, b -> a + b }
 */
fun <K, V> Map<K, V>.mergeWith(other: Map<K, V>, combine: (V, V) -> V): Map<K, V>

/**
 * Preenche meses ausentes no range com o valor padrão.
 * Garante que todos os meses do range estão presentes no mapa retornado.
 */
fun <V> Map<YearMonth, V>.fillGaps(range: ClosedRange<YearMonth>, defaultValue: V): LinkedHashMap<YearMonth, V>

/** Soma os valores de um Map<*, BigDecimal> */
fun <K> Map<K, BigDecimal>.sumValues(): BigDecimal

/** Retorna Map ordenado crescentemente por chave YearMonth */
fun <V> Map<YearMonth, V>.sortedByPeriodo(): Map<YearMonth, V>

/** Retorna os N pares com maior value (para rankings fiscais) */
fun <K> Map<K, BigDecimal>.topN(n: Int): Map<K, BigDecimal>
```

---

### RF-008 — Validação Composta: expansão de `ValidationExtensions.kt` (módulo `kotlin`)

```kotlin
/**
 * Combina lista de resultados: valido = todos válidos, mensagem = join das mensagens de erro.
 */
fun List<ResultadoValidacao>.combinar(): ResultadoValidacao

/**
 * Combina dois resultados de validação.
 */
operator fun ResultadoValidacao.plus(other: ResultadoValidacao): ResultadoValidacao

/**
 * DSL para acumulação de validações sem fail-fast.
 *
 * Exemplo:
 * val resultado = validacaoComposta {
 *     validar("CNPJ") { cnpj.validarCnpj() }
 *     validar("Período") { periodo.toString().validarPeriodo() }
 *     validarCondicao("Valor", valor.isPositive()) { "Valor deve ser positivo" }
 * }
 * resultado.valido          // false se qualquer validação falhou
 * resultado.erros.size      // total de falhas
 */
fun validacaoComposta(block: ValidacaoCompostaBuilder.() -> Unit): ResultadoCompostoValidacao

class ValidacaoCompostaBuilder {
    fun validar(campo: String, bloco: () -> ResultadoValidacao)
    fun validarCondicao(campo: String, condicao: Boolean, mensagem: () -> String)
}

data class ResultadoCompostoValidacao(
    val valido: Boolean,
    val erros: List<ErroValidacao>
) {
    data class ErroValidacao(
        val campo: String,
        val mensagem: String?,
        val nivel: ValidationLevel?
    )
    fun toResultadoValidacao(): ResultadoValidacao  // para integração com código existente
}
```

---

## Critérios de Sucesso

| # | Critério | Verificação |
|---|---------|-------------|
| CS-01 | `ValorMonetario.of("1234.56").formatBrl()` == `"1.234,56"` | Teste unitário |
| CS-02 | `Aliquota(BigDecimal("15")).aplicarSobre(BigDecimal("1000"))` == `BigDecimal("150.00")` | Teste unitário |
| CS-03 | `valor1 + valor2` compila e retorna `ValorMonetario` | Teste unitário |
| CS-04 | `GovCurrencyFormats.toSpedDecimal(new BigDecimal("1234.5"), 2)` == `"1234,50"` | Teste unitário |
| CS-05 | `listOf(bd("100"), null, bd("200")).sumOrZero()` == `BigDecimal("300")` | Teste unitário |
| CS-06 | `listOf(bd("100"), bd("150"), bd("200")).cumulativeSum()` == `[100, 250, 450]` | Teste unitário |
| CS-07 | `RegimeTributario.fromCodigo(1)` == `Optional.of(SIMPLES_NACIONAL)` | Teste unitário |
| CS-08 | `validacaoComposta { ... }` acumula todos os erros, não falha no primeiro | Teste unitário |
| CS-09 | Arredondamento: `BigDecimal("2.5").setScale(0, HALF_EVEN)` == `2` (não 3) | Teste de conformidade ABNT |
| CS-10 | `ValorMonetario.of(12356L)` == `ValorMonetario(BigDecimal("123.56"))` | Teste unitário |
| CS-11 | `Aliquota.IRPJ.value` == `BigDecimal("15.00")` | Teste unitário |
| CS-12 | `mvn -q verify` no reator completo passa 90%/90% JaCoCo | Build CI |

---

## Entidades-Chave

| Entidade | Módulo | Tipo | Pacote |
|---------|--------|------|--------|
| `GovNumberConstants` | `domain` | `final class` (Java) | `...govcore.util` |
| `GovCurrencyFormats` | `format` | `final class` (Java) | `...govcore.util` |
| `RegimeTributario` | `domain` | `enum` (Java) | `...govcore.table` |
| `SituacaoCadastral` | `domain` | `enum` (Java) | `...govcore.table` |
| `FinancialTypes.kt` | `kotlin` | arquivo novo | `...govcore.ext` |
| `NumberExtensions.kt` | `kotlin` | expansão | `...govcore.ext` |
| `TextExtensions.kt` | `kotlin` | expansão (constantes) | `...govcore.ext` |
| `CollectionExtensions.kt` | `kotlin` | expansão | `...govcore.ext` |
| `ValidationExtensions.kt` | `kotlin` | expansão | `...govcore.ext` |

---

## Dependências e Restrições

- **AR-003**: `domain` JDK-only — sem Jackson ou dependências externas
- **AR-004**: Java 8 baseline — sem `var`, records, sealed classes
- **AR-002**: Sem frameworks — sem Spring, Jakarta, Lombok
- **AR-008**: JaCoCo 90% linha/branch em `domain`, `format`; gates existentes em `kotlin`
- **Retrocompatibilidade**: `GovBigDecimalConstants`, `GovNumberUtils`, `GovNumberFormats`, extensões existentes nos arquivos expandidos **não devem ser modificadas**
- **Kotlin 1.8.22**: `@JvmInline value class` com `init` block disponível desde 1.5
- `GovCurrencyFormats` depende de `GovNumberConstants` (domain → format, respeitando AR-003)
- Value classes Kotlin importam de `GovCurrencyFormats` e `GovNumberConstants` via dependência transitiva
- Enums ficam no pacote `table` junto com `Uf` e `TipoInscricao` existentes
- **Dependência de spec 002**: `fillGaps` usa `YearMonth.rangeTo` (WP04 da 002). Implementar `fillGaps` somente após WP04 da 002 estar merged ou reimplementar iteração de range localmente

---

## Não-Objetivos (v1)

- Valor por extenso (`valorPorExtenso`) — complexidade alta, candidato v2
- Cálculos tributários complexos progressivos (IRPJ com adicional, tabela DAS) — ficam nos serviços
- Tipos multi-moeda — apenas BRL nesta versão
- `InscricaoEstadual` com validação por UF — 27 algoritmos distintos, candidato a spec independente
- Tabelas de alíquotas vigentes com datas de vigência — dependem de source externo

---

## Premissas

- Branch `develop` é a base e o destino
- `GovBigDecimalConstants` já existe e **não será modificada** — `GovNumberConstants` é classe nova complementar
- Enums adicionados ao pacote `table` já existente (junto com `Uf`, `TipoInscricao`, `TipoAmbiente`)
- Value classes Kotlin delegam formatação a `GovCurrencyFormats` (Java)
- O arredondamento `HALF_EVEN` é o padrão para todos os cálculos de value classes financeiras
