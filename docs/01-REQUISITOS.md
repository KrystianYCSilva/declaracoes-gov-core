# Documento de Requisitos - v1.0.0

## 1. Introducao

### 1.1 Proposito
Biblioteca Java fundacional para o ecossistema `declaracoes-*`, reutilizavel por ERPs fiscais e contabeis brasileiros para documentos, periodos, normalizacao, XML e criptografia compartilhada.

### 1.2 Escopo
A biblioteca fornece:

- tipos de dominio brasileiro reutilizaveis;
- validacao de documentos com classificacao explicita de confianca normativa;
- parse, mascara, normalizacao e formatacao;
- utilitarios de XML em modulo opcional;
- certificados digitais e `SSLContext` em modulo opcional.

Transporte HTTP/SOAP/REST, OAuth2, entrega de declaracoes, regras especificas de leiaute e orquestracao ficam fora do escopo.

### 1.3 Publico-alvo
- desenvolvedores Java que integram ERPs e servicos a Receita Federal, SPED, eSocial, EFD-Reinf e sistemas correlatos;
- mantenedores das bibliotecas `declaracoes-*`;
- times que precisam de componentes brasileiros reutilizaveis sem depender de frameworks pesados.

### 1.4 Referencias
- `CODEX-PLAN-V1.md`
- `PLANO-UNIFICADO-V1.md`
- `PRE-DOCUMENTO-DE-REQUISITOS-V1.md`
- Manual do Desenvolvedor eSocial
- Portal SPED EFD-Reinf
- Documentos tecnicos da Receita Federal sobre CNPJ e CNPJ alfanumerico
- Documentacao oficial de CAEPF, CNO, CEI e codigos territoriais quando aplicavel

---

## 2. Requisitos Funcionais

### RF-01: Fechamento controlado do baseline v0.1.0
- **Descricao**: O estado atual deve ser reconhecido como baseline funcional da `v0.1.0` antes da reestruturacao da `v1.0.0`.
- **Criterio**: baseline identificado, riscos conhecidos registrados, tag `v0.1.0` prevista e branch `develop` prevista para a nova linha.
- **Status**: Atendido

### RF-02: Arquitetura multi-modulo
- **Descricao**: A `v1.0.0` deve separar nucleo minimo e capacidades opcionais em modulos Maven independentes.
- **Criterio**: arquitetura alvo com `domain`, `format`, `xml`, `crypto`, mais parent e BOM.
- **Status**: Atendido

### RF-03: Nucleo minimo de dominio brasileiro
- **Descricao**: O nucleo deve oferecer pelo menos `Cnpj`, `Cpf`, `Nis` ou `PisPasep`, `CodigoMunicipio`, `Uf`, `TipoInscricao`, `TipoAmbiente`, `PeriodoApuracao` e `Vigencia`.
- **Criterio**: tipos publicos reutilizaveis, imutaveis quando aplicavel, com contratos claros.
- **Status**: Atendido

### RF-04: Politica explicita para validadores
- **Descricao**: Toda validacao deve ser classificada como `oficial`, `provisoria` ou `estrutural`.
- **Criterio**: documentacao e API deixam claro o nivel de confianca normativa de cada tipo suportado.
- **Referencia atual**: `docs/05-MATRIZ-VALIDADORES.md`
- **Status**: Atendido

### RF-05: Suporte estrutural para documentos sem algoritmo oficial
- **Descricao**: Quando nao houver regra oficial mapeada, a biblioteca deve oferecer apenas parse, mascara, formato, tamanho e normalizacao basica.
- **Criterio**: o consumidor nao e induzido a acreditar em uma validacao normativa inexistente.
- **Status**: Atendido

### RF-06: Modulo de formatacao e normalizacao
- **Descricao**: A biblioteca deve oferecer mascaras, remocao de mascara, uppercase/sanitizacao normativa e formatos reutilizaveis de string, data e numero.
- **Criterio**: contratos leves e agnosticos, sem dependencia de framework.
- **Status**: Atendido

### RF-07: Modulo XML opcional
- **Descricao**: A biblioteca deve prover parsing XML seguro, utilitarios DOM e assinatura XML configuravel sem acoplamento a uma declaracao especifica.
- **Criterio**: APIs reutilizaveis e configuraveis, com comportamento explicito para alvo de assinatura e atributo ID.
- **Status**: Atendido

