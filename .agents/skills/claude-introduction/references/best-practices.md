---
name: best-practices
description: |
  Best practices for effective prompting, incremental workflows, and review discipline with Claude Code.
  Use when: advising on how to write good Claude prompts or structure Claude-assisted development.
---

# Best Practices

Effective prompting, workflows, and tips.

## Core Principles

### 1. Be Specific
✅ **Good**: "Add form validation with email and required field checks"  
❌ **Bad**: "Add validation"

### 2. Provide Context
✅ **Good**: Share relevant files and architecture docs  
❌ **Bad**: Assume Claude knows your project

### 3. Set Goals Clearly
✅ **Good**: "Refactor to improve readability and performance"  
❌ **Bad**: "Make it better"

### 4. Review Changes
✅ **Good**: Always check Claude's work before accepting  
❌ **Bad**: Approve blindly

### 5. Iterate Incrementally
✅ **Good**: One feature at a time  
❌ **Bad**: "Build everything at once"

## Effective Prompting

### Structure

**Good prompt structure:**
```
1. What do you want?
2. Why (context)?
3. Any constraints?
4. Success criteria?
```

### Examples

**Bad Prompt**:
```
Make this code better
```

**Good Prompt**:
```
Refactor this authentication module to:
- Extract validation logic into separate file
- Add proper error handling with try/catch
- Add JSDoc comments for all functions
- Follow the existing code style (see src/utils/validation.ts for reference)
```

**Bad Prompt**:
```
Add tests
```

**Good Prompt**:
```
Add unit tests for the calculatePrice function:
- Test valid inputs (various price ranges)
- Test edge cases (0, negative numbers)
- Test null/undefined inputs
- Aim for 100% coverage
Use Jest with describe/it blocks like existing tests in tests/utils/
```

## Sharing Context

### Share Relevant Files

```
I want to add a settings dialog to my React app.
Here's the current structure:
- src/components/Dialog.tsx (base component)
- src/pages/Settings.tsx (where it goes)
- src/types/Settings.ts (data types)
```

### Point to Examples

```
Add validation similar to the approach in src/forms/LoginForm.tsx
```

### Explain Constraints

```
This project uses:
- Vite (not webpack)
- React 18
- TypeScript strict mode
- Tailwind CSS (no CSS modules)
- Jest for tests
```

## Workflow Patterns

### Pattern 1: Exploration First

```
1. "How does authentication work in this codebase?"
2. Claude explains current approach
3. "Now add OAuth support following the same pattern"
```

### Pattern 2: Test-First Development

```
1. "Write tests for the new signup flow"
2. Review tests, ask for changes
3. "Now implement the feature to pass these tests"
```

### Pattern 3: Refactor with Safety Net

```
1. "Write comprehensive tests for this module"
2. Verify tests pass
3. "Now refactor for performance"
4. Verify tests still pass
```

### Pattern 4: Parallel Exploration

```
/batch explore different implementations of user authentication
```

Creates parallel experiments for comparison.

## Performance Optimization

### Limit Context
```
✅ Share only relevant files
❌ Share entire codebase
```

### Use Compact Mode
```bash
/compact
```
Faster feedback, less verbose output.

### Leverage Git
```bash
# Commit frequently so Claude can use history
git commit -m "Add login feature"

# Claude will see recent commits for context
```

### Create Skills for Repeated Tasks
```bash
# Instead of re-explaining, create skill
mkdir -p ~/.claude/skills/code-review
# Then: /code-review
```

### Batch Similar Tasks
```
✅ "Fix all validation errors"
❌ "Fix validation error 1", then "Fix validation error 2"
```

## Coding Standards

### When Reviewing Code

Ask Claude to:
- Follow existing patterns in the codebase
- Match indentation and naming conventions
- Use same libraries/frameworks already in use

```
Add validation similar to existing approach in src/forms/LoginForm.tsx
Match the code style of src/components/Form.tsx
```

### Consistency is Key

```
Before refactoring:
- Read existing error handling patterns
- Check existing component structure
- Review naming conventions

Then implement with same patterns
```

## Security Practices

### Never Share Credentials

```
❌ Paste API keys
❌ Share passwords
❌ Include .env files
❌ Expose private data
```

### Always Review Operations

```
✅ Review git diff before push
✅ Review file changes before approving
✅ Verify API calls go to correct endpoint
✅ Check error handling is robust
```

### Use Permissions Carefully

```bash
❌ claude --dangerously-skip-permissions  (unless trusted)
✅ Review each change and approve [A]
✅ Use session approval [S] after first review
```

## Collaboration Tips

### For Teams

1. **Share skills** in `.claude/skills/`
2. **Document conventions** in `.claude/prompts/`
3. **Create custom agents** for specialized work
4. **Commit guidelines** to git for consistency

### For Code Review

```
Create a code-review skill:
mkdir -p .claude/skills/code-review
# Then team can: /code-review
```

## Advanced Techniques

### Dynamic Context

Use real data in skills:

```yaml
---
name: pr-review
---

## PR Info
- Files: !`gh pr diff --name-only`
- Diff: !`gh pr diff`

Review for quality and security...
```

### Parallel Work

```bash
/batch migrate from Solid to React
# Spawns parallel agents for speed
```

### Monitoring

```bash
/loop 5m check if deployment finished
/loop 10m verify tests still pass
```

### Iterative Improvement

```
1. "Implement basic version"
2. Review and discuss
3. "Now optimize for performance"
4. Review results
5. "Add error handling"
```

## Anti-Patterns

### ❌ Anti-Pattern 1: Vague Instructions
```
"Make this better"
"Add some features"
"Improve the code"
```
→ Claude doesn't know what you want

### ❌ Anti-Pattern 2: No Context
```
"Write tests for this file"
# But file references 10 dependencies Claude doesn't understand
```
→ Tests might not make sense

### ❌ Anti-Pattern 3: No Review
```
Approve all changes without looking
Use --dangerously-skip-permissions
```
→ Bugs and security issues

### ❌ Anti-Pattern 4: Task Switching
```
Start feature, mid-way: "Actually, let's do this instead"
Mid-way: "Wait, I changed my mind again"
```
→ Wastes time and context

### ❌ Anti-Pattern 5: Expecting Perfection
```
"Write production-ready code" (without review)
```
→ Always review and iterate

## Checklists

### Before Asking Claude

- [ ] Clear goal in mind?
- [ ] Shared relevant context?
- [ ] Explained constraints?
- [ ] Shown examples?

### Before Approving Changes

- [ ] Reviewed the diff?
- [ ] Tests pass?
- [ ] Follows project style?
- [ ] No security issues?
- [ ] Comments and documentation clear?

### Before Pushing to Main

- [ ] Code reviewed?
- [ ] All tests pass?
- [ ] No sensitive data included?
- [ ] Commit message clear?
- [ ] Related files updated (docs, etc)?

---

**Golden Rule**: Specific, contextual prompts with careful review yield the best results.