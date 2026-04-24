---
work_package_id: WP02
title: CNPJ Alfanumérico RF 2026
lane: "done"
dependencies: []
created_at: '2026-04-20T21:13:24.102057+00:00'
subtasks:
- T001: Criar interface `CnpjValidationStrategy`
- T002: Criar `NumericCnpjValidationStrategy`
- T003: Criar `AlphanumericCnpjValidationStrategy`
- T004: Criar `CnpjValidationContext`
- T005: Expandir `Cnpj.of()` para aceitar CNPJ alfanumérico
- T006: Testes para todas as classes de validação e Cnpj expandido
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:16:49.046771+00:00"
loops_doing_to_for_review: "1"
for_review_started_at: "2026-04-20T21:28:03.157260+00:00"
loops_for_review_to_done: "1"
ended_at: "2026-04-20T21:28:23.426513+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP02 — CNPJ Alfanumérico RF 2026

## Context

A Receita Federal publicou a Instrução Normativa RF 2026 definindo o novo formato de CNPJ alfanumérico (12 caracteres alfanuméricos + 2 dígitos verificadores numéricos). O valor object `Cnpj` atual aceita apenas o formato numérico de 14 dígitos. Implementações divergentes do validador alfanumérico já foram criadas de forma independente em `gateway-bardo` (Kotlin: `CnpjUtils.kt`) e `obrigacoes-service-esocial` (Java) — este WP consolida a lógica no domínio compartilhado usando o padrão Strategy, mantendo 100% de retrocompatibilidade com CNPJs numéricos existentes.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `domain` ≥ 90% linha + ≥ 90% branch
- Todos os value objects: `final`, construtor privado, factory estático, `Serializable` com `serialVersionUID = 1L`
- Módulo `domain` = zero dependências externas (JDK only)
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Criar interface `CnpjValidationStrategy`

**Objetivo:** Definir o contrato de validação de CNPJ como interface pura, sem dependências, para permitir múltiplas implementações selecionáveis por contexto.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/CnpjValidationStrategy.java`
2. Declarar como `public interface CnpjValidationStrategy`
3. Método: `boolean validate(String cnpj)` — recebe CNPJ como string (com ou sem máscara)
4. Javadoc em português: "Estratégia de validação de CNPJ. Implementações devem verificar formato e dígitos verificadores conforme a instrução normativa aplicável."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/CnpjValidationStrategy.java` — nova interface

**Validação:**
- Compila sem erros; sem dependências externas
- `javap` confirma que é uma interface pública

**Edge cases:**
- Interface deve aceitar `null` como contrato (implementações decidem como tratar; documentar no Javadoc)

---

### T002 — Criar `NumericCnpjValidationStrategy`

**Objetivo:** Extrair a lógica de validação numérica existente da classe `Cnpj` para uma strategy isolada e testável de forma independente.

**Passos:**
1. Inspecionar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/model/Cnpj.java` para localizar a lógica atual de validação de dígitos verificadores
2. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/NumericCnpjValidationStrategy.java`
3. Implementar `CnpjValidationStrategy`
4. Extrair lógica: remover máscara, verificar 14 dígitos numéricos, calcular e comparar dígitos verificadores (Módulo 11 padrão Receita Federal)
5. Javadoc em português explicando que a estratégia valida apenas CNPJs no formato numérico clássico (14 dígitos)
6. **Não remover ainda** a lógica do `Cnpj.java` — a refatoração acontece no T005

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/NumericCnpjValidationStrategy.java` — nova implementação

**Validação:**
- CNPJs numéricos válidos retornam `true`
- CNPJs com dígito verificador incorreto retornam `false`
- CNPJs com comprimento errado retornam `false`

**Edge cases:**
- CNPJ com máscara (pontos, barras, hífen) deve ser normalizado antes da validação
- Sequências de dígitos iguais (ex: "11111111111111") devem retornar `false`
- `null` ou string vazia deve retornar `false` (não lançar NPE)

---

### T003 — Criar `AlphanumericCnpjValidationStrategy`

**Objetivo:** Implementar a validação do novo formato de CNPJ alfanumérico conforme RF 2026: 12 caracteres `[0-9A-Z]` seguidos de 2 dígitos verificadores numéricos, com algoritmo de dígito verificador adaptado.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/AlphanumericCnpjValidationStrategy.java`
2. Implementar `CnpjValidationStrategy`
3. Compilar regex: `^[0-9A-Z]{12}[0-9]{2}$`
4. Implementar algoritmo de dígito verificador para alfanumérico: mapear cada caractere para seu valor numérico (dígitos = valor face, letras A=10, B=11, ... Z=35); aplicar pesos 2–9 ciclicamente da direita para a esquerda; calcular dois dígitos verificadores
5. Consultar e replicar o comportamento dos algoritmos encontrados em `gateway-bardo/CnpjUtils.kt` e `obrigacoes-service-esocial` para garantir paridade
6. Javadoc em português com referência à Instrução Normativa RF 2026

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/AlphanumericCnpjValidationStrategy.java` — nova implementação

**Validação:**
- CNPJs alfanuméricos com dígitos corretos retornam `true`
- CNPJs com dígitos incorretos retornam `false`
- CNPJs com caracteres inválidos (letras minúsculas, caracteres especiais) retornam `false`

**Edge cases:**
- CNPJ puramente numérico de 14 dígitos **não** deve ser aceito por esta strategy (é domínio da `NumericCnpjValidationStrategy`)
- Letras minúsculas devem ser normalizadas para maiúsculas antes da validação (ou rejeitadas — documentar decisão)
- Verificar se o algoritmo de dígito verificador para alfanumérico coincide com Módulo 11 ou é uma variante

---

### T004 — Criar `CnpjValidationContext`

**Objetivo:** Agregar as strategies disponíveis e selecionar automaticamente a strategy correta com base no formato da entrada.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/CnpjValidationContext.java`
2. Injetar `NumericCnpjValidationStrategy` e `AlphanumericCnpjValidationStrategy` como dependências (construtor ou constante interna)
3. Método público: `boolean validate(String cnpj)` — inspeciona o formato e delega à strategy correta:
   - Se após normalizar (remover máscara) contém apenas dígitos → `NumericCnpjValidationStrategy`
   - Se contém letras maiúsculas → `AlphanumericCnpjValidationStrategy`
