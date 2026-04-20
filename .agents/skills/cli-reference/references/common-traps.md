# CLI Configuration Common Traps

## Trap 1: Assuming All CLIs Use the Same Context File
- **What happens**: You create `AGENTS.md` expecting all CLIs to read it, but Claude ignores it (reads `CLAUDE.md`)
- **Fix**: Create the CLI-specific file OR use `AGENTS.md` which most CLIs support as fallback
- **Safe bet**: Keep both `AGENTS.md` (cross-CLI) and CLI-specific file (if needed)

## Trap 2: Skills in Wrong Directory
- **What happens**: Skill exists but CLI doesn't discover it
- **Fix**: Check the exact path per CLI (see SKILL.md table). Note: `.opencode/skills/` ≠ `.claude/skills/`
- **Global fallback**: `~/.agents/skills/` is searched by most CLIs

## Trap 3: Expecting /init to Scaffold Everything
- **What happens**: Running `/init` and expecting a complete project setup
- **Reality**: `/init` generates a context file, not a project. Manual curation is where real control lives.
- **Cursor and Vibe have NO `/init`** — configure via rules/settings

## Trap 4: Subagent Recursion
- **What happens**: Designing hierarchical agent chains (agent calls agent calls agent)
- **Reality**: Claude Code subagents CANNOT call other subagents. Most CLIs enforce 1-level delegation.
- **Fix**: Design flat delegation: main agent → N parallel subagents, each returns results

## Trap 5: Context File Duplication Across CLIs
- **What happens**: Same rules in `CLAUDE.md`, `AGENTS.md`, `GEMINI.md` → drift when one is updated
- **Fix**: Single source of truth in `AGENTS.md`. CLI-specific files import or reference it.
- **OpenCode precedence**: `AGENTS.md` > `CLAUDE.md` — if both exist, AGENTS.md wins
