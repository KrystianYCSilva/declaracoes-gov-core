---
work_package_id: WP07
title: GovDateUtils + VigenciaConverter + GovCollections + GovFileUtils
lane: "done"
dependencies: []
created_at: '2026-04-20T21:13:24.169349+00:00'
subtasks:
- T001: Expandir `GovDateUtils` com conversões de data/período
- T002: Criar `VigenciaConverter<P, V>`
- T003: Criar `VigenciaValidator`
- T004: Criar `GovCollectionUtils`
- T005: Criar `GovFileUtils`
- T006: Testes para todas as novas classes
- T007: Verificar BOM para novos módulos se necessário
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:34:59.359798+00:00"
loops_doing_to_done: "1"
ended_at: "2026-04-20T21:47:32.147642+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP07 — GovDateUtils + VigenciaConverter + GovCollections + GovFileUtils

## Context

O módulo `format` precisa de quatro grupos de utilitários desacoplados de Spring: (1) `GovDateUtils` expandido com métodos de conversão de data/período extraídos de `DateComponent` do eSocial; (2) `VigenciaConverter` e `VigenciaValidator` que existem em múltiplos projetos com `@Component` do Spring — precisam ser desacoplados para a lib; (3) `GovCollectionUtils` com operações null-safe de coleção duplicadas em `contabilizei-back-core` e `obrigacoes-service-reinf`; (4) `GovFileUtils` com detecção de MIME por magic numbers do `plataforma-fopag-worker`. Todos dependem de WP03 (interfaces `Periodico`/`Vigencia`) e WP06 (contexto de build do format).

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `format` ≥ 90% linha + ≥ 90% branch
- Módulo `format`: pode depender de `domain` + Jackson (opcional)
- Zero dependências externas além do JDK para `GovFileUtils` e `GovCollectionUtils`
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Expandir `GovDateUtils` com conversões de data/período

**Objetivo:** Adicionar métodos de conversão de data e período extraídos de `DateComponent` do `obrigacoes-service-esocial` (removendo `@Component`), expandindo a classe existente no módulo `format`.

**Passos:**
1. Verificar se `GovDateUtils` já existe em `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/` ou se é `XmlDates`
2. Localizar o arquivo correto e adicionar (ou criar `GovDateUtils.java` se não existir):
   - `convertToLocalDateTime(Date date): LocalDateTime` — converte `java.util.Date` para `LocalDateTime` usando `ZoneId` padrão
   - `parsePeriodoToLocalDateTime(Integer yyyyMM): LocalDateTime` — converte período yyyyMM para o primeiro dia do mês como `LocalDateTime` meia-noite
   - `getStartMinuteDateForQuery(Date date): Date` — retorna a data com hora definida para `00:00:00.000`
   - `getLastMinuteDateForQuery(Date date): Date` — retorna a data com hora definida para `23:59:59.999`
3. Remover qualquer anotação `@Component` se existir nos métodos originais
4. Javadoc em português para cada método; documentar qual `ZoneId` é usado em `convertToLocalDateTime`

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateUtils.java` — criar ou expandir

**Validação:**
- `parsePeriodoToLocalDateTime(202501)` retorna `LocalDateTime.of(2025, 1, 1, 0, 0)`
- `getStartMinuteDateForQuery(date).getHours() == 0` (início do dia)
- `getLastMinuteDateForQuery(date)` hora == 23:59:59.999

**Edge cases:**
- `parsePeriodoToLocalDateTime(null)` lança `IllegalArgumentException`
- `convertToLocalDateTime(null)` retorna `null` ou lança exceção — documentar decisão
- Fuso horário: documentar se usa `ZoneId.systemDefault()` ou `ZoneId.of("America/Sao_Paulo")`

---

### T002 — Criar `VigenciaConverter<P extends Periodico, V extends Vigencia>`

**Objetivo:** Extrair a lógica de conversão de listas de periódicos em vigências dos projetos de origem, desacoplando de Spring e tornando o converter genérico e reusável.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaConverter.java`
2. Declarar como `public final class VigenciaConverter<P extends Periodico, V extends Vigencia>`
3. Definir interface funcional interna ou aceitar `java.util.function.Function<P, V>` como parâmetro (Java 8)
4. Método principal: `List<V> transformarPeriodosEmVigencia(List<P> periodicos, Function<P, V> converter)`
   - Ordenar periódicos por `getPeriodo()` crescente
   - Para cada período consecutivo, chamar o `converter` para criar a vigência com início/fim adequados
   - Lógica de agrupamento: sequências contínuas de períodos formam uma vigência
