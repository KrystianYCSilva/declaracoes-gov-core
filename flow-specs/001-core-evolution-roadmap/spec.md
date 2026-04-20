## Source Document

**Tipo:** `generic-discovery`
**Origem:** Síntese Copilot CLI (sessão `5abf4613`) × Gemini CLI — análise independente de 18+ projetos Java/Kotlin
**Data:** 2026-04-20
**Path:** `~/.copilot/session-state/fdb81c68.../plan.md`

### Resumo da análise

Dois agentes de IA analisaram de forma independente o ecossistema `IdeaProjects` (~18 projetos, ~12k arquivos Java/Kotlin) para identificar código técnico reutilizável sem lógica de negócio. Os resultados convergiram: há um **bug crítico** no módulo XML atual e ~40 candidatos a extração distribuídos em 4 módulos existentes, além de justificativa para um novo submódulo Kotlin (somente extensions) e 4 projetos cloud futuros fora do escopo desta lib.

---

## Problema

A biblioteca `declaracoes-gov-core` possui uma falha crítica no `XmlDsigSigner` (ausência da transformação C14N INCLUSIVE), o que causa o **Erro 142** ao submeter eventos ao portal eSocial/ReInF. Como workaround, dois projetos (`v2/esocial-tombamento`, `obrigacoes-service-reinf`) criaram implementações locais do signer — indicando que a lib não está servindo como fonte de verdade.

Paralelamente, a análise revelou que dezenas de utilitários técnicos JDK-puros (string, number, zip, date, domínio fiscal, exceções) estão duplicados em 2–6 projetos sem coordenação, gerando divergências de comportamento e regressões silenciosas.

---

## Cenários de Uso

| # | Ator | Cenário | Resultado esperado |
|---|------|---------|-------------------|
| U1 | Serviço eSocial/ReInF | Assina XML com `XmlDsigSigner` | Assinatura com ENVELOPED + C14N INCLUSIVE; zero erro 142 |
| U2 | Serviço qualquer | Valida CNPJ alfanumérico RF 2026 | `Cnpj.of("AB1234567890AB")` aceito; dígito verificador correto |
| U3 | Serviço qualquer | Comprime e base64-encoda payload GZIP | Usa `GovZipUtils` em vez de copiar 4 implementações divergentes |
| U4 | Serviço qualquer | Lança exceção de negócio com código de erro | `BusinessException(Integer codigo)` do domínio, sem framework |
| U5 | Desenvolvedor Kotlin | Filtra coleção nullable, converte YearMonth | Usa extensions idiomáticas sobre tipos Java existentes da lib |
| U6 | Serviço com certificado | Cria SSLContext dinâmico multi-tenant | Usa `GovSslContextFactory` no módulo crypto |

---

## Requisitos Funcionais

### RF-001 — Hotfix: C14N Transform no XmlDsigSigner *(Prioridade: CRÍTICA)*

- `XmlSignatureOptions` deve ter campo `boolean includeC14nTransform` (default `true`)
- `XmlDsigSigner.createReference()` deve incluir `CanonicalizationMethod.INCLUSIVE` quando `includeC14nTransform = true`
- Documentar no Javadoc que eSocial/ReInF requerem C14N (previne regressão)
- Testes de regressão verificam presença dos dois transforms na referência gerada

### RF-002 — CNPJ Alfanumérico RF 2026 *(Prioridade: ALTA — já duplicado em 2 projetos)*

- Interface `CnpjValidationStrategy` com `validate(String cnpj): boolean`
- `NumericCnpjValidationStrategy` — implementa validação existente
- `AlphanumericCnpjValidationStrategy` — regex `^[0-9A-Z]{12}[0-9]{2}$` + dígito verificador
- `CnpjValidationContext` — agrega strategies; seleciona strategy por formato de entrada
- `Cnpj.of(String)` existente expandido para aceitar alfanumérico via context

### RF-003 — Domínio Período/Vigência *(Prioridade: ALTA)*

