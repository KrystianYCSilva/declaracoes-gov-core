package br.com.contabilizei.obrigacoes.govcore.model.layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Documenta a descrição semântica de um campo de layout governamental.
 * Disponível em runtime via reflection para geração de metadados.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Description {

    /** Texto descritivo do campo. */
    String value() default "";
}
