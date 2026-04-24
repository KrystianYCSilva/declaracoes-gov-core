---
work_package_id: WP02
title: Text + Escape (ApacheTextExtensions, ApacheEscapeExtensions)
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T003
- T004
- T005
---

# WP02 — Text + Escape Extensions

## Objetivo

Criar `ApacheTextExtensions.kt` e `ApacheEscapeExtensions.kt` no módulo `kotlin-apache`. Pode rodar em paralelo com WP03, WP04, WP05 após WP01.

---

## T003 — Criar `ApacheTextExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-apache/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/text/ApacheTextExtensions.kt`

### Contexto

- Usar `org.apache.commons.lang3.StringUtils` para a maioria das operações.
- Usar `org.apache.commons.text.WordUtils` para `capitalizeWords`, `initials`, `wrap`.
- `reverseText` usa `StringUtils.reverse` — evita colisão com `String.reversed()` da stdlib Kotlin.
- `capitalizeFirst` usa `StringUtils.capitalize` — evita deprecação de `String.capitalize()` do Kotlin 1.5+.
- Zero regex criada neste arquivo — apenas delegates para APIs Apache.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.apache.text

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.WordUtils

// ---------------------------------------------------------------------------
// StringUtils wrappers
// ---------------------------------------------------------------------------
/** true se null ou em branco (espaços, tabs). */
fun String?.isBlankOrNull(): Boolean = StringUtils.isBlank(this)

fun String?.isNotBlankOrNull(): Boolean = !StringUtils.isBlank(this)

/** Retorna default se null ou em branco; caso contrário retorna o valor original. */
fun String?.defaultIfBlank(default: String): String = StringUtils.defaultIfBlank(this, default)

/**
 * Remove acentos usando NFD + remoção de combining chars via StringUtils.
 * Mais robusto que java.text.Normalizer para caracteres do português.
 * null → null
 */
fun String?.stripAccents(): String? = this?.let { StringUtils.stripAccents(it) }

/**
 * Abrevia a string para maxWidth chars, adicionando abbrevMarker no final.
 * null → null. "Prestacao de servicos contabeis".abbreviate(20) → "Prestacao de servic..."
 */
fun String?.abbreviate(maxWidth: Int, abbrevMarker: String = "..."): String? =
    this?.let { StringUtils.abbreviate(it, abbrevMarker, maxWidth) }

/** Capitaliza apenas o primeiro caractere (equivalente ao deprecated capitalize() do Kotlin). */
fun String.capitalizeFirst(): String = StringUtils.capitalize(this)

/** Descapitaliza o primeiro caractere. */
fun String.uncapitalizeFirst(): String = StringUtils.uncapitalize(this)

/** Inverte maiúsculas/minúsculas de cada caractere. */
fun String.swapCase(): String = StringUtils.swapCase(this)

/** Inverte a string (usar em vez de reversed() quando precisar de nome explícito). */
fun String.reverseText(): String = StringUtils.reverse(this)

/** Centraliza a string em um campo de tamanho size preenchido com padChar. */
fun String.center(size: Int, padChar: Char = ' '): String = StringUtils.center(this, size, padChar)

/** Retorna a substring entre os delimitadores open e close, ou null se não encontrar. */
fun String.substringBetween(open: String, close: String): String? =
    StringUtils.substringBetween(this, open, close)

/** Conta o número de ocorrências de sub na string. */
fun String.countOccurrences(sub: String): Int = StringUtils.countMatches(this, sub)

// ---------------------------------------------------------------------------
// WordUtils (commons-text)
// ---------------------------------------------------------------------------
/** Capitaliza a primeira letra de cada palavra. "MARIA DA SILVA" → "Maria Da Silva" */
fun String.capitalizeWords(): String = WordUtils.capitalizeFully(this)

/** Extrai as iniciais de cada palavra. "Maria Da Silva" → "MDS" */
fun String.initials(): String = WordUtils.initials(this)

