# QWEN-PLAN-V1: Reestruturação da Lib `declaracoes-gov-core` para v1.0.0

## Meta-Information

- **Feature**: Reestruturação completa da biblioteca `declaracoes-gov-core` para torná-la uma core library abrangente para sistemas fiscais e contábeis brasileiros
- **Author**: Qwen Code (assistente IA)
- **Date**: 2026-04-10
- **Status**: Planejamento para v1.0.0 (Java 8, agnóstica a frameworks)
- **Branch Strategy**: Tag v0.1.0 no código atual → branch `develop` para desenvolvimento v1.0.0 → merge em `main` na release

---

## 1. Contexto e Justificativa

### 1.1 Situação Atual (v0.1.0)

A versão atual do `declaracoes-gov-core` é funcional mas **incompleta** para o papel de coração de um ecossistema fiscal/contábil:

**✅ Implementado:**
- Value Objects básicos: `Cnpj`, `Cpf`, `Nis`, `Recibo`, `CodigoMunicipio`, `PeriodoApuracao`
- Validadores: CPF, CNPJ (numérico e alfanumérico ADE 15/2024), NIS
- Criptografia: `Pkcs12Provider` (A1), `Pkcs11Provider` (A3), `SslContextBuilder`
- Assinatura XML: `XmlDsigSigner` (XMLDSIG RSA-SHA256)
- Utilitários XML/JSON: `XmlDocuments`, `XmlDates`, `GovJsonFactory`
- Enums: `TipoInscricao`, `TipoAmbiente`, `Uf`
- Hierarquia de exceções fiscal

**❌ Lacunas identificadas:**
- **Validadores ausentes**: CEI, CNO, CAEPF, Inscrição Estadual (por UF)
- **Formatação**: Nenhum formatador de documentos (máscaras CNPJ/CPF/telefones)
- **HTTP**: Cliente HTTP com mTLS inexistente (apenas exceção definida, sem implementação)
- **Utilitários fiscais**: Cálculo de períodos, competência, exercícios, dias úteis
- **Tabelas oficiais**: Catálogos de códigos (natureza de rendimentos, códigos de receita, municípios IBGE)
- **Geração de IDs**: Padrões de ID para eventos (eSocial: `ID{cpf}{seq}`, REINF: `ID{cnpj}{seq}`)
- **Validação de processos judiciais**: Usado em ambas as declarações
- **Valores monetários**: Conversão BigDecimal ↔ formato XSD `[0-9]{1,12}[,][0-9]{2}`
- **Hash/integridade**: SHA-256 para eventos (presente em eSocial/REINF)
- **SPI/extensibilidade**: Zero configuration para plugins
- **Documentação pública**: Sem README.md, CHANGELOG.md, Javadoc

### 1.2 Ecossistema Atual

**19 projetos dependentes** (organizados em 3 categorias):

| Categoria | Projetos | Status |
|-----------|----------|--------|
| **Core** | `gov-core`, `gov-bom` | ✅ Implementados |
| **Leiautes** | `esocial-leiautes`, `efd-reinf-leiautes`, `dctfweb-leiautes`, `defis-leiautes`, `mit-leiautes`, `pgdas-leiautes`, `pgmei-leiautes`, `perdcomp-leiautes`, `parcelamento-leiautes` | ✅ Esocial (JAXB), ⚠️ Demais (skeleton) |
| **Transmissores** | `esocial-transmissor`, `efd-reinf-transmissor`, `serpro-transmissor` | ✅ Esocial, ⚠️ Demais (skeleton) |

**Nenhum** dos projetos dependentes importa `gov-core` atualmente. A migração ainda não começou.

### 1.3 Padrões Técnicos Identificados

Análise profunda dos WSDLs/XSDs oficiais de eSocial e REINF revelou:

| Conceito | eSocial | REINF | DCTFWeb | PGDAS |
|----------|---------|-------|---------|-------|
| **Protocolo** | SOAP 1.1 | REST/JSON | REST | Texto |
| **Auth** | mTLS + XML-DSig | mTLS + XML-DSig | OAuth2 + mTLS | Web login |
| **Eventos** | S-XXXX (52 tipos) | R-XXXX (4 famílias) | — | Registros |
| **Ambiente** | tpAmb 1/2 | tpAmb 1/2 | Produção/Dev | — |
| **Período** | perApur AAAA-MM | perApur AAAA-MM | Período apuração | Competência |
| **Retificação** | indRetif 1/2 | indRetif 1/2 | — | — |
| **Inscrição** | tpInsc + nrInsc | tpInsc + nrInsc | CNPJ | CNPJ/CPF |
| **Assinatura** | XML-DSig por evento | XML-DSig por evento | — | — |
| **Retorno** | XML (SOAP) | JSON (REST) | JSON | Texto |