4. Estratégia de normalização: remover `.`, `/`, `-` antes de inspecionar formato
5. Javadoc em português explicando a delegação automática

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/CnpjValidationContext.java` — nova classe de contexto

**Validação:**
- CNPJ numérico válido → delega a `NumericCnpjValidationStrategy` → `true`
- CNPJ alfanumérico válido → delega a `AlphanumericCnpjValidationStrategy` → `true`
- CNPJ numérico inválido → `false`
- CNPJ alfanumérico inválido → `false`

**Edge cases:**
- CNPJ `null` → `false` (sem NPE)
- CNPJ com comprimento errado após normalização → `false`
- CNPJ com caracteres inesperados (letras minúsculas, símbolos) → `false`

---

### T005 — Expandir `Cnpj.of()` para aceitar CNPJ alfanumérico

**Objetivo:** Fazer o value object `Cnpj` aceitar CNPJs alfanuméricos RF 2026 sem quebrar o comportamento existente para CNPJs numéricos, usando o `CnpjValidationContext` criado no T004.

**Passos:**
1. Abrir `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/model/Cnpj.java`
2. Substituir ou completar a validação interna com delegação ao `CnpjValidationContext.validate()`
3. Manter a exceção `InvalidDocumentException` para entradas inválidas — não alterar a assinatura de `of(String)`
4. Verificar se `Cnpj.valor` deve armazenar o CNPJ normalizado (sem máscara) ou conforme entrado — manter consistência com comportamento atual
5. Avaliar necessidade de método factory separado `Cnpj.ofAlphanumeric(String)` vs expandir `Cnpj.of()` — preferir expandir para transparência ao chamador
6. Remover lógica duplicada de validação numérica inlining após confirmar que `NumericCnpjValidationStrategy` cobre os casos

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/model/Cnpj.java` — expandir factory `of()` e substituir validação interna

**Validação:**
- `Cnpj.of("12345678000195")` continua funcionando (numérico)
- `Cnpj.of("AB1234567890AB")` aceito se dígitos verificadores corretos (alfanumérico RF 2026 — usar exemplo real)
- `Cnpj.of(null)` lança `InvalidDocumentException`
- `Cnpj.of("invalido")` lança `InvalidDocumentException`

**Edge cases:**
- CNPJ alfanumérico com dígito verificador inválido deve lançar `InvalidDocumentException`
- Sequências triviais alfanuméricas (ex: "AAAAAAAAAAAA00") devem ser rejeitadas se o algoritmo de dígito verificador assim determinar
- Verificar impacto em `GovValidationCatalog` — o nível de confiança deve ser `OFFICIAL` para numérico e definir nível para alfanumérico

---

### T006 — Testes para todos os validadores e Cnpj expandido

**Objetivo:** Garantir cobertura completa (≥90%) de todas as classes criadas neste WP, incluindo paridade comportamental com as implementações originais dos projetos de origem.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/validator/NumericCnpjValidationStrategyTest.java`
2. Criar `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/validator/AlphanumericCnpjValidationStrategyTest.java`
3. Criar `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/validator/CnpjValidationContextTest.java`
4. Expandir `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/model/CnpjTest.java` (ou criar se não existir)
5. Para cada classe de teste, cobrir: entradas válidas, entradas com dígito verificador errado, comprimento errado, nulos, mascarados, alfanuméricos

**Arquivos:**
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/validator/NumericCnpjValidationStrategyTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/validator/AlphanumericCnpjValidationStrategyTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/validator/CnpjValidationContextTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/model/CnpjTest.java`

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-domain` verde
- JaCoCo ≥ 90% linha + branch no módulo `domain`

**Edge cases:**
- Usar fixtures de CNPJs alfanuméricos reais publicados pela RF 2026 (ou gerados com algoritmo validado) para confirmar corretude do algoritmo
- Testar CNPJ numérico existente que era válido antes permanece válido após a refatoração do `Cnpj.of()`

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP02
```

## Review Feedback

TBD

## Activity Log

- 2026-04-20T21:16:49Z – unknown – lane=doing – Moved to doing
- 2026-04-20T21:28:03Z – unknown – lane=for_review – Moved to for_review
- 2026-04-20T21:28:23Z – unknown – lane=done – Moved to done
