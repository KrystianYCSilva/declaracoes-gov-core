---
name: software-documentation-guide
description: |
  Guide software documentation standards including README, API docs, ADRs, changelogs, inline comments, and user guides.
  Use when: writing documentation for a project, establishing documentation standards, creating READMEs, writing API documentation, or maintaining changelogs.
activation: Manual
estimated_tokens: 1070
---

# Software Documentation Guide

Documentation is code for humans. It must be accurate, minimal, and maintained. This skill covers WHAT to document, HOW to document it, and WHERE to put it.

## Documentation Types

| Type | Audience | Location | Updates |
|------|----------|----------|---------|
| README | Developers, users | Root of repository | Every significant change |
| API Docs | API consumers | `/docs/api/` or auto-generated | Every API change |
| ADRs | Future developers | `/docs/adr/` | One per decision, never edited |
| Changelog | Users, ops | `CHANGELOG.md` in root | Every release |
| Inline comments | Developers | In source code | With code changes |
| User Guide | End users | `/docs/` or wiki | Every feature change |
| AGENTS.md | AI agents | Root of repository | When conventions change |

## How to Write a README

A README must answer 5 questions in this order:

```
1. WHAT: What does this project do? (1-2 sentences)
2. WHY:  Why would someone use it? (problem it solves)
3. HOW:  How to install and run it? (commands, not prose)
4. TEST: How to run tests? (exact commands)
5. MORE: Where to find more info? (links to docs, contributing guide)
```

Load `templates/readme-template.md` for a fill-in README template.

## How to Write Inline Comments

Comments explain WHY, not WHAT. The code shows WHAT.

```java
// ❌ Bad: explains WHAT (obvious from the code)
// Set the student's name
student.setName(name);

// ✅ Good: explains WHY (not obvious from the code)
// DB2 returns names in uppercase — normalize for display
student.setName(name.toLowerCase());

// ✅ Good: explains a business rule
// Students with more than 2 failed subjects lose scholarship eligibility
if (failedCount > 2) {
    student.setScholarshipEligible(false);
}
```

**When to comment:**
- Business rules that aren't obvious from the code
- Workarounds with explanation (link to issue/bug)
- Performance-critical sections (why this algorithm was chosen)
- Regex patterns (always explain complex regex)

**When NOT to comment:**
- Getters/setters (self-documenting)
- Simple CRUD operations
- Anything the method/variable name already explains

## How to Write API Documentation

For REST APIs, document each endpoint with:

```markdown
### POST /api/students

Create a new student registration.

**Request:**
```json
{
  "name": "Maria Silva",
  "ra": "12345",
  "course": "Computer Science"
}
```

**Response (201):**
```json
{
  "id": 1,
  "name": "Maria Silva",
  "ra": "12345",
  "course": "Computer Science",
  "createdAt": "2025-01-15T10:30:00Z"
}
```

**Errors:**
| Status | Reason |
|--------|--------|
| 400 | Missing required field (name, ra) |
| 409 | Student with this RA already exists |
```

## How to Write a Changelog

Follow [Keep a Changelog](https://keepachangelog.com/) format:

```markdown
## [1.2.0] - 2025-03-15

### Added
- Student enrollment history export (PDF)

### Changed
- Fee calculation now considers scholarship percentage

### Fixed
- Duplicate RA validation was case-sensitive (#123)

### Removed
- Legacy XML import endpoint (deprecated since 1.0.0)
```

**Categories:** Added, Changed, Deprecated, Removed, Fixed, Security.

Load `templates/changelog-template.md` for a starter template.

## How to Maintain Documentation

1. **Documentation is part of the Definition of Done** — a feature without docs is not done
2. **Review docs with code** — every PR that changes behavior must update relevant docs
3. **Automate what you can** — API docs from annotations (Swagger/OpenAPI), README badges from CI
4. **Delete outdated docs** — wrong documentation is worse than no documentation
5. **LLM agents update docs** — include in AGENTS.md: "Update README and CHANGELOG when modifying features"

Load `references/common-traps.md` for documentation anti-patterns.
