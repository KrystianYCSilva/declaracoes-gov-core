# Documento de Design - v1.0.0

## 1. Visao Arquitetural

### 1.1 Diagrama de componentes alvo

```
+--------------------------------------------------------------------+
|                     declaracoes-gov-core v1.0.0                    |
+--------------------------------------------------------------------+
|                                                                    |
|  +-- parent / bom ------------------------------------------------+|
|  | versionamento, alinhamento de dependencias, modulos            ||
|  +----------------------------------------------------------------+|
|                                                                    |
|  +-- domain ------------------------------------------------------+ |
|  | documentos, identificadores, periodos, vigencias, territorio  | |
|  | contratos de validacao, normalizacao e formatacao             | |
|  +----------------------------------------------------------------+ |
|                                                                    |
|  +-- format ------------------------------------------------------+ |
|  | mascaras, desmascaramento, texto fiscal, formatos numericos   | |
|  | e representacoes de data/periodo                              | |
|  +----------------------------------------------------------------+ |
|                                                                    |
|  +-- xml ---------------------------------------------------------+ |
|  | parsing seguro, DOM utils, serializacao, assinatura XML       | |
|  | configuravel e agnostica ao leiaute                           | |
|  +----------------------------------------------------------------+ |
|                                                                    |
|  +-- crypto ------------------------------------------------------+ |
|  | A1, A3, PKCS11, SSLContext, diagnosticos operacionais         | |
|  +----------------------------------------------------------------+ |
|                                                                    |
|  Fora do core: transporte, OAuth2, SOAP/REST clients, regras de   |
|  declaracao, lotes, protocolos especificos e tabelas altamente    |
|  volateis.                                                        |
+--------------------------------------------------------------------+
```

### 1.2 Estado implementado na v1.0.0

Estado atual observado no repositorio:

- reactor Maven multi-modulo com `parent`, `bom`, `domain`, `format`, `xml` e `crypto`;
- politica publica de validadores implementada e documentada;
- `CPF` e `NIS` com construtores estruturais e validacao algoritmica provisoria por opt-in;
- `XmlDsigSigner` com configuracao explicita de alvo via `XmlSignatureOptions`;
- `SslContextBuilder` com suporte a `TrustStore` explicito;
- `mvn -q verify` verde com gate de cobertura ativo nos modulos.

---

## 2. Decisoes de Design

### DD-01: Multi-modulo desde o inicio
- **Problema**: o estado atual mistura nucleo de dominio, XML e crypto no mesmo artefato.
- **Opcao A**: manter um unico modulo e separar apenas por pacotes.
- **Opcao B**: separar desde ja em `domain`, `format`, `xml` e `crypto`.
- **Decisao**: Opcao B.
- **Consequencia**: adocao incremental pelos consumidores e menor acoplamento entre partes leves e pesadas.

### DD-02: Core nao e transmissor
- **Problema**: parte dos planos propunha empurrar HTTP, OAuth2, proxies e eventos especificos para dentro do core.
- **Opcao A**: absorver transporte e conveniencias de canal.
- **Opcao B**: manter o core restrito a blocos transversais de dominio e infraestrutura.
- **Decisao**: Opcao B.
- **Consequencia**: `serpro-transmissor`, `esocial-transmissor` e afins continuam responsaveis por canal, autenticacao e entrega.

### DD-03: Matriz de confiabilidade para validadores
- **Problema**: nem todo documento brasileiro tem algoritmo oficial claro, estavel e publicamente mapeado.
- **Opcao A**: tratar qualquer heuristica de mercado como validacao padrao.
- **Opcao B**: classificar validadores em `oficial`, `provisorio` e `estrutural`.
- **Decisao**: Opcao B.
- **Consequencia**:
  - somente validacao oficial pode sustentar fail-fast normativo;
  - validacao provisoria deve ser opt-in e claramente sinalizada;
  - suporte estrutural valida apenas formato, mascara, parse e tamanho.

### DD-04: Construtores/factories nao devem embutir regra provisoria
- **Problema**: value objects fail-fast combinam bem com regras oficiais, mas sao arriscados quando a validacao e incerta.
- **Opcao A**: fazer `of(...)` aplicar qualquer algoritmo disponivel.
- **Opcao B**: fazer `of(...)` aplicar apenas regras oficiais ou estruturais documentadas.
- **Decisao**: Opcao B.
- **Consequencia**: algoritmos provisiorios ficam fora do caminho obrigatorio do construtor, evitando falsas garantias para o consumidor.

