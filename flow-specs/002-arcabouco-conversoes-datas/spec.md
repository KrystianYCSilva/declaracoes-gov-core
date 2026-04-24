## Source Document

**Tipo:** `generic-discovery`
**Origem:** Sessão de planejamento Copilot CLI — análise de 5 projetos do ecossistema (`obrigacoes-service-esocial`, `obrigacoes-service-reinf`, `obrigacoes-service-dctfweb`, `contabilizei-back-core`, `folha-service`)
**Data:** 2026-04-24
**Path:** `C:/Users/krystian.silva_conta/.copilot/session-state/07f6024f-55c4-4382-a77c-c2de1f1938e4/plan.md`

### Resumo do contexto

Varredura de 5 projetos do ecossistema `declaracoes-*` identificou os seguintes padrões repetitivos sem utilitário central:

| Padrão | Ocorrências | Impacto |
|--------|------------|---------|
| `DateTimeFormatter.ofPattern(...)` inline | 78 | Formatters não reutilizados; risco de inconsistência |
| `toLocalDateTime(...)` / `toLocalDate()` | 59 / 43 | Conversões verbosas e duplicadas |
| `new SimpleDateFormat(...)` inline | 35 | Instâncias não thread-safe; anti-pattern |
| `ZoneId.of("America/Sao_Paulo")` hardcoded | 20+ arquivos | String mágica espalhada |
| `Calendar.getInstance(TimeZone...)` | 15 | Boilerplate de 5+ linhas |
| `Duration.of(...)` / `Duration.between(...)` | 38 | Sem wrappers idiomáticos |

O módulo `declaracoes-gov-core-kotlin` já possui `DateExtensions.kt` com extensões básicas, mas carece de: Calendar properties, ZoneId↔TimeZone, Duration idiomático, String parsing, value classes tipadas.

---

## Problema

O ecossistema `declaracoes-*` não possui uma API centralizada para:
1. Conversões entre tipos legados (`java.util.Date`, `java.util.Calendar`) e `java.time`
2. Constantes reutilizáveis de fuso horário e formatters thread-safe
3. Parsing e formatação de strings de data com padrões recorrentes
4. Types seguros (value classes Kotlin) para unidades temporais primitivas (`Long`/`Int` sem semântica) e strings de data formatadas (`String` anônimas)

O resultado é duplicação de código, inconsistências de comportamento e vulnerabilidades silenciosas ao longo dos serviços.

---

## Cenários de Uso

| # | Ator | Cenário | Resultado esperado |
|---|------|---------|-------------------|
| U1 | Desenvolvedor Java | Converte `java.util.Date` para `LocalDate` | `LegacyDateConverter.toLocalDate(date)` — uma linha, sem verbosidade |
| U2 | Desenvolvedor Java | Converte `LocalDateTime` para `java.util.Date` | `LegacyDateConverter.toDate(localDateTime)` com ZoneId systemDefault |
| U3 | Desenvolvedor Java | Precisa do ZoneId de São Paulo | `GovTimeConstants.ZONE_SAO_PAULO` — sem string hardcoded |
| U4 | Desenvolvedor Java | Formata `LocalDate` no padrão BR | `GovDateParser.formatBrDate(date)` retorna `"01/06/2025"` |
| U5 | Desenvolvedor Java | Obtém primeiro/último dia do mês | `JavaTimeConversions.toFirstDayOfMonth(ym)` / `toLastDayOfMonth(ym)` |
| U6 | Desenvolvedor Java | Calcula duração entre dois `LocalDate` | `JavaTimeConversions.between(d1, d2)` retorna `Duration` |
| U7 | Desenvolvedor Java | Faz parse XML gov `"2025-06-01T10:00:00-03:00"` | `GovDateFormats.parseXmlDateTime(s)` retorna `ZonedDateTime` |
| U8 | Desenvolvedor Kotlin | Acessa ano de `Calendar` de forma idiomática | `calendar.year` em vez de `calendar.get(Calendar.YEAR)` |
| U9 | Desenvolvedor Kotlin | Cria Duration de forma legível | `2.hours + 30.minutes` em vez de `Duration.ofHours(2).plus(Duration.ofMinutes(30))` |
| U10 | Desenvolvedor Kotlin | Parseia string `"01/06/2025"` para `LocalDate` | `"01/06/2025".toBrDate()` retorna `LocalDate?` |
| U11 | Desenvolvedor Kotlin | Tipagem explícita de epoch | `val ts: EpochMillis = Instant.now().epochMillisValue` — sem `Long` anônimo |
| U12 | Desenvolvedor Kotlin | Parâmetro de método com tipo claro | `fun process(data: DataISO)` — não `data: String` |
| U13 | Desenvolvedor Kotlin | Valida string de data no construtor | `DataISO.of("invalido")` lança `IllegalArgumentException` com mensagem clara |
| U14 | Desenvolvedor Kotlin | Roundtrip string → tipo | `LocalDate.now().toDataBR().toLocalDate()` preserva o valor |

