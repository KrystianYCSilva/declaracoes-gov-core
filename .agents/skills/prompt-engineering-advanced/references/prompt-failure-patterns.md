---
description: |
  Common prompt engineering failure patterns with fixes.
  Use when: debugging prompts that produce inconsistent, fabricated, or low-quality LLM output.
---

# Prompt Failure Patterns

## Pattern 1: The Kitchen Sink Prompt

**Symptom:** Prompt is 2,000+ words with 15 instructions. LLM follows 3-4 of them, ignores the rest.

**Cause:** Attention distribution. LLMs weight the beginning and end of a prompt more than the middle. More instructions = lower per-instruction compliance.

**Fix:**
- Maximum 7 instructions per prompt (cognitive limit applies to LLMs too)
- Most important instructions go FIRST and LAST
- Use numbered lists, not prose paragraphs
- If you need 15 rules, split into phases: 7 rules for phase 1, then 7 for phase 2

---

## Pattern 2: Implicit Constraints

**Symptom:** You expected Java 8 syntax but got Java 17 features. You wanted REST but got GraphQL.

**Cause:** Constraint was "obvious" to you but never stated. LLM defaults to its training data mode (usually latest version, most popular framework).

**Fix:**
- State EVERY constraint explicitly: language version, framework, coding style
- Use negative constraints: "Do NOT use Java 17 features (records, sealed classes, text blocks)"
- Provide an example of the expected output format

---

## Pattern 3: Asking for Explanation Instead of Execution

**Symptom:** "How would you refactor this code?" → LLM explains refactoring theory. You wanted it to actually refactor.

**Cause:** "How would you" is a question about approach, not a command to execute.

**Fix:**
- ❌ "How would you add logging to this class?"
- ✅ "Add SLF4J logging to this class. Log method entry at DEBUG, errors at ERROR."
- Use imperative mood: "Create", "Add", "Refactor", "Delete" — not "How", "Could", "Would"

---

## Pattern 4: Missing Examples (Zero-Shot When Few-Shot is Needed)

**Symptom:** Output format varies every time. Sometimes JSON, sometimes YAML, sometimes prose.

**Cause:** No example of expected output was provided. LLM picks whatever format seems reasonable.

**Fix:**
- Always provide 1 example of expected input/output
- For code generation: provide 1 existing file as "follow this pattern"
- For structured output: show the exact format with placeholder values

---

## Pattern 5: Contradictory Instructions

**Symptom:** Agent alternates between two approaches. Output is inconsistent within the same response.

**Cause:** Prompt contains conflicting rules. "Keep it simple" + "Handle all edge cases" + "Be concise" + "Be thorough."

**Fix:**
- Review prompt for contradictions before using
- When two rules conflict, state which one takes priority
- Example: "Prefer simplicity over completeness. If a method handles >3 edge cases, extract a helper."

---

## Pattern 6: Temperature Mismatch

**Symptom:** Code output has creative variable names, unusual patterns, or inconsistent style.

**Cause:** Temperature too high for code generation. High temperature = more creative = less predictable.

**Fix:**
- Code generation: temperature 0.0-0.2
- Analysis/reasoning: temperature 0.3-0.5
- Creative writing: temperature 0.7-1.0
- When using CLI tools, check if temperature is configurable and set it for code tasks
