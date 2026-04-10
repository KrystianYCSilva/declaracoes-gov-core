# Plano de Implementação: Configuração de Agentes de IA

## Visão Geral

Este plano define a implementação de uma estrutura multi-agente de IA nos projetos `declaracoes-*`, seguindo o padrão estabelecido nos projetos `npd-mec-service` e `npd-service-ces`.

## Estrutura de Diretórios a Criar

Para cada projeto `declaracoes-*`, criar a seguinte estrutura:

```
{projeto}/
├── AGENTS.md                          # Entrypoint compartilhado multi-CLI
├── .gitignore                         # Atualizado com regras para secrets
├── .agents/                           # Configurações compartilhadas de agentes
│   ├── prompts/
│   ├── skills/
│   └── sdd-flow-orchestrator.agents.md
├── .context/                          # Contexto para IA (em inglês)
│   ├── README.md                      # Hub de navegação
│   ├── ai-assistant-guide.md          # Guia de operação
│   ├── _meta/                         # Metadados do projeto
│   │   ├── project-overview.md
│   │   ├── tech-stack.md
│   │   ├── key-decisions.md
│   │   └── codebase-map.md
│   ├── standards/                     # Regras T0 (absolutas)
│   │   ├── architectural-rules.md
│   │   ├── code-quality.md
│   │   └── testing-strategy.md
│   ├── patterns/                      # Padrões arquiteturais
│   │   ├── architecture-patterns.md
│   │   └── testing-and-tdd.md
│   ├── workflows/                     # Playbooks de execução
│   │   ├── development-workflow.md
│   │   ├── testing-and-validation-workflow.md
│   │   └── context-sync-workflow.md
│   └── troubleshooting/
│       └── common-issues.md
├── .specify/                          # Spec Kit (se usar Spec Kit)
│   ├── init-options.json
│   ├── templates/
│   │   ├── spec-template.md
│   │   ├── plan-template.md
│   │   └── tasks-template.md
│   └── integrations/
└── .{agent}/                          # Um diretório por agente
    ├── AGENTS.md                      # ou {AGENT}.md
    └── agents/                        # Config específica
        └── speckit-orchestrator.yaml
```

## Agentes Suportados

Criar diretórios para cada agente:
- `.claude/` - Claude Code
- `.codex/` - Codex CLI
- `.cursor/` - Cursor IDE
- `.gemini/` - Gemini CLI
- `.qwen/` - Qwen Code
- `.kimi/` - Kimi CLI (já existe parcialmente)
- `.opencode/` - OpenCode
- `.junie/` - JetBrains Junie
- `.vibe/` - Mistral Vibe
- `.codebuddy/` - CodeBuddy
- `.github/copilot-instructions.md` - GitHub Copilot

## Arquivos Principais

### 1. AGENTS.md (Raiz do Projeto)

Template baseado nos projetos NPD:

```markdown
# AGENTS

This file is the shared multi-CLI entrypoint for the repository.
CLI-specific primary instruction files live in the native tool directories:

- Codex: `.codex/AGENTS.md`
- Claude: `.claude/CLAUDE.md`
- Cursor: `.cursor/rules/00-project-bootstrap.mdc`
- Gemini: `.gemini/GEMINI.md`
- Qwen: `.qwen/QWEN.md`
- Kimi: `.kimi/AGENTS.md`
- OpenCode: `.opencode/AGENTS.md`
- Junie: `.junie/AGENTS.md`
- Vibe: `.vibe/AGENTS.md`
- CodeBuddy: `.codebuddy/CODEBUDDY.md`
- Copilot: `.github/copilot-instructions.md`

All LLM-facing files in this repository must stay in English:
- main instruction files
- skills
- commands
- agents and subagents
- prompt assets

## AI Context Bootstrap

Before generating code, tests, or AI-facing documentation, load the AI context in this order:
1. `.context/README.md`
2. `.context/ai-assistant-guide.md`
3. `.context/standards/architectural-rules.md`

Then load only the task-specific files needed for the current request.

## AI Context Tier System

| Tier | Purpose | Authority | Files |
|------|---------|-----------|-------|
| `T0` | Enforcement | Absolute | `.context/standards/architectural-rules.md` |
| `T1` | Standards and patterns | Normative | `.context/standards/code-quality.md`, `.context/standards/testing-strategy.md`, `.context/patterns/` |
| `T2` | Project context | Informative | `.context/_meta/` |
| `T3` | Examples | Illustrative | `.context/examples/` |

Conflict resolution:
- If `T0` conflicts with any other tier, `T0` wins.
- If `T1` conflicts with `T2` or `T3`, `T1` wins.
- If `T2` conflicts with `T3`, `T2` wins.
- If `.context/`, `docs/`, and `src/` disagree, confirm the current truth in `src/`, then update `docs/`, then update `.context/`.

## AI Context Index

Bootstrap and navigation:
- `.context/README.md`
- `.context/ai-assistant-guide.md`

Project metadata:
- `.context/_meta/project-overview.md`
- `.context/_meta/tech-stack.md`
- `.context/_meta/key-decisions.md`
- `.context/_meta/codebase-map.md`

Standards:
- `.context/standards/architectural-rules.md`
- `.context/standards/code-quality.md`
- `.context/standards/testing-strategy.md`

Patterns:
- `.context/patterns/architecture-patterns.md`
- `.context/patterns/testing-and-tdd.md`

Workflows:
- `.context/workflows/development-workflow.md`
- `.context/workflows/testing-and-validation-workflow.md`
- `.context/workflows/context-sync-workflow.md`

## AI Context Routing

- For endpoint or behavior changes: load `.context/_meta/codebase-map.md`, `.context/patterns/architecture-patterns.md`, and `.context/workflows/development-workflow.md`.
- For security changes: load `.context/_meta/key-decisions.md`, `.context/_meta/tech-stack.md`, and `.context/standards/testing-strategy.md`.
- For testing work: load `.context/standards/testing-strategy.md`, `.context/patterns/testing-and-tdd.md`, and `.context/workflows/testing-and-validation-workflow.md`.
- For review or QA: load `.context/workflows/review-qa-and-release-workflow.md` and `.context/troubleshooting/common-issues.md`.

## Context Synchronization Rule

- `src/` is the live implementation.
- `docs/` is the human-facing record and must remain in Portuguese.
- `.context/` is the AI-facing compressed record and must remain in English.
- Any change to behavior, architecture, tests, profiles, or operational setup must keep `src/`, `docs/`, and `.context/` synchronized.

## Project

- Name: `{declaracoes-xxx}`
- Baseline release: `1.0.0`
- Type: `{Java Library | REST API | etc}`
- Domain: `{Descrição do domínio}`

## Stack

- Java 8+
- Maven
- {Outras tecnologias}

## Architecture Rules

- {Regras específicas do projeto}

## Quality Gate

- `mvn verify` must stay green.
- Minimum coverage: `80%` line, `75%` branch.
```

