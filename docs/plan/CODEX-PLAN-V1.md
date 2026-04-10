# CODEX-PLAN-V1

## 1. Objetivo

Reposicionar a `declaracoes-gov-core` como a fundacao reutilizavel do ecossistema de declaracoes governamentais brasileiras, com foco em:

- documentos e identificadores brasileiros;
- periodos, vigencias e tipos transversais;
- normalizacao, formatacao e parsing reutilizaveis;
- infraestrutura opcional de XML e criptografia;
- compatibilidade com Java 8 e uso agnostico em ERPs fiscais e contabeis.

O objetivo da v1.0.0 nao e virar framework de transmissao nem concentrar regras de entrega de declaracoes. O objetivo e fornecer os blocos estaveis que os modulos de negocio e orquestracao precisam reutilizar.

## 2. Premissas

- Java 8 permanece como baseline tecnico.
- A biblioteca deve continuar sem dependencia de frameworks invasivos.
- Reuso de bibliotecas consagradas e obrigatorio quando fizer sentido tecnico e economico.
- O desenho deve seguir YAGNI: somente entra no core aquilo que tem alto reuso e baixo acoplamento a declaracao especifica.
- O baseline atual sera fechado como `v0.1.0` antes da reestruturacao da `v1.0.0`.

## 3. Diagnostico do Estado Atual

O estado atual ja possui valor real e nao deve ser tratado como experimento descartavel. A base existente ja contem:

- `Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, `Vigencia`, `CodigoMunicipio`, `Uf`, `TipoInscricao`, `TipoAmbiente`;
- validadores para CPF, CNPJ numerico e alfanumerico, NIS;
- `GovJsonFactory`, `XmlDocuments`, `XmlDates`;
- `CertificateProvider`, A1/A3, `SslContextBuilder`, `XmlDsigSigner`;
- testes unitarios para parte importante do nucleo.

As principais fragilidades identificadas sao:

- heuristicas provisoria para alguns tipos de inscricao em `GovValidators`;
- fronteira semantica pouco clara entre nucleo de dominio e infraestrutura pesada;
- acoplamentos implicitos em assinatura XML;
- cobertura e politica de testes ainda aquem do rigor esperado para um core 1.0.0;
- documentacao ainda descrevendo a lib como fundacao mais estreita do que o objetivo real.

## 4. Decisao Arquitetural

### 4.1 Estrategia de artefatos

A v1.0.0 sera organizada como repositorio multi-modulo Maven:

- `declaracoes-gov-core-parent`
- `declaracoes-gov-core-bom`
- `declaracoes-gov-core-domain`
- `declaracoes-gov-core-format`
- `declaracoes-gov-core-xml`
- `declaracoes-gov-core-crypto`

### 4.2 Fronteiras dos modulos

`declaracoes-gov-core-domain`

- documentos e identificadores;
- periodos, vigencias e tipos comuns;
- territorio e codigos estaveis;
- contratos de validacao, normalizacao e formatacao.

`declaracoes-gov-core-format`

- mascaras e desmascaramento;
- normalizacao textual brasileira;
- formatos de datas e periodos;
- representacao numerica reutilizavel para integracoes gov.

`declaracoes-gov-core-xml`

- parsing e serializacao XML segura;
- utilitarios DOM;
- assinatura XML configuravel e agnostica ao leiaute.

`declaracoes-gov-core-crypto`

- certificados A1 e A3;
- `SSLContext`;
- suporte a PKCS11;
- diagnosticos e erros operacionais.

## 5. Politica de Inclusao no Core

Algo so entra no core quando atender simultaneamente aos criterios abaixo:

1. alto potencial de reuso entre declaracoes ou ERPs fiscais;
2. valor de dominio brasileiro real;
3. fronteira tecnica estavel;
4. ausencia de acoplamento direto a uma declaracao especifica;
5. custo de manutencao aceitavel para a versao 1.0.0.

Nao entram na v1.0.0:

- clientes HTTP, SOAP ou REST;
- OAuth2 e autenticacao de canal;
- orquestracao de entrega;
- protocolos e IDs especificos por declaracao;
- tabelas legais muito volateis no nucleo base;
- utilitarios genericos que seriam apenas uma copia inferior de Apache Commons ou Guava.

## 6. Politica de Validadores

Esta e uma regra central da v1.0.0:

- nenhum validador sera tratado como oficial sem fonte normativa primaria mapeada;
- quando a regra oficial existir e estiver confirmada, o tipo pode ser fail-fast;
- quando a regra for incompleta, discutivel ou dependente de mercado, a validacao sera marcada como provisoria/experimental;
- quando nao houver regra oficial suficiente, a lib entregara apenas tratamento estrutural:
  - parse;
  - normalizacao;
  - mascara;
  - tamanho;
  - checagens basicas de formato.

Cada documento suportado sera classificado em uma matriz publica:

- `oficial`
- `provisorio`
- `estrutural`

Essa classificacao devera aparecer em:

- Javadoc;
- README;
- documento de requisitos;
- plano unificado.

## 7. Escopo Alvo da v1.0.0

### 7.1 Nucleo minimo obrigatorio

- `Cnpj`
- `Cpf`
- `Nis` ou `PisPasep`
- `CodigoMunicipio`
- `Uf`
- `TipoInscricao`
- `TipoAmbiente`
- `PeriodoApuracao`
- `Vigencia`

### 7.2 Candidatos fortes para expansao

- `Cep`
- `Cei`
- `Cno`
- `Caepf`
- normalizadores e formatadores comuns de documentos;
- utilitarios de texto fiscal;
- formatos adicionais de competencia e apuracao.

### 7.3 Candidatos condicionais

Entram apenas se houver base normativa e beneficio transversal claro:

- `InscricaoEstadual`
- `CodigoPais`
- `Cnae`

## 8. Roadmap

### Passo 0

- validar o baseline atual;
- corrigir release metadata para `v0.1.0`;
- criar tag `v0.1.0`;
- abrir `develop`.

### Fase 1 - Reestruturacao tecnica

- converter o projeto para parent + BOM + modulos;
- mover codigo atual para os modulos corretos;
- estabilizar pacotes publicos.

### Fase 2 - Dominio e validacao

- consolidar VOs e contratos do nucleo;
- remover heuristicas improprias;
- classificar cada validador por nivel de confianca normativa.

### Fase 3 - Formatacao e normalizacao

- implementar mascaras, parse e normalizacao;
- padronizar formatos reutilizaveis de string, data e numero.

### Fase 4 - XML e crypto

- modularizar e endurecer APIs;
- explicitar configuracoes de assinatura;
- melhorar tratamento de erros operacionais para A3/PKCS11.

### Fase 5 - Migracao e documentacao

- reescrever documentacao da lib;
- publicar matriz de validadores;
- preparar adocao pelos demais projetos do ecossistema.

## 9. Criterios de aceite da v1.0.0

- API publica enxuta, coerente e documentada;
- separacao clara entre nucleo minimo e modulos opcionais;
- nenhum validador nao oficial promovido como oficial;
- cobertura alta no dominio;
- baseline Java 8 preservado;
- ausencia de acoplamento com framework ou declaracao especifica.
