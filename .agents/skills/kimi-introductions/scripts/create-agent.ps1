#requires -Version 5.1
<#
.SYNOPSIS
    Script para criar um novo agente customizado no Kimi CLI
.DESCRIPTION
    Cria os arquivos YAML e MD para um agente customizado
.PARAMETER AgentName
    Nome do agente a ser criado
.EXAMPLE
    .\create-agent.ps1 meu-agente
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory=$true, Position=0)]
    [string]$AgentName
)

# Normalizar nome
$AgentName = $AgentName.ToLower() -replace '\s+', '-'

# Detectar diretório de agentes
$AgentsDir = $null
if (Test-Path ".kimi/agents") {
    $AgentsDir = ".kimi/agents"
} elseif (Test-Path ".claude/agents") {
    $AgentsDir = ".claude/agents"
} else {
    $AgentsDir = ".kimi/agents"
    New-Item -ItemType Directory -Force -Path $AgentsDir | Out-Null
}

$AgentFile = Join-Path $AgentsDir "$AgentName.yaml"
$PromptFile = Join-Path $AgentsDir "$AgentName-prompt.md"

if (Test-Path $AgentFile) {
    Write-Error "Agente '$AgentName' já existe"
    exit 1
}

# Criar arquivo do agente
$AgentContent = @"
version: 1
agent:
  name: $AgentName
  extend: default
  system_prompt_path: ./$AgentName-prompt.md
  # system_prompt_args:
  #   CUSTOM_VAR: "valor"
  # exclude_tools:
  #   - "kimi_cli.tools.web:SearchWeb"
  # subagents:
  #   coder:
  #     path: ./${AgentName}-coder.yaml
  #     description: "Specialized coder subagent"
"@

Set-Content -Path $AgentFile -Value $AgentContent -Encoding UTF8

# Criar arquivo de prompt
$PromptContent = @"
# $AgentName Agent

You are the $AgentName agent, specialized in...

## Role

Describe the specific role and expertise of this agent.

## Guidelines

- Guideline 1
- Guideline 2
- Guideline 3

## Context

Current time: `${KIMI_NOW}`
Working directory: `${KIMI_WORK_DIR}`
`${KIMI_AGENTS_MD}`

## Custom Variables

`${CUSTOM_VAR}`
"@

Set-Content -Path $PromptFile -Value $PromptContent -Encoding UTF8

Write-Host ""
Write-Host "✅ Agente '$AgentName' criado com sucesso!" -ForegroundColor Green
Write-Host ""
Write-Host "📂 Arquivos criados:"
Write-Host "  - $AgentFile"
Write-Host "  - $PromptFile"
Write-Host ""
Write-Host "Próximos passos:"
Write-Host "  1. Edite $AgentFile para configurar ferramentas e subagentes"
Write-Host "  2. Edite $PromptFile para definir o comportamento"
Write-Host "  3. Use com: kimi --agent-file $AgentFile"
