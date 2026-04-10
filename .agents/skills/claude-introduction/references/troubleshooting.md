---
name: troubleshooting
description: |
  Troubleshooting guide for common Claude Code installation and runtime issues.
  Use when: diagnosing installation failures, PATH issues, or other Claude Code setup problems.
---

# Troubleshooting

Debug common issues and find solutions.

## Installation Issues

### "claude: command not found"

**Problem**: Claude Code not installed or not in PATH

**Solutions**:

1. Verify installation:
```bash
curl https://claude.sh | bash
```

2. Check PATH:
```bash
echo $PATH
which claude
```

3. Restart terminal/shell

4. Manual installation:
```bash
uv tool install claude
```

### "Permission denied"

**Problem**: Installation script not executable

**Solution**:
```bash
chmod +x ~/claude
# Or reinstall:
curl https://claude.sh | bash
```

## Session Issues

### "Claude doesn't understand my code"

**Problem**: Claude lacks context

**Solutions**:
1. Point to similar files:
   ```
   See how authentication is done in src/auth.ts
   ```

2. Share architecture docs:
   ```
   Here's our tech stack:
   - React 18, TypeScript, Tailwind
   - Django backend, PostgreSQL
   - REST API at /api/v1/
   ```

3. Explain constraints:
   ```
   We use ESM imports, strict TypeScript, Jest tests
   ```

4. Share project README

### "Session taking too long"

**Problem**: Large context or slow model

**Solutions**:
1. Use compact mode:
   ```bash
   /compact
   ```

2. Limit files in scope:
   ```
   Only focus on src/components/Form.tsx
   ```

3. Create focused git branch:
   ```bash
   git checkout -b feature/auth
   ```

4. Split into smaller tasks

### "Session disconnected"

**Problem**: Connection lost or timeout

**Solutions**:
1. Check internet connection
2. Start new session:
   ```bash
   claude
   ```
3. Use `--timeout` flag:
   ```bash
   claude --timeout 600
   ```

## Git Issues

### "Git context not loading"

**Problem**: Claude doesn't see git history

**Solutions**:

1. Verify git repo:
   ```bash
   git status
   ls -la .git/
   ```

2. Ensure commits exist:
   ```bash
   git log --oneline
   ```

3. If needed, disable git:
   ```bash
   claude --no-git
   ```

### "/batch failing with git errors"

**Problem**: Worktree creation failed

**Solutions**:
1. Clean working directory:
   ```bash
   git status
   git add .
   git commit -m "checkpoint"
   ```

2. Free disk space (worktrees need room)

3. Check `.git` directory:
   ```bash
   git fsck --full
   ```

### "Merge conflicts in batch PRs"

**Problem**: Parallel agents created conflicting changes

**Solutions**:
1. Resolve manually:
   ```bash
   git diff
   # Edit conflicts manually
   git add .
   git commit -m "resolve conflicts"
   ```

2. Review one PR at a time

3. Smaller batch scopes

## Permission Issues

### "Permission denied for Bash"

**Problem**: Claude needs to run command but isn't approved

**Solutions**:
1. Approve when prompted:
   ```
   [A] Yes
   ```

2. Approve for session:
   ```
   [S] Session approval
   ```

3. Pre-approve in skill:
   ```yaml
   allowed-tools: Bash
   ```

4. Add to settings:
   ```json
   "Bash": "allow"
   ```

### "Too many permission prompts"

**Problem**: Approving each action one-by-one

**Solutions**:
1. Use session approval:
   ```
   [S] Approve all this session
   ```

2. Pre-approve in skill frontmatter

3. Use settings.json

### "Want to deny all deletions"

**Problem**: Too dangerous to allow deletes

**Solution** (in `.claude/settings.json`):
```json
{
  "permissions": {
    "Delete": "deny"
  }
}
```

## Skill Issues

### "Skill not auto-triggering"

**Problem**: Claude doesn't use the skill

**Solutions**:
1. Check description is specific:
   ```yaml
   # Bad
   description: Does stuff
   
   # Good
   description: Review code for quality and security issues
   ```

2. Verify skill appears:
   ```
   /help skills
   ```

3. Try invoking directly:
   ```
   /my-skill
   ```

4. Reword description to match your request

### "Skill showing permission errors"

**Problem**: Skill blocked from using needed tools

**Solutions**:
1. Add `allowed-tools`:
   ```yaml
   allowed-tools: Read Write Bash
   ```

2. Or approve each time when prompted

3. Check settings don't block tools:
   ```json
   "Bash": "deny"
   ```

