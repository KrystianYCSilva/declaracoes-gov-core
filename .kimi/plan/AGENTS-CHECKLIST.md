# Checklist de Implementação: Agentes de IA

## Projetos a Configurar

### Leiautes
- [ ] declaracoes-esocial-leiautes
- [ ] declaracoes-efd-reinf-leiautes
- [ ] declaracoes-mit-leiautes
- [ ] declaracoes-dctfweb-leiautes
- [ ] declaracoes-defis-leiautes
- [ ] declaracoes-pgdas-leiautes
- [ ] declaracoes-pgmei-leiautes
- [ ] declaracoes-perdcomp-leiautes
- [ ] declaracoes-parcelamento-leiautes

### Transmissores
- [ ] declaracoes-esocial-transmissor
- [ ] declaracoes-efd-reinf-transmissor
- [ ] declaracoes-serpro-transmissor

### Core/BOM
- [ ] declaracoes-gov-core
- [ ] declaracoes-gov-bom

---

## Checklist por Projeto

### Fase 1: Estrutura Base

#### Diretórios
```bash
mkdir -p .context/{_meta,standards,patterns,workflows,troubleshooting,examples}
mkdir -p .agents/{prompts,skills}
mkdir -p .claude .codex .cursor .gemini .qwen .opencode .junie .vibe .codebuddy
mkdir -p .github
```

- [ ] Criar `.context/` com subdiretórios
- [ ] Criar `.agents/` com subdiretórios
- [ ] Criar diretórios por agente
- [ ] Criar `.github/` para Copilot

#### Arquivos Obrigatórios (Fase 1)

| Arquivo | Descrição | Prioridade |
|---------|-----------|------------|
| `AGENTS.md` | Entrypoint multi-CLI | ALTA |
| `.gitignore` | Atualizado com secrets | ALTA |
| `.context/README.md` | Hub de navegação | ALTA |
| `.context/ai-assistant-guide.md` | Guia de operação | ALTA |
| `.context/standards/architectural-rules.md` | Regras T0 | ALTA |
| `.kimi/AGENTS.md` | Config Kimi | ALTA |

---

### Fase 2: Metadados do Projeto

#### Arquivos em `.context/_meta/`

- [ ] `project-overview.md` - Visão geral
- [ ] `tech-stack.md` - Tecnologias
- [ ] `key-decisions.md` - Decisões arquiteturais
- [ ] `codebase-map.md` - Mapa do código

#### Conteúdo Mínimo: project-overview.md
```markdown
---
name: project-overview
description: High-level summary of {PROJECT_NAME}
---

# Project Overview

## Name
{PROJECT_NAME}

## Purpose
{Purpose description}

## Version
1.0.0

## Domain
{Domain description}

## Key Features
- Feature 1
- Feature 2
```

#### Conteúdo Mínimo: tech-stack.md
```markdown
---
name: tech-stack
description: Technology stack
---

# Tech Stack

- Java 8+
- Maven
- {Specific libs}
```

#### Conteúdo Mínimo: codebase-map.md
```markdown
---
name: codebase-map
description: Package structure
---

# Codebase Map

## Structure
```
src/
├── main/
│   └── java/
│       └── br/gov/receita/declaracoes/{project}/
│           ├── model/
│           ├── service/
│           └── util/
└── test/
    └── java/
```

## Key Classes
- Main API class
- Configuration class
```

---

### Fase 3: Padrões e Standards

#### Arquivos em `.context/standards/`

- [ ] `architectural-rules.md` - Regras T0 (absolutas)
- [ ] `code-quality.md` - Padrões de código
- [ ] `testing-strategy.md` - Estratégia de testes

#### Arquivos em `.context/patterns/`

- [ ] `architecture-patterns.md` - Padrões arquiteturais
- [ ] `testing-and-tdd.md` - Padrões de teste

---

### Fase 4: Workflows

#### Arquivos em `.context/workflows/`

- [ ] `development-workflow.md` - Desenvolvimento
- [ ] `testing-and-validation-workflow.md` - Testes
- [ ] `context-sync-workflow.md` - Sincronização

