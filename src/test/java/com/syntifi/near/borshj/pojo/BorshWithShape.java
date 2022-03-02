package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

import java.util.Objects;

public class BorshWithShape implements Borsh {
    @BorshField(order = 1)
    private Shape shape;

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public BorshWithShape() {
    }

    public BorshWithShape(Shape action) {
        this.shape = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorshWithShape that = (BorshWithShape) o;
        return shape.equals(that.shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shape);
    }
}
