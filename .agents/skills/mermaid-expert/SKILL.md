---
name: mermaid-expert
description: |
  Guide creating Mermaid diagrams with readable structure, stable syntax, and pragmatic complexity control.
  Use when: generating any Mermaid diagram type (flowcharts, sequence, state, ER, Gantt, class, mindmap, timeline, git graph, or C4) in Markdown.
---

# Mermaid Expert

Treat Mermaid as diagram code, not decoration.
Clarity of the model matters more than visual novelty.

Activate when generating any Mermaid diagram in Markdown. Do not activate if the renderer is confirmed not to support Mermaid — offer a text-based alternative instead.

## How to Pick the Right Diagram Type

Choose the diagram by the question being answered:

- flowchart: branching logic or process steps → `references/flowcharts.md`
- sequence: interaction between actors over time → `references/sequence.md`
- state, ER, Gantt, class, mindmap, timeline, or git: structured models → `references/advanced-diagrams.md`
- C4 context diagrams for system architecture → `references/advanced-diagrams.md`

If a diagram tries to answer too many questions at once, split it.

## How to Keep Mermaid Readable

Keep:

1. short node ids
2. descriptive labels
3. consistent direction
4. limited branching per diagram
5. one semantic focus per artifact

## How to Avoid Rendering Failures

Validate tricky diagrams before shipping.
Watch for:

- malformed arrows
- special characters in labels
- subgraph direction conflicts
- diagrams too dense for the target renderer

Use the live editor or renderer-native preview when the syntax is non-trivial.

## How to Stay Accurate

- prefer the official sources in `references/sources.md` when syntax or renderer behavior is uncertain
- validate against the Mermaid live editor for non-trivial diagrams

## How to Review Diagram Quality

A good Mermaid diagram should be:

- semantically correct
- easy to scan
- no denser than the prose it replaces
- understandable even with minimal styling
