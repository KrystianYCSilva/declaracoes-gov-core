---
work_package_id: WP04
title: Hierarquia de Exceções Expandida
lane: "doing"
dependencies: []
created_at: '2026-04-20T21:13:24.129559+00:00'
subtasks:
- T001: Criar `BusinessException` e `BusinessRuntimeException` com código de erro
- T002: Criar `SchemaValidationException`
- T003: Criar `CertificadoInvalidoException`
- T004: Criar `ArquivoInvalidoReciboException`, `SemConexaoException`, `RetryableException`
- T005: Criar `PeriodoFaltanteException` e `PeriodoRepetidoException`
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:34:55.340569+00:00"
---

# WP04 — Hierarquia de Exceções Expandida

## Context

A hierarquia de exceções atual do domínio (`GovCoreException` e derivadas) não cobre os casos de uso de negócio encontrados nos projetos `obrigacoes-service-esocial`, `obrigacoes-service-dctfweb` e `microservico-ecd/ecf`. Cada projeto criou suas próprias exceções locais desconectadas da hierarquia base, impedindo tratamento centralizado por consumidores da lib. Este WP expande a hierarquia com as exceções de domínio faltantes, todas sem dependência de framework, mantendo a raiz `GovCoreException`. A classe `PeriodoFaltanteException` e `PeriodoRepetidoException` são necessárias para o `VigenciaValidator` do WP07.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `domain` ≥ 90% linha + ≥ 90% branch
- Módulo `domain` = zero dependências externas (JDK only)
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Criar `BusinessException` e `BusinessRuntimeException`

**Objetivo:** Introduzir exceções de negócio com campo de código de erro numérico para permitir que consumidores programem tratamento baseado em código sem depender de mensagem de texto.

**Passos:**
1. Verificar a hierarquia existente em `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/`
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/BusinessException.java`:
   - Extends `GovCoreException`
   - Campo `private final Integer codigo`
   - Construtores: `(Integer codigo)`, `(Integer codigo, String message)`, `(Integer codigo, String message, Throwable cause)`
   - Getter `getCodigo(): Integer`
3. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/BusinessRuntimeException.java`:
   - Extends `RuntimeException` (não `GovCoreException` — é uma unchecked counterpart)
   - Mesma estrutura de campo `codigo` e construtores
