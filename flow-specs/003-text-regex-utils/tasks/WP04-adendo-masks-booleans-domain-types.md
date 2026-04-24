---
work_package_id: WP04
title: Adendo — Masks, Booleanos SPED e Domain Types
lane: planned
dependencies: [WP01, WP02, WP03]
created_at: '2026-04-24T00:00:00.000000+00:00'
subtasks:
- T013
- T014
- T015
- T016
addendum: true
addendum_reason: "Gap analysis identificou ausência de mask application (complemento ao removeMask existente), convenções booleanas SPED (S/N, 1/0), e domain types fiscais (CodigoReceita, CFOP) — não cobertos no escopo original da spec 003."
---

# WP04 — Adendo: Masks, Booleanos SPED e Domain Types (Adendo à Spec 003)

> **ADENDO**: Este WP é um complemento ao escopo original da spec 003, identificado em gap analysis posterior.
> Não modifica WP01–WP03. Depende de WP03 (FiscalTypes.kt já existindo).
> Agent responsável: Claude (não Gemini).

## Objetivo

Adicionar ao módulo `declaracoes-gov-core-kotlin` funcionalidades de alta frequência não cobertas na spec original:

1. **Mask application** — complemento ao `removeMask()` existente em `TextExtensions.kt`
2. **Booleanos SPED** — convenções `"S"/"N"` e `1/0` em `TextExtensions.kt`
3. **Domain types** — `CodigoReceita` e `CFOP` em `FiscalTypes.kt` (do WP03)
4. **GovRegexPatterns adicionais** — padrões para mascaramento e validação estrutural

---

## T013 — Mask Application: extensões em `TextExtensions.kt`

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/TextExtensions.kt`

Adicionar após o bloco de `removeMask()` existente. Não alterar nenhuma função existente.

### API

```kotlin
// ---------------------------------------------------------------------------
// Mask Application — complemento ao removeMask() existente
// ---------------------------------------------------------------------------

/**
 * Aplica máscara de CNPJ: "12345678000190" → "12.345.678/0001-90"
 * Funciona com CNPJ numérico (14 dígitos) ou alfanumérico (RF 2026, 14 chars).
 * Retorna a string sem máscara se o tamanho for inválido.
 */
fun String.applyCnpjMask(): String

/**
 * Aplica máscara de CPF: "12345678901" → "123.456.789-01"
 * Retorna a string sem máscara se não tiver exatamente 11 dígitos.
 */
fun String.applyCpfMask(): String

/**
 * Aplica máscara de CEP: "01310100" → "01310-100"
 * Retorna a string sem máscara se não tiver exatamente 8 dígitos.
 */
fun String.applyCepMask(): String

/**
 * Aplica máscara de telefone brasileiro:
 * 11 dígitos (celular): "11987654321" → "(11) 98765-4321"
 * 10 dígitos (fixo):    "1134567890"  → "(11) 3456-7890"
 * Retorna a string sem máscara para qualquer outro tamanho.
 */
fun String.applyTelefoneMask(): String

/**
 * Aplica máscara de NIS/PIS: "12345678901" → "123.45678.90-1"
 */
fun String.applyNisMask(): String

/**
 * Aliases semânticos para leitura fluente
 */
fun String.formatAsCnpj(): String = applyCnpjMask()
fun String.formatAsCpf(): String = applyCpfMask()
fun String.formatAsCep(): String = applyCepMask()
fun String.formatAsTelefone(): String = applyTelefoneMask()
```

### Implementação de referência (para o implementador)

As máscaras devem chamar `digitsOnly()` / `removeMask()` primeiro, depois aplicar a formatação com `StringBuilder` ou interpolação — sem regex complexa para evitar ReDoS.

```kotlin
fun String.applyCnpjMask(): String {
    val digits = this.removeMask()
    return if (digits.length != 14) this
    else "${digits.substring(0,2)}.${digits.substring(2,5)}.${digits.substring(5,8)}/${digits.substring(8,12)}-${digits.substring(12,14)}"
}
```

### Testes obrigatórios

```kotlin
@Test fun cnpjMaskFormatado()       // "12345678000190" → "12.345.678/0001-90"
@Test fun cnpjMaskJaFormatadoRetornaSemAlteracao() // "12.345.678/0001-90".removeMask().applyCnpjMask() → "12.345.678/0001-90"
@Test fun cpfMaskFormatado()         // "12345678901" → "123.456.789-01"
@Test fun cepMaskFormatado()          // "01310100" → "01310-100"
@Test fun telefoneCelularMask()       // "11987654321" → "(11) 98765-4321"
@Test fun telefoneFixoMask()           // "1134567890" → "(11) 3456-7890"
@Test fun telefoneTamanhoInvalidoRetornaOriginal()
@Test fun formatAsCnpjEhAliasDeApplyCnpjMask()
```

---

## T014 — Booleanos SPED: extensões em `TextExtensions.kt`

**Arquivo:** mesmo `TextExtensions.kt`

Adicionar após o bloco de mask application.

### API

```kotlin
// ---------------------------------------------------------------------------
// Booleanos SPED — convenção "S"/"N" e 1/0
// ---------------------------------------------------------------------------