---

## Requisitos Funcionais

### RF-001 — `GovTimeConstants` (módulo `domain`)

Classe `final` com construtor privado, somente campos `static final`.

**ZoneId (Brasil + UTC):**
- `ZONE_SAO_PAULO` = `ZoneId.of("America/Sao_Paulo")` — padrão da maioria dos sistemas
- `ZONE_UTC` = `ZoneOffset.UTC.normalized()`
- `ZONE_MANAUS` = `ZoneId.of("America/Manaus")`
- `ZONE_FORTALEZA` = `ZoneId.of("America/Fortaleza")`
- `ZONE_RIO_BRANCO` = `ZoneId.of("America/Rio_Branco")`
- `ZONE_NORONHA` = `ZoneId.of("America/Noronha")`
- `ZONE_CUIABA` = `ZoneId.of("America/Cuiaba")`
- `ZONE_CAMPO_GRANDE` = `ZoneId.of("America/Campo_Grande")`

**ZoneOffset:**
- `OFFSET_BRT` = `ZoneOffset.of("-03:00")` (Brasília)
- `OFFSET_AMT` = `ZoneOffset.of("-04:00")` (Amazonas)
- `OFFSET_ACT` = `ZoneOffset.of("-05:00")` (Acre)
- `OFFSET_UTC` = `ZoneOffset.UTC`

**TimeZone (interop com legado):**
- `TZ_SAO_PAULO` = `TimeZone.getTimeZone(ZONE_SAO_PAULO)`
- `TZ_UTC` = `TimeZone.getTimeZone("UTC")`

**DateTimeFormatter thread-safe (todos `static final`):**
- `FORMATTER_YYYYMM` — `yyyyMM`
- `FORMATTER_YYYY_MM` — `yyyy-MM`
- `FORMATTER_ISO_DATE` — `yyyy-MM-dd`
- `FORMATTER_BR_DATE` — `dd/MM/yyyy`
- `FORMATTER_ISO_DATE_TIME` — `yyyy-MM-dd HH:mm:ss`
- `FORMATTER_BR_DATE_TIME` — `dd/MM/yyyy HH:mm:ss`
- `FORMATTER_TIMESTAMP` — `yyyyMMddHHmmss`
- `FORMATTER_ISO_OFFSET` — `yyyy-MM-dd'T'HH:mm:ssXXX`

---

### RF-002 — `LegacyDateConverter` (módulo `domain`)

Classe `final` com construtor privado, métodos `static`. Null-safe: retorna `null` para entrada `null`.  
Estratégia ZoneId: default `ZoneId.systemDefault()` + sobrecarga com `ZoneId`/`ZoneOffset` explícito.

**`java.util.Date` → `java.time`:**
- `toInstant(Date)` → `Instant`
- `toLocalDate(Date)` → `LocalDate` (systemDefault)
- `toLocalDate(Date, ZoneId)` → `LocalDate`
- `toLocalDateTime(Date)` → `LocalDateTime` (systemDefault)
- `toLocalDateTime(Date, ZoneId)` → `LocalDateTime`
- `toZonedDateTime(Date, ZoneId)` → `ZonedDateTime`
- `toOffsetDateTime(Date, ZoneOffset)` → `OffsetDateTime`

