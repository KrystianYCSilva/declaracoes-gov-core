---
name: examples
description: |
  Practical Qwen CLI workflow examples. Use when: demonstrating Qwen usage patterns.
---

# Qwen Code - Usage Examples

## Example 1: Project Bootstrap and Exploration

```
User: /init

User: What does this project do? Summarize the architecture and tech stack.

Qwen Code: [Analyzes project structure, reads config files,
             examines key source files, and provides summary]

User: Create a diagram of the main components and their relationships.
```

## Example 2: Using a SubAgent

**Agent Configuration** (`.qwen/agents/testing-expert.md`):

```yaml
---
name: testing-expert
description: Writes comprehensive unit tests and handles test automation. Use PROACTIVELY when creating or reviewing tests.
tools:
  - read_file
  - write_file
  - read_many_files
  - run_shell_command
---
You are a testing specialist focused on creating high-quality, maintainable tests.
```

**Usage**:

```
User: Let the testing-expert create comprehensive unit tests for
      the payment processing module. Cover positive, negative,
      and edge cases.

[Qwen delegates to testing-expert agent]

Testing Expert: [Creates test files with comprehensive coverage]
```

**Automatic Delegation** (if description includes "PROACTIVELY"):

```
User: Create tests for the user registration module.

[Qwen automatically delegates to testing-expert based on description match]
```

## Example 3: Pipes and CLI Automation

```bash
# Watch logs and detect issues
tail -f app.log | qwen -p "Alert me if you see any errors or warnings"

# Analyze build output
mvn verify 2>&1 | qwen -p "Fix any build errors"

# Review git changes
git diff HEAD | qwen -p "Review these changes and suggest improvements"
```

## Tips for Better Results

**Provide context** -- include project type, relevant file paths, and existing patterns
when requesting features or fixes.

**Iterate and refine** -- follow up with additional requirements rather than accepting
the first attempt if it does not match expectations.

**Use the right tool**:
- `/init` for project understanding
- SubAgents for specialized tasks
- Skills for recurring patterns
- `-p` flag and pipes for automation
