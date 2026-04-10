---
name: permissions
description: |
  Reference for Claude Code's permission model, tool access categories, and safety controls.
  Use when: explaining tool permissions, configuring allowed tools, or reviewing Claude Code security settings.
---

# Permissions & Safety

Permission model, tool access, and security.

## Permission Model

Claude Code is **permission-based**: it asks before taking actions.

### Default Behavior

| Category | Tools | Default |
|----------|-------|---------|
| **Read** | Read, Grep, Glob | ✅ Always allowed |
| **Write** | Write | ❓ Always ask |
| **Execute** | Bash | ❓ Always ask |
| **Delete** | Delete | ❌ Always ask |

`✅` = Pre-approved  
`❓` = Ask each time  
`❌` = Requires approval

### Permission Prompt

When Claude wants to use a restricted tool:

```
Claude wants to:
  - Write to src/components/Button.tsx

Do you want to:
[A] Yes (allow this action)
[D] Details (explain this change)
[S] Session approval (allow all for this session)
[N] No (skip this)
```

## Approval Options

### Per-Action Approval
```
[A] Allow this specific action
```
Good for reviewing each change carefully.

### Session Approval
```
[S] Approve all similar actions this session
```
Speeds up workflow when you trust the current work.

### Skip Action
```
[N] Don't do this
```
Tell Claude to try a different approach.

## Restricting Tool Access in Skills

Limit tools available in a skill:

```yaml
---
name: safe-reader
description: Read files safely
allowed-tools: Read Grep Glob
---

# Claude can only read, not modify
# No permission prompts for these tools
```

### Available Tools

```yaml
allowed-tools: Read              # Read files only
allowed-tools: Read Grep         # Read and search
allowed-tools: Read Write Bash   # Full access
allowed-tools: Read Write Bash Delete  # All tools
```

## CLI Options for Permissions

### Skip All Permissions ⚠️
```bash
claude --dangerously-skip-permissions
```

**Use only for:**
- Trusted scripts
- Automated workflows
- When you've reviewed the code

**Never use for:**
- Untrusted sources
- Unknown operations
- Exploratory work

### Auto-Approve Writes
```bash
claude --auto
```

Approves file writes without prompting.

### Add Directory Access
```bash
claude --add-dir /path/to/directory
```

Grant access to external directory.

## Permission Configuration

### Via `.claude/settings.json`

```json
{
  "permissions": {
    "Read": "allow",
    "Bash": "ask",
    "Write": "ask",
    "Delete": "deny"
  }
}
```

### Levels

```
"allow"   - Always permit without asking
"ask"     - Prompt before each use
"deny"    - Never permit, always refuse
```

## Security Best Practices

### Never Share

Never put these in prompts:

- ❌ API keys or credentials
- ❌ Private SSH keys
- ❌ Database passwords
- ❌ Access tokens
- ❌ Customer PII
- ❌ Private company data

### Always Review

Always check before approving:

- 🔍 External API calls
- 🔍 Credential handling
- 🔍 Permission changes
- 🔍 System modifications
- 🔍 Deployment operations

### Use Git Safely

```bash
# SAFE: Review before pushing
git diff
git status
git push

# RISKY: Skip reviews
--dangerously-skip-permissions  # Review first!
```

## Tool Reference

### Read
**Purpose**: Read files  
**Risk**: Low (information gathering)  
**Status**: Always allowed (no prompt)

### Write
**Purpose**: Modify/create files  
**Risk**: Medium (changes your code)  
**Status**: Always ask

### Bash
**Purpose**: Run commands  
**Risk**: High (executes anything)  
**Status**: Always ask

### Delete
**Purpose**: Remove files  
**Risk**: Critical (irreversible)  
**Status**: Always ask, high confirmation

### Grep
**Purpose**: Search code  
**Risk**: Low (read-only)  
**Status**: Always allowed (no prompt)

### Glob
**Purpose**: List files  
**Risk**: Low (no content access)  
**Status**: Always allowed (no prompt)

## Common Workflows

### Careful Review (Safest)

```
1. Ask Claude to show changes
2. Review each change
3. Approve individually [A]
4. Check git diff before pushing
```

### Session Approval (Balanced)

```
1. Understand the task
2. First action: Review carefully [D]
3. Approve for session [S]
4. Subsequent actions auto-approved
5. Still review final result
```

### Automated (Fastest, Requires Trust)

```
claude --dangerously-skip-permissions
# Use only for thoroughly reviewed scripts
```

## Troubleshooting Permissions

**"Permission denied for Bash"**

Option 1: Approve when prompted
```
[A] to allow this command
```

Option 2: Pre-approve in skill
```yaml
allowed-tools: Bash
```

Option 3: Add to settings
```json
"Bash": "allow"
```

**"Too many permission prompts"**

Option 1: Session approval
```
[S] Approve all this session
```

Option 2: Pre-approve in skill
```yaml
allowed-tools: Read Write Bash
```

**"Want to deny a tool completely"**

In `.claude/settings.json`:
```json
"Bash": "deny"
```

Now Claude cannot run commands.

## Environment Variables

```bash
# Skip all permissions
CLAUDE_CODE_SKIP_PERMISSIONS=1

# Auto-approve writes
CLAUDE_CODE_AUTO_APPROVE_WRITES=1

# Enable debug
CLAUDE_DEBUG=1
```

## Enterprise Permissions

Administrators can enforce permissions organization-wide:

```json
{
  "enforced_permissions": {
    "Delete": "deny",
    "Bash": "ask",
    "external_api_calls": "deny"
  }
}
```

## Principles

✅ **Do:**
- Review changes carefully
- Use session approval after first review
- Trust but verify
- Commit frequently so changes are trackable
- Use git before pushing

❌ **Don't:**
- Use `--dangerously-skip-permissions` carelessly
- Share credentials or secrets
- Approve blindly
- Skip git review
- Run unreviewed commands from untrusted sources

## Related Topics

- See `cli-basics.md` for permission CLI options
- See `best-practices.md` for security tips
- See `troubleshooting.md` for permission issues

---

Remember: Permissions exist to protect you. Use them wisely!
