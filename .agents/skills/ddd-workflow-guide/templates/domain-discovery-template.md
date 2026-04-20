---
description: |
  Structured domain discovery worksheet. Copy and fill before coding.
  Use when: starting a DDD project and need to understand the business domain.
---

# Domain Discovery Worksheet

## 1. Actors (Who uses the system?)

| Actor | Role | Key Needs |
|-------|------|-----------|
| | | |
| | | |
| | | |

## 2. Core Processes (What are the key business workflows?)

| Process | Trigger | Steps | Output |
|---------|---------|-------|--------|
| | | | |
| | | | |

## 3. Ubiquitous Language (One term = One meaning)

| Term | Definition | Context | NOT to confuse with |
|------|------------|---------|-------------------|
| | | | |
| | | | |

## 4. Bounded Contexts

| Context | Responsibility | Key Entities | Owns Data |
|---------|---------------|-------------|-----------|
| | | | |
| | | | |

## 5. Context Relationships

```
[Context A] ──publishes──> [Event] ──subscribes──> [Context B]
[Context C] ──calls──> [Context A API] (via anti-corruption layer)
```

## 6. Aggregates (per context)

### Context: ________________

| Aggregate Root | Internal Entities | Value Objects | Key Invariants |
|---------------|-------------------|---------------|----------------|
| | | | |
| | | | |

### Context: ________________

| Aggregate Root | Internal Entities | Value Objects | Key Invariants |
|---------------|-------------------|---------------|----------------|
| | | | |

## 7. Domain Events

| Event | Published by | Consumed by | Data |
|-------|-------------|-------------|------|
| | | | |
| | | | |
