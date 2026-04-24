---
work_package_id: WP03
title: CPF + Domínio Período/Vigência
lane: "done"
dependencies: []
created_at: '2026-04-20T21:13:24.115716+00:00'
subtasks:
- T001: Integrar validação de dígito verificador CPF em `Cpf.of()`
- T002: Criar interface `Periodico`
- T003: Criar/expandir interface `Vigencia`
- T004: Criar `VigenciaUtils`
- T005: Criar `YearMonthIntegerConverter`
- T006: Testes para todas as novas classes
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:29:39.693628+00:00"
loops_doing_to_done: "1"
ended_at: "2026-04-20T21:34:50.921787+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP03 — CPF + Domínio Período/Vigência

## Context

O módulo `domain` possui o value object `Cpf` mas sem validação de dígito verificador (apenas estrutural). Além disso, os projetos `obrigacoes-service-esocial`, `obrigacoes-service-reinf` e `obrigacoes-service-dctfweb` implementam independentemente interfaces de Período/Vigência e conversores de `YearMonth ↔ Integer yyyyMM`, criando divergências silenciosas. Este WP consolida a validação de CPF e o domínio de período/vigência no módulo `domain`, que é dependência transitiva de todos os serviços — eliminando as duplicatas. **Atenção:** verificar se `Vigencia` já existe no domínio antes de criar para evitar conflito de nomes.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `domain` ≥ 90% linha + ≥ 90% branch
- Todos os value objects: `final`, construtor privado, factory estático, `Serializable` com `serialVersionUID = 1L`
- Módulo `domain` = zero dependências externas (JDK only)
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Integrar validação de dígito verificador CPF em `Cpf.of()`

**Objetivo:** Elevar o nível de validação do `Cpf` value object de `STRUCTURAL` para `PROVISIONAL` ou `OFFICIAL` integrando o algoritmo de dígito verificador extraído de `ValidadorCPF.java` do projeto `obrigacoes-service-esocial`.

**Passos:**
1. Localizar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/model/Cpf.java`
2. Verificar o nível de validação atual em `GovValidationCatalog` para CPF
3. Extrair algoritmo de dígito verificador CPF de `obrigacoes-service-esocial/ValidadorCPF.java`:
   - Remover máscara, verificar 11 dígitos
   - Rejeitar sequências homogêneas ("00000000000", "11111111111", ...)
   - Calcular dois dígitos verificadores (Módulo 11 padrão CPF)
4. Integrar a lógica diretamente no `Cpf.of(String)` ou via método privado `validarDigitos(String cpf)`
5. Garantir que testes existentes continuam passando
6. Atualizar `GovValidationCatalog` para refletir o novo nível de confiança da validação de CPF

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/model/Cpf.java` — integrar validação de dígito verificador
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/GovValidationCatalog.java` — atualizar nível de confiança CPF

**Validação:**
- CPFs com dígitos verificadores incorretos lançam `InvalidDocumentException`
- Sequências homogêneas ("00000000000") são rejeitadas
- `Cpf.of("52998224725")` (CPF válido de teste) é aceito

**Edge cases:**
- CPF com máscara (`000.000.000-00`) deve ser aceito e normalizado
- Verificar impacto em testes existentes de `Cpf` — não deve quebrar nenhum teste de CPF estruturalmente válido

---

### T002 — Criar interface `Periodico`

**Objetivo:** Definir o contrato de tipos de domínio que representam períodos de apuração no formato `Integer yyyyMM`, permitindo que `VigenciaConverter` e `VigenciaValidator` operem sobre qualquer tipo que implemente o contrato.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/Periodico.java`
2. Declarar como `public interface Periodico`
3. Método: `Integer getPeriodo()` — retorna o período no formato `yyyyMM` como `Integer`
4. Método default opcional: `boolean equalsIgnoringPeriod(Periodico other)` — compara dois Periodicoes ignorando o período (ex: mesma competência, diferente versão)
5. Javadoc em português: "Contrato para tipos de domínio que possuem período de apuração no formato AAAAMM (ex: 202501 para janeiro de 2025)."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/Periodico.java` — nova interface

**Validação:**
- Compila sem erros; interface pura sem dependências externas
- Pode ser usada como tipo genérico `<P extends Periodico>`

**Edge cases:**
- Definir comportamento esperado quando `getPeriodo()` retorna `null` — documentar no Javadoc que implementações devem garantir não-nulo

---

### T003 — Criar/expandir interface `Vigencia`

**Objetivo:** Definir (ou expandir, caso já exista) o contrato de tipos de domínio que representam uma vigência com data de início e fim, para uso em `VigenciaConverter` e `VigenciaValidator`.

**Passos:**
1. **Primeiro:** Verificar se `Vigencia` já existe no módulo `domain` com `grep -r "class Vigencia\|interface Vigencia" declaracoes-gov-core-domain/src/`
2. **Se já existir:** avaliar se a assinatura é compatível com `LocalDate getInicioVigencia()` e `LocalDate getFimVigencia()` — fazer merge ou criar `VigenciaPeriodo` se houver conflito irreconciliável
3. **Se não existir:** criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/Vigencia.java`
4. Métodos: `LocalDate getInicioVigencia()`, `LocalDate getFimVigencia()`
5. Javadoc em português: "Contrato para tipos de domínio que possuem período de vigência com data de início e fim."
6. **Nota:** `LocalDate` está disponível no Java 8 (`java.time.LocalDate`) — sem dependência externa

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/Vigencia.java` — nova interface (ou arquivo existente se merge)

**Validação:**
- Compila sem erros
- Compatível com o uso genérico `<V extends Vigencia>` no `VigenciaConverter` do WP07

**Edge cases:**
- Se `Vigencia` existente tem campos mutáveis (setters), a nova interface não precisa incluir setters — apenas getters como contrato mínimo
- Documentar no Javadoc qual o comportamento esperado quando `getFimVigencia()` retorna `null` (vigência aberta/indeterminada)

---

### T004 — Criar `VigenciaUtils`

**Objetivo:** Centralizar as operações utilitárias sobre períodos no formato `Integer yyyyMM` que estão atualmente duplicadas em múltiplos projetos.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaUtils.java`
2. Declarar como `public final class VigenciaUtils` com construtor privado
3. Implementar os métodos:
   - `parseAnoMes(Integer yyyyMM): YearMonth` — converte Integer para `java.time.YearMonth`
   - `formatAnoMes(YearMonth ym): Integer` — converte `YearMonth` para Integer yyyyMM
   - `getPeriodoAtual(): Integer` — retorna o período atual (mês corrente) como yyyyMM
   - `getPeriodoSeguinte(Integer periodo): Integer` — avança um mês
   - `getPeriodoAnterior(Integer periodo): Integer` — recua um mês
   - `calcularDiferencaMeses(Integer periodoInicio, Integer periodoFim): int` — diferença em meses entre dois períodos
