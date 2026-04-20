---
work_package_id: WP06
title: GovZipUtils + GovStringUtils + GovNumberUtils
lane: "done"
dependencies: []
created_at: '2026-04-20T21:13:24.155220+00:00'
subtasks:
- T001: Criar `GovZipUtils`
- T002: Expandir `GovTextNormalizer` (ou criar `GovStringUtils`)
- T003: Criar `GovNumberUtils`
- T004: Criar `GovBigDecimalConstants`
- T005: Testes para `GovZipUtils` com fixtures cross-projeto
- T006: Testes para `GovStringUtils`/`GovNumberUtils`
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:16:52.767458+00:00"
loops_doing_to_for_review: "1"
for_review_started_at: "2026-04-20T21:28:27.956497+00:00"
loops_for_review_to_done: "1"
ended_at: "2026-04-20T21:28:32.492471+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP06 — GovZipUtils + GovStringUtils + GovNumberUtils

## Context

Três categorias de utilitários estão fortemente duplicadas no ecossistema: (1) compressão GZIP com Base64 — **4 cópias idênticas** em `microservico-ecd`, `microservico-ecf`, `efd-piscofins` e `contabilizei-back-core`; (2) manipulação de strings — ~15 métodos espalhados em `contabilizei-back-core`, `contabilizei-framework` e `obrigacoes-service-esocial`; (3) operações null-safe de BigDecimal — 11 métodos em `contabilizei-back-core` e `contabilizei-framework`. Este WP consolida tudo no módulo `format`, verificando primeiro se `GovTextNormalizer` já existe para expandir em vez de criar classe paralela.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `format` ≥ 90% linha + ≥ 90% branch
- Módulo `format`: pode depender de `domain` + Jackson (opcional, já declarado)
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Criar `GovZipUtils`

**Objetivo:** Consolidar as 4 cópias idênticas de compressão GZIP+Base64 em uma implementação canônica no módulo `format`, com comportamento verificado por fixtures dos projetos originais.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovZipUtils.java`
2. Declarar como `public final class GovZipUtils` com construtor privado
3. Implementar `compressAndEncodeBase64(String content): String`:
   - Converter string para bytes (UTF-8)
   - Comprimir com `java.util.zip.GZIPOutputStream`
   - Encodar resultado com `java.util.Base64`
   - Retornar string Base64
4. Implementar `decompress(byte[] compressed): byte[]`:
   - Decodificar Base64
   - Descomprimir com `java.util.zip.GZIPInputStream`
   - Retornar bytes decomprimidos
5. Implementar `zipToString(Object obj): String`:
   - Serializar objeto como bytes via `ObjectOutputStream`
   - Comprimir e encodar com `compressAndEncodeBase64`
   - Lançar `IllegalArgumentException` se objeto não for `Serializable`
6. Tratar `IOException` internamente, encapsulando em `GovCoreException` ou `RuntimeException` documentada
7. Javadoc em português para cada método

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovZipUtils.java`

**Validação:**
- Round-trip: `decompress(Base64.decode(compressAndEncodeBase64("teste")))` retorna bytes de "teste"
- Comportamento idêntico às 4 implementações originais (verificado via fixtures)

**Edge cases:**
- String vazia `""` deve ser comprimida sem erro
- `null` deve lançar `IllegalArgumentException` com mensagem clara
- `zipToString` com objeto não-Serializable deve lançar exceção descritiva
- Garantir que `GZIPOutputStream`/`GZIPInputStream` são fechados corretamente (try-with-resources)

---

### T002 — Expandir `GovTextNormalizer` (ou criar `GovStringUtils`)

**Objetivo:** Adicionar ~10 métodos de manipulação de strings encontrados em projetos de origem sem criar classe paralela se `GovTextNormalizer` já existir com métodos compatíveis.

**Passos:**
1. **Primeiro:** Verificar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovTextNormalizer.java` — listar métodos existentes
2. **Se métodos existentes são compatíveis:** adicionar novos métodos à classe existente
3. **Se não existir ou incompatível:** criar `GovStringUtils.java` com todos os métodos
4. Adicionar os seguintes métodos (se não existirem):
   - `emptyIfNull(String s): String` — retorna `""` se nulo
   - `defaultIfNull(String s, String defaultValue): String`
   - `isNullOrEmpty(String s): boolean`
   - `lpad(String s, int length, char padChar): String`
   - `rpad(String s, int length, char padChar): String`
   - `padLeftZeros(String s, int length): String` — atalho para `lpad(s, length, '0')`
   - `truncate(String s, int maxLength): String` — retorna `s.substring(0, maxLength)` se maior
   - `removeMascara(String s): String` — remove `.`, `/`, `-`, `(`, `)`, ` `
   - `somenteNumeros(String s): String` — mantém apenas dígitos `[0-9]`
5. Javadoc em português para cada método novo

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovTextNormalizer.java` — expandir (preferencial)
- OU `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovStringUtils.java` — criar se necessário

**Validação:**
- `emptyIfNull(null)` retorna `""`
- `padLeftZeros("123", 8)` retorna `"00000123"`
- `somenteNumeros("123.456.789-00")` retorna `"12345678900"`
- `truncate("abcdefgh", 5)` retorna `"abcde"`

**Edge cases:**
- `lpad` quando string já é maior que `length`: retornar string original sem truncar (ou documentar truncamento)
- `removeMascara(null)` retorna `null` ou `""` — documentar decisão
- Verificar que `digitsOnly` existente em `GovTextNormalizer` é idêntico a `somenteNumeros` — se sim, apenas adicionar alias

