#!/bin/bash
# validate-agent.sh - Validates agent file syntax and structure
# Usage: ./validate-agent.sh [agent-file.md]
# If no file provided, validates all agents in .qwen/agents/

set -e

validate_agent() {
    local AGENT_FILE="$1"
    echo "🔍 Validating Agent: $AGENT_FILE"
    
    # Check if file exists
    if [ ! -f "$AGENT_FILE" ]; then
        echo "❌ File not found: $AGENT_FILE"
        return 1
    fi
    
    # Check if file starts with ---
    FIRST_LINE=$(head -n 1 "$AGENT_FILE")
    if [ "$FIRST_LINE" != "---" ]; then
        echo "❌ Agent file must start with --- on line 1"
        return 1
    fi
    
    echo "  ✅ Frontmatter delimiter found"
    
    # Extract name
    if grep -q "^name:" "$AGENT_FILE"; then
        NAME=$(grep "^name:" "$AGENT_FILE" | head -1 | sed 's/^name: *//')
        echo "  ✅ name: $NAME"
    else
        echo "  ❌ Missing 'name' field"
        return 1
    fi
    
    # Extract description
    if grep -q "^description:" "$AGENT_FILE"; then
        DESC=$(grep "^description:" "$AGENT_FILE" | head -1 | sed 's/^description: *//')
        DESC_LEN=${#DESC}
        echo "  ✅ description ($DESC_LEN chars)"
        
        if [ $DESC_LEN -gt 1000 ]; then
            echo "  ⚠️  Warning: Description >1000 chars will show visual warning"
        fi
        
        if echo "$DESC" | grep -qi "proactively\|must be used"; then
            echo "  ✅ Contains auto-delegation trigger phrase"
        else
            echo "  💡 Tip: Add 'PROACTIVELY' or 'MUST BE USED' for auto-delegation"
        fi
    else
        echo "  ❌ Missing 'description' field"
        return 1
    fi
    
    # Check for tools field
    if grep -q "^tools:" "$AGENT_FILE"; then
        echo "  ✅ tools field present"
    else
        echo "  💡 Tip: Consider adding 'tools' field to restrict agent capabilities"
    fi
    
    # Check for system prompt (content after frontmatter)
    FRONTMATTER_END=$(grep -n "^---$" "$AGENT_FILE" | tail -1 | cut -d: -f1)
    TOTAL_LINES=$(wc -l < "$AGENT_FILE")
    
    if [ -n "$FRONTMATTER_END" ] && [ "$TOTAL_LINES" -gt "$FRONTMATTER_END" ]; then
        PROMPT_LINES=$((TOTAL_LINES - FRONTMATTER_END))
        echo "  ✅ System prompt present ($PROMPT_LINES lines)"
        
        if [ $PROMPT_LINES -gt 333 ]; then
            echo "  ⚠️  Warning: System prompt >10000 chars (~333 lines) may show visual warning"
        fi
    else
        echo "  ❌ Missing system prompt after frontmatter"
        return 1
    fi
    
    echo ""
    return 0
}

# Main logic
if [ $# -eq 1 ]; then
    validate_agent "$1"
else
    # Validate all agents in .qwen/agents/
    AGENT_DIR=".qwen/agents"
    if [ ! -d "$AGENT_DIR" ]; then
        echo "ℹ️  No .qwen/agents/ directory found"
        echo "   Create one with: mkdir -p .qwen/agents"
        exit 0
    fi
    
    AGENT_FILES=$(find "$AGENT_DIR" -name "*.md" -type f 2>/dev/null || true)
    if [ -z "$AGENT_FILES" ]; then
        echo "ℹ️  No agent files found in $AGENT_DIR"
        exit 0
    fi
    
    ERRORS=0
    for FILE in $AGENT_FILES; do
        if ! validate_agent "$FILE"; then
            ERRORS=$((ERRORS + 1))
        fi
    done
    
    if [ $ERRORS -eq 0 ]; then
        echo "✅ All agents validated successfully!"
    else
        echo "❌ $ERRORS agent(s) failed validation"
        exit 1
    fi
fi
