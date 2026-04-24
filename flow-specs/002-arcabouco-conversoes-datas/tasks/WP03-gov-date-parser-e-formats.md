---
work_package_id: WP03
title: GovDateParser + GovDateFormats (domain + format)
lane: planned
dependencies: []
created_at: '2026-04-24T13:51:14.675181+00:00'
subtasks:
- T008
- T009
- T010
- T011
- T012
---

# WP03 — GovDateParser + GovDateFormats (domain + format)

## Objetivo

Criar:
1. `GovDateParser.java` no módulo `declaracoes-gov-core-domain` — parsing e formatação de strings de data usando os formatters de `GovTimeConstants`.
2. `GovDateFormats.java` no módulo `declaracoes-gov-core-format` — complemento de `XmlDates` e `GovCompetenceFormats` para formatos específicos do ecossistema gov.

**Pré-requisito:** WP01 deve estar completo — `GovTimeConstants` é dependência direta de `GovDateParser`.

**ATENÇÃO CRÍTICA:** `GovDateParser` está no módulo `domain` — NÃO pode importar nem depender de `GovCompetenceFormats` (que está em `format`). A lógica de `parseYearMonth` deve ser reimplementada de forma independente usando `DateTimeFormatter`.

---

## T008 — `GovDateParser.java` (Parte 1: métodos de parsing)

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateParser.java`

### Instruções gerais

1. `public final class GovDateParser` com `private GovDateParser()`.
2. Javadoc de classe em português: `"Parser e formatador de strings de data para o ecossistema declaracoes-*, usando constantes de GovTimeConstants."`.
3. Todos os métodos são `public static`.
4. Null-safe: retorna `null` para entrada `null` ou string vazia (após `.trim()`).
5. Strings inválidas (não-nulas, não-vazias) lançam `DateTimeParseException` sem serem capturadas.
6. Java 8 apenas.

### Imports necessários

```java
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
```

### Helper privado (evitar repetição)

```java
private static boolean isNullOrEmpty(String value) {
    return value == null || value.trim().isEmpty();
}
```

### Métodos de parsing

```java
/**
 * Converte string ISO (yyyy-MM-dd) para LocalDate.
 * Retorna null para entrada nula ou vazia. Lança DateTimeParseException se inválida.
 */
public static LocalDate parseLocalDate(String value) {
    if (isNullOrEmpty(value)) return null;
    return LocalDate.parse(value.trim(), GovTimeConstants.FORMATTER_ISO_DATE);
}

/**
 * Converte string para LocalDate usando o formatter fornecido.
 */
public static LocalDate parseLocalDate(String value, DateTimeFormatter formatter) {
    if (isNullOrEmpty(value) || formatter == null) return null;
    return LocalDate.parse(value.trim(), formatter);
}

/**
 * Converte string no formato BR (dd/MM/yyyy) para LocalDate.
 */
public static LocalDate parseBrDate(String value) {
    if (isNullOrEmpty(value)) return null;
    return LocalDate.parse(value.trim(), GovTimeConstants.FORMATTER_BR_DATE);
}

/**
 * Converte string ISO de data e hora (yyyy-MM-dd HH:mm:ss) para LocalDateTime.
 */
public static LocalDateTime parseLocalDateTime(String value) {
    if (isNullOrEmpty(value)) return null;
    return LocalDateTime.parse(value.trim(), GovTimeConstants.FORMATTER_ISO_DATE_TIME);
}

/**
 * Converte string para LocalDateTime usando o formatter fornecido.
 */
public static LocalDateTime parseLocalDateTime(String value, DateTimeFormatter formatter) {
    if (isNullOrEmpty(value) || formatter == null) return null;
    return LocalDateTime.parse(value.trim(), formatter);
}

/**
 * Converte string para ZonedDateTime usando o formatter e ZoneId fornecidos.
 */
public static ZonedDateTime parseZonedDateTime(String value, DateTimeFormatter formatter, ZoneId zone) {
    if (isNullOrEmpty(value) || formatter == null || zone == null) return null;
    return ZonedDateTime.parse(value.trim(), formatter.withZone(zone));
}

/**
 * Converte string ISO com offset (yyyy-MM-dd'T'HH:mm:ssXXX) para OffsetDateTime.
 */
public static OffsetDateTime parseOffsetDateTime(String value) {
    if (isNullOrEmpty(value)) return null;
    return OffsetDateTime.parse(value.trim(), GovTimeConstants.FORMATTER_ISO_OFFSET);
}

/**
 * Converte string ISO-8601 para Instant (ex: "2025-06-01T10:30:00Z").
 */
public static Instant parseInstant(String value) {
    if (isNullOrEmpty(value)) return null;
    return Instant.parse(value.trim());
}

