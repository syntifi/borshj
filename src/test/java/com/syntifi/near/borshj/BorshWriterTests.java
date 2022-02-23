package com.syntifi.near.borshj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshWriterTests {
    private ByteArrayOutputStream output;
    private BorshWriter writer;

    @BeforeEach
    void newWriter() {
        output = new ByteArrayOutputStream();
        writer = new BorshWriter(output);
    }

    @Test
    void constructWithNull() {
        assertThrows(NullPointerException.class, () -> new BorshWriter(null));
    }

    @Test
    void captureOutput() {
        writer.writeString("Borsh");
        assertArrayEquals(new byte[]{5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'}, output.toByteArray());
    }

    @Test
    void captureOutputByte() {
        byte byteToWrite = 'B';
        writer.write(byteToWrite);
        assertArrayEquals(new byte[] { byteToWrite }, output.toByteArray());
    }
}
