package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Constantes de fuso horário e formatadores de data/hora para o ecossistema declaracoes-*.
 *
 * <p>Todos os campos são {@code static final} e thread-safe. Use estas constantes no lugar de
 * strings literais como {@code "America/Sao_Paulo"} ou chamadas inline a
 * {@link DateTimeFormatter#ofPattern(String)}.
 */
public final class GovTimeConstants {

    private GovTimeConstants() {
    }

    // -------------------------------------------------------------------------
    // ZoneId — regiões do Brasil + UTC
    // -------------------------------------------------------------------------

    /** Fuso horário de São Paulo / Brasília (BRT/BRST). */
    public static final ZoneId ZONE_SAO_PAULO    = ZoneId.of("America/Sao_Paulo");

    /** UTC. */
    public static final ZoneId ZONE_UTC          = ZoneOffset.UTC.normalized();

    /** Fuso horário de Manaus (AMT). */
    public static final ZoneId ZONE_MANAUS       = ZoneId.of("America/Manaus");

    /** Fuso horário de Fortaleza (BRT fixo, sem horário de verão). */
    public static final ZoneId ZONE_FORTALEZA    = ZoneId.of("America/Fortaleza");

    /** Fuso horário de Rio Branco (ACT). */
    public static final ZoneId ZONE_RIO_BRANCO   = ZoneId.of("America/Rio_Branco");

    /** Fuso horário de Fernando de Noronha (FNT). */
    public static final ZoneId ZONE_NORONHA      = ZoneId.of("America/Noronha");

    /** Fuso horário de Cuiabá (AMT). */
    public static final ZoneId ZONE_CUIABA       = ZoneId.of("America/Cuiaba");

    /** Fuso horário de Campo Grande (AMT/AMST). */
    public static final ZoneId ZONE_CAMPO_GRANDE = ZoneId.of("America/Campo_Grande");

    // -------------------------------------------------------------------------
    // ZoneOffset — offsets fixos
    // -------------------------------------------------------------------------

    /** Offset BRT: -03:00 (Brasília). */
    public static final ZoneOffset OFFSET_BRT = ZoneOffset.of("-03:00");

    /** Offset AMT: -04:00 (Amazonas). */
    public static final ZoneOffset OFFSET_AMT = ZoneOffset.of("-04:00");

    /** Offset ACT: -05:00 (Acre). */
    public static final ZoneOffset OFFSET_ACT = ZoneOffset.of("-05:00");

    /** Offset UTC: 00:00. */
    public static final ZoneOffset OFFSET_UTC = ZoneOffset.UTC;

    // -------------------------------------------------------------------------
    // TimeZone — interoperabilidade com java.util legado
    // -------------------------------------------------------------------------

    /** {@link TimeZone} de São Paulo, equivalente a {@link #ZONE_SAO_PAULO}. */
    public static final TimeZone TZ_SAO_PAULO = TimeZone.getTimeZone(ZONE_SAO_PAULO);

    /** {@link TimeZone} UTC. */
    public static final TimeZone TZ_UTC       = TimeZone.getTimeZone("UTC");

    // -------------------------------------------------------------------------
    // DateTimeFormatter — todos thread-safe (instâncias imutáveis)
    // -------------------------------------------------------------------------

    /** Formato compacto de competência: {@code yyyyMM} (ex: {@code "202506"}). */
    public static final DateTimeFormatter FORMATTER_YYYYMM =
            DateTimeFormatter.ofPattern("yyyyMM");

    /** Formato XML de competência: {@code yyyy-MM} (ex: {@code "2025-06"}). */
    public static final DateTimeFormatter FORMATTER_YYYY_MM =
            DateTimeFormatter.ofPattern("yyyy-MM");

    /** Formato ISO de data: {@code yyyy-MM-dd} (ex: {@code "2025-06-01"}). */
    public static final DateTimeFormatter FORMATTER_ISO_DATE =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** Formato BR de data: {@code dd/MM/yyyy} (ex: {@code "01/06/2025"}). */
    public static final DateTimeFormatter FORMATTER_BR_DATE =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formato ISO de data e hora: {@code yyyy-MM-dd HH:mm:ss} (ex: {@code "2025-06-01 10:30:00"}). */
    public static final DateTimeFormatter FORMATTER_ISO_DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** Formato BR de data e hora: {@code dd/MM/yyyy HH:mm:ss} (ex: {@code "01/06/2025 10:30:00"}). */
    public static final DateTimeFormatter FORMATTER_BR_DATE_TIME =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /** Formato timestamp compacto: {@code yyyyMMddHHmmss} (ex: {@code "20250601103000"}). */
    public static final DateTimeFormatter FORMATTER_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /** Formato ISO com offset: {@code yyyy-MM-dd'T'HH:mm:ssXXX} (ex: {@code "2025-06-01T10:30:00-03:00"}). */
    public static final DateTimeFormatter FORMATTER_ISO_OFFSET =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
}