**Padrões comuns que justificam uma core library:**
- `tpInsc` (1=CNPJ, 2=CPF) e `tpAmb` (1=Produção, 2=Produção Restrita) **idênticos**
- `perApur` no formato `gYearMonth` (AAAA-MM) **idêntico**
- Assinatura XML-DSig com C14N inclusive + Enveloped Transform **idêntico**
- Certificados A1/A3 para mTLS **idêntico**
- Recibos com formato estruturado `[0-9]{1,18}[-][0-9]{2}[-][0-9]{4}[-][0-9]{4}[-][0-9]{1,18}`
- Ocorrências/erros com estrutura `{tipo, código, descrição, localização}`
- Hash SHA-256 de eventos para integridade
- Protocolo de envio assíncrono: `X.AAAAMM.NNNNNNNNNNNNNNNNNNN`

### 1.4 Referências de Mercado

**Bibliotecas existentes analisadas:**

| Biblioteca | Linguagem | Status | Gap para nosso contexto |
|------------|-----------|--------|-------------------------|
| **Caelum Stella** | Java | ⚠️ Abandonado (JSF/Flex legado) | Core validador bom, mas stack desatualizada |
| **validation-br** | Node.js | ✅ Ativo | Não é Java, não serve |
| **java-validator-safeguard** | Java | ✅ Ativo | Foco em validação, sem utilitários fiscais |
| **ACBr (Delphi)** | Delphi | ✅ Ativo | Não é Java, mas referência de escopo |
| **Apache Commons** | Java | ✅ Ativo | Genérica, sem contexto fiscal BR |
| **Guava** | Java | ✅ Ativo | Genérica, sem contexto fiscal BR |

**Conclusão da pesquisa:** Não existe uma biblioteca Java **moderna, abrangente e específica** para o contexto fiscal brasileiro que una validação de documentos, utilitários de data/período, integração criptográfica e tabelas oficiais. A `gov-core` tem oportunidade de preencher essa lacuna.

---

## 2. Visão para v1.0.0

### 2.1 Princípios de Design

1. **Java 8+ mandatory**: Compatível com ambientes governamentais e corporativos legados
2. **Zero framework dependencies**: Sem Spring, Jakarta EE, etc. (agnóstica)
3. **Reutilizar consagradas**: Apache Commons (lang3, io, validator, text, csv), Guava onde fizer sentido
4. **YAGNI rigoroso**: Só implementar o que é útil no contexto fiscal/contábil brasileiro
5. **Imutabilidade**: Value Objects são imutáveis e thread-safe
6. **SPI-first**: Extensibilidade via Service Provider Interface para validadores, tabelas, etc.
7. **Documentation-driven**: Javadoc completo, README, CHANGELOG, exemplos de uso
8. **Test-covered**: Cobertura mínima de 90% (JaCoCo)

### 2.2 Arquitetura de Módulos

A estrutura será reorganizada em **pacotes lógicos** (semelhante ao Apache Commons, mas contextualizado):

```
br.uem.npd.govcore/
├── docs/                    → Documentos Brasileiros (CNPF, CPF, IE, etc.)
├── fiscal/                  → Utilitários Fiscais (períodos, competências, exercícios)
├── monetary/                → Valores Monetários (BigDecimal ↔ formato XSD)
├── time/                    → Tempo Brasileiro (dias úteis, competência, apuração)
├── geo/                     → Geografia (UF, municípios IBGE, códigos)
├── crypto/                  → Criptografia (A1/A3, mTLS, SSL) [existente]
├── signature/               → Assinatura Digital (XMLDSIG) [existente]
├── http/                    → Cliente HTTP com mTLS [novo]
├── tables/                  → Tabelas Oficiais (códigos de receita, natureza de rendimentos) [novo]
├── events/                  → Utilitários para Eventos (IDs, hash, protocolo) [novo]
├── validation/              → Validações (Strategy, Facade) [reestruturar]
├── format/                  → Formatação (máscaras, parse) [novo]
├── exception/               → Exceções [existente, expandir]
├── enums/                   → Enums de domínio [reorganizar de table/]
├── util/                    → Utilitários gerais (XML, JSON, IO) [existente, expandir]
└── spi/                     → Service Provider Interfaces [novo]
```

### 2.3 Novos Componentes Detalhados

#### 2.3.1 Pacote `docs/` (Documentos Brasileiros)

**Value Objects (imutáveis, thread-safe):**

