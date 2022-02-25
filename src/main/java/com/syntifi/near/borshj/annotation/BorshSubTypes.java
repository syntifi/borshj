package com.syntifi.near.borshj.annotation;

import java.lang.annotation.*;

/**
 * SubTypes mapping annotation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface BorshSubTypes {
    /**
     * value() gets the BorshSubType collection
     *
     * @return the BorshSubType collection
     */
    BorshSubType[] value();

    /**
     * SubType mapping annotation
     */
    @interface BorshSubType {
        /**
         * when() gets the ordinal int value to map
         *
         * @return the ordinal int value to map
         */
        int when();

        /**
         * use() gets the class type to use for given int value
         *
         * @return the class type to use for given int value
         */
        Class<?> use();
    }
}
