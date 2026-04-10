#!/bin/bash
#
# Script para criar uma nova skill no Kimi CLI
# Uso: ./create-skill.sh <nome-da-skill>

set -e

SKILL_NAME="$1"

if [ -z "$SKILL_NAME" ]; then
    echo "❌ Erro: Nome da skill não fornecido"
    echo "Uso: $0 <nome-da-skill>"
    exit 1
fi

# Normalizar nome (lowercase, hífens)
SKILL_NAME=$(echo "$SKILL_NAME" | tr '[:upper:]' '[:lower:]' | tr ' ' '-')

# Detectar diretório de skills
if [ -d ".kimi/skills" ]; then
    SKILLS_DIR=".kimi/skills"
elif [ -d ".claude/skills" ]; then
    SKILLS_DIR=".claude/skills"
elif [ -d ".agents/skills" ]; then
    SKILLS_DIR=".agents/skills"
else
    SKILLS_DIR=".kimi/skills"
    mkdir -p "$SKILLS_DIR"
fi

SKILL_DIR="$SKILLS_DIR/$SKILL_NAME"

if [ -d "$SKILL_DIR" ]; then
    echo "❌ Erro: Skill '$SKILL_NAME' já existe em $SKILL_DIR"
    exit 1
fi

# Criar estrutura
echo "📁 Criando skill '$SKILL_NAME'..."
mkdir -p "$SKILL_DIR"/{scripts,references,assets}

# Criar SKILL.md
cat > "$SKILL_DIR/SKILL.md" << 'EOF'
---
name: __SKILL_NAME__
description: Descrição da skill __SKILL_NAME__
---

# __SKILL_NAME__

## Visão Geral

Adicione aqui uma descrição do que esta skill faz.

## Uso

Explique como usar esta skill:

```bash
/skill:__SKILL_NAME__
```

## Funcionalidades

- Funcionalidade 1
- Funcionalidade 2
- Funcionalidade 3

## Referências

- **Guia Detalhado**: Veja [references/guide.md](references/guide.md)
- **Exemplos**: Veja [references/examples.md](references/examples.md)
- **Scripts**: Veja [scripts/](scripts/)

## Exemplos

### Exemplo 1

```
Prompt de exemplo para usar com esta skill
```

### Exemplo 2

```
Outro exemplo de uso
```
EOF

# Substituir placeholder
sed -i.bak "s/__SKILL_NAME__/$SKILL_NAME/g" "$SKILL_DIR/SKILL.md"
rm "$SKILL_DIR/SKILL.md.bak"

# Criar referências
cat > "$SKILL_DIR/references/guide.md" << EOF
# Guia de __SKILL_NAME__

## Conceitos

Explique os conceitos principais desta skill.

## Configuração

Detalhes de configuração.

## API/Interface

Documentação da interface.
EOF

sed -i.bak "s/__SKILL_NAME__/$SKILL_NAME/g" "$SKILL_DIR/references/guide.md"
rm "$SKILL_DIR/references/guide.md.bak"

cat > "$SKILL_DIR/references/examples.md" << EOF
# Exemplos de __SKILL_NAME__

## Exemplo Básico

\`\`\`
Exemplo de uso básico aqui
\`\`\`

## Exemplo Avançado

\`\`\`
Exemplo de uso avançado aqui
\`\`\`
EOF

sed -i.bak "s/__SKILL_NAME__/$SKILL_NAME/g" "$SKILL_DIR/references/examples.md"
rm "$SKILL_DIR/references/examples.md.bak"

# Criar script de exemplo
cat > "$SKILL_DIR/scripts/example.sh" << 'EOF'
#!/bin/bash
# Script de exemplo para __SKILL_NAME__

echo "Script de exemplo para __SKILL_NAME__"
echo "Modifique este script conforme necessário"
EOF

sed -i.bak "s/__SKILL_NAME__/$SKILL_NAME/g" "$SKILL_DIR/scripts/example.sh"
rm "$SKILL_DIR/scripts/example.sh.bak"
chmod +x "$SKILL_DIR/scripts/example.sh"

echo ""
echo "✅ Skill '$SKILL_NAME' criada com sucesso!"
echo ""
echo "📂 Estrutura criada em: $SKILL_DIR"
echo ""
echo "Próximos passos:"
echo "  1. Edite $SKILL_DIR/SKILL.md"
echo "  2. Adicione conteúdo a references/"
echo "  3. Crie scripts em scripts/"
echo "  4. Teste com: /skill:$SKILL_NAME"
