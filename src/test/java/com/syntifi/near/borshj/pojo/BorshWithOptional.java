package com.syntifi.near.borshj.pojo;

import java.util.Optional;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

public class BorshWithOptional implements Borsh {

    @SuppressWarnings("DefaultAnnotationParam")
    @BorshField(1)
    public final Optional<String> optional;

    public BorshWithOptional() {
        this.optional = Optional.empty();
    }

    public BorshWithOptional(Optional<String> optional) {
        this.optional = optional;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((!optional.isPresent()) ? 0 : optional.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BorshWithOptional other = (BorshWithOptional) obj;
        if (!optional.isPresent()) {
            return !other.optional.isPresent();
        } else
            return optional.equals(other.optional);
    }
}