/** Quebra a string em múltiplas linhas no wrapLength. */
fun String.wrap(wrapLength: Int): String = WordUtils.wrap(this, wrapLength)
```

---

## T004 — Criar `ApacheEscapeExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-apache/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/text/ApacheEscapeExtensions.kt`

### Contexto

- Usar `org.apache.commons.text.StringEscapeUtils` para todos os escapes.
- `sanitizeForSped` e `sanitizeForEsocial` são wrappers de alto nível com semântica de domínio.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.apache.text

import org.apache.commons.text.StringEscapeUtils

// ---------------------------------------------------------------------------
// XML — CRÍTICO para web services governamentais (eSocial, Reinf, NF-e)
// ---------------------------------------------------------------------------
/** Escapa entidades XML 1.0: &amp; &lt; &gt; &quot; &apos; */
fun String.escapeXml10(): String = StringEscapeUtils.escapeXml10(this)

/** Escapa entidades XML 1.1 (inclui control chars adicionais). */
fun String.escapeXml11(): String = StringEscapeUtils.escapeXml11(this)

fun String.unescapeXml(): String = StringEscapeUtils.unescapeXml(this)

// ---------------------------------------------------------------------------
// HTML (relatórios, portais governamentais)
// ---------------------------------------------------------------------------
fun String.escapeHtml4(): String = StringEscapeUtils.escapeHtml4(this)
fun String.unescapeHtml4(): String = StringEscapeUtils.unescapeHtml4(this)

// ---------------------------------------------------------------------------
// CSV (exportações fiscais)
// ---------------------------------------------------------------------------
fun String.escapeCsv(): String = StringEscapeUtils.escapeCsv(this)
fun String.unescapeCsv(): String = StringEscapeUtils.unescapeCsv(this)

// ---------------------------------------------------------------------------
// Java (logging de dados fiscais)
// ---------------------------------------------------------------------------
fun String.escapeJava(): String = StringEscapeUtils.escapeJava(this)

// ---------------------------------------------------------------------------
// Sanitização específica para o domínio
// ---------------------------------------------------------------------------
/**
 * Sanitização para campos SPED: escapeXml10 + stripAccents + uppercase.
 * Remove acentos pois SPED 1.0 não aceita caracteres non-ASCII em todos os campos.
 */
fun String.sanitizeForSped(): String =
    this.escapeXml10().stripAccents()?.uppercase() ?: this.uppercase()

/**
 * Sanitização para eSocial: escapeXml10 + substituição de control chars por espaço.
 * eSocial aceita UTF-8 mas rejeita caracteres de controle (U+0000-U+001F exceto tab/LF).
 */
fun String.sanitizeForEsocial(): String {
    val escaped = this.escapeXml10()
    // Substituir control chars (exceto \t e \n) por espaço
    return escaped.replace(Regex("[\u0000-\u0008\u000B\u000C\u000E-\u001F]"), " ")
}
```

---

## T005 — Criar testes para ApacheTextExtensions e ApacheEscapeExtensions

### Testes obrigatórios — ApacheTextExtensions

```kotlin
@Test fun `stripAccents portugues`() {
    assertEquals("Acucar Cubico", "Açúcar Cúbico".stripAccents())
}

@Test fun `stripAccents null retorna null`() {
    assertNull(null.stripAccents())
}

@Test fun `abbreviate trunca com marcador`() {
    val result = "Prestacao de servicos contabeis tributarios".abbreviate(30)
    assertNotNull(result)
    assertTrue(result!!.length <= 30)
    assertTrue(result.endsWith("..."))
}

@Test fun `capitalizeFirst minuscula`() {
    assertEquals("Hello", "hello".capitalizeFirst())
}

@Test fun `reverseText`() {
    assertEquals("olleH", "Hello".reverseText())
}

@Test fun `capitalizeWords`() {
    assertEquals("Maria Da Silva", "MARIA DA SILVA".capitalizeWords())
}

@Test fun `initials`() {
    assertEquals("MDS", "Maria Da Silva".initials())
}

@Test fun `substringBetween encontra`() {
    assertEquals("CNPJ", "[CNPJ]".substringBetween("[", "]"))
}

@Test fun `substringBetween nao encontra retorna null`() {
    assertNull("CNPJ".substringBetween("[", "]"))
}

@Test fun `countOccurrences`() {
    assertEquals(3, "abcabcabc".countOccurrences("abc"))
}

@Test fun `defaultIfBlank usa default para blank`() {
    assertEquals("default", "   ".defaultIfBlank("default"))
}
```

### Testes obrigatórios — ApacheEscapeExtensions

```kotlin
@Test fun `escapeXml10 ampersand e chevrons`() {
    assertEquals("Empresa &amp; Cia &lt;Ltda&gt;", "Empresa & Cia <Ltda>".escapeXml10())
}

@Test fun `unescapeXml roundtrip`() {
    val original = "Empresa & Cia <Ltda>"
    assertEquals(original, original.escapeXml10().unescapeXml())
}

@Test fun `escapeCsv envolve campo com virgula`() {
    val result = "Empresa, SA".escapeCsv()
    assertTrue(result.startsWith("\""))
    assertTrue(result.endsWith("\""))
}

@Test fun `sanitizeForSped uppercase sem acentos`() {
    val result = "Açúcar & Cia".sanitizeForSped()
    assertEquals("ACUCAR &amp; CIA", result)
}

@Test fun `sanitizeForEsocial preserva UTF8 remove control chars`() {
    val result = "Empresa\u0001Ltda".sanitizeForEsocial()
    assertFalse(result.contains('\u0001'))
}

@Test fun `escapeXml10_abc_roundtrip`() {
    assertEquals("abc", "abc".escapeXml10().unescapeXml())
}
```

---

## Checklist de Validação

- [ ] `"Açúcar Cúbico".stripAccents()` == `"Acucar Cubico"`
- [ ] `"MARIA DA SILVA".capitalizeWords()` == `"Maria Da Silva"`
- [ ] `"Empresa & Cia <Ltda>".escapeXml10()` == `"Empresa &amp; Cia &lt;Ltda&gt;"`
- [ ] `escapeXml10().unescapeXml()` == string original (roundtrip)
- [ ] `"Empresa, SA".escapeCsv()` envolve com aspas duplas
- [ ] `sanitizeForSped` retorna uppercase sem acentos
- [ ] `reverseText()` não conflita com `reversed()` da stdlib
- [ ] `capitalizeFirst()` não usa o deprecated `capitalize()` do Kotlin
- [ ] JaCoCo 90% linha e 90% branch
