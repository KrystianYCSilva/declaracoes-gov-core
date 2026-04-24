# Tasks Directory

This directory contains work package (WP) prompt files with lane status in frontmatter.

## Directory Structure

```
tasks/
├── WP01-gov-number-constants-enums-dominio.md
├── WP02-gov-currency-formats.md
├── WP03-financial-types-kotlin.md
├── WP04-expansoes-extensions-kotlin.md
└── README.md
```

All WP files are stored flat in `tasks/`. The lane (planned, doing, for_review, done) is stored in the YAML frontmatter `lane:` field.

## Work Package File Format

Each WP file **MUST** use YAML frontmatter:

```yaml
---
work_package_id: "WP01"
title: "Work Package Title"
lane: "planned"
subtasks:
  - "T001"
  - "T002"
dependencies: []
created_at: "2026-04-24T00:00:00Z"
---
```

## Valid Lane Values

- `planned` - Ready for implementation
- `doing` - Currently being worked on
- `for_review` - Awaiting review
- `done` - Completed