**`java.time` → `java.util.Date`:**
- `toDate(Instant)` → `Date`
- `toDate(LocalDate)` → `Date` (systemDefault, início do dia)
- `toDate(LocalDate, ZoneId)` → `Date`
- `toDate(LocalDateTime)` → `Date` (systemDefault)
- `toDate(LocalDateTime, ZoneId)` → `Date`
- `toDate(ZonedDateTime)` → `Date`
- `toDate(OffsetDateTime)` → `Date`

**`java.util.Calendar` → `java.time`:**
- `toInstant(Calendar)` → `Instant`
- `toLocalDate(Calendar)` → `LocalDate`
- `toLocalDateTime(Calendar)` → `LocalDateTime`
- `toZonedDateTime(Calendar)` → `ZonedDateTime`

**`java.time` → `java.util.Calendar`:**
- `toCalendar(ZonedDateTime)` → `Calendar` (preserva fuso)
- `toCalendar(LocalDateTime)` → `Calendar` (systemDefault)
- `toCalendar(LocalDateTime, ZoneId)` → `Calendar`
- `toCalendar(LocalDate)` → `Calendar` (systemDefault, início do dia)
- `toCalendar(LocalDate, ZoneId)` → `Calendar`

---

### RF-003 — `JavaTimeConversions` (módulo `domain`)

Classe `final` com construtor privado, métodos `static`. Null-safe para todos os métodos.

**`LocalDate` ↔ `LocalDateTime`:**
- `toStartOfDay(LocalDate)` → `LocalDateTime` (00:00:00)
- `toEndOfDay(LocalDate)` → `LocalDateTime` (23:59:59.999999999)
- `toLocalDate(LocalDateTime)` → `LocalDate`

**`YearMonth` ↔ `LocalDate`:**
- `toFirstDayOfMonth(YearMonth)` → `LocalDate`
- `toLastDayOfMonth(YearMonth)` → `LocalDate`
- `toYearMonth(LocalDate)` → `YearMonth`

**`Instant` ↔ `LocalDate`/`LocalDateTime`:**
- `toLocalDate(Instant, ZoneId)` → `LocalDate`
- `toLocalDateTime(Instant)` → `LocalDateTime` (systemDefault)
- `toLocalDateTime(Instant, ZoneId)` → `LocalDateTime`
- `toInstant(LocalDateTime, ZoneId)` → `Instant`
- `toInstant(LocalDate, ZoneId)` → `Instant` (início do dia)

**`ZonedDateTime` / `OffsetDateTime` ↔ `LocalDateTime`:**
- `toLocalDateTime(ZonedDateTime)` → `LocalDateTime`
- `toZonedDateTime(LocalDateTime, ZoneId)` → `ZonedDateTime`
- `toLocalDateTime(OffsetDateTime)` → `LocalDateTime`
- `toOffsetDateTime(LocalDateTime, ZoneOffset)` → `OffsetDateTime`
- `toOffsetDateTime(ZonedDateTime)` → `OffsetDateTime`

**`Duration` — criação e extração:**
- `durationOfDays(long)`, `durationOfHours(long)`, `durationOfMinutes(long)`, `durationOfSeconds(long)`, `durationOfMillis(long)` → `Duration`
- `between(LocalDate, LocalDate)` → `Duration` (via start-of-day)
- `between(LocalDateTime, LocalDateTime)` → `Duration`
- `between(Instant, Instant)` → `Duration`
- `toDays(Duration)`, `toHours(Duration)`, `toMinutes(Duration)`, `toSeconds(Duration)`, `toMillis(Duration)` → `long`

---

### RF-004 — `GovDateParser` (módulo `domain`)

Classe `final` com construtor privado. Null-safe: retorna `null` para entrada `null`/vazia.  
Usa constantes de `GovTimeConstants`. Lança `DateTimeParseException` (unchecked) para strings não-nulas inválidas.