/**
 * Converte Boolean para o padrão SPED de sim/não.
 * true → "S", false/null → "N"
 */
fun Boolean?.toStringSimNao(): String

/**
 * Converte Boolean para "S"/"N" retornando null se o receptor for null.
 * null → null, true → "S", false → "N"
 */
fun Boolean?.toStringSimNaoOrNull(): String?

/**
 * Parse do padrão SPED "S"/"N".
 * "S"/"SIM"/"1"/"TRUE"/"YES" (case-insensitive, trimmed) → true
 * Qualquer outro valor incluindo null/blank → false
 */
fun String?.toBooleanSimNao(): Boolean

/**
 * Parse do padrão SPED com null-safety.
 * null/blank → null, "S" → true, "N" → false
 */
fun String?.toBooleanSimNaoOrNull(): Boolean?

/**
 * Flag numérico SPED: 1 → true, qualquer outro valor → false
 */
fun Int.toBooleanSped(): Boolean

/**
 * Inverte toBooleanSped: true → 1, false → 0
 */
fun Boolean.toIntSped(): Int
```

### Testes obrigatórios

```kotlin
@Test fun trueParaS()
@Test fun falseParaN()
@Test fun nullParaN()
@Test fun nullParaNullComOrNull()
@Test fun sStringParaTrue()
@Test fun simStringParaTrue()
@Test fun nStringParaFalse()
@Test fun nullStringParaFalse()
@Test fun nullStringParaNullComOrNull()
@Test fun um_paraTrue_sped()
@Test fun zero_paraFalse_sped()
@Test fun outro_paraFalse_sped()   // 2.toBooleanSped() == false
@Test fun toIntSped_true_retorna1()
@Test fun toIntSped_false_retorna0()
```

---

## T015 — `CodigoReceita` value class em `FiscalTypes.kt`

**Arquivo:** `declaracoes-gov-core-kotlin/src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/FiscalTypes.kt`

Adicionar ao arquivo existente (gerado pelo WP03 da spec 003). Não alterar value classes existentes.

### API

```kotlin
/**
 * Código de pagamento DARF (4 dígitos).
 * Exemplos: 0588 = IRPJ Estimativa Mensal, 1708 = IRRF, 2089 = CSLL, 0561 = PIS, 5856 = COFINS
 */
@JvmInline
value class CodigoReceita(val value: String) {
    init {
        require(value.matches(GovRegex.CODIGO_RECEITA)) {
            "Código de receita DARF inválido (esperado 4 dígitos): '$value'"
        }
    }

    override fun toString(): String = value

    companion object {
        fun of(value: String): CodigoReceita
        fun ofOrNull(value: String?): CodigoReceita?

        // Códigos frequentes — convenience
        val IRPJ_ESTIMATIVA: CodigoReceita        // "0588"
        val IRPJ_AJUSTE: CodigoReceita            // "0220"
        val CSLL_ESTIMATIVA: CodigoReceita        // "2484"
        val CSLL_AJUSTE: CodigoReceita            // "6012"
        val PIS: CodigoReceita                    // "0561" (cumulativo)
        val COFINS: CodigoReceita                 // "5856" (cumulativo)
        val IRRF_TRABALHO: CodigoReceita          // "0561" (atenção: verificar)
        val IRRF_SERVICOS: CodigoReceita          // "1708"
        val GPS_EMPRESA: CodigoReceita            // não se aplica — GPS não usa código DARF
        val FGTS_CODIGO: CodigoReceita            // "0500"
    }
}
```

### `GovRegex.kt` — adicionar pattern

No objeto `GovRegex` (WP03), adicionar:
```kotlin
val CODIGO_RECEITA: Regex by lazy { GovRegexPatterns.CODIGO_RECEITA.toRegex() }
```

### `GovRegexPatterns.java` — adicionar pattern

No `GovRegexPatterns.java` (WP01), adicionar:
```java
/** Código de receita DARF: 4 dígitos numéricos. */
public static final Pattern CODIGO_RECEITA = Pattern.compile("\\d{4}");
```

---

## T016 — `CFOP` value class em `FiscalTypes.kt`

**Arquivo:** mesmo `FiscalTypes.kt`

### API

```kotlin
/**
 * Código Fiscal de Operações e Prestações (4 dígitos).
 * Primeiro dígito indica natureza:
 *   1 = Entrada - operação estadual
 *   2 = Entrada - operação interestadual
 *   3 = Entrada - importação
 *   5 = Saída - operação estadual
 *   6 = Saída - operação interestadual
 *   7 = Saída - exportação
 */
