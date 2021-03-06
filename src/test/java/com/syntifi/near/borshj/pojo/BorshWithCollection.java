package com.syntifi.near.borshj.pojo;

import java.util.Collection;
import java.util.LinkedList;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

public class BorshWithCollection implements Borsh {
    @BorshField(order = 1)
    public final Collection<String> collection;

    public BorshWithCollection() {
        this.collection = new LinkedList<>();
    }

    public BorshWithCollection(Collection<String> collection) {
        this.collection = collection;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collection == null) ? 0 : collection.hashCode());
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
        BorshWithCollection other = (BorshWithCollection) obj;
        if (collection == null) {
            return other.collection == null;
        } else
            return collection.equals(other.collection);
    }

}
