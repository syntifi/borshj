package com.syntifi.near.borshj;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.SortedSet;

import com.syntifi.near.borshj.annotation.BorshFields;
import com.syntifi.near.borshj.exception.BorshException;

import androidx.annotation.NonNull;

/**
 * Interface with default implementations for input bytes/reading data
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public interface BorshInput {

    /**
     * Reads from buffer to specified type of clazz
     *
     * @param clazz the type to read data to
     * @param <T>   type of clazz parameter
     * @return the data mapped to the type of T
     */
    @SuppressWarnings("unchecked")
    default <T> T read(@NonNull final Class<T> clazz) {
        requireNonNull(clazz);
        if (clazz == Byte.class || clazz == byte.class) {
            return (T) Byte.valueOf(this.readU8());
        } else if (clazz == Short.class || clazz == short.class) {
            return (T) Short.valueOf(this.readU16());
        } else if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(this.readU32());
        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(this.readU64());
        } else if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(this.readF32());
        } else if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(this.readF64());
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(this.readBoolean());
        } else if (clazz == BigInteger.class) {
            return (T) this.readU128();
        } else if (clazz == String.class) {
            return (T) this.readString();
        } else if (clazz == Optional.class) {
            return (T) this.readOptional();
        } else if (Borsh.isSerializable(clazz)) {
            return this.readPOJO(clazz);
        }
        throw new IllegalArgumentException();
    }

    /**
     * Reads a value for a Generic type
     *
     * @param clazz          the generic class
     * @param parameterClass clazz parameter class
     * @param <T>
     * @param <P>
     * @return the data mapped to the type of Generic of T.
     */
    @SuppressWarnings("unchecked")
    default <T, P> T read(final @NonNull Class<T> clazz, final @NonNull Class<P> parameterClass) {
        requireNonNull(clazz);
        requireNonNull(parameterClass);
        if (clazz == Optional.class) {
            return (T) this.readOptional(parameterClass);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return (T) this.readGenericArray(parameterClass);
        }
        throw new IllegalArgumentException();
    }

    /**
     * Reads into a Borsh POJO
     *
     * @param clazz Borsh POJO class
     * @param <T>   type of Borsh POJO class
     * @return Borsh POJO instance with its data
     */
    default <T> T readPOJO(@NonNull final Class<T> clazz) {
        try {
            final T object = clazz.getConstructor().newInstance();
            SortedSet<Field> fields = BorshFields.filterAndSort(object.getClass().getDeclaredFields());
            for (final Field field : fields) {
                field.setAccessible(true);
                final Class<?> fieldClass = field.getType();
                // Is generic type?
                if (fieldClass.getTypeParameters().length > 0) {
                    final Type fieldType = field.getGenericType();
                    if (!(fieldType instanceof ParameterizedType)) {
                        throw new AssertionError("unsupported Generic type");
                    }
                    final Type[] typeArgs = ((ParameterizedType) fieldType).getActualTypeArguments();
                    assert (typeArgs.length == 1);
                    final Class<?> parameterClass = (Class<?>) typeArgs[0];
                    field.set(object, this.read(fieldClass, parameterClass));
                } else if (fieldClass == byte[].class) {
                    field.set(object, this.readFixedArray(Array.getLength(field.get(object))));
                } else {
                    field.set(object, this.read(field.getType()));
                }
            }
            return object;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                | InvocationTargetException error) {
            throw new BorshException(error);
        }
    }


    /**
     * Read data as U8
     *
     * @return U8 as byte
     */
    default byte readU8() {
        return this.read();
    }

    /**
     * Read data as U16
     *
     * @return U16 as short
     */
    default short readU16() {
        return BorshBuffer.wrap(this.read(2)).readU16();
    }

    /**
     * Read data as U32
     *
     * @return U32 as int
     */
    default int readU32() {
        return BorshBuffer.wrap(this.read(4)).readU32();
    }

    /**
     * Read data as U64
     *
     * @return U64 as long
     */
    default long readU64() {
        return BorshBuffer.wrap(this.read(8)).readU64();
    }

    /**
     * Read data as U128
     *
     * @return U128 as BigInteger
     */
    @NonNull
    default BigInteger readU128() {
        final byte[] bytes = new byte[16];
        this.read(bytes);
        for (int i = 0; i < 8; i++) {
            final byte a = bytes[i];
            final byte b = bytes[15 - i];
            bytes[i] = b;
            bytes[15 - i] = a;
        }
        return new BigInteger(bytes);
    }

    /**
     * Read data as F32
     *
     * @return F32 as float
     */
    default float readF32() {
        return BorshBuffer.wrap(this.read(4)).readF32();
    }

    /**
     * Read data as F64
     *
     * @return F64 as double
     */
    default double readF64() {
        return BorshBuffer.wrap(this.read(8)).readF64();
    }

    /**
     * Read data as String
     *
     * @return the String
     */
    @NonNull
    default String readString() {
        final int length = this.readU32();
        final byte[] bytes = new byte[length];
        this.read(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Read data as FixedArray
     *
     * @param length the length of the array
     * @return data as byte[]
     */
    @NonNull
    default byte[] readFixedArray(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        final byte[] bytes = new byte[length];
        this.read(bytes);
        return bytes;
    }

    /**
     * Reads array to array of T
     *
     * @param clazz class of array items
     * @param <T>   type of class
     * @return the array of T with its elements read
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <T> T[] readArray(@NonNull final Class<T> clazz) {
        final int length = this.readU32();
        final T[] elements = (T[]) Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            elements[i] = this.read(clazz);
        }
        return elements;
    }

    /**
     * Reads a Generic Array
     *
     * @param parameterClass the parametrization class
     * @param <T>            the type of parameterClass
     * @return the collection with items of type T with its data read
     */
    @NonNull
    default <T> Collection<T> readGenericArray(@NonNull final Class<T> parameterClass) {
        final int length = this.readU32();
        Collection<T> elements;
        elements = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            elements.add(this.read(parameterClass));
        }
        return elements;
    }

    /**
     * Reads a boolean
     *
     * @return the boolean
     */
    default boolean readBoolean() {
        return (this.readU8() != 0);
    }

    /**
     * Reads an optional
     *
     * @param <T> type of optional data
     * @return the Optional object
     */
    @NonNull
    default <T> Optional<T> readOptional() {
        final boolean isPresent = (this.readU8() != 0);
        if (!isPresent) {
            return Optional.empty();
        }
        throw new AssertionError("Optional type has been erased and cannot be reconstructed");
    }

    /**
     * Reads an optional
     *
     * @param clazz parameter class of optional
     * @param <T>   the type of the parameter class
     * @return the optional with its value read
     */
    @NonNull
    default <T> Optional<T> readOptional(@NonNull final Class<T> clazz) {
        final boolean isPresent = (this.readU8() != 0);
        return isPresent ? Optional.of(this.read(clazz)) : Optional.empty();
    }

    /**
     * Reads a byte
     *
     * @return the byte read
     */
    byte read();

    /**
     * Reads a byte array
     *
     * @param length the length of the array
     * @return the read byte array
     */
    default byte[] read(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        final byte[] result = new byte[length];
        this.read(result);
        return result;
    }

    /**
     * Reads byte array into result
     *
     * @param result the byte array to read into
     */
    default void read(@NonNull final byte[] result) {
        this.read(result, 0, result.length);
    }

    /**
     * Reads byte array into result
     *
     * @param result the byte array to read into
     * @param offset the offset to skip
     * @param length the length to read
     */
    void read(@NonNull byte[] result, int offset, int length);
}
