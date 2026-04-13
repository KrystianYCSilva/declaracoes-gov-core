# Copilot Instructions

Use `AGENTS.md` as the repository index.

## Project Snapshot

- `declaracoes-gov-core` is a Maven parent for `domain`, `format`, `xml`, `crypto`, and `core-bom` modules.
- Live implementation is in `declaracoes-gov-core-*/src` and the related `pom.xml` files.
- Human docs are Portuguese; AI docs are English.

## Must-Honor Rules

- Keep the core declaration-agnostic and framework-agnostic.
- Preserve the published validator confidence model and the current `Cnpj` / `Cpf` / `Nis` behavior.
- `mvn -q verify` must stay green.

## Working Rule

- Put mutable details in `AGENTS.md` or `.context/`, not here.