**String → `java.time`:**
- `parseLocalDate(String)` → `LocalDate` (ISO: `yyyy-MM-dd`)
- `parseLocalDate(String, DateTimeFormatter)` → `LocalDate`
- `parseBrDate(String)` → `LocalDate` (`dd/MM/yyyy`)
- `parseLocalDateTime(String)` → `LocalDateTime` (`yyyy-MM-dd HH:mm:ss`)
- `parseLocalDateTime(String, DateTimeFormatter)` → `LocalDateTime`
- `parseZonedDateTime(String, DateTimeFormatter, ZoneId)` → `ZonedDateTime`
- `parseOffsetDateTime(String)` → `OffsetDateTime` (ISO offset: `yyyy-MM-dd'T'HH:mm:ssXXX`)
- `parseInstant(String)` → `Instant` (ISO-8601)
- `parseDuration(String)` → `Duration` (ISO-8601: `PT2H30M`)
- `parseYearMonth(String)` → `YearMonth` (aceita `yyyy-MM` ou `yyyyMM`)

**`java.time` → String:**
- `format(LocalDate, DateTimeFormatter)` → `String`
- `formatBrDate(LocalDate)` → `String` (`dd/MM/yyyy`)
- `formatIsoDate(LocalDate)` → `String` (`yyyy-MM-dd`)
- `format(LocalDateTime, DateTimeFormatter)` → `String`
- `formatIsoDateTime(LocalDateTime)` → `String` (`yyyy-MM-dd HH:mm:ss`)
- `formatBrDateTime(LocalDateTime)` → `String` (`dd/MM/yyyy HH:mm:ss`)
- `formatTimestamp(LocalDateTime)` → `String` (`yyyyMMddHHmmss`)
- `format(ZonedDateTime, DateTimeFormatter)` → `String`
- `formatIsoOffset(ZonedDateTime)` → `String` (`yyyy-MM-dd'T'HH:mm:ssXXX`)
- `formatDuration(Duration)` → `String` (ISO-8601)

---

### RF-005 — `GovDateFormats` (módulo `format`)

Classe `final` com construtor privado. Complementa `XmlDates` e `GovCompetenceFormats` (sem modificá-los).  
Null-safe.

- `parseXmlDateTime(String)` → `ZonedDateTime` (formato RFB: `yyyy-MM-dd'T'HH:mm:ssXXX`)
- `formatXmlDateTime(ZonedDateTime)` → `String`
- `parseDataCompetencia(String)` → `YearMonth` (delega a `GovCompetenceFormats.parse`)
- `formatDataCompetencia(YearMonth)` → `String` (delega a `GovCompetenceFormats.toXmlFormat`)

---

### RF-006 — Expansão de `DateExtensions.kt` (módulo `kotlin`)

Adicionar ao arquivo existente `DateExtensions.kt`. Não modificar extensões já existentes.

**`Calendar` — propriedades read-only:**
```kotlin
val Calendar.year: Int          // get(Calendar.YEAR)
val Calendar.month: Int         // get(Calendar.MONTH) + 1  ← base 1, não base 0
val Calendar.dayOfMonth: Int    // get(Calendar.DAY_OF_MONTH)
val Calendar.hourOfDay: Int     // get(Calendar.HOUR_OF_DAY)
val Calendar.minute: Int        // get(Calendar.MINUTE)
val Calendar.second: Int        // get(Calendar.SECOND)
val Calendar.millisecond: Int   // get(Calendar.MILLISECOND)
```

**`Calendar` — conversões (delegam a `LegacyDateConverter`):**
```kotlin
fun Calendar.toLocalDate(): LocalDate
fun Calendar.toLocalDateTime(): LocalDateTime
fun Calendar.toZonedDateTime(): ZonedDateTime
fun Calendar.toInstant(): Instant
```

**`ZoneId` ↔ `TimeZone`:**
```kotlin
fun ZoneId.toTimeZone(): TimeZone   // TimeZone.getTimeZone(this)
fun TimeZone.toZoneId(): ZoneId     // this.toZoneId()
```

**`Duration` — criação idiomática:**
```kotlin
val Int.days: Duration
val Int.hours: Duration
val Int.minutes: Duration
val Int.seconds: Duration
val Int.milliseconds: Duration
val Long.days: Duration
val Long.hours: Duration
val Long.minutes: Duration
val Long.seconds: Duration
val Long.milliseconds: Duration
```

