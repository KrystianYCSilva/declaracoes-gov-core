# Tasks — 001-core-evolution-roadmap

## Status

| WP | Title | Lane | Dependencies |
|----|-------|------|-------------|
| WP01 | XmlDsigSigner — Hotfix C14N Inclusive | planned | — |
| WP02 | CNPJ Alfanumérico RF 2026 | planned | — |
| WP03 | CPF + Domínio Período/Vigência | planned | WP02 |
| WP04 | Hierarquia de Exceções Expandida | planned | WP03 |
| WP05 | Tipos de Domínio e Contratos | planned | WP04 |
| WP06 | GovZipUtils + GovStringUtils + GovNumberUtils | planned | — |
| WP07 | GovDateUtils + VigenciaConverter + GovCollections + GovFileUtils | planned | WP03, WP06 |
| WP08 | KeyManagerFactoryBuilder + GovSslContextFactory | planned | WP04 |
| WP09 | Submódulo Kotlin — Extension Functions | planned | WP05, WP07, WP08 |

## Wave Execution Plan

Wave 1 (parallel): WP01 + WP02 + WP06
Wave 2: WP03 (after WP02)
Wave 3 (parallel): WP04 + WP07 (after WP03 + WP06)
Wave 4 (parallel): WP05 + WP08 (after WP04)
Wave 5: WP09 (after WP05 + WP07 + WP08)

## Work Package Details

### WP01 — XmlDsigSigner — Hotfix C14N Inclusive
- **Módulo:** `xml`
- **Prioridade:** CRÍTICA
- **Subtarefas:** 4
- **Problema:** Ausência de C14N INCLUSIVE causa Erro 142 no portal eSocial/ReInF
- **Arquivo de trabalho:** `tasks/WP01-xml-dsig-signer-hotfix-c14n.md`

### WP02 — CNPJ Alfanumérico RF 2026
- **Módulo:** `domain`
- **Prioridade:** ALTA
- **Subtarefas:** 6
- **Problema:** Lógica de CNPJ alfanumérico duplicada em 2 projetos (gateway-bardo, esocial)
- **Arquivo de trabalho:** `tasks/WP02-cnpj-alfanumerico-rf-2026.md`

### WP03 — CPF + Domínio Período/Vigência
- **Módulo:** `domain`
- **Prioridade:** ALTA
- **Subtarefas:** 6
- **Problema:** Validação de dígito verificador CPF e interfaces Periodico/Vigencia duplicadas
- **Arquivo de trabalho:** `tasks/WP03-cpf-dominio-periodo-vigencia.md`

### WP04 — Hierarquia de Exceções Expandida
- **Módulo:** `domain`
- **Prioridade:** ALTA
- **Subtarefas:** 5
- **Problema:** Exceções de negócio criadas localmente em cada projeto sem base comum
- **Arquivo de trabalho:** `tasks/WP04-hierarquia-excecoes-expandida.md`

### WP05 — Tipos de Domínio e Contratos
- **Módulo:** `domain`
- **Prioridade:** MÉDIA
- **Subtarefas:** 5
- **Problema:** CallbackAsync, CertificadoDTO, Filter, enums duplicados em ecd/ecf/back-core
- **Arquivo de trabalho:** `tasks/WP05-tipos-dominio-contratos.md`

### WP06 — GovZipUtils + GovStringUtils + GovNumberUtils
- **Módulo:** `format`
- **Prioridade:** ALTA
- **Subtarefas:** 6
- **Problema:** 4 cópias idênticas de GovZipUtils; ~15 métodos de string e 11 de BigDecimal duplicados
- **Arquivo de trabalho:** `tasks/WP06-gov-zip-string-number-utils.md`

### WP07 — GovDateUtils + VigenciaConverter + GovCollections + GovFileUtils
- **Módulo:** `format`
- **Prioridade:** MÉDIA
- **Subtarefas:** 7
- **Problema:** DateComponent acoplado a Spring; VigenciaConverter com @Component; utilitários de coleção/arquivo duplicados
- **Arquivo de trabalho:** `tasks/WP07-gov-date-vigencia-collections-file-utils.md`

### WP08 — KeyManagerFactoryBuilder + GovSslContextFactory
- **Módulo:** `crypto`
- **Prioridade:** MÉDIA
- **Subtarefas:** 4
- **Problema:** Criação de KeyManagerFactory e SSLContext multi-tenant duplicada em reinf e esocial-tombamento
- **Arquivo de trabalho:** `tasks/WP08-key-manager-ssl-context-factory.md`

### WP09 — Submódulo Kotlin — Extension Functions
- **Módulo:** `kotlin` (novo módulo)
- **Prioridade:** BAIXA
- **Subtarefas:** 6
- **Problema:** Extensions Kotlin sobre tipos Java criadas de forma inconsistente em reinf/bardo/dctfweb
- **Arquivo de trabalho:** `tasks/WP09-kotlin-extension-functions.md`

## Totals

- **Work Packages:** 9
- **Total Subtasks:** 49
- **Modules affected:** `xml` (1 WP), `domain` (4 WPs), `format` (2 WPs), `crypto` (1 WP), `kotlin` new module (1 WP)
- **Critical path:** WP02 → WP03 → WP04 → WP05 → WP09
