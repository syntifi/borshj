package com.syntifi.near.borshj.pojo;

public class Empty implements Shape {

    public Empty() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }
}
