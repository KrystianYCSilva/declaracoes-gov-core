---
work_package_id: WP02
title: GovCurrencyFormats (format)
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T006
- T007
---

# WP02 — GovCurrencyFormats (format)

## Objetivo

Criar `GovCurrencyFormats.java` no módulo `declaracoes-gov-core-format` com formatação e parse de valores monetários e SPED centralizados. Depende de `GovNumberConstants` (WP01).

---

## T006 — Criar `GovCurrencyFormats.java`

**Arquivo:** `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovCurrencyFormats.java`

### Instruções

1. Classe `public final class GovCurrencyFormats` com construtor `private GovCurrencyFormats()`.
2. Javadoc: `"Formatação e parse de valores monetários para padrões BRL e SPED."`.
3. Todos os métodos `public static`.
4. Null-safe: métodos formatadores retornam `null` para entrada `null`; parsers retornam `null` para null/blank/inválido.
5. Criar `NumberFormat` via `NumberFormat.getNumberInstance(new Locale("pt", "BR"))` — **nunca** reutilizar instância como campo estático (não é thread-safe).
6. Java 8 apenas — sem `var`.
7. Importar `GovNumberConstants` do módulo domain.

### Imports necessários

```java
import br.com.contabilizei.obrigacoes.govcore.util.GovNumberConstants;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
```

### Métodos

```java
/**
 * Formata BigDecimal para padrão monetário brasileiro sem símbolo.
 * null → null. BigDecimal("1234.56") → "1.234,56"
 */
public static String formatBrl(BigDecimal value) {
    if (value == null) return null;
    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
    nf.setMinimumFractionDigits(GovNumberConstants.SCALE_MONETARIO);
    nf.setMaximumFractionDigits(GovNumberConstants.SCALE_MONETARIO);
    return nf.format(value);
}

/**
 * Formata com prefixo "R$ ".
 * null → null. BigDecimal("1234.56") → "R$ 1.234,56"
 */
public static String formatBrlWithSymbol(BigDecimal value) {
    String formatted = formatBrl(value);
    return formatted == null ? null : "R$ " + formatted;
}

/**
 * Formata para campo numérico SPED/posicional.
 * Vírgula decimal, sem separador de milhar, escala fixa.
 * null → null. (BigDecimal("1234.5"), 2) → "1234,50"
 */
public static String toSpedDecimal(BigDecimal value, int scale) {
    if (value == null) return null;
    BigDecimal scaled = value.setScale(scale, GovNumberConstants.ROUNDING_FISCAL);
    return scaled.toPlainString().replace('.', ',');
}

/**
 * Formata como percentual brasileiro.
 * null → null. BigDecimal("15.5") → "15,50%"
 */
public static String formatPercentual(BigDecimal value) {
    if (value == null) return null;
    NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
    nf.setMinimumFractionDigits(GovNumberConstants.SCALE_PERCENTUAL);
    nf.setMaximumFractionDigits(GovNumberConstants.SCALE_PERCENTUAL);
    return nf.format(value) + "%";
}

/**
 * Parse de string BRL para BigDecimal.
 * Aceita: "1.234,56" | "R$ 1.234,56" | "1234,56"
 * null/blank/inválido → null
 */
public static BigDecimal parseBrl(String value) {
    if (value == null || value.trim().isEmpty()) return null;
    String cleaned = value.trim().replace("R$", "").trim();
    try {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pt", "BR"));
        nf.setParseBigDecimal(true);
        return (BigDecimal) nf.parse(cleaned);
    } catch (ParseException e) {
        return null;
    }
}

/**
 * Parse de string SPED (vírgula decimal) para BigDecimal.
 * Aceita: "1234,56" | "0,00"
 * null/blank/inválido → null
 */
public static BigDecimal parseSpedDecimal(String value) {
    if (value == null || value.trim().isEmpty()) return null;
    try {
        return new BigDecimal(value.trim().replace(',', '.'));
    } catch (NumberFormatException e) {
        return null;
    }
}
```

---

## T007 — Criar `GovCurrencyFormatsTest.java`

**Arquivo:** `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovCurrencyFormatsTest.java`

