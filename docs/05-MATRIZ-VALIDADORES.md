# Matriz de Validadores

Atualizado em 11 de abril de 2026.

## Objetivo

Esta matriz registra o nivel de confianca publicado pelo `declaracoes-gov-core`
para os validadores e checagens estruturais atualmente expostos.

Classificacoes usadas:

- `OFFICIAL`: regra oficial mapeada no core e apta a sustentar fail-fast.
- `PROVISIONAL`: implementacao existente, mas a fonte primaria ainda nao foi
  catalogada no core de forma suficiente para promovela como normativa.
- `STRUCTURAL`: apenas normalizacao, tamanho e forma basica; sem promessa de
  validacao algoritmica oficial.

## Matriz Atual

| Tipo | API principal | Nivel | Fail-fast no core | Fonte/Referencia | Observacoes |
| --- | --- | --- | --- | --- | --- |
| `CNPJ` / `CGC` | `Cnpj`, `GovValidators.isCnpjValid` | `OFFICIAL` | Sim | https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastros/cnpj | Suporte a CNPJ numerico e alfanumerico com DV no core. |
| `CPF` | `Cpf`, `GovValidators.isCpfValid` | `PROVISIONAL` | Hoje sim, por compatibilidade historica | Fonte primaria de DV ainda nao catalogada no core | A implementacao existe, mas a promocao para `OFFICIAL` depende de mapeamento documental explicito. |
| `NIS/PIS/PASEP/NIT` | `Nis`, `GovValidators.isNisValid` | `PROVISIONAL` | Hoje sim, por compatibilidade historica | Fonte primaria de DV ainda nao catalogada no core | A implementacao existe, mas a promocao para `OFFICIAL` depende de mapeamento documental explicito. |
| `CAEPF` | `Caepf`, `GovValidators.isCaepfStructureValid` | `STRUCTURAL` | Nao | https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastros/caepf | O core normaliza digitos e valida apenas o comprimento estrutural de 14 digitos. |
| `CNO` | `Cno`, `GovValidators.isCnoStructureValid` | `STRUCTURAL` | Nao | https://www.gov.br/receitafederal/pt-br/assuntos/construcao-civil/cno | O core normaliza digitos e valida apenas o comprimento estrutural de 12 digitos. |
| `CEI` | `Cei`, `GovValidators.isCeiStructureValid` | `STRUCTURAL` | Nao | https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastros/cei | O core normaliza digitos e valida apenas o comprimento estrutural de 12 digitos. |

## Notas de Uso

- Os tipos estruturais `Caepf`, `Cno` e `Cei` retornam a representacao
  normalizada sem pontuacao em `getFormatted()`, porque o core ainda nao publica
  uma mascara oficial estabilizada para esses documentos.
- Esta matriz descreve o contrato publicado pelo core. Ela nao substitui a
  validacao normativa que um modulo consumidor decida aplicar por fora.
- Antes do freeze de `1.0.0`, `CPF` e `NIS` devem ser ou promovidos para
  `OFFICIAL` com fonte primaria catalogada, ou rebaixados no comportamento
  fail-fast para refletir integralmente esta politica.
