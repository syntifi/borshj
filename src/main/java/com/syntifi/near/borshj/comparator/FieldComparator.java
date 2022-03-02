package com.syntifi.near.borshj.comparator;

import com.syntifi.near.borshj.annotation.BorshOrder;
import com.syntifi.near.borshj.exception.BorshException;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Comparator to sort the object's fields according to the BorshOrder annotation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class FieldComparator implements Comparator<Field> {

    public int compare(Field f1, Field f2) {
        if (!f1.isAnnotationPresent(BorshOrder.class) || !f2.isAnnotationPresent(BorshOrder.class)) {
            throw new BorshException(
                    "Borsh POJO must specify the serialization order using the BorshOrder annotation.");
        }
        return Integer.compare(
                f1.getAnnotation(BorshOrder.class).value(),
                f2.getAnnotation(BorshOrder.class).value());
    }
}