- `ValidadorCPF` com cálculo de dígito verificador integrado a `Cpf.of()`
- Interface `Periodico` (`getPeriodo(): Integer`)
- Interface `Vigencia` (início, fim, setters) — verificar merge com tipo existente
- `VigenciaUtils` — `parseAnoMes`, `formatAnoMes`, `getPeriodoAtual/Seguinte/Anterior`, `calcularDiferencaMeses`
- `YearMonthIntegerConverter` — `yyyyMM` Integer ↔ `YearMonth`

### RF-004 — Hierarquia de Exceções Expandida *(Prioridade: ALTA)*

- `BusinessException(Integer codigo)` e `BusinessRuntimeException(Integer codigo)` sob `GovCoreException`
- `SchemaValidationException`
- `CertificadoInvalidoException` — extensão de `GovSignatureException`
- `ArquivoInvalidoReciboException`, `SemConexaoException`, `RetryableException`
- `PeriodoFaltanteException`, `PeriodoRepetidoException`

### RF-005 — Tipos de Domínio e Contratos *(Prioridade: MÉDIA)*

- `CallbackAsync<O>` — `onSuccess(O)`, `onFailure(O, Throwable)`. Zero dep.
- `CertificadoDTO` — `{certificadoBase64: String, senha: String}`. Serializable.
- `Filter` {name, value} + `FilterCollection` (Iterable<Filter> + fluent builder)
- `ActiveProfile` enum (PROD, HOM, DEV) e `DateFormatType` enum
- Annotations `@Description`, `@IgnoreElement` e enum `FieldType`

### RF-006 — GovZipUtils *(Prioridade: ALTA — 4 cópias idênticas)*

- `compressAndEncodeBase64(String): String`
- `decompress(byte[]): byte[]`
- `zipToString(Object): String`

### RF-007 — GovStringUtils e GovNumberUtils *(Prioridade: MÉDIA)*

- `GovStringUtils`: consolidar ~15 métodos de back-core + framework + esocial
- `GovNumberUtils`: 11 métodos null-safe BigDecimal (add, subtract, multiply, divide, isZero, isPositive, max, percentage…)
- `GovBigDecimalConstants` (CEM, ZERO_DECIMAL)

### RF-008 — Expansão GovDateUtils e Utilitários de Coleção/Arquivo *(Prioridade: MÉDIA)*

- `GovDateUtils`: `convertToLocalDateTime(Date)`, `parsePeriodoToLocalDateTime(Integer)`, `getStartMinuteDateForQuery`, `getLastMinuteDateForQuery`
- `VigenciaConverter<P,V>` e `VigenciaValidator` desacoplados de Spring (remover `@Component`)
- `GovCollectionUtils`: `isNullOrEmpty(List/Array)`, `orNull()`, `getFirst()`
- `GovFileUtils`: `getMagicNumbers(byte[])`, `getMimeType(byte[])`, `throwIfMalicious(byte[])`

### RF-009 — Expansão do Módulo Crypto *(Prioridade: MÉDIA)*

- `KeyManagerFactoryBuilder` — `createKeyManagerFactory(pfxBase64, password): KeyManagerFactory` (JDK puro)
- `GovSslContextFactory` — criação de `SSLContext` dinâmico multi-tenant por CNPJ

### RF-010 — Novo Submódulo Kotlin: Extension Functions *(Prioridade: BAIXA)*

- Escopo restrito: **somente extension functions** sobre tipos Java existentes da lib
- **NÃO inclui** logger DSL, lógica negocial, dependências além de `kotlin-stdlib`
- Extensions para tipos de domínio: `String.toCnpj()`, `String.toCpf()`, null-safety sobre value objects
- Extensions de data: `LocalDate.toYearMonth()`, `YearMonth.toPeriodo()`, `LocalDateTime.toUtc/toBrasilia()`
- Extensions de coleção: `List<T>.orNull()`, `Optional<T>.orNull()`, `T?.whenNullThrow()`
- Extensions JSON (usa Jackson já opcional no format): `T.toJsonOrNull()`, `String.fromJsonOrNull<T>()`

---

## Requisitos Não-Funcionais

