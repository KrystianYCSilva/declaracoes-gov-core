# Plano de Reestruturação: declaracoes-gov-core v1.0.0

> **Autor:** Kimi CLI  
> **Data:** 10/04/2026  
> **Versão Atual:** 0.1.0-SNAPSHOT → **Versão Alvo:** 1.0.0  
> **Stack:** Java 8+ (Agnóstico de Framework)  

---

## 1. VISÃO GERAL E FILOSOFIA

### 1.1 Propósito da Lib

A `declaracoes-gov-core` é a **fundação do ecossistema fiscal brasileiro** - não apenas um suporte para libs de declarações, mas um **framework de abstração legal** para ERPs fiscais e contábeis. Ela deve:

- **Abstrair complexidade legal brasileira** em Value Objects imutáveis e validadores confiáveis
- **Garantir conformidade técnica** com especificações da Receita Federal, Serpro, SPED, eSocial, EFD-Reinf
- **Eliminar reinventação de roda** reusando Apache Commons, Guava quando aplicável
- **Manter agnosticidade total** - ZERO dependências de frameworks (Spring, Jakarta EE, etc.)

### 1.2 Princípios Não-Negociáveis

| Princípio | Descrição |
|-----------|-----------|
| **YAGNI** | Apenas o que é útil para o contexto fiscal brasileiro |
| **Imutabilidade** | Todos os Value Objects são `final` e thread-safe |
| **Fail-Fast** | Validações no construtor/factory - objetos inválidos nunca existem |
| **Zero External Dependencies** (exceto utilitários) | Jackson (optional), SLF4J (facade), Apache Commons (approved) |
| **Retrocompatibilidade** | CNPJ alfanumérico deve validar CNPJs numéricos legados |
| **Conformidade Oficial** | Algoritmos validados contra documentação RFB/Serpro |

---

## 2. ANÁLISE DO ESTADO ATUAL (v0.1.0)

### 2.1 Estrutura Existente

```
src/main/java/br/com/contabilizei/obrigacoes/govcore/
├── crypto/           # Provedores de keystore, certificados (PKCS11, PKCS12)
├── exception/        # Hierarquia de exceções (GovCoreException, etc.)
├── model/            # Value Objects: Cnpj, Cpf, Nis, PeriodoApuracao, Vigencia, etc.
├── signature/        # Assinatura XML (XMLDsig, ICP-Brasil)
├── table/            # Enums: Uf, TipoInscricao, TipoAmbiente
├── util/             # Utilitários: XmlDates, XmlDocuments, GovJsonFactory
└── validator/        # Validadores: Cnpj (numérico/alfanumérico), Cpf, Nis
```

### 2.2 Pontos Fortes ✅

- Implementação correta do **CNPJ Alfanumérico** (ASCII - 48, Módulo 11)
- Value Objects imutáveis com factory methods `of()`
- Hierarquia de exceções bem definida
- Suporte a certificados digitais (A1, A3, PKCS11)
- PeríodoApuracao com YearMonth (Java 8 time API)

### 2.3 Lacunas Identificadas ❌

| Categoria | Omissão | Impacto |
|-----------|---------|---------|
| **Documentos** | CAEPF, CNO, CEI, CGC | eSocial/EFD-Reinf requerem |
| **Endereço** | CEP, Logradouro sem validação | Endereçamento incompleto |
| **Fiscal** | CNAE (7 dígitos), Código País (1058) | Classificação econômica |
| **Tributário** | Código Imposto (IRPJ, CSLL, PIS, COFINS, etc.) | Apuração tributária |
| **Períodos** | PeríodoArquivamento (AAAAMM), Competencia | Outros formatos SPED |
| **Validação** | Inscrição Estadual (UF-dependent) | NF-e, CT-e |
| **Texto** | Normalização de strings fiscais (acentos, case) | Consistência de dados |
| **IO** | Leitura/escrita de arquivos SPED (ISO-8859-1) | EFD-ICMS, EFD-Contrib |
| **Lang** | Resultado<T>, Optional utilities | Programação funcional |