| Classe | Finalidade | Formato | Validação |
|--------|-----------|---------|-----------|
| `Cnpj` | Já existe | 14 chars (numérico ou alfanumérico) | Módulo 11 duplo |
| `Cpf` | Já existe | 11 dígitos | Módulo 11 |
| `Cei` | **[NOVO]** Cadastro Especifico INSS | 10-12 dígitos | Módulo 11 específico |
| `Cno` | **[NOVO]** Cadastro Nacional de Obra | 14 dígitos | Módulo 11 |
| `Caepf` | **[NOVO]** Cadastro de Atividade Econômica PF | 14 dígitos | Validação própria |
| `Nis` | Já existe | 11 dígitos | Módulo 11 (pesos {3,2,9,8,7,6,5,4,3,2}) |
| `InscricaoEstadual` | **[NOVO]** Inscrição Estadual | Varia por UF | Algoritmo por estado (27 UFs) |
| `InscricaoMunicipal` | **[NOVO]** Inscrição Municipal | Varia por município | Validação por município |
| `ProcessoJudicial` | **[NOVO]** Número de processo | 17, 20 ou 21 dígitos | Formato + DV |
| `Cnae` | **[NOVO]** Classificação Nacional de Atividades | 7 dígitos | Tabela oficial |
| `Ncm` | **[NOVO]** Nomenclatura Comum do Mercosul | 8 dígitos | Tabela oficial |

**Validadores (padrão Strategy):**

| Classe | Finalidade | Status |
|--------|-----------|--------|
| `CeiValidator` | Valida CEI (Mod 11, 10-12 dígitos) | **[NOVO]** |
| `CnoValidator` | Valida CNO (Mod 11, 14 dígitos) | **[NOVO]** |
| `CaepfValidator` | Valida CAEPF | **[NOVO]** |
| `InscricaoEstadualValidator` | Valida IE por UF | **[NOVO]** |
| `ProcessoJudicialValidator` | Valida número de processo | **[NOVO]** |
| `CnaeValidator` | Valida CNAE | **[NOVO]** |
| `NcmValidator` | Valida NCM | **[NOVO]** |

**Facade:**
- `Documentos` (substitui `GovValidators`): API unificada para validação, formatação e geração de documentos

#### 2.3.2 Pacote `fiscal/` (Utilitários Fiscais)

**Value Objects:**

| Classe | Finalidade | Atributos |
|--------|-----------|-----------|
| `PeriodoApuracao` | Já existe (YearMonth wrapper) | ano, mes |
| `Competencia` | **[NOVO]** Período de competência fiscal | inicio, fim (LocalDate) |
| `ExercicioFiscal` | **[NOVO]** Ano fiscal | ano (Year) |
| `Vigencia` | Já existe (interface) | inicio, fim (LocalDate) |

**Utilitários:**

| Classe | Finalidade | Métodos principais |
|--------|-----------|-------------------|
| `PeriodoUtils` | **[NOVO]** Manipulação de períodos fiscais | `mesesEntre()`, `periodosNoAno()`, `iterarPeriodos()` |
| `CompetenciaUtils` | **[NOVO]** Cálculos de competência | `competenciaAtual()`, `competenciaAnterior()`, `proximoPeriodo()` |
| `RetificacaoUtils` | **[NOVO]** Lógica de retificação | `isRetificacao()`, `getReciboOriginal()` |

#### 2.3.3 Pacote `monetary/` (Valores Monetários)

**Motivação:** Padrão XSD `[0-9]{1,12}[,][0-9]{2}` aparece em **dezenas** de campos em eSocial/REINF

| Classe | Finalidade | Métodos |
|--------|-----------|---------|
| `MonetarioUtils` | **[NOVO]** Conversão BigDecimal ↔ string XSD | `toXsdString(BigDecimal)`, `fromXsdString(String)`, `formatarBr(BigDecimal)`, `parseBr(String)` |
| `TributoUtils` | **[NOVO]** Cálculos de tributos auxiliares | `calcularBase()`, `calcularAliquota()`, `calcularRetencao()` |

#### 2.3.4 Pacote `time/` (Tempo Brasileiro)

**Motivação:** Cálculos de competência, prazo, apuração são **críticos** para declarações

| Classe | Finalidade | Métodos |
|--------|-----------|---------|
| `DiasUteisUtils` | **[NOVO]** Cálculo com dias úteis (feriados nacionais) | `addDiasUteis()`, `isDiaUtil()`, `proximoDiaUtil()`, `diasUteisEntre()` |
| `FeriadosNacionais` | **[NOVO]** Catálogo de feriados nacionais BR | `getFeriados(ano)`, `isFeriado(date)` |
| `CompetenciaTimeUtils` | **[NOVO]** Manipulação de competência | `inicioCompetencia()`, `fimCompetencia()`, `mesesCompetencia()` |

#### 2.3.5 Pacote `geo/` (Geografia)

**Reorganizar de `table/`:**

