package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

import java.util.Objects;

public class BorshWithEnum implements Borsh {
    public enum TestEnum {
        A, B, C
    }

    @BorshField(order = 0)
    public TestEnum testEnum;

    public BorshWithEnum() {
    }

    public BorshWithEnum(TestEnum testEnum) {
        this.testEnum = testEnum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorshWithEnum that = (BorshWithEnum) o;
        return testEnum == that.testEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(testEnum);
    }
}