@JvmInline
value class CFOP(val value: String) {
    init {
        require(value.matches(GovRegex.CFOP)) {
            "CFOP inválido (esperado 4 dígitos, primeiro deve ser 1-3 ou 5-7): '$value'"
        }
    }

    /** true se for operação de entrada (começa com 1, 2 ou 3) */
    val isEntrada: Boolean get() = value[0] in '1'..'3'

    /** true se for operação de saída (começa com 5, 6 ou 7) */
    val isSaida: Boolean get() = value[0] in '5'..'7'

    /** true se for operação estadual (começa com 1 ou 5) */
    val isEstadual: Boolean get() = value[0] == '1' || value[0] == '5'

    /** true se for operação interestadual (começa com 2 ou 6) */
    val isInterestadual: Boolean get() = value[0] == '2' || value[0] == '6'

    /** true se for importação/exportação (começa com 3 ou 7) */
    val isExterior: Boolean get() = value[0] == '3' || value[0] == '7'

    override fun toString(): String = value

    companion object {
        fun of(value: String): CFOP
        fun ofOrNull(value: String?): CFOP?
    }
}
```

### `GovRegexPatterns.java` — adicionar pattern

```java
/** CFOP: 4 dígitos, primeiro deve ser 1, 2, 3, 5, 6 ou 7. */
public static final Pattern CFOP = Pattern.compile("[1-35-7]\\d{3}");
```

### `GovRegex.kt` — adicionar:

```kotlin
val CFOP: Regex by lazy { GovRegexPatterns.CFOP.toRegex() }
```

### Testes obrigatórios

```kotlin
@Test fun cfopEntradaEstadual_isEntrada_true()    // "1102"
@Test fun cfopSaidaInterestadual_isSaida_true()   // "6102"
@Test fun cfopExportacao_isExterior_true()          // "7101"
@Test fun cfopInvalido_primeiroDigito4_lancaException()  // "4102"
@Test fun cfopInvalido_3Digitos_lancaException()
@Test fun codigoReceita_0588_valido()
@Test fun codigoReceita_5Digitos_invalido()
@Test fun codigoReceitaIRPJ_estimativa_constante()
```

---

## Atualização de `tasks.md` da spec 003

Adicionar seção:

```
## Phase 4: Adendo — Masks, Booleanos SPED e Domain Types (WP04)

- [ ] T013 [P] Implementar mask application (applyCnpjMask, applyCpfMask, applyCepMask, applyTelefoneMask, applyNisMask) em TextExtensions.kt
- [ ] T014 [P] Implementar booleanos SPED (toStringSimNao, toBooleanSimNao, toBooleanSped, toIntSped) em TextExtensions.kt
- [ ] T015 Adicionar CodigoReceita value class em FiscalTypes.kt + GovRegexPatterns + GovRegex
- [ ] T016 Adicionar CFOP value class em FiscalTypes.kt + GovRegexPatterns + GovRegex
```

## Checklist de Validação

- [ ] `"12345678000190".applyCnpjMask()` == `"12.345.678/0001-90"`
- [ ] `"12345678901".applyCpfMask()` == `"123.456.789-01"`
- [ ] `true.toStringSimNao()` == `"S"`
- [ ] `"S".toBooleanSimNao()` == `true`
- [ ] `1.toBooleanSped()` == `true`
- [ ] `CFOP("1102").isEntrada` == `true`
- [ ] `CFOP("4000")` lança `IllegalArgumentException`
- [ ] `CodigoReceita("0588")` não lança exceção
- [ ] `mvn -q verify` no módulo `kotlin` passa sem erros

## Review Feedback

TBD

