# Data Model: Validator Registry

## Entities

### ValidatorRegistry (Singleton)
- **Role**: Holds the mapping between document types and their respective validation chains.
- **Key Methods**:
  - `register(Class<? extends Validator> validator)`
  - `getValidator(DocumentType type)`
  - `validate(String value, DocumentType type)`

### Validator (Interface)
- **Methods**:
  - `boolean isValid(String value)`
  - `ValidationMetadata getMetadata()`

## Interaction Pattern

1. **Initialization**: `ValidatorRegistry` loads default validators (`CpfValidator`, `NisValidator`, etc.).
2. **Static Delegation**: `GovValidators.isNisStructureValid(nis)` calls `ValidatorRegistry.getInstance().validate(nis, NIS_STRUCTURE)`.
3. **Customization**: A consumer calls `ValidatorRegistry.getInstance().register(new CustomNisValidator())` before calling the static helper.
