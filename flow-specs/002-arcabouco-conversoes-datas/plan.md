# Plano de Implementação: Arcabouço de Conversões de Datas

**Branch**: `feature/002-arcabouco-conversoes-datas` | **Spec**: `flow-specs/002-arcabouco-conversoes-datas/spec.md`
**Target**: `master-ctbz`

---

## Project Conventions

> Auto-detectado do repositório. Todos os work packages DEVEM seguir estas convenções.

- **Linguagens**: Java 8 (`source`/`target 1.8`, `pom.xml`) + Kotlin 1.8.22 (`kotlin.version`)
- **Package manager**: Maven 3.x (reactor parent `pom.xml` com `settings.xml`)
- **Linter/Formatter**: Nenhum linter configurado explicitamente — seguir estilo existente (indent 4 espaços Java, 4 espaços Kotlin)
- **Test runner**: JUnit 4.13.2 (`junit.version` em `pom.xml`) + `kotlin-test-junit` para módulo Kotlin
- **Mocking**: Mockito 4.11.0 (`mockito.version`)
- **Coverage**: JaCoCo 0.8.11 — gate 90% linha/branch (`domain`, `format`); gates existentes para `kotlin`
- **CI/CD**: Bitbucket Pipelines (`bitbucket-pipelines.yml`)
- **Build**: `mvn -q verify` (reator completo) | `cd <modulo> && mvn -q verify` (módulo individual)
- **Módulos relevantes**: `declaracoes-gov-core-domain`, `declaracoes-gov-core-format`, `declaracoes-gov-core-kotlin`

---

## Sumário

Criação de arcabouço centralizado de conversões de datas em 3 módulos existentes, eliminando 78+ ocorrências de boilerplate identificadas em 5 projetos do ecossistema `declaracoes-*`. Nenhum novo módulo Maven será criado — apenas novos arquivos `.java` e `.kt` nos pacotes `util` e `ext` já existentes.

**8 arquivos novos / 1 expansão** → 9 arquivos de produção + 9 arquivos de teste.

---

## Contexto Técnico

- **Linguagem/Versão**: Java 8 (JDK-only em `domain`; Jackson opcional em `format`) + Kotlin 1.8.22
- **Dependências primárias**: JDK 8 (`java.time`, `java.util.Date`, `java.util.Calendar`, `java.util.TimeZone`) — zero dependências externas adicionais
- **Storage**: N/A
- **Testing**: JUnit 4 (`@Test`, `Assert.*`); Kotlin: `kotlin-test-junit`
- **Plataforma**: JVM (Java 8+, Kotlin JVM)
- **Constraints**: AR-003 (`domain` JDK-only), AR-004 (Java 8 baseline), AR-002 (sem frameworks), retrocompatibilidade total com `GovDateUtils`, `XmlDates`, `GovCompetenceFormats` e extensões Kotlin existentes
- **Escopo**: 4 classes Java em `domain`, 1 em `format`, expansão de 1 arquivo Kotlin, 2 novos arquivos Kotlin

---

## Constitution Check

| Princípio | Status | Observação |
|-----------|--------|-----------|
| I. Declaration & Framework Agnostic | ✅ | Nenhuma dependência de framework. JDK-only em `domain`. |
| II. Multi-Agent Shared Memory | ✅ | MEMORY.md será atualizado em cada transição de fase |
| III. Test-Driven Integrity (90% Coverage) | ✅ | JaCoCo gates mantidos; testes escritos para cada classe |
| IV. Collaborative Agent Delegation | ✅ | Subagente gerado para tasks/WP |
| V. Secure XML & Crypto Standards | ✅ | Sem impacto; nenhuma classe toca XML ou criptografia |

---

## Estrutura do Projeto

### Documentação (esta feature)

```
flow-specs/002-arcabouco-conversoes-datas/
├── spec.md          ✅ criado
├── plan.md          ✅ este arquivo
├── meta.json        ✅ atualizado
└── tasks/           ← WP files gerados por subagente
    ├── WP01-constantes-e-conversoes-java-time.md
    ├── WP02-legacy-date-converter.md
    ├── WP03-gov-date-parser-e-formats.md
    ├── WP04-date-extensions-kotlin.md
    ├── WP05-temporal-value-types.md
    └── WP06-date-string-types.md
```

### Código Fonte (raiz do repositório)