| # | Requisito |
|---|-----------|
| NF-01 | Nenhum módulo do reator conhece endpoint, token, fila ou protocolo de entrega (DD-05) |
| NF-02 | Módulos domain e formato devem manter JaCoCo ≥ 90% linha + 90% branch |
| NF-03 | Módulo crypto mantém JaCoCo ≥ 85% linha + 90% branch |
| NF-04 | Nenhuma dependência nova de framework (Spring, Jakarta EE, Bean Validation, Lombok) |
| NF-05 | Código legado (Java 8 `source`/`target` 1.8) — sem APIs pós Java 8 |
| NF-06 | Komments e Javadoc em português (padrão do projeto) |
| NF-07 | Build limpo via `mvn -B -q verify` no reactor completo |

---

## Critérios de Sucesso

| # | Critério | Como verificar |
|---|----------|---------------|
| S1 | Bug C14N corrigido | `XmlDsigSignerTest` valida presença de ENVELOPED + C14N INCLUSIVE na referência |
| S2 | `Cnpj.of("AB1234567890AB")` aceito com dígito verificador correto | Teste unitário com exemplos RF 2026 |
| S3 | `mvn -B -q verify` verde com todos os novos módulos | CI passa sem erros |
| S4 | JaCoCo gates não caem abaixo do mínimo configurado | Relatório JaCoCo no CI |
| S5 | Projetos `v2/esocial-tombamento` e `obrigacoes-service-reinf` podem migrar para a lib sem workaround | Smoke test de compatibilidade (manual) |
| S6 | Módulo Kotlin compila com `kotlin-stdlib` only, sem transitive de Spring/GCP | `mvn dependency:tree` no novo módulo |
| S7 | GovZipUtils tem comportamento idêntico às 4 implementações existentes | Testes com fixtures dos projetos originais |

---

## Restrições e Riscos

| # | Tipo | Descrição |
|---|------|-----------|
| R1 | Constraint | Java 8 — impossibilita `var`, records, sealed classes, text blocks |
| R2 | Constraint | Kotlin no submódulo requer `kotlin-maven-plugin` e decisão de versão Kotlin alinhada com reinf/bardo |
| R3 | Risco | Interface `Vigencia` já existe no domínio — verificar antes de criar conflito de nomes |
| R4 | Risco | `GovStringUtils` pode conflitar com `GovTextNormalizer` existente — fazer merge cuidadoso |
| R5 | Risco | Retrocompatibilidade do `XmlDsigSigner`: consumidores que dependiam do comportamento ENVELOPED-only podem quebrar se `includeC14nTransform` default = `true` — avaliar se default deve ser `false` (opt-in) |
| R6 | Dependência | WPs de format dependem de WPs de domain (VigenciaConverter precisa de Periodico/Vigencia) |

---

## Fora de Escopo

- Logger DSL (→ projeto futuro separado)
- PubSub, HTTP clients, ControllerAdvice, Keycloak, GCP utilities (→ viola DD-05)
- Regras de negócio específicas de eSocial, ReInF, DIRF, etc.
- Código gerado de XSD/JAXB
- `TrasmissaoException` com `Runtime.exec("kill -15")` e `System.exit(1)` (anti-pattern, não extrair)

---

## Dependências Externas

Nenhuma dependência nova necessária para Fases 0–3. A Fase 4 (Kotlin) requer:
- `kotlin-stdlib` (versão a definir, alinhar com projetos consumidores)
- `kotlin-maven-plugin` no parent pom
- `jackson-module-kotlin` (opcional, para extensions JSON — já usa Jackson no format)

---

## Premissas

- Branch de destino: `develop`
- Logger DSL confirmado como projeto futuro (fora desta lib)
- Kotlin module = somente extension functions sobre tipos Java existentes; sem lógica negocial
- `XmlSignatureOptions.includeC14nTransform` default = `true` (breaking change justificado — o comportamento atual é um bug)

---

## Appendix: Original Discovery Document

Ver: `C:/Users/krystian.silva_conta/.copilot/session-state/fdb81c68-455d-4d81-bb8b-78f780a74830/plan.md`