---

## 3. ARQUITETURA ALVO (v1.0.0)

### 3.1 Estrutura de Módulos (Estilo Apache Commons)

```
declaracoes-gov-core/
├── gov-core-lang/          # Extensões de linguagem, Result, Try
├── gov-core-text/          # Normalização, sanitização de strings fiscais
├── gov-core-io/            # Leitura/escrita de arquivos SPED, encoding
├── gov-core-docs/          # Todos os documentos (CNPJ, CPF, CEP, etc.)
├── gov-core-fiscal/        # Períodos, Vigências, CNAE, Código País
├── gov-core-tributario/    # Códigos de imposto, CST, CFOP (básico)
├── gov-core-crypto/        # Certificados, assinatura (já existe)
├── gov-core-validation/    # Framework de validação reutilizável
└── gov-core-xml/           # Utilitários XML (já existe)
```

> **NOTA:** Para v1.0.0, manteremos como **single module** mas com estrutura de packages preparada para futura divisão.

### 3.2 Diagrama de Pacotes

```java
br.com.contabilizei.obrigacoes.govcore
├── .lang                    # Try<T>, Result<T>, Preconditions
├── .text                    # StringFiscal.normalize(), removeAccents()
├── .io                      # SpedFileReader, EncodingDetector
├── .document                # Cnpj, Cpf, Cep, Cnae, CodigoPais
│   ├── .validator           # DocumentValidator<T>
│   └── .formatter          # DocumentFormatter<T>
├── .fiscal                  # PeriodoApuracao, Competencia, Vigencia
│   ├── .period             # Períodos específicos
│   └── .tables             # Tabelas fiscais (natureza jurídica, etc.)
├── .tributario              # CodigoImposto, Cst, Cfop (básico)
├── .crypto                  # (existente)
├── .signature              # (existente)
├── .exception              # (existente)
└── .util                   # (existente - deprecate gradual)
```

---

## 4. CATÁLOGO DE ARTEFATOS (v1.0.0)

### 4.1 Documentos (br.com.contabilizei.obrigacoes.govcore.document)

| Classe | Descrição | Validação | Formatação |
|--------|-----------|-----------|------------|
| `Cnpj` | Cadastro Nacional PJ | Alfanumérico (ASCII-48) | XX.XXX.XXX/XXXX-XX |
| `Cpf` | Cadastro PF | Módulo 11 | XXX.XXX.XXX-XX |
| `Cep` | Código Endereçamento Postal | 8 dígitos | XXXXX-XXX |
| `Cnae` | Classificação Nacional Atividade Econômica | 7 dígitos, DV opcional | XXXX-X/XX |
| `CodigoPais` | Código país BACEN/RFB | 4 dígitos | - |
| `CodigoMunicipio` | Código IBGE (já existe) | 7 dígitos | - |
| `Nis` | PIS/PASEP/NIT (já existe) | Módulo 11 específico | XXX.XXXXX.XX-X |
| `Caepf` | Cadastro Atividade Econômica PF | 14 dígitos | - |
| `Cno` | Cadastro Nacional de Obra | 12 dígitos | - |
| `Cei` | Cadastro Específico INSS | 12 dígitos | DV módulo 11 |
| `InscricaoEstadual` | IE por estado | UF-dependent | - |

### 4.2 Períodos Fiscais (br.com.contabilizei.obrigacoes.govcore.fiscal.period)

| Classe | Formato | Uso Principal |
|--------|---------|---------------|
| `PeriodoApuracao` | yyyy-MM | eSocial, EFD-Reinf, DCTFWeb |
| `Competencia` | MMyyyy ou MM/yyyy | Visualização, relatórios |
| `PeriodoArquivamento` | yyyyMM | SPED Fiscal (bloco 0) |
| `AnoCalendario` | yyyy | IRPF, DIRF |
| `DataFiscal` | LocalDate com validações | Datas em contexto fiscal |

