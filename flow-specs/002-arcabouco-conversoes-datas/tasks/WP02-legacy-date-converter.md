---
work_package_id: WP02
title: LegacyDateConverter (domain)
lane: planned
dependencies: []
created_at: '2026-04-24T13:51:14.658200+00:00'
subtasks:
- T005
- T006
- T007
---

# WP02 — LegacyDateConverter (domain)

## Objetivo

Criar `LegacyDateConverter.java` no módulo `declaracoes-gov-core-domain`, cobrindo todas as conversões bidirecionais entre `java.util.Date`/`java.util.Calendar` e os tipos modernos `java.time`.

**Pré-requisito:** WP01 deve estar completo — `GovTimeConstants` é usado nos testes de zona explícita.

---

## T005 — `LegacyDateConverter.java` (Parte 1: java.util.Date ↔ java.time)

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/LegacyDateConverter.java`

### Instruções gerais

1. `public final class LegacyDateConverter` com `private LegacyDateConverter()`.
2. Javadoc de classe em português: `"Conversores entre tipos legados (java.util.Date, java.util.Calendar) e java.time."`.
3. Todos os métodos são `public static`.
4. Null-safe: retorna `null` para entrada `null` (incluindo o `ZoneId`/`ZoneOffset` em sobrecargas onde ambos são necessários).
5. Java 8 apenas — usar `date.toInstant()` para `Date → Instant`.

### Imports necessários

```java
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
```

### Grupo A: java.util.Date → java.time

```java
/**
 * Converte Date para Instant.
 * Retorna null se date for null.
 */
public static Instant toInstant(Date date) {
    return date == null ? null : date.toInstant();
}

/**
 * Converte Date para LocalDate usando o fuso padrão da JVM.
 */
public static LocalDate toLocalDate(Date date) {
    return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
}

/**
 * Converte Date para LocalDate usando o ZoneId fornecido.
 */
public static LocalDate toLocalDate(Date date, ZoneId zone) {
    if (date == null || zone == null) return null;
    return date.toInstant().atZone(zone).toLocalDate();
}

/**
 * Converte Date para LocalDateTime usando o fuso padrão da JVM.
 */
public static LocalDateTime toLocalDateTime(Date date) {
    return date == null ? null :
            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
}

/**
 * Converte Date para LocalDateTime usando o ZoneId fornecido.
 */
public static LocalDateTime toLocalDateTime(Date date, ZoneId zone) {
    if (date == null || zone == null) return null;
    return date.toInstant().atZone(zone).toLocalDateTime();
}

/**
 * Converte Date para ZonedDateTime usando o ZoneId fornecido.
 */
public static ZonedDateTime toZonedDateTime(Date date, ZoneId zone) {
    if (date == null || zone == null) return null;
    return date.toInstant().atZone(zone);
}

/**
 * Converte Date para OffsetDateTime usando o ZoneOffset fornecido.
 */
public static OffsetDateTime toOffsetDateTime(Date date, ZoneOffset offset) {
    if (date == null || offset == null) return null;
    return date.toInstant().atOffset(offset);
}
```

### Grupo B: java.time → java.util.Date

```java
/**
 * Converte Instant para Date.
 */
public static Date toDate(Instant instant) {
    return instant == null ? null : Date.from(instant);
}

/**
 * Converte LocalDate para Date no início do dia usando fuso padrão da JVM.
 */
public static Date toDate(LocalDate date) {
    return date == null ? null :
            Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
}

/**
 * Converte LocalDate para Date no início do dia usando o ZoneId fornecido.
 */
public static Date toDate(LocalDate date, ZoneId zone) {
    if (date == null || zone == null) return null;
    return Date.from(date.atStartOfDay(zone).toInstant());
}

/**
 * Converte LocalDateTime para Date usando fuso padrão da JVM.
 */
public static Date toDate(LocalDateTime dateTime) {
    return dateTime == null ? null :
            Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
}

/**
 * Converte LocalDateTime para Date usando o ZoneId fornecido.
 */
public static Date toDate(LocalDateTime dateTime, ZoneId zone) {
    if (dateTime == null || zone == null) return null;
    return Date.from(dateTime.atZone(zone).toInstant());
}

/**
 * Converte ZonedDateTime para Date.
 */
public static Date toDate(ZonedDateTime zonedDateTime) {
    return zonedDateTime == null ? null : Date.from(zonedDateTime.toInstant());
}

