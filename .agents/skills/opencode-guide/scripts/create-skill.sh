#!/bin/bash
# create-skill.sh - Create a new opencode skill
# Usage: ./create-skill.sh <skill-name> [description]

set -e

SKILL_NAME="$1"
DESCRIPTION="${2:-New skill for opencode}"

if [ -z "$SKILL_NAME" ]; then
    echo "Usage: ./create-skill.sh <skill-name> [description]"
    echo ""
    echo "Example:"
    echo "  ./create-skill.sh my-skill 'My custom skill description'"
    exit 1
fi

# Validate skill name (lowercase, hyphens only)
if ! [[ "$SKILL_NAME" =~ ^[a-z0-9]+(-[a-z0-9]+)*$ ]]; then
    echo "❌ Invalid skill name. Use lowercase letters and hyphens only."
    echo "   Example: my-skill, api-guide, code-review"
    exit 1
fi

# Determine location
LOCATION="${SKILL_LOCATION:-.opencode/skills}"

# Create skill directory
SKILL_DIR="$LOCATION/$SKILL_NAME"
mkdir -p "$SKILL_DIR"

# Create SKILL.md
cat > "$SKILL_DIR/SKILL.md" << EOF
---
name: $SKILL_NAME
description: $DESCRIPTION
license: MIT
compatibility: opencode
metadata:
  audience: developers
  version: 1.0.0
---

## What I do

[Describe what this skill does]

## When to use me

[Explain when to load and use this skill]

## Usage

Load this skill when working on related tasks.

## Examples

### Example 1

[Show an example of using this skill]

### Example 2

[Show another example]
EOF

echo "✅ Created skill: $SKILL_NAME"
echo "   Location: $SKILL_DIR/SKILL.md"
echo ""
echo "Edit the skill file to customize it:"
echo "  $SKILL_DIR/SKILL.md"
