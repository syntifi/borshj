package com.syntifi.near.borshj;

import com.syntifi.near.borshj.pojo.Point2Df;
import com.syntifi.near.borshj.pojo.Rect2Df;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshTests {
    @Test
    void roundtripPoint2Df() {
        final Point2Df point = new Point2Df(123, 456);
        assertEquals(point, Borsh.deserialize(Borsh.serialize(point), Point2Df.class));
    }

    @Test
    void roundtripRect2Df() {
        final Point2Df topLeft = new Point2Df(-123, -456);
        final Point2Df bottomRight = new Point2Df(123, 456);
        final Rect2Df rect = new Rect2Df(topLeft, bottomRight);
        assertEquals(rect, Borsh.deserialize(Borsh.serialize(rect), Rect2Df.class));
    }
}
