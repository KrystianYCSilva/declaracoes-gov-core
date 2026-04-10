#!/bin/bash
# create-command.sh - Create a new opencode slash command
# Usage: ./create-command.sh <command-name> [description]

set -e

COMMAND_NAME="$1"
DESCRIPTION="${2:-New command for opencode}"

if [ -z "$COMMAND_NAME" ]; then
    echo "Usage: ./create-command.sh <command-name> [description]"
    echo ""
    echo "Example:"
    echo "  ./create-command.sh test 'Run test suite'"
    exit 1
fi

# Determine location
LOCATION="${COMMAND_LOCATION:-.opencode/command}"

# Create commands directory
mkdir -p "$LOCATION"

# Create command file
COMMAND_FILE="$LOCATION/$COMMAND_NAME.md"

cat > "$COMMAND_FILE" << EOF
---
description: $DESCRIPTION
agent: build
---

[Describe what this command does]

$ARGUMENTS

[Detailed instructions for the agent]
EOF

echo "✅ Created command: $COMMAND_NAME"
echo "   Location: $COMMAND_FILE"
echo ""
echo "Edit the command file to customize it:"
echo "  $COMMAND_FILE"
echo ""
echo "Use with: /$COMMAND_NAME"
