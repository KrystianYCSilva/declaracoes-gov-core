---
work_package_id: WP02
title: Java Format Text Utils
lane: planned
subtasks:
  - T004
  - T005
  - T006
  - T007
  - T008
dependencies: [WP01]
---
# WP02 - Java Format Text Utils

## Objective
Criar uma suíte de utilitários `GovStringUtils` no módulo `declaracoes-gov-core-format` para manipulação limpa de Strings, focando em padding (usado extensivamente em arquivos SPED/TXT) e sanitização W3C XML e Unicode (remoção de acentos NFD).

## Steps
1. Criar a classe `GovStringUtils` (com construtor privado) no pacote `br.com.contabilizei.obrigacoes.govcore.util`.
2. Criar os métodos de `removeAcentos` usando `java.text.Normalizer` e regex `\\p{M}`.
3. Criar os métodos utilitários W3C/XML como `sanitizeForXml` integrando ao Pattern do WP01.
4. Criar métodos como `lpad`, `rpad`, `truncate`, `toSpedFormat` e `stripHtmlTags`.
5. Proteger contra NullPointers (a lib deve aceitar Nulls retornando null ou string vazia dependendo do caso, conforme Javadoc).
6. Criar `GovStringUtilsTest.java`.

## Validation
- Não ter quebra em strings unicode (e.g., emojis na descrição).
- Gate do JaCoCo (90%+) passa em `mvn -q verify -pl declaracoes-gov-core-format`.

## Review Feedback

TBD