**`String` — parsing idiomático (null-safe, retorna `null` para `null`/vazio):**
```kotlin
fun String?.toLocalDate(): LocalDate?
fun String?.toLocalDate(formatter: DateTimeFormatter): LocalDate?
fun String?.toBrDate(): LocalDate?
fun String?.toLocalDateTime(): LocalDateTime?
fun String?.toLocalDateTime(formatter: DateTimeFormatter): LocalDateTime?
fun String?.toOffsetDateTime(): OffsetDateTime?
fun String?.toInstant(): Instant?
fun String?.toDuration(): Duration?
```

**Formatação:**
```kotlin
fun LocalDate.format(pattern: String): String
fun LocalDateTime.format(pattern: String): String
fun ZonedDateTime.format(pattern: String): String
fun LocalDate.toBrFormat(): String      // dd/MM/yyyy
fun LocalDateTime.toBrFormat(): String  // dd/MM/yyyy HH:mm:ss
fun Duration.toIsoString(): String      // PT2H30M
```

**Helpers de `LocalDate`/`YearMonth`:**
```kotlin
fun LocalDate.atStartOfDay(): LocalDateTime
fun LocalDate.atEndOfDay(): LocalDateTime
fun YearMonth.firstDay(): LocalDate
fun YearMonth.lastDay(): LocalDate
```

---

### RF-007 — `TemporalValueTypes.kt` (módulo `kotlin`) — nova classe

`@JvmInline value class` para unidades temporais primitivas sem semântica explícita no tipo.  
Todas as value classes:
- São imutáveis e inline (sem overhead em runtime)
- Têm propriedade `val value` do tipo primitivo
- Incluem `init { require(...) }` para validação de range quando aplicável

```kotlin
@JvmInline value class DayOfMonth(val value: Int)      // init: require(value in 1..31)
@JvmInline value class DayOfYear(val value: Int)       // init: require(value in 1..366)
@JvmInline value class HourOfDay(val value: Int)       // init: require(value in 0..23)
@JvmInline value class MinuteOfHour(val value: Int)    // init: require(value in 0..59)
@JvmInline value class SecondOfMinute(val value: Int)  // init: require(value in 0..59)
@JvmInline value class SecondOfDay(val value: Long)    // init: require(value in 0..86399)
@JvmInline value class NanoOfSecond(val value: Int)    // init: require(value in 0..999_999_999)
@JvmInline value class NanoOfDay(val value: Long)
@JvmInline value class EpochMillis(val value: Long)
@JvmInline value class EpochSeconds(val value: Long)
@JvmInline value class EpochDay(val value: Long)
```

**Extensões de extração (receiver → value class):**
```kotlin
val LocalDate.dayOfMonthValue: DayOfMonth
val LocalDate.dayOfYearValue: DayOfYear
val LocalDateTime.hourOfDayValue: HourOfDay
val LocalDateTime.minuteOfHourValue: MinuteOfHour
val LocalDateTime.secondOfMinuteValue: SecondOfMinute
val Instant.epochMillisValue: EpochMillis
val Instant.epochSecondsValue: EpochSeconds
val LocalDate.epochDayValue: EpochDay
```

**Extensões de construção (value class → java.time):**
```kotlin
fun EpochMillis.toInstant(): Instant
fun EpochSeconds.toInstant(): Instant
fun EpochDay.toLocalDate(): LocalDate
```

---

### RF-008 — `DateStringTypes.kt` (módulo `kotlin`) — nova classe

`@JvmInline value class` para strings de data formatadas com validação via regex.  
Cada tipo:
- Valida o formato no construtor via regex (lança `IllegalArgumentException` com mensagem descritiva)
- Fornece `companion object { fun of(s: String): T; fun ofOrNull(s: String?): T? }`
- Implementa `toString()` retornando `value`
- Fornece método(s) de conversão para o tipo `java.time` correspondente

