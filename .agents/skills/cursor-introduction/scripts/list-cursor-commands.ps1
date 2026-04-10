# Lista ficheiros Markdown em .cursor/commands/ (slash commands do projeto).
$ErrorActionPreference = 'Stop'
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
# scripts/ -> cursor-introduction/ -> skills/ -> .cursor/
$CursorCmd = Join-Path (Resolve-Path (Join-Path $ScriptDir '..\..\..')).Path 'commands'
if (-not (Test-Path -LiteralPath $CursorCmd)) {
    Write-Error "Diretório não encontrado: $CursorCmd"
    exit 1
}
Write-Host '.cursor/commands (relativo à raiz do repositório):'
Get-ChildItem -LiteralPath $CursorCmd -Filter '*.md' -File | Sort-Object Name | ForEach-Object {
    $base = $_.BaseName
    Write-Host "  $($_.Name)  ->  tipicamente /$base no chat"
}