/**
 * Converte OffsetDateTime para Date.
 */
public static Date toDate(OffsetDateTime offsetDateTime) {
    return offsetDateTime == null ? null : Date.from(offsetDateTime.toInstant());
}
```

---

## T006 — `LegacyDateConverter.java` (Parte 2: java.util.Calendar ↔ java.time)

**Arquivo:** o mesmo arquivo criado em T005 — adicionar os métodos Calendar ao corpo da classe.

### Grupo C: java.util.Calendar → java.time

```java
/**
 * Converte Calendar para Instant.
 */
public static Instant toInstant(Calendar calendar) {
    return calendar == null ? null : calendar.toInstant();
}

/**
 * Converte Calendar para LocalDate usando o fuso do próprio Calendar.
 */
public static LocalDate toLocalDate(Calendar calendar) {
    if (calendar == null) return null;
    return calendar.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();
}

/**
 * Converte Calendar para LocalDateTime usando o fuso do próprio Calendar.
 */
public static LocalDateTime toLocalDateTime(Calendar calendar) {
    if (calendar == null) return null;
    return calendar.toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDateTime();
}

/**
 * Converte Calendar para ZonedDateTime preservando o fuso do Calendar.
 */
public static ZonedDateTime toZonedDateTime(Calendar calendar) {
    if (calendar == null) return null;
    return calendar.toInstant().atZone(calendar.getTimeZone().toZoneId());
}
```

### Grupo D: java.time → java.util.Calendar

```java
/**
 * Converte ZonedDateTime para Calendar, preservando o fuso horário original.
 */
public static Calendar toCalendar(ZonedDateTime zonedDateTime) {
    if (zonedDateTime == null) return null;
    Calendar cal = Calendar.getInstance(
            TimeZone.getTimeZone(zonedDateTime.getZone()));
    cal.setTimeInMillis(zonedDateTime.toInstant().toEpochMilli());
    return cal;
}

/**
 * Converte LocalDateTime para Calendar usando fuso padrão da JVM.
 */
public static Calendar toCalendar(LocalDateTime dateTime) {
    return dateTime == null ? null : toCalendar(dateTime, ZoneId.systemDefault());
}

/**
 * Converte LocalDateTime para Calendar usando o ZoneId fornecido.
 */
public static Calendar toCalendar(LocalDateTime dateTime, ZoneId zone) {
    if (dateTime == null || zone == null) return null;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zone));
    cal.setTimeInMillis(dateTime.atZone(zone).toInstant().toEpochMilli());
    return cal;
}

/**
 * Converte LocalDate para Calendar no início do dia usando fuso padrão da JVM.
 */
public static Calendar toCalendar(LocalDate date) {
    return date == null ? null : toCalendar(date, ZoneId.systemDefault());
}

/**
 * Converte LocalDate para Calendar no início do dia usando o ZoneId fornecido.
 */
public static Calendar toCalendar(LocalDate date, ZoneId zone) {
    if (date == null || zone == null) return null;
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(zone));
    cal.setTimeInMillis(date.atStartOfDay(zone).toInstant().toEpochMilli());
    return cal;
}
```

---

## T007 — Criar `LegacyDateConverterTest.java`

**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/LegacyDateConverterTest.java`

### Instruções

1. JUnit 4, `Assert.*`.
2. Use `GovTimeConstants.ZONE_SAO_PAULO`, `GovTimeConstants.ZONE_UTC`, `GovTimeConstants.OFFSET_BRT`, `GovTimeConstants.OFFSET_UTC` das constantes do WP01.
3. Testar: null-safety, roundtrips, epoch, Y2038, fuso America/Sao_Paulo.

### Testes obrigatórios

#### Null-safety para Date

```java
@Test
public void toLocalDate_Date_null_retornaNull() {
    assertNull(LegacyDateConverter.toLocalDate((Date) null));
}

@Test
public void toLocalDateTime_Date_null_retornaNull() {
    assertNull(LegacyDateConverter.toLocalDateTime((Date) null));
}

@Test
public void toDate_LocalDate_null_retornaNull() {
    assertNull(LegacyDateConverter.toDate((LocalDate) null));
}

@Test
public void toDate_LocalDateTime_null_retornaNull() {
    assertNull(LegacyDateConverter.toDate((LocalDateTime) null));
}

@Test
public void toDate_Instant_null_retornaNull() {
    assertNull(LegacyDateConverter.toDate((Instant) null));
}
```

