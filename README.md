# declaracoes-gov-core

Reator Maven e fundação Java 11 da linha `v1.1.x` do ecossistema `declaracoes-*`. Este diretório mantém o parent `br.uem.npd:declaracoes-gov-core-parent:1.1.0-SNAPSHOT` e os módulos manuais reutilizados por leiautes, transmissores e consumidores Java.

## Papel no portfólio

- concentrar tipos brasileiros, validadores e exceções transversais;
- separar formatação, XML, criptografia e transporte neutro em módulos opcionais;
- publicar um BOM interno (`declaracoes-gov-core-bom`) para consumidores que precisam só da fundação.

## Módulos do reator

| Artefato | Papel atual |
| --- | --- |
| `declaracoes-gov-core-bom` | `dependencyManagement` do próprio core; não contém código Java. |
| `declaracoes-gov-core-domain` | value objects, catálogo de validação, metadados de leiaute, enums e exceções-base. |
| `declaracoes-gov-core-format` | normalização textual, formatos numéricos e de competência, datas XML, `GovJsonFactory` e parsers/serializers delimitados e posicionais. |
| `declaracoes-gov-core-crypto` | abstrações de certificado (`CertificateProvider`), PKCS#12, PKCS#11 e `SSLContext`. |
| `declaracoes-gov-core-xml` | parsing XML seguro, utilitários DOM, `XmlSigner`, `XmlDsigSigner` e `XmlSignatureOptions`. |
| `declaracoes-gov-core-transport` | SPI HTTP neutra, retry/policy, proxy e implementação padrão com Apache HttpClient 5 para transmissores. |

## Escopo

Entram no core:

- domínio brasileiro reutilizável (`Cnpj`, `Cpf`, `Nis`, `PeriodoApuracao`, `Vigencia`, `CodigoMunicipio`, `TipoInscricao`, `Uf`);
- política pública de validadores (`OFFICIAL`, `PROVISIONAL`, `STRUCTURAL`);
- utilitários de formatação e serialização voltados ao contexto governamental;
- infraestrutura transversal de XML, certificados e transporte neutro.

Ficam fora do core:

- clientes HTTP/SOAP/REST específicos de declaração;
- OAuth2, filas, polling e orquestração de entrega;
- regras negociais específicas de cada declaração;
- código gerado por esquemas oficiais.

## Consumo pelo Maven

Consumidores do ecossistema normalmente importam o BOM interno do core e escolhem apenas os módulos necessários:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>br.uem.npd</groupId>
            <artifactId>declaracoes-gov-core-bom</artifactId>
            <version>1.1.0-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

```xml
<dependencies>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-domain</artifactId>
    </dependency>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-xml</artifactId>
    </dependency>
    <dependency>
        <groupId>br.uem.npd</groupId>
        <artifactId>declaracoes-gov-core-transport</artifactId>
    </dependency>
</dependencies>
```

## Validação local

Da raiz deste módulo, rode:

```bash
mvn verify
```

Esse comando valida o reator inteiro (`domain`, `format`, `crypto`, `xml`, `transport` e o BOM interno).

## Mapa da documentação

Documentação canônica na raiz:

- `README.md`: visão rápida, escopo e consumo;
- `ONBOARDING.md`: primeiro fluxo local e pontos de entrada;
- `CONTRIBUTING.md`: regras de contribuição e gatilhos de atualização documental;
- `ARCHITECTURE.md`: visão estrutural estável.

Documentação detalhada em `docs/`:

- `docs/01-REQUISITOS.md`
- `docs/02-DESIGN.md`
- `docs/03-PLANO-TESTES.md`
- `docs/04-IMPLANTACAO.md`
- `docs/05-MATRIZ-VALIDADORES.md`
- `docs/06-GUIA-MIGRACAO-0.1.x-1.0.0.md`

Material congelado da formação da linha `1.0.0` fica em `docs/hist/`.