4. Javadoc em português para ambas, explicando a diferença checked/unchecked e o propósito do `codigo`

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/BusinessException.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/BusinessRuntimeException.java`

**Validação:**
- `new BusinessException(1001).getCodigo() == 1001`
- `BusinessException` é uma checked exception (`extends Exception` via `GovCoreException`)
- `BusinessRuntimeException` é uma unchecked exception (`extends RuntimeException`)

**Edge cases:**
- Construtor com `codigo == null` deve ser permitido (código pode ser desconhecido em contextos legados) — documentar
- `cause` propagada corretamente para inspeção de stack trace

---

### T002 — Criar `SchemaValidationException`

**Objetivo:** Fornecer exceção específica para falhas de validação de schema XML/XSD, usada pelo módulo `xml` e por consumidores que validam payloads antes da transmissão.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/SchemaValidationException.java`
2. Extends `GovCoreException`
3. Construtores: `(String message)`, `(String message, Throwable cause)`
4. Opcional: campo `private final String schemaName` para indicar qual schema falhou
5. Javadoc em português: "Lançada quando um documento XML não é válido conforme o schema (XSD) esperado."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/SchemaValidationException.java`

**Validação:**
- `new SchemaValidationException("Documento inválido").getMessage()` retorna a mensagem
- `instanceof GovCoreException` retorna `true`

**Edge cases:**
- Garantir que `cause` (ex: `SAXException`) é encapsulada corretamente para diagnóstico

---

### T003 — Criar `CertificadoInvalidoException`

**Objetivo:** Especializar a hierarquia de exceções de assinatura para o caso específico de certificado inválido, separando-o de erros genéricos de assinatura.

**Passos:**
1. Verificar se `GovSignatureException` existe em `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/`
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/CertificadoInvalidoException.java`
3. Extends `GovSignatureException` (se existir) ou `GovCoreException` (fallback)
4. Construtores: `(String message)`, `(String message, Throwable cause)`
5. Javadoc em português: "Lançada quando o certificado digital fornecido é inválido, expirado ou não pode ser lido."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/CertificadoInvalidoException.java`

**Validação:**
- `new CertificadoInvalidoException("Certificado expirado")` mensagem propagada
- `instanceof GovCoreException` retorna `true` (via herança)

**Edge cases:**
- Se `GovSignatureException` não existir, criar também neste passo para manter a hierarquia semântica

---

### T004 — Criar `ArquivoInvalidoReciboException`, `SemConexaoException`, `RetryableException`

**Objetivo:** Adicionar exceções para cenários de transmissão e conectividade que aparecem em múltiplos projetos sem exceção base comum.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/ArquivoInvalidoReciboException.java`:
   - Extends `GovCoreException`
   - Construtores: `(String message)`, `(String message, Throwable cause)`
   - Javadoc: "Lançada quando o arquivo de recibo recebido do portal é inválido ou não reconhecido."
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/SemConexaoException.java`:
   - Extends `GovCoreException`
   - Construtores: `(String message)`, `(String message, Throwable cause)`
   - Javadoc: "Lançada quando não há conectividade com o portal governamental."
3. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/RetryableException.java`:
   - Extends `BusinessRuntimeException` (operação falhou mas pode ser retentada)
   - Construtor com `Integer codigo`
   - Javadoc: "Exceção de runtime indicando que a operação pode ser retentada. Consumidores devem implementar política de retry ao capturar esta exceção."

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/ArquivoInvalidoReciboException.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/SemConexaoException.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/RetryableException.java`

**Validação:**
- `RetryableException` é unchecked (`RuntimeException`)
- `ArquivoInvalidoReciboException` e `SemConexaoException` são checked (`GovCoreException`)

**Edge cases:**
- `RetryableException extends BusinessRuntimeException` — garantir que `BusinessRuntimeException` foi criado em T001 antes
- Documentar no Javadoc de `RetryableException` que a lógica de retry não é responsabilidade desta lib (DD-05)

---

### T005 — Criar `PeriodoFaltanteException` e `PeriodoRepetidoException`

**Objetivo:** Fornecer exceções específicas para falhas de validação de sequências de períodos, necessárias para o `VigenciaValidator` criado no WP07.

**Passos:**
1. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/PeriodoFaltanteException.java`:
   - Extends `GovCoreException`
   - Campo `private final Integer periodoEsperado`
   - Construtores: `(Integer periodoEsperado)`, `(Integer periodoEsperado, String message)`
   - Getter `getPeriodoEsperado(): Integer`
   - Javadoc: "Lançada quando um período esperado na sequência de competências está ausente."
2. Criar `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/PeriodoRepetidoException.java`:
   - Extends `GovCoreException`
   - Campo `private final Integer periodoRepetido`
   - Construtores: `(Integer periodoRepetido)`, `(Integer periodoRepetido, String message)`
   - Getter `getPeriodoRepetido(): Integer`
   - Javadoc: "Lançada quando um período de competência aparece mais de uma vez na sequência, indicando duplicata."
3. Criar testes para **todas** as exceções deste WP (T001–T005) em um único arquivo de testes ou arquivos separados por classe

**Arquivos:**
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/PeriodoFaltanteException.java`
- `declaracoes-gov-core-domain/src/main/java/br/uem/npd/govcore/exception/PeriodoRepetidoException.java`
- `declaracoes-gov-core-domain/src/test/java/br/uem/npd/govcore/exception/BusinessExceptionTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/uem/npd/govcore/exception/PeriodoFaltanteExceptionTest.java`
- `declaracoes-gov-core-domain/src/test/java/br/uem/npd/govcore/exception/PeriodoRepetidoExceptionTest.java`

**Validação:**
- `new PeriodoFaltanteException(202502).getPeriodoEsperado() == 202502`
- Todas as exceções propagam mensagem e causa corretamente
- `mvn -B -q verify -pl declaracoes-gov-core-domain` verde com JaCoCo ≥ 90%

**Edge cases:**
- Testar encadeamento de causa (`getCause()` não nulo)
- Testar `getMessage()` com e sem mensagem explícita no construtor

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP04
```

## Activity Log

- 2026-04-20T21:34:55Z – unknown – lane=doing – Moved to doing