---

### T003 — Criar `GovNumberUtils`

**Objetivo:** Centralizar operações null-safe de `BigDecimal` encontradas em 11 métodos distribuídos entre `contabilizei-back-core` e `contabilizei-framework`, evitando `NullPointerException` em cálculos financeiros.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovNumberUtils.java`
2. Declarar como `public final class GovNumberUtils` com construtor privado
3. Implementar métodos null-safe (tratar `null` como `BigDecimal.ZERO`):
   - `add(BigDecimal a, BigDecimal b): BigDecimal`
   - `subtract(BigDecimal a, BigDecimal b): BigDecimal`
   - `multiply(BigDecimal a, BigDecimal b): BigDecimal`
   - `divide(BigDecimal a, BigDecimal b, int scale, RoundingMode mode): BigDecimal` — lançar `ArithmeticException` se `b` é zero após null-check
4. Implementar métodos de comparação null-safe (tratar `null` como `BigDecimal.ZERO`):
   - `isZero(BigDecimal value): boolean`
   - `isPositive(BigDecimal value): boolean`
   - `isNegative(BigDecimal value): boolean`
   - `isGreaterThan(BigDecimal value, BigDecimal other): boolean`
   - `max(BigDecimal a, BigDecimal b): BigDecimal`
5. Implementar cálculo percentual:
   - `percentage(BigDecimal value, BigDecimal total): BigDecimal` — retorna `(value / total) * 100` com escala razoável
6. Javadoc em português para cada método

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovNumberUtils.java`

**Validação:**
- `add(null, BigDecimal.ONE)` retorna `BigDecimal.ONE`
- `isZero(null)` retorna `true`
- `divide(BigDecimal.TEN, BigDecimal.ZERO, 2, HALF_UP)` lança `ArithmeticException`

**Edge cases:**
- `percentage(null, BigDecimal.ZERO)` — ambos nulos e total zero — documentar comportamento
- Escala do resultado de `divide` e `percentage` deve ser explicitamente controlada — não usar escala ilimitada (evita `ArithmeticException: Non-terminating decimal expansion`)

---

### T004 — Criar `GovBigDecimalConstants`

**Objetivo:** Fornecer constantes de `BigDecimal` frequentemente usadas em cálculos financeiros para evitar instanciação repetida.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovBigDecimalConstants.java`
2. Declarar como `public final class GovBigDecimalConstants` com construtor privado
3. Definir constantes:
   - `public static final BigDecimal CEM = BigDecimal.valueOf(100L)`
   - `public static final BigDecimal ZERO_DECIMAL = BigDecimal.ZERO`
4. Verificar nos projetos de origem se há outras constantes comuns (ex: `DOZE`, `TREZE` para INSS) — adicionar se encontradas
5. Javadoc em português

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovBigDecimalConstants.java`

**Validação:**
- `GovBigDecimalConstants.CEM.compareTo(BigDecimal.valueOf(100)) == 0`
- Constantes são `final` e imutáveis (BigDecimal é imutável por natureza)

**Edge cases:**
- Usar `BigDecimal.valueOf(100L)` em vez de `new BigDecimal("100")` para consistência de escala

---

### T005 — Testes para `GovZipUtils`

**Objetivo:** Garantir que o comportamento de `GovZipUtils` é idêntico ao das 4 implementações originais usando fixtures reais.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovZipUtilsTest.java`
2. Teste de round-trip: `decompress(Base64.decode(compressAndEncodeBase64(input)))` retorna bytes de `input`
3. Teste de fixture cross-projeto: usar string payload de teste idêntica à usada nos testes originais dos 4 projetos — verificar que o output Base64 é idêntico
4. Teste de nulo: `compressAndEncodeBase64(null)` lança exceção esperada
5. Teste de string vazia: `compressAndEncodeBase64("")` não lança exceção
6. Teste de `zipToString`: objeto `Serializable` é comprimido e string resultante pode ser decomprimida

**Arquivos:**
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovZipUtilsTest.java`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-format` verde
- JaCoCo ≥ 90% linha + branch

**Edge cases:**
- Testar com payload grande (>1MB) para verificar comportamento de memória
- Verificar que streams são fechados mesmo em caso de exceção

---

### T006 — Testes para `GovStringUtils`/`GovNumberUtils`

**Objetivo:** Garantir cobertura ≥ 90% nos métodos de string e número com foco em entradas nulas, vazias e de fronteira.

**Passos:**
1. Criar/expandir `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovTextNormalizerTest.java` (ou `GovStringUtilsTest.java`)
2. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovNumberUtilsTest.java`
3. Para cada método: testar entrada nula, entrada vazia, entrada válida e caso de fronteira
4. Para `GovNumberUtils`: testar todos os 11 métodos com pelo menos 3 cenários cada

**Arquivos:**
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovTextNormalizerTest.java`
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovNumberUtilsTest.java`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-format` verde
- JaCoCo ≥ 90% linha + branch no módulo `format`

**Edge cases:**
- `GovNumberUtils.divide` com divisor zero
- `GovNumberUtils.percentage` com total zero
- `padLeftZeros` com string já maior que o tamanho alvo

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP06
```

## Activity Log

- 2026-04-20T21:16:52Z – unknown – lane=doing – Moved to doing
- 2026-04-20T21:28:27Z – unknown – lane=for_review – Moved to for_review
- 2026-04-20T21:28:32Z – unknown – lane=done – Moved to done