### 4.3 Tabelas Fiscais (br.com.contabilizei.obrigacoes.govcore.fiscal.table)

| Enum | Descrição | Fonte |
|------|-----------|-------|
| `Uf` | Unidades da Federação + EX | IBGE/RFB |
| `TipoInscricao` | 1=CNPJ, 2=CPF, 3=CAEPF, 4=CNO, 5=CGC, 6=CEI | eSocial/EFD-Reinf |
| `TipoAmbiente` | 1=Produção, 2=Homologação | Padrão SPED |
| `NaturezaJuridica` | 2 dígitos (RFB) | Cadastro CNPJ |
| `CodigoImposto` | IRPJ, CSLL, PIS, COFINS, etc. | SPED/DCTF |
| `IndicadorMes` | Janeiro a Dezembro + 13º | Folha de pagamento |

### 4.4 Utilitários de Texto (br.com.contabilizei.obrigacoes.govcore.text)

| Classe | Função |
|--------|--------|
| `StringFiscal` | Normalização de strings fiscais |
| `StringFiscal.normalize(String)` | Remove acentos, converte para maiúsculas |
| `StringFiscal.sanitizeForXml(String)` | Remove caracteres não permitidos em XML |
| `StringFiscal.onlyNumbers(String)` | Extrai apenas dígitos |
| `StringFiscal.onlyAlphanumeric(String)` | Extrai letras e números |
| `StringFiscal.lpad(String, int, char)` | Preenche à esquerda |
| `StringFiscal.rpad(String, int, char)` | Preenche à direita |

### 4.5 IO (br.com.contabilizei.obrigacoes.govcore.io)

| Classe | Função |
|--------|--------|
| `SpedFileReader` | Leitor de arquivos SPED (ISO-8859-1, delimitado por pipe) |
| `SpedFileWriter` | Escritor de arquivos SPED |
| `EncodingDetector` | Detecta encoding de arquivos fiscais |
| `DelimitedFileParser` | Parser genérico para arquivos delimitados |

### 4.6 Lang (br.com.contabilizei.obrigacoes.govcore.lang)

| Classe | Inspirado em | Função |
|--------|--------------|--------|
| `Result<T>` | Rust/Vavr | Container para sucesso/falha |
| `Try<T>` | Scala/Vavr | Execução segura de operações |
| `Preconditions` | Guava | Validações prévias |
| `Tuples` | Apache Commons | Pares e triplas imutáveis |

---

## 5. ESPECIFICAÇÕES TÉCNICAS DETALHADAS

### 5.1 CNPJ Alfanumérico (Implementado ✅)

```java
// Validação conforme Nota Técnica Conjunta 2025.001
// Fórmula: Valor = ASCII(c) - 48
// '0' = 0, '9' = 9, 'A' = 17, 'Z' = 42

Cnpj cnpj = Cnpj.of("12.ABC.345/01DE-35"); // Válido
Cnpj legacy = Cnpj.of("11.222.333/0001-81"); // Também válido (retrocompatível)

// Estrutura: [A-Z0-9]{12}\d{2}
// DV calculado via Módulo 11 com pesos rotativos
```

**Vetores de Teste Oficiais:**
- `12.ABC.345/01DE-35` → DVs esperados: 35 ✅
- `00.000.000/0001-91` → DVs esperados: 91 ✅

### 5.2 CEP (Novo)

```java
public final class Cep {
    // 8 dígitos numéricos
    // Formatação: XXXXX-XXX
    // Validação: Faixas por UF (opcional, via serviço externo)
}
```

### 5.3 CNAE (Novo)

