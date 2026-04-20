---
name: session-resume
description: |
  Resume work from where we left off by reading plan and recent context.
  Use when: starting a new session, after rate limits, or after compaction.
---

Read the current plan.md, recent checkpoints, and any TODO tracking.
Then give me a concise briefing:

1. **O que foi feito** — last completed milestone (1 sentence)
2. **Onde paramos** — exact point of interruption (file, function, phase)
3. **Próximo passo** — what to do next (1 concrete action)
4. **Bloqueios** — anything that prevents progress (0 if none)

Keep the briefing under 10 lines. Do not re-explain context I already know.
Then ask: "Continuo de onde paramos ou há mudança de direção?"
