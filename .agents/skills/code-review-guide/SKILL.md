---
name: code-review-guide
description: |
  Guide structured code review for AI agents and human reviewers with severity-based checklists and actionable feedback.
  Use when: reviewing pull requests, auditing AI-generated code, doing pre-merge quality checks, or giving feedback on code changes.
activation: Auto
estimated_tokens: 940
---

# Code Review Guide

This skill provides a structured, repeatable code review process.
It ensures reviews are consistent, actionable, and focused on what matters — not style nitpicks.

## How to Structure a Review

Every review follows 3 passes, in order. Do NOT mix them.

```
Pass 1: Correctness  — Does the code do what it's supposed to?
Pass 2: Safety       — Can it break, leak, or corrupt?
Pass 3: Maintainability — Will the next developer understand it?
```

Never comment on style (formatting, naming preferences) unless it violates an explicit project convention in AGENTS.md.

## How to Classify Findings

Use severity levels so the author knows what to fix first:

| Severity | Label | Meaning | Action |
|----------|-------|---------|--------|
| 🔴 Critical | `MUST FIX` | Bug, security vulnerability, data loss risk | Block merge |
| 🟡 Important | `SHOULD FIX` | Logic error, missing test, poor error handling | Fix before merge or justify |
| 🔵 Suggestion | `CONSIDER` | Better approach exists, readability improvement | Author decides |
| ⚪ Nitpick | `NIT` | Style, naming preference, minor optimization | Ignore unless easy |

Load `references/review-checklist.md` for the full checklist organized by pass.

## How to Review AI-Generated Code

AI-generated code has specific failure patterns. Check for these FIRST:

1. **Fabricated names** — classes, methods, or fields that don't exist in the project
2. **Missing tests** — code committed without corresponding test files
3. **Framework hallucination** — using APIs or config keys that don't exist in the project's framework version
4. **Over-abstraction** — unnecessary interfaces, abstract base classes, or design patterns for simple code
5. **Copy-paste from training data** — code that looks like a tutorial, not like the project's style

Load `references/common-traps.md` for detailed AI-generated code review traps.

## How to Write Actionable Feedback

Every review comment must include:

```
1. WHERE: File and line (or code snippet)
2. WHAT: The specific issue
3. WHY: Why it matters (not "because best practice" — the actual risk)
4. HOW: Suggested fix (concrete, not vague)
```

**❌ Bad feedback:**
> "This could be improved."

**✅ Good feedback:**
> `src/UserService.java:45` — `findUser()` returns null when user not found.
> **Risk:** NullPointerException in every caller.
> **Fix:** Return `Optional<User>` or throw `UserNotFoundException`.

## How to Handle Disagreements

When author pushes back on a review finding:

1. If it's 🔴 Critical: non-negotiable. Evidence required to override.
2. If it's 🟡 Important: discuss. If the author provides valid justification, accept.
3. If it's 🔵/⚪: author decides. Don't block merge over suggestions.

The reviewer's job is to find real problems, not to enforce personal preferences.

## How to Review Tests

Tests have their own review criteria:

1. Does each test have a descriptive name (Given/When/Then)?
2. Does each test assert a BUSINESS behavior (not just `assertNotNull`)?
3. Are tests independent (no shared mutable state, no execution order dependency)?
4. Are edge cases covered (null, empty, boundary values)?
5. Are mocks minimal (only external dependencies, not internal classes)?

Load `templates/review-comment-template.md` for a structured review comment format.
