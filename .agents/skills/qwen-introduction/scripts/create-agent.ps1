@echo off
REM create-agent.ps1 - PowerShell script to create a new SubAgent
REM Usage: .\create-agent.ps1 [agent-name]

param(
    [Parameter(Mandatory=$false)]
    [string]$AgentName
)

Write-Host "🤖 Qwen Code - Agent Creator" -ForegroundColor Green
Write-Host "=============================" -ForegroundColor Green
Write-Host ""

if (-not $AgentName) {
    $AgentName = Read-Host "Enter agent name (lowercase, hyphens)"
}

# Validate name
if ($AgentName -notmatch '^[a-z0-9-]+$') {
    Write-Host "❌ Invalid name. Use only lowercase letters, numbers, and hyphens." -ForegroundColor Red
    exit 1
}

Write-Host ""
$Description = Read-Host "Enter description (when and how to use)"

Write-Host ""
$ToolsInput = Read-Host "Select tools (comma-separated, or 'all' for common)"

if ($ToolsInput -eq "all") {
    $Tools = "[read_file, write_file, read_many_files, run_shell_command, web_search, web_fetch]"
} else {
    $ToolsArray = $ToolsInput -split ',' | ForEach-Object { $_.Trim() }
    $Tools = "[" + ($ToolsArray -join ", ") + "]"
}

Write-Host ""
Write-Host "Scope:"
Write-Host "  1) Project (.qwen/agents/) - shared via Git"
Write-Host "  2) Personal (~/.qwen/agents/) - local only"
$Scope = Read-Host "Choose (1/2) [1]"

if ($Scope -eq "2") {
    $AgentDir = Join-Path $HOME ".qwen\agents"
} else {
    $AgentDir = ".qwen\agents"
}

New-Item -ItemType Directory -Force -Path $AgentDir | Out-Null

$AgentFile = Join-Path $AgentDir "$AgentName.md"

$DisplayName = $AgentName -replace '-', ' '

$AgentContent = @"
---
name: $AgentName
description: $Description
tools: $Tools
---
You are a $DisplayName specialist for `${project_name}.

Your expertise includes:
- [Add expertise areas]

Always:
1. Detect language and framework used in the project
2. Follow project's existing patterns and conventions
3. Provide clear, actionable output
4. Include examples and explanations where helpful
"@

Set-Content -Path $AgentFile -Value $AgentContent -Encoding UTF8

Write-Host ""
Write-Host "✅ Agent created at: $AgentFile" -ForegroundColor Green
Write-Host ""
Write-Host "📝 Next steps:"
Write-Host "  1. Edit the file to refine system prompt"
Write-Host "  2. Test with a request matching the description"
Write-Host "  3. Use: /agents manage to view/edit"