### 2. .gitignore

Adicionar/atualizar com:

```gitignore
### Maven ###
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### IDE ###
.idea/
*.iws
*.iml
*.ipr
.vscode/
.classpath
.project
.settings/

### OS ###
.DS_Store
Thumbs.db

### Logs ###
*.log
logs/

### Local Secrets ###
secret/
.env
.env.*
*.pem
*.p12
*.pfx
keystore/

### Temp Files ###
.tmp/
*.tmp
```

### 3. .context/README.md

```markdown
---
name: context-hub
description: |
  Navigation hub for the AI-facing context of {projeto}.
  Use when: loading the minimum authoritative context before changing code, tests, or documentation.
---

# .context Hub

## Quick Start

1. Load `standards/architectural-rules.md` first.
2. Load `_meta/project-overview.md` and `_meta/codebase-map.md`.
3. Load only the task-specific files listed below.

## Authority Model

| Tier | Kind | Authority | Typical Files |
| --- | --- | --- | --- |
| T0 | Enforcement | Absolute | `standards/architectural-rules.md` |
| T1 | Standards and patterns | Normative | `standards/`, `patterns/` |
| T2 | Project context | Informative | `_meta/` |
| T3 | Examples | Illustrative | `examples/` |

## Source Of Truth

- `src/` is authoritative for implementation details and current runtime behavior.
- `docs/` is the human-facing record and remains in Portuguese.
- `.context/` is the compressed English AI-facing layer and must mirror the current code and human docs.

## Task Routing

| Task | Load Next |
| --- | --- |
| Change code behavior | `_meta/codebase-map.md`, `patterns/architecture-patterns.md`, `workflows/development-workflow.md` |
| Change tests | `standards/testing-strategy.md`, `patterns/testing-and-tdd.md`, `workflows/testing-and-validation-workflow.md` |
| Review or QA | `_meta/codebase-map.md`, `workflows/review-qa-and-release-workflow.md` |

## Directory Map

- `_meta/`: project summary, technology, decisions, and package map.
- `standards/`: absolute and normative rules.
- `patterns/`: architectural and design patterns.
- `workflows/`: execution playbooks.
```

### 4. .context/standards/architectural-rules.md

```markdown
---
name: architectural-rules
description: |
  Tier T0 rules that define the non-negotiable architecture and security boundaries.
  Use when: proposing, reviewing, or implementing any change in this repository.
---

# Architectural Rules

> Tier: T0 - ABSOLUTE. Every change must comply with these rules.

## `AR-001` Keep Library Agnostic

Rule:
- This is a library, not an application.
- Do not introduce framework-specific dependencies (Spring, Jakarta EE, etc.) in core modules.
- Keep the public API framework-agnostic.

## `AR-002` Thread Safety

Rule:
- All public classes must be thread-safe.
- Prefer immutable objects.
- Document thread-safety guarantees in Javadoc.

## `AR-003` Java 8 Compatibility

Rule:
- Source and target compatibility must remain at Java 8.
- Do not use Java 9+ features (var, new Optional methods, etc.).

## `AR-004` Dependency Minimalism

Rule:
- Keep external dependencies to a minimum.
- Mark optional dependencies as `<optional>true</optional>`.
- Provide SPI for extensibility rather than direct integration.

## `AR-005` No Secrets in Git

Rule:
- Never commit certificates, keys, or credentials.
- Never embed test credentials in source code.
```

