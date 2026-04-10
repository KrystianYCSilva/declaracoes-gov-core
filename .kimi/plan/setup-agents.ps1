# Script de Setup para Configuração de Agentes de IA
# Uso: .\setup-agents.ps1 -ProjectPath "C:\caminho\para\projeto" [-ProjectName "nome"]

param(
    [Parameter(Mandatory=$true)]
    [string]$ProjectPath,
    
    [Parameter(Mandatory=$false)]
    [string]$ProjectName = "",
    
    [Parameter(Mandatory=$false)]
    [switch]$SkipGitignore,
    
    [Parameter(Mandatory=$false)]
    [switch]$DryRun
)

# Cores para output
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

# Detectar nome do projeto se não fornecido
if ([string]::IsNullOrWhiteSpace($ProjectName)) {
    $ProjectName = Split-Path $ProjectPath -Leaf
}

Write-Status "Configurando agentes de IA para: $ProjectName"
Write-Status "Caminho: $ProjectPath"

if ($DryRun) {
    Write-Warning "MODO SIMULAÇÃO - Nenhum arquivo será criado"
}

# Verificar se diretório existe
if (-not (Test-Path $ProjectPath)) {
    Write-Error "Diretório não encontrado: $ProjectPath"
    exit 1
}

Set-Location $ProjectPath

# Estrutura de diretórios
$directories = @(
    # Context
    ".context\_meta",
    ".context\standards",
    ".context\patterns",
    ".context\workflows",
    ".context\troubleshooting",
    ".context\examples",
    
    # Agents
    ".agents\prompts",
    ".agents\skills",
    
    # Agent-specific
    ".claude",
    ".codex",
    ".gemini",
    ".qwen",
    ".opencode",
    ".junie",
    ".vibe",
    ".codebuddy",
    
    # GitHub
    ".github"
)

Write-Status "`nCriando estrutura de diretórios..."

foreach ($dir in $directories) {
    $fullPath = Join-Path $ProjectPath $dir
    if (-not (Test-Path $fullPath)) {
        if (-not $DryRun) {
            New-Item -ItemType Directory -Path $fullPath -Force | Out-Null
        }
        Write-Success "Criado: $dir"
    } else {
        Write-Warning "Já existe: $dir"
    }
}

