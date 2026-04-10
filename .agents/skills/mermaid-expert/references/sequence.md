---
name: sequence
description: |
  Sequence diagram syntax reference for Mermaid: messages, activations, fragments, and common pitfalls.
  Use when: generating sequence or interaction diagrams between actors.

---

# Sequence Diagram Guide

## Messages & Activations

```mermaid
sequenceDiagram
    autonumber
    Alice->>John: Hello John, how are you?
    loop Healthcheck
        John->>John: Fight against hypochondria
    end
    Note right of John: Rational thoughts!
    John-->>Alice: Great!
    John->>Bob: How about you?
    Bob-->>John: Jolly good!
```

## Fragments (Alt/Opt/Par)

```mermaid
sequenceDiagram
    Alice->>Bob: Hello Bob, how are you?
    alt is sick
        Bob->>Alice: Not so good :(
    else is well
        Bob->>Alice: Feeling fresh like a daisy
    end
    opt Extra response
        Bob->>Alice: Thanks for asking
    end
```

## Activation Boxes

Show when a participant is active:

```mermaid
sequenceDiagram
    Alice->>+Bob: Start request
    Bob-->>-Alice: Response
```

`+` activates the participant, `-` deactivates it.

## Common Pitfalls

- Actor names with spaces must be aliased: `participant "My Service" as MS`
- Arrow variants: `->>` (solid), `-->>` (dashed), `->>+` (activate), `<<->>` (bidirectional, v11)
- Keep actors to 5 or fewer per diagram to remain readable.
- `autonumber` adds step numbers to all messages.

