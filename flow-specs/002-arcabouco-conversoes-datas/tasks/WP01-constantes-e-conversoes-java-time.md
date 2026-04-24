---
work_package_id: WP01
title: GovTimeConstants + JavaTimeConversions (domain)
lane: "doing"
dependencies: []
created_at: '2026-04-24T13:51:14.642700+00:00'
subtasks:
- T001
- T002
- T003
- T004
loops_planned_to_doing: "1"
doing_started_at: "2026-04-24T14:05:32.025371+00:00"
loops_doing_to_for_review: "1"
for_review_started_at: "2026-04-24T13:57:20.905318+00:00"
loops_for_review_to_done: "1"
ended_at: "2026-04-24T13:57:25.696868+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
loops_done_to_doing: "1"
---

# WP01 — GovTimeConstants + JavaTimeConversions (domain)

## Objetivo

Criar as duas classes utilitárias de base no módulo `declaracoes-gov-core-domain`:

1. `GovTimeConstants` — todas as constantes de fuso horário e formatters thread-safe.
2. `JavaTimeConversions` — conversões internas entre tipos `java.time` (sem tocar em `java.util.Date`/`Calendar`).

Essas classes são a fundação de todos os WPs subsequentes.

---

## T001 — Criar `GovTimeConstants.java`

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovTimeConstants.java`

### Instruções

1. Crie a classe como `public final class GovTimeConstants` com construtor `private GovTimeConstants()`.
2. Adicione Javadoc de classe em português: `"Constantes de fuso horário e formatadores de data/hora para o ecossistema declaracoes-*."`.
3. Declare todos os campos como `public static final`.

### Constantes ZoneId (8 campos)

```java
public static final ZoneId ZONE_SAO_PAULO   = ZoneId.of("America/Sao_Paulo");
public static final ZoneId ZONE_UTC         = ZoneOffset.UTC.normalized();
public static final ZoneId ZONE_MANAUS      = ZoneId.of("America/Manaus");
public static final ZoneId ZONE_FORTALEZA   = ZoneId.of("America/Fortaleza");
public static final ZoneId ZONE_RIO_BRANCO  = ZoneId.of("America/Rio_Branco");
public static final ZoneId ZONE_NORONHA     = ZoneId.of("America/Noronha");
public static final ZoneId ZONE_CUIABA      = ZoneId.of("America/Cuiaba");
public static final ZoneId ZONE_CAMPO_GRANDE = ZoneId.of("America/Campo_Grande");
```

### Constantes ZoneOffset (4 campos)

```java
public static final ZoneOffset OFFSET_BRT = ZoneOffset.of("-03:00");
public static final ZoneOffset OFFSET_AMT = ZoneOffset.of("-04:00");
public static final ZoneOffset OFFSET_ACT = ZoneOffset.of("-05:00");
public static final ZoneOffset OFFSET_UTC = ZoneOffset.UTC;
```

### Constantes TimeZone (2 campos, para interop com java.util legado)

```java
public static final TimeZone TZ_SAO_PAULO = TimeZone.getTimeZone(ZONE_SAO_PAULO);
public static final TimeZone TZ_UTC       = TimeZone.getTimeZone("UTC");
```

### Constantes DateTimeFormatter (8 campos — todos thread-safe)

```java
/** Formato compacto de competência: yyyyMM (ex: "202506"). */
public static final DateTimeFormatter FORMATTER_YYYYMM =
        DateTimeFormatter.ofPattern("yyyyMM");

/** Formato XML de competência: yyyy-MM (ex: "2025-06"). */
public static final DateTimeFormatter FORMATTER_YYYY_MM =
        DateTimeFormatter.ofPattern("yyyy-MM");

/** Formato ISO de data: yyyy-MM-dd (ex: "2025-06-01"). */
public static final DateTimeFormatter FORMATTER_ISO_DATE =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");

/** Formato BR de data: dd/MM/yyyy (ex: "01/06/2025"). */
public static final DateTimeFormatter FORMATTER_BR_DATE =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

/** Formato ISO de data e hora: yyyy-MM-dd HH:mm:ss (ex: "2025-06-01 10:30:00"). */
public static final DateTimeFormatter FORMATTER_ISO_DATE_TIME =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

