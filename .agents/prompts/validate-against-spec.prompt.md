---
name: validate-against-spec
description: |
  Compare implementation against official specification or documentation.
  Use when: validating against XSD schemas, MEC layouts, eSocial specs, or any official format.
---

Compare the implementation in the specified files against the official specification.

For each field/element/rule in the spec:
1. **✅ Conforme** — implementation matches spec exactly
2. **⚠️ Divergente** — implementation differs (show both: spec says X, code does Y)
3. **❌ Ausente** — spec requires it but implementation is missing
4. **🔶 Extra** — implementation has it but spec doesn't require it (potential YAGNI)

Output format:
```
## Validação: [component] vs [spec name]

| Campo/Regra | Spec | Implementação | Status |
|-------------|------|---------------|--------|
| ...         | ...  | ...           | ✅/⚠️/❌/🔶 |

### Divergências Críticas
- [list only ⚠️ and ❌ items with fix suggestions]
```

Be precise. Quote the spec. Quote the code. No assumptions.
