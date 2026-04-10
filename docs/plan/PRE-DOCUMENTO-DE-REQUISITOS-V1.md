# PRE-DOCUMENTO-DE-REQUISITOS-V1

## 1. Objetivo do documento

Este documento registra o pre-alinhamento de requisitos para a reestruturacao da `declaracoes-gov-core` rumo a `v1.0.0`.

Ele existe para fechar as decisoes de produto e arquitetura antes de qualquer implementacao estrutural, reduzindo risco de escopo errado e de mudancas caras nos modulos consumidores.

## 2. Problema de negocio

O ecossistema de declaracoes governamentais brasileiras possui varias responsabilidades transversais que se repetem:

- documentos e identificadores brasileiros;
- periodos, vigencias e competencias;
- normalizacao de texto e numero;
- assinatura XML e infraestrutura criptografica;
- pequenos contratos de dominio que reaparecem em quase toda declaracao.

Hoje essas responsabilidades tendem a se espalhar ou se duplicar entre leiautes, transmissores e aplicacoes. Isso aumenta:

- custo de manutencao;
- risco de divergencia comportamental;
- retrabalho;
- dificuldade para evoluir o ecossistema como produto.

## 3. Objetivo do produto

A `declaracoes-gov-core` deve ser a base reutilizavel para o desenvolvimento de bibliotecas e aplicacoes que orquestram declaracoes do governo brasileiro, permitindo que os modulos consumidores foquem em:

- regra de negocio;
- orquestracao;
- adaptacao de canais;
- integracao com o ERP.

## 4. Usuarios e consumidores

Consumidores diretos:

- bibliotecas de leiaute;
- bibliotecas de transmissao;
- servicos internos;
- aplicacoes ERP fiscais e contabeis.

Consumidores indiretos:

- desenvolvedores do ecossistema;
- times de integracao gov;
- times de manutencao e suporte tecnico.

## 5. Escopo funcional da v1.0.0

### 5.1 Deve fazer

- modelar tipos de dominio brasileiro reutilizaveis;
- validar documentos quando houver base normativa confiavel;
- normalizar, parsear e formatar documentos e periodos;
- prover utilitarios de XML e criptografia em modulos opcionais;
- manter contratos publicos claros, estaveis e agnosticos.

### 5.2 Nao deve fazer

- implementar entrega/transmissao completa de declaracoes;
- centralizar regras de leiaute especifico;
- depender de framework de injecao, validacao ou web;
- absorver tabelas altamente volateis sem estrategia propria;
- prometer validacao oficial para documentos sem regra oficial mapeada.

## 6. Requisitos funcionais

### RF01 - Nucleo de dominio brasileiro

A biblioteca deve oferecer value objects e tipos reutilizaveis para o contexto brasileiro, incluindo no minimo:

- `Cnpj`
- `Cpf`
- `Nis` ou `PisPasep`
- `CodigoMunicipio`
- `Uf`
- `TipoInscricao`
- `TipoAmbiente`
- `PeriodoApuracao`
- `Vigencia`

### RF02 - Validacao orientada por confiabilidade normativa

Cada validador deve ser classificado como:

- oficial;
- provisiorio;
- estrutural.

Essa classificacao deve fazer parte do contrato do produto e da documentacao.

### RF03 - Suporte estrutural quando a regra oficial nao existir

Para documentos sem algoritmo oficial mapeado, a biblioteca deve oferecer apenas:

- parse;
- normalizacao;
- mascara;
- validacao basica de formato;
- checagem de tamanho.

A decisao de aplicar validacao material fica com o usuario consumidor.

### RF04 - Modularizacao do ecossistema

O produto deve separar claramente:

- dominio minimo obrigatorio;
- formatacao e normalizacao;
- XML;
- crypto.

### RF05 - Compatibilidade de adocao

O produto deve permitir que bibliotecas consumidoras adotem apenas o modulo necessario, sem arrastar dependencias pesadas do ecossistema inteiro.

## 7. Requisitos nao funcionais

### RNF01 - Java 8

Toda a `v1.0.0` deve permanecer compativel com Java 8.

### RNF02 - Agnosticidade

Nao deve haver dependencia obrigatoria de Spring, Jakarta EE, Bean Validation ou stack equivalente.

### RNF03 - Thread-safety

Value objects, validadores e factories devem ser imutaveis ou stateless sempre que aplicavel.

### RNF04 - Transparencia documental

A documentacao publica deve explicitar:

- o que o core faz;
- o que o core nao faz;
- quais validadores sao oficiais;
- quais sao provisiorios;
- quais sao apenas estruturais.

### RNF05 - Reuso de bibliotecas maduras

A implementacao deve reutilizar bibliotecas consolidadas quando isso reduzir custo e risco sem comprometer a agnosticidade.

## 8. Regras de inclusao de artefatos

Um novo tipo, utilitario ou validador so deve entrar na `v1.0.0` quando houver:

1. reuso transversal comprovado;
2. significado real no contexto brasileiro;
3. baixa dependencia de declaracao especifica;
4. custo de manutencao justificavel;
5. contrato claro para o consumidor.

## 9. Requisitos de documentacao

Antes da implementacao estrutural devem existir na raiz do projeto:

- um plano tecnico do Codex;
- um plano unificado entre os agentes;
- este pre-documento de requisitos.

Depois disso, a documentacao formal da `v1.0.0` deve derivar para:

- requisitos consolidados;
- design;
- arquitetura;
- plano de migracao;
- matriz de confiabilidade dos validadores.

## 10. Criterios de pronto para iniciar implementacao

A implementacao pode comecar quando:

- o baseline `v0.1.0` estiver reconhecido como linha fechada;
- a estrategia multi-modulo estiver aceita;
- a politica de validadores estiver aceita;
- o escopo da `v1.0.0` estiver fechado;
- os artefatos de planejamento e requisitos estiverem publicados na raiz.