4. Javadoc em português para cada método, incluindo exemplos: "Ex: {@code VigenciaUtils.parseAnoMes(202501)} retorna {@code YearMonth.of(2025, 1)}"
5. Todos os métodos devem ser `null-safe`: lançar `IllegalArgumentException` se `null` recebido

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaUtils.java` — nova classe utilitária

**Validação:**
- `parseAnoMes(202501)` retorna `YearMonth.of(2025, 1)`
- `getPeriodoSeguinte(202512)` retorna `202601` (rollover de ano)
- `getPeriodoAnterior(202501)` retorna `202412` (rollover de ano)
- `calcularDiferencaMeses(202501, 202506)` retorna `5`

**Edge cases:**
- Período `202513` (mês 13) deve lançar `IllegalArgumentException`
- Período `0` ou negativo deve lançar `IllegalArgumentException`
- `calcularDiferencaMeses` com `periodoFim < periodoInicio` deve retornar valor negativo ou lançar exceção — documentar decisão

---

### T005 — Criar `YearMonthIntegerConverter`

**Objetivo:** Criar conversor bidirecional entre `YearMonth` e `Integer yyyyMM`, encontrado duplicado em `obrigacoes-service-reinf` e `obrigacoes-service-dctfweb`.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/YearMonthIntegerConverter.java`
2. Declarar como `public final class YearMonthIntegerConverter` com construtor privado (métodos estáticos)
3. Métodos:
   - `YearMonth toYearMonth(Integer yyyyMM)` — delega a `VigenciaUtils.parseAnoMes()`
   - `Integer toInteger(YearMonth ym)` — delega a `VigenciaUtils.formatAnoMes()`
4. Javadoc em português com nota de que esta classe é um façade conveniente sobre `VigenciaUtils`
5. Verificar se nos projetos de origem havia interface `Converter<F,T>` — se sim, implementar ou deixar como classe utilitária simples

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/YearMonthIntegerConverter.java` — novo conversor

**Validação:**
- `YearMonthIntegerConverter.toYearMonth(202501)` retorna `YearMonth.of(2025, 1)`
- `YearMonthIntegerConverter.toInteger(YearMonth.of(2025, 1))` retorna `202501`
- Round-trip: `toInteger(toYearMonth(202506)) == 202506`

**Edge cases:**
- `null` lança `IllegalArgumentException` com mensagem descritiva
- Conversão de `YearMonth.of(2025, 12)` → `202512` (não `2025012`)

---

### T006 — Testes para todas as novas classes

**Objetivo:** Garantir cobertura ≥ 90% em todos os artefatos deste WP com testes de casos nominais, de fronteira e de erro.

**Passos:**
1. Criar/expandir `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/model/CpfTest.java` — cobrir dígitos verificadores válidos e inválidos
2. Criar `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaUtilsTest.java` — cobrir todos os 6 métodos, com especial atenção a rollover janeiro/dezembro
3. Criar `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/YearMonthIntegerConverterTest.java` — cobrir round-trips e nulos
4. Se `Periodico` e `Vigencia` têm default methods, criar teste de contrato simples usando classe anônima

**Arquivos:**
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/model/CpfTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/VigenciaUtilsTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/YearMonthIntegerConverterTest.java`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-domain` verde
- JaCoCo ≥ 90% linha + branch no módulo `domain`

**Edge cases:**
- Testar `VigenciaUtils.getPeriodoSeguinte(202512)` → `202601` (fronteira dezembro→janeiro com virada de ano)
- Testar `VigenciaUtils.getPeriodoAnterior(202501)` → `202412` (fronteira janeiro→dezembro com volta de ano)
- Testar CPF com todos os dígitos iguais (sequências homogêneas inválidas)

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP03
```

## Review Feedback

TBD

## Activity Log

- 2026-04-20T21:29:39Z – unknown – lane=doing – Moved to doing
- 2026-04-20T21:34:51Z – unknown – lane=done – Moved to done