/** Formato BR de data e hora: dd/MM/yyyy HH:mm:ss (ex: "01/06/2025 10:30:00"). */
public static final DateTimeFormatter FORMATTER_BR_DATE_TIME =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

/** Formato timestamp compacto: yyyyMMddHHmmss (ex: "20250601103000"). */
public static final DateTimeFormatter FORMATTER_TIMESTAMP =
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

/** Formato ISO com offset: yyyy-MM-dd'T'HH:mm:ssXXX (ex: "2025-06-01T10:30:00-03:00"). */
public static final DateTimeFormatter FORMATTER_ISO_OFFSET =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
```

### Imports necessários

```java
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
```

---

## T002 — Criar `GovTimeConstantsTest.java`

**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovTimeConstantsTest.java`

### Instruções

1. Classe JUnit 4: `public class GovTimeConstantsTest`.
2. Use `Assert.*` (não `assertThat` do Hamcrest — apenas `assertEquals`, `assertNotNull`, `assertTrue`).
3. Sem Mockito — não há dependências externas.

### Testes obrigatórios

#### Validação das ZoneIds (não lançam exceção ao carregar regras)

```java
@Test
public void devemCarregarRegrasDeFuso() {
    // ZoneId.getRules() lança ZoneRulesException se a string for inválida
    assertNotNull(GovTimeConstants.ZONE_SAO_PAULO.getRules());
    assertNotNull(GovTimeConstants.ZONE_UTC.getRules());
    assertNotNull(GovTimeConstants.ZONE_MANAUS.getRules());
    assertNotNull(GovTimeConstants.ZONE_FORTALEZA.getRules());
    assertNotNull(GovTimeConstants.ZONE_RIO_BRANCO.getRules());
    assertNotNull(GovTimeConstants.ZONE_NORONHA.getRules());
    assertNotNull(GovTimeConstants.ZONE_CUIABA.getRules());
    assertNotNull(GovTimeConstants.ZONE_CAMPO_GRANDE.getRules());
}
```

#### Validação dos ZoneOffsets (valores corretos)

```java
@Test
public void offsetBrtDeveSer_menos3() {
    assertEquals(-3 * 3600, GovTimeConstants.OFFSET_BRT.getTotalSeconds());
}

@Test
public void offsetAmtDeveSer_menos4() {
    assertEquals(-4 * 3600, GovTimeConstants.OFFSET_AMT.getTotalSeconds());
}

@Test
public void offsetActDeveSer_menos5() {
    assertEquals(-5 * 3600, GovTimeConstants.OFFSET_ACT.getTotalSeconds());
}

@Test
public void offsetUtcDeveSer_zero() {
    assertEquals(0, GovTimeConstants.OFFSET_UTC.getTotalSeconds());
}
```

#### Validação dos TimeZones

```java
@Test
public void tzSaoPauloDeveConterAmerica_SaoPaulo() {
    assertTrue(GovTimeConstants.TZ_SAO_PAULO.getID().contains("Paulo"));
}

@Test
public void tzUtcDeveSerUtc() {
    assertEquals("UTC", GovTimeConstants.TZ_UTC.getID());
}
```

#### Roundtrip de cada formatter (format → parse → format == original)

```java
@Test
public void formatterYyyymmRoundtrip() {
    String original = "202506";
    String resultado = java.time.YearMonth.parse(original, GovTimeConstants.FORMATTER_YYYYMM)
            .format(GovTimeConstants.FORMATTER_YYYYMM);
    assertEquals(original, resultado);
}

@Test
public void formatterIsoDateRoundtrip() {
    String original = "2025-06-01";
    String resultado = java.time.LocalDate.parse(original, GovTimeConstants.FORMATTER_ISO_DATE)
            .format(GovTimeConstants.FORMATTER_ISO_DATE);
    assertEquals(original, resultado);
}

@Test
public void formatterBrDateRoundtrip() {
    String original = "01/06/2025";
    String resultado = java.time.LocalDate.parse(original, GovTimeConstants.FORMATTER_BR_DATE)
            .format(GovTimeConstants.FORMATTER_BR_DATE);
    assertEquals(original, resultado);
}

@Test
public void formatterIsoDateTimeRoundtrip() {
    String original = "2025-06-01 10:30:00";
    String resultado = java.time.LocalDateTime.parse(original, GovTimeConstants.FORMATTER_ISO_DATE_TIME)
            .format(GovTimeConstants.FORMATTER_ISO_DATE_TIME);
    assertEquals(original, resultado);
}

@Test
public void formatterTimestampRoundtrip() {
    String original = "20250601103000";
    String resultado = java.time.LocalDateTime.parse(original, GovTimeConstants.FORMATTER_TIMESTAMP)
            .format(GovTimeConstants.FORMATTER_TIMESTAMP);
    assertEquals(original, resultado);
}

@Test
public void formatterIsoOffsetRoundtrip() {
    String original = "2025-06-01T10:30:00-03:00";
    String resultado = java.time.OffsetDateTime.parse(original, GovTimeConstants.FORMATTER_ISO_OFFSET)
            .format(GovTimeConstants.FORMATTER_ISO_OFFSET);
    assertEquals(original, resultado);
}
```

