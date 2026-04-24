---
work_package_id: WP03
title: Similarity (ApacheSimilarityExtensions)
lane: "planned"
dependencies:
- WP01
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T006
- T007
---

# WP03 — ApacheSimilarityExtensions

## Objetivo

Criar `ApacheSimilarityExtensions.kt` com métricas de similaridade para reconciliação de razões sociais entre sistemas. Pode rodar em paralelo com WP02, WP04, WP05 após WP01.

---

## T006 — Criar `ApacheSimilarityExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin-guava/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/text/ApacheSimilarityExtensions.kt`

**Nota:** O caminho correto é `kotlin-apache`, não `kotlin-guava`:
`declaracoes-gov-core-kotlin-apache/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/apache/text/ApacheSimilarityExtensions.kt`

### Contexto

- `JaroWinklerSimilarity` de `commons-text` — retorna `Double` em [0.0, 1.0].
- `LevenshteinDistance` de `commons-text` — retorna `Int` (distância de edição).
- `LongestCommonSubsequence` de `commons-text`.
- `FuzzyScore` de `commons-text` — pontua match por caracteres consecutivos.
- Zero regex criada — apenas delegates para APIs Apache.

```kotlin
package br.com.contabilizei.obrigacoes.govcore.apache.text

import org.apache.commons.text.similarity.FuzzyScore
import org.apache.commons.text.similarity.JaroWinklerSimilarity
import org.apache.commons.text.similarity.LevenshteinDistance
import org.apache.commons.text.similarity.LongestCommonSubsequence
import java.util.Locale

// ---------------------------------------------------------------------------
// Métricas de similaridade
// ---------------------------------------------------------------------------
/**
 * Similaridade Jaro-Winkler entre [0.0, 1.0].
 * 1.0 = idêntico. Bom para nomes curtos e prefixos comuns.
 * Ex: "CONTABILIZEI".jaroWinklerSimilarity("CONTABILIZEI TECNOLOGIA") ≥ 0.90
 */
fun String.jaroWinklerSimilarity(other: String): Double =
    JaroWinklerSimilarity().apply(this, other)

/**
 * Distância de edição Levenshtein (número de inserções/deleções/substituições).
 * 0 = idêntico. Sem limite de threshold.
 */
fun String.levenshteinDistance(other: String): Int =
    LevenshteinDistance.getDefaultInstance().apply(this, other)

/**
 * Longest common subsequence entre as duas strings.
 */
fun String.longestCommonSubsequence(other: String): CharSequence =
    LongestCommonSubsequence().apply(this, other)

/**
 * FuzzyScore — pontua correspondência de caracteres consecutivos (pt-BR locale).
 * Útil para autocomplete e busca de nomes próprios.
 */
fun String.fuzzyScore(query: String): Int =
    FuzzyScore(Locale("pt", "BR")).fuzzyScore(this, query)

// ---------------------------------------------------------------------------
// Decisões baseadas em similaridade
// ---------------------------------------------------------------------------
/**
 * Verifica se a string é similar a other com score ≥ threshold.
 * threshold padrão: 0.85 (85% de similaridade Jaro-Winkler).
 */
fun String.isSimilarTo(other: String, threshold: Double = 0.85): Boolean =
    jaroWinklerSimilarity(other) >= threshold

/**
 * Retorna o melhor candidato com similaridade ≥ threshold, ou null se nenhum atingir.
 * Ex: "CONTABILIZEI".bestMatchIn(listOf("CONTABILIZEI TECNOLOGIA LTDA", "OUTRA EMPRESA SA"))
 *     → "CONTABILIZEI TECNOLOGIA LTDA"
 */
fun String.bestMatchIn(candidates: List<String>, threshold: Double = 0.85): String? =
    candidates.mapNotNull { candidate ->
        val score = jaroWinklerSimilarity(candidate)
        if (score >= threshold) Pair(candidate, score) else null
    }.maxByOrNull { it.second }?.first

/**
 * Retorna o melhor match com seu score, ou null se abaixo do threshold.
 */
fun String.bestMatchWithScoreIn(
    candidates: List<String>,
    threshold: Double = 0.0
): Pair<String, Double>? =
    candidates.map { it to jaroWinklerSimilarity(it) }
        .filter { it.second >= threshold }
        .maxByOrNull { it.second }

/**
 * Ordena candidatos por similaridade decrescente.
 * Todos os candidatos são retornados, inclusive os com score baixo.
 */
fun String.rankCandidates(candidates: List<String>): List<Pair<String, Double>> =
    candidates.map { it to jaroWinklerSimilarity(it) }
        .sortedByDescending { it.second }
```

