package br.com.contabilizei.obrigacoes.govcore.model.contract;

public interface Cnpj extends Documento {

    String getBase();

    default String formatter() {

    }

    String onlyNumbers();
}
