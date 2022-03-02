package com.syntifi.near.borshj.comparator;

import com.syntifi.near.borshj.annotation.BorshField;
import com.syntifi.near.borshj.exception.BorshException;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Comparator to sort the object's fields according to the BorshField annotation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class FieldComparator implements Comparator<Field> {

    public int compare(Field f1, Field f2) {
        if (!f1.isAnnotationPresent(BorshField.class) || !f2.isAnnotationPresent(BorshField.class)) {
            throw new BorshException(
                    "Borsh POJO must specify the serialization order using the BorshField annotation.");
        }
        return Integer.compare(
                f1.getAnnotation(BorshField.class).order(),
                f2.getAnnotation(BorshField.class).order());
    }
}
