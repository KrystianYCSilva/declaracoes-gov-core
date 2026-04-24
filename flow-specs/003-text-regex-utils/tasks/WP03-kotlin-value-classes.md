---
work_package_id: WP03
title: Kotlin Syntax Sugar e Value Classes
lane: planned
subtasks:
  - T009
  - T010
  - T011
  - T012
dependencies: [WP01, WP02]
---
# WP03 - Kotlin Syntax Sugar e Value Classes

## Objective
Construir em `declaracoes-gov-core-kotlin` as extensões fluidas e as tipagens rígidas `value class` (Type Safety em runtime-zero) baseadas nas constantes criadas na arquitetura Java, criando uma API nativa incrivelmente robusta para Kotlin.

## Steps
1. Mapear `GovRegex` usando `.toRegex()` sobre as contantes Java criadas em `GovRegexPatterns.java`.
2. Adicionar extensões (`val String?.digitsOnly`, `fun String?.removeAcentos()`) em `TextExtensions.kt` usando a camada de formatação de Java criada.
3. Criar arquivo `FiscalTypes.kt` hospedando tipos baseados em `@JvmInline value class` como: `Email`, `TelefoneBR`, `Cep`, `Passaporte`, `CpfFormato`, `CnpjFormato`, `PisPasep`, `ChaveAcessoNfe`, `Cnae`, `Cbo`, `Ncm`, `ReciboGov`.
4. Os blocos `init` das `value classes` devem invocar `require(this.value.matches(...))` jogando `IllegalArgumentException`.
5. Criar `TextExtensionsTest.kt` e `FiscalTypesTest.kt` garantindo fail-fast correto.

## Validation
- O projeto Kotlin compila com Target Java 8.
- Gate do JaCoCo (90%+) no módulo Kotlin.

## Review Feedback

TBD

