#!/usr/bin/env bash
# Lista ficheiros Markdown em .cursor/commands/ (slash commands do projeto).
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
# scripts/ -> cursor-introduction/ -> skills/ -> .cursor/
CURSOR_CMD="$(cd "$SCRIPT_DIR/../../.." && pwd)/commands"
if [[ ! -d "$CURSOR_CMD" ]]; then
  echo "Diretório não encontrado: $CURSOR_CMD" >&2
  exit 1
fi
echo ".cursor/commands (relativo à raiz do repositório):"
for f in "$CURSOR_CMD"/*.md; do
  [[ -e "$f" ]] || continue
  base="$(basename "$f" .md)"
  echo "  ${base}.md  ->  tipicamente /${base} no chat"
done
