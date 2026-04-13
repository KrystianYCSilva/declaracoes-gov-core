package br.uem.npd.govcore.model;

import java.io.Serializable;

/**
 * Interface polimórfica que permite tratar diferentes tipos de inscrição
 * (CNPJ, CPF, CNO, CAEPF) sob o mesmo contrato quando agem
 * como titulares de uma declaração governamental.
 */
@Deprecated
public interface IdentificadorEmpregador extends InscricaoGovernamental, Serializable {
}
