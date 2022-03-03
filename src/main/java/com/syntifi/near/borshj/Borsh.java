package com.syntifi.near.borshj;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * Borsh interface for POJO serialization support
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public interface Borsh {
    /**
     * Serializes object with a BorshBuffer
     *
     * @param object the object to serialize
     * @return the serialized byte array
     */
    @NonNull
    static byte[] serialize(@NonNull final Object object) {
        return BorshBuffer.allocate(4096).write(requireNonNull(object)).toByteArray();
    }

    /**
     * Deserializes given byte array to class type
     *
     * @param bytes bytes to deserialize
     * @param clazz class type
     * @param <T>   type param
     * @return populated instance of the given class type
     */
    @NonNull
    static <T> T deserialize(@NonNull final byte[] bytes, @NonNull final Class<T> clazz) {
        return deserialize(BorshBuffer.wrap(requireNonNull(bytes)), clazz);
    }

    /**
     * Deserializes a buffer to a class type
     *
     * @param buffer BorshBuffer to read from
     * @param clazz  class type
     * @param <T>    type param
     * @return populated instance of the given class type
     */
    @NonNull
    static <T> T deserialize(@NonNull final BorshBuffer buffer, @NonNull final Class<T> clazz) {
        return buffer.read(requireNonNull(clazz));
    }

    /**
     * Checks if a class type is serializable with borsh
     *
     * @param clazz the class type to test
     * @return true if it is serializable
     */
    static boolean isSerializable(@Nullable final Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        return Arrays.stream(clazz.getInterfaces()).anyMatch(i -> i == Borsh.class) ||
                isSerializable(clazz.getSuperclass());
    }
}
