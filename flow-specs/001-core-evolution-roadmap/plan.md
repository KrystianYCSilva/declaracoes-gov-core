# Plan: Core Evolution Roadmap

## Project Conventions

| Category | Value |
|----------|-------|
| Language | Java 8 (`maven.compiler.source=1.8`, `target=1.8`) |
| Build | Maven 3.x multi-module reactor (`mvn -B -q verify`) |
| Test framework | JUnit 4.13.2 + Mockito 4.11.0 |
| Coverage | JaCoCo 0.8.11 — `domain`/`format`/`xml`: ≥90% line + ≥90% branch; `crypto`: ≥85% line + ≥90% branch |
| CI/CD | GitHub Actions `.github/workflows/ci.yml` → `mvn -B -q verify` on push/PR to main/develop |
| Javadoc/comments | Português (pt-BR) |
| No frameworks | Sem Spring, Jakarta EE, Bean Validation, Lombok em nenhum módulo |
| Package base | `br.uem.npd.govcore` |
| Modules | `bom`, `domain`, `format`, `crypto`, `xml`, `transport` (existentes) + `kotlin` (novo) |

---

## Summary

Evolução da biblioteca `declaracoes-gov-core` em 4 fases sequenciais prioritizadas:

1. **Fase 0 — HOTFIX**: Corrigir `XmlDsigSigner` com C14N INCLUSIVE (Erro 142 no portal gov)
2. **Fase 1 — domain**: Expandir com CNPJ alfanumérico RF 2026, domínio Período/Vigência, hierarquia de exceções e tipos de contrato
3. **Fase 2 — format**: Consolidar 4+ duplicatas (GovZipUtils, GovStringUtils, GovNumberUtils, DateUtils) e desacoplar converters de Spring
4. **Fase 3 — crypto**: Adicionar KeyManagerFactoryBuilder e GovSslContextFactory
5. **Fase 4 — kotlin (novo módulo)**: Somente extension functions Kotlin sobre os tipos Java existentes; sem lógica negocial; sem logger DSL (projeto futuro)

Todos os itens respeitam DD-05: nenhum módulo conhece endpoint, fila, token ou protocolo.

---

## Technical Context

| Item | Detalhe |
|------|---------|
| Language / Runtime | Java 8 (source 1.8, target 1.8) |
| Build system | Maven 3.x reactor — `pom.xml` raiz gerencia versões e plugins |
| Dependencies (existentes) | Jackson 2.16.1 (format, opcional), Apache Santuario xmlsec 3.0.3 (xml), BouncyCastle bcpkix 1.78.1 (test scope crypto) |
| Dependencies (novas, Fase 4) | `kotlin-stdlib` (versão a definir), `kotlin-maven-plugin`, opcionalmente `jackson-module-kotlin` |
| Testing | JUnit 4 `@Test`, Mockito, fixtures de projetos de origem para validar comportamento idêntico |
| Coverage gates | JaCoCo enforced no CI — `mvn verify` falha se abaixo do mínimo |
| Key constraint | Módulo domain = zero dependências externas; format = somente `domain` + Jackson opcional |

---

## Constitution Check

Nenhum arquivo `.flowflow/memory/constitution.md` encontrado. Usando como fonte de verdade:
- `AGENTS.md` do repositório (custom instruction do projeto)
- `docs/02-DESIGN.md` (decisões arquiteturais, inclui DD-05)
- `.context/standards/architectural-rules.md` (AR-001 a AR-009)

**Alinhamento confirmado**: todos os WPs respeitam o constraint fundamental DD-05.

---

## Project Structure

```
declaracoes-gov-core/
├── declaracoes-gov-core-bom/          # BOM — sem mudanças de código
├── declaracoes-gov-core-domain/       # WP02, WP03, WP04, WP05
│   └── src/main/java/br/uem/npd/govcore/
│       ├── model/                     # Cnpj, Cpf (expand), CertificadoDTO, Filter, FilterCollection
│       ├── model/layout/              # sem mudanças
│       ├── table/                     # ActiveProfile (novo), DateFormatType (novo)
│       ├── validator/                 # CnpjValidationStrategy, Context, impls; ValidadorCPF
│       ├── exception/                 # BusinessException, BusinessRuntimeException, SchemaValidationException, ...
│       └── util/                      # Periodico, Vigencia, VigenciaUtils, YearMonthIntegerConverter, CallbackAsync
├── declaracoes-gov-core-format/       # WP06, WP07, WP08
│   └── src/main/java/br/uem/npd/govcore/
│       ├── util/                      # GovZipUtils (novo), GovStringUtils (novo/expand), GovNumberUtils (novo)
│       ├── util/                      # GovDateUtils (expand), GovCollectionUtils (novo), GovFileUtils (novo)
│       └── util/                      # VigenciaConverter, VigenciaValidator (sem @Component)
├── declaracoes-gov-core-crypto/       # WP09
│   └── src/main/java/br/uem/npd/govcore/
│       └── crypto/                    # KeyManagerFactoryBuilder (novo), GovSslContextFactory (novo)
├── declaracoes-gov-core-xml/          # WP01 (HOTFIX)
│   └── src/main/java/br/uem/npd/govcore/
│       └── signature/                 # XmlDsigSigner (fix), XmlSignatureOptions (add includeC14nTransform)
└── declaracoes-gov-core-kotlin/       # WP10 (NOVO MÓDULO)
    └── src/main/kotlin/br/uem/npd/govcore/
        └── ext/                       # extensions sobre domain, format, crypto
```