| Classe | Finalidade | Status |
|--------|-----------|--------|
| `Uf` | Já existe (27 UFs + EX) | ✅ Reutilizar |
| `CodigoMunicipio` | Já existe (IBGE 7 dígitos) | ✅ Reutilizar |
| `Municipio` | **[NOVO]** VO completo com nome, UF, código | Novo |
| `RegiaoFiscal` | **[NOVO]** Regiões fiscais (SEFAZ) | Novo |

#### 2.3.6 Pacote `http/` (Cliente HTTP com mTLS)

**Motivação:** REINF usa REST/JSON, DCTFWeb usa REST, eSocial usa SOAP (mas pode ter REST futuro)

| Classe | Finalidade | Detalhes |
|--------|-----------|----------|
| `GovHttpClient` | **[NOVO]** Cliente HTTP com mTLS integrado | Apache HttpClient 5 (optional), suporta A1/A3 |
| `GovHttpRequest` | **[NOVO]** Builder de requisições | Fluent API, timeout, retry |
| `GovHttpResponse` | **[NOVO]** Resposta HTTP | Parse JSON/XML, tratamento de erros |
| `GovHttpInterceptor` | **[NOVO]** SPI para interceptors | Logging, métricas, retry |

**Dependência:** `org.apache.httpcomponents.client5:httpclient5:5.x` (optional)

#### 2.3.7 Pacote `tables/` (Tabelas Oficiais)

**Motivação:** eSocial e REINF usam tabelas de códigos oficiais que mudam periodicamente

| Classe | Finalidade | Conteúdo |
|--------|-----------|----------|
| `TabNaturezaRendimentos` | **[NOVO]** Tabela 01 REINF | {código, descrição, vigencia} |
| `TabCodigoReceita` | **[NOVO]** Códigos de receita | CPRB, contribuições, etc. |
| `TabAmbiente` | Renomear de `TipoAmbiente` | 1=Produção, 2=Pre-Produção/Dev |
| `TabTipoInscricao` | Renomear de `TipoInscricao` | 1=CNPJ, 2=CPF, 3=CAEPF, 4=CNO, 5=CEI |
| `TabVersao` | **[NOVO]** Versões de leiautes | eSocial, REINF, DCTFWeb |

**SPI para extensibilidade:**
```java
public interface TabelaOficialProvider {
    String getDeclaracao();
    String getTabela();
    List<TabelaItem> getItens();
    LocalDate getVigencia();
}
```

#### 2.3.8 Pacote `events/` (Utilitários para Eventos)

**Motivação:** eSocial e REINF compartilham padrões de ID, hash e protocolo

| Classe | Finalidade | Detalhes |
|--------|-----------|----------|
| `EventIdGenerator` | **[NOVO]** Geração de IDs de eventos | eSocial: `ID{cpf}{seq}`, REINF: `ID{cnpj}{seq}` |
| `EventHashUtils` | **[NOVO]** SHA-256 para integridade | `calcularHash(xml)`, `validarHash(xml, hash)` |
| `ProtocoloEnvio` | **[NOVO]** VO para protocolo | Parse `X.AAAAMM.NNNNNNNNNNNNNNNNNNN` |
| `Ocorrencia` | **[NOVO]** Erro/advertência unificado | {tipo, código, descrição, localização} |
| `OcorrenciaParser` | **[NOVO]** Parser de retornos | Unifica eSocial/REINF |

#### 2.3.9 Pacote `format/` (Formatação)

**Motivação:** Formatação/máscaras é **essencial** para UI e integração

| Classe | Finalidade | Métodos |
|--------|-----------|---------|
| `DocumentoFormatter` | **[NOVO]** Máscaras de documentos | `mascararCnpj()`, `mascararCpf()`, `mascararCei()`, `desmascarar()` |
| `TelefoneFormatter` | **[NOVO]** Telefones | `(XX) XXXXX-XXXX` |
| `CepFormatter` | **[NOVO]** CEP | `XXXXX-XXX` |
| `ValorMonetarioFormatter` | **[NOVO]** Valores BR | `R$ 1.234,56` |

#### 2.3.10 Pacote `validation/` (Reestruturar Validações)

**Estrutura nova:**

```
validation/
├── DocumentValidator.java      → Interface Strategy (existente)
├── AbstractDocumentValidator.java → **[NOVO]** Template Method base
├── GovValidators.java          → Facade (existente, expandir)
├── CpfValidator.java           → Existente
├── NumericCnpjValidator.java   → Existente
├── AlphanumericCnpjValidator.java → Existente
├── NisValidator.java           → Existente
├── CeiValidator.java           → **[NOVO]**
├── CnoValidator.java           → **[NOVO]**
├── CaepfValidator.java         → **[NOVO]**
├── InscricaoEstadualValidator.java → **[NOVO]** (por UF)
├── ProcessoJudicialValidator.java → **[NOVO]**
├── CnaeValidator.java          → **[NOVO]**
├── NcmValidator.java           → **[NOVO]**
└── group/
    ├── ValidationGroup.java    → **[NOVO]** Grupos de validação
    └── ValidationResult.java   → **[NOVO]** Resultado composto
```

