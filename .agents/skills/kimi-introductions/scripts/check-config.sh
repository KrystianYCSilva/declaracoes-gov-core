#!/bin/bash
#
# Script para verificar configuração do Kimi CLI

set -e

echo "🔍 Verificando configuração do Kimi CLI..."
echo ""

# Verificar instalação
if command -v kimi &> /dev/null; then
    echo "✅ Kimi CLI instalado"
    echo "   Versão: $(kimi --version)"
else
    echo "❌ Kimi CLI não encontrado"
    echo "   Instale com: curl -LsSf https://code.kimi.com/install.sh | bash"
    exit 1
fi

echo ""

# Verificar Python
if command -v python3 &> /dev/null; then
    PYTHON_VERSION=$(python3 --version | cut -d' ' -f2)
    echo "✅ Python encontrado: $PYTHON_VERSION"
else
    echo "⚠️  Python não encontrado"
fi

echo ""

# Verificar configuração
CONFIG_FILE="$HOME/.kimi/config.toml"
if [ -f "$CONFIG_FILE" ]; then
    echo "✅ Arquivo de configuração encontrado"
    echo "   Local: $CONFIG_FILE"
    
    # Verificar provider configurado
    if grep -q "api_key" "$CONFIG_FILE"; then
        echo "✅ API key configurada"
    else
        echo "⚠️  API key não configurada"
        echo "   Execute: /login ou kimi login"
    fi
else
    echo "⚠️  Arquivo de configuração não encontrado"
    echo "   Execute: kimi login"
fi

echo ""

# Verificar diretórios de skills
echo "📁 Diretórios de skills:"
SKILL_DIRS=(
    "$HOME/.config/agents/skills"
    "$HOME/.kimi/skills"
    "$HOME/.claude/skills"
    ".kimi/skills"
    ".claude/skills"
    ".agents/skills"
)

for dir in "${SKILL_DIRS[@]}"; do
    if [ -d "$dir" ]; then
        COUNT=$(find "$dir" -name "SKILL.md" | wc -l)
        echo "   ✅ $dir ($COUNT skills)"
    fi
done

echo ""

# Verificar MCP
MCP_FILE="$HOME/.kimi/mcp.json"
if [ -f "$MCP_FILE" ]; then
    echo "✅ Configuração MCP encontrada"
    echo "   Local: $MCP_FILE"
else
    echo "ℹ️  Configuração MCP não encontrada (opcional)"
fi

echo ""

# Verificar sessões
SESSIONS_DIR="$HOME/.kimi/sessions"
if [ -d "$SESSIONS_DIR" ]; then
    SESSION_COUNT=$(find "$SESSIONS_DIR" -maxdepth 1 -type d | wc -l)
    SESSION_COUNT=$((SESSION_COUNT - 1))
    echo "📂 Sessões armazenadas: $SESSION_COUNT"
fi

echo ""
echo "✨ Verificação completa!"
