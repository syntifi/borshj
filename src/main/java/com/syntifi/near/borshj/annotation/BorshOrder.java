package com.syntifi.near.borshj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Order decorator 
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BorshOrder {
    public int order() default 1;
}
