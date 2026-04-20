---
work_package_id: WP05
title: Tipos de Domínio e Contratos
lane: "doing"
dependencies: []
created_at: '2026-04-20T21:13:24.141434+00:00'
subtasks:
- T001: Criar interface `CallbackAsync<O>`
- T002: Criar `CertificadoDTO`
- T003: Criar `Filter` e `FilterCollection`
- T004: Criar enums `ActiveProfile` e `DateFormatType`
- T005: Criar annotations `@Description`, `@IgnoreElement` e enum `FieldType`
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:47:36.223813+00:00"
---

# WP05 — Tipos de Domínio e Contratos

## Context

Contratos genéricos e tipos de suporte (`CallbackAsync`, `CertificadoDTO`, `Filter`, enums de perfil/formato) estão duplicados em `microservico-ecd`, `microservico-ecf`, `contabilizei-framework-datastore`, `contabilizei-back-core` e `contabilizei-framework-metadata`. A ausência desses tipos na lib força cada projeto a recriar sua própria abstração, resultando em incompatibilidades de interface quando os serviços precisam interoperar. Este WP consolida todos esses contratos no módulo `domain` sem introduzir dependências externas.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `domain` ≥ 90% linha + ≥ 90% branch
- Módulo `domain` = zero dependências externas (JDK only)
- `CertificadoDTO` deve implementar `Serializable` com `serialVersionUID = 1L`
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Criar interface `CallbackAsync<O>`

**Objetivo:** Definir o contrato de callback assíncrono genérico que aparece em múltiplos projetos para comunicação de resultado de operações não-bloqueantes, sem dependência de framework.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/util/CallbackAsync.java`
2. Declarar como `public interface CallbackAsync<O>`
3. Métodos:
   - `void onSuccess(O result)` — chamado quando a operação assíncrona conclui com sucesso
   - `void onFailure(O result, Throwable cause)` — chamado quando a operação falha; `result` pode ser parcial ou nulo
4. Javadoc em português: "Contrato de callback para operações assíncronas. Não introduz dependência de framework; o controle de execução assíncrona é responsabilidade do consumidor."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/util/CallbackAsync.java`

**Validação:**
- Interface compila sem dependências externas
- Pode ser usada como tipo genérico em Java 8

**Edge cases:**
- Parâmetro `result` em `onFailure` pode ser nulo — documentar no Javadoc

---

### T002 — Criar `CertificadoDTO`

**Objetivo:** Consolidar o DTO de certificado digital (base64 + senha) que está duplicado em `microservico-ecd` e `microservico-ecf` em uma classe compartilhada, imutável e serializável.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/CertificadoDTO.java`
2. Declarar como `public final class CertificadoDTO implements Serializable`
3. Campos: `private final String certificadoBase64`, `private final String senha`
4. Construtor privado
5. Factory: `public static CertificadoDTO of(String certificadoBase64, String senha)`
6. Getters: `getCertificadoBase64()`, `getSenha()`
7. `private static final long serialVersionUID = 1L`
8. `toString()` deve **omitir** o campo `senha` para evitar vazamento em logs — retornar apenas "CertificadoDTO{certificadoBase64=[present/null]}"
9. Javadoc em português

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/CertificadoDTO.java`

**Validação:**
- `CertificadoDTO.of("base64...", "senha123").getSenha()` retorna `"senha123"`
- `toString()` não contém a senha
- `implements Serializable` verificado por `instanceof Serializable`

**Edge cases:**
- `certificadoBase64 == null` deve ser aceito ou rejeitado? Documentar e testar a decisão
- `senha == null` — suporte a certificados sem senha (ex: A3 hardware) — documentar

---

### T003 — Criar `Filter` e `FilterCollection`

**Objetivo:** Criar estrutura de filtro genérica (name/value) com coleção fluente encontrada em `contabilizei-framework-datastore` e `contabilizei-back-core`, para uso em queries de repositório sem dependência de framework.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/util/Filter.java`:
   - `public final class Filter`
   - Campos: `private final String name`, `private final Object value`
   - Construtor privado; factory `Filter.of(String name, Object value)`
   - Getters `getName()`, `getValue()`
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/util/FilterCollection.java`:
   - `public final class FilterCollection implements Iterable<Filter>`
   - Backing: `private final List<Filter> filters` internamente
   - Método fluente: `public FilterCollection add(String name, Object value)` — retorna `this`
   - `iterator()` — delega à lista interna
   - Factory: `FilterCollection.empty()`
   - `size(): int`, `isEmpty(): boolean`