| Tipo | Regex | Formato | Conversão principal |
|------|-------|---------|---------------------|
| `DataISO` | `\d{4}-\d{2}-\d{2}` | `yyyy-MM-dd` | `toLocalDate(): LocalDate` |
| `DataBR` | `\d{2}/\d{2}/\d{4}` | `dd/MM/yyyy` | `toLocalDate(): LocalDate` |
| `DataCompacta` | `\d{8}` | `yyyyMMdd` | `toLocalDate(): LocalDate` |
| `DataHoraISO` | `\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}` | `yyyy-MM-dd HH:mm:ss` | `toLocalDateTime(): LocalDateTime` |
| `DataHoraBR` | `\d{2}/\d{2}/\d{4} \d{2}:\d{2}:\d{2}` | `dd/MM/yyyy HH:mm:ss` | `toLocalDateTime(): LocalDateTime` |
| `DataHoraOffset` | `\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}[+-]\d{2}:\d{2}` | ISO offset | `toOffsetDateTime(): OffsetDateTime`; `toZonedDateTime(): ZonedDateTime` |
| `CompetenciaISO` | `\d{4}-\d{2}` | `yyyy-MM` | `toYearMonth(): YearMonth` |
| `CompetenciaCompacta` | `\d{6}` | `yyyyMM` | `toYearMonth(): YearMonth`; `toPeriodo(): Int` |
| `Timestamp` | `\d{14}` | `yyyyMMddHHmmss` | `toLocalDateTime(): LocalDateTime` |

**Extensões inversas (java.time → DateStringType):**
```kotlin
fun LocalDate.toDataISO(): DataISO
fun LocalDate.toDataBR(): DataBR
fun LocalDate.toDataCompacta(): DataCompacta
fun LocalDateTime.toDataHoraISO(): DataHoraISO
fun LocalDateTime.toDataHoraBR(): DataHoraBR
fun OffsetDateTime.toDataHoraOffset(): DataHoraOffset
fun YearMonth.toCompetenciaISO(): CompetenciaISO
fun YearMonth.toCompetenciaCompacta(): CompetenciaCompacta
fun LocalDateTime.toTimestamp(): Timestamp
```

---

## Critérios de Sucesso

| # | Critério | Verificação |
|---|---------|-------------|
| CS-01 | `LegacyDateConverter` cobre todas as combinações Date/Calendar ↔ java.time | Testes com roundtrips (A → B → A) em UTC e America/Sao_Paulo |
| CS-02 | `GovTimeConstants.ZONE_SAO_PAULO` é válido e idêntico a `ZoneId.of("America/Sao_Paulo")` | Teste de igualdade |
| CS-03 | `GovDateParser.parseBrDate("01/06/2025")` retorna `LocalDate.of(2025, 6, 1)` | Teste unitário |
| CS-04 | `GovDateParser.formatBrDate(null)` retorna `null` | Teste de null-safety |
| CS-05 | `JavaTimeConversions.toLastDayOfMonth(YearMonth.of(2024, 2))` retorna `LocalDate.of(2024, 2, 29)` (bissexto) | Teste de edge case |
| CS-06 | `3.hours + 30.minutes` em Kotlin produz `Duration.ofHours(3).plusMinutes(30)` | Teste idiomático Kotlin |
| CS-07 | `DataISO.of("invalido")` lança `IllegalArgumentException` com mensagem clara | Teste de validação |
| CS-08 | `LocalDate.now().toDataBR().toLocalDate()` preserva a data (roundtrip) | Teste de consistência |
| CS-09 | `EpochMillis(Instant.now().toEpochMilli()).toInstant()` preserva milissegundos | Teste de roundtrip |
| CS-10 | `mvn -q verify` no reator completo passa todos os testes e JaCoCo gates (90%/90%) | Build CI |

---

## Entidades-Chave

| Entidade | Módulo | Tipo | Pacote |
|---------|--------|------|--------|
| `GovTimeConstants` | `domain` | `final class` (Java) | `br.com.contabilizei.obrigacoes.govcore.util` |
| `LegacyDateConverter` | `domain` | `final class` (Java) | `br.com.contabilizei.obrigacoes.govcore.util` |
| `JavaTimeConversions` | `domain` | `final class` (Java) | `br.com.contabilizei.obrigacoes.govcore.util` |
| `GovDateParser` | `domain` | `final class` (Java) | `br.com.contabilizei.obrigacoes.govcore.util` |
| `GovDateFormats` | `format` | `final class` (Java) | `br.com.contabilizei.obrigacoes.govcore.util` |
| `DateExtensions.kt` | `kotlin` | expansão de arquivo existente | `br.com.contabilizei.obrigacoes.govcore.ext` |
| `TemporalValueTypes.kt` | `kotlin` | novo arquivo | `br.com.contabilizei.obrigacoes.govcore.ext` |
| `DateStringTypes.kt` | `kotlin` | novo arquivo | `br.com.contabilizei.obrigacoes.govcore.ext` |

