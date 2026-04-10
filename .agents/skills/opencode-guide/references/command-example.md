---
name: command-example
description: |
  OpenCode command syntax, naming rules, and representative examples. Use when: creating custom OpenCode commands.
---

# Command Configuration Examples

## Command File Naming

- File name = command name (without `/`)
- `test.md` -> `/test`
- `run-coverage.md` -> `/run-coverage`

## Command Locations

- Global: `~/.config/opencode/commands/`
- Project: `.opencode/command/`

## Basic Command

Create `.opencode/command/test.md`:

```markdown
---
description: Run tests with coverage
agent: build
---

Run the full test suite with coverage report.
Show any failures and suggest fixes.
```

Usage: `/test`

## Command with Arguments

```markdown
---
description: Create a new module
agent: build
---

Create a new module named $ARGUMENTS.
Follow existing project conventions.
Include basic structure and exports.
```

Usage: `/module ReportService`

## Command with Shell Output

```markdown
---
description: Analyze test coverage
agent: build
---

Current test results:
!`mvn test`

Analyze coverage and suggest improvements.
```

Usage: `/coverage`

## Command with Git Integration

```markdown
---
description: Review recent changes
agent: plan
---

Recent commits:
!`git log --oneline -10`

Review changes and suggest improvements.
Focus on: code quality, potential bugs, documentation.
```

Usage: `/review-changes`

## Best Practices

1. **Descriptive names**: Use clear, actionable names
2. **Brief descriptions**: 1-2 sentences explaining purpose
3. **Right agent**: Use `explore` for analysis, `build` for changes
4. **Include context**: Reference files with `@`
5. **Show output**: Use `` !`command` `` to include shell results
