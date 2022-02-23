package com.syntifi.near.borshj;

import com.syntifi.near.borshj.exception.BorshException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshReaderTests {
    private BorshReader reader;

    protected BorshReader newReader(final byte[] bytes) {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        reader = new BorshReader(input);
        return reader;
    }

    @Test
    void constructWithNull() {
        assertThrows(NullPointerException.class, () -> new BorshReader(null));
    }

    @Test
    void parseInput() {
        assertEquals("Borsh", newReader(new byte[]{5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'}).readString());
    }

    @Test
    void parseInputByte() {
        byte byteToRead = 'B';
        assertEquals(byteToRead, newReader(new byte[] { byteToRead }).read());
    }

    @Test
    void readByteThrowsEOF() {
        byte byteToRead = 'B';
        assertEquals(byteToRead, newReader(new byte[] { byteToRead }).read());
        Throwable t = assertThrows(BorshException.class, () -> reader.read());
        assertTrue(t.getCause() instanceof EOFException);
    }

    @Test
    void outOfBoundsReadByteArrayByLength() {
        byte byteToRead = 'B';
        byte[] output = new byte[2];
        newReader(new byte[] { byteToRead });
        assertThrows(IndexOutOfBoundsException.class, () -> reader.read(output, 1, -1));
    }

    @Test
    void outOfBoundsReadByteArrayByOffset() {
        byte byteToRead = 'B';
        byte[] output = new byte[2];
        newReader(new byte[] { byteToRead });
        assertThrows(IndexOutOfBoundsException.class, () -> reader.read(output, -1, 1));
    }

    @Test
    void outOfBoundsReadByteArrayByReadingMoreThanLength() {
        byte byteToRead = 'B';
        byte[] output = new byte[2];
        newReader(new byte[] { byteToRead });
        assertThrows(IndexOutOfBoundsException.class, () -> reader.read(output, 1, 3));
    }

    @Test
    void readByteArrayThrowsEOF() {
        byte byteToRead = 'B';
        byte[] output = new byte[2];
        newReader(new byte[] { byteToRead });
        reader.read(output, 0, 1);
        Throwable t = assertThrows(BorshException.class, () -> reader.read(output, 0, 1));
        assertTrue(t.getCause() instanceof EOFException);
    }

    @Test
    void readPrimitiveIntArrayThrowsIllegalArgumentException() {
        newReader(new byte[] {});
        assertThrows(IllegalArgumentException.class, () -> reader.read(int[].class));
    }

    @Test
    void readFixedArrayWithLengthLessThanZeroThrowsIllegalArgumentException() {
        newReader(new byte[] {});
        assertThrows(IllegalArgumentException.class, () -> reader.readFixedArray(-1));
    }

    @Test
    void readErasedOptionalThrowsAssertionError() {
        newReader(new byte[] { 1 });
        assertThrows(AssertionError.class, () -> reader.readOptional());
    }

    @Test
    void readWithLengthLessThanZeroThrowsIllegalArgumentException() {
        newReader(new byte[] {});
        assertThrows(IllegalArgumentException.class, () -> reader.read(-1));
    }
}
