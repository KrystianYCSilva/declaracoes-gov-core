#!/usr/bin/env bash
# install-copilot.sh — Install or update GitHub Copilot CLI
#
# Usage:
#   ./install-copilot.sh [--version VERSION] [--prefix PREFIX]
#
# Options:
#   --version VERSION   Install a specific version (e.g. v0.0.369)
#                       Default: latest
#   --prefix  PREFIX    Install to PREFIX/bin/
#                       Default: /usr/local (root) or ~/.local (non-root)
#
# Examples:
#   ./install-copilot.sh
#   ./install-copilot.sh --version v0.0.369
#   ./install-copilot.sh --prefix "$HOME/.local"
#
# Supported platforms: macOS, Linux
# Windows users: use WinGet or npm (see https://gh.io/copilot-install)

set -euo pipefail

VERSION=""
PREFIX=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --version) VERSION="$2"; shift 2 ;;
    --prefix)  PREFIX="$2"; shift 2 ;;
    -h|--help)
      sed -n '2,20p' "$0" | sed 's/^# //' | sed 's/^#//'
      exit 0
      ;;
    *) echo "Unknown option: $1" >&2; exit 1 ;;
  esac
done

OS="$(uname -s)"
if [[ "$OS" != "Darwin" && "$OS" != "Linux" ]]; then
  echo "This script supports macOS and Linux only." >&2
  echo "Windows: winget install GitHub.Copilot" >&2
  echo "         npm install -g @github/copilot" >&2
  exit 1
fi

# Build install command
INSTALL_CMD="curl -fsSL https://gh.io/copilot-install | "
[[ -n "$VERSION" ]] && INSTALL_CMD+="VERSION=\"$VERSION\" "
[[ -n "$PREFIX" ]]  && INSTALL_CMD+="PREFIX=\"$PREFIX\" "
INSTALL_CMD+="bash"

echo "Installing GitHub Copilot CLI..."
[[ -n "$VERSION" ]] && echo "  Version: $VERSION" || echo "  Version: latest"
[[ -n "$PREFIX" ]]  && echo "  Prefix:  $PREFIX"  || echo "  Prefix:  default"
echo ""

eval "$INSTALL_CMD"

echo ""
echo "✓ GitHub Copilot CLI installed successfully."
echo ""

# Verify installation
if command -v copilot &>/dev/null; then
  INSTALLED_VERSION="$(copilot --version 2>/dev/null || true)"
  echo "  Binary: $(command -v copilot)"
  [[ -n "$INSTALLED_VERSION" ]] && echo "  Version: $INSTALLED_VERSION"
else
  echo "Note: 'copilot' not found in PATH."
  echo "You may need to add the install prefix to PATH:"
  echo "  export PATH=\"\$HOME/.local/bin:\$PATH\""
fi

echo ""
echo "Next steps:"
echo "  1. cd /your/project"
echo "  2. copilot"
echo "  3. /login  (if not already authenticated)"
echo "  4. /init   (generate instructions for this repo)"
