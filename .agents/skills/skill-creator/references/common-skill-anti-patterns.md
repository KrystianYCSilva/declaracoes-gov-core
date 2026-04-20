---
description: |
  Common anti-patterns in skill design with fixes.
  Use when: creating, reviewing, or maintaining skills in .agents/skills/.
---

# Common Skill Anti-Patterns

## Anti-Pattern 1: Theory Without Templates

**Symptom:** SKILL.md explains concepts beautifully but the agent still doesn't know WHAT to do. It understands the theory but can't execute.

**Example:** "Use JaCoCo for coverage measurement" (theory) vs providing `templates/pom-additions.xml` with exact XML to paste (operational).

**Fix:**
- Every "How to" section in SKILL.md must point to a concrete artifact (template, reference, or command)
- If you can't provide a copy-paste artifact, the guidance is too abstract
- Test: can an agent follow this skill WITHOUT searching the internet? If not, add the missing artifact.

---

## Anti-Pattern 2: Bloated SKILL.md

**Symptom:** SKILL.md is 500+ lines. Agent loads it and burns 5,000+ tokens before starting work.

**Cause:** All content lives in SKILL.md instead of being distributed to references/ and templates/.

**Fix:**
- SKILL.md ≤ 100 lines (< 1,500 tokens)
- SKILL.md = routing table ("for X, load references/Y")
- Deep content lives in references/ (loaded only when needed)
- Templates live in templates/ (loaded only when copying into projects)
- Test: measure SKILL.md token count. If > 2,000 tokens, refactor.

---

## Anti-Pattern 3: Dead References

**Symptom:** SKILL.md says "load references/deep-guide.md" but the file doesn't exist, or its content doesn't match what SKILL.md promises.

**Cause:** Skill was created in one session and never maintained. References drift from the SKILL.md description.

**Fix:**
- Every reference mentioned in SKILL.md must exist and contain what SKILL.md says it contains
- Use `scripts/quick_validate.py` (from skill-creator) to check structural integrity
- Periodic audit: read SKILL.md, then read each referenced file, verify alignment

---

## Anti-Pattern 4: Missing Frontmatter

**Symptom:** Skill is never loaded by the LLM because the CLI can't match the user's task to the skill's topic.

**Cause:** Missing or vague `description` in SKILL.md frontmatter. The description is how CLIs decide whether to load a skill.

**Fix:**
```yaml
# ❌ Bad: too vague
description: Guide software development practices.

# ✅ Good: specific triggers
description: |
  Guide brownfield refactoring of legacy Java/Hibernate projects with TDD, JaCoCo coverage gates, and H2 test infrastructure.
  Use when: refactoring legacy Java code, adding tests to untested projects, setting up JaCoCo coverage.
```

The `Use when:` line is critical — it tells the CLI exactly which user requests should trigger this skill.

---

## Anti-Pattern 5: No Cross-References

**Symptom:** Two skills cover overlapping topics. Agent loads one but misses critical information in the other.

**Cause:** Skills are islands. No skill mentions related skills.

**Fix:**
- Add a "Related Skills" section at the end of SKILL.md
- Example: brownfield-refactoring should mention quality-assurance for coverage strategy
- Keep cross-references light: just the skill name and when to load it
- Don't create circular dependencies (A→B→A)
