package com.syntifi.near.borshj.util;

import com.syntifi.near.borshj.comparator.FieldComparator;
import com.syntifi.near.borshj.exception.BorshException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Static methods to perform various actions
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public final class BorshUtil {

    private BorshUtil() {
    }

    /**
     * Exclude transient fields and sort them
     *
     * @param fields fields to filter and sort
     * @return sorted fields
     */
    public static SortedSet<Field> filterAndSort(Field[] fields) {
        SortedSet<Field> sortedFields = new TreeSet<>(new FieldComparator());
        for (Field field : fields) {
            if (!Modifier.isTransient(field.getModifiers())) {
                sortedFields.add(field);
            }
        }
        return sortedFields;
    }

    /**
     * Finds a given annotation on a list of interfaces
     *
     * @param interfaces the interfaces to search
     * @param annotation the annotation to search
     * @param <T>        type parameter of annotation
     * @return the annotation of given type
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(Class<?>[] interfaces, Class<T> annotation) {
        List<Annotation> annotationList = Arrays.stream(interfaces)
                // filter any interface with desired annotation
                .filter(i -> Arrays.stream(i.getAnnotations()).anyMatch(a -> a.annotationType().equals(annotation)))
                // get a list of all annotation of given type found
                .map(a -> a.getAnnotation(annotation))
                .collect(Collectors.toList());

        if (annotationList.isEmpty()) {
            throw new BorshException(String.format("Annotation %s not found on class interfaces", annotation.getSimpleName()));
        } else if (annotationList.size() != 1) {
            throw new BorshException(String.format("Multiple (%s) annotation %s found on class interfaces", annotationList.size(), annotation.getSimpleName()));
        } else {
            return (T) annotationList.get(0);
        }
    }

    /**
     * Finds a given annotation on an object
     *
     * @param obj        the object to search
     * @param annotation the annotation to search
     * @param <T>        type parameter of annotation
     * @return the annotation of given type
     */
    public static <T extends Annotation> T findAnnotation(Object obj, Class<T> annotation) {
        return findAnnotation(obj.getClass(), annotation);
    }

    /**
     * Finds a given annotation on a class
     *
     * @param clazz      the class to search
     * @param annotation the annotation to search
     * @param <T>        type parameter of annotation
     * @return the annotation of given type
     * @throws BorshException thrown if not found or if more than one is found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(Class<?> clazz, Class<T> annotation) throws BorshException {
        Optional<Annotation> ann = Arrays.stream(clazz.getAnnotations()).filter(a -> a.annotationType().equals(annotation)).findFirst();
        if (ann.isPresent()) {
            return (T) ann.get();
        } else {
            throw new BorshException(String.format("Annotation %s not found on %s", annotation.getSimpleName(), clazz.getSimpleName()));
        }
    }

    /**
     * Checks if an annotation is present
     *
     * @param interfaces the interfaces to search
     * @param annotation the annotation to search
     * @return true if annotation is found, false otherwise
     */
    public static boolean hasAnnotation(Class<?>[] interfaces, Class<? extends Annotation> annotation) {
        return Arrays.stream(interfaces).anyMatch(i -> Arrays.stream(i.getAnnotations()).anyMatch(a -> a.annotationType().equals(annotation)));
    }

    /**
     * Checks if an annotation is present
     *
     * @param obj        the object to search
     * @param annotation the annotation to search
     * @return true if annotation is found, false otherwise
     */
    public static boolean hasAnnotation(Object obj, Class<? extends Annotation> annotation) {
        return hasAnnotation(obj.getClass(), annotation);
    }

    /**
     * Checks if an annotation is present
     *
     * @param clazz      the class to search
     * @param annotation the annotation to search
     * @return true if annotation is found, false otherwise
     */
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return clazz.isAnnotationPresent(annotation);
    }
}