---

## T003 — Criar `JavaTimeConversions.java`

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/JavaTimeConversions.java`

### Instruções

1. Classe `public final class JavaTimeConversions` com construtor `private JavaTimeConversions()`.
2. Todos os métodos são `public static`.
3. Null-safe: todos os métodos retornam `null` se o argumento principal for `null`.
4. Javadoc e comentários em português.
5. Java 8 apenas — sem `var`, sem `List.of()`, sem nada pós-Java 8.

### Imports necessários

```java
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
```

### Grupo 1: LocalDate ↔ LocalDateTime

```java
/** Retorna o início do dia para a data fornecida (00:00:00). */
public static LocalDateTime toStartOfDay(LocalDate date) {
    return date == null ? null : date.atStartOfDay();
}

/** Retorna o fim do dia para a data fornecida (23:59:59.999999999). */
public static LocalDateTime toEndOfDay(LocalDate date) {
    return date == null ? null : date.atTime(LocalTime.MAX);
}

/** Extrai a data de um LocalDateTime. */
public static LocalDate toLocalDate(LocalDateTime dateTime) {
    return dateTime == null ? null : dateTime.toLocalDate();
}
```

### Grupo 2: YearMonth ↔ LocalDate

```java
/** Retorna o primeiro dia do mês da competência fornecida. */
public static LocalDate toFirstDayOfMonth(YearMonth yearMonth) {
    return yearMonth == null ? null : yearMonth.atDay(1);
}

/** Retorna o último dia do mês da competência fornecida. */
public static LocalDate toLastDayOfMonth(YearMonth yearMonth) {
    return yearMonth == null ? null : yearMonth.atEndOfMonth();
}

/** Converte uma data para a competência correspondente. */
public static YearMonth toYearMonth(LocalDate date) {
    return date == null ? null : YearMonth.of(date.getYear(), date.getMonth());
}
```

### Grupo 3: Instant ↔ LocalDate / LocalDateTime

```java
/** Converte Instant para LocalDate no fuso fornecido. */
public static LocalDate toLocalDate(Instant instant, ZoneId zone) {
    if (instant == null || zone == null) return null;
    return instant.atZone(zone).toLocalDate();
}

/** Converte Instant para LocalDateTime no fuso padrão da JVM. */
public static LocalDateTime toLocalDateTime(Instant instant) {
    return instant == null ? null : LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
}

/** Converte Instant para LocalDateTime no fuso fornecido. */
public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zone) {
    if (instant == null || zone == null) return null;
    return LocalDateTime.ofInstant(instant, zone);
}

/** Converte LocalDateTime para Instant no fuso fornecido. */
public static Instant toInstant(LocalDateTime dateTime, ZoneId zone) {
    if (dateTime == null || zone == null) return null;
    return dateTime.atZone(zone).toInstant();
}

/** Converte LocalDate para Instant no início do dia no fuso fornecido. */
public static Instant toInstant(LocalDate date, ZoneId zone) {
    if (date == null || zone == null) return null;
    return date.atStartOfDay(zone).toInstant();
}
```

### Grupo 4: ZonedDateTime / OffsetDateTime ↔ LocalDateTime

```java
/** Extrai o LocalDateTime de um ZonedDateTime. */
public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? null : zonedDateTime.toLocalDateTime();
}

/** Cria um ZonedDateTime a partir de LocalDateTime no fuso fornecido. */
public static ZonedDateTime toZonedDateTime(LocalDateTime dateTime, ZoneId zone) {
    if (dateTime == null || zone == null) return null;
    return dateTime.atZone(zone);
}

