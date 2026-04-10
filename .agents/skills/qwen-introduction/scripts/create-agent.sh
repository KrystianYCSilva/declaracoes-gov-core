#!/bin/bash
# create-agent.sh - Interactive wizard to create a new SubAgent
# Usage: ./create-agent.sh [agent-name]

set -e

echo "🤖 Qwen Code - Agent Creator"
echo "============================="
echo ""

if [ -z "$1" ]; then
    echo "Enter agent name (lowercase, hyphens): "
    read AGENT_NAME
else
    AGENT_NAME="$1"
fi

# Validate name
if ! echo "$AGENT_NAME" | grep -qE '^[a-z0-9-]+$'; then
    echo "❌ Invalid name. Use only lowercase letters, numbers, and hyphens."
    exit 1
fi

echo ""
echo "Enter description (when and how to use): "
echo "(Tip: Include 'PROACTIVELY' for auto-delegation)"
read -r DESCRIPTION

echo ""
echo "Select tools (comma-separated, or 'all' for common tools):"
echo "Available: read_file, write_file, read_many_files, run_shell_command, web_search, web_fetch, grep_search, glob, list_directory, edit"
read -r TOOLS_INPUT

if [ "$TOOLS_INPUT" = "all" ]; then
    TOOLS="[read_file, write_file, read_many_files, run_shell_command, web_search, web_fetch]"
else
    # Convert to YAML array format
    IFS=',' read -ra TOOLS_ARRAY <<< "$TOOLS_INPUT"
    TOOLS="["
    for i in "${!TOOLS_ARRAY[@]}"; do
        TOOL=$(echo "${TOOLS_ARRAY[$i]}" | xargs)
        if [ $i -gt 0 ]; then
            TOOLS="$TOOLS, $TOOL"
        else
            TOOLS="$TOOLS$TOOL"
        fi
    done
    TOOLS="$TOOLS]"
fi

# Determine scope
echo ""
echo "Scope:"
echo "  1) Project (.qwen/agents/) - shared via Git"
echo "  2) Personal (~/.qwen/agents/) - local only"
read -p "Choose (1/2) [1]: " SCOPE

if [ "$SCOPE" = "2" ]; then
    AGENT_DIR="$HOME/.qwen/agents"
else
    AGENT_DIR=".qwen/agents"
fi

mkdir -p "$AGENT_DIR"

AGENT_FILE="$AGENT_DIR/$AGENT_NAME.md"

# Ask for expertise description
echo ""
echo "Enter agent expertise description:"
echo "(What is this agent specialized in?)"
echo "(Press Ctrl+D when done, or enter single line):"
EXPERTISE=$(cat)

# Create agent file
cat > "$AGENT_FILE" << EOF
---
name: $AGENT_NAME
description: $DESCRIPTION
tools: $TOOLS
---
You are a $(echo "$AGENT_NAME" | sed 's/-/ /g') specialist for \${project_name}.

Your expertise includes:
$EXPERTISE

Always:
1. Detect language and framework used in the project
2. Follow project's existing patterns and conventions
3. Provide clear, actionable output
4. Include examples and explanations where helpful
EOF

echo ""
echo "✅ Agent created at: $AGENT_FILE"
echo ""
echo "📝 Next steps:"
echo "  1. Edit the file to refine system prompt"
echo "  2. Test with a request matching the description"
echo "  3. Use: /agents manage to view/edit"
echo "  4. If project scope, commit and push for team access"
