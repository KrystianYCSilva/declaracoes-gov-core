---
work_package_id: WP01
title: GovNumberConstants + Enums de Domínio (domain)
lane: "planned"
dependencies: []
created_at: '2026-04-24T00:00:00Z'
subtasks:
- T001
- T002
- T003
- T004
- T005
---

# WP01 — GovNumberConstants + Enums de Domínio (domain)

## Objetivo

Criar as constantes numéricas e fiscais centralizadas e os enums de domínio no módulo `declaracoes-gov-core-domain`:

1. `GovNumberConstants` — constantes de arredondamento, escalas e BigDecimal.
2. `RegimeTributario` — enum de regime tributário com código RFB.
3. `SituacaoCadastral` — enum de situação cadastral conforme RFB.

Essas classes são a fundação para WP02 (GovCurrencyFormats) e WP03 (FinancialTypes.kt).

---

## T001 — Criar `GovNumberConstants.java`

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/util/GovNumberConstants.java`

### Instruções

1. Classe `public final class GovNumberConstants` com construtor `private GovNumberConstants()`.
2. Javadoc de classe: `"Constantes numéricas e de arredondamento para cálculos contábeis-fiscais conforme ABNT NBR 5891."`.
3. Todos os campos `public static final`.
4. **Complementa** `GovBigDecimalConstants` — não substitui. Não redeclarar constantes já existentes (ZERO_DECIMAL, UM, DOIS, DEZ, CEM).
5. Java 8 apenas — sem `var`, sem `List.of()`.

### Imports necessários

```java
import java.math.BigDecimal;
import java.math.RoundingMode;
```

### Constantes RoundingMode

```java
/** Arredondamento fiscal conforme ABNT NBR 5891 (Banker's Rounding). */
public static final RoundingMode ROUNDING_FISCAL    = RoundingMode.HALF_EVEN;
/** Truncamento — para campos sem arredondamento. */
public static final RoundingMode ROUNDING_TRUNCATE  = RoundingMode.DOWN;
/** Arredondamento comercial (legado — preferir HALF_EVEN para uso fiscal). */
public static final RoundingMode ROUNDING_COMERCIAL = RoundingMode.HALF_UP;
```

### Constantes de escala

```java
/** Escala para valores monetários em BRL (centavos). */
public static final int SCALE_MONETARIO      = 2;
/** Escala para alíquotas tributárias (ex: 4.5000%). */
public static final int SCALE_ALIQUOTA       = 4;
/** Escala para percentuais de exibição (ex: 15,00%). */
public static final int SCALE_PERCENTUAL     = 2;
/** Escala para quantidades fiscais (NF-e/SPED). */
public static final int SCALE_QUANTIDADE     = 4;
/** Escala para preço unitário tributável (vUnitTrib NF-e: até 10 casas). */
public static final int SCALE_PRECO_UNITARIO = 10;
```

### Constantes BigDecimal (complemento a GovBigDecimalConstants)

```java
public static final BigDecimal TRES             = new BigDecimal("3");
public static final BigDecimal QUATRO           = new BigDecimal("4");
public static final BigDecimal CINCO            = new BigDecimal("5");
public static final BigDecimal SEIS             = new BigDecimal("6");
public static final BigDecimal SETE             = new BigDecimal("7");
public static final BigDecimal OITO             = new BigDecimal("8");
public static final BigDecimal NOVE             = new BigDecimal("9");
public static final BigDecimal ONZE             = new BigDecimal("11");
public static final BigDecimal DOZE             = new BigDecimal("12");
public static final BigDecimal QUINZE           = new BigDecimal("15");
public static final BigDecimal VINTE            = new BigDecimal("20");
public static final BigDecimal VINTE_CINCO      = new BigDecimal("25");
public static final BigDecimal TRINTA           = new BigDecimal("30");
public static final BigDecimal CINQUENTA        = new BigDecimal("50");
public static final BigDecimal MIL              = new BigDecimal("1000");
public static final BigDecimal MENOS_UM         = new BigDecimal("-1");
```

### Alíquotas federais nominais

```java
// IMPORTANTE: constantes de conveniência — não substituem tabela dinâmica de vigências
public static final BigDecimal ALIQUOTA_IRPJ               = new BigDecimal("15.00");
public static final BigDecimal ALIQUOTA_IRPJ_ADICIONAL     = new BigDecimal("10.00");
public static final BigDecimal ALIQUOTA_CSLL_LP            = new BigDecimal("9.00");
public static final BigDecimal ALIQUOTA_CSLL_IF            = new BigDecimal("15.00");
public static final BigDecimal ALIQUOTA_PIS_CUMULATIVO     = new BigDecimal("0.65");
public static final BigDecimal ALIQUOTA_PIS_NAO_CUMULATIVO = new BigDecimal("1.65");
public static final BigDecimal ALIQUOTA_COFINS_CUMULATIVO     = new BigDecimal("3.00");
public static final BigDecimal ALIQUOTA_COFINS_NAO_CUMULATIVO = new BigDecimal("7.60");
public static final BigDecimal ALIQUOTA_INSS_PATRONAL      = new BigDecimal("20.00");
public static final BigDecimal ALIQUOTA_ISS_MINIMO         = new BigDecimal("2.00");
```

---

## T002 — Criar `GovNumberConstantsTest.java`

**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/util/GovNumberConstantsTest.java`

