---
name: markdown-expert
description: |
  Guide writing technical documentation and reports in Markdown with portable structure and diagram-aware formatting.
  Use when: writing READMEs, technical reports, RFCs, or Markdown documents that must render cleanly across common tooling.
activation: Auto
estimated_tokens: 510
---

# Markdown Expert

Prefer portable Markdown first, then add renderer-specific features only when they materially improve the output.

Activate when producing any Markdown document that must render cleanly across tooling: READMEs, RFCs, technical reports, changelogs, or documentation sites. Do not activate for rich text editors, HTML-first authoring, or non-Markdown output formats.

## How to Structure a Markdown Document

Keep the document easy to scan:

1. one clear H1
2. ordered heading hierarchy
3. short paragraphs
4. flat lists where possible
5. fenced code blocks with info strings

Use tables only when comparison or matrix reading is genuinely improved by them.

## How to Write Technical Content That Survives Rendering Differences

Prefer CommonMark and GitHub Flavored Markdown features that are broadly stable.
Avoid relying on raw HTML for normal structure unless the renderer requires it.

For syntax edge cases and portability notes, read `references/syntax.md`.

## How to Embed Diagrams and Richer Blocks

Use Mermaid or callouts only when the renderer supports them.
Keep the surrounding prose understandable even if the diagram does not render.

For diagram-specific guidance, read `references/diagrams.md`.
For tooling and conversion tradeoffs, read `references/tools.md`.

## How to Stay Accurate

- prefer the official sources in `references/sources.md` when syntax behavior is uncertain
- load deeper references only for the specific topic under discussion

## How to Review a Markdown Draft

Check:

1. heading order
2. link correctness
3. code fence language hints
4. portability of special syntax
5. whether the structure helps scanning instead of fighting it