/** Extrai o LocalDateTime de um OffsetDateTime. */
public static LocalDateTime toLocalDateTime(OffsetDateTime offsetDateTime) {
    return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
}

/** Cria um OffsetDateTime a partir de LocalDateTime com o offset fornecido. */
public static OffsetDateTime toOffsetDateTime(LocalDateTime dateTime, ZoneOffset offset) {
    if (dateTime == null || offset == null) return null;
    return dateTime.atOffset(offset);
}

/** Converte ZonedDateTime para OffsetDateTime. */
public static OffsetDateTime toOffsetDateTime(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? null : zonedDateTime.toOffsetDateTime();
}
```

### Grupo 5: Duration — criação

```java
/** Cria Duration de dias. */
public static Duration durationOfDays(long days) {
    return Duration.ofDays(days);
}

/** Cria Duration de horas. */
public static Duration durationOfHours(long hours) {
    return Duration.ofHours(hours);
}

/** Cria Duration de minutos. */
public static Duration durationOfMinutes(long minutes) {
    return Duration.ofMinutes(minutes);
}

/** Cria Duration de segundos. */
public static Duration durationOfSeconds(long seconds) {
    return Duration.ofSeconds(seconds);
}

/** Cria Duration de milissegundos. */
public static Duration durationOfMillis(long millis) {
    return Duration.ofMillis(millis);
}
```

### Grupo 6: Duration — between

```java
/** Calcula a duração entre duas datas (início do dia). */
public static Duration between(LocalDate start, LocalDate end) {
    if (start == null || end == null) return null;
    return Duration.between(start.atStartOfDay(), end.atStartOfDay());
}

/** Calcula a duração entre dois LocalDateTime. */
public static Duration between(LocalDateTime start, LocalDateTime end) {
    if (start == null || end == null) return null;
    return Duration.between(start, end);
}

/** Calcula a duração entre dois Instant. */
public static Duration between(Instant start, Instant end) {
    if (start == null || end == null) return null;
    return Duration.between(start, end);
}
```

### Grupo 7: Duration — extração

```java
/** Retorna os dias completos de uma Duration. */
public static long toDays(Duration duration) {
    return duration == null ? 0L : duration.toDays();
}

/** Retorna as horas completas de uma Duration. */
public static long toHours(Duration duration) {
    return duration == null ? 0L : duration.toHours();
}

/** Retorna os minutos completos de uma Duration. */
public static long toMinutes(Duration duration) {
    return duration == null ? 0L : duration.toMinutes();
}

/** Retorna os segundos completos de uma Duration. */
public static long toSeconds(Duration duration) {
    return duration == null ? 0L : duration.getSeconds();
}

/** Retorna os milissegundos totais de uma Duration. */
public static long toMillis(Duration duration) {
    return duration == null ? 0L : duration.toMillis();
}
```

---

## T004 — Criar `JavaTimeConversionsTest.java`

**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/JavaTimeConversionsTest.java`

### Instruções

1. JUnit 4, `Assert.*` estático importado.
2. Testar null-safety, roundtrips, edge cases.

### Testes obrigatórios

#### Null-safety

```java
@Test
public void toStartOfDay_null_retornaNull() {
    assertNull(JavaTimeConversions.toStartOfDay(null));
}

@Test
public void toEndOfDay_null_retornaNull() {
    assertNull(JavaTimeConversions.toEndOfDay(null));
}

@Test
public void toLocalDate_LocalDateTime_null_retornaNull() {
    assertNull(JavaTimeConversions.toLocalDate((LocalDateTime) null));
}

@Test
public void toFirstDayOfMonth_null_retornaNull() {
    assertNull(JavaTimeConversions.toFirstDayOfMonth(null));
}

@Test
public void toLastDayOfMonth_null_retornaNull() {
    assertNull(JavaTimeConversions.toLastDayOfMonth(null));
}

@Test
public void between_LocalDate_null_retornaNull() {
    assertNull(JavaTimeConversions.between((LocalDate) null, LocalDate.now()));
    assertNull(JavaTimeConversions.between(LocalDate.now(), (LocalDate) null));
}
```

#### LocalDate ↔ LocalDateTime