# Templates de arquivos
$files = @{
    # AGENTS.md (raiz)
    "AGENTS.md" = @"
# AGENTS

This file is the shared multi-CLI entrypoint for the repository.
CLI-specific primary instruction files live in the native tool directories:

- Codex: `.codex/AGENTS.md`
- Claude: `.claude/CLAUDE.md`
- Cursor: `.cursor/rules/00-project-bootstrap.mdc`
- Gemini: `.gemini/GEMINI.md`
- Qwen: `.qwen/QWEN.md`
- Kimi: `.kimi/AGENTS.md`
- OpenCode: `.opencode/AGENTS.md`
- Junie: `.junie/AGENTS.md`
- Vibe: `.vibe/AGENTS.md`
- CodeBuddy: `.codebuddy/CODEBUDDY.md`
- Copilot: `.github/copilot-instructions.md`

All LLM-facing files in this repository must stay in English:
- main instruction files
- skills
- commands
- agents and subagents
- prompt assets

## AI Context Bootstrap

Before generating code, tests, or AI-facing documentation, load the AI context in this order:
1. `.context/README.md`
2. `.context/ai-assistant-guide.md`
3. `.context/standards/architectural-rules.md`

Then load only the task-specific files needed for the current request.

## AI Context Tier System

| Tier | Purpose | Authority | Files |
|------|---------|-----------|-------|
| `T0` | Enforcement | Absolute | `.context/standards/architectural-rules.md` |
| `T1` | Standards and patterns | Normative | `.context/standards/code-quality.md`, `.context/standards/testing-strategy.md`, `.context/patterns/` |
| `T2` | Project context | Informative | `.context/_meta/` |
| `T3` | Examples | Illustrative | `.context/examples/` |

Conflict resolution:
- If `T0` conflicts with any other tier, `T0` wins.
- If `T1` conflicts with `T2` or `T3`, `T1` wins.
- If `T2` conflicts with `T3`, `T2` wins.
- If `.context/`, `docs/`, and `src/` disagree, confirm the current truth in `src/`, then update `docs/`, then update `.context/`.

## AI Context Index

Bootstrap and navigation:
- `.context/README.md`
- `.context/ai-assistant-guide.md`

Project metadata:
- `.context/_meta/project-overview.md`
- `.context/_meta/tech-stack.md`
- `.context/_meta/key-decisions.md`
- `.context/_meta/codebase-map.md`

Standards:
- `.context/standards/architectural-rules.md`
- `.context/standards/code-quality.md`
- `.context/standards/testing-strategy.md`

Patterns:
- `.context/patterns/architecture-patterns.md`
- `.context/patterns/testing-and-tdd.md`

Workflows:
- `.context/workflows/development-workflow.md`
- `.context/workflows/testing-and-validation-workflow.md`
- `.context/workflows/context-sync-workflow.md`

## Context Synchronization Rule

- `src/` is the live implementation.
- `docs/` is the human-facing record and must remain in Portuguese.
- `.context/` is the AI-facing compressed record and must remain in English.
- Any change to behavior, architecture, tests, profiles, or operational setup must keep `src/`, `docs/`, and `.context/` synchronized.

## Project

- Name: `$ProjectName`
- Baseline release: `1.0.0`
- Type: `Java Library`
- Domain: `Brazilian Tax Declarations`

## Stack

- Java 8+
- Maven 3.9+
- JAXB (XML binding)
- Apache XML Security (signature)
- Jackson (JSON, optional)
- JUnit 5 + Mockito (testing)

## Architecture Rules

- Keep library agnostic - no framework dependencies.
- Keep business logic in domain services.
- Keep models immutable where possible.
- All public APIs must be thread-safe.
- Maintain Java 8 source compatibility.

## Quality Gate

- `mvn verify` must stay green.
- Minimum coverage:
  - `80%` line
  - `75%` branch

## Useful Commands

- Compile: `mvn -q -DskipTests compile`
- Test: `mvn -q test`
- Validate: `mvn -q verify`
- Package: `mvn -q -DskipTests package`
"@

    # .context/README.md
    ".context\README.md" = @"
---
name: context-hub
description: |
  Navigation hub for the AI-facing context of $ProjectName.
  Use when: loading the minimum authoritative context before changing code, tests, or documentation.
---

# .context Hub

## Quick Start

1. Load `standards/architectural-rules.md` first.
2. Load `_meta/project-overview.md` and `_meta/codebase-map.md`.
3. Load only the task-specific files listed below.

## Authority Model

| Tier | Kind | Authority | Typical Files |
| --- | --- | --- | --- |
| T0 | Enforcement | Absolute | `standards/architectural-rules.md` |
| T1 | Standards and patterns | Normative | `standards/`, `patterns/` |
| T2 | Project context | Informative | `_meta/` |
| T3 | Examples | Illustrative | `examples/` |

Conflict resolution:
- If T0 conflicts with anything else, T0 wins.
- If T1 conflicts with T2 or T3, T1 wins.
- If T2 conflicts with T3, T2 wins.
- If code and docs disagree, confirm the current behavior in `src/` and then update both `docs/` and `.context/`.

## Source Of Truth

- `src/` is authoritative for implementation details and current runtime behavior.
- `docs/` is the human-facing record and remains in Portuguese.
- `.context/` is the compressed English AI-facing layer and must mirror the current code and human docs.

## Task Routing

| Task | Load Next |
| --- | --- |
| Change code behavior | `_meta/codebase-map.md`, `patterns/architecture-patterns.md`, `workflows/development-workflow.md` |
| Change tests | `standards/testing-strategy.md`, `patterns/testing-and-tdd.md`, `workflows/testing-and-validation-workflow.md` |
| Review or QA | `_meta/codebase-map.md`, `workflows/review-qa-and-release-workflow.md` |

## Directory Map

- `_meta/`: project summary, technology, decisions, and package map.
- `standards/`: absolute and normative rules that every change must follow.
- `patterns/`: the real architectural and design patterns used by this codebase.
- `workflows/`: execution playbooks for development, testing, review, QA, and synchronization.
- `troubleshooting/`: common failure modes and their likely causes.
"@

    # .context/ai-assistant-guide.md
    ".context\ai-assistant-guide.md" = @"
---
name: ai-assistant-guide
description: |
  Bootstrap and operating guide for AI assistants working in $ProjectName.
  Use when: deciding what to load, how to classify a request, and what done means in this repository.
---

# AI Assistant Guide

## Bootstrap Sequence

1. Load `standards/architectural-rules.md`.
2. Load `_meta/project-overview.md` and `_meta/codebase-map.md`.
3. Load only the task-specific standard, pattern, workflow, or example files required for the current change.

## Request Classification

| Request Type | Minimum Files To Load |
| --- | --- |
| Model or API change | `standards/architectural-rules.md`, `_meta/codebase-map.md`, `patterns/architecture-patterns.md`, `workflows/development-workflow.md` |
| Security or crypto change | `standards/architectural-rules.md`, `_meta/key-decisions.md`, `_meta/tech-stack.md`, `standards/testing-strategy.md` |
| Test change or regression fix | `standards/testing-strategy.md`, `patterns/testing-and-tdd.md`, `workflows/testing-and-validation-workflow.md` |
| Review or QA request | `_meta/codebase-map.md`, `workflows/review-qa-and-release-workflow.md` |

## Operating Rules

- This is a library project; maintain framework agnosticism.
- Prefer the existing package boundaries and extension points over new abstractions.
- Never invent behavior that is not documented in `docs/` or implemented in `src/`.
- When architecture, behavior, or operational setup changes, update `docs/` first for humans and `.context/` second for AI compression.

## Definition Of Done

- The change respects `standards/architectural-rules.md`.
- The affected tests are updated or added.
- `mvn -q test` stays green for behavior changes.
- `mvn -q verify` stays green before release-level completion.
- JaCoCo minimums remain at `80%` line and `75%` branch.
- No secrets are added to Git.
- `docs/` and `.context/` are synchronized when behavior, architecture, tests, or environment requirements change.

## Research Method

1. Read the relevant human doc in `docs/`.
2. Confirm the current implementation in `src/`.
3. Resolve conflicts in favor of the real code, then update documentation.
4. Summarize the stable truth in `.context/` without duplicating full documents.

## Sync Triggers

Update `.context/` whenever any of the following changes:

- endpoint list, authorization, or request/response contract
- environment variables, profiles, deployment assumptions, or secret locations
- package responsibilities, class responsibilities, or extension points
- test strategy, coverage thresholds, or release gates
- accepted architectural decisions or removed legacy artifacts
"@

    # .context/standards/architectural-rules.md
    ".context\standards\architectural-rules.md" = @"
---
name: architectural-rules
description: |
  Tier T0 rules that define the non-negotiable architecture and security boundaries.
  Use when: proposing, reviewing, or implementing any change in this repository.
---

# Architectural Rules

> Tier: T0 - ABSOLUTE. Every change must comply with these rules.

## `AR-001` Keep Library Agnostic

Rule:
- This is a library, not an application.
- Do not introduce framework-specific dependencies (Spring, Jakarta EE, etc.) in core modules.
- Keep the public API framework-agnostic.

## `AR-002` Thread Safety

Rule:
- All public classes must be thread-safe.
- Prefer immutable objects.
- Document thread-safety guarantees in Javadoc.

## `AR-003` Java 8 Compatibility

Rule:
- Source and target compatibility must remain at Java 8.
- Do not use Java 9+ features (var, new Optional methods, etc.).

## `AR-004` Dependency Minimalism

Rule:
- Keep external dependencies to a minimum.
- Mark optional dependencies as `<optional>true</optional>`.
- Provide SPI for extensibility rather than direct integration.

## `AR-005` No Secrets in Git

Rule:
- Never commit certificates, keys, or credentials.
- Never embed test credentials in source code.
- Use test keystores only for unit tests, never for integration tests with real data.

## `AR-006` Preserve Public API Stability

Rule:
- Do not break backward compatibility in public APIs without major version bump.
- Use `@Deprecated` with migration path before removal.
- Semantic versioning: MAJOR.MINOR.PATCH

## `AR-007` Document Public APIs

Rule:
- All public classes and methods must have Javadoc.
- Include thread-safety notes in class-level Javadoc.
- Document preconditions and exceptions thrown.
"@

    # .context/_meta/project-overview.md
    ".context\_meta\project-overview.md" = @"
---
name: project-overview
description: |
  High-level summary of $ProjectName: purpose, scope, domain, and key stakeholders.
  Use when: onboarding, planning, or writing human-facing docs.
---

# Project Overview

## Name
$ProjectName

## Purpose
Java library for Brazilian tax declarations.

## Version
1.0.0

## Domain
Brazilian Federal Revenue Service (Receita Federal) tax declaration processing.

## Key Features
- Layout models
- Validation
- Serialization

## Status
Planning
"@

    # .context/_meta/tech-stack.md
    ".context\_meta\tech-stack.md" = @"
---
name: tech-stack
description: |
  Technology stack and runtime dependencies.
  Use when: onboarding, debugging, or planning changes.
---

# Technology Stack

## Core

- Java 8+
- Maven 3.9+

## Dependencies

- JAXB (XML binding)
- Apache XML Security (XML signature)
- Jackson (JSON processing, optional)

## Testing

- JUnit 5
- Mockito
- AssertJ

## Build

- Maven Surefire (tests)
- Maven JaCoCo (coverage)
"@

    # .context/_meta/codebase-map.md
    ".context\_meta\codebase-map.md" = @"
---
name: codebase-map
description: |
  Package structure and key classes.
  Use when: navigating the codebase or planning changes.
---

# Codebase Map

## Package Structure

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

TBD - Update after initial implementation
"@

    # .kimi/AGENTS.md
    ".kimi\AGENTS.md" = @"
# Kimi Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific Kimi guidance.

## Baseline

- Project: `$ProjectName`
- Java 8+ Maven library
- Thread-safe, framework-agnostic

## Guardrails

- Keep secrets out of Git.
- Keep public APIs stable.
- Maintain Java 8 compatibility.
- Keep `mvn verify` green.
- All code changes must have tests.

## Useful Commands

- Compile: `mvn -q -DskipTests compile`
- Test: `mvn -q test`
- Validate: `mvn -q verify`
- Package: `mvn -q -DskipTests package`
"@

    # .gemini/GEMINI.md
    ".gemini\GEMINI.md" = @"
@../AGENTS.md

# Gemini-Specific Notes

- This repository keeps Gemini project memory in `.gemini/GEMINI.md`.
- The Gemini-native Spec Kit entrypoints live in `.gemini/commands/`.
- `.gemini/agents/` is kept only as a repository compatibility layer; do not describe it as an official Gemini auto-discovery surface.
- If Gemini bootstrap is requested, preserve the root `AGENTS.md` as the cross-agent index and update this file instead of creating a root `GEMINI.md`.
"@

    # .claude/CLAUDE.md
    ".claude\CLAUDE.md" = @"
# Claude Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific Claude guidance.

## Baseline

- Project: `$ProjectName`
- Java 8+ Maven library
- Thread-safe, framework-agnostic

## Guardrails

- Keep secrets out of Git.
- Keep public APIs stable.
- Maintain Java 8 compatibility.
- Keep `mvn verify` green.
"@

    # .qwen/QWEN.md
    ".qwen\QWEN.md" = @"
# Qwen Instructions

Use the root `AGENTS.md` as the shared repository index.
This file stores the repository-specific Qwen guidance.

## Baseline

- Project: `$ProjectName`
- Java 8+ Maven library
- Thread-safe, framework-agnostic

## Guardrails

- Keep secrets out of Git.
- Keep public APIs stable.
- Maintain Java 8 compatibility.
- Keep `mvn verify` green.
"@

    # .github/copilot-instructions.md
    ".github\copilot-instructions.md" = @"
# Copilot Instructions

This is a Java library project for Brazilian tax declarations.

## Key Points

- Java 8+ compatibility required
- Thread-safe public APIs
- Framework-agnostic (no Spring/Jakarta dependencies)
- Use JAXB for XML, Jackson for JSON
- Minimum 80% line coverage, 75% branch coverage
- All public APIs must have Javadoc

## Code Style

- Follow Java conventions
- Use immutable objects where possible
- Document thread-safety in class-level Javadoc
"@
}

