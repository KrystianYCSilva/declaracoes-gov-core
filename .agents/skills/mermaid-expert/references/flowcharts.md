---
name: flowcharts
description: |
  Flowchart syntax reference for Mermaid: shapes, arrows, subgraphs, styling, and v11 features (ELK layout, neo look, extended shapes).
  Use when: generating flowcharts or applying v11 config options.

---

# Flowchart Syntax Guide

## Basic Shapes
```mermaid
graph TD
    id1[This is the text in the box]
    id2(This is the text in the circle)
    id3([This is the text in the stadium block])
    id4[[This is the text in the subroutine block]]
    id5[(Database)]
    id6((Circle))
    id7>Asymmetric shape]
    id8{Rhombus}
    id9{{Hexagon}}
    id10[/Parallelogram/]
    id11[\Parallelogram alt\]
    id12[/Trapezoid\]
    id13[\Trapezoid alt/]
```

## Styling
Apply CSS styles to nodes.

```mermaid
graph LR
    A:::someclass --> B
    classDef someclass fill:#f9f,stroke:#333,stroke-width:4px;
```

## Subgraphs
Group related nodes.

```mermaid
graph TB
    c1-->a2
    subgraph one
    a1-->a2
    end
    subgraph two
    b1-->b2
    end
    subgraph three
    c1-->c2
    end
```

## v11: Frontmatter Config

Configure layout and appearance per diagram using YAML frontmatter above the diagram keyword.

```
---
config:
  layout: elk
  look: neo
  theme: default
---
flowchart LR
    A --> B --> C
```

`layout: elk` gives deterministic, non-overlapping positioning.
`look: neo` applies the modern default visual style (v11 default).
`theme`: `default`, `forest`, `dark`, `neutral`.

## v11: Extended Shape Syntax

30+ shapes are available via the `@{ shape: ... }` annotation:

```mermaid
flowchart LR
    A@{ shape: diamond, label: "Decision" }
    B@{ shape: stadium, label: "Terminal" }
    C@{ shape: cyl, label: "Database" }
    A --> B
    A --> C
```

Legacy shape syntax (`{Rhombus}`, `[(Database)]`, `[/Parallelogram/]`) still works and is backward-compatible.