```java
public final class Cnae {
    // Estrutura: DDDDD-D/DD (onde D é dígito)
    // 7 dígitos: Seção(1) + Divisão(2) + Grupo(1) + Classe(2) + Subclasse(2)
    // Exemplo: 62.01-5/01 = Desenvolvimento de software sob encomenda
    
    public String getSecao();        // "C"
    public String getDivisao();      // "62"
    public String getGrupo();        // "01"
    public String getClasse();       // "6201-5"
    public String getSubclasse();    // "6201501"
}
```

### 5.4 Inscrição Estadual (Novo)

```java
public final class InscricaoEstadual {
    // Validação por UF específica
    // SP, MG, RJ têm algoritmos próprios
    // IEIsenta, IENaoContribuinte (estados especiais)
    
    public static InscricaoEstadual of(String ie, Uf uf);
    public boolean isIsenta();
    public boolean isNaoContribuinte();
}
```

### 5.5 Períodos Fiscais

```java
// Período de Apuração (já existe, reforçar)
PeriodoApuracao pa = PeriodoApuracao.of(2025, 1); // Janeiro/2025
pa.toXmlFormat();    // "2025-01" (eSocial, EFD-Reinf)
pa.toPlainFormat();  // "202501" (compacto)
pa.getMesSeguinte(); // Fev/2025
pa.getMesAnterior(); // Dez/2024
pa.isBefore(other);  // comparação
pa.isAfter(other);   // comparação

// Novo: Competencia
Competencia c = Competencia.parse("01/2025");
c.toMmaaaa();        // "012025"
c.toAaaamm();        // "202501"

// Novo: Período de Arquivamento
PeriodoArquivamento pa = PeriodoArquivamento.of(2025, 1);
pa.toString();       // "202501"
```

---

## 6. IMPLEMENTATION ROADMAP

### Fase 0: Preparação (v0.1.0 Release)

```markdown
- [ ] Criar tag `v0.1.0` no estado atual
- [ ] Atualizar pom.xml: version = 0.1.0
- [ ] Criar branch `develop` a partir de master
- [ ] Documentar breaking changes (se houver)
```

### Fase 1: Fundação (Semanas 1-2)

```markdown
- [ ] Criar pacote `br.com.contabilizei.obrigacoes.govcore.lang`
  - [ ] Implementar `Result<T>` com Success/Failure
  - [ ] Implementar `Try<T>` com map/recover
  - [ ] Implementar `Preconditions` (nonNull, nonEmpty, checkArgument)
  
- [ ] Criar pacote `br.com.contabilizei.obrigacoes.govcore.text`
  - [ ] Implementar `StringFiscal` com normalização Unicode
  - [ ] Implementar remoção de acentos (NFKD)
  - [ ] Implementar sanitização XML
  
- [ ] Refatorar validadores para usar `Result<T>`
  - [ ] `DocumentValidator<T>` retorna `Result<T>`
  - [ ] Manter compatibilidade via métodos `isValid()`
```

### Fase 2: Documentos Core (Semanas 3-4)

```markdown
- [ ] Implementar `Cep` (Value Object)
  - [ ] Validação 8 dígitos
  - [ ] Formatação XXXXX-XXX
  - [ ] Tests: 100% cobertura
  
- [ ] Implementar `Cnae` (Value Object)
  - [ ] Validação 7 dígitos
  - [ ] Parsing hierárquico
  - [ ] Tabela de seções (A-U)
  
- [ ] Implementar `CodigoPais` (Value Object)
  - [ ] Tabela BACEN (1058 = Brasil, etc.)
  - [ ] Atualizar via fonte oficial RFB
  
- [ ] Implementar `Caepf` (Value Object)
  - [ ] Estrutura: 14 dígitos numéricos
  - [ ] Validação básica
  
- [ ] Implementar `Cno` (Value Object)
  - [ ] Estrutura: 12 dígitos
  - [ ] Validação básica
  
- [ ] Implementar `Cei` (Value Object)  
  - [ ] Estrutura: 12 dígitos com DV
  - [ ] Algoritmo DV específico CEI
```

