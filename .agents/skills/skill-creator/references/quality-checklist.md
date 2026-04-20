---
description: |
  13-point quality gate for validating skills before deployment.
  Use when: reviewing a skill for completeness, correctness, and token efficiency before publishing.
  Extracted from: skill quality checklist (validated across multiple skill packages)
---

# Skill Quality Checklist

Score each point: PASS / WARNING / CRITICAL FAIL. A skill is deployable only with 0 CRITICAL fails.

## Grading

| Grade | Criteria | Action |
|-------|----------|--------|
| **PASS** | 0 CRITICAL, 0-2 WARNING | Deploy |
| **PARTIAL** | 0 CRITICAL, 3+ WARNING | Fix warnings, then deploy |
| **FAIL** | 1+ CRITICAL | Do not deploy. Fix critical issues first |

## The 13 Points

### 1. Frontmatter Valid (CRITICAL)
- [ ] YAML is valid and parseable
- [ ] `name` matches pattern `^[a-z0-9]+(-[a-z0-9]+)*$`, max 64 chars
- [ ] `description` is present with `Use when:` trigger line

### 2. No Extra Frontmatter Fields (WARNING)
- [ ] Frontmatter contains ONLY `name` and `description`
- [ ] No `version`, `license`, `author`, `tags` in frontmatter (these belong in README, not SKILL.md)

### 3. Description Quality (CRITICAL)
- [ ] Starts with action verb ("Guide...", "Create...", "Analyze...")
- [ ] Includes `Use when:` with specific trigger conditions
- [ ] ~100 tokens (2-3 sentences max)
- [ ] Would a CLI match this description to the right user task? If not, rewrite.

### 4. Body is Operational (CRITICAL)
- [ ] Uses `## How to [verb]` section headers
- [ ] Each section has at least one concrete artifact: command, code snippet, or template reference
- [ ] No sections that are purely theoretical without actionable guidance

### 5. Token Economy (CRITICAL)
- [ ] SKILL.md ≤ 100 lines (~1,500 tokens)
- [ ] Does NOT teach well-documented patterns (assume LLM knows Java, REST, SQL basics)
- [ ] Test: "Would a senior dev need this information?" If no for a section → cut it

### 6. Progressive Disclosure (WARNING)
- [ ] Deep content is in `references/`, not in SKILL.md body
- [ ] References are max 1 level deep (no `references/sub/deep/file.md`)
- [ ] Each referenced file is truly optional (SKILL.md works without loading it)
- [ ] Every file mentioned in SKILL.md actually exists

### 7. Templates Are Copy-Paste Ready (WARNING)
- [ ] Templates in `templates/` can be copied directly into a project
- [ ] Templates have clear `[PLACEHOLDER]` markers for customization
- [ ] Templates include usage instructions in their frontmatter

### 8. Common Traps Documented (WARNING)
- [ ] `references/common-traps.md` exists with at least 3 validated traps
- [ ] Each trap has: Symptom, Cause, Fix (concrete, not vague)
- [ ] Traps come from real failures, not speculation

### 9. No Broken References (CRITICAL)
- [ ] Every `Load references/X.md` in SKILL.md has a corresponding file
- [ ] Every `Load templates/Y.md` in SKILL.md has a corresponding file
- [ ] No dead links to non-existent files

### 10. Cross-References (WARNING)
- [ ] Related skills are mentioned (e.g., "See also: quality-assurance")
- [ ] No circular dependencies (A→B→A)

### 11. No Duplication (WARNING)
- [ ] Content in SKILL.md is NOT repeated in references/
- [ ] SKILL.md summarizes, references/ elaborates

### 12. Scripts Work (WARNING, if applicable)
- [ ] Scripts in `scripts/` are executable
- [ ] Scripts have usage instructions (comments or --help)
- [ ] Scripts handle errors gracefully

### 13. Tested with Fresh Agent (CRITICAL)
- [ ] A fresh agent (zero context) can follow this skill and produce correct output
- [ ] If the skill requires internet search to execute, it's missing a reference or template