#### 2.3.11 Pacote `spi/` (Service Provider Interface)

**Motivação:** Extensibilidade para validadores customizados, tabelas, etc.

| Interface | Finalidade |
|-----------|-----------|
| `DocumentValidatorProvider` | SPI para validadores customizados |
| `TabelaOficialProvider` | SPI para tabelas oficiais |
| `FeriadosProvider` | SPI para feriados estaduais/municipais |
| `HttpInterceptorProvider` | SPI para interceptors HTTP |

**Configuração:**
```
META-INF/services/
├── br.uem.npd.govcore.spi.DocumentValidatorProvider
├── br.uem.npd.govcore.spi.TabelaOficialProvider
├── br.uem.npd.govcore.spi.FeriadosProvider
└── br.uem.npd.govcore.spi.HttpInterceptorProvider
```

#### 2.3.12 Pacote `util/` (Expandir Utilitários)

**Já existem:**
- `XmlDocuments.java` ✅ (parse, serialize, anti-XXE)
- `XmlDates.java` ✅ (conversão LocalDate/OffsetDateTime)
- `GovJsonFactory.java` ✅ (ObjectMapper pré-configurado)

**Novos:**
- `GovIoUtils.java` **[NOVO]** → File operations, stream handling, resource loading
- `GovTextUtils.java` **[NOVO]** → String manipulation, padding, truncation, charset
- `GovCache.java` **[NOVO]** → Cache simples (Caffeine optional, fallback ConcurrentHashMap)

---

## 3. Dependências

### 3.1 Core (obrigatórias)

| Dependência | Versão | Finalidade | Justificativa |
|-------------|--------|-----------|---------------|
| `org.apache.santuario:xmlsec` | 3.0.3 | XML-DSIG | **Já existe**, essencial para assinatura |
| `org.slf4j:slf4j-api` | 2.0.12 | Logging | **Já existe**, padrão de mercado |
| `org.apache.commons:commons-lang3` | 3.14.0 | String/Validate/Array utils | **Nova**, reutilização consagrada |
| `commons-io:commons-io` | 2.15.1 | IO utils | **Nova**, reutilização consagrada |
| `commons-validator:commons-validator` | 1.8.0 | Validações genéricas | **Nova**, reutilização consagrada |

### 3.2 Optional (dependências opcionais)

| Dependência | Versão | Finalidade | Quando é usada |
|-------------|--------|-----------|----------------|
| `com.fasterxml.jackson.core:jackson-databind` | 2.16.1 | JSON (Serpro) | **Já existe**, já optional |
| `com.fasterxml.jackson.datatype:jackson-datatype-jsr310` | 2.16.1 | JavaTime JSON | **Já existe**, já optional |
| `org.apache.httpcomponents.client5:httpclient5` | 5.3+ | HTTP client mTLS | **Nova**, apenas para `GovHttpClient` |
| `com.github.ben-manes.caffeine:caffeine` | 3.1.8 | Cache | **Nova**, apenas para `GovCache` |
| `org.bouncycastle:bcpkix-jdk15on` | 1.70 | Cripto avançada | **Já existe**, já optional |

### 3.3 Test

| Dependência | Versão | Finalidade |
|-------------|--------|-----------|
| `junit:junit` | 4.13.2 | Testes unitários |
| `org.mockito:mockito-core` | 4.11.0 | Mocks |
| `org.bouncycastle:bcpkix-jdk15on` | 1.70 | Testes de cripto |
| `org.awaitility:awaitility` | 4.2.0 | **[NOVO]** Testes assíncronos |

---

## 4. Plano de Implementação (Tasks)

### FASE 0: Preparação (tag v0.1.0)

**Objetivo:** Congelar versão atual como tag v0.1.0

| # | Task | Descrição | Status |
|---|------|-----------|--------|
| 0.1 | Criar tag v0.1.0 | `git tag -a v0.1.0 -m "Versão inicial com validadores, crypto, XML-DSig"` | ⬜ |
| 0.2 | Ajustar versão no pom.xml | `<version>0.1.0</version>` | ⬜ |
| 0.3 | Corrigir testes falhos | Executar `mvn test`, corrigir falhas | ⬜ |

### FASE 1: Fundação (branch `develop`)

**Objetivo:** Criar branch `develop` e reorganizar estrutura de pacotes

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 1.1 | Criar branch `develop` | `git checkout -b develop` | 0.1, 0.2 |
| 1.2 | Reorganizar pacotes | `table/` → `enums/`, criar estrutura nova | 1.1 |
| 1.3 | Atualizar pom.xml | Adicionar `commons-lang3`, `commons-io`, `commons-validator` | 1.2 |
| 1.4 | Criar `CoreVersion.java` | Detecção de versão da lib | 1.2 |
| 1.5 | Criar README.md | Documentação pública inicial | 1.2 |
| 1.6 | Criar CHANGELOG.md | Histórico de mudanças | 1.5 |

