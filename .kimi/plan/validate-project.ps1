# Script de Validação de Configuração de Agentes
# Uso: .\validate-project.ps1 -ProjectPath "declaracoes-esocial-leiautes"

param(
    [Parameter(Mandatory=$true)]
    [string]$ProjectPath
)

# Cores
$Green = "Green"
$Yellow = "Yellow"
$Red = "Red"
$Cyan = "Cyan"

function Write-Status($message, $color = $Cyan) {
    Write-Host $message -ForegroundColor $color
}

function Write-Success($message) {
    Write-Host "✓ $message" -ForegroundColor $Green
}

function Write-Warning($message) {
    Write-Host "⚠ $message" -ForegroundColor $Yellow
}

function Write-Error($message) {
    Write-Host "✗ $message" -ForegroundColor $Red
}

$projectName = Split-Path $ProjectPath -Leaf
Write-Status "Validando projeto: $projectName"
Write-Status "Caminho: $ProjectPath"

if (-not (Test-Path $ProjectPath)) {
    Write-Error "Projeto não encontrado: $ProjectPath"
    exit 1
}

$issues = @()
$warnings = @()

# Verificar arquivos obrigatórios
Write-Status "`n=== Verificando arquivos obrigatórios ==="

$requiredFiles = @(
    @{ Path = "AGENTS.md"; Critical = $true },
    @{ Path = ".gitignore"; Critical = $true },
    @{ Path = ".context/README.md"; Critical = $true },
    @{ Path = ".context/ai-assistant-guide.md"; Critical = $true },
    @{ Path = ".context/standards/architectural-rules.md"; Critical = $true },
    @{ Path = ".context/_meta/project-overview.md"; Critical = $false },
    @{ Path = ".context/_meta/tech-stack.md"; Critical = $false },
    @{ Path = ".context/_meta/key-decisions.md"; Critical = $false },
    @{ Path = ".context/_meta/codebase-map.md"; Critical = $false },
    @{ Path = ".kimi/AGENTS.md"; Critical = $true }
)

foreach ($file in $requiredFiles) {
    $filePath = Join-Path $ProjectPath $file.Path
    if (Test-Path $filePath) {
        Write-Success $file.Path
    } else {
        if ($file.Critical) {
            Write-Error "$($file.Path) (CRÍTICO)"
            $issues += "Missing critical file: $($file.Path)"
        } else {
            Write-Warning "$($file.Path) (recomendado)"
            $warnings += "Missing recommended file: $($file.Path)"
        }
    }
}

# Verificar configurações de agentes
Write-Status "`n=== Verificando configurações de agentes ==="

$agentConfigs = @(
    @{ Name = "Kimi"; Path = ".kimi/AGENTS.md" },
    @{ Name = "Gemini"; Path = ".gemini/GEMINI.md" },
    @{ Name = "Claude"; Path = ".claude/CLAUDE.md" },
    @{ Name = "Qwen"; Path = ".qwen/QWEN.md" },
    @{ Name = "CodeBuddy"; Path = ".codebuddy/CODEBUDDY.md" },
    @{ Name = "Codex"; Path = ".codex/AGENTS.md" },
    @{ Name = "OpenCode"; Path = ".opencode/AGENTS.md" },
    @{ Name = "Junie"; Path = ".junie/AGENTS.md" },
    @{ Name = "Vibe"; Path = ".vibe/AGENTS.md" },
    @{ Name = "Cursor"; Path = ".cursor/rules/00-project-bootstrap.mdc" },
    @{ Name = "Copilot"; Path = ".github/copilot-instructions.md" }
)

$agentCount = 0
foreach ($agent in $agentConfigs) {
    $agentPath = Join-Path $ProjectPath $agent.Path
    if (Test-Path $agentPath) {
        Write-Success "$($agent.Name): $($agent.Path)"
        $agentCount++
    } else {
        Write-Error "$($agent.Name): $($agent.Path)"
        $issues += "Missing agent config: $($agent.Name)"
    }
}

# Verificar conteúdo de AGENTS.md
Write-Status "`n=== Verificando conteúdo de AGENTS.md ==="

