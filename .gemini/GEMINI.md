@../AGENTS.md

# Gemini-Specific Notes

- This repository keeps Gemini project memory in `.gemini/GEMINI.md`.
- The Gemini-native Spec Kit entrypoints live in `.gemini/commands/` (if created).
- `.gemini/agents/` is kept only as a repository compatibility layer; do not describe it as an official Gemini auto-discovery surface.
- If Gemini bootstrap is requested, preserve the root `AGENTS.md` as the cross-agent index and update this file instead of creating a root `GEMINI.md`.

## Gemini Focus Areas for This Project

### Certificate Management
When working with certificate code:
- Support both A1 (file-based) and A3 (hardware token) certificates
- Never log private keys or passwords
- Clear password arrays immediately after use with `Arrays.fill()`

### XML Signature
When implementing signature:
- Follow eSocial/EFD-Reinf specifications exactly
- Use Apache XML Security library
- Required transforms: Enveloped + C14N
- Include only the user certificate (EndCertOnly)

### Validation
When working with validators:
- CNPJ must handle both numeric and alphanumeric (2026+) formats
- CPF must reject all-identical digits
- IE validation is state-specific

## Code Style Preferences (Gemini)

- Explicit types over `var` (Java 8 compatibility)
- Final fields where possible
- Builder pattern for complex object construction
- Comprehensive Javadoc on public APIs
