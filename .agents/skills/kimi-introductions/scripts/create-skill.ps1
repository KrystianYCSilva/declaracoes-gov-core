#requires -Version 5.1
<#
.SYNOPSIS
    Script para criar uma nova skill no Kimi CLI
.DESCRIPTION
    Cria a estrutura completa de uma skill com SKILL.md, referências e scripts
.PARAMETER SkillName
    Nome da skill a ser criada
.EXAMPLE
    .\create-skill.ps1 minha-skill
#>
[CmdletBinding()]
param(
    [Parameter(Mandatory=$true, Position=0)]
    [string]$SkillName
)

# Normalizar nome (lowercase, hífens)
$SkillName = $SkillName.ToLower() -replace '\s+', '-'

# Detectar diretório de skills
$SkillsDir = $null
if (Test-Path ".kimi/skills") {
    $SkillsDir = ".kimi/skills"
} elseif (Test-Path ".claude/skills") {
    $SkillsDir = ".claude/skills"
} elseif (Test-Path ".agents/skills") {
    $SkillsDir = ".agents/skills"
} else {
    $SkillsDir = ".kimi/skills"
    New-Item -ItemType Directory -Force -Path $SkillsDir | Out-Null
}

$SkillDir = Join-Path $SkillsDir $SkillName

if (Test-Path $SkillDir) {
    Write-Error "Skill '$SkillName' já existe em $SkillDir"
    exit 1
}

# Criar estrutura
Write-Host "📁 Criando skill '$SkillName'..."
$null = New-Item -ItemType Directory -Force -Path (Join-Path $SkillDir "scripts")
$null = New-Item -ItemType Directory -Force -Path (Join-Path $SkillDir "references")
$null = New-Item -ItemType Directory -Force -Path (Join-Path $SkillDir "assets")

# Criar SKILL.md
$SkillContent = @"
---
name: $SkillName
description: Descrição da skill $SkillName
---

# $SkillName

## Visão Geral

Adicione aqui uma descrição do que esta skill faz.

## Uso

Explique como usar esta skill:

```bash
/skill:$SkillName
```

## Funcionalidades

- Funcionalidade 1
- Funcionalidade 2
- Funcionalidade 3

## Referências

- **Guia Detalhado**: Veja [references/guide.md](references/guide.md)
- **Exemplos**: Veja [references/examples.md](references/examples.md)
- **Scripts**: Veja [scripts/](scripts/)

## Exemplos

### Exemplo 1

```
Prompt de exemplo para usar com esta skill
```

### Exemplo 2

```
Outro exemplo de uso
```
"@

Set-Content -Path (Join-Path $SkillDir "SKILL.md") -Value $SkillContent -Encoding UTF8

# Criar referências
$GuideContent = @"
# Guia de $SkillName

## Conceitos

Explique os conceitos principais desta skill.

## Configuração

Detalhes de configuração.

## API/Interface

Documentação da interface.
"@

Set-Content -Path (Join-Path $SkillDir "references/guide.md") -Value $GuideContent -Encoding UTF8

$ExamplesContent = @"
# Exemplos de $SkillName

## Exemplo Básico

```
Exemplo de uso básico aqui
```

## Exemplo Avançado

```
Exemplo de uso avançado aqui
```
"@

Set-Content -Path (Join-Path $SkillDir "references/examples.md") -Value $ExamplesContent -Encoding UTF8

# Criar script de exemplo
$ScriptContent = @"
# Script de exemplo para $SkillName

Write-Host "Script de exemplo para $SkillName"
Write-Host "Modifique este script conforme necessário"
"@

Set-Content -Path (Join-Path $SkillDir "scripts/example.ps1") -Value $ScriptContent -Encoding UTF8

Write-Host ""
Write-Host "✅ Skill '$SkillName' criada com sucesso!" -ForegroundColor Green
Write-Host ""
Write-Host "📂 Estrutura criada em: $SkillDir"
Write-Host ""
Write-Host "Próximos passos:"
Write-Host "  1. Edite $SkillDir/SKILL.md"
Write-Host "  2. Adicione conteúdo a references/"
Write-Host "  3. Crie scripts em scripts/"
Write-Host "  4. Teste com: /skill:$SkillName"
