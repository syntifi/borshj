package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

public class Point2Df implements Borsh {

    @BorshField(order = 1)
    private float x;

    @BorshField(order = 2)
    private float y;

    public Point2Df() {
    }

    public Point2Df(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Point2Df(%f, %f)", this.x, this.y);
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != this.getClass())
            return false;
        final Point2Df other = (Point2Df) object;
        return this.x == other.x && this.y == other.y;
    }
}