---

### Fase 5: Configurações por Agente

#### Arquivos Específicos

| Agente | Arquivo | Conteúdo |
|--------|---------|----------|
| Kimi | `.kimi/AGENTS.md` | Referência root + guardrails |
| Gemini | `.gemini/GEMINI.md` | `@../AGENTS.md` + notas |
| Claude | `.claude/CLAUDE.md` | Referência root + guardrails |
| Codex | `.codex/AGENTS.md` | Referência root + guardrails |
| Qwen | `.qwen/QWEN.md` | Referência root + guardrails |
| Cursor | `.cursor/rules/00-project-bootstrap.mdc` | Regras específicas |
| OpenCode | `.opencode/AGENTS.md` | Referência root + guardrails |
| Junie | `.junie/AGENTS.md` | Referência root + guardrails |
| Vibe | `.vibe/AGENTS.md` | Referência root + guardrails |
| CodeBuddy | `.codebuddy/CODEBUDDY.md` | Referência root + guardrails |
| Copilot | `.github/copilot-instructions.md` | Instruções Copilot |

---

## Validação

### Comandos de Verificação

```bash
# Verificar estrutura
tree -a -L 2 -d .context

# Verificar arquivos existentes
ls -la AGENTS.md .gitignore
ls -la .context/
ls -la .kimi/

# Testar build
mvn -q verify
```

### Critérios de Aceitação

- [ ] `AGENTS.md` existe na raiz
- [ ] `.gitignore` ignora `secret/` e `.env`
- [ ] `.context/README.md` existe
- [ ] `.context/standards/architectural-rules.md` existe
- [ ] `.kimi/AGENTS.md` existe
- [ ] Build passa (`mvn verify`)

---

## Dicas de Implementação

### Ordem de Prioridade

1. **ALTA** (MVP): Arquivos necessários para o agente funcionar
   - `AGENTS.md`
   - `.gitignore`
   - `.context/README.md`
   - `.context/standards/architectural-rules.md`
   - `.kimi/AGENTS.md`

2. **MÉDIA** (Contexto): Metadados e padrões
   - `.context/_meta/*`
   - `.context/patterns/*`
   - `.context/workflows/*`

3. **BAIXA** (Opcional): Outros agentes e extras
   - Outros diretórios `.agent/`
   - `.context/examples/`
   - `.context/troubleshooting/`

### Sincronização com Código

Sempre que houver mudanças em:
- Estrutura de pacotes
- Endpoints ou APIs públicas
- Decisões arquiteturais
- Estratégia de testes

Atualizar:
1. `docs/` (português, humanos)
2. `.context/` (inglês, IA)

---

## Comandos Úteis

### Criar estrutura completa

```bash
#!/bin/bash
# setup-agents.sh

PROJECT_DIR=$1

cd "$PROJECT_DIR" || exit 1

# Criar diretórios
mkdir -p .context/{_meta,standards,patterns,workflows,troubleshooting,examples}
mkdir -p .agents/{prompts,skills}
mkdir -p .claude .codex .gemini .qwen .opencode .junie .vibe .codebuddy .github

# Copiar templates (ajustar caminho)
# cp $TEMPLATES_DIR/AGENTS.md .
# cp $TEMPLATES_DIR/gitignore .gitignore
# ...

echo "Estrutura criada em $PROJECT_DIR"
```

### Verificar status

```bash
#!/bin/bash
# check-agents.sh

PROJECT_DIR=$1

echo "=== Verificando $PROJECT_DIR ==="

cd "$PROJECT_DIR" || exit 1

REQUIRED_FILES=(
  "AGENTS.md"
  ".context/README.md"
  ".context/ai-assistant-guide.md"
  ".context/standards/architectural-rules.md"
  ".kimi/AGENTS.md"
)

for file in "${REQUIRED_FILES[@]}"; do
  if [ -f "$file" ]; then
    echo "✓ $file"
  else
    echo "✗ $file (FALTANDO)"
  fi
done
```

---

*Checklist criado em: Abril/2026*
