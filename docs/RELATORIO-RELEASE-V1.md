# Relatorio Tecnico para Diretoria
## Reestruturacao da declaracoes-gov-core para a linha v1.0.0

**Data:** 10/04/2026  
**Projeto:** declaracoes-gov-core  
**Linha atual de trabalho:** 1.0.0-SNAPSHOT  
**Baseline fechado:** v0.1.0  
**Status:** DOCUMENTACAO E PASSO 0 TECNICO CONCLUIDOS

---

## 1. Resumo Executivo

A `declaracoes-gov-core` deixou de ser tratada apenas como suporte pontual para leiautes e transmissores e passou a ser definida como a fundacao reutilizavel do ecossistema `declaracoes-*` e, por extensao, de ERPs fiscais e contabeis brasileiros.

Nesta etapa foram concluidos:

- consolidacao do planejamento tecnico na raiz do repositorio;
- confronto dos planos de Codex, Qwen, Kimi, Gemini, Claude e OpenCode;
- criacao do workflow cascata em `docs/`;
- alinhamento da linha de trabalho `develop` para `1.0.0-SNAPSHOT`.

Resultado: o projeto agora possui uma trilha documental coerente para iniciar a implementacao da `v1.0.0` com escopo e governanca claros.

---

## 2. Estado Atual do Projeto

### 2.1 Baseline tecnico observado

| Item | Valor |
|------|-------|
| Classes principais mapeadas | 31 |
| Suites de teste atuais | 17 |
| Testes executados no baseline local | 47 |
| Resultado do baseline local | Build de testes bem-sucedido |
| Cobertura agregada atual | 71.63% instrucoes / 53.23% branches |
| Branch de trabalho atual | `develop` |
| Tag de baseline | `v0.1.0` |

### 2.2 Principais capacidades ja existentes

- documentos e value objects: `Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, `Recibo`, `CodigoMunicipio`, `Vigencia`;
- validadores de CPF, CNPJ numerico e alfanumerico, NIS;
- enums e tabelas pequenas: `Uf`, `TipoInscricao`, `TipoAmbiente`;
- utilitarios: `GovJsonFactory`, `XmlDocuments`, `XmlDates`;
- crypto e assinatura: `CertificateProvider`, A1/A3, `SslContextBuilder`, `XmlDsigSigner`.

---

## 3. Riscos Reais Identificados

### 3.1 Bugs funcionais
- `Uf` contem nomes incorretos em alguns estados.

### 3.2 Debitos de confianca
- `crypto/` e `signature/` ainda nao possuem cobertura compativel com o papel fundacional da biblioteca;
- `GovValidators` mistura regras fortes com heuristicas estruturais;
- o contrato do `GovJsonFactory` precisa ser explicitado para nao parecer um mapper generico;
- a deteccao implicita de `Id` em assinatura XML precisa ser redesenhada.

### 3.3 Risco de produto
- sem uma politica formal para validadores, o consumidor pode assumir como "oficial" uma regra que ainda nao foi confirmada normativamente.

---

## 4. Decisoes Estrategicas Aprovadas

### 4.1 Arquitetura
A `v1.0.0` sera multi-modulo:

- `declaracoes-gov-core-parent`
- `declaracoes-gov-core-bom`
- `declaracoes-gov-core-domain`
- `declaracoes-gov-core-format`
- `declaracoes-gov-core-xml`
- `declaracoes-gov-core-crypto`

### 4.2 Escopo
Entram no core:

- dominio brasileiro reutilizavel;
- validacao com classificacao de confianca;
- formatacao e normalizacao;
- XML e crypto em modulos opcionais.

Ficam fora:

- transporte;
- OAuth2;
- entrega e orquestracao de declaracoes;
- regras especificas de leiaute;
- catalogos altamente volateis sem estrategia propria.

### 4.3 Politica de validadores
Cada validacao sera tratada como:

- `oficial`
- `provisoria`
- `estrutural`

Nenhum algoritmo nao confirmado sera promovido como validacao normativa definitiva.

---

## 5. Artefatos Gerados nesta Etapa

### 5.1 Raiz do repositorio
- `CODEX-PLAN-V1.md`
- `PLANO-UNIFICADO-V1.md`
- `PRE-DOCUMENTO-DE-REQUISITOS-V1.md`

### 5.2 Workflow cascata em `docs/`
- `01-REQUISITOS.md`
- `02-DESIGN.md`
- `03-PLANO-TESTES.md`
- `04-IMPLANTACAO.md`
- `ARCHITECTURE.md`
- `RELATORIO-RELEASE-V1.md`

---

## 6. Proximo Passo Recomendado

Iniciar a implementacao tecnica da `v1.0.0` em `develop` na seguinte ordem:

1. corrigir bugs factuais e debitos do baseline;
2. endurecer cobertura de testes, principalmente em `crypto` e `signature`;
3. extrair `Modulo11` compartilhado;
4. preparar a modularizacao Maven;
5. mover o codigo atual para os modulos alvo sem ampliar escopo indevidamente.

---

## 7. Conclusao

A `declaracoes-gov-core` agora possui:

- visao de produto consolidada;
- escopo controlado;
- politica formal para validadores;
- workflow documental completo;
- linha `develop` preparada para receber a implementacao da `v1.0.0`.

O projeto esta pronto para sair da fase de definicao e entrar na fase de execucao tecnica.
