# Guia de migração da linha 0.1.x para 1.0.0

## 1. Quando este guia se aplica

Use este guia se o consumidor ainda depende da versão monolítica anterior ao reator `declaracoes-gov-core-parent`.

## 2. O que mudou na estrutura

Antes, um único artefato reunia domínio, formatação, XML e criptografia.

Agora, a linha `1.0.0` foi separada em:

- `declaracoes-gov-core-domain`
- `declaracoes-gov-core-format`
- `declaracoes-gov-core-crypto`
- `declaracoes-gov-core-xml`
- `declaracoes-gov-core-bom`

## 3. Ajustes de contrato que exigem atenção

### 3.1 CPF e NIS

- `Cpf.of(...)` e `Nis.of(...)` ficaram estruturais.
- O algoritmo legado continua disponível em `ofProvisionallyValidated(...)`.
- Os utilitários diretos seguem a mesma separação entre estrutural e provisório.

### 3.2 XML assinado

- `XmlDsigSigner` continua existindo.
- O caminho preferido para alvo explícito passa por `XmlSignatureOptions`.

### 3.3 SSL e certificados

- `CertificateProvider`, `Pkcs12Provider`, `Pkcs11Provider` e `SslContextBuilder` vivem em módulo dedicado (`declaracoes-gov-core-crypto`).

## 4. Passos recomendados

1. importar `declaracoes-gov-core-bom`;
2. substituir a dependência monolítica pelos módulos mínimos necessários;
3. revisar todo uso de `Cpf` e `Nis` que esperava DV forte no construtor padrão;
4. revisar integrações XML para usar `XmlSignatureOptions` quando o alvo de assinatura não for trivial;
5. validar o projeto consumidor com `mvn verify`.

## 5. Exemplo de adoção

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>br.uem.npd</groupId>
            <artifactId>declaracoes-gov-core-bom</artifactId>
            <version>1.0.0</version>
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
</dependencies>
```

## 6. Checklist de encerramento

- o consumidor importa apenas os módulos necessários;
- o uso de `CPF` e `NIS` está alinhado com a política `PROVISIONAL` do core;
- qualquer assinatura XML com alvo específico usa `XmlSignatureOptions`;
- o build consumidor continua compatível com Java 8.
