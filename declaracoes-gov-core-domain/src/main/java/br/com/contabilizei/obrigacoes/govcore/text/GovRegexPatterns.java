package br.com.contabilizei.obrigacoes.govcore.text;

import java.util.regex.Pattern;

/**
 * Catálogo estático de expressões regulares pré-compiladas para validação estrutural básica e sanitização de texto.
 * Focado no domínio governamental e fiscal brasileiro.
 */
public final class GovRegexPatterns {

    private GovRegexPatterns() {
        // Classe utilitária, não instanciável.
    }

    /** E-mail padrão RFC 5322 simplificado. */
    public static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /** CEP (8 dígitos numéricos). */
    public static final Pattern CEP = Pattern.compile("^\\d{8}$");

    /** Telefone Brasileiro (10 ou 11 dígitos numéricos, incluindo DDD). */
    public static final Pattern TELEFONE_BR = Pattern.compile("^\\d{10,11}$");

    /** CNPJ (14 caracteres, aceita o novo padrão alfanumérico da Receita Federal 2026). */
    public static final Pattern CNPJ = Pattern.compile("^[A-Za-z0-9]{14}$");

    /** CPF (11 dígitos numéricos). */
    public static final Pattern CPF = Pattern.compile("^\\d{11}$");

    /** NIS/PIS/PASEP (11 dígitos numéricos). */
    public static final Pattern NIS_PIS_PASEP = Pattern.compile("^\\d{11}$");

    /** CEI, CNO ou CAEPF (12 a 14 dígitos numéricos). */
    public static final Pattern CEI_CNO_CAEPF = Pattern.compile("^\\d{12,14}$");

    /** Passaporte (Alfanumérico, 1 a 20 caracteres). */
    public static final Pattern PASSAPORTE = Pattern.compile("^[A-Za-z0-9]{1,20}$");

    /** Título de Eleitor (12 dígitos numéricos). */
    public static final Pattern TITULO_ELEITOR = Pattern.compile("^\\d{12}$");

    /** CNH (11 dígitos numéricos). */
    public static final Pattern CNH = Pattern.compile("^\\d{11}$");

    /** RG (Alfanumérico, aceita pontos e hifens, 2 a 20 caracteres). */
    public static final Pattern RG = Pattern.compile("^[A-Za-z0-9.\\-]{2,20}$");

    /** Chave de Acesso (44 dígitos numéricos - NFe, CTe, MDFe). */
    public static final Pattern CHAVE_ACESSO = Pattern.compile("^\\d{44}$");

    /** CFOP (Inicia com 1-7 seguido de 3 dígitos numéricos). */
    public static final Pattern CFOP = Pattern.compile("^[1-7]\\d{3}$");

    /** CST (2 ou 3 dígitos numéricos). */
    public static final Pattern CST = Pattern.compile("^\\d{2,3}$");

    /** CNAE (7 dígitos numéricos). */
    public static final Pattern CNAE = Pattern.compile("^\\d{7}$");

    /** NCM (8 dígitos numéricos). */
    public static final Pattern NCM = Pattern.compile("^\\d{8}$");

    /** CEST (7 dígitos numéricos). */
    public static final Pattern CEST = Pattern.compile("^\\d{7}$");

    /** CBO (6 dígitos numéricos). */
    public static final Pattern CBO = Pattern.compile("^\\d{6}$");

    /** Matrícula eSocial (Alfanumérico, hífen e underscore, 1 a 30 caracteres). */
    public static final Pattern MATRICULA_ESOCIAL = Pattern.compile("^[A-Za-z0-9_\\-]{1,30}$");

    /** Número de Recibo GOV (Padrão 1.x.20_dígitos). */
    public static final Pattern NUMERO_RECIBO_GOV = Pattern.compile("^1\\.\\d\\.\\d{20}$");

    /** Processo Judicial NUP (17 ou 20 dígitos numéricos). */
    public static final Pattern PROCESSO_JUDICIAL_NUP = Pattern.compile("^\\d{17}(\\d{3})?$");

    /** Filtro para caracteres inválidos W3C XML 1.0. */
    public static final Pattern XML_INVALID_CHARS = Pattern.compile("[^\\u0009\\u000A\\u000D\\u0020-\\uD7FF\\uE000-\\uFFFD\\x{10000}-\\x{10FFFF}]");

    /** Apenas dígitos. */
    public static final Pattern ONLY_DIGITS = Pattern.compile("^\\d+$");

}
