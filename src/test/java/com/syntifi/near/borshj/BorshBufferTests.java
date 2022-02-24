package com.syntifi.near.borshj;

import com.syntifi.near.borshj.pojo.BorshWithCollection;
import com.syntifi.near.borshj.pojo.BorshWithMap;
import com.syntifi.near.borshj.pojo.BorshWithOptional;
import com.syntifi.near.borshj.pojo.PlayerAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshBufferTests {
    private BorshBuffer buffer;

    @BeforeEach
    void newBuffer() {
        buffer = BorshBuffer.allocate(256);
    }

    @Test
    void readU8() {
        buffer = BorshBuffer.wrap(new byte[]{0x42});
        assertEquals(0x42, buffer.readU8());
    }

    @Test
    void readU16() {
        buffer = BorshBuffer.wrap(new byte[]{0x11, 0x00});
        assertEquals(0x0011, buffer.readU16());
    }

    @Test
    void readU32() {
        buffer = BorshBuffer.wrap(new byte[]{0x33, 0x22, 0x11, 0x00});
        assertEquals(0x00112233, buffer.readU32());
    }

    @Test
    void readU64() {
        buffer = BorshBuffer.wrap(new byte[]{0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00});
        assertEquals(0x0011223344556677L, buffer.readU64());
    }

    @Test
    void readU128() {
        final byte[] input = new byte[]{
                0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };
        buffer = BorshBuffer.wrap(input);
        assertEquals(BigInteger.valueOf(0x0011223344556677L), buffer.readU128());
    }

    @Test
    void readF32() {
        assertEquals(0.0f, BorshBuffer.wrap(new byte[]{0, 0, 0, 0}).readF32());
        assertEquals(1.0f, BorshBuffer.wrap(new byte[]{0, 0, (byte) 0x80, (byte) 0x3f}).readF32());
    }

    @Test
    void readF64() {
        assertEquals(0.0, BorshBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, 0, 0}).readF64());
        assertEquals(1.0, BorshBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, (byte) 0xf0, (byte) 0x3f}).readF64());
    }

    @Test
    void readString() {
        final byte[] input = new byte[]{5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'};
        buffer = BorshBuffer.wrap(input);
        assertEquals("Borsh", buffer.readString());
    }

    @Test
    void readFixedArray() {
        final byte[] input = new byte[]{1, 2, 3, 4, 5};
        buffer = BorshBuffer.wrap(input);
        assertEquals(0, buffer.reset().readFixedArray(0).length);
        assertEquals(1, buffer.reset().readFixedArray(1).length);
        assertEquals(5, buffer.reset().readFixedArray(5).length);
        assertArrayEquals(input, buffer.reset().readFixedArray(5));
    }

    @Test
    void readArray() {
        final byte[] input = new byte[]{3, 0, 0, 0, 1, 0, 2, 0, 3, 0};
        buffer = BorshBuffer.wrap(input);
        assertArrayEquals(new Short[]{1, 2, 3}, buffer.readArray(Short.class));
    }

    @Test
    void readBoolean() {
        assertFalse(BorshBuffer.wrap(new byte[]{0}).readBoolean());
        assertTrue(BorshBuffer.wrap(new byte[]{1}).readBoolean());
    }

    @Test
    void readOptional() {
        assertEquals(Optional.empty(), BorshBuffer.wrap(new byte[]{0}).readOptional());
        assertEquals(Optional.of(42), BorshBuffer.wrap(new byte[]{1, 42, 0, 0, 0}).readOptional(Integer.class));
    }

    @Test
    void readWithClassOptional() {
        assertEquals(Optional.empty(), BorshBuffer.wrap(new byte[]{0}).read(Optional.class));
        assertEquals(Optional.of(42), BorshBuffer.wrap(new byte[]{1, 42, 0, 0, 0}).read(Optional.class, Integer.class));

    }

    @Test
    void readWithClassList() {
        assertEquals(Arrays.asList(1, 2, 3),
                BorshBuffer.wrap(new byte[]{3, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0})
                        .read(ArrayList.class, Integer.class));
    }

    @Test
    void readBorshWithMapPOJO() {
        final byte[] input = new byte[]{3, 0, 0, 0, 1, 0, 0, 0, 5, 0, 0, 0, 'B', 'o', 'r', 's', 'h', 2, 0, 0, 0, 2, 0, 0, 0, 'i', 's', 3, 0, 0, 0, 4, 0, 0, 0, 'C', 'o', 'o', 'l'};

        buffer = BorshBuffer.wrap(input);
        BorshWithMap readObject = buffer.read(BorshWithMap.class);

        Map<Integer, String> testMap = new TreeMap<>();
        testMap.put(1, "Borsh");
        testMap.put(2, "is");
        testMap.put(3, "Cool");

        assertEquals(new BorshWithMap(testMap), readObject);
    }

    @Test
    void readBorshWithOptionalPOJO() {
        final byte[] input = new byte[]{1, 1, 0, 0, 0, 65};

        buffer = BorshBuffer.wrap(input);
        BorshWithOptional readObject = buffer.read(BorshWithOptional.class);

        assertEquals(new BorshWithOptional(Optional.of("A")), readObject);
    }

    @Test
    void readBorshWithCollectionPOJO() {
        final byte[] input = new byte[]{2, 0, 0, 0, 3, 0, 0, 0, 65, 66, 67, 2, 0, 0, 0, 68, 69};

        buffer = BorshBuffer.wrap(input);
        BorshWithCollection readObject = buffer.read(BorshWithCollection.class);

        assertEquals(new BorshWithCollection(Arrays.asList("ABC", "DE")), readObject);
    }

    @Test
    void readArrayOfPlayerAccount() {
        final byte[] input = new byte[]{
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                1, 1};

        buffer = BorshBuffer.wrap(input);
        PlayerAccount readObject = buffer.read(PlayerAccount.class);

        assertEquals(
                new PlayerAccount(
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes(),
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes(),
                        (byte) 1,
                        (byte) 1),
                readObject);
    }

    @Test
    void writeU8() {
        final byte[] actual = buffer.writeU8(0x42).toByteArray();
        final byte[] expected = new byte[]{0x42};
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeU16() {
        final byte[] actual = buffer.writeU16(0x0011).toByteArray();
        final byte[] expected = new byte[]{0x11, 0x00};
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeU32() {
        final byte[] actual = buffer.writeU32(0x00112233).toByteArray();
        final byte[] expected = new byte[]{0x33, 0x22, 0x11, 0x00};
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeU64() {
        final byte[] actual = buffer.writeU64(0x0011223344556677L).toByteArray();
        final byte[] expected = new byte[]{
                0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeU128() {
        final byte[] actual = buffer.writeU128(0x0011223344556677L).toByteArray();
        final byte[] expected = new byte[]{
                0x77, 0x66, 0x55, 0x44, 0x33, 0x22, 0x11, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
        };
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeF32() {
        final byte[] actual = buffer.writeF32(1.0f).toByteArray();
        final byte[] expected = new byte[]{0, 0, (byte) 0x80, (byte) 0x3f};
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeF64() {
        final byte[] actual = buffer.writeF64(1.0).toByteArray();
        final byte[] expected = new byte[]{0, 0, 0, 0, 0, 0, (byte) 0xf0, (byte) 0x3f};
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeString() {
        final byte[] actual = buffer.writeString("Borsh").toByteArray();
        final byte[] expected = new byte[]{5, 0, 0, 0, 'B', 'o', 'r', 's', 'h'};
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeFixedArray() {
        buffer.writeFixedArray(new byte[]{1, 2, 3, 4, 5});
        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        final byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeArray() {
        buffer.writeGenericArray(new Short[]{1, 2, 3});
        final byte[] expected = new byte[]{3, 0, 0, 0, 1, 0, 2, 0, 3, 0};
        final byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeArrayOfList() {
        buffer.writeGenericArray(Arrays.asList(new Short[]{1, 2, 3}));
        final byte[] expected = new byte[]{3, 0, 0, 0, 1, 0, 2, 0, 3, 0};
        final byte[] actual = buffer.toByteArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    void writeBoolean() {
        assertArrayEquals(new byte[]{0}, buffer.reset().writeBoolean(false).toByteArray());
        assertArrayEquals(new byte[]{1}, buffer.reset().writeBoolean(true).toByteArray());
    }

    @Test
    void writeOptional() {
        assertArrayEquals(new byte[]{0}, buffer.reset().writeOptional(Optional.empty()).toByteArray());
        assertArrayEquals(new byte[]{1, 42, 0, 0, 0}, buffer.reset().writeOptional(Optional.of(42)).toByteArray());
    }

    @Test
    void writeWithClassOptional() {
        assertArrayEquals(new byte[]{0}, buffer.reset().write(Optional.empty()).toByteArray());
    }

    @Test
    void writeWithClassList() {
        assertArrayEquals(new byte[]{3, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 3, 0, 0, 0},
                buffer.reset().write(Arrays.asList(1, 2, 3)).toByteArray());
    }

    @Test
    void writeBorshWithOptionalPOJO() {
        buffer.write(new BorshWithOptional(Optional.of("A")));

        final byte[] expected = new byte[]{1, 1, 0, 0, 0, 65};

        final byte[] actual = buffer.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    void writeBorshWithMapPOJO() {
        Map<Integer, String> testMap = new TreeMap<>();
        testMap.put(1, "Borsh");
        testMap.put(2, "is");
        testMap.put(3, "Cool");
        buffer.write(new BorshWithMap(testMap));

        final byte[] expected = new byte[]{3, 0, 0, 0, 1, 0, 0, 0, 5, 0, 0, 0, 'B', 'o', 'r', 's', 'h', 2, 0, 0, 0, 2, 0, 0, 0, 'i', 's', 3, 0, 0, 0, 4, 0, 0, 0, 'C', 'o', 'o', 'l'};

        final byte[] actual = buffer.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    void writeBorshWithCollectionPOJO() {
        buffer.write(new BorshWithCollection(Arrays.asList("ABC", "DE")));

        final byte[] expected = new byte[]{2, 0, 0, 0, 3, 0, 0, 0, 65, 66, 67, 2, 0, 0, 0, 68, 69};

        final byte[] actual = buffer.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    void writeArrayOfPlayerAccount() {
        buffer.write(
                new PlayerAccount(
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes(),
                        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes(),
                        (byte) 1,
                        (byte) 1));

        final byte[] expected = new byte[]{
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65,
                1, 1};

        final byte[] actual = buffer.toByteArray();

        assertArrayEquals(expected, actual);
    }

    @Test
    void testF32() {
        final float value = 3.1415f;
        assertEquals(value, BorshBuffer.wrap(buffer.writeF32(value).toByteArray()).readF32());
    }

    @Test
    void testF64() {
        final double value = 3.1415;
        assertEquals(value, BorshBuffer.wrap(buffer.writeF64(value).toByteArray()).readF64());
    }
}