---

## Parallel Work Analysis

### Grafo de Dependências entre WPs

```
WP01 (hotfix xml)   ─────────────────────────────────────────► independente
WP02 (CNPJ alfa)    ─────────────────────────────────────────► independente
WP03 (CPF+Vigencia) ─── depende WP02 (mesmo módulo) ─────────►
WP04 (Exceptions)   ─── depende WP03 ────────────────────────►
WP05 (Domain utils) ─── depende WP04 ────────────────────────►
WP06 (GovZipUtils + String/Number) ──────────────────────────► independente
WP07 (DateUtils + Coleções + VigenciaConverter) ─── depende WP03, WP06 ──►
WP08 (Crypto expand) ── depende WP04 ────────────────────────►
WP09 (Kotlin module) ── depende WP05, WP07, WP08 ────────────►
```

### Distribuição por Wave (execução paralela máxima)

| Wave | WPs em paralelo | Racional |
|------|----------------|---------|
| Wave 1 | WP01 + WP02 + WP06 | Todos independentes; iniciar imediatamente |
| Wave 2 | WP03 (pós WP02) | domain, mesma base |
| Wave 3 | WP04 + WP07 (pós WP03 + WP06) | exceptions + format converters em paralelo |
| Wave 4 | WP05 + WP08 (pós WP04) | domain utils + crypto em paralelo |
| Wave 5 | WP09 (pós WP05 + WP07 + WP08) | kotlin extensions sobre tudo |

### Work Package Summary

| WP | Título | Módulo | Subtarefas | Prioridade |
|----|--------|--------|------------|-----------|
| WP01 | XmlDsigSigner — Hotfix C14N Inclusive | `xml` | 4 | 🔴 CRÍTICA |
| WP02 | CNPJ Alfanumérico RF 2026 | `domain` | 6 | 🔴 ALTA |
| WP03 | CPF + Domínio Período/Vigência | `domain` | 6 | 🟠 ALTA |
| WP04 | Hierarquia de Exceções Expandida | `domain` | 5 | 🟠 ALTA |
| WP05 | Tipos de Domínio e Contratos | `domain` | 5 | 🟡 MÉDIA |
| WP06 | GovZipUtils + GovStringUtils + GovNumberUtils | `format` | 6 | 🟠 ALTA |
| WP07 | GovDateUtils + VigenciaConverter + GovCollections + GovFileUtils | `format` | 7 | 🟡 MÉDIA |
| WP08 | KeyManagerFactoryBuilder + GovSslContextFactory | `crypto` | 4 | 🟡 MÉDIA |
| WP09 | Submódulo Kotlin — Extension Functions | `kotlin` (novo) | 6 | 🟢 BAIXA |

**Total: 9 WPs / 49 subtarefas**

---

## Riscos e Mitigações

| Risco | Impacto | Mitigação |
|-------|---------|-----------|
| Interface `Vigencia` já existe na lib com diferente assinatura | BLOCKER em WP03 | Verificar `declaracoes-gov-core-domain` antes de escrever código; fazer merge ou renomear |
| `XmlSignatureOptions.includeC14nTransform=true` por padrão quebra consumidores atuais | MEDIUM | Avaliar default `false` (opt-in) vs `true` no WP01; documentar breaking change no CHANGELOG |
| `GovStringUtils` conflita com `GovTextNormalizer` existente | LOW | Fazer merge expandindo `GovTextNormalizer` em vez de criar classe paralela |
| Versão Kotlin a definir para o submódulo | LOW | Verificar versão usada em `obrigacoes-service-reinf` e `obrigacoes-gateway-bardo` antes do WP09 |
