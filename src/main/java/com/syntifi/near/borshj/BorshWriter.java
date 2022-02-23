package com.syntifi.near.borshj;

import androidx.annotation.NonNull;

import java.io.*;

import static java.util.Objects.requireNonNull;

/**
 * Borsh Writer implementation for writing to output stream
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshWriter implements BorshOutput<BorshWriter> {
    protected final ByteArrayOutputStream stream;

    /**
     * Instantiate a BorshWriter from an output stream.
     *
     * @param stream the output stream to write to
     */
    public BorshWriter(@NonNull final ByteArrayOutputStream stream) {
        this.stream = requireNonNull(stream);
    }


    /**
     * Writes a byte
     *
     * @param b the byte to write
     * @return the calling BorshWriter instance
     */
    @Override
    @NonNull
    public BorshWriter write(final byte b) {
        this.stream.write(b);
        return this;
    }

    /**
     * Writes a byte array
     *
     * @param array the byte array to write
     * @return the calling BorshWriter instance
     */
    @Override
    @NonNull
    public BorshWriter write(@NonNull final byte[] array) {
        this.stream.write(array, 0, array.length);
        return this;
    }
}