---

## Dependências e Restrições

- **AR-003**: `domain` é JDK-only — sem Jackson, xmlsec ou dependências externas
- **AR-004**: Java 8 baseline — sem `var`, sem lambda record patterns, sem Java 9+ APIs
- **AR-002**: Sem frameworks — sem Spring, sem Jakarta, sem Lombok
- **AR-008**: JaCoCo gates: `domain` e `format` = 90% linha/branch; `kotlin` = gates existentes
- **Retrocompatibilidade**: `GovDateUtils`, `XmlDates`, `GovCompetenceFormats` e `DateExtensions.kt` (parte existente) não devem ser modificados
- **Kotlin 1.8.22**: `@JvmInline value class` disponível (disponível desde Kotlin 1.5)
- **Colisão de nomes no `domain`**: `toLocalDate(Calendar)` e `toLocalDateTime(Calendar)` em `LegacyDateConverter` têm mesmo nome de método de `GovDateUtils.convertToLocalDateTime(Date)` — manter os dois; `GovDateUtils` permanece para retrocompatibilidade
- O `GovDateParser.parseYearMonth` deve aceitar `yyyy-MM` e `yyyyMM` (delegando ao `GovCompetenceFormats.parse` existente no módulo `format`) — ATENÇÃO: `GovDateParser` é no `domain`, não pode depender do `format`; deve reimplementar a lógica de forma independente com `DateTimeFormatter`

---

## Não-Objetivos (v1)

- Integração com APIs de feriados ou calendário fiscal
- Conversão para/de Joda-Time (legado de projetos externos)
- Formatters específicos de declarações (eSocial, Reinf) — ficam em seus respectivos módulos
- `Period` (anos/meses) — apenas `Duration` (dias/horas/minutos/segundos)
- Suporte a microsegundos em `DateStringTypes` (regex simplificada)
- Localização (locale) nos formatters — padrão `Locale.ROOT` implícito

---

## Premissas

- A branch `develop` é a branch base e de destino
- Os 4 módulos Java (`domain`, `format`, `crypto`, `kotlin`) já estão configurados no reator Maven e compilam com `mvn -q verify`
- O módulo `kotlin` já tem `DateExtensions.kt` com conteúdo que não deve ser alterado, apenas expandido
- `GovCompetenceFormats` em `format` já cobre `YearMonth` → String e String → `YearMonth`; `GovDateParser` em `domain` deve reimplementar de forma independente (sem dependência de `format`)

---

## Apêndice: Documento de Origem (Planning Session)

### Análise de padrões — 5 projetos

Dados brutos coletados durante a sessão de planejamento em 2026-04-24:

```
Padrão identificado            Ocorrências
DateTimeFormatter               78
toLocalDateTime                 59
toLocalDate()                   43
toInstant                       36
SimpleDateFormat                35
Duration.of                     35
ZoneOffset                      30
atZone                          21
Calendar.getInstance            15
Date.from                       10
ZoneId.of                        6
Duration.between                 3
```

Projetos analisados: `obrigacoes-service-esocial`, `obrigacoes-service-reinf`, `obrigacoes-service-dctfweb`, `contabilizei-back-core`, `folha-service`.

Principais anti-patterns encontrados:
1. `new SimpleDateFormat("yyyyMM")` — 26+ arquivos, 80+ instâncias
2. `ZoneId.of("America/Sao_Paulo")` — 20+ arquivos hardcoded
3. `date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()` — 12+ arquivos
4. `Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"))` — 6+ arquivos
5. `Date.from(date.atZone(ZoneId.systemDefault()).toInstant())` — 8+ arquivos
