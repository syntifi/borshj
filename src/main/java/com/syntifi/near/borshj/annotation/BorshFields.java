package com.syntifi.near.borshj.annotation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Static methods to sort the fields 
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshFields {

    private BorshFields(){};

    public static SortedSet<Field> sort(Field[] fields) {
        SortedSet<Field> sortedFields = new TreeSet<Field>(new FieldComparator());
        sortedFields.addAll(Arrays.asList(fields));
        return sortedFields;
    }

}
