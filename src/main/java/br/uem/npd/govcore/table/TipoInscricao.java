package br.uem.npd.govcore.table;

import java.util.Optional;

/**
 * Tabela oficial de tipos de inscrição das declarações governamentais.
 * <p>
 * Códigos padrão Receita Federal:
 * <ul>
 *   <li>1 = CNPJ</li>
 *   <li>2 = CPF</li>
 *   <li>3 = CAEPF</li>
 *   <li>4 = CNO</li>
 *   <li>5 = CGC</li>
 *   <li>6 = CEI</li>
 * </ul>
 * <p>
 * Usado por: eSocial, EFD-Reinf, MIT, DCTFWeb, DEFIS, PGDAS-D, PGMEI.
 */
public enum TipoInscricao {

    CNPJ(1, "CNPJ - Cadastro Nacional da Pessoa Juridica"),
    CPF(2, "CPF - Cadastro de Pessoa Fisica"),
    CAEPF(3, "CAEPF - Cadastro de Atividade Economica de Pessoa Fisica"),
    CNO(4, "CNO - Cadastro Nacional de Obra"),
    CGC(5, "CGC - Cadastro Geral de Contribuinte"),
    CEI(6, "CEI - Cadastro Especifico do INSS");

    private final int code;
    private final String description;

    TipoInscricao(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Optional<TipoInscricao> fromCode(int code) {
        for (TipoInscricao t : values()) {
            if (t.code == code) return Optional.of(t);
        }
        return Optional.empty();
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
