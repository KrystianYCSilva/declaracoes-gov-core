---
description: Criar branch de implementação e orquestrar work packages automaticamente.
---

## ⚠️ Verificar versão do flow antes de tudo

```bash
flow -v
flow --help
```

Este prompt foi escrito para o workflow v1.x (3-prompt). Se a versão instalada for 0.x, consulte `.agents/skills/flow-fundamentals/references/flow-v0.x.md` antes de prosseguir — os comandos e o modelo de branch diferem.

## Response Language Policy

- Always respond in the same language as the user's latest message.
- If the user writes in Portuguese, respond in Portuguese (pt-BR).
- Keep CLI commands, paths, and code snippets unchanged unless translation is explicitly requested.

## Política de Idioma

- Todas as interações deste comando devem ser em **português (pt-BR)**.
- Comandos CLI, paths e trechos de código permanecem inalterados.

## ⚠️ IMPORTANTE: Tudo na branch do slug

**Toda implementação acontece em uma única branch nomeada pelo slug da feature.** Código, commits, atualizações do kanban — tudo fica nessa branch.

---

## Seu Papel: Orquestrador

Você é o **orquestrador**. Você NÃO implementa código diretamente. Para cada work package, você DEVE delegar a implementação a um subagente, passando o contexto necessário.

**Para Claude Code**: use a ferramenta Agent (Task tool) com o agente `@"flow-wp-implementer (agent)"`.
**Para GitHub Copilot**: use `@flow-wp-implementer`.

---

## Passo 1: Identificar a feature

`$ARGUMENTS` deve conter o **número da feature** (ex: `/flow.build 002`, `/flow.build 004`).

Se `$ARGUMENTS` estiver vazio ou não for um número, liste as features disponíveis e pergunte:

```bash
ls flow-specs/
```

> "Qual feature deseja implementar? Informe o número (ex: `002`)."

Aguarde a resposta antes de prosseguir.

### Resolver o slug

A partir do número fornecido, encontre o diretório correspondente em `flow-specs/`:

```bash
ls flow-specs/ | grep "^0*<número>-"
```

Exemplo: se o usuário passou `002`, o slug pode ser `002-minha-feature`. Guarde o **slug completo** (ex: `002-minha-feature`).

---

## Passo 2: Ler a branch base do meta.json

```bash
cat flow-specs/<slug>/meta.json
```

Extraia o campo `target_branch` (ex: `main`, `develop`, `master`). Esta é a branch que contém os dados mais recentes e de onde a branch de implementação será criada.

Se `target_branch` não existir no meta.json, pergunte ao usuário:

> "Qual é a branch base? (ex: `main`, `master`, `develop`)"

---

## Passo 3: Criar/trocar para a branch de implementação

O nome da branch de implementação é o **slug da feature** (ex: `002-minha-feature`).

Primeiro, verifique se o repositório já possui commits:

```bash
git rev-parse HEAD 2>/dev/null
```

### Se o repositório NÃO possui commits (repo novo):

Crie a branch diretamente (ela será o primeiro branch):

```bash
git checkout -b <slug>
```

### Se o repositório possui commits:

Se a branch `<slug>` **ainda não existe**:

```bash
git checkout <target_branch>
git pull origin <target_branch>
git checkout -b <slug>
```

Se a branch `<slug>` **já existe**:

```bash
git checkout <slug>
```

**TODOS os work packages, commits e atualizações de status vão para esta MESMA branch.**

---

## Passo 3.1: Commit dos artefatos de planejamento

Os artefatos de planejamento (spec, plan, tasks) ficam **uncommitted** até este momento. Agora que estamos na branch de implementação, faça o primeiro commit com eles:

```bash
git add flow-specs/<slug>/
git add .flowflow/ 2>/dev/null
git commit -m "feat: planning artifacts for <slug>"
```

Se não houver nada para commitar (artefatos já commitados), ignore e continue.

---

## Passo 4: Coletar contexto do projeto

Antes de delegar os WPs, colete:

1. **Constituição do projeto** (se existir):
   ```bash
   cat .flowflow/memory/constitution.md 2>/dev/null
   ```
2. **Padrões de teste**: identifique o comando de teste e requisitos de cobertura da constituição. Se não existir, o padrão é `pytest --cov --cov-report=term-missing` com **>90% de cobertura**.

Guarde esses dados — eles serão incluídos no prompt de cada subagente.

