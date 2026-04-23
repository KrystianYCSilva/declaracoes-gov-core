# Deferred Internal Contracts

This feature does **not** ship internal SPI contracts such as `ValidatorRegistry`, `SslContextFactory`, or mutable `GovJsonFactory` hooks.

## Current Contract

- The core publishes stable defaults through the existing public classes.
- Consumers compose specialized behavior in their own modules.
- Future internal SPI contracts must be introduced only when there is proven multi-project reuse and matching implementation in code and tests.

## Deferred Candidates

- `ValidatorRegistry`
- `SslContextFactory`
- `DefaultSslContextFactory`
- Mutable or pluggable `GovJsonFactory`

## Guardrail

Specs, quickstarts, and tasks must never advertise these candidates as shipped APIs until the corresponding code and tests exist in the repository.
