---
name: bundled-skills
description: |
  Reference for built-in skills bundled with Claude Code, including /batch, /debug, /loop, and /simplify.
  Use when: invoking or explaining built-in Claude Code skills.
---

# Bundled Skills

Powerful built-in skills that ship with Claude Code.

## Overview

Bundled skills are prompt-based tools that Claude Code includes. You invoke them with `/skill-name`.

| Skill | Purpose | Example |
|-------|---------|---------|
| `/batch` | Parallelize large refactors | `/batch migrate React to Vue` |
| `/debug` | Enable debug logging | `/debug [description]` |
| `/loop` | Run prompts on interval | `/loop 5m check deployment` |
| `/simplify` | Batch code review | `/simplify focus on performance` |
| `/claude-api` | Load API reference | Auto-loads when relevant |

## /batch

**Parallelize large changes across codebase.**

```bash
/batch <instruction>
```

### How It Works

1. Research codebase structure
2. Decompose into 5-30 independent units
3. Present plan for approval
4. Spawn parallel agents in git worktrees
5. Each agent: implement → test → open PR
6. You review and merge PRs

### Examples

```bash
# Migrate framework
/batch migrate src/ from React to Vue

# Add TypeScript
/batch add TypeScript strict mode to all files

# Convert components
/batch convert class components to functional

# Add feature across codebase
/batch add dark mode support everywhere
```

### Requirements

- Git repository
- Clean working tree (no uncommitted changes)
- Sufficient disk space for worktrees

### When to Use

- Large refactors affecting many files
- Framework migrations
- API contract changes
- Codebase-wide features

### When NOT to Use

- Single file changes
- Complex interdependencies
- Real-time coordination needed

## /debug

**Enable debug logging for troubleshooting.**

```bash
/debug [description]
```

### Usage

```bash
# Enable debug
/debug

# Debug specific issue
/debug git context not loading

# Check memory usage
/debug memory issues

# Permission handling
/debug permission denials
```

### What It Shows

- Internal Claude decisions
- Tool usage and results
- Permission checks
- Error details
- Context information

### Output

- **Terminal**: Shows directly
- **Log files**: `~/.claude/logs/` (if configured)

## /loop

**Run prompts repeatedly on interval.**

```bash
/loop [interval] <prompt>
```

### Examples

```bash
# Default interval (10 minutes)
/loop verify tests still pass

# Custom interval
/loop 5m check deployment status

# Check every hour
/loop 1h run health check

# Every 30 seconds
/loop 30s is API responding?
```

### Interval Format

- `5m` - 5 minutes
- `30s` - 30 seconds
- `1h` - 1 hour
- `2h 30m` - 2 hours 30 minutes

### When to Use

- Monitor deployments
- Poll for status changes
- Periodic health checks
- Wait for operations to complete

### When NOT to Use

- One-time tasks
- Tasks needing complex logic
- Real-time monitoring (use proper monitoring)

## /simplify

**Batch code review and refactoring.**

```bash
/simplify [focus]
```

### How It Works

1. Identifies recently changed files
2. Spawns 3 review agents in parallel
3. Each checks different aspects
4. Aggregates findings
5. Applies fixes automatically

### Examples

```bash
# General review
/simplify

# Focused review
/simplify focus on performance

/simplify focus on reducing duplication

/simplify focus on memory efficiency

/simplify focus on security issues
```

### What It Reviews

- Code reuse opportunities
- Quality issues
- Performance problems
- Security vulnerabilities
- Maintainability concerns

### Notes

- Only reviews recently changed files
- Tests changes before suggesting
- Shows explanations for fixes

## /claude-api

**Load Claude API reference material.**

```bash
/claude-api
```

### Auto-Triggers When

- Code imports `anthropic`
- Code imports `@anthropic-ai/sdk`
- Code imports `claude_agent_sdk`
- User asks about Claude API

### Covers

- API endpoints and methods
- SDK usage (Python, TypeScript)
- Tool use and function calling
- Streaming and async patterns
- Common pitfalls
- Best practices

### Languages Supported

- Python (Anthropic SDK)
- TypeScript (@anthropic-ai/sdk)
- Java
- Go
- Ruby
- C#
- PHP
- cURL

## Using Bundled Skills Together

### Example 1: Large Refactor with Review

```bash
# 1. Parallelize refactor
/batch convert to TypeScript strict mode

# 2. When PRs open, review each
# (Check each PR in GitHub)

# 3. After merging, optimize
/simplify focus on reducing any duplication introduced

# 4. Monitor tests
/loop 5m verify no regressions in tests
```

### Example 2: Feature Development

```bash
# 1. Plan work with batch
/batch add dark mode theme throughout app

# 2. Review quality
/simplify

# 3. Test thoroughly
/loop 5m run full test suite

# 4. Deploy and monitor
/loop 2m check if deployment is healthy
```

## Best Practices

✅ **Do:**
- Use `/batch` for large refactors
- Use `/simplify` after making changes
- Use `/loop` for monitoring
- Clean up with `/loop` when done (Ctrl+C)
- Review `/batch` PRs carefully before merging

❌ **Don't:**
- Use `/batch` for single-file changes
- Leave `/loop` running indefinitely
- Use `/simplify` on unreviewed code
- Run multiple `/batch` at same time
- Force-merge without reviewing

## Troubleshooting

**"/batch failing with git errors?"**
- Ensure no uncommitted changes
- Ensure sufficient disk space
- Check `.git/` directory exists
- Try: `git status`

**"/loop not running?"**
- Check session is still active
- Verify syntax: `/loop 5m command`
- Check command works manually first

**"/simplify finding nothing?"**
- Only works on recently changed files
- Must have modified files since session start
- Check: `git diff` shows changes

**"/debug producing too much output?"**
- Redirect to file: `2> debug.log`
- Filter for specific topic
- Check specific issue in question

## Advanced Patterns

### Monitoring Deployment

```bash
# Deploy
npm run deploy

# Monitor every 5 minutes
/loop 5m check if deployment is healthy

# When done, stop with Ctrl+C
```

### Parallel Refactor with Validation

```bash
# Start batch
/batch add error boundary to all components

# When all PRs are merged, validate
/simplify focus on error handling consistency

# Run tests periodically
/loop 10m verify tests passing
```

### Large Feature Development

```bash
# Day 1: Plan and parallelize
/batch add authentication to all pages

# Day 2: Quality pass
/simplify focus on security

# Day 3: Monitor
/loop 2m check for any production issues
```

---

Next: See `permissions.md` for permission model and safety.