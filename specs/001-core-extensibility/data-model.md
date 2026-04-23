# Data Model: Current Core Surface and Extension Boundaries

## Entities

### Core Defaults
- **Role**: Stable, shared implementations already shipped by the core.
- **Examples**:
  - `GovValidators`
  - `Nis`
  - `GovJsonFactory`
  - `SslContextBuilder`
  - XML helpers in `declaracoes-gov-core-xml`

### Consumer Composition Layer
- **Role**: Project-specific services, builders, and wrappers created outside the core.
- **Examples**:
  - Service that applies extra validation after `Nis.of(...)`
  - Custom `ObjectMapper` derived from `GovJsonFactory.getMapper().copy()`
  - Custom TLS bootstrap that bypasses `SslContextBuilder` when needed

### Deprecated Compatibility Types
- **Role**: Legacy contracts preserved for backward compatibility only.
- **Examples**:
  - `IdentificadorEmpregador`
  - Existing implementing types such as `Cnpj` and `Cpf`

## Interaction Pattern

1. **Default Path**: Consumers use the current core defaults directly.
2. **Customization Path**: Consumers wrap or extend behavior in their own module instead of mutating global core state.
3. **Future Extraction Rule**: Internal registries or SPIs are introduced only when reuse across multiple declaration projects proves the need.
