# PLANO-UNIFICADO-V1

## 1. Proposito

Este documento consolida e revisa criticamente os planejamentos produzidos por Codex, Qwen, Kimi e Gemini para a reestruturacao da `declaracoes-gov-core`.

A decisao final e orientada por dois eixos:

- solidez tecnica de longo prazo;
- utilidade negocial real para um ecossistema de ERPs fiscais e contabeis.

## 2. Sintese do confronto entre os planos

### 2.1 Consensos validos

Houve convergencia real entre os planos nos seguintes pontos:

- a lib deve ser o coracao reutilizavel do ecossistema gov;
- Java 8 deve ser mantido;
- a biblioteca deve ser agnostica de framework;
- documentos, periodos, vigencias, XML e certificados sao os eixos centrais;
- o baseline atual merece ser fechado como `v0.1.0`;
- a documentacao atual precisa ser reescrita antes de ampliar o codigo.

### 2.2 Aportes relevantes por plano

`Codex`

- trouxe a melhor separacao entre nucleo minimo e modulos opcionais;
- tratou com mais rigor a fronteira entre core e transmissao;
- introduziu politica explicita para validadores oficiais, provisiorios e estruturais.

`Qwen`

- mapeou bem lacunas do baseline atual;
- destacou a necessidade de pensar na migracao dos projetos consumidores;
- levantou varios itens de conveniencia que serao uteis no futuro.

`Kimi`

- reforcou a visao da lib como abstracao legal brasileira;
- trouxe preocupacoes corretas com texto fiscal, formatos e arquivos do ecossistema SPED;
- chamou atencao para artefatos de dominio que ainda faltam.

`Gemini`

- foi o mais prudente na exclusao de escopo volatil;
- acertou ao proteger o core de tabelas que exigiriam versionamento constante;
- reforcou a necessidade de amadurecer crypto/signature sem transformar isso no centro da lib.

### 2.3 Pontos rejeitados ou postergados

Os itens abaixo apareceram em parte dos planos, mas ficam fora da `v1.0.0`:

- clientes HTTP/SOAP/REST e interceptors;
- OAuth2, autenticacao de canal e proxy como parte do core;
- IDs de eventos e protocolos especificos por declaracao;
- framework generico `lang` com `Result`, `Try` e afins;
- tabelas volateis amplas como CST, CFOP, NCM e natureza de rendimentos no nucleo base;
- SPI ampla antes de consolidar contratos estaveis;
- modulos criados apenas por analogia com Apache Commons, sem conteudo suficiente.

## 3. Decisoes finais

### 3.1 Estrategia de arquitetura

A `v1.0.0` sera multi-modulo desde o inicio:

- `declaracoes-gov-core-parent`
- `declaracoes-gov-core-bom`
- `declaracoes-gov-core-domain`
- `declaracoes-gov-core-format`
- `declaracoes-gov-core-xml`
- `declaracoes-gov-core-crypto`

Motivos da decisao:

- evita contaminar o nucleo com dependencias pesadas;
- permite adocao incremental pelos projetos consumidores;
- reduz risco de breaking change acidental no dominio;
- prepara o ecossistema para evolucao sem inflar o artefato principal.

### 3.2 Fronteira funcional

Entram no core:

- documentos e identificadores brasileiros;
- periodos, vigencias e tipos transversais;
- normalizacao e formatacao reutilizaveis;
- XML e crypto como modulos opcionais do mesmo ecossistema.

Nao entram no core:

- entrega da declaracao;
- validacao de campo especifico de leiaute;
- transporte;
- autenticacao de API;
- regras de negocio de modulo tributario;
- orquestracao de lotes e retorno.

### 3.3 Regra final para validadores

Toda validacao tera classificacao explicita:

- `oficial`: regra confirmada em fonte primaria oficial;
- `provisoria`: existe implementacao, mas com aviso de instabilidade normativa;
- `estrutural`: apenas mascara, formato, tamanho e parse.

Nenhum documento sem regra oficial mapeada podera ser exposto como validacao normativa confiavel.

Quando nao houver regra oficial suficiente:

- o tipo pode existir;
- a classe de validacao pode existir;
- mas o contrato precisa explicitar que a decisao final de validar materialmente fica com o usuario.

## 4. Escopo de entrega da v1.0.0

### 4.1 Nucleo obrigatorio

- `Cnpj`
- `Cpf`
- `Nis` ou `PisPasep`
- `CodigoMunicipio`
- `Uf`
- `TipoInscricao`
- `TipoAmbiente`
- `PeriodoApuracao`
- `Vigencia`

### 4.2 Expansao prevista

Entram se confirmados por valor transversal e base tecnica suficiente:

- `Cep`
- `Cei`
- `Cno`
- `Caepf`
- formatadores de documentos;
- normalizadores de texto fiscal;
- formatos complementares de competencia e apuracao.

### 4.3 Expansao condicional

Entram somente com confirmacao normativa e custo de manutencao aceitavel:

- `InscricaoEstadual`
- `CodigoPais`
- `Cnae`

## 5. Sequencia de execucao

### Passo 0 - Fechamento da linha atual

- validar o baseline atual;
- ajustar metadados e release notes;
- criar a tag `v0.1.0`;
- abrir `develop`.

### Fase 1 - Documentacao e arquitetura

- publicar este plano unificado;
- publicar o plano Codex;
- publicar o pre-documento de requisitos;
- alinhar nomenclatura e fronteiras da `v1.0.0`.

### Fase 2 - Modularizacao

- converter o projeto para parent + BOM + modulos;
- mover o codigo atual sem ampliar escopo desnecessariamente.

### Fase 3 - Estabilizacao do dominio

- revisar VOs existentes;
- remover heuristicas;
- classificar validadores;
- estabilizar pacotes publicos.

### Fase 4 - Complementos reutilizaveis

- formatacao e normalizacao;
- XML e crypto com contratos mais robustos;
- melhoria de mensagens de erro e documentacao.

### Fase 5 - Preparacao para adocao

- guias de migracao;
- matriz de confiabilidade de validadores;
- criterios de uso pelos modulos consumidores.

## 6. Criterios de aceite

- a documentacao deixa claro o que e oficial, provisiorio e estrutural;
- o nucleo nao depende de framework;
- o nucleo continua compativel com Java 8;
- o escopo da `v1.0.0` e ambicioso, mas controlado;
- os modulos opcionais nao contaminam o artefato minimo;
- a biblioteca fica apta a servir eSocial, EFD-Reinf, DCTFWeb e futuras declaracoes sem virar um monolito de integracao.
