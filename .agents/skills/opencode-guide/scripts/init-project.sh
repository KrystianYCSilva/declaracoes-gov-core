#!/bin/bash
# init-project.sh - Initialize a project with opencode best practices

PROJECT_PATH="${1:-.}"

echo "Initializing project with opencode..."

cd "$PROJECT_PATH"

# Check if git repo
if [ ! -d ".git" ]; then
    echo "Not a git repository. Initializing..."
    git init
fi

# Create opencode directory structure
mkdir -p .opencode/{agents,commands,skills}

# Create default AGENTS.md if not exists
if [ ! -f "AGENTS.md" ]; then
    cat > AGENTS.md << 'EOF'
# Project Name

## Quick Commands

- npm run dev
- npm test
- npm run build
EOF
    echo "Created AGENTS.md"
fi

# Create opencode.json if not exists
if [ ! -f "opencode.json" ]; then
    cat > opencode.json << 'EOF'
{
  "$schema": "https://opencode.ai/config.json",
  "model": "anthropic/claude-sonnet-4-20250514",
  "permission": {
    "edit": "ask",
    "bash": "ask"
  }
}
EOF
    echo "Created opencode.json"
fi

echo "Done!"