### RF-08: Modulo crypto opcional
- **Descricao**: A biblioteca deve prover certificados A1/A3, `SSLContext` e suporte a PKCS11 em modulo opcional.
- **Criterio**: suporte a `CertificateProvider`, A1/A3 e tratamento claro de erros operacionais.
- **Status**: Atendido

### RF-09: Reuso de algoritmo compartilhado de Modulo 11
- **Descricao**: Validadores baseados em Modulo 11 devem convergir para um utilitario compartilhado e testado com vetores oficiais.
- **Criterio**: ausencia de duplicacao desnecessaria em CPF, CNPJ, NIS e futuros validadores oficiais baseados no mesmo mecanismo.
- **Status**: Atendido

### RF-10: Documentacao de adocao e rastreabilidade
- **Descricao**: A biblioteca deve publicar requisitos, design, plano de testes e implantacao com rastreabilidade para o escopo da `v1.0.0`.
- **Criterio**: documentos em `docs/` alinhados com o workflow cascata e com a politica de validadores.
- **Status**: Atendido

---

## 3. Requisitos Nao-Funcionais

### RNF-01: Compatibilidade Java 8
- **Descricao**: Todo o codigo da `v1.0.0` deve permanecer compativel com Java 8.
- **Criterio**: compilacao com `source/target 1.8`.
- **Status**: Obrigatorio

### RNF-02: Agnosticidade
- **Descricao**: O produto nao deve depender de Spring, Jakarta EE, Bean Validation ou stack equivalente.
- **Criterio**: runtime leve e uso possivel em qualquer aplicacao Java.
- **Status**: Obrigatorio

### RNF-03: Thread-safety
- **Descricao**: Value objects, validadores e factories devem ser imutaveis ou stateless sempre que aplicavel.
- **Criterio**: ausencia de estado compartilhado mutavel sem controle explicito.
- **Status**: Obrigatorio

### RNF-04: Cobertura de testes
- **Descricao**: A `v1.0.0` deve atingir cobertura minima de 90% em linhas e branches no codigo mantido pelo projeto, sem esconder modulos criticos por exclusoes amplas.
- **Criterio**: JaCoCo >= 90% no escopo proprio do produto.
- **Status**: Atendido na release `1.0.0`

### RNF-05: Transparencia sobre confianca normativa
- **Descricao**: Toda validacao deve ter fonte, nivel de confianca e limitacoes explicitadas.
- **Criterio**: matriz publica de validadores e Javadoc consistente.
- **Status**: Atendido

### RNF-06: Reuso de bibliotecas maduras
- **Descricao**: A implementacao deve reutilizar bibliotecas maduras quando isso reduzir custo e risco.
- **Criterio**: uso preferencial de libs como Apache Commons Lang3, Jackson e Santuario em vez de reimplementacoes inferiores.
- **Status**: Obrigatorio

### RNF-07: YAGNI e controle de volatilidade
- **Descricao**: Tabelas, clientes e utilitarios altamente volateis ou especificos nao devem entrar na `v1.0.0` sem justificativa forte.
- **Criterio**: escopo controlado e custo de manutencao previsivel.
- **Status**: Obrigatorio

---

## 4. Baseline Atual e Gaps

### 4.1 Baseline identificado
- baseline `v0.1.0` identificado e preservado por tag
- linha `v1.0.0` reestruturada em multi-modulo Maven
- reactor validado com `mvn -q verify`
- gate de cobertura real ativo em todos os modulos do produto

### 4.2 Gaps conhecidos
- `CPF` e `NIS` permanecem `PROVISIONAL` ate catalogacao de fonte primaria suficiente
- `Pkcs11Provider` continua limitado pelo provider e driver nativos disponiveis no ambiente do consumidor
- a tag Git final da release depende do fluxo de versionamento do repositorio

---

## 5. Fora do Escopo da v1.0.0

- clientes HTTP, SOAP, REST ou OAuth2
- transporte de eventos e declaracoes
- regras de negocio especificas de cada declaracao
- IDs e protocolos especificos por sistema
- grandes tabelas volateis sem estrategia propria de manutencao
- utilitarios genericos sem valor brasileiro claro