/**
 * Converte string ISO-8601 de duração para Duration (ex: "PT2H30M").
 */
public static Duration parseDuration(String value) {
    if (isNullOrEmpty(value)) return null;
    return Duration.parse(value.trim());
}

/**
 * Converte string de competência para YearMonth.
 * Aceita os formatos "yyyy-MM" (ex: "2025-06") e "yyyyMM" (ex: "202506").
 * Reimplementado de forma independente — domain não pode depender de format.
 */
public static YearMonth parseYearMonth(String value) {
    if (isNullOrEmpty(value)) return null;
    String trimmed = value.trim();
    if (trimmed.contains("-")) {
        return YearMonth.parse(trimmed, GovTimeConstants.FORMATTER_YYYY_MM);
    }
    return YearMonth.parse(trimmed, GovTimeConstants.FORMATTER_YYYYMM);
}
```

---

## T009 — `GovDateParser.java` (Parte 2: métodos de formatação)

**Arquivo:** o mesmo arquivo de T008 — adicionar os métodos de formatação ao corpo da classe.

### Métodos de formatação

Todos retornam `null` para entrada `null`.

```java
/**
 * Formata LocalDate com o formatter fornecido.
 */
public static String format(LocalDate date, DateTimeFormatter formatter) {
    if (date == null || formatter == null) return null;
    return date.format(formatter);
}

/**
 * Formata LocalDate no padrão BR: dd/MM/yyyy.
 */
public static String formatBrDate(LocalDate date) {
    return date == null ? null : date.format(GovTimeConstants.FORMATTER_BR_DATE);
}

/**
 * Formata LocalDate no padrão ISO: yyyy-MM-dd.
 */
public static String formatIsoDate(LocalDate date) {
    return date == null ? null : date.format(GovTimeConstants.FORMATTER_ISO_DATE);
}

/**
 * Formata LocalDateTime com o formatter fornecido.
 */
public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
    if (dateTime == null || formatter == null) return null;
    return dateTime.format(formatter);
}

/**
 * Formata LocalDateTime no padrão ISO: yyyy-MM-dd HH:mm:ss.
 */
public static String formatIsoDateTime(LocalDateTime dateTime) {
    return dateTime == null ? null : dateTime.format(GovTimeConstants.FORMATTER_ISO_DATE_TIME);
}

/**
 * Formata LocalDateTime no padrão BR: dd/MM/yyyy HH:mm:ss.
 */
public static String formatBrDateTime(LocalDateTime dateTime) {
    return dateTime == null ? null : dateTime.format(GovTimeConstants.FORMATTER_BR_DATE_TIME);
}

/**
 * Formata LocalDateTime como timestamp compacto: yyyyMMddHHmmss.
 */
public static String formatTimestamp(LocalDateTime dateTime) {
    return dateTime == null ? null : dateTime.format(GovTimeConstants.FORMATTER_TIMESTAMP);
}

/**
 * Formata ZonedDateTime com o formatter fornecido.
 */
public static String format(ZonedDateTime zonedDateTime, DateTimeFormatter formatter) {
    if (zonedDateTime == null || formatter == null) return null;
    return zonedDateTime.format(formatter);
}

/**
 * Formata ZonedDateTime no padrão ISO com offset: yyyy-MM-dd'T'HH:mm:ssXXX.
 */
public static String formatIsoOffset(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? null : zonedDateTime.format(GovTimeConstants.FORMATTER_ISO_OFFSET);
}

/**
 * Formata Duration no padrão ISO-8601 (ex: "PT2H30M").
 */
public static String formatDuration(Duration duration) {
    return duration == null ? null : duration.toString();
}
```

---

## T010 — Criar `GovDateParserTest.java`

**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateParserTest.java`

### Instruções

1. JUnit 4, `Assert.*`.
2. Para testes de exceção, usar `@Test(expected = DateTimeParseException.class)` — não capturar a exceção.

### Testes obrigatórios

#### Null-safety e string vazia

```java
@Test
public void parseLocalDate_null_retornaNull() {
    assertNull(GovDateParser.parseLocalDate(null));
}

@Test
public void parseLocalDate_vazio_retornaNull() {
    assertNull(GovDateParser.parseLocalDate(""));
    assertNull(GovDateParser.parseLocalDate("   "));
}

@Test
public void parseBrDate_null_retornaNull() {
    assertNull(GovDateParser.parseBrDate(null));
}

@Test
public void parseBrDate_vazio_retornaNull() {
    assertNull(GovDateParser.parseBrDate(""));
}

@Test
public void formatBrDate_null_retornaNull() {
    assertNull(GovDateParser.formatBrDate(null));
}

@Test
public void formatIsoDate_null_retornaNull() {
    assertNull(GovDateParser.formatIsoDate(null));
}

@Test
public void parseDuration_null_retornaNull() {
    assertNull(GovDateParser.parseDuration(null));
}

@Test
public void parseYearMonth_null_retornaNull() {
    assertNull(GovDateParser.parseYearMonth(null));
}

@Test
public void parseYearMonth_vazio_retornaNull() {
    assertNull(GovDateParser.parseYearMonth(""));
}
```

