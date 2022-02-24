package com.syntifi.near.borshj.annotation;

import java.lang.reflect.Field;
import java.util.Comparator;

public class FieldComparator implements Comparator<Field> {

    public int compare(Field f1, Field f2) {
        if (!f1.isAnnotationPresent(BorshOrder.class) || !f2.isAnnotationPresent(BorshOrder.class)) {
            return f1.getName().compareTo(f2.getName());
            //throw new BorshException(
            //        "Borsh POJO must specify the serialization order using the BorshOrder annotation.");
        }
        return Integer.compare(
                f1.getAnnotation(BorshOrder.class).order(),
                f2.getAnnotation(BorshOrder.class).order());
    }

}