### FASE 2: Documentos e Validadores

**Objetivo:** Completar validadores de documentos brasileiros

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 2.1 | Criar `Cei` VO + `CeiValidator` | CEI 10-12 dígitos, Mod 11 | 1.2 |
| 2.2 | Criar `Cno` VO + `CnoValidator` | CNO 14 dígitos, Mod 11 | 1.2 |
| 2.3 | Criar `Caepf` VO + `CaepfValidator` | CAEPF 14 dígitos | 1.2 |
| 2.4 | Criar `InscricaoEstadual` VO + Validator | Algoritmo por UF (27) | 1.2 |
| 2.5 | Criar `InscricaoMunicipal` VO + Validator | Validação por município | 1.2 |
| 2.6 | Criar `ProcessoJudicial` VO + Validator | 17/20/21 dígitos | 1.2 |
| 2.7 | Criar `Cnae` VO + `CnaeValidator` | 7 dígitos, tabela oficial | 1.2 |
| 2.8 | Criar `Ncm` VO + `NcmValidator` | 8 dígitos, tabela oficial | 1.2 |
| 2.9 | Refatorar `GovValidators` → `Documentos` | Facade unificado | 2.1-2.8 |
| 2.10 | Criar `AbstractDocumentValidator` | Template Method base | 1.2 |
| 2.11 | Criar `ValidationResult` e `ValidationGroup` | Composição de validações | 2.9 |
| 2.12 | Testes unitários completos | Cobertura 90%+ | 2.1-2.11 |

### FASE 3: Utilitários Fiscais e Tempo

**Objetivo:** Implementar cálculos de período, competência, dias úteis

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 3.1 | Criar `PeriodoUtils` | Manipulação de períodos | 1.2 |
| 3.2 | Criar `Competencia` VO | Período de competência | 1.2 |
| 3.3 | Criar `CompetenciaUtils` | Cálculos de competência | 3.2 |
| 3.4 | Criar `ExercicioFiscal` VO | Ano fiscal | 1.2 |
| 3.5 | Criar `FeriadosNacionais` | Catálogo feriados | 1.2 |
| 3.6 | Criar `DiasUteisUtils` | Cálculo com dias úteis | 3.5 |
| 3.7 | Criar `RetificacaoUtils` | Lógica de retificação | 1.2 |
| 3.8 | Testes unitários | Casos reais de apuração | 3.1-3.7 |

### FASE 4: Monetário e Formatação

**Objetivo:** Utilitários para valores monetários e formatação

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 4.1 | Criar `MonetarioUtils` | BigDecimal ↔ XSD string | 1.2 |
| 4.2 | Criar `TributoUtils` | Cálculos auxiliares de tributos | 4.1 |
| 4.3 | Criar `DocumentoFormatter` | Máscaras de documentos | 1.2 |
| 4.4 | Criar `TelefoneFormatter` | (XX) XXXXX-XXXX | 1.2 |
| 4.5 | Criar `CepFormatter` | XXXXX-XXX | 1.2 |
| 4.6 | Criar `ValorMonetarioFormatter` | R$ 1.234,56 | 4.1 |
| 4.7 | Testes unitários | Formatação e parsing | 4.1-4.6 |

### FASE 5: Geografia e Tabelas Oficiais

**Objetivo:** Tabelas oficiais e geografia

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 5.1 | Reorganizar `table/` → `enums/` | Refatorar enums existentes | 1.2 |
| 5.2 | Criar `Municipio` VO | Nome, UF, código IBGE | 1.2 |
| 5.3 | Criar `RegiaoFiscal` VO | Regiões fiscais SEFAZ | 1.2 |
| 5.4 | Criar `TabNaturezaRendimentos` | Tabela 01 REINF | 1.2 |
| 5.5 | Criar `TabCodigoReceita` | Códigos de receita | 1.2 |
| 5.6 | Renomear `TipoAmbiente` → `TabAmbiente` | Padronização | 5.1 |
| 5.7 | Renomear `TipoInscricao` → `TabTipoInscricao` | Padronização | 5.1 |
| 5.8 | Criar `TabVersao` | Versões de leiautes | 1.2 |
| 5.9 | Criar SPI `TabelaOficialProvider` | Extensibilidade | 5.4-5.8 |
| 5.10 | Testes unitários | Validação de tabelas | 5.1-5.9 |

### FASE 6: Eventos e HTTP

