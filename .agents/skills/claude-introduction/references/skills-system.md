---
name: skills-system
description: |
  Reference for the Claude Code skills system — structure, discovery locations, and invocation via /skill-name.
  Use when: creating, configuring, or explaining Claude Code skills.
---

# Skills System

Creating, configuring, and managing reusable skills.

## What is a Skill?

A **skill** is a directory containing:
- `SKILL.md` — Main instructions (required)
- Supporting files — Templates, examples, scripts, docs (optional)

When you invoke `/skill-name`, Claude receives instructions on how to complete that task.

## Skill Locations

Skills are loaded from three levels (in priority order):

```
Enterprise (admin-managed)  ← Enterprise settings
       ↓
~/.claude/skills/           ← Personal (all projects)
       ↓
.claude/skills/             ← Project-specific
```

Higher priority overrides lower priority.

## Minimal Skill

Smallest possible skill:

```
my-skill/
└── SKILL.md
```

**SKILL.md:**
```yaml
---
name: my-skill
description: What this skill does
---

Your instructions here.
```

## Full-Featured Skill

Complete skill structure:

```
my-skill/
├── SKILL.md                 # Required: main instructions
├── references/
│   └── detailed-guide.md    # Optional: detailed docs
├── examples/
│   └── example-output.md    # Optional: usage examples
├── templates/
│   └── template.md          # Optional: templates for Claude to fill
└── scripts/
    ├── validate.sh
    └── helper.py
```

## SKILL.md Frontmatter

All available configuration fields:

```yaml
---
name: my-skill                      # Command name (lowercase, max 64 chars)
description: What it does           # Auto-load trigger (max 250 chars)
argument-hint: "[arg1] [arg2]"     # Autocomplete hint
disable-model-invocation: false     # true = user-only
user-invocable: true                # false = Claude-only
allowed-tools: Read Grep Bash       # Pre-approved tools
model: claude-opus-4-6              # Model override
effort: medium                      # low/medium/high/max
context: fork                       # "fork" = isolated subagent
agent: Explore                      # Agent type if fork
paths: "src/**/*.ts,tests/**"       # Auto-activate globs
---
```

## Frontmatter Field Reference

| Field | Type | Default | Notes |
|-------|------|---------|-------|
| `name` | string | dir name | For `/name` command |
| `description` | string | first paragraph | Helps Claude decide when to load |
| `argument-hint` | string | none | Shown in autocomplete |
| `disable-model-invocation` | boolean | false | Prevents auto-loading |
| `user-invocable` | boolean | true | Hides from menu if false |
| `allowed-tools` | string/list | none | Pre-approve tools |
| `model` | string | session default | Which model to use |
| `effort` | string | session default | Effort level |
| `context` | string | inline | "fork" for subagent |
| `agent` | string | general-purpose | Agent type if fork |
| `paths` | string/list | none | Glob patterns for auto-activation |

## Creating Your First Skill

### Step 1: Create Directory
```bash
# Personal skill
mkdir -p ~/.claude/skills/my-skill

# Project skill
mkdir -p .claude/skills/my-skill
```

### Step 2: Write SKILL.md
```yaml
---
name: code-review
description: Review code for quality, security, and best practices
disable-model-invocation: true
---

Review the code for:

1. **Code Quality**
   - Readability and maintainability
   - Following project style guide
   - Proper error handling

2. **Best Practices**
   - Design patterns
   - Performance considerations
   - Security vulnerabilities

3. **Testing**
   - Adequate test coverage
   - Edge cases handled
   - Test quality
```

### Step 3: Test It
```bash
# Invoke directly
/code-review

# Or ask Claude naturally
# "Please review this code"
```

## Skill Patterns

### Pattern 1: Task Automation

For workflows you control timing of:

```yaml
---
name: deploy
description: Deploy application to production
disable-model-invocation: true
allowed-tools: Bash Read Write
---

Deploy to production:

1. Run test suite
2. Build application
3. Push to deployment target
4. Verify deployment
```

Usage: `You'll invoke manually with /deploy`

### Pattern 2: Background Knowledge

Reference material Claude loads automatically:

```yaml
---
name: api-conventions
description: API design conventions for this codebase
user-invocable: false
---

When writing API endpoints:

- Use RESTful naming conventions
- Return consistent error formats
- Include request validation
- Document with OpenAPI
```

Usage: `Claude auto-loads when relevant`

### Pattern 3: Subagent Delegation

Complex work in isolated context:

```yaml
---
name: deep-analysis
description: Thoroughly analyze code for improvements
context: fork
agent: Explore
allowed-tools: Read Grep Glob
---

Analyze $ARGUMENTS thoroughly:

1. Explore structure and patterns
2. Identify improvement opportunities
3. Suggest specific fixes
4. Explain rationale
```

Usage: `/deep-analysis src/components`

### Pattern 4: Dynamic Context

Inject real data from shell commands:

```yaml
---
name: pr-review
description: Review pull request
context: fork
allowed-tools: Bash(gh *)
---

## PR Information
- **Title**: !`gh pr view --json title -q .title`
- **Files**: !`gh pr diff --name-only`
- **Diff**: !`gh pr diff`

Review this PR for:
- Code quality
- Best practices
- Security issues
```

## Passing Arguments

### $ARGUMENTS Placeholder

```yaml
---
name: fix-issue
description: Fix a GitHub issue
---

Fix GitHub issue $ARGUMENTS:

1. Read the issue description
2. Understand requirements
3. Implement fix
4. Write tests
```

Usage: `/fix-issue 123` → "Fix GitHub issue 123: ..."

### Individual Arguments

```yaml
---
name: migrate-component
---

Migrate the $0 component from $1 to $2.
```

Usage: `/migrate-component Button React Vue`

## String Substitutions

```yaml
---
name: logger
---

# Session ID
Log to session_${CLAUDE_SESSION_ID}.log

# Skill directory
python ${CLAUDE_SKILL_DIR}/scripts/validate.py
```

## Tool Access Control

Restrict tools available to Claude:

```yaml
---
name: safe-reader
description: Read files safely without modifying
allowed-tools: Read Grep Glob
---

# Claude can only read/search, not modify
```

## Common Examples

### Code Review Skill
```yaml
---
name: code-review
description: Review code for quality and issues
---

Review for:
- Readability and maintainability
- Following conventions
- Proper error handling
- Security issues
```

### Documentation Skill
```yaml
---
name: generate-docs
description: Generate API documentation
argument-hint: "[directory]"
---

Generate docs for $ARGUMENTS:
1. Find exported functions/classes
2. Document each export
3. Create examples
4. Validate completeness
```

### Testing Skill
```yaml
---
name: add-tests
description: Add tests for code
argument-hint: "[file-or-module]"
---

Add tests for $ARGUMENTS:
1. Understand functionality
2. Write unit tests
3. Add integration tests
4. Verify coverage
```

## Sharing Skills

### With Team Members

1. Copy skill directory:
```bash
cp -r ~/.claude/skills/my-skill ~/Downloads/
```

2. Share via email, cloud storage, or GitHub

3. Recipient installs:
```bash
cp -r ~/Downloads/my-skill ~/.claude/skills/
```

### Via GitHub Repository

1. Create `.claude-skills` repo
2. Organize skills in `skills/` directory
3. Document in README
4. Share repo URL with team

### Enterprise-Wide

Contact administrator to deploy via managed settings.

## Best Practices

✅ **Do:**
- Make descriptions specific and keyword-rich
- Start simple, evolve with use
- Test before sharing
- Document supporting files
- Use `/help skills` to verify setup

❌ **Don't:**
- Use vague descriptions
- Over-engineer first version
- Assume Claude understands without context
- Put credentials in skills
- Share without testing

## Troubleshooting

**Skill not auto-triggering?**
- Check description is specific
- Use `/help skills` to see it listed
- Try invoking with `/skill-name`
- Reword description to match request

**Permission denied?**
- Add `allowed-tools: Bash Write` to skill
- Or approve when prompted
- Or use `--dangerously-skip-permissions`

**Slow performance?**
- Limit scope of operation
- Use `/compact` mode
- Create focused files
- Pre-approve tools

## Next Steps

- Read `agents-system.md` to delegate to agents
- Read `best-practices.md` for effective prompting
- Read `bundled-skills.md` for built-in skills
- Check `sources.md` for official documentation