# Research: Core Audit and Composition Boundaries

## Decision: Do Not Introduce `ValidatorRegistry` in This Iteration
- **Decision**: Keep `GovValidators` and `Nis` as the shipped defaults for the current release.
- **Rationale**: The codebase does not contain a registry today, and the current release does not need a mutable validation chain inside the shared core. Consumer-specific validation can be composed externally without forcing a global registry.
- **Consequence**: The feature documentation must stop promising `ValidatorRegistry` or `Validator` APIs that are not shipped.

## Decision: Keep JSON and SSL Helpers Opinionated
- **Decision**: Keep `GovJsonFactory` and `SslContextBuilder` as utility-first defaults in this iteration.
- **Rationale**: They already provide the common path needed by current consumers. Adding mutable factory hooks now would increase global surface area without proven multi-project demand.
- **Consequence**: Consumers with special cases should create their own `ObjectMapper` or `SSLContext` outside the core.

## Decision: Contain `IdentificadorEmpregador`
- **Decision**: Preserve the deprecated interface only where it already exists for compatibility.
- **Rationale**: `Cnpj` and `Cpf` still implement it, but no new validators, registries, or helpers should be built around this contract.
- **Consequence**: Future extractions must target neutral value objects and services rather than the deprecated marker interface.
