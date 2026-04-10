# create-skill.ps1 — Scaffold a new Copilot/Claude/Agent skill (PowerShell)
#
# Usage:
#   .\create-skill.ps1 <SkillName> [SkillsDir]
#
# Examples:
#   .\create-skill.ps1 my-new-skill
#   .\create-skill.ps1 github-actions-debugging .github/skills
#   .\create-skill.ps1 svg-converter "$HOME/.copilot/skills"

param(
    [Parameter(Mandatory = $true, Position = 0)]
    [string]$SkillName,

    [Parameter(Position = 1)]
    [string]$SkillsDir = ".github/skills"
)

$ErrorActionPreference = 'Stop'

# Validate naming convention (agentskills.io: lowercase, hyphens)
if ($SkillName -notmatch '^[a-z][a-z0-9-]*[a-z0-9]$|^[a-z]$') {
    Write-Error "Skill name must be lowercase letters and hyphens only (e.g., my-skill, github-actions-debugging)."
    exit 1
}

$SkillDir = Join-Path $SkillsDir $SkillName

if (Test-Path $SkillDir) {
    Write-Error "'$SkillDir' already exists."
    exit 1
}

New-Item -ItemType Directory -Path $SkillDir -Force | Out-Null

$SkillContent = @"
---
name: $SkillName
description: >
  [Describe what this skill does and WHEN Copilot should use it.
  Include explicit trigger phrases, e.g. "Use when asked to ...".
  This description is how Copilot decides to load the skill automatically.]
# allowed-tools: shell   # uncomment ONLY if you have reviewed all scripts here
---

# $SkillName

<!-- Replace this block with actual instructions for Copilot to follow. -->

## When to Use

- [Specific scenario 1 that triggers this skill]
- [Specific scenario 2 that triggers this skill]

## Steps

1. [First action Copilot should take]
2. [Second action]
3. [Validation / confirmation step]

## Notes

- [Prerequisites, caveats, or important constraints]
- [Any tools or CLIs the user needs installed]
"@

Set-Content -Path (Join-Path $SkillDir "SKILL.md") -Value $SkillContent -Encoding UTF8

Write-Host "✓ Created skill: $SkillDir\SKILL.md" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:"
Write-Host "  1. Edit $SkillDir\SKILL.md"
Write-Host "     - Set a clear, trigger-phrase-rich 'description'"
Write-Host "     - Replace placeholder instructions with real steps"
Write-Host "  2. Optionally add scripts to $SkillDir\"
Write-Host "     - Reference them by relative path in SKILL.md"
Write-Host "  3. Reload skills in the CLI: /skills reload"
Write-Host "  4. Test: ask Copilot the exact question from the description"
