---
name: installation
description: |
  Installation and first-run guide for Kimi Code CLI on Linux, macOS, and Windows.
  Use when: setting up Kimi CLI for the first time or troubleshooting installation.
---

# Kimi CLI installation and first run

## Install

**Linux / macOS:**
```sh
curl -LsSf https://code.kimi.com/install.sh | bash
```

**Windows (PowerShell):**
```powershell
Invoke-RestMethod https://code.kimi.com/install.ps1 | Invoke-Expression
```

**via uv (Python 3.13+):**
```sh
uv tool install --python 3.13 kimi-cli
```

**Verify:**
```sh
kimi --version
```

## First-run authentication

1. Run `kimi` to start an interactive session.
2. Enter `/login` and choose "Kimi Code" as the provider.
3. Complete OAuth in the browser or enter an API key.

## First-run surfaces that matter

- interactive session: `kimi`
- headless: `kimi -p "<prompt>" --print`
- continue: `kimi --continue`
- custom agent: `kimi --agent-file <file>`

## Repository convention

- Keep `.kimi/AGENTS.md` as the repository-specific Kimi guide.
- Keep the root `AGENTS.md` as the shared cross-agent index.
- Keep project custom agents in `.kimi/agents/`.

## `/init` note

Public Kimi docs are thinner than Claude or Qwen on repository bootstrap. In this
repository, treat initialization as a local workflow:

- preserve the root `AGENTS.md`
- update `.kimi/AGENTS.md`
- keep custom agent files under `.kimi/agents/`
