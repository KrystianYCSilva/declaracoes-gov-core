---
name: architectures
description: |
  Reference for mapping cognitive-architecture concepts to implementable language-agent components.
  Use when: engineering memory, action, and decision modules from a cognitive design into a concrete system.
---

# Cognitive Architectures Deep Dive

## CoALA

CoALA is a useful language-agent decomposition around memory, action space, and decision modules.
It helps name the parts of the system without pretending the model itself is the whole architecture.

## ACT-R

ACT-R is useful when distinguishing declarative and procedural memory concepts.
It is most helpful as a vocabulary aid, not as a blueprint to reproduce mechanically.

## Engineering Translation

When translating theory into software, define:

1. where state lives
2. who can write which state
3. what triggers each decision phase
4. how the system exits or escalates

## Source Anchors

- CoALA is useful for decomposing language-agent systems into modules.
- ACT-R is useful for distinguishing declarative and procedural memory concepts.
- orchestration frameworks are useful only after ownership, retries, and closure rules are explicit.