Write-Status "`nCriando arquivos..."

foreach ($file in $files.GetEnumerator()) {
    $fullPath = Join-Path $ProjectPath $file.Key
    $dir = Split-Path $fullPath -Parent
    
    if (-not (Test-Path $fullPath)) {
        if (-not $DryRun) {
            if (-not (Test-Path $dir)) {
                New-Item -ItemType Directory -Path $dir -Force | Out-Null
            }
            $file.Value | Out-File -FilePath $fullPath -Encoding UTF8
        }
        Write-Success "Criado: $($file.Key)"
    } else {
        Write-Warning "Já existe: $($file.Key)"
    }
}

# Atualizar .gitignore
if (-not $SkipGitignore) {
    $gitignorePath = Join-Path $ProjectPath ".gitignore"
    $gitignoreContent = @"

### Agent Secrets ###
secret/
.env
.env.*
*.pem
*.p12
*.pfx
keystore/
truststore/

### AI Context Cache ###
.context/.cache/
.agents/.cache/
"@

    if (Test-Path $gitignorePath) {
        $existing = Get-Content $gitignorePath -Raw
        if (-not $existing.Contains("secret/")) {
            if (-not $DryRun) {
                Add-Content -Path $gitignorePath -Value $gitignoreContent
            }
            Write-Success "Atualizado: .gitignore (adicionadas regras de secrets)"
        } else {
            Write-Warning ".gitignore já contém regras de secrets"
        }
    } else {
        if (-not $DryRun) {
            $defaultGitignore = @"
HELP.md
target/
.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

### VS Code ###
.vscode/

### OS / Logs ###
.DS_Store
Thumbs.db
*.log
logs/

### Local Secrets ###
secret/
.env
.env.*
*.pem
*.p12
*.pfx
keystore/
"@
            $defaultGitignore | Out-File -FilePath $gitignorePath -Encoding UTF8
        }
        Write-Success "Criado: .gitignore"
    }
}

Write-Status "`n=== Resumo ==="
Write-Success "Configuração concluída para: $ProjectName"

if ($DryRun) {
    Write-Warning "Modo simulação - nenhum arquivo foi realmente criado"
}

Write-Status "`nPróximos passos:"
Write-Status "1. Personalize os arquivos em .context/_meta/"
Write-Status "2. Adicione regras específicas em .context/standards/"
Write-Status "3. Documente padrões em .context/patterns/"
Write-Status "4. Teste o build: mvn verify"
