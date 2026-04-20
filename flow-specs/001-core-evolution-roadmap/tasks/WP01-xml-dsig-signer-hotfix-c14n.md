---
work_package_id: WP01
title: XmlDsigSigner — Hotfix C14N Inclusive
lane: "done"
dependencies: []
created_at: '2026-04-20T21:13:24.088051+00:00'
subtasks:
- T001: Adicionar campo `includeC14nTransform` em `XmlSignatureOptions`
- T002: Modificar `XmlDsigSigner.createReference()` para incluir C14N
- T003: Atualizar testes de assinatura XML com cenários C14N
- T004: Verificar alinhamento de versões no BOM
loops_planned_to_doing: "1"
doing_started_at: "2026-04-20T21:16:45.306824+00:00"
loops_doing_to_for_review: "1"
for_review_started_at: "2026-04-20T21:27:34.250317+00:00"
loops_for_review_to_done: "1"
ended_at: "2026-04-20T21:27:59.079194+00:00"
reviewed_by: "krystian.silva_conta"
review_status: "approved"
---

# WP01 — XmlDsigSigner — Hotfix C14N Inclusive

## Context

O `XmlDsigSigner` atual injeta apenas a transformação `ENVELOPED` na referência de assinatura XML. Os portais eSocial e ReInF exigem **ENVELOPED + C14N INCLUSIVE** — a ausência do segundo transform causa o **Erro 142** na submissão de eventos. Como consequência, dois projetos consumidores (`v2/esocial-tombamento` e `obrigacoes-service-reinf`) criaram implementações locais do signer como workaround, quebrando a premissa de fonte-única da biblioteca. Este WP corrige o bug de forma retrocompatível e documenta o comportamento esperado no Javadoc para prevenir regressão futura.

## Constraints

- Java 8 (source/target 1.8)
- No Spring/Jakarta EE/Lombok/Bean Validation
- Javadoc e comentários em português (pt-BR)
- JaCoCo gates: `xml` ≥ 90% linha + ≥ 90% branch
- Todos os value objects: `final`, construtor privado, factory estático, `Serializable` com `serialVersionUID = 1L`
- Run `mvn -B -q verify` from reactor root to validate

## Subtask Inventory

### T001 — Adicionar campo `includeC14nTransform` em `XmlSignatureOptions`

**Objetivo:** Expor controle explícito sobre a inclusão da transformação C14N INCLUSIVE na assinatura, mantendo retrocompatibilidade com o comportamento já existente (agora documentado como bugado por default `true`).

**Passos:**
1. Abrir `declaracoes-gov-core-xml/src/main/java/br/com/contabilizei/obrigacoes/govcore/signature/XmlSignatureOptions.java`
2. Adicionar campo `private boolean includeC14nTransform = true;`
3. Adicionar getter `public boolean isIncludeC14nTransform()`
4. Adicionar setter fluente `public XmlSignatureOptions includeC14nTransform(boolean includeC14nTransform)` (retorna `this`)
5. Adicionar Javadoc em português: "Inclui a transformação C14N INCLUSIVE além da transformação ENVELOPED. eSocial e ReInF exigem ambas as transformações; o padrão {@code true} previne o Erro 142 no portal governamental."

**Arquivos:**
- `declaracoes-gov-core-xml/src/main/java/br/com/contabilizei/obrigacoes/govcore/signature/XmlSignatureOptions.java` — adicionar campo, getter e setter fluente

**Validação:**
- `XmlSignatureOptionsTest` verifica que `new XmlSignatureOptions().isIncludeC14nTransform()` retorna `true`
- Setter fluente retorna a mesma instância (encadeamento)

**Edge cases:**
- Default `true` é breaking change intencional — o comportamento anterior era o bug; documentar no Javadoc que `false` só deve ser usado para XML fora do padrão eSocial/ReInF

---

### T002 — Modificar `XmlDsigSigner.createReference()` para incluir C14N

**Objetivo:** Quando `options.isIncludeC14nTransform()` for `true`, criar dois transforms (`ENVELOPED` + `CanonicalizationMethod.INCLUSIVE`) na referência de assinatura; quando `false`, manter comportamento anterior com apenas `ENVELOPED`.