3. Javadoc em português para ambas as classes

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/util/Filter.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/util/FilterCollection.java`

**Validação:**
- `FilterCollection.empty().add("cnpj", "123").add("periodo", 202501).size() == 2`
- `for (Filter f : collection)` funciona (Iterable)

**Edge cases:**
- `FilterCollection` deve ser segura para uso em single-thread (não precisa ser thread-safe — documentar)
- `Filter.value` pode ser `null` — documentar comportamento

---

### T004 — Criar enums `ActiveProfile` e `DateFormatType`

**Objetivo:** Centralizar enums de perfil de ambiente e formato de data que estão espalhados em múltiplos projetos.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/table/ActiveProfile.java`:
   - `public enum ActiveProfile { PROD, HOM, DEV }`
   - Método helper: `public static ActiveProfile fromString(String value)` — case-insensitive
   - Javadoc em português: "Perfis de ambiente de execução. Usar para decisões de configuração sem dependência de framework."
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/table/DateFormatType.java`:
   - `public enum DateFormatType { YYYY_MM_DD, YYYYMM }`
   - Campo `private final String pattern` — ex: `"yyyy-MM-dd"`, `"yyyyMM"`
   - Getter `getPattern(): String`
   - Javadoc: "Tipos de formato de data utilizados em conversões e serializações."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/table/ActiveProfile.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/table/DateFormatType.java`

**Validação:**
- `ActiveProfile.fromString("prod") == ActiveProfile.PROD`
- `DateFormatType.YYYY_MM_DD.getPattern()` retorna `"yyyy-MM-dd"`

**Edge cases:**
- `ActiveProfile.fromString("UNKNOWN")` deve lançar `IllegalArgumentException` com mensagem descritiva
- Verificar se há outros perfis nos projetos de origem (ex: `TEST`, `LOCAL`) e adicioná-los se encontrados

---

### T005 — Criar annotations `@Description`, `@IgnoreElement` e enum `FieldType`

**Objetivo:** Fornecer annotations de metadata de campos de layout encontradas em `contabilizei-framework-metadata`, necessárias para documentação e processamento de campos de registros SPED/eSocial sem dependência de framework.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout/Description.java`:
   - `@interface Description` com `@Retention(RetentionPolicy.RUNTIME)` e `@Target({ElementType.FIELD, ElementType.METHOD})`
   - Atributo: `String value() default ""`
   - Javadoc em português
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout/IgnoreElement.java`:
   - `@interface IgnoreElement` com `@Retention(RetentionPolicy.RUNTIME)` e `@Target({ElementType.FIELD})`
   - Sem atributos (marcador)
   - Javadoc: "Marca um campo para ser ignorado em serialização/deserialização de layout."
3. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout/FieldType.java`:
   - `public enum FieldType { STRING, INTEGER, DATE, BOOLEAN, DECIMAL, LONG, TIME, TIMESTAMP }`
   - Javadoc em português
4. Criar testes que verificam `@Retention` e `@Target` usando reflection

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout/Description.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout/IgnoreElement.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/model/layout/FieldType.java`
- `declaracoes-gov-core-domain/src/test/java/br/uem/npd/govcore/model/layout/LayoutAnnotationsTest.java`

**Validação:**
- `Description.class.getAnnotation(Retention.class).value() == RetentionPolicy.RUNTIME`
- Campo anotado com `@Description("Número de identificação")` tem o valor acessível via reflection em runtime
- `IgnoreElement` é marcador (sem atributos)

**Edge cases:**
- Verificar se `@Target` inclui todos os elementos necessários (FIELD, METHOD para Description; FIELD para IgnoreElement)
- JaCoCo pode não cobrir anotações — verificar se gate é satisfeito com testes de reflection

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP05
```

## Activity Log

- 2026-04-20T21:47:36Z – unknown – lane=doing – Moved to doing
