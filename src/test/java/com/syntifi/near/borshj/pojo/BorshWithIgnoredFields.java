package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

import java.util.Objects;

public class BorshWithIgnoredFields implements Borsh {
    public long ignoredValue;

    @BorshField(order = 1)
    public String notIgnoredValue;

    public BorshWithIgnoredFields() {
    }

    public BorshWithIgnoredFields(long ignoredValue, String notIgnoredValue) {
        this.ignoredValue = ignoredValue;
        this.notIgnoredValue = notIgnoredValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorshWithIgnoredFields that = (BorshWithIgnoredFields) o;
        return ignoredValue == that.ignoredValue && Objects.equals(notIgnoredValue, that.notIgnoredValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ignoredValue, notIgnoredValue);
    }
}
