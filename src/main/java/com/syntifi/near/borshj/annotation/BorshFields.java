package com.syntifi.near.borshj.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

    /**
     *  Exclude transient fields and sort them
     *
     * @param fields fields to filter and sort
     * @return sorted fields
     */
    public static SortedSet<Field> filterAndSort(Field[] fields) {
        SortedSet<Field> sortedFields = new TreeSet<Field>(new FieldComparator());
        for (Field field : fields) {
            if (!Modifier.isTransient(field.getModifiers())) {
                sortedFields.add(field);
            }
        }
        return sortedFields;
    }
}