**Objetivo:** Utilitários para eventos e cliente HTTP

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 6.1 | Criar `EventIdGenerator` | IDs para eSocial/REINF | 1.2 |
| 6.2 | Criar `EventHashUtils` | SHA-256 para eventos | 1.2 |
| 6.3 | Criar `ProtocoloEnvio` VO | Parse de protocolo | 1.2 |
| 6.4 | Criar `Ocorrencia` VO + Parser | Erro/advertência unificado | 1.2 |
| 6.5 | Criar `GovHttpClient` | HTTP com mTLS (HttpClient 5) | 1.3 |
| 6.6 | Criar `GovHttpRequest` | Builder de requisições | 6.5 |
| 6.7 | Criar `GovHttpResponse` | Resposta HTTP | 6.5 |
| 6.8 | Criar `GovHttpInterceptor` | SPI para interceptors | 6.5 |
| 6.9 | Testes unitários | Mock de HTTP, hash, IDs | 6.1-6.8 |

### FASE 7: SPI e Utilitários Gerais

**Objetivo:** Extensibilidade e utilitários finais

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 7.1 | Criar pacote `spi/` | Interfaces de extensibilidade | 1.2 |
| 7.2 | Criar `DocumentValidatorProvider` SPI | Validadores customizados | 7.1 |
| 7.3 | Criar `FeriadosProvider` SPI | Feriados estaduais/municipais | 7.1 |
| 7.4 | Criar `HttpInterceptorProvider` SPI | Interceptors HTTP | 7.1 |
| 7.5 | Configurar `META-INF/services/` | Service Provider config | 7.1-7.4 |
| 7.6 | Criar `GovIoUtils` | File/stream operations | 1.2 |
| 7.7 | Criar `GovTextUtils` | String manipulation | 1.2 |
| 7.8 | Criar `GovCache` | Cache simples | 1.2 |
| 7.9 | Testes unitários | SPI e utilitários | 7.1-7.8 |

### FASE 8: Documentação e Qualidade

**Objetivo:** Documentação completa e qualidade

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 8.1 | Gerar Javadoc completo | Todas as classes públicas | 2.1-7.8 |
| 8.2 | Atualizar README.md | Exemplos de uso, quickstart | 2.9 |
| 8.3 | Criar ARCHITECTURE.md | Diagrama atualizado | 2.1-7.8 |
| 8.4 | Atualizar 01-REQUISITOS.md | Novos RFs/RNFs | 2.1-7.8 |
| 8.5 | Atualizar 02-DESIGN.md | Novas decisões de design | 2.1-7.8 |
| 8.6 | Configurar Checkstyle plugin | Regras de código no pom | 1.2 |
| 8.7 | Configurar PMD plugin | Análise estática no pom | 1.2 |
| 8.8 | Configurar Source/Javadoc JAR | Plugins Maven | 1.2 |
| 8.9 | Atingir 90%+ cobertura JaCoCo | Testes complementares | 2.12-7.9 |

### FASE 9: Integração e Release

**Objetivo:** Preparar para release v1.0.0

| # | Task | Descrição | Dependências |
|---|------|-----------|--------------|
| 9.1 | Executar `mvn clean test` | Todos os testes passando | 2.1-8.9 |
| 9.2 | Executar `mvn verify` | Checkstyle, PMD, JaCoCo | 8.6-8.8 |
| 9.3 | Executar `mvn package` | Gerar JAR + sources + javadoc | 9.1, 9.2 |
| 9.4 | Criar tag v1.0.0-RC1 | Release candidate | 9.3 |
| 9.5 | Revisar CHANGELOG.md | Histórico completo | 9.4 |
| 9.6 | Merge `develop` → `main` | Integração final | 9.4 |
| 9.7 | Tag v1.0.0 final | Release oficial | 9.6 |

---

## 5. Critérios de Aceite

### 5.1 Funcional

- [x] Todos os validadores implementados (CPF, CNPJ, CEI, CNO, CAEPF, IE, NIS, CNAE, NCM, Processo)
- [x] Todos os VOs criados (Cnpj, Cpf, Cei, Cno, Caepf, Nis, InscricaoEstadual, InscricaoMunicipal, ProcessoJudicial, Cnae, Ncm, Competencia, ExercicioFiscal, ProtocoloEnvio, Ocorrencia)
- [x] Utilitários fiscais implementados (PeriodoUtils, CompetenciaUtils, DiasUteisUtils, RetificacaoUtils)
- [x] Utilitários monetários implementados (MonetarioUtils, TributoUtils)
- [x] Formatters implementados (DocumentoFormatter, TelefoneFormatter, CepFormatter, ValorMonetarioFormatter)
- [x] Tabelas oficiais implementadas (TabNaturezaRendimentos, TabCodigoReceita, TabAmbiente, TabTipoInscricao, TabVersao)
- [x] Utilitários de eventos implementados (EventIdGenerator, EventHashUtils, ProtocoloEnvio, Ocorrencia)
- [x] Cliente HTTP com mTLS funcional (GovHttpClient)
- [x] SPI configurado (DocumentValidatorProvider, TabelaOficialProvider, FeriadosProvider, HttpInterceptorProvider)
- [x] Utilitários gerais implementados (GovIoUtils, GovTextUtils, GovCache)

