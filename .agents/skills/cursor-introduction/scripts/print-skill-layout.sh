#!/usr/bin/env bash
# Imprime a estrutura mínima recomendada para uma Agent Skill (agentskills.io).
cat <<'EOF'
skill-name/
├── SKILL.md          # name + description (YAML) + instruções
├── scripts/          # opcional
├── references/       # opcional (carga JIT)
└── assets/           # opcional (templates, dados estáticos)

Regras: nome da pasta == campo `name` em SKILL.md (minúsculas, hífens).
Validar: skills-ref validate ./skill-name
EOF