### DD-05: Modulo11 compartilhado
- **Problema**: CPF, CNPJ numerico, CNPJ alfanumerico e NIS repetem variacoes do mesmo checksum.
- **Opcao A**: manter logica duplicada em cada validador.
- **Opcao B**: extrair utilitario compartilhado com vetores oficiais.
- **Decisao**: Opcao B.
- **Consequencia**: menor duplicacao, melhor auditabilidade e melhor suporte a futuros validadores oficiais baseados no mesmo padrao.

### DD-06: Reuso deliberado de bibliotecas maduras
- **Problema**: parte do escopo sugerido pelos agentes invade utilitarios ja resolvidos por bibliotecas consolidadas.
- **Opcao A**: criar utilitarios internos para tudo.
- **Opcao B**: reutilizar `commons-lang3`, Jackson, Santuario e JCA/JCE quando apropriado.
- **Decisao**: Opcao B.
- **Consequencia**: menor codigo de manutencao e foco nas lacunas brasileiras reais.

### DD-07: JSON governamental com contrato explicito
- **Problema**: o comportamento atual do `GovJsonFactory` e util para integracoes gov, mas nao e um `ObjectMapper` "generico".
- **Opcao A**: vender o mapper atual como padrao universal.
- **Opcao B**: tratá-lo como configuracao especializada de integracao.
- **Decisao**: Opcao B.
- **Consequencia**: o modulo `format` ou utilitario equivalente deve documentar com clareza a serializacao de `BigDecimal` e suas implicacoes.

### DD-08: Assinatura XML configuravel e nao implicita
- **Problema**: a assinatura atual depende de heuristicas para localizar o atributo `Id`.
- **Opcao A**: manter descoberta implicita.
- **Opcao B**: explicitar alvo, atributo ID e politica de assinatura.
- **Decisao**: Opcao B.
- **Consequencia**: API mais previsivel, menor acoplamento a eSocial/Reinf e menor risco de assinatura incorreta.

### DD-09: Inscricao Estadual nao e requisito obrigatorio da primeira entrega
- **Problema**: a IE tem grande relevancia de negocio, mas alta variabilidade e custo de manutencao.
- **Opcao A**: tornar suporte amplo de IE obrigatorio na `v1.0.0`.
- **Opcao B**: tratar IE como expansao condicional, entrando apenas com framework ou estados realmente sustentados por fontes confiaveis e custo justificavel.
- **Decisao**: Opcao B.
- **Consequencia**: evita scope creep na primeira reestruturacao sem bloquear evolucao futura.

---

## 3. Debitos Tecnicos do Baseline

### 3.1 Bugs do baseline absorvidos
- inconsistencias de descricao em `Uf` foram corrigidas;
- heuristicas indevidas no facade de validadores foram segregadas por nivel de confianca;
- XML e crypto deixaram de depender de cobertura artificialmente inflada.

### 3.2 Pontos ainda deliberadamente controlados
- `CPF` e `NIS` seguem `PROVISIONAL` por ausencia de fonte primaria catalogada no core;
- `PKCS11` continua dependente de driver nativo e token real no ambiente do consumidor;
- a release Git final depende do fluxo de versionamento do repositorio local.

---

## 4. Padroes de Projeto Aplicados

| Padrao | Onde | Motivacao |
|--------|------|-----------|
| Value Object | `Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao` | Garantir contratos de dominio claros |
| Strategy | validadores por tipo | Permitir variacao controlada de algoritmo |
| Factory | criacao de mappers, SSLContext, parsers | Isolar configuracoes tecnicas |
| Facade | `GovValidators` e futuros equivalentes | Expor acesso simplificado sem acoplar o consumidor |
| Module Boundary | `domain`, `format`, `xml`, `crypto` | Evitar monolito de integracao |

---

## 5. Thread-Safety Model

| Componente | Thread-safe? | Estrategia |
|------------|-------------|------------|
| Value objects | Sim | imutabilidade |
| Validadores oficiais | Sim | stateless |
| Factories de JSON/XML | Sim, quando configuradas uma vez | configuracao imutavel e reuso controlado |
| Builders especificos | Nao por definicao | instancia por uso |
| PKCS11/A3 | Cuidado especial | documentar limitacoes de provider e driver |

---

## 6. Estrategia de evolucao

1. Baseline `v0.1.0` fechado e preservado.
2. Dividas criticas do baseline absorvidas na linha `1.0.0`.
3. Projeto reestruturado em modulos.
4. Nucleo de dominio e politica de validadores endurecidos.
5. `format`, `xml` e `crypto` fechados com contratos publicos mais claros.
6. Documentacao de migracao publicada para os consumidores.
