package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshOrder;

public class Rect2Df implements Borsh {
    @BorshOrder(order = 1)
    private Point2Df topLeft;
    @BorshOrder(order = 2)
    private Point2Df bottomRight;

    public Rect2Df() {
    }

    public Rect2Df(final Point2Df topLeft, final Point2Df bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    @Override
    public String toString() {
        return String.format("Rect2Df(%s, %s)", this.topLeft.toString(), this.bottomRight.toString());
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null || object.getClass() != this.getClass())
            return false;
        final Rect2Df other = (Rect2Df) object;
        return this.topLeft.equals(other.topLeft) && this.bottomRight.equals(other.bottomRight);
    }
}
