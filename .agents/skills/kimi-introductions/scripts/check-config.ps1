#requires -Version 5.1
<#
.SYNOPSIS
    Script para verificar configuração do Kimi CLI
.DESCRIPTION
    Verifica instalação, configuração e diretórios do Kimi CLI
.EXAMPLE
    .\check-config.ps1
#>
[CmdletBinding()]
param()

Write-Host "🔍 Verificando configuração do Kimi CLI..."
Write-Host ""

# Verificar instalação
$KimiPath = Get-Command kimi -ErrorAction SilentlyContinue
if ($KimiPath) {
    Write-Host "✅ Kimi CLI instalado"
    $Version = kimi --version 2>$null
    Write-Host "   Versão: $Version"
} else {
    Write-Host "❌ Kimi CLI não encontrado"
    Write-Host "   Instale com: Invoke-RestMethod https://code.kimi.com/install.ps1 | Invoke-Expression"
    exit 1
}

Write-Host ""

# Verificar Python
$PythonPath = Get-Command python -ErrorAction SilentlyContinue
if (-not $PythonPath) {
    $PythonPath = Get-Command python3 -ErrorAction SilentlyContinue
}
if ($PythonPath) {
    $PythonVersion = & $PythonPath.Source --version 2>&1
    Write-Host "✅ Python encontrado: $PythonVersion"
} else {
    Write-Host "⚠️  Python não encontrado"
}

Write-Host ""

# Verificar configuração
$ConfigFile = "$env:USERPROFILE\.kimi\config.toml"
if (Test-Path $ConfigFile) {
    Write-Host "✅ Arquivo de configuração encontrado"
    Write-Host "   Local: $ConfigFile"
    
    $Content = Get-Content $ConfigFile -Raw
    if ($Content -match "api_key") {
        Write-Host "✅ API key configurada"
    } else {
        Write-Host "⚠️  API key não configurada"
        Write-Host "   Execute: /login ou kimi login"
    }
} else {
    Write-Host "⚠️  Arquivo de configuração não encontrado"
    Write-Host "   Execute: kimi login"
}

Write-Host ""

# Verificar diretórios de skills
Write-Host "📁 Diretórios de skills:"
$SkillDirs = @(
    "$env:USERPROFILE\.config\agents\skills"
    "$env:USERPROFILE\.kimi\skills"
    "$env:USERPROFILE\.claude\skills"
    ".kimi\skills"
    ".claude\skills"
    ".agents\skills"
)

foreach ($dir in $SkillDirs) {
    if (Test-Path $dir) {
        $Count = (Get-ChildItem -Path $dir -Recurse -Filter "SKILL.md" -ErrorAction SilentlyContinue).Count
        Write-Host "   ✅ $dir ($Count skills)"
    }
}

Write-Host ""

# Verificar MCP
$McpFile = "$env:USERPROFILE\.kimi\mcp.json"
if (Test-Path $McpFile) {
    Write-Host "✅ Configuração MCP encontrada"
    Write-Host "   Local: $McpFile"
} else {
    Write-Host "ℹ️  Configuração MCP não encontrada (opcional)"
}

Write-Host ""

# Verificar sessões
$SessionsDir = "$env:USERPROFILE\.kimi\sessions"
if (Test-Path $SessionsDir) {
    $SessionCount = (Get-ChildItem -Path $SessionsDir -Directory -ErrorAction SilentlyContinue).Count
    Write-Host "📂 Sessões armazenadas: $SessionCount"
}

Write-Host ""
Write-Host "✨ Verificação completa!"
