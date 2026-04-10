@echo off
REM create-skill.ps1 - PowerShell script to create a new Skill
REM Usage: .\create-skill.ps1 [skill-name]

param(
    [Parameter(Mandatory=$false)]
    [string]$SkillName
)

Write-Host "🎯 Qwen Code - Skill Creator" -ForegroundColor Green
Write-Host "============================" -ForegroundColor Green
Write-Host ""

if (-not $SkillName) {
    $SkillName = Read-Host "Enter skill name (lowercase, hyphens)"
}

# Validate name
if ($SkillName -notmatch '^[a-z0-9-]+$') {
    Write-Host "❌ Invalid name. Use only lowercase letters, numbers, and hyphens." -ForegroundColor Red
    exit 1
}

Write-Host ""
$Description = Read-Host "Enter description (what it does + when to use)"

Write-Host ""
Write-Host "Scope:"
Write-Host "  1) Project (.qwen/skills/) - shared via Git"
Write-Host "  2) Personal (~/.qwen/skills/) - local only"
$Scope = Read-Host "Choose (1/2) [1]"

if ($Scope -eq "2") {
    $SkillDir = Join-Path $HOME ".qwen\skills\$SkillName"
} else {
    $SkillDir = ".qwen\skills\$SkillName"
}

# Create directory structure
New-Item -ItemType Directory -Force -Path "$SkillDir\scripts" | Out-Null
New-Item -ItemType Directory -Force -Path "$SkillDir\templates" | Out-Null

# Create SKILL.md
$SkillMd = @"
---
name: $SkillName
description: $Description
---

# $($SkillName -replace '-', ' ')

## Instructions
Provide step-by-step guidance for Qwen Code.

## Examples
Show concrete usage examples.

``````bash
# Example command
echo "Hello from $SkillName"
``````
"@

Set-Content -Path "$SkillDir\SKILL.md" -Value $SkillMd -Encoding UTF8

Write-Host ""
Write-Host "✅ Skill created at: $SkillDir" -ForegroundColor Green
Write-Host ""
Write-Host "📁 Structure:"
Write-Host "  $SkillDir/"
Write-Host "  ├── SKILL.md          ← Edit this file"
Write-Host "  ├── scripts/          ← Add helper scripts here"
Write-Host "  └── templates/        ← Add templates here"
Write-Host ""
Write-Host "📝 Next steps:"
Write-Host "  1. Edit SKILL.md with detailed instructions"
Write-Host "  2. Add helper scripts to scripts/ (optional)"
Write-Host "  3. Test with: /skills $SkillName"