### Instruções

1. JUnit 4, `Assert.*` importado estaticamente.
2. Sem Mockito.

### Testes obrigatórios

```java
@Test
public void roundingFiscalDeveSerHalfEven() {
    assertEquals(RoundingMode.HALF_EVEN, GovNumberConstants.ROUNDING_FISCAL);
}

@Test
public void scaleMonetarioDeveSer2() {
    assertEquals(2, GovNumberConstants.SCALE_MONETARIO);
}

@Test
public void scaleAliquotaDeveSer4() {
    assertEquals(4, GovNumberConstants.SCALE_ALIQUOTA);
}

@Test
public void abntHalfEven_2_5_arredondaPara2() {
    // ABNT NBR 5891: 2.5 arredonda para 2 (par mais próximo), não 3
    BigDecimal result = new BigDecimal("2.5").setScale(0, GovNumberConstants.ROUNDING_FISCAL);
    assertEquals(new BigDecimal("2"), result);
}

@Test
public void abntHalfEven_3_5_arredondaPara4() {
    // 3.5 arredonda para 4 (par mais próximo)
    BigDecimal result = new BigDecimal("3.5").setScale(0, GovNumberConstants.ROUNDING_FISCAL);
    assertEquals(new BigDecimal("4"), result);
}

@Test
public void aliquotaIrpjDeve15() {
    assertEquals(new BigDecimal("15.00"), GovNumberConstants.ALIQUOTA_IRPJ);
}

@Test
public void aliquotaPisCumulativo_0_65() {
    assertEquals(new BigDecimal("0.65"), GovNumberConstants.ALIQUOTA_PIS_CUMULATIVO);
}
```

---

## T003 — Criar `RegimeTributario.java`

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/table/RegimeTributario.java`

### Instruções

1. Enum com campos `int codigo` e `String descricao`.
2. Construtor package-private.
3. Método `static Optional<RegimeTributario> fromCodigo(int codigo)` — busca linear em `values()`.
4. Javadoc de enum: `"Regime tributário conforme classificação SPED/eSocial/PGDAS-D."`.
5. Imports: `java.util.Optional`.

```java
public enum RegimeTributario {
    SIMPLES_NACIONAL(1, "Simples Nacional"),
    SIMPLES_NACIONAL_EXCESSO(2, "Simples Nacional - Excesso de sublimite"),
    LUCRO_PRESUMIDO(3, "Lucro Presumido"),
    LUCRO_REAL(4, "Lucro Real"),
    LUCRO_ARBITRADO(5, "Lucro Arbitrado"),
    MEI(6, "Microempreendedor Individual"),
    IMUNE(7, "Imune"),
    ISENTA(8, "Isenta");

