---
name: zk-stack-patterns
description: |
  ZK CE UI patterns: MVC composers, MVVM binders, session/desktop management, and performance tuning.
  Use when: building or refactoring ZK CE screens, debugging session leaks, fixing slow renders, or choosing MVC vs MVVM.
activation: Auto
estimated_tokens: 620
---

## How to choose MVC vs MVVM

Choose MVC (`SelectorComposer`) if you need:
- dynamic component trees at runtime
- imperative DOM integration with external JS libs
- fine-grained component manipulation that binding makes clunky

Choose MVVM (`BindComposer`) for everything else — especially CRUD screens.

## How to build an MVC screen

ZUL: `apply="com.app.web.composer.MyComposer"`
Composer: extend `SelectorComposer<Component>`, use `@Wire` + `@Listen`.

**Thin Composer Rule**: composer orchestrates UI only. Delegate logic to Spring services.
Never store large data in component attributes. Prefer `ListModel` over manual row creation.

## How to build an MVVM screen

ZUL: `apply="org.zkoss.bind.BindComposer"` + `viewModel="@id('vm') @init('...')"`.
ViewModel: private fields + getters + `@Command` + `@NotifyChange({"prop1","prop2"})`.

Binding rules:
- `@load(vm.x)` = one-way VM→View (read-only displays)
- `@save(vm.x, before='cmd')` = View→VM before command (form inputs)
- `@bind(vm.x)` = two-way (use carefully)
- **NEVER** use `@NotifyChange("*")` unless measured safe — causes rebind storms

Form pattern: `FormDTO` detached from JPA → `@Validator` → Service maps DTO→entity.

Load `references/common-traps.md` for ZK-specific pitfalls.

## How to manage sessions and desktops

- HttpSession = shared across tabs. Desktop = per tab (holds full component tree).
- Limit desktops per session (3-5). Don't store entity graphs in session.
- Persist drafts in DB, not in session. Warn users before timeout.
- **NEVER** update UI from background threads directly — use desktop activation/scheduling.

## How to tune performance

Golden rules:
1. Never render large datasets without paging (`paging="true" pageSize="20"`)
2. Reduce component count: use native HTML tags for layout
3. Keep view state small: DTOs, not entities
4. Move long work off the event thread (async + safe UI update)
5. Minimize binder churn: no `@NotifyChange("*")`

For DB2-heavy tables, prefer keyset pagination:
```sql
WHERE key > :lastKey ORDER BY key FETCH FIRST :n ROWS ONLY
```

Any call that can exceed ~200ms under load must be async with busy indicator.
