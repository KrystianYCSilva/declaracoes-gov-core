---
name: operating-model
description: |
  Reference for turning cognitive-system ideas into an operating model with ownership, retries, closure, and escalation.
  Use when: a design is leaving the whiteboard and must become an auditable engineering workflow.
---

# Operating Model

## Minimum Components

Every production-grade cognitive system should name:

1. who plans
2. who executes
3. who reviews
4. what state is authoritative
5. how closure and recovery work

## Engineering Anchors

- use cognitive architecture sources to name memory and control concepts
- use workflow and governance protocols to define ownership and stop conditions
- use secure-delivery and release standards when the system can modify code or operational state

## Practical Translation

A good design is not just "an autonomous agent".
It is an operating model with retries, escalation, write boundaries, and a clean path back to a safe idle state.
