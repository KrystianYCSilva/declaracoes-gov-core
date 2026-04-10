---
name: syntax
description: |
  Advanced Markdown syntax reference: GFM vs CommonMark portability, tables, front matter, math, alerts, footnotes, and MDX basics.
  Use when: handling non-trivial syntax, checking cross-renderer portability, or using GitHub-specific or SSG-specific features.

---

# Advanced Markdown Syntax

## Tables (GFM)
Colons define alignment.

```markdown
| Left | Center | Right |
|:-----|:------:|------:|
| Cell | Cell   | Cell  |
```

## Task Lists
Interactive checklists in GitHub/GitLab.

```markdown
- [x] Completed task
- [ ] Pending task
```

## Math (LaTeX)
Supported by GitHub, GitLab, and Obsidian.

```markdown
$$
f(x) = \int_{-\infty}^\infty \hat f(\xi)\,e^{2\pi i \xi x} \,d\xi
$$
```

## Footnotes
Supported by GFM and many parsers.

```markdown
Here is a footnote reference.[^1]

[^1]: Here is the footnote.
```

## Alerts (GitHub)
Special blockquote syntax.

```markdown
> [!NOTE]
> Highlights information that users should take into account.

> [!WARNING]
> Critical content demanding immediate user attention.
```

## CommonMark vs GFM: Portability Guide

CommonMark is the baseline standard. GFM (GitHub Flavored Markdown) extends it. Renderers outside GitHub/GitLab may not support GFM-only features — default to CommonMark when portability matters.

| Feature           | CommonMark | GFM | Notes                        |
|-------------------|:----------:|:---:|------------------------------|
| Tables            | ✗          | ✓   | pipe syntax, colon alignment |
| Task lists        | ✗          | ✓   | `- [x]` / `- [ ]`           |
| Strikethrough     | ✗          | ✓   | `~~text~~`                   |
| Autolinks (bare)  | ✗          | ✓   | raw URLs auto-linked         |
| Alerts/callouts   | ✗          | ✓   | `> [!NOTE]` (GitHub 2023+)   |
| Math (`$$`)       | ✗          | ✓   | GitHub, GitLab, Obsidian     |
| Footnotes         | ✗          | ✓   | `[^1]` reference syntax      |

## Front Matter (YAML)

Supported by Jekyll, Hugo, Docusaurus, VuePress, and Obsidian. Must be the very first block in the file — no blank lines before it.

```yaml
---
title: My Document
date: 2024-01-01
tags: [docs, reference]
---
```

Shown as a table on raw GitHub Markdown preview. Invisible after SSG rendering.

## Code Fence Language Tags

Always specify a language for syntax highlighting: `bash`, `python`, `java`, `json`, `yaml`, `sql`, `text`.
Use `text` or `plaintext` when no language applies. Avoid blank fences — some renderers disable highlighting for the entire block.

## MDX Basics

MDX = Markdown + JSX. Used by Docusaurus and Next.js doc sites.
Keep MDX-specific syntax (imports, JSX tags, `{}` expressions) out of plain `.md` files — non-MDX renderers will fail to parse them.

