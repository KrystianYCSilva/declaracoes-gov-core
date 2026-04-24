# Tasks: Text, Regex and Value Classes Utils

**Input**: Design documents from `flow-specs/003-text-regex-utils/`
**Prerequisites**: [plan.md](plan.md), [spec.md](spec.md)

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3)
- Include exact file paths in descriptions

---

## Phase 1: Java Domain Regex (WP01)

- [ ] T001 [US1] Criar classe `GovRegexPatterns.java` no módulo `declaracoes-gov-core-domain`
- [ ] T002 [US1] Adicionar constants pré-compiladas em `GovRegexPatterns.java` (Dados contato, doc, SPED, etc)
- [ ] T003 [US1] Criar `GovRegexPatternsTest.java` validando compilação correta das Regexes

---

## Phase 2: Java Format Text Utils (WP02)

- [ ] T004 [US2] Criar classe `GovStringUtils.java` no módulo `declaracoes-gov-core-format`
- [ ] T005 [US2] Implementar método de remoção de acentos com `java.text.Normalizer`
- [ ] T006 [US2] Implementar sanitização posicional (lpad, rpad, truncate, toSpedFormat)
- [ ] T007 [US2] Implementar filter de controle W3C para XML validando contra `GovRegexPatterns`
- [ ] T008 [US2] Criar `GovStringUtilsTest.java` com coverage de 90%+

---

## Phase 3: Kotlin Syntax Sugar e Value Classes (WP03)

- [ ] T009 [US3] Criar object `GovRegex.kt` em `declaracoes-gov-core-kotlin` envelopando os patterns do Java em Kotlin Regex
- [ ] T010 [US3] Criar `TextExtensions.kt` com as extension functions para strings
- [ ] T011 [US3] Criar catálogo de Value Classes `FiscalTypes.kt` (`@JvmInline value class`) implementando chamadas `require()`
- [ ] T012 [US3] Criar testes unitários Kotlin para cobrir as extensões

---

## Phase 4: Adendo — Masks, Booleanos SPED e Domain Types (WP04)

> **Addendum** adicionado por Claude Opus em 2026-04-24. Não modifica WP01-WP03.

- [ ] T013 Adicionar mask application em `TextExtensions.kt` (`applyCnpjMask`, `applyCpfMask`, `applyCepMask`, `applyTelefoneMask`, `applyNisMask` + aliases semânticos)
- [ ] T014 Adicionar booleanos SPED em `TextExtensions.kt` (`Boolean?.toStringSimNao`, `String?.toBooleanSimNao`, `Int.toBooleanSped`, `Boolean.toIntSped`)
- [ ] T015 Adicionar `CodigoReceita` value class em `FiscalTypes.kt` (regex `\d{4}`, constantes IRPJ/PIS/COFINS/CSLL, `GovRegexPatterns.CODIGO_RECEITA`)
- [ ] T016 Adicionar `CFOP` value class em `FiscalTypes.kt` (regex `[1-35-7]\d{3}`, `isEntrada/isSaida/isEstadual/isInterestadual/isExterior`, `GovRegexPatterns.CFOP`)
