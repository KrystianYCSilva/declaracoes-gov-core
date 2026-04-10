---
name: cli-reference
description: |
  Machine-agnostic reference for terminal-executable LLM CLIs used in chat-driven delegation.
  Includes authorization-aware invocation patterns for Windows (PowerShell) and Linux (Bash).
  Use when: building CLI invocation commands, selecting a delegation CLI/model tier, or confirming whether a tool supports headless terminal workflows.
---

# CLI Reference - Multi-LLM Delegation (Windows/Linux)

> Last verified against official docs: 2026-02-27.
> This file is environment-agnostic by design. Do not hardcode local binary paths.
> For canonical vendor links, read `official-cli-docs.md`.

---

## Authorization-First Baseline

Before running any CLI delegation command:

1. obtain explicit developer approval in chat
2. confirm target CLI, model tier, and file scope
3. confirm whether the run is read-only, patching, or broader execution
4. prefer the safest permission mode supported by the tool

If vendor docs show automation shortcuts or safety-bypass examples, do not import them into Itzamna-managed workflows.

---

## Headless Delegation CLIs

### GitHub Copilot CLI (`copilot`)

Headless pattern:

```powershell
$prompt = "Implement service method from CARD-010"
$output = copilot -p $prompt --model "<model-id>" -s
```

```bash
prompt="Implement service method from CARD-010"
output=$(copilot -p "$prompt" --model "<model-id>" -s)
```

Notes:
- Supports `--agent <name>` for predefined sub-agents.
- Model availability is dynamic; verify via current CLI/docs.

---

### Gemini CLI (`gemini`)

Headless pattern:

```powershell
$prompt = "Review this patch for auth bypass risk"
$output = gemini -p $prompt -m "<model-id>" --output-format text
```

```bash
prompt="Review this patch for auth bypass risk"
output=$(gemini -p "$prompt" -m "<model-id>" --output-format text)
```

Notes:
- Prefer explicit `--output-format` when parsing output.

---

### Claude Code CLI (`claude`)

Headless pattern:

```powershell
$prompt = "Audit this diff for race conditions"
$output = claude -p $prompt --model sonnet --output-format text
```

```bash
prompt="Audit this diff for race conditions"
output=$(claude -p "$prompt" --model sonnet --output-format text)
```

Notes:
- Keep permission settings explicit and aligned with the current governance toolchain.

---

### OpenAI Codex CLI (`codex`)

Headless pattern:

```powershell
$prompt = "Generate tests for UserService"
$output = codex exec $prompt -m "<model-id>"
```

```bash
prompt="Generate tests for UserService"
output=$(codex exec "$prompt" -m "<model-id>")
```

Notes:
- `codex exec` is the non-interactive entrypoint.

---

### OpenCode CLI (`opencode`)

Headless pattern:

```powershell
$prompt = "Implement DTOs and mappers"
$output = opencode run $prompt -m "<provider/model>"
```

```bash
prompt="Implement DTOs and mappers"
output=$(opencode run "$prompt" -m "<provider/model>")
```

Notes:
- Multi-provider format typically uses `<provider>/<model>`.

---

### Qwen Code CLI (`qwen`)

Headless pattern:

```powershell
$prompt = "Write integration tests for payment flow"
$output = qwen -p $prompt
```

```bash
prompt="Write integration tests for payment flow"
output=$(qwen -p "$prompt")
```

Notes:
- Official docs describe terminal usage and prompt mode.
- Optional autonomy features vary by release; confirm via current docs/help before documenting them.

---

### Cursor Agent CLI (`cursor-agent`)

Headless pattern:

```powershell
$prompt = "Refactor module while preserving public API"
$output = cursor-agent -p $prompt --model "<model-id>" --output-format text
```

```bash
prompt="Refactor module while preserving public API"
output=$(cursor-agent -p "$prompt" --model "<model-id>" --output-format text)
```

Notes:
- This is distinct from IDE-launch commands.
- Supports agentic coding flows via terminal.

---

### Mistral Vibe CLI (`vibe`)

Terminal pattern:

```powershell
$output = vibe "Refactor command handler into isolated units"
```

```bash
output=$(vibe "Refactor command handler into isolated units")
```

Notes:
- Official docs provide install/quickstart and agent routing.
- Confirm automation flags/output mode in current release before scripting.

---

## IDE-Integrated, Not Primary Headless Delegation

### Windsurf Command / Cascade

Windsurf documentation exposes command workflows inside the IDE context.
Treat it as IDE-integrated orchestration, not as a primary standalone headless CLI for scripted delegation.

---

## Quick Decision Matrix

| Task Type | Primary CLI Choices | Escalation Path |
|-----------|---------------------|-----------------|
| Architecture and planning | `claude`, `copilot` | Move to strongest model tier |
| Code review/security | `gemini`, `copilot`, `claude` | Add second-pass L2 review |
| Implementation (single file/function) | `qwen`, `copilot`, `codex` | Escalate model, keep same prompt context |
| Multi-file refactor | `cursor-agent`, `opencode`, `codex` | Split by file boundaries and parallelize safely |
| Cost-optimized drafts | `gemini` fast tier, `qwen`, `opencode` | Escalate only if failure repeats |

---

## Staleness Warning

CLI flags and model catalogs change frequently.
Always verify final syntax against the official links in `official-cli-docs.md` before updating governance or automation scripts.
