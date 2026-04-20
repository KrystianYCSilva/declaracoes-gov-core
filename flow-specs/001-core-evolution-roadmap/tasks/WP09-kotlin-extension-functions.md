---
work_package_id: WP09
title: Submódulo Kotlin — Extension Functions
lane: "doing"
dependencies: []
created_at: '2026-04-20T21:13:24.195910+00:00'
subtasks:
- T001: Scaffold novo módulo Maven `declaracoes-gov-core-kotlin`
- T002: Extension functions para tipos de domínio
- T003: Extension functions de data/tempo
- T004: Extension functions de coleção/null-safety
- T005: Extension functions JSON (opcional, jackson-module-kotlin)
- T006: Testes para todas as extensions Kotlin
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:58:28.606428+00:00"
---

# WP09 — Submódulo Kotlin — Extension Functions

## Context

Projetos consumidores em Kotlin (`obrigacoes-service-reinf`, `gateway-bardo`, `obrigacoes-service-dctfweb`) criam extensões idiomáticas sobre os tipos Java da lib de forma independente, gerando inconsistências. Este WP cria um submódulo dedicado `declaracoes-gov-core-kotlin` com **somente extension functions** como thin wrappers sobre tipos Java existentes — sem lógica de negócio, sem logger DSL (projeto futuro separado), dependência exclusiva de `kotlin-stdlib` (+ `jackson-module-kotlin` opcional para JSON extensions). **Antes de iniciar:** verificar versão Kotlin usada em `obrigacoes-service-reinf` e `gateway-bardo` para alinhar.

## Constraints

- Kotlin (versão a definir — alinhar com projetos consumidores)
- Java 8 (source/target 1.8) — interop deve ser Java 8 compatível
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc/KDoc e comentários em português (pt-BR)
- JaCoCo gates: `kotlin` ≥ 90% linha + ≥ 90% branch
- Dependências: `kotlin-stdlib` ONLY + `declaracoes-gov-core-domain` + `declaracoes-gov-core-format`
- `jackson-module-kotlin` APENAS para extensions JSON (opcional, já transitivo via `format`)
- **ZERO lógica de negócio** — apenas wrappers idiomáticos sobre tipos Java existentes
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Scaffold novo módulo Maven `declaracoes-gov-core-kotlin`

**Objetivo:** Criar a estrutura do novo módulo Kotlin no reactor Maven, configurando o `kotlin-maven-plugin`, dependências corretas e JaCoCo gate.

**Passos:**
1. Verificar versão Kotlin em `obrigacoes-service-reinf/pom.xml` e `gateway-bardo/pom.xml` — usar a versão mais recente compatível
2. Adicionar `<kotlin.version>X.X.X</kotlin.version>` ao `pom.xml` raiz do reactor (dentro de `<properties>`)
3. Adicionar `kotlin-maven-plugin` ao `pom.xml` raiz em `<pluginManagement>` com configuração `compile` e `test-compile`
4. Criar diretório `declaracoes-gov-core-kotlin/`
5. Criar `declaracoes-gov-core-kotlin/pom.xml`:
   - `parent`: `declaracoes-gov-core-parent`
   - `artifactId`: `declaracoes-gov-core-kotlin`
   - `packaging`: `jar`
   - Dependências: `declaracoes-gov-core-domain`, `declaracoes-gov-core-format`, `kotlin-stdlib`
   - `jackson-module-kotlin` com scope opcional
   - `kotlin-maven-plugin` com goals `compile` e `test-compile`
   - JaCoCo gate: linha ≥ 90%, branch ≥ 90%
6. Criar `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/` (diretório)
7. Criar `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/` (diretório)
8. Adicionar `<module>declaracoes-gov-core-kotlin</module>` ao `pom.xml` raiz

