package br.uem.npd.govcore.validator;

import br.uem.npd.govcore.table.TipoInscricao;

import java.util.Arrays;
import java.util.List;

/**
 * Facade utilitário para facilitar o acesso direto e *stateless* 
 * aos algoritmos de validação do core.
 */
public final class GovValidators {

    // Chains de validação
    private static final List<DocumentValidator> CNPJ_CHAIN = Arrays.asList(
        new NumericCnpjValidator(),
        new AlphanumericCnpjValidator()
    );

    private static final DocumentValidator CPF_VALIDATOR = new CpfValidator();
    private static final DocumentValidator NIS_VALIDATOR = new NisValidator();

    private GovValidators() {
        // Prevents instantiation
    }

    /**
     * Valida um CNPJ iterando pelas estratégias conhecidas (Numérico ou Alfanumérico).
     */
    public static boolean isCnpjValid(String cnpj) {
        return CNPJ_CHAIN.stream().anyMatch(validator -> validator.isValid(cnpj));
    }

    /**
     * Limpa a string de um CNPJ apenas se for válido em alguma estratégia.
     */
    public static String stripCnpjIfValid(String cnpj) {
        return CNPJ_CHAIN.stream()
                .filter(v -> v.isValid(cnpj))
                .findFirst()
                .map(v -> v.strip(cnpj))
                .orElse(null);
    }

    /**
     * Valida um CPF (Módulo 11).
     */
    public static boolean isCpfValid(String cpf) {
        return CPF_VALIDATOR.isValid(cpf);
    }

    /**
     * Valida um NIS/PIS/PASEP/NIT (Módulo 11 específico).
     */
    public static boolean isNisValid(String nis) {
        return NIS_VALIDATOR.isValid(nis);
    }

    /**
     * Valida dinamicamente baseando-se na tabela de tipos do governo.
     * Obs: Validadores de CAEPF, CNO, CEI e CGC podem ser adicionados progressivamente aqui.
     */
    public static boolean isInscricaoValid(TipoInscricao tipo, String numero) {
        if (numero == null) return false;
        switch (tipo) {
            case CNPJ:
            case CGC:
                return isCnpjValid(numero);
            case CPF:
                return isCpfValid(numero);
            // CAEPF, CNO e CEI exigem regras específicas que podem ser criadas futuramente.
            // Por enquanto, aceitamos tamanhos padrões se formatados.
            case CAEPF:
                return numero.replaceAll("[^0-9]", "").length() == 14;
            case CNO:
            case CEI:
                return numero.replaceAll("[^0-9]", "").length() == 12;
            default:
                return false;
        }
    }
}
