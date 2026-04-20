---
name: mcp-document-servers
description: |
  MCP servers for reading binary documents — safety-vetted options only.
  Use when: checking which MCP servers are installed or considering alternatives.
---

# MCP Servers for Document Reading

## Installed: @modelcontextprotocol/server-pdf (Node.js) ✅

**Status**: Installed globally and configured in `~/.github/copilot/mcp.json`

- **Source**: Official `modelcontextprotocol` org (Linux Foundation)
- **License**: MIT
- **Stars**: 83k+ (monorepo)
- **Formats**: PDF only (text extraction, chunked pagination, remote URLs)

### Configuration (already applied)

```json
{
  "mcpServers": {
    "pdf-reader": {
      "command": "npx",
      "args": ["-y", "@modelcontextprotocol/server-pdf", "--stdio"]
    }
  }
}
```

## For XLSX, DOCX, PPTX: use convert.py script

No MCP server for these formats passed our safety criteria (unknown authors, low trust scores).
Use the local `scripts/convert.py` instead — it uses well-known, audited Python libraries:

- `PyPDF2` (54M+ downloads)
- `openpyxl` (100M+ downloads)
- `python-docx` (50M+ downloads)
- `python-pptx` (20M+ downloads)

## Rejected packages (unsafe)

| Package | Reason |
|---------|--------|
| `mcp-file-contents-reader` | Trust score 55/100, author unknown, no license |
| `mcp-documents-reader` | Low adoption, unverified maintainer |

These may improve over time. Re-evaluate periodically.