5. Remover qualquer `@Component` do código original
6. Javadoc em português; incluir exemplo de uso

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaConverter.java`

**Validação:**
- Lista de períodos [202501, 202502, 202503] gera uma vigência contínua de jan a mar/2025
- Lista de períodos [202501, 202503] (gap em fev) gera duas vigências separadas
- Lista vazia retorna lista vazia (não lança NPE)

**Edge cases:**
- Lista `null` deve lançar `IllegalArgumentException`
- Periódicos com `getPeriodo() == null` devem ser tratados (ignorar ou lançar exceção — documentar)
- Ordenação deve ser estável para não alterar a lógica de agrupamento

---

### T003 — Criar `VigenciaValidator`

**Objetivo:** Validar a consistência cronológica de uma lista de `Periodico`, detectando períodos faltantes e repetidos, usando as exceções criadas no WP04.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaValidator.java`
2. Declarar como `public final class VigenciaValidator`
3. Método: `void validar(List<? extends Periodico> periodicos)` — lança `PeriodoFaltanteException` ou `PeriodoRepetidoException` do pacote de exceções do WP04
4. Lógica:
   - Verificar duplicatas: se mesmo período aparece mais de uma vez → `PeriodoRepetidoException(periodo)`
   - Verificar continuidade: se há gap entre períodos consecutivos → `PeriodoFaltanteException(periodoEsperado)`
5. Remover qualquer `@Component` do código original
6. Javadoc em português

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaValidator.java`

**Validação:**
- [202501, 202503] → lança `PeriodoFaltanteException(202502)`
- [202501, 202501, 202502] → lança `PeriodoRepetidoException(202501)`
- [202501, 202502, 202503] → não lança exceção

**Edge cases:**
- Lista nula ou vazia: não lança exceção (ou lança `IllegalArgumentException` — documentar)
- Lista com um único elemento: considera válido (sem gap verificável)
- Periódicos fora de ordem: ordenar antes de validar ou exigir ordem pré-definida — documentar

---

### T004 — Criar `GovCollectionUtils`

**Objetivo:** Centralizar operações null-safe de coleção duplicadas em `contabilizei-back-core` e `obrigacoes-service-reinf`.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovCollectionUtils.java`
2. Declarar como `public final class GovCollectionUtils` com construtor privado
3. Implementar:
   - `isNullOrEmpty(List<?> list): boolean` — `null` ou vazia retorna `true`
   - `isNullOrEmpty(Object[] array): boolean` — `null` ou comprimento zero retorna `true`
   - `orNull(List<T> list): List<T>` — retorna `null` se lista for nula ou vazia, senão retorna a lista
   - `getFirst(List<T> list): Optional<T>` — retorna `Optional.ofNullable(list.get(0))` ou `Optional.empty()` se vazia/nula
