#!/bin/bash
#
# Script para inicializar um projeto com Kimi CLI
# Cria AGENTS.md e estrutura básica

set -e

echo "🚀 Inicializando projeto para Kimi CLI..."

# Detectar tipo de projeto
PROJECT_TYPE="generic"

if [ -f "package.json" ]; then
    PROJECT_TYPE="node"
elif [ -f "pyproject.toml" ] || [ -f "setup.py" ] || [ -f "requirements.txt" ]; then
    PROJECT_TYPE="python"
elif [ -f "Cargo.toml" ]; then
    PROJECT_TYPE="rust"
elif [ -f "go.mod" ]; then
    PROJECT_TYPE="go"
elif [ -f "pom.xml" ] || [ -f "build.gradle" ]; then
    PROJECT_TYPE="java"
fi

echo "📋 Tipo de projeto detectado: $PROJECT_TYPE"

# Criar diretório .kimi se não existir
if [ ! -d ".kimi" ]; then
    mkdir -p .kimi
    echo "✅ Diretório .kimi criado"
fi

# Criar AGENTS.md base
if [ ! -f "AGENTS.md" ]; then
    cat > "AGENTS.md" << 'EOF'
# AGENTS.md

## Project Overview

**Project Name**: [Nome do Projeto]
**Type': [Tipo de Projeto]
**Description**: [Breve descrição]

## Technology Stack

- Language: [Linguagem principal]
- Framework: [Framework principal]
- Package Manager: [Gerenciador de pacotes]

## Project Structure

```
.
├── src/              # Source code
├── tests/            # Test files
├── docs/             # Documentation
└── [outros dirs]     # Outros diretórios importantes
```

## Build and Run

```bash
# Install dependencies
[comando]

# Build
[comando]

# Run tests
[comando]

# Start development
[comando]
```

## Code Style

- [Convenção de código]
- [Padrões de nomenclatura]
- [Outras convenções]

## Notes

[Informações adicionais importantes]
EOF
    echo "✅ AGENTS.md criado"
else
    echo "ℹ️  AGENTS.md já existe"
fi

# Criar .gitignore se não existir
if [ ! -f ".gitignore" ]; then
    cat > ".gitignore" << 'EOF'
# Dependencies
node_modules/
__pycache__/
.venv/
venv/
target/
build/
dist/

# IDE
.idea/
.vscode/
*.swp
*.swo

# OS
.DS_Store
Thumbs.db

# Logs
*.log
logs/

# Environment
.env
.env.local
EOF
    echo "✅ .gitignore criado"
fi

# Sugestões específicas por tipo
case $PROJECT_TYPE in
    node)
        echo ""
        echo "💡 Sugestões para projetos Node.js:"
        echo "  - Execute: kimi --prompt 'Analyze package.json and suggest improvements'"
        echo "  - Execute: kimi --prompt 'Set up ESLint and Prettier configuration'"
        ;;
    python)
        echo ""
        echo "💡 Sugestões para projetos Python:"
        echo "  - Execute: kimi --prompt 'Analyze pyproject.toml and suggest improvements'"
        echo "  - Execute: kimi --prompt 'Set up ruff and black configuration'"
        ;;
    rust)
        echo ""
        echo "💡 Sugestões para projetos Rust:"
        echo "  - Execute: kimi --prompt 'Analyze Cargo.toml and suggest improvements'"
        ;;
    *)
        echo ""
        echo "💡 Próximos passos:"
        echo "  1. Edite AGENTS.md com informações do projeto"
        echo "  2. Execute: kimi --prompt 'Analyze this project structure'"
        ;;
esac

echo ""
echo "🎉 Projeto inicializado!"
echo ""
echo "Para começar:"
echo "  1. Edite AGENTS.md com detalhes do projeto"
echo "  2. Execute: kimi"
echo "  3. Ou: kimi --prompt 'Your first task'"
