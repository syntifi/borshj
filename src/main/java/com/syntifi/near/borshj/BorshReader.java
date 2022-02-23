package com.syntifi.near.borshj;

import androidx.annotation.NonNull;
import com.syntifi.near.borshj.exception.BorshException;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

/**
 * Borsh Reader implementation for reading from input stream
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshReader implements BorshInput {
    private final InputStream stream;

    /**
     * Instantiate a BorshReader from an input stream.
     *
     * @param stream the input stream with bytes to read
     */
    public BorshReader(@NonNull final InputStream stream) {
        this.stream = requireNonNull(stream);
    }

    /**
     * Reads a byte
     *
     * @return the byte read
     */
    @Override
    public byte read() {
        try {
            final int result = this.stream.read();
            if (result == -1) {
                throw new EOFException();
            }
            return (byte) result;
        } catch (final IOException error) {
            throw new BorshException(error);
        }
    }

    /**
     * Reads a byte array into result
     *
     * @param result the array to read into
     * @param offset the offset to skip
     * @param length the length to read
     */
    @Override
    public void read(@NonNull final byte[] result, final int offset, final int length) {
        if (offset < 0 || length < 0 || length > result.length - offset) {
            throw new IndexOutOfBoundsException();
        }
        try {
            int n = 0;
            while (n < length) {
                final int count = this.stream.read(result, offset + n, length - n);
                if (count == -1) {
                    throw new EOFException();
                }
                n += count;
            }
        } catch (final IOException error) {
            throw new BorshException(error);
        }
    }
}