#### Null-safety para Calendar

```java
@Test
public void toLocalDate_Calendar_null_retornaNull() {
    assertNull(LegacyDateConverter.toLocalDate((Calendar) null));
}

@Test
public void toZonedDateTime_Calendar_null_retornaNull() {
    assertNull(LegacyDateConverter.toZonedDateTime((Calendar) null));
}

@Test
public void toCalendar_ZonedDateTime_null_retornaNull() {
    assertNull(LegacyDateConverter.toCalendar((ZonedDateTime) null));
}
```

#### Epoch: Date(0) = 1970-01-01 em UTC

```java
@Test
public void toLocalDate_epoch_UTC() {
    Date epoch = new Date(0L);
    LocalDate result = LegacyDateConverter.toLocalDate(epoch, ZoneOffset.UTC);
    assertEquals(LocalDate.of(1970, 1, 1), result);
}

@Test
public void toDate_Instant_EPOCH_roundtrip() {
    Date result = LegacyDateConverter.toDate(Instant.EPOCH);
    assertEquals(0L, result.getTime());
}
```

#### Roundtrip Date → LocalDate → Date (UTC)

```java
@Test
public void roundtrip_Date_LocalDate_UTC() {
    LocalDate date = LocalDate.of(2025, 6, 1);
    Date asDate = LegacyDateConverter.toDate(date, GovTimeConstants.ZONE_UTC);
    LocalDate result = LegacyDateConverter.toLocalDate(asDate, GovTimeConstants.ZONE_UTC);
    assertEquals(date, result);
}
```

#### Roundtrip Date → LocalDate → Date (America/Sao_Paulo)

```java
@Test
public void roundtrip_Date_LocalDate_SaoPaulo() {
    LocalDate date = LocalDate.of(2025, 6, 1);
    Date asDate = LegacyDateConverter.toDate(date, GovTimeConstants.ZONE_SAO_PAULO);
    LocalDate result = LegacyDateConverter.toLocalDate(asDate, GovTimeConstants.ZONE_SAO_PAULO);
    assertEquals(date, result);
}
```

#### Y2038: 2038-01-19 (não deve lançar exceção)

```java
@Test
public void toLocalDate_Y2038_naoLancaExcecao() {
    // 2038-01-19T03:14:07Z é o limite do int de 32 bits Unix timestamp
    Instant y2038 = Instant.parse("2038-01-19T03:14:07Z");
    Date date = Date.from(y2038);
    LocalDate result = LegacyDateConverter.toLocalDate(date, ZoneOffset.UTC);
    assertEquals(LocalDate.of(2038, 1, 19), result);
}
```

#### Calendar roundtrip

```java
@Test
public void roundtrip_Calendar_ZonedDateTime_Calendar() {
    ZonedDateTime zdt = ZonedDateTime.of(
            LocalDateTime.of(2025, 6, 1, 10, 30, 0),
            GovTimeConstants.ZONE_SAO_PAULO);
    Calendar cal = LegacyDateConverter.toCalendar(zdt);
    ZonedDateTime result = LegacyDateConverter.toZonedDateTime(cal);
    // Comparar os instants, não os objetos (timezone ID pode diferir)
    assertEquals(zdt.toInstant(), result.toInstant());
}

@Test
public void toCalendar_ZonedDateTime_preservaFuso() {
    ZonedDateTime zdt = ZonedDateTime.of(
            LocalDateTime.of(2025, 6, 1, 10, 0, 0),
            GovTimeConstants.ZONE_SAO_PAULO);
    Calendar cal = LegacyDateConverter.toCalendar(zdt);
    // O fuso do Calendar deve conter o offset -03:00 de São Paulo
    assertTrue(cal.getTimeZone().getID().contains("Paulo") ||
               cal.getTimeZone().getRawOffset() == -3 * 3600 * 1000);
}
```

---

## Checklist de Validação

- [ ] `toDate(null)` retorna `null` para todas as sobrecargas
- [ ] `toLocalDate(null)` retorna `null` para todas as sobrecargas (Date e Calendar)
- [ ] `new Date(0L)` com UTC = `LocalDate.of(1970, 1, 1)`
- [ ] ZonedDateTime timezone preservado em `toCalendar`
- [ ] Y2038 (2038-01-19) não lança exceção
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-domain` passa sem erros
- [ ] JaCoCo 90% linha e 90% branch no módulo `domain`
