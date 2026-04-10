# create-agent.ps1 — Scaffold a new Copilot custom agent profile (PowerShell)
#
# Usage:
#   .\create-agent.ps1 <AgentName> [AgentsDir]
#
# Examples:
#   .\create-agent.ps1 test-specialist
#   .\create-agent.ps1 implementation-planner .github/agents
#   .\create-agent.ps1 my-reviewer "$HOME/.copilot/agents"

param(
    [Parameter(Mandatory = $true, Position = 0)]
    [string]$AgentName,

    [Parameter(Position = 1)]
    [string]$AgentsDir = ".github/agents"
)

$ErrorActionPreference = 'Stop'

# Validate naming convention (letters, numbers, . - _)
if ($AgentName -notmatch '^[a-zA-Z0-9._-]+$') {
    Write-Error "Agent name may only contain letters, numbers, '.', '-', '_'."
    exit 1
}

$AgentFile = Join-Path $AgentsDir "$AgentName.agent.md"

if (Test-Path $AgentFile) {
    Write-Error "'$AgentFile' already exists."
    exit 1
}

New-Item -ItemType Directory -Path $AgentsDir -Force | Out-Null

$AgentContent = @"
---
name: $AgentName
description: >
  [One paragraph describing what this agent does and its specific domain.
  Be concrete — Copilot reads this to decide whether to auto-delegate.
  Example: "Focuses on test coverage. Use when writing or reviewing tests.
  Does NOT modify production code."]
tools: ["read", "edit", "search"]
# tools: ["read", "search"]           # read-only variant
# tools: ["read", "edit", "search", "shell"]   # with shell access
# model: claude-sonnet-4.5             # IDE-only; ignored in CLI
# target: github-copilot               # "vscode" | "github-copilot" | omit for both
---

You are a [ROLE] specialist focused on [DOMAIN].

## Responsibilities

- [Primary responsibility — be specific]
- [Secondary responsibility]
- [What you explicitly do NOT do]

## Approach

1. [First step you always take — e.g., read existing code first]
2. [Second step]
3. [Validation or confirmation step]

## Output Standards

- [File naming conventions]
- [Structure or formatting requirements]
- [Quality bar — what "done" looks like]
"@

Set-Content -Path $AgentFile -Value $AgentContent -Encoding UTF8

Write-Host "✓ Created agent: $AgentFile" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:"
Write-Host "  1. Edit $AgentFile"
Write-Host "     - Sharpen the 'description' (it drives auto-delegation)"
Write-Host "     - Adjust 'tools' to the minimum needed"
Write-Host "     - Replace placeholder prompt with real expertise"
Write-Host "  2. Commit the file — it is live when merged to default branch"
Write-Host "  3. Use it:"
Write-Host "     /agent $AgentName"
Write-Host "     Use the $AgentName agent to ..."
Write-Host "     copilot --agent=$AgentName --prompt `"...`""
