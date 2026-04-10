---
name: evaluation-checkpoints
description: |
  Reference for evaluating whether a cognitive architecture is safe, observable, and worth its complexity.
  Use when: reviewing agent architecture proposals, comparing patterns, or deciding whether autonomy is justified.
---

# Evaluation Checkpoints

## Architecture Questions

1. who owns state?
2. what stops the loop?
3. what evidence shows the loop is helping rather than hurting?
4. can the system recover from stale memory or tool failure?
5. where can a human interrupt or override?

## Rejection Signals

- opaque state transitions
- no escalation path
- powerful actions without narrow contracts
- context or memory treated as unbounded

## Evidence Threshold

Do not keep a complex loop because it sounds advanced.
Keep it only if it improves completion rate, recovery quality, or operator trust relative to a simpler baseline.
