---
work_package_id: WP01
title: Java Domain Regex
lane: planned
subtasks:
  - T001
  - T002
  - T003
---
# WP01 - Java Domain Regex

## Objective
Criar o catálogo estático de expressões regulares pré-compiladas (`java.util.regex.Pattern`) para validação estrutural básica e sanitização de texto, hospedado no módulo agnóstico `declaracoes-gov-core-domain`.

## Steps
1. Criar a classe `GovRegexPatterns` em `br.com.contabilizei.obrigacoes.govcore.text`.
2. Adicionar as contantes para identificadores fiscais: CEP, Telefone, Cnpj, Cpf, NIS, CEI, CNO, CAEPF, Passaporte, Titulo, CNH, RG, Chave de Acesso, CFOP, CST, CNAE, NCM, CEST, CBO, Matricula eSocial, NUP (processo judicial) e regras de chars XML.
3. Criar os testes unitários (`GovRegexPatternsTest.java`) para validar o match contra algumas strings simples para certificar a integridade das Regexes criadas.

## Validation
- As variáveis DEVEM ser `public static final Pattern`.
- O teste Junit 4 deve passar com 100% nas constantes.
