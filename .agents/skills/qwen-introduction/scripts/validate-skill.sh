#!/bin/bash
# validate-skill.sh - Validates SKILL.md syntax and structure
# Usage: ./validate-skill.sh [skill-path]
# If no path provided, validates current directory

set -e

SKILL_PATH="${1:-.}"
SKILL_FILE="$SKILL_PATH/SKILL.md"

echo "🔍 Validating Skill: $SKILL_PATH"

# Check if SKILL.md exists
if [ ! -f "$SKILL_FILE" ]; then
    echo "❌ SKILL.md not found at: $SKILL_FILE"
    exit 1
fi

echo "✅ SKILL.md exists"

# Check if file starts with ---
FIRST_LINE=$(head -n 1 "$SKILL_FILE")
if [ "$FIRST_LINE" != "---" ]; then
    echo "❌ SKILL.md must start with --- on line 1"
    exit 1
fi

echo "✅ Frontmatter delimiter found"

# Extract YAML frontmatter
if grep -q "^---$" "$SKILL_FILE"; then
    echo "✅ Frontmatter section found"
else
    echo "❌ Missing closing --- for frontmatter"
    exit 1
fi

# Check for required fields
if grep -q "^name:" "$SKILL_FILE"; then
    NAME=$(grep "^name:" "$SKILL_FILE" | head -1 | sed 's/^name: *//')
    echo "✅ name field: $NAME"
else
    echo "❌ Missing 'name' field in frontmatter"
    exit 1
fi

if grep -q "^description:" "$SKILL_FILE"; then
    DESC=$(grep "^description:" "$SKILL_FILE" | head -1 | sed 's/^description: *//')
    DESC_LEN=${#DESC}
    echo "✅ description field ($DESC_LEN chars): $DESC"
    
    if [ $DESC_LEN -lt 20 ]; then
        echo "⚠️  Warning: Description is very short. Consider making it more specific with trigger keywords."
    fi
else
    echo "❌ Missing 'description' field in frontmatter"
    exit 1
fi

# Validate name format
if echo "$NAME" | grep -qE '^[a-z0-9-]+$'; then
    echo "✅ Name format is valid (lowercase, numbers, hyphens)"
else
    echo "⚠️  Warning: Name should use lowercase letters, numbers, and hyphens only"
fi

# Check for markdown content after frontmatter
LINE_COUNT=$(wc -l < "$SKILL_FILE")
if [ "$LINE_COUNT" -lt 5 ]; then
    echo "⚠️  Warning: SKILL.md is very short. Consider adding more instructions."
fi

echo ""
echo "✅ Validation complete - Skill structure looks good!"
echo ""
echo "📝 Tips:"
echo "   - Description should include trigger keywords for auto-activation"
echo "   - Use relative paths for scripts/templates"
echo "   - Test with relevant requests before sharing"
