#!/bin/bash
# create-skill.sh - Interactive wizard to create a new Skill
# Usage: ./create-skill.sh [skill-name]

set -e

echo "🎯 Qwen Code - Skill Creator"
echo "============================"
echo ""

if [ -z "$1" ]; then
    echo "Enter skill name (lowercase, hyphens): "
    read SKILL_NAME
else
    SKILL_NAME="$1"
fi

# Validate name
if ! echo "$SKILL_NAME" | grep -qE '^[a-z0-9-]+$'; then
    echo "❌ Invalid name. Use only lowercase letters, numbers, and hyphens."
    exit 1
fi

echo ""
echo "Enter description (what it does + when to use): "
echo "(Be specific, include trigger keywords)"
read -r DESCRIPTION

# Determine scope
echo ""
echo "Scope:"
echo "  1) Project (.qwen/skills/) - shared via Git"
echo "  2) Personal (~/.qwen/skills/) - local only"
read -p "Choose (1/2) [1]: " SCOPE

if [ "$SCOPE" = "2" ]; then
    SKILL_DIR="$HOME/.qwen/skills/$SKILL_NAME"
else
    SKILL_DIR=".qwen/skills/$SKILL_NAME"
fi

# Create directory structure
mkdir -p "$SKILL_DIR"
mkdir -p "$SKILL_DIR/scripts"
mkdir -p "$SKILL_DIR/templates"

# Create SKILL.md
cat > "$SKILL_DIR/SKILL.md" << EOF
---
name: $SKILL_NAME
description: $DESCRIPTION
---

# ${SKILL_NAME//-/ }

## Instructions
Provide step-by-step guidance for Qwen Code.

## Examples
Show concrete usage examples.

\`\`\`bash
# Example command
echo "Hello from $SKILL_NAME"
\`\`\`
EOF

echo ""
echo "✅ Skill created at: $SKILL_DIR"
echo ""
echo "📁 Structure:"
echo "  $SKILL_DIR/"
echo "  ├── SKILL.md          ← Edit this file"
echo "  ├── scripts/          ← Add helper scripts here"
echo "  └── templates/        ← Add templates here"
echo ""
echo "📝 Next steps:"
echo "  1. Edit SKILL.md with detailed instructions"
echo "  2. Add helper scripts to scripts/ (optional)"
echo "  3. Test with: /skills $SKILL_NAME"
echo "  4. If project scope, commit and push for team access"
