# Research: Core Extensibility and Composition

## Decision: Validator Registry Singleton
- **Decision**: Introduce a `ValidatorRegistry` as a singleton in the `domain` module.
- **Rationale**: Currently, `GovValidators` uses static chains that are impossible to extend without modifying the core library. A registry allows consumers to inject or override validators at runtime.
- **Backward Compatibility**: `GovValidators` will be refactored to delegate its static methods to the default instance of the `ValidatorRegistry`, ensuring existing code continues to work without changes.

## Decision: SPI for Factories
- **Decision**: Create `SslContextFactory` and `GovJsonFactory` as Service Provider Interfaces (SPI).
- **Rationale**: `SslContextBuilder` and `GovJsonFactory` are currently utility-first and rigid. By introducing SPIs, we allow specialized modules (like the future `core-transport`) or end-users to provide custom SSL configurations or Jackson `ObjectMapper` setups.
- **Alternatives Considered**: Direct injection into every method call was rejected to avoid breaking all existing method signatures.

## Decision: Decoupling IdentificadorEmpregador
- **Decision**: Generic `Validator` contract will work with `Object` or a new base `Identifier` interface.
- **Rationale**: `IdentificadorEmpregador` is deprecated and carries legacy weight. Moving to a more generic contract prevents new code from being tied to this interface.