```java
@Test
public void toStartOfDay_retornaMeiaNoite() {
    LocalDateTime result = JavaTimeConversions.toStartOfDay(LocalDate.of(2025, 6, 1));
    assertEquals(LocalDateTime.of(2025, 6, 1, 0, 0, 0), result);
}

@Test
public void toEndOfDay_retornaLocalTimeMax() {
    LocalDateTime result = JavaTimeConversions.toEndOfDay(LocalDate.of(2025, 6, 1));
    assertEquals(LocalTime.MAX, result.toLocalTime());
}

@Test
public void toEndOfDay_temPrecisaoDeNanosegundos() {
    LocalDateTime result = JavaTimeConversions.toEndOfDay(LocalDate.of(2025, 6, 1));
    assertEquals(23, result.getHour());
    assertEquals(59, result.getMinute());
    assertEquals(59, result.getSecond());
    assertEquals(999_999_999, result.getNano());
}
```

#### Ano bissexto 2024-02-29

```java
@Test
public void toLastDayOfMonth_fevBissexto2024() {
    LocalDate result = JavaTimeConversions.toLastDayOfMonth(YearMonth.of(2024, 2));
    assertEquals(LocalDate.of(2024, 2, 29), result);
}

@Test
public void toEndOfDay_bissexto2024() {
    LocalDateTime result = JavaTimeConversions.toEndOfDay(LocalDate.of(2024, 2, 29));
    assertNotNull(result);
    assertEquals(29, result.getDayOfMonth());
}
```

#### YearMonth roundtrips

```java
@Test
public void toYearMonth_roundtrip() {
    LocalDate date = LocalDate.of(2025, 6, 15);
    YearMonth ym = JavaTimeConversions.toYearMonth(date);
    assertEquals(YearMonth.of(2025, 6), ym);
}

@Test
public void toFirstDayOfMonth_junho() {
    assertEquals(LocalDate.of(2025, 6, 1),
            JavaTimeConversions.toFirstDayOfMonth(YearMonth.of(2025, 6)));
}
```

#### Duration — criação e extração

```java
@Test
public void durationOfDays_2() {
    assertEquals(2L, JavaTimeConversions.toDays(JavaTimeConversions.durationOfDays(2)));
}

@Test
public void durationOfHours_negativo() {
    Duration d = JavaTimeConversions.durationOfHours(-1);
    assertEquals(-1L, JavaTimeConversions.toHours(d));
}

@Test
public void between_LocalDate_roundtrip() {
    LocalDate d1 = LocalDate.of(2025, 6, 1);
    LocalDate d2 = LocalDate.of(2025, 6, 8);
    Duration duration = JavaTimeConversions.between(d1, d2);
    assertEquals(7L, JavaTimeConversions.toDays(duration));
}
```

#### Instant conversions

```java
@Test
public void toLocalDate_Instant_UTC_epoch() {
    LocalDate result = JavaTimeConversions.toLocalDate(Instant.EPOCH, ZoneOffset.UTC);
    assertEquals(LocalDate.of(1970, 1, 1), result);
}

@Test
public void toInstant_toLocalDateTime_roundtrip() {
    LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 0, 0);
    Instant instant = JavaTimeConversions.toInstant(ldt, ZoneOffset.UTC);
    LocalDateTime result = JavaTimeConversions.toLocalDateTime(instant, ZoneOffset.UTC);
    assertEquals(ldt, result);
}
```

---

## Checklist de Validação

- [ ] `GovTimeConstants` tem exatamente 8 constantes ZoneId
- [ ] `GovTimeConstants` tem exatamente 4 constantes ZoneOffset
- [ ] `GovTimeConstants` tem exatamente 2 constantes TimeZone
- [ ] `GovTimeConstants` tem exatamente 8 constantes DateTimeFormatter
- [ ] `JavaTimeConversions.toEndOfDay` retorna `LocalTime.MAX` (23:59:59.999999999)
- [ ] Todos os métodos retornam `null` para entrada `null`
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-domain` passa sem erros
- [ ] JaCoCo 90% linha e 90% branch para o módulo `domain`

## Activity Log

- 2026-04-24T13:54:23Z – unknown – lane=doing – Moved to doing
- 2026-04-24T13:57:20Z – unknown – lane=for_review – Moved to for_review
- 2026-04-24T13:57:26Z – unknown – lane=done – Moved to done
- 2026-04-24T14:05:32Z – unknown – lane=doing – Moved to doing
