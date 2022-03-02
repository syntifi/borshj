package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.annotation.BorshField;

import java.util.Objects;

public class Circle implements Shape {

    @BorshField(order = 1)
    private int radius;

    public Circle() {
        super();
    }

    public Circle(int radius) {
        super();
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return radius == circle.radius;
    }

    @Override
    public int hashCode() {
        return Objects.hash(radius);
    }
}
