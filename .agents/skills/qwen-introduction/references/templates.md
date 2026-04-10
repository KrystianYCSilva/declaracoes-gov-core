---
name: templates
description: |
  Annotated templates for Qwen agents and prompts. Use when: creating new agents or prompts for Qwen CLI.
---

# Qwen Code - Templates

## Skill Template

```
.qwen/skills/my-skill/
├── SKILL.md
└── reference.md (optional)
```

**SKILL.md**:

```yaml
---
name: my-skill-name
description: Clear description of what it does and when to use it. Include trigger keywords.
---

# Skill Name

## Instructions
Provide clear, step-by-step guidance for Qwen Code.

## Examples
Show concrete examples of using this Skill.

## Scripts (if applicable)
Run the helper script:
```bash
python scripts/helper.py input.txt
```
```

## SubAgent Template

**File**: `.qwen/agents/testing-specialist.md`

```yaml
---
name: testing-specialist
description: Writes comprehensive unit tests, integration tests, E2E tests, and handles test automation with best practices. Use PROACTIVELY when creating tests, reviewing test coverage, or fixing failing tests.
tools:
  - read_file
  - write_file
  - read_many_files
  - run_shell_command
---
You are a testing specialist focused on creating high-quality, maintainable tests for ${project_name}.

Your expertise includes:
- Unit testing, integration testing, E2E testing
- Test-Driven Development (TDD)
- Edge case identification and boundary testing
- Mock and stub best practices

Always:
1. Detect language and testing framework used in the project
2. Follow project's existing test patterns and conventions
3. Include positive, negative, and edge cases
4. Use descriptive test names (should_do_something pattern)
5. Add assertions with meaningful messages
6. Mock external dependencies appropriately

Output format:
- Test file in same directory as source file
- Test file naming: [source-name].test.[extension]
- Follow project conventions if they exist
- Include setup/teardown if needed
- Group related tests with describe blocks
```

## Prompt Template: Feature Request

```
Create [feature description].

Context:
- Project type: [language/framework]
- Purpose: [what it should do]
- Existing patterns: [follow X pattern from Y directory]

Requirements:
- [Requirement 1]
- [Requirement 2]

Constraints:
- [Constraint 1]
- [Constraint 2]

Output:
- [What files/components to create]
- [Any specific structure to follow]
```

## Prompt Template: Bug Fix Request

```
Fix this error:
[Error message and stack trace]

Context:
- When it happens: [describe scenario]
- Relevant file: [file path and line]
- Recent changes: [what changed before error appeared]

Expected behavior:
[What should happen instead]
```

## Validation Checklist

Before committing a new Skill or Agent:

- [ ] Name is lowercase with hyphens (no spaces or capitals)
- [ ] Description is specific and includes trigger keywords
- [ ] YAML syntax is valid (no tabs, correct indentation)
- [ ] SKILL.md or Agent file starts with `---` on line 1
- [ ] Instructions are clear and actionable
- [ ] Examples are provided (for Skills)
- [ ] Tools array only includes necessary tools
- [ ] System prompt defines clear expertise (for Agents)
- [ ] File is in correct location (`.qwen/skills/` or `.qwen/agents/`)
- [ ] Tested with a relevant request to verify it activates
