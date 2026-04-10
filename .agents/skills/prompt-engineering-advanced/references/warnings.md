---
name: warnings
description: |
  Common prompt engineering pitfalls: over-prompting, context pollution, negative-constraint failures, and role amnesia, with concrete fixes for each.
  Use when: a prompt is failing in ways that suggest structural or semantic mistakes rather than capability gaps.
---

# Warnings & Common Pitfalls (Prompt Engineering)

## 1. Over-Prompting
- **Error**: Writing 2000-word prompts for simple tasks.
- **Fix**: Start simple. Use "Least-to-Most" prompting. Add complexity only when the model fails.

## 2. Context Pollution
- **Error**: Dumping entire codebases into the prompt "just in case".
- **Fix**: Use RAG or careful selection. Irrelevant info distracts the model ("Lost in the Middle").

## 3. Negative Constraints
- **Error**: Saying "Don't do X". Models often miss "not".
- **Fix**: Say "Do Y instead". Positive instructions are stronger.
  - *Bad*: "Don't write spaghetti code."
  - *Good*: "Write modular, SOLID-compliant code."

## 4. Role Amnesia
- **Error**: Long conversations where the model forgets its initial persona.
- **Fix**: Re-inject the System Prompt or Role periodically (or use Prompt Caching at the top).