### "Skill not found"

**Problem**: Skill.md missing or not in right place

**Solutions**:
1. Verify file exists:
   ```bash
   ls -la ~/.claude/skills/my-skill/SKILL.md
   # or
   ls -la .claude/skills/my-skill/SKILL.md
   ```

2. Check YAML syntax:
   ```yaml
   ---
   name: my-skill
   ---
   ```

3. Restart Claude Code session

## Performance Issues

### "Claude is slow"

**Solutions**:
1. Use `/compact` mode
2. Reduce context (fewer files open)
3. Create focused git branch
4. Close IDE tabs with large files
5. Use skills to avoid re-explaining

### "Running out of context"

**Problem**: Too many files or conversation

**Solutions**:
1. Start new session
2. Archive old conversation
3. Use `/compact` mode
4. Limit files in scope
5. Remove unnecessary files from `.claude/`

### "High latency responses"

**Problem**: Slow API or model

**Solutions**:
1. Check internet speed
2. Try different time (less load)
3. Use `/loop` with longer intervals
4. Split work into smaller tasks

## Output Issues

### "Output is unreadable"

**Problem**: Too verbose output

**Solutions**:
1. Use compact mode:
   ```bash
   /compact
   ```

2. Use plain text:
   ```bash
   claude --plain
   ```

3. Disable colors:
   ```bash
   claude --no-color
   ```

### "Missing information in output"

**Problem**: Claude skipping details

**Solutions**:
1. Ask explicitly:
   ```
   Show me the complete diff
   ```

2. Disable compact mode:
   ```
   /compact  (toggle off)
   ```

3. Increase context

## Debug Mode

### Enable debugging

```bash
# Start with debug
claude --debug

# Enable mid-session
/debug

# Debug specific issue
/debug git context not loading
```

### What Debug Shows

- Internal Claude decisions
- Tool execution results
- Permission checks
- Error details
- Context information

### Check Logs

```bash
# View logs (if configured)
cat ~/.claude/logs/debug.log

# Or pipe directly to file:
2> debug.log
```

## Common Issues by Symptom

### "Claude making wrong changes"

**Causes:**
- Unclear instructions
- Missing context
- Wrong permission approval

**Fix:**
1. Be more specific
2. Share relevant files
3. Review before approving
4. Use test-first approach

### "Tests failing after Claude's changes"

**Causes:**
- Edge cases not considered
- Integration issues
- Missing imports

**Fix:**
1. Ask Claude to review failed tests
2. Provide more context
3. Use `/simplify` to review changes
4. Run tests more frequently

### "Claude exceeding my instructions"

**Causes:**
- Vague instructions
- Claude inferring more features
- Misunderstood requirements

**Fix:**
1. Be very specific
2. Say "only implement X"
3. Start with minimal feature
4. Review changes carefully

### "Can't get Claude to understand"

**Causes:**
- Complex domain knowledge needed
- Poor context sharing
- Asking wrong questions

**Fix:**
1. Start simple: "How does this work?"
2. Share architecture docs
3. Point to similar code
4. Explain from first principles

## Getting Help

### In Session
```bash
/help
/help skills
/help [skill-name]
```

### Official Resources
- **Docs**: https://code.claude.com/docs
- **GitHub Issues**: Report bugs
- **Discord**: Community support

### Report a Bug
1. Note exact error message
2. Describe steps to reproduce
3. Include environment info:
   ```bash
   claude --version
   git --version
   node --version
   ```
4. Open issue on GitHub

## Emergency Procedures

### "Accidentally deleted important files"

1. Check git:
```bash
git log --all -p --full-history -- deleted-file
git checkout <commit>^ -- deleted-file
```

2. If not in git, file recovery may not be possible

3. Lesson: Always commit before major operations

### "Infinite loop in /loop"

Stop it:
```bash
Ctrl+C
```

Then restart session:
```bash
claude
```

### "Session completely stuck"

Force quit:
```bash
Ctrl+C (multiple times)
# or
killall claude
```

Restart:
```bash
claude
```

## Prevention Tips

✅ **Do:**
- Commit frequently
- Review changes before approving
- Use `/compact` for large codebases
- Test incrementally
- Keep CLAUDE.md updated
- Document custom agents

❌ **Don't:**
- Use `--dangerously-skip-permissions` carelessly
- Share credentials or secrets
- Approve large changes without review
- Leave `/loop` running unattended
- Ignore git status warnings

---

**Still stuck?** Check `sources.md` for official documentation links.