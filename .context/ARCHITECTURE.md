# Architecture — declaracoes-gov-core

## Overview

Multi-module foundation library providing shared domain models, text-format parsing/serialization, XML digital signature, and cryptographic utilities for all declarações modules.

## Structure

```
declaracoes-gov-core-domain/     → br.uem.npd.govcore.model, .exception, .table, .validator
declaracoes-gov-core-format/     → br.uem.npd.govcore.format (.parser), .util
declaracoes-gov-core-crypto/     → br.uem.npd.govcore.crypto
declaracoes-gov-core-xml/        → br.uem.npd.govcore.signature, .util
declaracoes-gov-core-bom/        → BOM for core modules
```

## Modules

- **core-domain** — Base model interfaces (`Layout`, `Registro`), exception hierarchy, table enums, validators.
- **core-format** — Pipe-delimited and positional text format parser/serializer engine.
- **core-crypto** — ICP-Brasil A1/A3 certificate handling, PKCS#12 key stores.
- **core-xml** — XML digital signature (`Signature`, `SignedInfo`, `Reference`) and XML utilities.

## Format

- Text-format engine supports pipe-delimited (`|`) and positional (fixed-width) layouts.
- XML module handles Enveloped/Detached XML Signature per W3C XMLDSig.

## Legal basis

- ICP-Brasil certificate standards (DOC-ICP-04).
- W3C XML Signature Syntax and Processing.
