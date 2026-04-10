#!/usr/bin/env bash
# create-skill.sh — Scaffold a new Copilot/Claude/Agent skill
#
# Usage:
#   ./create-skill.sh <skill-name> [skills-dir]
#
# Examples:
#   ./create-skill.sh my-new-skill
#   ./create-skill.sh github-actions-debugging .github/skills
#   ./create-skill.sh svg-converter ~/.copilot/skills

set -euo pipefail

SKILL_NAME="${1:-}"
SKILLS_DIR="${2:-.github/skills}"

if [[ -z "$SKILL_NAME" ]]; then
  echo "Usage: $0 <skill-name> [skills-dir]" >&2
  echo "  skill-name  lowercase, hyphens only (e.g., my-skill)" >&2
  echo "  skills-dir  default: .github/skills" >&2
  exit 1
fi

# Validate naming convention (agentskills.io: lowercase, hyphens)
if [[ ! "$SKILL_NAME" =~ ^[a-z][a-z0-9-]*[a-z0-9]$|^[a-z]$ ]]; then
  echo "Error: skill name must be lowercase letters and hyphens only." >&2
  echo "  Good: my-skill, github-actions-debugging" >&2
  echo "  Bad:  MySkill, my_skill, -my-skill" >&2
  exit 1
fi

SKILL_DIR="$SKILLS_DIR/$SKILL_NAME"

if [[ -d "$SKILL_DIR" ]]; then
  echo "Error: '$SKILL_DIR' already exists." >&2
  exit 1
fi

mkdir -p "$SKILL_DIR"

cat > "$SKILL_DIR/SKILL.md" << SKILL_EOF
---
name: ${SKILL_NAME}
description: >
  [Describe what this skill does and WHEN Copilot should use it.
  Include explicit trigger phrases, e.g. "Use when asked to ...".
  This description is how Copilot decides to load the skill automatically.]
# allowed-tools: shell   # uncomment ONLY if you have reviewed all scripts here
---

# ${SKILL_NAME}

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
SKILL_EOF

echo "✓ Created skill: $SKILL_DIR/SKILL.md"
echo ""
echo "Next steps:"
echo "  1. Edit $SKILL_DIR/SKILL.md"
echo "     - Set a clear, trigger-phrase-rich 'description'"
echo "     - Replace placeholder instructions with real steps"
echo "  2. Optionally add scripts to $SKILL_DIR/"
echo "     - Reference them by relative path in SKILL.md"
echo "  3. Reload skills in the CLI: /skills reload"
echo "  4. Test: ask Copilot the exact question from the description"
