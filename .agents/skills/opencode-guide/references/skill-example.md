---
name: skill-example
description: |
  OpenCode skill definition format and validation rules. Use when: creating custom OpenCode skills.
---

# Skill Definition Examples

## Annotated Skill

Create `.opencode/skills/git-release/SKILL.md`:

```markdown
---
name: git-release
description: Create consistent releases and changelogs
license: MIT
compatibility: opencode
metadata:
  audience: maintainers
  workflow: github
---

## What I do

- Draft release notes from merged PRs
- Propose a version bump
- Provide a copy-pasteable `gh release create` command

## When to use me

Use this when you are preparing a tagged release.
Ask clarifying questions if the target versioning scheme is unclear.
```

## Frontmatter Validation

| Field | Required | Description |
|-------|----------|-------------|
| `name` | Yes | Skill identifier (1-64 chars, lowercase with hyphens) |
| `description` | Yes | What skill does (1-1024 chars) |
| `license` | No | License for skill content |
| `compatibility` | No | Target agent/product |
| `metadata` | No | Custom key-value data |

### Name Validation

```
^[a-z0-9]+(-[a-z0-9]+)*$
```

Valid: `my-skill`, `git-release`, `code-review`
Invalid: `MySkill`, `my_skill`, `my--skill`

## Skill Discovery Locations

| Location | Path |
|----------|------|
| Project | `.opencode/skills/<name>/SKILL.md` |
| Global | `~/.config/opencode/skills/<name>/SKILL.md` |
| Claude-compatible | `.claude/skills/<name>/SKILL.md` |
| Agent-compatible | `.agents/skills/<name>/SKILL.md` |

## Loading Skills

In conversation:

```
skill({ name: "git-release" })
```

OpenCode shows available skills in tool description:

```xml
<available_skills>
  <skill>
    <name>git-release</name>
    <description>Create consistent releases</description>
  </skill>
</available_skills>
```

## Skill Permissions

Control skill access in `opencode.json`:

```json
{
  "permission": {
    "skill": {
      "*": "allow",
      "internal-*": "deny",
      "experimental-*": "ask"
    }
  }
}
```

| Permission | Behavior |
|------------|----------|
| `allow` | Loads immediately |
| `deny` | Hidden from agent |
| `ask` | User prompted first |
