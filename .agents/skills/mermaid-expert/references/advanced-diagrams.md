---
name: advanced-diagrams
description: |
  Advanced Mermaid diagram types: ER, state, Gantt, git graph, class, mindmap, timeline, and C4 context diagrams.
  Use when: generating any diagram type other than flowchart or sequence.

---

# Advanced Diagram Types

## Entity Relationship (ER)
Used for database schemas.

```mermaid
erDiagram
    CAR ||--o{ NAMED-DRIVER : allows
    CAR {
        string registrationNumber
        string make
        string model
    }
    PERSON ||--o{ NAMED-DRIVER : is
    PERSON {
        string driversLicense
        string firstname
        string lastname
    }
```

## State Diagram
State machines and transitions.

```mermaid
stateDiagram-v2
    [*] --> Still
    Still --> [*]

    Still --> Moving
    Moving --> Still
    Moving --> Crash
    Crash --> [*]
```

## Gantt Chart
Project planning.

```mermaid
gantt
    title A Gantt Diagram
    dateFormat  YYYY-MM-DD
    section Section
    A task           :a1, 2014-01-01, 30d
    Another task     :after a1  , 20d
    section Another
    Task in sec      :2014-01-12  , 12d
    another task     : 24d
```

## Git Graph
Visualize git history.

```mermaid
gitGraph
    commit
    commit
    branch develop
    checkout develop
    commit
    commit
    checkout main
    merge develop
    commit
```

## Class Diagram
Object model and inheritance.

```mermaid
classDiagram
    class Animal {
        +String name
        +int age
        +makeSound() void
    }
    class Dog {
        +fetch() void
    }
    Animal <|-- Dog
    Animal <|-- Cat
```

## Mindmap
Hierarchical topic breakdown.

```mermaid
mindmap
  root((Project))
    Backend
      API
      Database
    Frontend
      UI
      State
```

## Timeline
Ordered events in time.

```mermaid
timeline
    title Project Milestones
    2024-01 : Kickoff
    2024-03 : Alpha release
    2024-06 : Beta release
    2024-09 : GA
```

## C4 Context Diagram
System context modeling (experimental in v11).

```mermaid
C4Context
    title System Context
    Person(user, "User", "End user of the system")
    System(app, "Application", "Core service")
    Rel(user, app, "Uses")
```