**Passos:**
1. Abrir `declaracoes-gov-core-xml/src/main/java/br/com/contabilizei/obrigacoes/govcore/signature/XmlDsigSigner.java`
2. Localizar o método `createReference()` (ou equivalente que constrói a lista de transforms)
3. Verificar a API do Apache Santuario (`org.apache.xml.security.transforms.Transforms`) para adicionar `Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS` ou `CanonicalizationMethod.INCLUSIVE`
4. Quando `options.isIncludeC14nTransform() == true`, adicionar transform `CanonicalizationMethod.INCLUSIVE` após `ENVELOPED`
5. Adicionar comentário em português explicando a ordem dos transforms exigida pelo portal
6. Garantir que quando `false`, apenas `ENVELOPED` é incluído (retrocompatibilidade)

**Arquivos:**
- `declaracoes-gov-core-xml/src/main/java/br/com/contabilizei/obrigacoes/govcore/signature/XmlDsigSigner.java` — modificar método de criação de referência/transforms

**Validação:**
- Assinar um documento XML real e inspecionar o XML resultante para confirmar presença de ambos os transforms
- `XmlDsigSignerTest` verifica a estrutura da referência gerada

**Edge cases:**
- A ordem dos transforms importa: `ENVELOPED` deve vir antes de `C14N INCLUSIVE`
- Garantir que o namespace `http://www.w3.org/TR/2001/REC-xml-c14n-20010315` está corretamente declarado no documento gerado

---

### T003 — Atualizar testes de assinatura XML com cenários C14N

**Objetivo:** Garantir regressão zero verificando que a referência gerada contém exatamente os dois transforms esperados com `includeC14nTransform=true`, e apenas `ENVELOPED` com `false`.

**Passos:**
1. Abrir `declaracoes-gov-core-xml/src/test/java/.../XmlDsigSignerTest.java`
2. Adicionar teste `assinarComC14nInclusive_deveConterDoisTransforms()`:
   - Criar `XmlSignatureOptions` com `includeC14nTransform(true)` (default)
   - Assinar documento XML de teste
   - Parsear o XML resultante e verificar que `<ds:Transform>` contém `ENVELOPED` e `C14N INCLUSIVE`
3. Adicionar teste `assinarSemC14n_deveConterApenasTransformEnveloped()`:
   - Criar `XmlSignatureOptions` com `includeC14nTransform(false)`
   - Assinar e verificar que apenas `ENVELOPED` está presente
4. Se `XmlSignatureOptionsTest` não existir, criar com testes do getter/setter

**Arquivos:**
- `declaracoes-gov-core-xml/src/test/java/br/com/contabilizei/obrigacoes/govcore/signature/XmlDsigSignerTest.java` — adicionar testes de regressão C14N
- `declaracoes-gov-core-xml/src/test/java/br/com/contabilizei/obrigacoes/govcore/signature/XmlSignatureOptionsTest.java` — criar se não existir

**Validação:**
- `mvn -B -q verify -pl declaracoes-gov-core-xml` passa sem erros
- JaCoCo gate ≥ 90% linha + branch no módulo `xml`

**Edge cases:**
- Teste deve usar certificado de teste do `crypto` test-jar (BouncyCastle) — não criar certificado real
- Verificar que a assinatura gerada é verificável pelo próprio `XmlDocuments`

---

### T004 — Verificar alinhamento de versões no BOM

**Objetivo:** Garantir que o `declaracoes-gov-core-bom/pom.xml` está alinhado com qualquer incremento de versão derivado deste WP.

**Passos:**
1. Verificar versão atual em `declaracoes-gov-core-bom/pom.xml` e `pom.xml` raiz
2. Se versão `1.1.0-SNAPSHOT` se mantém, nenhuma ação necessária
3. Se for necessário incrementar versão (e.g., patch `1.0.1-SNAPSHOT`), atualizar `<version>` em todos os módulos do reactor
4. Executar `mvn -B -q verify` do reactor root para confirmar build verde

**Arquivos:**
- `declaracoes-gov-core-bom/pom.xml` — verificar versão (somente leitura se não mudar)
- `pom.xml` (raiz) — verificar versão do reactor

**Validação:**
- `mvn -B -q verify` verde no reactor completo
- Nenhum conflito de versão entre módulos

**Edge cases:**
- Se consumidores externos estão usando `1.0.x`, este hotfix deve ser lançado como patch para não quebrar imports por versão

---

## Implement Command

```bash
# When dependencies are ready:
flow agent feature implement --feature 001-core-evolution-roadmap --wp WP01
```

## Activity Log

- 2026-04-20T21:16:45Z – unknown – lane=doing – Moved to doing
- 2026-04-20T21:27:34Z – unknown – lane=for_review – Moved to for_review
- 2026-04-20T21:27:59Z – unknown – lane=done – Moved to done
