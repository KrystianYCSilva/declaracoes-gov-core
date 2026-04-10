---
name: skills
description: |
  Reference for the Qwen Code skills system including skill structure, storage paths, creation steps, and distinction from slash commands.
  Use when: creating or managing Qwen Code skills.
---

# Qwen Code - Skills Reference

## What are Skills?

Skills are modular capabilities that extend the model's effectiveness, packaging technical knowledge into organized directories with instructions and (optionally) scripts/resources.

**Key difference from slash commands**:
- Slash commands: **User-triggered** (you type `/command`)
- Skills: **Model-invoked** (AI decides when to load based on request + description)

## Skill Structure

```
my-skill/
├── SKILL.md              # Required: YAML metadata + Markdown
├── reference.md          # Optional: Reference documentation
├── examples.md           # Optional: Usage examples
├── scripts/              # Optional: Helper scripts
│   └── helper.py
└── templates/            # Optional: Templates
    └── template.txt
```

## SKILL.md Format

**Required frontmatter**:
```yaml
---
name: skill-name
description: What it does + when to use it. Include trigger keywords.
---
```

**Fields**:
- `name`: Unique ID (lowercase, numbers, hyphens)
- `description`: Triggers auto-activation. Be specific with context keywords

**Body**: Markdown instructions, examples, workflows

## Storage Locations

| Location | Scope | Sharing |
|----------|-------|---------|
| `~/.qwen/skills/` | Personal | Individual use |
| `.qwen/skills/` | Project | Shared via Git |
| Extension `skills/` | Extension | Automatic with install |

## Creating Skills

### Steps
1. Create directory: `mkdir -p .qwen/skills/my-skill`
2. Create `SKILL.md` with frontmatter + instructions
3. Add helpers/scripts (optional)
4. Test with relevant requests
5. Share via Git (commit + push project skills)

### Example SKILL.md

```yaml
---
name: pdf-processor
description: Extract text/tables from PDFs, fill forms, merge documents. Use when working with PDFs, forms, document extraction.
---

# PDF Processor

## Instructions
1. Use PyPDF2 or pdfplumber for text extraction
2. Handle encrypted PDFs with password prompt
3. Preserve formatting when possible
4. Output to markdown

## Examples

Extract text:
```bash
python scripts/extract.py input.pdf output.md
```

Merge PDFs:
```bash
python scripts/merge.py file1.pdf file2.pdf output.pdf
```
```

## Using Skills

**Automatic**: Make natural request related to skill. AI detects relevance and applies it.

**Manual**: `/skills skill-name` (tab autocomplete available)

**Discovery**: Ask `What Skills are available?` or inspect filesystem.

## Best Practices

1. **Single Focus**: One skill = one capability (e.g., `pdf-form-filling` not `document-processing`)
2. **Specific Descriptions**: Include activation triggers explicitly
3. **Relative Paths**: Reference scripts/templates relative to skill directory
4. **Team Testing**: Validate skill activates correctly and instructions are unambiguous
5. **Reference Files**: Use `reference.md` for detailed docs, keep `SKILL.md` concise

## Debugging (Skill Not Activating)

1. **Vague Description**: Make specific with context keywords
2. **Wrong Path**: Verify `SKILL.md` is exactly in `~/.qwen/skills/<name>/SKILL.md` or `.qwen/skills/<name>/SKILL.md`
3. **Invalid YAML**: Ensure `---` on line 1 and before Markdown. No tabs, correct indentation
4. **Error Logs**: Run `qwen --debug` to see loading failures

## Updating/Removing

- **Update**: Edit `SKILL.md` directly
- **Remove**: `rm -rf .qwen/skills/my-skill`
- **Reload**: Restart Qwen Code if already running