### Fase 3: Períodos e Tabelas (Semanas 5-6)

```markdown
- [ ] Refatorar `PeriodoApuracao`
  - [ ] Usar `Result<PeriodoApuracao>` em factories
  - [ ] Adicionar operações de range (between, overlaps)
  - [ ] Suporte a períodos trimestrais (1T, 2T...)
  
- [ ] Implementar `Competencia`
  - [ ] Parsing flexível (MM/yyyy, yyyy-MM, MMyyyy)
  - [ ] Conversão para PeriodoApuracao
  
- [ ] Implementar `PeriodoArquivamento`
  - [ ] Formato yyyyMM
  - [ ] Validação de períodos fiscais
  
- [ ] Expandir tabelas fiscais
  - [ ] `NaturezaJuridica` (completa)
  - [ ] `CodigoImposto` (IRPJ, CSLL, PIS, COFINS, IPI, ICMS, ISS)
  - [ ] `IndicadorMes` (enum 1-13)
```

### Fase 4: IO e Arquivos SPED (Semanas 7-8)

```markdown
- [ ] Implementar `SpedFileReader`
  - [ ] Encoding ISO-8859-1
  - [ ] Parsing de registros (| delimitado)
  - [ ] Streaming para arquivos grandes
  - [ ] Validação de estrutura hierárquica
  
- [ ] Implementar `SpedFileWriter`
  - [ ] Geração de registros formatados
  - [ ] Validação de campos obrigatórios
  - [ ] Quebra de linha CRLF (padrão SPED)
  
- [ ] Implementar `EncodingDetector`
  - [ ] Detecção de UTF-8, ISO-8859-1, Windows-1252
  - [ ] Fallback sensato
```

### Fase 5: Inscrição Estadual (Semanas 9-10)

```markdown
- [ ] Implementar `InscricaoEstadual` base
  - [ ] Interface comum
  - [ ] Estados especiais (Isento, Não Contribuinte)
  
- [ ] Implementar validadores por UF
  - [ ] SP (Módulo 11 com pesos)
  - [ ] MG (algoritmo específico)
  - [ ] RJ (Módulo 11)
  - [ ] RS, PR, SC (principais estados)
  - [ ] Demais UFs (validação básica)
  
- [ ] Tabela de regras por UF
  - [ ] Tamanhos permitidos
  - [ ] Máscaras de formatação
```

### Fase 6: Integração e Polish (Semanas 11-12)

```markdown
- [ ] Refatorar pacotes para estrutura alvo
- [ ] Depreciar classes em `util` (mover para pacotes específicos)
- [ ] Atualizar todos os tests para JUnit 5 (se possível em Java 8)
- [ ] Garantir 90%+ cobertura de testes
- [ ] Documentação JavaDoc completa
- [ ] Guia de migração v0.1.0 → v1.0.0
- [ ] Benchmark de performance (comparar com v0.1.0)
```

---

## 7. CRITÉRIOS DE ACEITAÇÃO

### 7.1 Qualidade de Código

- [ ] Cobertura de testes: **mínimo 90%** (linhas), **80%** (branches)
- [ ] Zero warnings do compiler ( `-Xlint:all` )
- [ ] SpotBugs/ErrorProne sem erros críticos
- [ ] Checkstyle conforme padrão do projeto

### 7.2 Conformidade Fiscal

- [ ] CNPJ alfanumérico valida vetores de teste oficiais
- [ ] CPF valida contra casos de borda (repetidos, DVs especiais)
- [ ] IE de SP, MG, RJ validam corretamente
- [ ] Tabelas de UF, País, CNAE atualizadas (2025)

### 7.3 Performance

- [ ] Criação de Value Objects: < 1μs (microssegundo)
- [ ] Parsing de arquivo SPED 100MB: < 5s
- [ ] Memória: sem leaks em operações de IO

### 7.4 Compatibilidade