#### Parsing correto

```java
@Test
public void parseBrDate_converte_01_06_2025() {
    assertEquals(LocalDate.of(2025, 6, 1), GovDateParser.parseBrDate("01/06/2025"));
}

@Test
public void parseLocalDate_converte_ISO() {
    assertEquals(LocalDate.of(2025, 6, 1), GovDateParser.parseLocalDate("2025-06-01"));
}

@Test
public void parseLocalDateTime_converte_ISO() {
    LocalDateTime expected = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
    assertEquals(expected, GovDateParser.parseLocalDateTime("2025-06-01 10:30:00"));
}

@Test
public void parseOffsetDateTime_converte_comOffset() {
    OffsetDateTime result = GovDateParser.parseOffsetDateTime("2025-06-01T10:30:00-03:00");
    assertNotNull(result);
    assertEquals(LocalDate.of(2025, 6, 1), result.toLocalDate());
}

@Test
public void parseInstant_converte_Z() {
    Instant result = GovDateParser.parseInstant("2025-06-01T10:30:00Z");
    assertNotNull(result);
}

@Test
public void parseDuration_PT2H30M() {
    Duration result = GovDateParser.parseDuration("PT2H30M");
    assertEquals(150L, result.toMinutes());
}
```

#### parseYearMonth — ambos os formatos

```java
@Test
public void parseYearMonth_formatoCompacto_202506() {
    assertEquals(YearMonth.of(2025, 6), GovDateParser.parseYearMonth("202506"));
}

@Test
public void parseYearMonth_formatoISO_2025_06() {
    assertEquals(YearMonth.of(2025, 6), GovDateParser.parseYearMonth("2025-06"));
}

@Test
public void parseYearMonth_ambosFormatosRetornamMesmoValor() {
    assertEquals(
        GovDateParser.parseYearMonth("2025-06"),
        GovDateParser.parseYearMonth("202506")
    );
}
```

#### Formato inválido lança DateTimeParseException

```java
@Test(expected = DateTimeParseException.class)
public void parseBrDate_formatoInvalido_lancaExcecao() {
    GovDateParser.parseBrDate("99/99/9999");
}

@Test(expected = DateTimeParseException.class)
public void parseLocalDate_formatoInvalido_lancaExcecao() {
    GovDateParser.parseLocalDate("nao-e-uma-data");
}
```

#### Formatação

```java
@Test
public void formatBrDate_retorna_01_06_2025() {
    assertEquals("01/06/2025", GovDateParser.formatBrDate(LocalDate.of(2025, 6, 1)));
}

@Test
public void formatIsoDate_retorna_2025_06_01() {
    assertEquals("2025-06-01", GovDateParser.formatIsoDate(LocalDate.of(2025, 6, 1)));
}

@Test
public void formatIsoDateTime_retorna_formato_correto() {
    LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
    assertEquals("2025-06-01 10:30:00", GovDateParser.formatIsoDateTime(ldt));
}

@Test
public void formatTimestamp_retorna_14_digitos() {
    LocalDateTime ldt = LocalDateTime.of(2025, 6, 1, 10, 30, 0);
    assertEquals("20250601103000", GovDateParser.formatTimestamp(ldt));
}

@Test
public void formatDuration_PT2H() {
    assertEquals("PT2H", GovDateParser.formatDuration(Duration.ofHours(2)));
}
```

#### Roundtrip

```java
@Test
public void roundtrip_parseLocalDate_formatIsoDate() {
    String original = "2025-06-01";
    assertEquals(original, GovDateParser.formatIsoDate(GovDateParser.parseLocalDate(original)));
}

@Test
public void roundtrip_parseBrDate_formatBrDate() {
    String original = "01/06/2025";
    assertEquals(original, GovDateParser.formatBrDate(GovDateParser.parseBrDate(original)));
}
```

---

## T011 — Criar `GovDateFormats.java` (módulo format)

**Arquivo:** `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateFormats.java`

### Instruções

1. `public final class GovDateFormats` com `private GovDateFormats()`.
2. Javadoc em português: `"Complemento de XmlDates e GovCompetenceFormats para formatos XML fiscais do ecossistema declaracoes-*."`.
3. Módulo `format` — pode usar `GovCompetenceFormats` (que já está em `format`).
4. Usa `DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")` para XML datetime.
5. Null-safe.
6. NÃO modificar `XmlDates.java` nem `GovCompetenceFormats.java`.

