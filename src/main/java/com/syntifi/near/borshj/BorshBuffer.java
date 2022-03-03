package com.syntifi.near.borshj;

import androidx.annotation.NonNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import static java.util.Objects.requireNonNull;

/**
 * Borsh buffer implementation for both input/output operations
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshBuffer implements BorshInput, BorshOutput<BorshBuffer> {
    @NonNull
    protected final ByteBuffer buffer;

    /**
     * Creates a BorshBuffer instance
     *
     * @param buffer the buffer to read/write
     */
    protected BorshBuffer(@NonNull final ByteBuffer buffer) {
        this.buffer = requireNonNull(buffer);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN); // serialization standard (java works with big endian as default)
        this.buffer.mark();
    }

    /**
     * Gets the buffer's backing byte array, asserting it exists.
     *
     * @return the buffer's backing byte array
     */
    protected byte[] array() {
        assert this.buffer.hasArray();
        return this.buffer.array();
    }

    /**
     * Creates a BoshBuffer instance with given capacity.
     * Whether it has a backing array is unspecified.
     *
     * @param capacity the capacity to allocate
     * @return the BorshBuffer instance
     */
    @NonNull
    public static BorshBuffer allocate(final int capacity) {
        return new BorshBuffer(ByteBuffer.allocate(capacity));
    }

    /**
     * Creates a BoshBuffer instance with given capacity.
     * It will have a backing array, and its array offset will be zero.
     *
     * @param capacity the capacity to allocate
     * @return the BorshBuffer instance
     */
    @NonNull
    public static BorshBuffer allocateDirect(final int capacity) {
        return new BorshBuffer(ByteBuffer.allocateDirect(capacity));
    }

    /**
     * Instantiate a BorshBuffer from a byte array.
     *
     * @param array the array to back the buffer
     * @return the BorshBuffer instance
     */
    @NonNull
    public static BorshBuffer wrap(final byte[] array) {
        return new BorshBuffer(ByteBuffer.wrap(array));
    }

    /**
     * Gets a byte array from the buffer's content.
     *
     * @return byte array from the buffer's content
     */
    @NonNull
    public byte[] toByteArray() {
        assert this.buffer.hasArray();
        final int arrayOffset = this.buffer.arrayOffset();
        return Arrays.copyOfRange(this.buffer.array(),
                arrayOffset, arrayOffset + this.buffer.position());
    }

    /**
     * Gets the buffer capacity
     *
     * @return the buffer's capacity
     */
    public int capacity() {
        return this.buffer.capacity();
    }

    /**
     * Resets the buffer.
     *
     * @return itself
     */
    @NonNull
    public BorshBuffer reset() {
        this.buffer.reset();
        return this;
    }

    @Override
    public short readU16() {
        return this.buffer.getShort();
    }

    @Override
    public int readU32() {
        return this.buffer.getInt();
    }

    @Override
    public long readU64() {
        return this.buffer.getLong();
    }

    @Override
    public float readF32() {
        return this.buffer.getFloat();
    }

    @Override
    public double readF64() {
        return this.buffer.getDouble();
    }

    @Override
    public byte read() {
        return this.buffer.get();
    }

    @Override
    public void read(@NonNull final byte[] result, final int offset, final int length) {
        this.buffer.get(result, offset, length);
    }

    @Override
    @NonNull
    public BorshBuffer writeU16(final short value) {
        this.buffer.putShort(value);
        return this;
    }

    @Override
    @NonNull
    public BorshBuffer writeU32(final int value) {
        this.buffer.putInt(value);
        return this;
    }

    @Override
    @NonNull
    public BorshBuffer writeU64(final long value) {
        this.buffer.putLong(value);
        return this;
    }

    @Override
    @NonNull
    public BorshBuffer writeF32(final float value) {
        this.buffer.putFloat(value);
        return this;
    }

    @Override
    @NonNull
    public BorshBuffer writeF64(final double value) {
        this.buffer.putDouble(value);
        return this;
    }

    @Override
    @NonNull
    public BorshBuffer write(@NonNull final byte[] bytes) {
        this.buffer.put(bytes);
        return this;
    }

    @Override
    @NonNull
    public BorshBuffer write(final byte b) {
        this.buffer.put(b);
        return this;
    }
}