- [ ] Java 8, 11, 17, 21 (testar em todas)
- [ ] Retrocompatibilidade: código usando v0.1.0 funciona em v1.0.0
- [ ] Opcional: Android API 26+ (verificar)

---

## 8. REFERÊNCIAS OFICIAIS

### 8.1 Documentação Receita Federal / Serpro

| Documento | URL | Relevância |
|-----------|-----|------------|
| Manual Cálculo DV CNPJ Alfanumérico | [gov.br](https://www.gov.br/receitafederal/pt-br/centrais-de-conteudo/publicacoes/documentos-tecnicos/cnpj) | Algoritmo de validação |
| Nota Técnica Conjunta CNPJ Alf. | [nfe.fazenda.gov.br](http://www.nfe.fazenda.gov.br/PORTal/exibirArquivo.aspx) | Especificação completa |
| eSocial - Manual de Orientação | [gov.br/esocial](https://www.gov.br/esocial/pt-br/documentacao-tecnica/manuais) | Tabelas e leiautes |
| EFD-Reinf - Leiautes | [sped.rfb.gov.br](http://sped.rfb.gov.br/pagina/show/1494) | Estrutura XML |
| SPED Fiscal - EFD ICMS IPI | [sped.rfb.gov.br](http://sped.rfb.gov.br/item/show/1573) | Leiautes e notas técnicas |
| Tabela CNAE | [cnae.ibge.gov.br](https://cnae.ibge.gov.br) | Códigos de atividade |
| Código País BACEN | [bcb.gov.br](https://www.bcb.gov.br) | Tabela de países |

### 8.2 Leis e Normas

- **Instrução Normativa RFB nº 2.229/2024** - CNPJ Alfanumérico
- **Ato Cotepe nº 79/2025** - EFD ICMS IPI 2026
- **Decreto nº 8.373/2014** - eSocial
- **Resoluções IBGE/CONCLA** - CNAE

---

## 9. NOTAS E DECISÕES

### 9.1 Decisões Arquiteturais

1. **Manter Java 8**: Compatibilidade máxima com sistemas legados fiscais
2. **Single Module (por enquanto)**: Divisão em módulos Maven apenas quando justificado
3. **Sem Lombok**: Clareza sobre o que o código faz, mesmo que mais verboso
4. **Sem Records (Java 16+)**: Manter Java 8 como baseline

### 9.2 O que FOI descartado (YAGNI)

- ❌ Suporte a CNPJ com letras minúsculas (RFB exige maiúsculas)
- ❌ Validação de blacklist de CNPJ (letras proibidas) - responsabilidade da RFB
- ❌ Framework de ORM/JPA - agnosticidade
- ❌ Suporte a moedas (BigDecimal utilities) - fora do escopo fiscal
- ❌ Validação de NF-e (Chave de Acesso) - pertence à lib nf-e

### 9.3 Pós-v1.0.0 (Backlog Futuro)

- Suporte a EFD-Contribuições (PIS/COFINS)
- CST (Código de Situação Tributária) completo
- CFOP (Código Fiscal de Operações) básico
- Tabela de Natureza da Operação (SPED)
- Suporte a DACTE (CT-e)

---

## 10. CHECKLIST DE RELEASE v1.0.0

```markdown
### Pré-release
- [ ] Todas as features implementadas
- [ ] Todos os tests passando
- [ ] Documentação JavaDoc 100% pública
- [ ] Guia de migração escrito
- [ ] CHANGELOG.md atualizado

### Release
- [ ] Merge para master
- [ ] Tag `v1.0.0` criada
- [ ] Deploy para repositório Maven (Nexus/Artifactory)
- [ ] Anúncio/documentação publicada

### Pós-release
- [ ] Branch `develop` atualizada
- [ ] Planejamento v1.1.0 iniciado
```

---

**FIM DO PLANO**

> *"A complexidade do fisco brasileiro merece uma fundação sólida. Esta lib é essa fundação."*