```
declaracoes-gov-core-domain/
└── src/
    ├── main/java/br/com/contabilizei/obrigacoes/govcore/util/
    │   ├── GovTimeConstants.java          ← NOVO
    │   ├── JavaTimeConversions.java       ← NOVO
    │   ├── LegacyDateConverter.java       ← NOVO
    │   └── GovDateParser.java             ← NOVO
    └── test/java/br/com/contabilizei/obrigacoes/govcore/util/
        ├── GovTimeConstantsTest.java      ← NOVO
        ├── JavaTimeConversionsTest.java   ← NOVO
        ├── LegacyDateConverterTest.java   ← NOVO
        └── GovDateParserTest.java         ← NOVO

declaracoes-gov-core-format/
└── src/
    ├── main/java/br/com/contabilizei/obrigacoes/govcore/util/
    │   └── GovDateFormats.java            ← NOVO
    └── test/java/br/com/contabilizei/obrigacoes/govcore/util/
        └── GovDateFormatsTest.java        ← NOVO

declaracoes-gov-core-kotlin/
└── src/
    ├── main/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/
    │   ├── DateExtensions.kt              ← EXPANDIR (não deletar existente)
    │   ├── TemporalValueTypes.kt          ← NOVO
    │   └── DateStringTypes.kt             ← NOVO
    └── test/kotlin/br/com/contabilizei/obrigacoes/govcore/ext/
        ├── DateExtensionsTest.kt          ← EXPANDIR
        ├── TemporalValueTypesTest.kt      ← NOVO
        └── DateStringTypesTest.kt         ← NOVO
```

---

## Decisões Arquiteturais

| Decisão | Escolha | Justificativa |
|---------|---------|--------------|
| Módulo | Pacotes nos módulos existentes | Novo módulo seria over-engineering; todo o conteúdo é JDK-puro |
| ZoneId default | `ZoneId.systemDefault()` + sobrecarga explícita | Consistente com `GovDateUtils` existente |
| Null safety | Retorna `null` para entrada `null` | Consistente com padrão do projeto |
| `GovDateParser.parseYearMonth` em `domain` | Reimplementar logicamente (sem delegar a `format`) | AR-003: `domain` não pode depender de `format` |
| `Calendar.month` em Kotlin | Base 1 (não base 0 do Java) | Semântica intuitiva para o consumidor |
| Regex em `DateStringTypes` | Validação estrutural, não semântica | Validação semântica (ex: mês ≤ 12) via parsing subsequente com `java.time` |
| Value classes Kotlin | `@JvmInline value class` com `init { require(...) }` | Validação no construtor, zero overhead em runtime |

---

## Análise de Paralelismo dos WPs

```
WP01 (GovTimeConstants + JavaTimeConversions)
  └── WP02 (LegacyDateConverter) — depende de WP01 para ZoneId constants
        └── WP03 (GovDateParser + GovDateFormats) — depende de WP01 (formatters)
              └── WP04 (DateExtensions.kt) — depende de WP02+WP03 (delega para converters)
                    ├── WP05 (TemporalValueTypes.kt) — independente após WP04
                    └── WP06 (DateStringTypes.kt) — independente após WP04
```

WPs em paralelo possíveis após WP01: WP02 pode iniciar imediatamente. WP05 e WP06 podem ser executados em paralelo após WP04.

---

## Estratégia de Testes

Cobertura obrigatória em **todos** os testes:

1. **Null-safety** — `null` de entrada → `null` de saída (sem NPE)
2. **Roundtrips** — `A → B → A` preserva o valor original
3. **Datas de borda**:
   - Epoch: `1970-01-01`
   - Fevereiro bissexto: `2024-02-29`
   - Y2038: `2038-01-19`
   - DST America/Sao_Paulo (ex: `2025-11-02` — transição de horário de verão)
4. **ZoneId explícito**: UTC e `America/Sao_Paulo` (pelo menos)
5. **Duration**: zero, negativo, valores grandes
6. **DateStringTypes**: string válida, string com formato errado (→ `IllegalArgumentException`), `ofOrNull` com `null` → `null`

Gate de aprovação: `mvn -q verify` no reator completo sem falhas.

---

## Rastreabilidade de Requisitos

| RF | Classe | WP |
|----|--------|----|
| RF-001 | `GovTimeConstants` | WP01 |
| RF-002 | `LegacyDateConverter` | WP02 |
| RF-003 | `JavaTimeConversions` | WP01 |
| RF-004 | `GovDateParser` | WP03 |
| RF-005 | `GovDateFormats` | WP03 |
| RF-006 | `DateExtensions.kt` (expansão) | WP04 |
| RF-007 | `TemporalValueTypes.kt` | WP05 |
| RF-008 | `DateStringTypes.kt` | WP06 |
