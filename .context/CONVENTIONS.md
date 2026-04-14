# Conventions — declaracoes-gov-core

## Java

- Java 8 source/target.
- All model classes implement `Serializable` with `serialVersionUID = 1L`.
- Mutable POJOs: private fields, public getters/setters.
- Javadoc on every public class citing official source.

## Naming

- Base package: `br.uem.npd.govcore`.
- Modules follow `declaracoes-gov-core-{concern}` pattern (domain, format, crypto, xml).
- Interfaces for contracts (`Layout`, `Registro`); concrete classes for implementations.

## Testing

- JUnit 4.13.2.
- JaCoCo coverage: 90% instruction / 90% branch minimum.

## Documentation

- Human docs in Portuguese under `docs/`.
- AI context in English under `.context/`.
