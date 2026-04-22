package br.com.contabilizei.obrigacoes.govcore.util;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilitários de conversão e manipulação de datas para o ecossistema de declarações
 * governamentais brasileiras.
 */
public final class GovDateUtils {

    private GovDateUtils() {
        // Impede instanciação
    }

    /**
     * Converte {@link Date} para {@link LocalDateTime} usando {@code ZoneId.systemDefault()}.
     * Retorna {@code null} se {@code date} for {@code null}.
     *
     * @param date a data a converter; pode ser {@code null}
     * @return o {@link LocalDateTime} correspondente, ou {@code null}
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Converte período AAAAMM para o primeiro dia do mês como {@link LocalDateTime} meia-noite.
     * Ex: {@code parsePeriodoToLocalDateTime(202501)} retorna {@code LocalDateTime.of(2025, 1, 1, 0, 0)}.
     *
     * @param yyyyMM o período no formato AAAAMM; não pode ser {@code null}
     * @return {@link LocalDateTime} correspondente ao primeiro dia do mês à meia-noite
     * @throws IllegalArgumentException se {@code yyyyMM} for nulo ou inválido
     */
    public static LocalDateTime parsePeriodoToLocalDateTime(Integer yyyyMM) {
        if (yyyyMM == null) {
            throw new IllegalArgumentException("Período não pode ser nulo");
        }
        YearMonth ym = VigenciaUtils.parseAnoMes(yyyyMM);
        return ym.atDay(1).atStartOfDay();
    }

    /**
     * Retorna a data com hora definida para {@code 00:00:00.000} (início do dia),
     * usando o fuso horário padrão da JVM ({@code TimeZone.getDefault()}).
     * <p>
     * <strong>Atenção:</strong> Em ambientes cloud (ex: GCP) o fuso padrão normalmente é UTC.
     * Para comportamento determinístico em qualquer ambiente, utilize a sobrecarga
     * {@link #getStartMinuteDateForQuery(Date, TimeZone)}.
     *
     * @param date a data de referência; não pode ser {@code null}
     * @return nova instância de {@link Date} com horário zerado no fuso padrão da JVM
     * @throws IllegalArgumentException se {@code date} for nulo
     */
    public static Date getStartMinuteDateForQuery(Date date) {
        return getStartMinuteDateForQuery(date, TimeZone.getDefault());
    }

    /**
     * Retorna a data com hora definida para {@code 00:00:00.000} (início do dia)
     * no fuso horário especificado.
     *
     * @param date     a data de referência; não pode ser {@code null}
     * @param timeZone o fuso horário a usar; não pode ser {@code null}
     * @return nova instância de {@link Date} com horário zerado no fuso informado
     * @throws IllegalArgumentException se {@code date} ou {@code timeZone} forem nulos
     */
    public static Date getStartMinuteDateForQuery(Date date, TimeZone timeZone) {
        if (date == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        if (timeZone == null) {
            throw new IllegalArgumentException("TimeZone não pode ser nulo");
        }
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Retorna a data com hora definida para {@code 23:59:59.999} (fim do dia),
     * usando o fuso horário padrão da JVM ({@code TimeZone.getDefault()}).
     * <p>
     * <strong>Atenção:</strong> Em ambientes cloud (ex: GCP) o fuso padrão normalmente é UTC.
     * Para comportamento determinístico em qualquer ambiente, utilize a sobrecarga
     * {@link #getLastMinuteDateForQuery(Date, TimeZone)}.
     *
     * @param date a data de referência; não pode ser {@code null}
     * @return nova instância de {@link Date} com horário no último instante do dia no fuso padrão
     * @throws IllegalArgumentException se {@code date} for nulo
     */
    public static Date getLastMinuteDateForQuery(Date date) {
        return getLastMinuteDateForQuery(date, TimeZone.getDefault());
    }

    /**
     * Retorna a data com hora definida para {@code 23:59:59.999} (fim do dia)
     * no fuso horário especificado.
     *
     * @param date     a data de referência; não pode ser {@code null}
     * @param timeZone o fuso horário a usar; não pode ser {@code null}
     * @return nova instância de {@link Date} com horário no último instante do dia no fuso informado
     * @throws IllegalArgumentException se {@code date} ou {@code timeZone} forem nulos
     */
    public static Date getLastMinuteDateForQuery(Date date, TimeZone timeZone) {
        if (date == null) {
            throw new IllegalArgumentException("Data não pode ser nula");
        }
        if (timeZone == null) {
            throw new IllegalArgumentException("TimeZone não pode ser nulo");
        }
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}
