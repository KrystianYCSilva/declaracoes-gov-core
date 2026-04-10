#requires -Version 5.1
<#
.SYNOPSIS
    Script para inicializar um projeto com Kimi CLI
.DESCRIPTION
    Cria AGENTS.md e estrutura básica para uso com Kimi CLI
.EXAMPLE
    .\init-project.ps1
#>
[CmdletBinding()]
param()

Write-Host "🚀 Inicializando projeto para Kimi CLI..."

# Detectar tipo de projeto
$ProjectType = "generic"

if (Test-Path "package.json") {
    $ProjectType = "node"
} elseif (Test-Path "pyproject.toml" -or Test-Path "setup.py" -or Test-Path "requirements.txt") {
    $ProjectType = "python"
} elseif (Test-Path "Cargo.toml") {
    $ProjectType = "rust"
} elseif (Test-Path "go.mod") {
    $ProjectType = "go"
} elseif (Test-Path "pom.xml" -or Test-Path "build.gradle") {
    $ProjectType = "java"
}

Write-Host "📋 Tipo de projeto detectado: $ProjectType"

# Criar diretório .kimi se não existir
if (-not (Test-Path ".kimi")) {
    $null = New-Item -ItemType Directory -Force -Path ".kimi"
    Write-Host "✅ Diretório .kimi criado"
}

# Criar AGENTS.md base
if (-not (Test-Path "AGENTS.md")) {
    $AgentsContent = @"
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
"@
    Set-Content -Path "AGENTS.md" -Value $AgentsContent -Encoding UTF8
    Write-Host "✅ AGENTS.md criado"
} else {
    Write-Host "ℹ️  AGENTS.md já existe"
}

# Criar .gitignore se não existir
if (-not (Test-Path ".gitignore")) {
    $GitignoreContent = @"
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
"@
    Set-Content -Path ".gitignore" -Value $GitignoreContent -Encoding UTF8
    Write-Host "✅ .gitignore criado"
}

# Sugestões específicas por tipo
switch ($ProjectType) {
    "node" {
        Write-Host ""
        Write-Host "💡 Sugestões para projetos Node.js:"
        Write-Host "  - Execute: kimi --prompt 'Analyze package.json and suggest improvements'"
        Write-Host "  - Execute: kimi --prompt 'Set up ESLint and Prettier configuration'"
    }
    "python" {
        Write-Host ""
        Write-Host "💡 Sugestões para projetos Python:"
        Write-Host "  - Execute: kimi --prompt 'Analyze pyproject.toml and suggest improvements'"
        Write-Host "  - Execute: kimi --prompt 'Set up ruff and black configuration'"
    }
    "rust" {
        Write-Host ""
        Write-Host "💡 Sugestões para projetos Rust:"
        Write-Host "  - Execute: kimi --prompt 'Analyze Cargo.toml and suggest improvements'"
    }
    default {
        Write-Host ""
        Write-Host "💡 Próximos passos:"
        Write-Host "  1. Edite AGENTS.md com informações do projeto"
        Write-Host "  2. Execute: kimi --prompt 'Analyze this project structure'"
    }
}

Write-Host ""
Write-Host "🎉 Projeto inicializado!" -ForegroundColor Green
Write-Host ""
Write-Host "Para começar:"
Write-Host "  1. Edite AGENTS.md com detalhes do projeto"
Write-Host "  2. Execute: kimi"
Write-Host "  3. Ou: kimi --prompt 'Your first task'"