    private final int codigo;
    private final String descricao;

    RegimeTributario(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }

    public static Optional<RegimeTributario> fromCodigo(int codigo) {
        for (RegimeTributario r : values()) {
            if (r.codigo == codigo) return Optional.of(r);
        }
        return Optional.empty();
    }
}
```

---

## T004 — Criar `SituacaoCadastral.java`

**Arquivo:** `declaracoes-gov-core-domain/src/main/java/br/com/contabilizei/obrigacoes/govcore/table/SituacaoCadastral.java`

### Instruções

Mesmo padrão que `RegimeTributario`. Javadoc: `"Situação cadastral do CNPJ conforme Receita Federal do Brasil."`.

```java
public enum SituacaoCadastral {
    NULA(1, "Nula"),
    ATIVA(2, "Ativa"),
    SUSPENSA(3, "Suspensa"),
    INAPTA(4, "Inapta"),
    BAIXADA(8, "Baixada");

    private final int codigo;
    private final String descricao;

    SituacaoCadastral(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }

    public static Optional<SituacaoCadastral> fromCodigo(int codigo) {
        for (SituacaoCadastral s : values()) {
            if (s.codigo == codigo) return Optional.of(s);
        }
        return Optional.empty();
    }
}
```

---

## T005 — Criar testes para enums

**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/table/RegimeTributarioTest.java`
**Arquivo:** `declaracoes-gov-core-domain/src/test/java/br/com/contabilizei/obrigacoes/govcore/table/SituacaoCadastralTest.java`

### Testes obrigatórios para RegimeTributario

```java
@Test
public void fromCodigo_1_retornaSimples() {
    assertEquals(Optional.of(RegimeTributario.SIMPLES_NACIONAL),
            RegimeTributario.fromCodigo(1));
}

@Test
public void fromCodigo_desconhecido_retornaEmpty() {
    assertEquals(Optional.empty(), RegimeTributario.fromCodigo(99));
}

@Test
public void todosOsCodigosResolvem() {
    // Garante que fromCodigo funciona para todos os valores declarados
    for (RegimeTributario r : RegimeTributario.values()) {
        assertEquals(Optional.of(r), RegimeTributario.fromCodigo(r.getCodigo()));
    }
}

@Test
public void lucroPressumidoTemCodigo3() {
    assertEquals(3, RegimeTributario.LUCRO_PRESUMIDO.getCodigo());
}
```

### Testes obrigatórios para SituacaoCadastral

```java
@Test
public void fromCodigo_2_retornaAtiva() {
    assertEquals(Optional.of(SituacaoCadastral.ATIVA),
            SituacaoCadastral.fromCodigo(2));
}

@Test
public void fromCodigo_8_retornaBaixada() {
    assertEquals(Optional.of(SituacaoCadastral.BAIXADA),
            SituacaoCadastral.fromCodigo(8));
}

@Test
public void fromCodigo_desconhecido_retornaEmpty() {
    assertEquals(Optional.empty(), SituacaoCadastral.fromCodigo(0));
}
```

---

## Checklist de Validação

- [ ] `GovNumberConstants.ROUNDING_FISCAL` == `RoundingMode.HALF_EVEN`
- [ ] Teste ABNT: `2.5` arredonda para `2` e `3.5` arredonda para `4`
- [ ] `RegimeTributario` possui exatamente 8 valores (SIMPLES_NACIONAL a ISENTA)
- [ ] `SituacaoCadastral` possui exatamente 5 valores (NULA a BAIXADA)
- [ ] `fromCodigo(99)` retorna `Optional.empty()` para ambos os enums
- [ ] `GovNumberConstants` **não** declara ZERO, UM, DOIS, DEZ, CEM (já em GovBigDecimalConstants)
- [ ] `mvn -q verify` no módulo `declaracoes-gov-core-domain` passa sem erros
- [ ] JaCoCo 90% linha e 90% branch

## Review Feedback

TBD

