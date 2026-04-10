# Script de Adaptação de Agentes para Projetos declaracoes-*
# Uso: .\adapt-project.ps1 -SourceProject "declaracoes-gov-core" -TargetProject "declaracoes-esocial-leiautes" -ProjectType "leiautes"

param(
    [Parameter(Mandatory=$true)]
    [string]$SourceProject,
    
    [Parameter(Mandatory=$true)]
    [string]$TargetProject,
    
    [Parameter(Mandatory=$true)]
    [ValidateSet("leiautes", "transmissor", "bom")]
    [string]$ProjectType,
    
    [Parameter(Mandatory=$false)]
    [string]$DomainDescription = "",
    
    [Parameter(Mandatory=$false)]
    [switch]$DryRun
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

# Configurações por tipo de projeto
$configs = @{
    leiautes = @{
        Type = "Java Library (JAR)"
        Focus = "XSD-based JAXB models, versioning, event catalog"
        Stack = @("Java 8+", "Maven", "JAXB", "declaracoes-gov-core")
        Patterns = @("Version enum", "Event catalog", "JAXB factories")
        Dependencies = "declaracoes-gov-core (validation)"
    }
    transmissor = @{
        Type = "Java Library (JAR)"
        Focus = "HTTP communication, SOAP/REST, mTLS, async processing"
        Stack = @("Java 8+", "Maven", "Apache HttpClient 5", "declaracoes-gov-core", "declaracoes-*-leiautes")
        Patterns = @("Client interfaces", "Retry strategies", "Circuit breaker")
        Dependencies = "declaracoes-gov-core, declaracoes-*-leiautes"
    }
    bom = @{
        Type = "Maven BOM (Bill of Materials)"
        Focus = "Dependency version management"
        Stack = @("Maven 3.9+")
        Patterns = @("Version alignment")
        Dependencies = "None (manages versions)"
    }
}

$config = $configs[$ProjectType]

if ([string]::IsNullOrWhiteSpace($DomainDescription)) {
    $DomainDescription = switch ($ProjectType) {
        "leiautes" { "Tax declaration layout models" }
        "transmissor" { "HTTP transmission client" }
        "bom" { "Dependency version management" }
    }
}

Write-Status "Adaptando projeto: $TargetProject"
Write-Status "Tipo: $($config.Type)"
Write-Status "Foco: $($config.Focus)"

if ($DryRun) {
    Write-Warning "MODO SIMULAÇÃO - Nenhuma alteração será feita"
}

# Verificar diretórios
$sourcePath = Join-Path $PWD $SourceProject
$targetPath = Join-Path $PWD $TargetProject

if (-not (Test-Path $sourcePath)) {
    Write-Error "Projeto fonte não encontrado: $sourcePath"
    exit 1
}

if (-not (Test-Path $targetPath)) {
    Write-Error "Projeto destino não encontrado: $targetPath"
    exit 1
}

Write-Status "`nCopiando estrutura..."

# Diretórios a copiar
$dirsToCopy = @(
    ".context/_meta",
    ".context/standards",
    ".context/patterns",
    ".context/workflows",
    ".context/troubleshooting",
    ".context/examples",
    ".agents",
    ".claude",
    ".codex",
    ".codebuddy",
    ".gemini",
    ".junie",
    ".kimi",
    ".opencode",
    ".qwen",
    ".vibe",
    ".cursor/rules",
    ".github"
)

# Arquivos a copiar
$filesToCopy = @(
    ".context/README.md",
    ".context/ai-assistant-guide.md",
    "AGENTS.md",
    ".gitignore"
)

# Copiar diretórios
foreach ($dir in $dirsToCopy) {
    $sourceDir = Join-Path $sourcePath $dir
    $targetDir = Join-Path $targetPath $dir
    
    if (Test-Path $sourceDir) {
        if (-not $DryRun) {
            if (-not (Test-Path $targetDir)) {
                New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
            }
            Copy-Item -Path "$sourceDir\*" -Destination $targetDir -Recurse -Force
        }
        Write-Success "Copiado: $dir"
    } else {
        Write-Warning "Não encontrado: $dir"
    }
}

# Copiar arquivos
foreach ($file in $filesToCopy) {
    $sourceFile = Join-Path $sourcePath $file
    $targetFile = Join-Path $targetPath $file
    
    if (Test-Path $sourceFile) {
        if (-not $DryRun) {
            Copy-Item -Path $sourceFile -Destination $targetFile -Force
        }
        Write-Success "Copiado: $file"
    } else {
        Write-Warning "Não encontrado: $file"
    }
}

Write-Status "`nAdaptando conteúdo..."

# Função para substituir texto em arquivos
function Update-FileContent {
    param($FilePath, $Replacements)
    
    if (-not (Test-Path $FilePath)) {
        return
    }
    
    $content = Get-Content $FilePath -Raw
    $originalContent = $content
    
    foreach ($replacement in $Replacements.GetEnumerator()) {
        $content = $content -replace $replacement.Key, $replacement.Value
    }
    
    if ($content -ne $originalContent) {
        if (-not $DryRun) {
            $content | Set-Content $FilePath -NoNewline
        }
        Write-Success "Atualizado: $(Split-Path $FilePath -Leaf)"
    }
}

# Substituições globais
$globalReplacements = @{
    "declaracoes-gov-core" = $TargetProject
    "Domain: Core shared functionality" = "Domain: $DomainDescription"
    "Domain: Brazilian tax declarations (certificates, XML signature, validators)" = "Domain: $DomainDescription"
}

# Atualizar arquivos
$filesToUpdate = Get-ChildItem $targetPath -Recurse -File -Include "*.md", "*.mdc" | 
    Where-Object { $_.FullName -notlike "*.kimi\plan*" -and $_.FullName -notlike "*PLAN*" }

foreach ($file in $filesToUpdate) {
    Update-FileContent -FilePath $file.FullName -Replacements $globalReplacements
}

# Atualizações específicas por tipo
switch ($ProjectType) {
    "bom" {
        Write-Status "`nAplicando adaptações específicas para BOM..."
        
        # Simplificar AGENTS.md para BOM
        $bomAgentsContent = @"
# AGENTS

This file is the shared multi-CLI entrypoint for the repository.

## Project

- Name: `$TargetProject`
- Type: `Maven BOM (Bill of Materials)`
- Domain: Centralized dependency version management for Brazilian tax declaration libraries

## Purpose

Maven BOM that centralizes version management for all declaracoes-* libraries.
Eliminates classpath conflicts by providing compatible version sets.

## Stack

- Maven 3.9+
- No Java code
- Only POM configuration

## Architecture Rules

- Keep versions synchronized across ecosystem
- Document version compatibility matrix
- Update with each release cycle
- Semantic versioning for BOM releases

## Quality Gate

- POM validation passes
- No dependency conflicts
- All referenced versions exist

## Useful Commands

- Validate POM: `mvn validate`
- Check dependencies: `mvn dependency:tree`
- Install locally: `mvn install`
"@
        if (-not $DryRun) {
            $bomAgentsContent | Set-Content (Join-Path $targetPath "AGENTS.md")
        }
        Write-Success "AGENTS.md simplificado para BOM"
        
        # Remover diretórios desnecessários para BOM
        $dirsToRemove = @(
            ".context/patterns",
            ".context/workflows",
            ".context/troubleshooting",
            ".context/examples"
        )
        
        foreach ($dir in $dirsToRemove) {
            $dirPath = Join-Path $targetPath $dir
            if (Test-Path $dirPath) {
                if (-not $DryRun) {
                    Remove-Item $dirPath -Recurse -Force
                }
                Write-Success "Removido (BOM): $dir"
            }
        }
    }
    
    "leiautes" {
        Write-Status "`nAplicando adaptações específicas para Leiautes..."
        
        # Atualizar architectural-rules.md
        $archRulesFile = Join-Path $targetPath ".context/standards/architectural-rules.md"
        if (Test-Path $archRulesFile) {
            $content = Get-Content $archRulesFile -Raw
            
            # Adicionar regra específica para leiautes
            $newRule = @"

## `AR-L01` XSD Compliance

Rule:
- All models must align with official Receita Federal XSD schemas
- Maintain backward compatibility when adding new fields
- Document version-specific differences
"@
            
            if (-not $content.Contains("AR-L01")) {
                if (-not $DryRun) {
                    $content + $newRule | Set-Content $archRulesFile
                }
                Write-Success "Adicionada regra AR-L01 para leiautes"
            }
        }
    }
    
    "transmissor" {
        Write-Status "`nAplicando adaptações específicas para Transmissor..."
        
        # Atualizar architectural-rules.md
        $archRulesFile = Join-Path $targetPath ".context/standards/architectural-rules.md"
        if (Test-Path $archRulesFile) {
            $content = Get-Content $archRulesFile -Raw
            
            # Adicionar regras específicas para transmissor
            $newRules = @"

## `AR-T01` Retry Strategy

Rule:
- All HTTP operations must implement retry with exponential backoff
- Maximum retry attempts: 3
- Respect Retry-After headers from server

## `AR-T02` Circuit Breaker

Rule:
- Implement circuit breaker for external service calls
- Open circuit after 5 consecutive failures
- Half-open after 30 seconds

## `AR-T03` Async Support

Rule:
- Provide both sync and async variants for I/O operations
- Use CompletableFuture for async operations
- Document thread pool requirements
"@
            
            if (-not $content.Contains("AR-T01")) {
                if (-not $DryRun) {
                    $content + $newRules | Set-Content $archRulesFile
                }
                Write-Success "Adicionadas regras AR-T01 a AR-T03 para transmissor"
            }
        }
    }
}

Write-Status "`n=== Resumo ===" $Green
Write-Success "Projeto adaptado: $TargetProject"
Write-Success "Tipo: $($config.Type)"

if ($DryRun) {
    Write-Warning "Modo simulação - nenhuma alteração foi salva"
    Write-Status "Execute sem -DryRun para aplicar as alterações"
} else {
    Write-Status "`nPróximos passos:"
    Write-Status "1. Revise os arquivos adaptados em $TargetProject/.context/"
    Write-Status "2. Atualize _meta/project-overview.md com descrição específica"
    Write-Status "3. Atualize _meta/codebase-map.md com estrutura real"
    Write-Status "4. Verifique se há referências ao gov-core que devem ser mantidas"
    Write-Status "5. Execute validação: .\declaracoes-gov-core\.kimi\plan\validate-project.ps1 -ProjectPath '$TargetProject'"
}