---

## Passo 5: Implementar os work packages via subagentes

### 5.0 — Verificar caminho de implementação

Antes de delegar, verificar o `implementation_path` da feature:

```bash
python3 -c "import json; d=json.load(open('flow-specs/<slug>/meta.json')); print(d.get('implementation_path','full'))"
```

**Se `implementation_path` for `"direct"`:**

1. Ler o conteúdo do WP01:
   ```bash
   cat flow-specs/<slug>/tasks/WP01-*.md
   ```

2. Mover WP01 para doing:
   ```bash
   flow agent tasks move-task WP01 --to doing
   ```

3. Delegar ao subagente — mesmo padrão do fluxo full:

   **Claude Code:**
   ```
   Agent({
     description: "Implementar WP01 — direct path",
     subagent_type: "flow-wp-implementer",
     prompt: "## Padrões de Teste\n{constituição}\n\n## Work Package\n{conteúdo completo do WP01}"
   })
   ```

   **GitHub Copilot:**
   ```
   @flow-wp-implementer

   ## Padrões de Teste do Projeto
   {conteúdo da constituição ou padrão}

   ## Work Package
   {conteúdo completo do WP01-nome.md}
   ```

4. Verificar resultado após o subagente finalizar:
   - Confirme que o commit foi feito: `git log -1 --oneline`
   - Confirme que os testes passam
   - Se algo falhou, instrua o subagente a corrigir

5. Mover kanban:
   ```bash
   flow agent tasks move-task WP01 --to for_review
   flow agent tasks move-task WP01 --to done
   ```

6. Reportar conclusão ao usuário:
   ```
   ✅ Direct path concluído

   WP01: done
   Branch: <slug> pronta para revisão
   ```

**Após o item 6, o orquestrador NÃO precisa fazer mais nada.**

⛔ NÃO executar os passos 5.1–5.5 para features `direct`.

**Se `implementation_path` for `"full"` (ou campo ausente):**

Continuar com o comportamento atual — iterar WPs e delegar ao `flow-wp-implementer`.

---

Para **cada WP** em ordem (verifique `flow-specs/<slug>/tasks/`):

### 5.1 — Ler o conteúdo do WP

Leia o arquivo completo do WP:

```bash
cat flow-specs/<slug>/tasks/WP##-nome.md
```

### 5.2 — Mover para "doing"

Antes de iniciar a implementação, atualize o status do WP:

```bash
flow agent tasks move-task WP## --to doing
```

### 5.3 — Delegar a um subagente

**Você DEVE criar um subagente para cada WP.** NÃO implemente o código você mesmo.

Monte o prompt do subagente incluindo:
1. Os padrões de teste do projeto (da constituição ou o padrão >90%)
2. O conteúdo completo do arquivo do WP
3. O ID do WP para o formato do commit

**Claude Code** — use a ferramenta Agent:
```
Agent({
  description: "Implementar WP##",
  subagent_type: "flow-wp-implementer",
  prompt: "## Padrões de Teste\n{constituição}\n\n## Work Package\n{conteúdo do WP}"
})
```

Se `flow-wp-implementer` não estiver disponível como subagent_type, use `general-purpose` e inclua as instruções completas do agente implementador no prompt (regras de teste, fluxo de trabalho, e requisito de commit).

**GitHub Copilot** — use @-mention:
```
@flow-wp-implementer

## Padrões de Teste do Projeto
{conteúdo da constituição ou padrão}

## Work Package
{conteúdo completo do WP##-nome.md}
```

O subagente irá:
- Implementar o WP
- Escrever testes
- Executar testes e validar cobertura
- Fazer o commit

### 5.4 — Verificar resultado e atualizar status

Após o subagente finalizar:

1. Confirme que o commit foi feito: `git log -1 --oneline`
2. Confirme que os testes passam: execute o comando de teste do projeto
3. Se algo falhou, instrua o subagente a corrigir antes de prosseguir
4. Mova para revisão: `flow agent tasks move-task WP## --to for_review`
5. Se a revisão passou (testes ok, código correto): `flow agent tasks move-task WP## --to done`

### 5.5 — Próximo WP

Repita os passos 5.1–5.4 para cada WP restante.

---

**NOTA**: Se o status parecer dessincronizado, não se preocupe. Rejeições movem para "planned" (com feedback) e aprovações movem para "done".
