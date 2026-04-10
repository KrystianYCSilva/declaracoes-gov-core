# Plano de Testes - v1.0.0

## 1. Estrategia de Testes

### 1.1 Niveis de teste
- **Unitarios**: classes isoladas, algoritmos, enums, parsers e formatadores
- **Integracao leve**: interacao entre value objects, validadores, XML e crypto
- **Contratuais**: garantem semantica publica da API e classificacao dos validadores
- **Concorrencia**: validam thread-safety de componentes compartilhados

### 1.2 Framework atual
- JUnit 4.13.2
- Mockito 4.11.0
- BouncyCastle em escopo de teste
- JaCoCo para cobertura

### 1.3 Baseline atual observado
- 17 suites de teste
- 47 testes executados
- 0 falhas e 0 erros no baseline local
- cobertura agregada atual do report completo: `71.63%` de instrucoes e `53.23%` de branches

### 1.4 Meta da v1.0.0
- cobertura minima de `90%` em linhas e branches no codigo mantido pelo projeto;
- sem exclusoes amplas para `crypto/` e `signature/`;
- excecoes apenas para codigo gerado ou casos tecnicamente justificados e documentados.

---

## 2. Casos de Teste por Componente

### 2.1 Dominio e validadores

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| DV-01 | CNPJ numerico valido | Positivo | objeto/validacao aceita |
| DV-02 | CNPJ alfanumerico com vetor oficial | Positivo | DV correto |
| DV-03 | CNPJ com DV errado | Negativo | rejeicao |
| DV-04 | CPF valido com e sem mascara | Positivo | aceita |
| DV-05 | CPF homogeneo | Negativo | rejeicao |
| DV-06 | NIS valido | Positivo | aceita |
| DV-07 | `PeriodoApuracao` em formatos suportados | Positivo | parse correto |
| DV-08 | `PeriodoApuracao` invalido | Negativo | excecao |
| DV-09 | `CodigoMunicipio` com 7 digitos | Positivo | aceita |
| DV-10 | `CodigoMunicipio` com tamanho incorreto | Negativo | excecao |
| DV-11 | `Recibo` em formato valido | Positivo | aceita |
| DV-12 | `Uf.fromSigla()` com sigla valida e invalida | Positivo/Negativo | optional coerente |

### 2.2 Politica de validadores

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| PV-01 | Documento com validacao oficial | Contratual | fail-fast permitido |
| PV-02 | Documento estrutural | Contratual | apenas formato/tamanho sao exigidos |
| PV-03 | Documento com algoritmo provisorio | Contratual | validacao opt-in e documentada |
| PV-04 | Matriz de confiabilidade publicada | Documental | tipo classificado como oficial/provisorio/estrutural |

### 2.3 Modulo11 compartilhado

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| M11-01 | Vetor oficial de CNPJ alfanumerico | Positivo | DV esperado |
| M11-02 | CPF com pesos oficiais | Positivo | DV esperado |
| M11-03 | NIS com pesos oficiais | Positivo | DV esperado |
| M11-04 | charToValue para faixa numerica e alfanumerica | Positivo | conversao correta |
| M11-05 | entrada invalida | Negativo | falha clara |

### 2.4 Formatacao e normalizacao

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| FM-01 | mascara de CNPJ | Positivo | formato correto |
| FM-02 | desmascaramento | Positivo | apenas caracteres relevantes |
| FM-03 | uppercase/sanitizacao | Positivo | normalizacao consistente |
| FM-04 | `BigDecimal` para representacao governamental | Positivo | sem notacao cientifica |
| FM-05 | round-trip documentado de `BigDecimal` | Integracao | comportamento previsivel |

### 2.5 XML

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| XML-01 | parse de XML valido | Positivo | DOM criado |
| XML-02 | XML invalido | Negativo | excecao clara |
| XML-03 | assinatura com atributo ID explicito | Positivo | `<ds:Signature>` presente |
| XML-04 | XML ja assinado | Borda | comportamento idempotente ou erro documentado |
| XML-05 | assinatura sem alvo configurado corretamente | Negativo | falha clara |

### 2.6 Crypto

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| CR-01 | carga de PKCS12 valido | Positivo | certificado e chave disponiveis |
| CR-02 | senha incorreta | Negativo | excecao clara |
| CR-03 | alias preferido inexistente | Negativo | excecao clara |
| CR-04 | `SSLContext` com provider valido | Positivo | contexto criado |
| CR-05 | cenarios de erro A3/PKCS11 | Negativo | mensagens operacionais compreensiveis |

### 2.7 Concorrencia

| ID | Caso | Tipo | Resultado esperado |
|----|------|------|--------------------|
| CC-01 | criacao concorrente de VOs | Concorrencia | sem corrupcao de estado |
| CC-02 | uso concorrente de validadores | Concorrencia | resultados consistentes |
| CC-03 | uso concorrente de `GovJsonFactory` e utilitarios XML | Concorrencia | sem falhas espurias |

---

## 3. Matriz de Rastreabilidade

| Requisito | Suites/casos principais |
|-----------|-------------------------|
| RF-03 | DV-01 a DV-12 |
| RF-04 | PV-01 a PV-04 |
| RF-05 | PV-02, PV-03 |
| RF-06 | FM-01 a FM-05 |
| RF-07 | XML-01 a XML-05 |
| RF-08 | CR-01 a CR-05 |
| RF-09 | M11-01 a M11-05 |
| RNF-03 | CC-01 a CC-03 |
| RNF-04 | JaCoCo + suites completas |
| RNF-05 | PV-01 a PV-04 + revisao documental |

---

## 4. Divida de Testes do Baseline

### 4.1 Riscos mais relevantes
- `crypto/` ainda esta abaixo do nivel exigido para um artefato fundacional;
- `signature/` ainda nao possui cobertura compativel com a criticidade funcional;
- o gate atual de cobertura nao representa a meta real da `v1.0.0`;
- faltam testes explicitos para a politica de validadores e para os contratos estruturais.

### 4.2 Acoes obrigatorias
- criar infraestrutura de teste de certificado para A1 e cenarios controlados de A3;
- adicionar testes especificos para `XmlDsigSigner`;
- cobrir `Vigencia` e branches faltantes dos value objects atuais;
- validar round-trip e contrato explicito do JSON governamental;
- remover a dependencia de exclusoes amplas como mecanismo de "sucesso" do gate.

---

## 5. Criterios de aceite

- nenhum componente critico do nucleo fica sem teste representativo;
- `crypto` e `signature` entram no mesmo padrao de exigencia dos demais modulos;
- a classificacao `oficial/provisorio/estrutural` e testada e documentada;
- a cobertura final atende a meta sem maquiagem artificial.