4. Javadoc em português para cada método

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovCollectionUtils.java`

**Validação:**
- `GovCollectionUtils.isNullOrEmpty(null)` retorna `true`
- `GovCollectionUtils.isNullOrEmpty(Collections.emptyList())` retorna `true`
- `GovCollectionUtils.getFirst(Arrays.asList(1, 2, 3)).get() == 1`
- `GovCollectionUtils.orNull(Collections.emptyList()) == null`

**Edge cases:**
- `getFirst(null)` retorna `Optional.empty()` (não lança NPE)
- `orNull` com lista de um elemento: retorna a lista (não nulo)

---

### T005 — Criar `GovFileUtils`

**Objetivo:** Centralizar detecção de tipo de arquivo por magic numbers e proteção contra upload de arquivos maliciosos, extraído de `plataforma-fopag-worker`, sem dependências externas.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovFileUtils.java`
2. Declarar como `public final class GovFileUtils` com construtor privado
3. Implementar:
   - `getMagicNumbers(byte[] fileBytes): byte[]` — extrai os primeiros 8 bytes (ou tamanho do array, o que for menor)
   - `getMimeType(byte[] fileBytes): String` — detecta MIME type pelos magic numbers (suporte mínimo: PDF `%PDF`, ZIP `PK`, PNG, JPEG, XML `<?xml`)
   - `throwIfMalicious(byte[] fileBytes)` — verifica magic numbers contra uma lista negra de tipos não permitidos; lança `GovCoreException` (ou `IllegalArgumentException`) se malicioso
4. Magic numbers suportados no mínimo (extrair da implementação original de `plataforma-fopag-worker`):
   - PDF: `25 50 44 46` (%PDF)
   - ZIP: `50 4B 03 04` (PK)
   - PNG: `89 50 4E 47`
   - JPEG: `FF D8 FF`
   - XML: verificar se inicia com `3C 3F 78 6D 6C` ou BOM
5. Javadoc em português; documentar quais tipos são bloqueados por `throwIfMalicious`

**Arquivos:**
- `declaracoes-gov-core-format/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovFileUtils.java`

**Validação:**
- `getMimeType(pdfBytes)` retorna `"application/pdf"`
- `throwIfMalicious(pdfBytes)` não lança exceção para PDF
- `getMagicNumbers(new byte[]{1,2,3,4,5,6,7,8,9})` retorna array de 8 bytes

**Edge cases:**
- `fileBytes` com menos de 4 bytes: `getMimeType` retorna `"application/octet-stream"` (tipo genérico)
- `fileBytes == null`: lança `IllegalArgumentException`
- Array vazio: `getMagicNumbers` retorna array vazio

---

### T006 — Testes para todas as novas classes

**Objetivo:** Garantir cobertura ≥ 90% em todos os artefatos do WP07.

**Passos:**
1. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateUtilsTest.java`
2. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaConverterTest.java`
3. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaValidatorTest.java`
4. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovCollectionUtilsTest.java`
5. Criar `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovFileUtilsTest.java`
6. Para `VigenciaValidator`: testar gap, duplicata e sequência válida
7. Para `GovFileUtils`: usar arrays de magic numbers hardcoded nos testes (não arquivos reais)

**Arquivos:**
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovDateUtilsTest.java`
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaConverterTest.java`
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaValidatorTest.java`
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovCollectionUtilsTest.java`
- `declaracoes-gov-core-format/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovFileUtilsTest.java`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-format` verde
- JaCoCo ≥ 90% linha + branch

**Edge cases:**
- `VigenciaConverterTest`: lista de 1 elemento e lista com múltiplos gaps
- `GovFileUtilsTest`: arquivo com array vazio, array com menos de 4 bytes

---

### T007 — Verificar BOM para novos módulos se necessário

**Objetivo:** Garantir que o `declaracoes-gov-core-bom/pom.xml` está alinhado se este WP introduzir novos artefatos que precisem ser declarados no BOM.

**Passos:**
1. Verificar se as classes adicionadas são no módulo `format` existente (não novo módulo) — se sim, BOM não precisa mudar
2. Se algum artefato novo foi introduzido como módulo separado, adicionar entrada no BOM
3. Executar `mvn -B -q verify` do reactor root para confirmar

**Arquivos:**
- `declaracoes-gov-core-bom/pom.xml` — verificar (somente leitura se não mudar)

**Validação:**
- `mvn -B -q verify` verde no reactor completo

**Edge cases:**
- Se apenas classes foram adicionadas a módulos existentes, nenhuma mudança no BOM é necessária

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP07
```

## Activity Log

- 2026-04-20T21:34:59Z – unknown – lane=doing – Moved to doing
- 2026-04-20T21:47:32Z – unknown – lane=done – Moved to done