### Instruções

1. JUnit 4, `Assert.*` importado estaticamente.
2. Sem Mockito.

### Testes obrigatórios

```java
// formatBrl
@Test
public void formatBrl_1234_56() {
    assertEquals("1.234,56", GovCurrencyFormats.formatBrl(new BigDecimal("1234.56")));
}

@Test
public void formatBrl_null_retornaNull() {
    assertNull(GovCurrencyFormats.formatBrl(null));
}

@Test
public void formatBrl_zero() {
    assertEquals("0,00", GovCurrencyFormats.formatBrl(BigDecimal.ZERO));
}

@Test
public void formatBrl_inteiro_padded() {
    assertEquals("100,00", GovCurrencyFormats.formatBrl(new BigDecimal("100")));
}

// formatBrlWithSymbol
@Test
public void formatBrlWithSymbol_prefixo_r$() {
    assertEquals("R$ 1.234,56", GovCurrencyFormats.formatBrlWithSymbol(new BigDecimal("1234.56")));
}

@Test
public void formatBrlWithSymbol_null_retornaNull() {
    assertNull(GovCurrencyFormats.formatBrlWithSymbol(null));
}

// toSpedDecimal
@Test
public void toSpedDecimal_virgula_semMilhar() {
    assertEquals("1234,50", GovCurrencyFormats.toSpedDecimal(new BigDecimal("1234.5"), 2));
}

@Test
public void toSpedDecimal_null_retornaNull() {
    assertNull(GovCurrencyFormats.toSpedDecimal(null, 2));
}

@Test
public void toSpedDecimal_scale4() {
    assertEquals("0,0065", GovCurrencyFormats.toSpedDecimal(new BigDecimal("0.0065"), 4));
}

@Test
public void toSpedDecimal_arredondaHalfEven() {
    // 2.565 com scale 2 HALF_EVEN → 2.56 (6 → par, já é 6)
    // 2.575 com scale 2 HALF_EVEN → 2.58 (8 é par)
    assertEquals("2,58", GovCurrencyFormats.toSpedDecimal(new BigDecimal("2.575"), 2));
}

// formatPercentual
@Test
public void formatPercentual_15() {
    assertEquals("15,00%", GovCurrencyFormats.formatPercentual(new BigDecimal("15")));
}

// parseBrl
@Test
public void parseBrl_1234_virgula_56() {
    assertEquals(new BigDecimal("1234.56"), GovCurrencyFormats.parseBrl("1.234,56"));
}

@Test
public void parseBrl_comSimbolo() {
    assertNotNull(GovCurrencyFormats.parseBrl("R$ 1.234,56"));
}

@Test
public void parseBrl_null_retornaNull() {
    assertNull(GovCurrencyFormats.parseBrl(null));
}

@Test
public void parseBrl_blank_retornaNull() {
    assertNull(GovCurrencyFormats.parseBrl("  "));
}

// parseSpedDecimal
@Test
public void parseSpedDecimal_virgula() {
    assertEquals(new BigDecimal("1234.56"), GovCurrencyFormats.parseSpedDecimal("1234,56"));
}

@Test
public void parseSpedDecimal_null_retornaNull() {
    assertNull(GovCurrencyFormats.parseSpedDecimal(null));
}
```

---

## Checklist de Validação

- [ ] `formatBrl(BigDecimal("1234.56"))` == `"1.234,56"` (separador de milhar ponto, decimal vírgula)
- [ ] `formatBrlWithSymbol(BigDecimal("1234.56"))` == `"R$ 1.234,56"`
- [ ] `toSpedDecimal(BigDecimal("1234.5"), 2)` == `"1234,50"` (sem separador de milhar)
- [ ] `toSpedDecimal` usa `ROUNDING_FISCAL` (HALF_EVEN) ao aplicar scale
- [ ] `parseBrl(null)` == `null`
- [ ] `parseBrl("R$ 1.234,56")` parseia corretamente
- [ ] `parseSpedDecimal("1234,56")` retorna `BigDecimal("1234.56")`
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-format` passa
- [ ] JaCoCo 90% linha e 90% branch

## Review Feedback

TBD

