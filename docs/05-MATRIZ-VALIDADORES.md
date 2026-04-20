# Matriz de validadores do declaracoes-gov-core

## Fonte de verdade

A matriz abaixo resume o que está implementado hoje em:

- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/GovValidationCatalog.java`
- `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/validator/GovValidators.java`

## Classificações

- `OFFICIAL`: algoritmo e fonte registrada no core permitem fail-fast forte.
- `PROVISIONAL`: existe algoritmo disponível, mas a fonte primária ainda não está catalogada no core.
- `STRUCTURAL`: o core garante apenas normalização, tamanho e forma básica.

## Matriz atual

| Tipo | APIs atuais | Nível | Comportamento padrão | Fonte registrada | Observações |
| --- | --- | --- | --- | --- | --- |
| `CNPJ` / `CGC` | `Cnpj.of`, `GovValidators.isCnpjValid`, `GovValidators.stripCnpjIfValid` | `OFFICIAL` | validação forte habilitada | URL gov.br cadastrada no catálogo | cobre CNPJ numérico e alfanumérico |
| `CPF` | `Cpf.of`, `Cpf.ofProvisionallyValidated`, `GovValidators.isCpfStructureValid`, `GovValidators.isCpfProvisionallyValid` | `PROVISIONAL` | criação padrão estrutural; algoritmo apenas por opt-in | catálogo registra ausência de fonte primária suficiente | `GovValidators.isCpfValid` permanece apenas como alias deprecated do caminho provisório |
| `NIS/PIS/PASEP/NIT` | `Nis.of`, `Nis.ofProvisionallyValidated`, `GovValidators.isNisStructureValid`, `GovValidators.isNisProvisionallyValid` | `PROVISIONAL` | criação padrão estrutural; algoritmo apenas por opt-in | catálogo registra ausência de fonte primária suficiente | `GovValidators.isNisValid` permanece deprecated |
| `CAEPF` | `Caepf`, `GovValidators.isCaepfStructureValid` | `STRUCTURAL` | apenas normalização e 14 dígitos | URL gov.br cadastrada no catálogo | sem DV publicado pelo core |
| `CNO` | `Cno`, `GovValidators.isCnoStructureValid` | `STRUCTURAL` | apenas normalização e 12 dígitos | URL gov.br cadastrada no catálogo | sem algoritmo oficial mapeado |
| `CEI` | `Cei`, `GovValidators.isCeiStructureValid` | `STRUCTURAL` | apenas normalização e 12 dígitos | URL gov.br cadastrada no catálogo | documento legado mantido de forma estrutural |

## Notas de uso

- `GovValidators.isInscricaoValid(...)` retorna validação forte apenas para tipos com algoritmo oficial hoje suportado no core.
- `GovValidators.isInscricaoStructureValid(...)` é o caminho genérico quando o consumidor precisa apenas da checagem estrutural.
- Se o nível de confiança de um documento mudar, sincronize código, testes e esta matriz no mesmo change set.
