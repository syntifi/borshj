package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshOrder;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class BorshWithMap implements Borsh {
    @BorshOrder
    public final Map<Integer, String> map;

    public BorshWithMap() {
        this.map = new TreeMap();
    }

    public BorshWithMap(Map<Integer, String> collection) {
        this.map = collection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorshWithMap that = (BorshWithMap) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
