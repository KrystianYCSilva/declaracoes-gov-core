#!/bin/bash
#
# Script para criar um novo agente customizado no Kimi CLI
# Uso: ./create-agent.sh <nome-do-agente>

set -e

AGENT_NAME="$1"

if [ -z "$AGENT_NAME" ]; then
    echo "❌ Erro: Nome do agente não fornecido"
    echo "Uso: $0 <nome-do-agente>"
    exit 1
fi

# Normalizar nome
AGENT_NAME=$(echo "$AGENT_NAME" | tr '[:upper:]' '[:lower:]' | tr ' ' '-')

# Detectar diretório de agentes
if [ -d ".kimi/agents" ]; then
    AGENTS_DIR=".kimi/agents"
elif [ -d ".claude/agents" ]; then
    AGENTS_DIR=".claude/agents"
else
    AGENTS_DIR=".kimi/agents"
    mkdir -p "$AGENTS_DIR"
fi

AGENT_FILE="$AGENTS_DIR/$AGENT_NAME.yaml"
PROMPT_FILE="$AGENTS_DIR/$AGENT_NAME-prompt.md"

if [ -f "$AGENT_FILE" ]; then
    echo "❌ Erro: Agente '$AGENT_NAME' já existe"
    exit 1
fi

# Criar arquivo do agente
cat > "$AGENT_FILE" << EOF
version: 1
agent:
  name: $AGENT_NAME
  extend: default
  system_prompt_path: ./$AGENT_NAME-prompt.md
  # system_prompt_args:
  #   CUSTOM_VAR: "valor"
  # exclude_tools:
  #   - "kimi_cli.tools.web:SearchWeb"
  # subagents:
  #   coder:
  #     path: ./${AGENT_NAME}-coder.yaml
  #     description: "Specialized coder subagent"
EOF

# Criar arquivo de prompt
cat > "$PROMPT_FILE" << EOF
# $AGENT_NAME Agent

You are the $AGENT_NAME agent, specialized in...

## Role

Describe the specific role and expertise of this agent.

## Guidelines

- Guideline 1
- Guideline 2
- Guideline 3

## Context

Current time: \${KIMI_NOW}
Working directory: \${KIMI_WORK_DIR}
\${KIMI_AGENTS_MD}

## Custom Variables

\${CUSTOM_VAR}
EOF

echo ""
echo "✅ Agente '$AGENT_NAME' criado com sucesso!"
echo ""
echo "📂 Arquivos criados:"
echo "  - $AGENT_FILE"
echo "  - $PROMPT_FILE"
echo ""
echo "Próximos passos:"
echo "  1. Edite $AGENT_FILE para configurar ferramentas e subagentes"
echo "  2. Edite $PROMPT_FILE para definir o comportamento"
echo "  3. Use com: kimi --agent-file $AGENT_FILE"