**Arquivos:**
- `pom.xml` (raiz) — adicionar `kotlin.version`, `kotlin-maven-plugin` em `pluginManagement`, novo módulo em `<modules>`
- `declaracoes-gov-core-kotlin/pom.xml` — criar do zero
- `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/.gitkeep`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-kotlin` compila sem erros (módulo vazio)
- `mvn dependency:tree -pl declaracoes-gov-core-kotlin | grep spring` retorna vazio (sem Spring transitivo)

**Edge cases:**
- Garantir que `kotlin-stdlib` não conflita com versões de Kotlin transitivas de outros módulos
- O módulo Kotlin deve ser o **último** na ordem de módulos do reactor (após `xml`) pois depende de todos os outros

---

### T002 — Extension functions para tipos de domínio

**Objetivo:** Criar wrappers idiomáticos Kotlin sobre os value objects do módulo `domain`, tornando o uso em Kotlin mais natural e null-safe.

**Passos:**
1. Criar `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/DomainExtensions.kt`
2. Implementar:
   - `fun String.toCnpj(): Cnpj = Cnpj.of(this)` — chama factory Java; propaga `InvalidDocumentException`
   - `fun String.toCpf(): Cpf = Cpf.of(this)` — idem para CPF
   - `fun String?.digitsOnly(): String = if (this == null) "" else GovTextNormalizer.digitsOnly(this)` — null-safe wrapper
   - `fun String?.emptyIfNull(): String = this ?: ""` — atalho idiomático
   - `fun Cnpj?.isValid(): Boolean = this != null` — verificação de nulidade semântica
3. KDoc em português para cada função
4. Garantir que as functions são top-level (não em objeto companion ou classe)

**Arquivos:**
- `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/DomainExtensions.kt`

**Validação:**
- `"12345678000195".toCnpj()` não lança exceção para CNPJ válido
- `null.digitsOnly()` retorna `""` (sem NPE)
- `"12.345.678/0001-95".toCnpj().valor == "12345678000195"` (valor normalizado)

**Edge cases:**
- `"invalido".toCnpj()` propaga `InvalidDocumentException` — documentar no KDoc
- Verificar se `Cnpj.valor` é o campo com o valor normalizado ou se o nome é diferente

---

### T003 — Extension functions de data/tempo

**Objetivo:** Criar wrappers idiomáticos Kotlin para conversões de data e período frequentes nos projetos consumidores.

**Passos:**
1. Criar `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/DateExtensions.kt`
2. Implementar:
   - `fun LocalDate.toYearMonth(): YearMonth = YearMonth.of(this.year, this.month)`
   - `fun YearMonth.toPeriodo(): Int = this.year * 100 + this.monthValue` — formato yyyyMM como Int
   - `fun LocalDateTime.toUtc(): LocalDateTime = this.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()`
   - `fun LocalDateTime.toBrasilia(): LocalDateTime = this.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()`
   - `infix fun YearMonth.isBeforeOrEqual(other: YearMonth): Boolean = !this.isAfter(other)`
   - `infix fun YearMonth.isAfterOrEqual(other: YearMonth): Boolean = !this.isBefore(other)`
3. KDoc em português para cada função; documentar timezone para `toUtc` e `toBrasilia`

**Arquivos:**
- `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/DateExtensions.kt`

**Validação:**
- `LocalDate.of(2025, 1, 15).toYearMonth() == YearMonth.of(2025, 1)`
- `YearMonth.of(2025, 6).toPeriodo() == 202506`
- `YearMonth.of(2025, 1) isBeforeOrEqual YearMonth.of(2025, 6)` retorna `true`

**Edge cases:**
- `toUtc()` e `toBrasilia()` dependem do `ZoneId` do sistema no servidor — documentar que a conversão é baseada no timezone da JVM
- Não usar `ZoneId.of("Brazil/East")` que é deprecated — usar `"America/Sao_Paulo"`

---

### T004 — Extension functions de coleção e null-safety

**Objetivo:** Criar wrappers idiomáticos Kotlin sobre os utilitários de coleção e conversões null-safe da lib, complementando as stdlib Kotlin.

**Passos:**
1. Criar `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/CollectionExtensions.kt`
2. Implementar:
   - `fun <T> List<T>?.orNull(): List<T>? = if (this.isNullOrEmpty()) null else this`
   - `fun <T> List<T>?.getFirst(): T? = this?.firstOrNull()`
   - `fun <T> Optional<T>.orNull(): T? = this.orElse(null)`
   - `fun <T : Any> T?.whenNullThrow(lazyMessage: () -> String): T = this ?: throw IllegalArgumentException(lazyMessage())`
3. KDoc em português para cada função

**Arquivos:**
- `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/CollectionExtensions.kt`

**Validação:**
- `emptyList<String>().orNull() == null`
- `listOf(1, 2, 3).orNull()!!.size == 3`
- `listOf(42).getFirst() == 42`
- `null.whenNullThrow { "obrigatório" }` lança `IllegalArgumentException` com mensagem "obrigatório"
- `Optional.of("valor").orNull() == "valor"`

**Edge cases:**
- `Optional.empty<String>().orNull() == null` (sem NPE)
- `null.whenNullThrow {}` — lambda deve ser chamada apenas quando nulo

---

### T005 — Extension functions JSON (opcional, jackson-module-kotlin)

**Objetivo:** Fornecer wrappers null-safe de serialização/deserialização JSON usando Jackson, para uso opcional em projetos que já têm Jackson como dependência.

**Passos:**
1. Criar `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/JsonExtensions.kt`
2. Verificar se `jackson-module-kotlin` está declarado como opcional no `pom.xml` do módulo kotlin
3. Implementar:
   - `private val objectMapper: ObjectMapper = ObjectMapper().registerKotlinModule()` — singleton interno
   - `fun Any.toJsonOrNull(): String? = try { objectMapper.writeValueAsString(this) } catch (e: JsonProcessingException) { null }`
   - `inline fun <reified T> String.fromJsonOrNull(): T? = try { objectMapper.readValue<T>(this) } catch (e: JsonProcessingException) { null }`
4. KDoc em português; documentar que o `ObjectMapper` interno é para conveniência e que consumidores com configuração específica devem usar seu próprio mapper

**Arquivos:**
- `declaracoes-gov-core-kotlin/src/main/kotlin/br/uem/npd/govcore/ext/JsonExtensions.kt`

**Validação:**
- `mapOf("key" to "value").toJsonOrNull()` retorna `"""{"key":"value"}"""`
- `"""{"name":"test"}""".fromJsonOrNull<Map<String, String>>()!!["name"] == "test"`
- `"json inválido".fromJsonOrNull<Map<String, String>>() == null` (sem exceção)

**Edge cases:**
- Thread-safety: `ObjectMapper` é thread-safe após configuração — documentar
- `null.toJsonOrNull()` retorna `"null"` (JSON null) ou `null` Kotlin — documentar decisão
- Se `jackson-module-kotlin` não estiver no classpath: compilar com `compileOnly`/`optional` e falhar graciosamente

---

### T006 — Testes para todas as extensions Kotlin

**Objetivo:** Garantir cobertura ≥ 90% de todas as extensions usando JUnit 4 em Kotlin.

**Passos:**
1. Criar `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/DomainExtensionsTest.kt`
2. Criar `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/DateExtensionsTest.kt`
3. Criar `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/CollectionExtensionsTest.kt`
4. Criar `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/JsonExtensionsTest.kt`
5. Usar JUnit 4 (`@Test`, `Assert.*`) — compatível com Kotlin sem dependências extras
6. Cobrir: null inputs (sem NPE), round-trips, valores inválidos que propagam exceção

**Arquivos:**
- `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/DomainExtensionsTest.kt`
- `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/DateExtensionsTest.kt`
- `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/CollectionExtensionsTest.kt`
- `declaracoes-gov-core-kotlin/src/test/kotlin/br/uem/npd/govcore/ext/JsonExtensionsTest.kt`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-kotlin` verde
- JaCoCo ≥ 90% linha + branch no módulo `kotlin`
- `mvn dependency:tree -pl declaracoes-gov-core-kotlin | grep spring` retorna vazio

**Edge cases:**
- Round-trip CNPJ: `"12345678000195".toCnpj().valor == "12345678000195"` (ou conforme o campo real de `Cnpj`)
- Data: `YearMonth.of(2025, 12).toPeriodo() == 202512` (não `2025012`)
- Collections: `listOf<String>().orNull() == null` vs `listOf("a").orNull() != null`

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP09
```

## Activity Log

- 2026-04-20T21:58:28Z – unknown – lane=doing – Moved to doing