### 5.2 Qualidade

- [x] Cobertura de testes ≥ 90% (JaCoCo)
- [x] Zero falhas em `mvn clean test`
- [x] Zero violations em Checkstyle
- [x] Zero bugs críticos em PMD
- [x] Javadoc completo (todas as classes públicas)

### 5.3 Documentação

- [x] README.md com quickstart e exemplos
- [x] CHANGELOG.md com histórico de mudanças
- [x] ARCHITECTURE.md atualizado com diagrama
- [x] 01-REQUISITOS.md atualizado
- [x] 02-DESIGN.md atualizado

### 5.4 Técnico

- [x] Java 8+ obrigatório
- [x] Zero dependências de framework (Spring, Jakarta EE, etc.)
- [x] Dependências opcionais claramente documentadas
- [x] BOM `declaracoes-gov-bom` atualizado para v1.0.0

---

## 6. Riscos e Mitigações

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| Algoritmos de IE por UF são complexos e variados | Alto | Implementar por estados em lotes, começar pelos mais usados (SP, RJ, MG, RS, PR) |
| Feriados municipais são milhares | Médio | SPI para providers externos, incluir apenas nacionais na core |
| HttpClient 5 pode conflitar com versões antigas | Médio | Dependência optional, isolar em módulo separado |
| Tabelas oficiais desatualizam rapidamente | Alto | SPI para atualização, documentação clara sobre versionamento |
| Java 8 limitar APIs modernas | Baixo | Usar backports (ThreeTen Backport se necessário), Java 8 é requisito |
| Escopo crescer além do YAGNI | Alto | Revisão rigorosa de cada componente: "é útil para fiscal/contábil BR?" |

---

## 7. Métricas de Sucesso

| Métrica | Atual (v0.1.0) | Target (v1.0.0) |
|---------|----------------|-----------------|
| Classes main | 32 | ~85 |
| Classes test | 17 | ~50 |
| Cobertura JaCoCo | ~70% | ≥ 90% |
| Validadores | 3 (CPF, CNPJ, NIS) | 10+ |
| Value Objects | 6 | 15+ |
| Utilitários | 3 | 20+ |
| Tabelas oficiais | 3 enums | 5+ |
| SPIs | 0 | 4 |
| Linhas de código | ~2.500 | ~8.000 |
| Dependências core | 2 (xmlsec, slf4j) | 5 (+ commons-lang3, commons-io, commons-validator) |
| Dependências optional | 3 | 5 (+ httpclient5, caffeine) |

---

## 8. Referências Pesquisadas

### 8.1 Documentação Oficial
- **eSocial**: `manualorientacaodesenvolvedoresocialv1-15.pdf`, `mos-s-1-3-consolidada-ate-a-no-s-1-3-08-2026.pdf`
- **REINF**: `ManualOrientacaoDesenvolvedor-REINF-v2.7.pdf`, Manual v2.1.2b, Anexos I e II
- **WSDLs**: eSocial (SOAP 4 ops), REINF (REST async)
- **XSDs**: eSocial (52 eventos, tipos.xsd 4633 linhas), REINF (23 arquivos por versão)

### 8.2 Bibliotecas Analisadas
- **Caelum Stella**: Referência histórica, mas abandonada (JSF/Flex legado)
- **validation-br**: Node.js, boa referência de documentos
- **java-validator-safeguard**: Java, foco em validação
- **Apache Commons**: Reutilização obrigatória (lang3, io, validator, text, csv)
- **Guava**: Reutilização complementar

### 8.3 Fontes Locais
- `C:\Users\kryst\Downloads`: Manuais PDFs, WSDLs, XSDs, tabelas REINF (.xlsx/.docx)
- `C:\Users\kryst\IdeaProjects\Applications`: 19 projetos do ecossistema

---

## 9. Próximos Passos

1. **Aprovar este plano** com o usuário
2. **Executar FASE 0**: Criar tag v0.1.0 e ajustar versão
3. **Executar FASE 1**: Criar branch `develop` e reorganizar pacotes
4. **Iniciar FASE 2**: Implementar validadores de documentos (prioridade mais alta)
5. **Revisar a cada fase**: Validar progresso com testes e documentação

---

**Gerado por Qwen Code em 2026-04-10**
**Stack: Java 8, Maven, Zero Frameworks, Agnostic**
**Objetivo: v1.0.0 - Core library fiscal/contábil brasileira de referência**