$agentsFile = Join-Path $ProjectPath "AGENTS.md"
if (Test-Path $agentsFile) {
    $content = Get-Content $agentsFile -Raw
    
    $checks = @(
        @{ Pattern = "Project:"; Description = "Seção Project" },
        @{ Pattern = "Stack:"; Description = "Seção Stack" },
        @{ Pattern = "Architecture Rules:"; Description = "Seção Architecture Rules" },
        @{ Pattern = "Quality Gate:"; Description = "Seção Quality Gate" },
        @{ Pattern = "Tier"; Description = "Referência ao Tier System" },
        @{ Pattern = $projectName; Description = "Nome do projeto mencionado" }
    )
    
    foreach ($check in $checks) {
        if ($content -match $check.Pattern) {
            Write-Success $check.Description
        } else {
            Write-Warning "$($check.Description) não encontrado"
            $warnings += "AGENTS.md missing: $($check.Description)"
        }
    }
    
    # Verificar se ainda tem referências genéricas
    if ($content -match "\{PROJECT_NAME\}") {
        Write-Error "AGENTS.md contém template não substituído: {PROJECT_NAME}"
        $issues += "AGENTS.md has unsubstituted template variables"
    }
    
    if ($content -match "declaracoes-gov-core" -and $projectName -ne "declaracoes-gov-core") {
        Write-Warning "AGENTS.md ainda contém referências ao gov-core - verifique se são intencionais"
        $warnings += "AGENTS.md may have incorrect gov-core references"
    }
}

# Verificar .gitignore
Write-Status "`n=== Verificando .gitignore ==="

$gitignorePath = Join-Path $ProjectPath ".gitignore"
if (Test-Path $gitignorePath) {
    $gitignore = Get-Content $gitignorePath -Raw
    
    $secretPatterns = @("secret/", "*.p12", "*.pfx", "*.pem", ".env")
    foreach ($pattern in $secretPatterns) {
        if ($gitignore -match $pattern) {
            Write-Success "Proteção de secrets: $pattern"
        } else {
            Write-Warning "Faltando proteção: $pattern"
            $warnings += ".gitignore missing: $pattern"
        }
    }
}

# Verificar estrutura .context
Write-Status "`n=== Verificando estrutura .context ==="

$contextDirs = @("_meta", "standards", "patterns", "workflows")
foreach ($dir in $contextDirs) {
    $dirPath = Join-Path $ProjectPath ".context/$dir"
    if (Test-Path $dirPath) {
        $files = Get-ChildItem $dirPath -File | Measure-Object
        Write-Success "$dir/ ($($files.Count) arquivos)"
    } else {
        Write-Warning "Diretório não encontrado: .context/$dir/"
        $warnings += "Missing context dir: $dir"
    }
}

# Resumo
Write-Status "`n=== Resumo da Validação ==="

if ($issues.Count -eq 0 -and $warnings.Count -eq 0) {
    Write-Success "Validação bem-sucedida! Nenhum problema encontrado."
    Write-Success "Agentes configurados: $agentCount/11"
} else {
    if ($issues.Count -gt 0) {
        Write-Error "Problemas críticos encontrados: $($issues.Count)"
        foreach ($issue in $issues) {
            Write-Error "  - $issue"
        }
    }
    
    if ($warnings.Count -gt 0) {
        Write-Warning "Avisos: $($warnings.Count)"
        foreach ($warning in $warnings) {
            Write-Warning "  - $warning"
        }
    }
    
    Write-Status "`nAgentes configurados: $agentCount/11"
    
    if ($issues.Count -gt 0) {
        exit 1
    }
}

# Verificação final
Write-Status "`n=== Recomendações ==="

if ($agentCount -lt 11) {
    Write-Warning "Faltam $($11 - $agentCount) configurações de agentes"
}

Write-Status "1. Revise o conteúdo dos arquivos em .context/_meta/"
Write-Status "2. Verifique se as regras arquiteturais refletem o projeto"
Write-Status "3. Atualize a documentação de patterns se necessário"
Write-Status "4. Teste o build: cd $projectName && mvn verify"
