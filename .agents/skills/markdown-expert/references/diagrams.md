---
name: diagrams
description: |
  Mermaid diagram examples for embedding in Markdown: flowchart, sequence, ER, and state diagrams with renderer compatibility notes.
  Use when: embedding or reviewing Mermaid diagrams inside Markdown documents, or checking renderer support.

---

# Diagrams as Code (Mermaid)

Render diagrams dynamically from text.

## Flowchart
```mermaid
graph TD
    A[Start] --> B{Is it working?}
    B -- Yes --> C[Great!]
    B -- No --> D[Debug]
```

## Sequence Diagram
```mermaid
sequenceDiagram
    Alice->>John: Hello John, how are you?
    John-->>Alice: Great!
```

## Entity Relationship (ER)
```mermaid
erDiagram
    CUSTOMER ||--o{ ORDER : places
    ORDER ||--|{ LINE-ITEM : contains
```

## State Diagram
```mermaid
stateDiagram-v2
    [*] --> Still
    Still --> [*]
    Still --> Moving
    Moving --> Still
    Moving --> Crash
    Crash --> [*]
```

## Renderer Compatibility

| Feature              | GitHub | GitLab | VS Code   | Obsidian  | Docusaurus |
|----------------------|:------:|:------:|:---------:|:---------:|:----------:|
| Mermaid (built-in)   | ✓      | ✓      | plugin    | plugin    | plugin     |
| Inline HTML          | limited| limited| ✓         | ✓         | ✓          |

## Common Pitfalls

- **Special characters in labels**: wrap labels in quotes — `A["label (with parens)"]`
- **Long labels**: break with `<br/>` inside quotes — `A["line one<br/>line two"]`
- **Syntax errors fail silently** in some renderers: validate in the Mermaid live editor (`mermaid.live`) first.
- **Never nest** a Mermaid block inside another fenced code block.