---

## T007 — Criar `ApacheSimilarityExtensionsTest.kt`

**Arquivo:** `...apache/text/ApacheSimilarityExtensionsTest.kt`

### Testes obrigatórios

```kotlin
@Test fun `jaroWinklerSimilarity identico e 1_0`() {
    assertEquals(1.0, "CONTABILIZEI".jaroWinklerSimilarity("CONTABILIZEI"), 0.001)
}

@Test fun `jaroWinklerSimilarity prefixo comum tem score alto`() {
    val score = "CONTABILIZEI TECNOLOGIA".jaroWinklerSimilarity("CONTABILIZEI TECNOLOGIA LTDA")
    assertTrue(score >= 0.90, "Esperado >= 0.90, obtido: $score")
}

@Test fun `jaroWinklerSimilarity strings completamente diferentes`() {
    val score = "AMAZON".jaroWinklerSimilarity("XPTO LTDA")
    assertTrue(score < 0.5)
}

@Test fun `levenshteinDistance identico e zero`() {
    assertEquals(0, "abc".levenshteinDistance("abc"))
}

@Test fun `levenshteinDistance um caractere diferente`() {
    assertEquals(1, "abc".levenshteinDistance("abd"))
}

@Test fun `isSimilarTo com threshold padrao`() {
    assertTrue("CONTABILIZEI".isSimilarTo("CONTABILIZEI TECNOLOGIA", 0.85))
    assertFalse("AMAZON".isSimilarTo("XPTO LTDA", 0.85))
}

@Test fun `bestMatchIn encontra melhor correspondencia`() {
    val candidatos = listOf("CONTABILIZEI TECNOLOGIA LTDA", "OUTRA EMPRESA SA", "XYZ COMERCIO")
    val match = "CONTABILIZEI TECNOLOGIA".bestMatchIn(candidatos, 0.80)
    assertEquals("CONTABILIZEI TECNOLOGIA LTDA", match)
}

@Test fun `bestMatchIn retorna null se nenhum atingir threshold`() {
    val candidatos = listOf("OUTRA EMPRESA SA", "XYZ COMERCIO")
    assertNull("CONTABILIZEI".bestMatchIn(candidatos, 0.99))
}

@Test fun `rankCandidates ordena por score decrescente`() {
    val query = "CONTABILIZEI"
    val candidatos = listOf("CONTABILIZEI TECNOLOGIA", "XYZ SA", "CONTABILIZEI LTDA")
    val ranked = query.rankCandidates(candidatos)
    assertTrue(ranked.first().second >= ranked.last().second)
}

@Test fun `bestMatchWithScoreIn retorna par com score`() {
    val result = "CONTABILIZEI".bestMatchWithScoreIn(
        listOf("CONTABILIZEI TECNOLOGIA", "OUTRA SA"), 0.8
    )
    assertNotNull(result)
    assertEquals("CONTABILIZEI TECNOLOGIA", result!!.first)
    assertTrue(result.second >= 0.8)
}
```

---

## Checklist de Validação

- [ ] `"CONTABILIZEI TECNOLOGIA".jaroWinklerSimilarity("CONTABILIZEI TECNOLOGIA LTDA")` >= 0.90
- [ ] `"abc".levenshteinDistance("abc")` == 0
- [ ] `bestMatchIn` retorna `null` quando nenhum candidato atinge threshold
- [ ] `rankCandidates` retorna lista ordenada por score decrescente (maior primeiro)
- [ ] `isSimilarTo` usa threshold padrão 0.85
- [ ] Zero regex criada no arquivo
- [ ] JaCoCo 90% linha e 90% branch
