# Plan: Text, Regex and Value Classes Utils

## Project Conventions
| Category | Value |
|----------|-------|
| Language/Runtime | Java 8 (`maven.compiler.source=1.8`) / Kotlin 1.8+ |
| Build system | Maven 3.x multi-module reactor |
| Test framework | JUnit 4.13.2 |
| Coverage | JaCoCo 0.8.11 — `domain`/`format`/`kotlin`: ≥90% line + ≥90% branch |
| Architecture | 100% Agnostic (No Spring, no Jakarta EE) |

## Summary
Adição do "arsenal definitivo" de processamento de texto e sanitização de regex focado no modelo fiscal brasileiro para os três módulos de infraestrutura da `declaracoes-gov-core`. Implementação centralizada de `GovRegexPatterns` compilados no `domain`, `GovStringUtils` no `format` para padding e caracteres inválidos (SPED/XML) e a "cereja do bolo": Tipagem forte com custo zero em Kotlin usando `@JvmInline value class` no submódulo `kotlin`.

## Technical Context
- **Dependências**: Nenhuma nova (JDK puramente + Kotlin Stdlib nativo).
- **Constraint**: O Regex e o utilitário string do Java devem evitar NPE de toda a forma e proteger contra injeções ou entradas massivas sem comprometer a performance.
- A `@JvmInline` requer o import nativo e a versão da linguagem configurada adequadamente, o plugin maven do kotlin 1.8+ provê suporte nativo total sem precisar the `@JvmInline` hack flags.

## Constitution Check
- **AR-001, AR-002, AR-004**: Nenhum framework será injetado. A base das Regex em Java será estritamente construída com a `java.util.regex.*`.
- **AR-003**: `format` e `kotlin` dependerão de `domain`.

## Project Structure

```
declaracoes-gov-core-domain/
└── src/main/java/br/com/contabilizei/obrigacoes/govcore/text/
    └── GovRegexPatterns.java

declaracoes-gov-core-format/
└── src/main/java/br/com/contabilizei/obrigacoes/govcore/util/
    └── GovStringUtils.java

declaracoes-gov-core-kotlin/
└── src/main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/
    ├── TextExtensions.kt
    └── types/
        └── FiscalTypes.kt
```

## Parallel Work Analysis

```
WP01 (domain) ─────────────────────► independente
WP02 (format) ─── depende WP01 ────►
WP03 (kotlin) ─── depende WP01 e WP02 ────►
```

### Distribuição por Wave

| Wave | WPs em paralelo | Racional |
|------|----------------|---------|
| Wave 1 | WP01 (Java Domain Regex) | Fundamentação. Nenhuma dependência cruzada. |
| Wave 2 | WP02 (Java Format String Utils) | Pode ler e usar constantes regex base do domain, ex: `XML_INVALID_CHARS`. |
| Wave 3 | WP03 (Kotlin Value Classes e Syntax Sugar) | Totalmente dependente do módulo format e domain concluídos. |