### Imports necessários

```java
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
```

### Implementação

```java
/** Formatter para o padrão XML gov: yyyy-MM-dd'T'HH:mm:ssXXX. */
private static final DateTimeFormatter XML_DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

/**
 * Converte string no formato XML gov (yyyy-MM-dd'T'HH:mm:ssXXX) para ZonedDateTime.
 * Retorna null para entrada nula.
 */
public static ZonedDateTime parseXmlDateTime(String value) {
    if (value == null || value.trim().isEmpty()) return null;
    return ZonedDateTime.parse(value.trim(), XML_DATE_TIME_FORMATTER);
}

/**
 * Formata ZonedDateTime no padrão XML gov: yyyy-MM-dd'T'HH:mm:ssXXX.
 * Retorna null para entrada nula.
 */
public static String formatXmlDateTime(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? null : zonedDateTime.format(XML_DATE_TIME_FORMATTER);
}

/**
 * Converte string de competência para YearMonth, delegando a GovCompetenceFormats.
 * Aceita "yyyy-MM" ou "yyyyMM".
 */
public static YearMonth parseDataCompetencia(String value) {
    return GovCompetenceFormats.parse(value);
}

/**
 * Formata YearMonth no padrão XML (yyyy-MM), delegando a GovCompetenceFormats.
 * Retorna null para entrada nula.
 */
public static String formatDataCompetencia(YearMonth yearMonth) {
    return GovCompetenceFormats.toXmlFormat(yearMonth);
}
```

---

## T012 — Criar `GovDateFormatsTest.java`

**Arquivo:** `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateFormatsTest.java`

### Instruções

1. JUnit 4, `Assert.*`.
2. Módulo `format` — pode importar `GovDateFormats`.

### Testes obrigatórios

#### Null-safety

```java
@Test
public void parseXmlDateTime_null_retornaNull() {
    assertNull(GovDateFormats.parseXmlDateTime(null));
}

@Test
public void parseXmlDateTime_vazio_retornaNull() {
    assertNull(GovDateFormats.parseXmlDateTime(""));
}

@Test
public void formatXmlDateTime_null_retornaNull() {
    assertNull(GovDateFormats.formatXmlDateTime(null));
}

@Test
public void parseDataCompetencia_null_retornaNull() {
    assertNull(GovDateFormats.parseDataCompetencia(null));
}

@Test
public void formatDataCompetencia_null_retornaNull() {
    assertNull(GovDateFormats.formatDataCompetencia(null));
}
```

#### XML datetime roundtrip com offset -03:00

```java
@Test
public void roundtrip_xmlDateTime_comOffset() {
    String original = "2025-06-01T10:30:00-03:00";
    ZonedDateTime parsed = GovDateFormats.parseXmlDateTime(original);
    String resultado = GovDateFormats.formatXmlDateTime(parsed);
    assertEquals(original, resultado);
}

@Test
public void parseXmlDateTime_preservaOffset() {
    ZonedDateTime result = GovDateFormats.parseXmlDateTime("2025-06-01T10:30:00-03:00");
    assertEquals(-3 * 3600, result.getOffset().getTotalSeconds());
}
```

#### Competência roundtrip

```java
@Test
public void roundtrip_dataCompetencia() {
    YearMonth ym = YearMonth.of(2025, 6);
    String formatted = GovDateFormats.formatDataCompetencia(ym);
    YearMonth result = GovDateFormats.parseDataCompetencia(formatted);
    assertEquals(ym, result);
}

@Test
public void parseDataCompetencia_formatoCompacto() {
    assertEquals(YearMonth.of(2025, 6), GovDateFormats.parseDataCompetencia("202506"));
}
```

---

## Checklist de Validação

- [ ] `parseYearMonth("202506")` == `parseYearMonth("2025-06")` == `YearMonth.of(2025, 6)`
- [ ] `formatBrDate(null)` retorna `null`
- [ ] `parseBrDate("")` retorna `null`
- [ ] `parseBrDate("99/99/9999")` lança `DateTimeParseException` (não capturada)
- [ ] `GovDateFormats` NÃO importa nada de `domain` diretamente — usa apenas `java.time` e `GovCompetenceFormats`
- [ ] `GovDateParser` NÃO importa `GovCompetenceFormats` (está em `domain`, dependência proibida)
- [ ] `mvn -q verify` em `declaracoes-gov-core-domain` e `declaracoes-gov-core-format` passa
- [ ] JaCoCo 90% linha e 90% branch em ambos os módulos
