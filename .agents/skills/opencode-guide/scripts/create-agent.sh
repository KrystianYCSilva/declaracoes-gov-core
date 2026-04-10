#!/bin/bash
# create-agent.sh - Create a new opencode agent
# Usage: ./create-agent.sh <agent-name> [mode] [description]

set -e

AGENT_NAME="$1"
MODE="${2:-subagent}"
DESCRIPTION="${3:-New agent for opencode}"

if [ -z "$AGENT_NAME" ]; then
    echo "Usage: ./create-agent.sh <agent-name> [mode] [description]"
    echo ""
    echo "Modes: primary, subagent (default: subagent)"
    echo ""
    echo "Example:"
    echo "  ./create-agent.sh reviewer subagent 'Code review agent'"
    exit 1
fi

# Validate mode
if [[ ! "$MODE" =~ ^(primary|subagent|all)$ ]]; then
    echo "❌ Invalid mode. Use: primary, subagent, or all"
    exit 1
fi

# Determine location
LOCATION="${AGENT_LOCATION:-.opencode/agents}"

# Create agent directory
mkdir -p "$LOCATION"

# Create agent file
AGENT_FILE="$LOCATION/$AGENT_NAME.md"

cat > "$AGENT_FILE" << EOF
---
description: $DESCRIPTION
mode: $MODE
---

You are a specialized agent.

## Your Role

[Describe what this agent does]

## Guidelines

- [Guideline 1]
- [Guideline 2]
- [Guideline 3]

## How to Work

[Explain the agent's approach and methodology]
EOF

echo "✅ Created agent: $AGENT_NAME"
echo "   Location: $AGENT_FILE"
echo "   Mode: $MODE"
echo ""
echo "Edit the agent file to customize it:"
echo "  $AGENT_FILE"
echo ""
echo "Invoke with: @$AGENT_NAME"
