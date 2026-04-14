package br.uem.npd.govcore.validator;

import br.uem.npd.govcore.table.TipoInscricao;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Catalogo publico do nivel de confianca dos validadores expostos pelo core.
 */
public final class GovValidationCatalog {

    private static final ValidationMetadata CNPJ = new ValidationMetadata(
        "CNPJ",
        "CNPJ/CGC",
        ValidationLevel.OFFICIAL,
        "https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastros/cnpj",
        "Validacao algoritmica oficial suportada pelo core para CNPJ numerico e alfanumerico."
    );

    private static final ValidationMetadata CPF = new ValidationMetadata(
        "CPF",
        "CPF",
        ValidationLevel.PROVISIONAL,
        "Fonte primaria de digito verificador ainda nao catalogada no core.",
        "O core publica checagem estrutural por padrao e mantem a validacao algoritmica apenas por opt-in provisório."
    );

    private static final ValidationMetadata NIS = new ValidationMetadata(
        "NIS",
        "NIS/PIS/PASEP/NIT",
        ValidationLevel.PROVISIONAL,
        "Fonte primaria de digito verificador ainda nao catalogada no core.",
        "O core publica checagem estrutural por padrao e mantem a validacao algoritmica apenas por opt-in provisório."
    );

    private static final ValidationMetadata CAEPF = new ValidationMetadata(
        "CAEPF",
        "CAEPF",
        ValidationLevel.STRUCTURAL,
        "https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastros/caepf",
        "O core aplica apenas normalizacao numerica e checagem estrutural de 14 digitos."
    );

    private static final ValidationMetadata CNO = new ValidationMetadata(
        "CNO",
        "CNO",
        ValidationLevel.STRUCTURAL,
        "https://www.gov.br/receitafederal/pt-br/assuntos/construcao-civil/cno",
        "O core aplica apenas normalizacao numerica e checagem estrutural de 12 digitos."
    );

    private static final ValidationMetadata CEI = new ValidationMetadata(
        "CEI",
        "CEI",
        ValidationLevel.STRUCTURAL,
        "https://www.gov.br/receitafederal/pt-br/assuntos/orientacao-tributaria/cadastros/cei",
        "O core aplica apenas normalizacao numerica e checagem estrutural de 12 digitos."
    );

    private static final Map<TipoInscricao, ValidationMetadata> INSCRICOES;
    private static final List<ValidationMetadata> ALL;

    static {
        EnumMap<TipoInscricao, ValidationMetadata> inscricoes = new EnumMap<>(TipoInscricao.class);
        inscricoes.put(TipoInscricao.CNPJ, CNPJ);
        inscricoes.put(TipoInscricao.CGC, CNPJ);
        inscricoes.put(TipoInscricao.CPF, CPF);
        inscricoes.put(TipoInscricao.CAEPF, CAEPF);
        inscricoes.put(TipoInscricao.CNO, CNO);
        inscricoes.put(TipoInscricao.CEI, CEI);
        INSCRICOES = Collections.unmodifiableMap(inscricoes);

        ALL = Collections.unmodifiableList(Arrays.asList(CNPJ, CPF, NIS, CAEPF, CNO, CEI));
    }

    private GovValidationCatalog() {
        // Prevents instantiation
    }

    /**
     * Performs the for inscricao operation.
     *
     * @param tipo the tipo
     * @return the validation metadata
     */
    public static ValidationMetadata forInscricao(TipoInscricao tipo) {
        return tipo == null ? null : INSCRICOES.get(tipo);
    }

    /**
     * Performs the cpf operation.
     * @return the validation metadata
     */
    public static ValidationMetadata cpf() {
        return CPF;
    }

    /**
     * Performs the nis operation.
     * @return the validation metadata
     */
    public static ValidationMetadata nis() {
        return NIS;
    }

    /**
     * Performs the all operation.
     * @return the list
     */
    public static List<ValidationMetadata> all() {
        return ALL;
    }
}
