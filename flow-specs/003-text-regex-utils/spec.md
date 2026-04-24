# Feature Specification: Text, Regex and Value Classes Utils

**Feature Branch**: `feature/003-text-regex-utils`
**Created**: 2026-04-24
**Status**: Planned

## Source Document
**Tipo:** `generic-discovery`
**Origem:** Chat session
**Data:** 2026-04-24

### Resumo da análise
A manipulação de strings, conversões, sanitização e expressões regulares (regex) no contexto fiscal brasileiro (SPED, REINF, eSocial, notas fiscais) é complexa e propensa a falhas (ex: caracteres invisíveis que quebram o XML, acentos não suportados em TXT, tamanhos fixos, formatação de números). O objetivo é criar o **arsenal definitivo** para processamento de strings no domínio governamental e corporativo, estendendo a biblioteca core.

O plano abrange:
- Java (`core-domain` e `core-format`): Padrões RegEx compilados e métodos de sanitização robusta.
- Kotlin (`core-kotlin`): Extensions fluídas e `@JvmInline value classes` idiomáticas para tipagem forte sem custo de alocação em runtime.

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Centralizar Expressões Regulares Fiscais (Priority: P1)
Como desenvolvedor, eu preciso de um catálogo centralizado e pré-compilado de expressões regulares (Regex) para os padrões exigidos pelos XSDs e manuais (eSocial, Reinf, SPED), para garantir alta performance e padronização na validação estrutural de identificadores e strings.

**Acceptance Scenarios**:
1. **Given** um formato exigido por XSD (e.g. e-mail, CEP, Chave de Acesso NF-e, Recibo eSocial), **When** avaliado contra os patterns pré-compilados, **Then** as strings válidas são aprovadas e as inválidas rejeitadas sem onerar a memória com recompilação contínua.

### User Story 2 - Utilitários de Tratamento de Texto Fiscal em Java (Priority: P1)
Como desenvolvedor, eu preciso de utilitários em Java (`GovStringUtils`) para remover acentuação, aplicar padding (LPad/RPad), sanitizar caracteres inválidos (W3C XML) e extrair apenas números ou alfanuméricos, para formatar dados de TXTs posicionais e gerar XMLs seguros para os WebServices do Serpro.

**Acceptance Scenarios**:
1. **Given** uma string com acentos, **When** processada pelo utilitário, **Then** ela retorna o formato NFD sem caracteres diacríticos (ex: "Açúcar" -> "Acucar").
2. **Given** um nome próprio, **When** sanitizado para XML, **Then** a string é retornada sem *control chars* ilegais (`[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFFF]`).
3. **Given** uma string para TXT, **When** aplicada máscara `toSpedFormat`, **Then** a string retorna convertida em maiúsculas, sem acentos, com padding ou truncada no comprimento exato.

### User Story 3 - Value Classes e Extensions em Kotlin (Priority: P1)
Como desenvolvedor Kotlin, eu preciso de extensões fluídas (e.g. `String.digitsOnly`) e Value Classes (`@JvmInline value class`) para identificadores comuns (Email, TelefoneBR, CEP, ChaveAcessoNfe, CNAE), para garantir forte tipagem de domínio (type safety) nas APIs e Workers de Integração sem custo de alocação de objetos.

**Acceptance Scenarios**:
1. **Given** a classe `TelefoneBR`, **When** eu tento instanciá-la com um telefone de 11 dígitos, **Then** ela aceita (mesmo com parênteses) removendo a máscara; se inválido, ela lança erro de regex.
2. **Given** o utilitário idiomático Kotlin `GovRegex`, **When** instanciado, **Then** ele expõe os patterns pré-compilados do Java de forma adaptada ao Kotlin.
3. **Given** um Value Class formatador (`CpfFormato`), **When** passo no payload, **Then** não permite string fora da estrutura.

---

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: O pacote `br.com.contabilizei.obrigacoes.govcore.text` do módulo `domain` DEVE fornecer uma classe estática `GovRegexPatterns` contendo `java.util.regex.Pattern` compilados para dados de contato, identificadores, chaves, classificação e sanitização XML W3C.
- **FR-002**: O pacote `br.com.contabilizei.obrigacoes.govcore.util` do módulo `format` DEVE fornecer a classe `GovStringUtils` com métodos null-safe para LPad, RPad, extração alfanumérica/numérica, remoção de acentos W3C NFD, *strip* HTML, truncamento e sanitização posicional (SPED/Caixa/BB).
- **FR-003**: O módulo `kotlin` DEVE expor extensões idiomáticas (properties como `.onlyDigits`, funções estendidas) em `TextExtensions.kt` usando internamente `GovStringUtils`.
- **FR-004**: O módulo `kotlin` DEVE fornecer um catálogo de `@JvmInline value class` para representar dados como `Email`, `TelefoneBR`, `Cep`, `Passaporte`, `Cnae`, `Cbo`, `Ncm`, e identificadores de formatação para Chave NFe, NUP, Recibo, usando as RegEx criadas no core Java para lançar `IllegalArgumentException` no bloco `init` caso o formato não bata.
- **FR-005**: As Value Classes DEVEM restringir a lógica estritamente à estrutura das strings (comprimento e Regex), deixando a validação matemática/tributária forte (ex: cálculo Módulo 11 da Inscrição Estadual) a cargo do pacote `validators` do `domain` ou serviços especialistas.

### Key Entities

- `GovRegexPatterns`: (Java) Padrões regex pré-compilados e estáticos.
- `GovStringUtils`: (Java) Funções de limpeza e formatação de strings SPED/XML.
- `GovRegex`: (Kotlin) Objeto Kotlin mapeando regex nativos do Java.
- `@JvmInline value class` (ex: `Cep`, `Email`, `TelefoneBR`, `Cbo`): Invólucros de strings sem custo de runtime, protegidos por regex (Type Safety).

### Non-Functional Requirements

- **NFR-001**: A implementação Java deve continuar restrita e compatível ao baseline Java 8.
- **NFR-002**: O código Kotlin DEVE ser construído de forma que a sua interoperabilidade com JSON parsing (Jackson com Kotlin Module) aconteça sem a necessidade de getters pesados ou reflection em runtime (Inline class efficiency).
- **NFR-003**: Os utilitários DEVEM ser resilientes ao problema conhecido de ReDoS (Regex Denial of Service), garantindo que expressões regulares são lineares.
- **NFR-004**: Toda classe (Java e Kotlin) deverá estar protegida por testes unitários alcançando 90% (JaCoCo gate).

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: O `mvn verify` deve executar com sucesso todas as classes recém-criadas em `domain`, `format` e `kotlin`.
- **SC-002**: Utilização zero-copy (na Heap JVM) em runtime confirmada para os `@JvmInline value classes`.
- **SC-003**: A conversão e sanitização XML em `GovStringUtils` deve aprovar a filtragem de textos com caracteres de controle Unicode sem danificar o conteúdo.