## Roadmap de Implementação

### Fase 1: Estrutura Base (Prioridade: ALTA)

Para cada projeto `declaracoes-*`:

1. **Criar diretórios**:
   ```bash
   mkdir -p .context/{_meta,standards,patterns,workflows,troubleshooting}
   mkdir -p .agents/{prompts,skills}
   mkdir -p .{claude,codex,cursor,gemini,qwen,opencode,junie,vibe,codebuddy}
   ```

2. **Criar arquivos base**:
   - `AGENTS.md` (raiz)
   - `.gitignore` (atualizar)
   - `.context/README.md`
   - `.context/ai-assistant-guide.md`

### Fase 2: Contexto do Projeto (Prioridade: ALTA)

1. **`.context/_meta/`**:
   - `project-overview.md` - Visão geral do projeto
   - `tech-stack.md` - Tecnologias utilizadas
   - `key-decisions.md` - Decisões arquiteturais
   - `codebase-map.md` - Mapa do código

2. **`.context/standards/`**:
   - `architectural-rules.md` - Regras T0
   - `code-quality.md` - Padrões de código
   - `testing-strategy.md` - Estratégia de testes

### Fase 3: Padrões e Workflows (Prioridade: MÉDIA)

1. **`.context/patterns/`**:
   - `architecture-patterns.md` - Padrões arquiteturais
   - `testing-and-tdd.md` - Padrões de teste

2. **`.context/workflows/`**:
   - `development-workflow.md` - Fluxo de desenvolvimento
   - `testing-and-validation-workflow.md` - Validação
   - `context-sync-workflow.md` - Sincronização

### Fase 4: Configurações por Agente (Prioridade: MÉDIA)

1. **Arquivos específicos por agente**:
   - `.kimi/AGENTS.md`
   - `.gemini/GEMINI.md`
   - `.claude/CLAUDE.md`
   - `.qwen/QWEN.md`
   - etc.

2. **Estrutura padrão por agente**:
   ```markdown
   # {Agent} Instructions

   Use the root `AGENTS.md` as the shared repository index.
   This file stores the repository-specific {Agent} guidance.

   ## Baseline

   - Project: `{nome}`
   - Java 8+ Maven library
   - {Outras características}

   ## Guardrails

   - Keep secrets out of Git.
   - Keep public APIs stable.
   - Maintain Java 8 compatibility.
   - Keep `mvn verify` green.

   ## Useful Commands

   - Compile: `mvn -q -DskipTests compile`
   - Test: `mvn -q test`
   - Validate: `mvn -q verify`
   ```

### Fase 5: Spec Kit (Opcional)

Se usar Spec Kit para especificação:

1. **Criar `.specify/`**:
   - `init-options.json`
   - `templates/{spec,plan,tasks}-template.md`
   - `integrations/`

## Checklist por Projeto

```markdown
- [ ] AGENTS.md (raiz)
- [ ] .gitignore atualizado
- [ ] .context/
  - [ ] README.md
  - [ ] ai-assistant-guide.md
  - [ ] _meta/
    - [ ] project-overview.md
    - [ ] tech-stack.md
    - [ ] key-decisions.md
    - [ ] codebase-map.md
  - [ ] standards/
    - [ ] architectural-rules.md
    - [ ] code-quality.md
    - [ ] testing-strategy.md
  - [ ] patterns/
    - [ ] architecture-patterns.md
    - [ ] testing-and-tdd.md
  - [ ] workflows/
    - [ ] development-workflow.md
    - [ ] testing-and-validation-workflow.md
    - [ ] context-sync-workflow.md
- [ ] .agents/
  - [ ] prompts/
  - [ ] skills/
- [ ] .{agent}/ (um por agente)
  - [ ] AGENTS.md ou {AGENT}.md
```

## Projetos a Configurar

1. declaracoes-esocial-leiautes
2. declaracoes-esocial-transmissor
3. declaracoes-efd-reinf-leiautes
4. declaracoes-efd-reinf-transmissor
5. declaracoes-mit-leiautes
6. declaracoes-dctfweb-leiautes
7. declaracoes-defis-leiautes
8. declaracoes-pgdas-leiautes
9. declaracoes-pgmei-leiautes
10. declaracoes-perdcomp-leiautes
11. declaracoes-parcelamento-leiautes
12. declaracoes-gov-core
13. declaracoes-serpro-transmissor
14. declaracoes-gov-bom

## Notas Importantes

1. **Idioma**: Todos os arquivos em `.context/` devem estar em inglês.
2. **Sincronização**: Manter `src/`, `docs/` e `.context/` sincronizados.
3. **Tier System**: Respeitar hierarquia T0 > T1 > T2 > T3.
4. **Manutenção**: Atualizar `.context/` sempre que houver mudanças em comportamento, arquitetura ou endpoints.

---

*Plano criado em: Abril/2026*
*Baseado em: npd-mec-service e npd-service-ces*
