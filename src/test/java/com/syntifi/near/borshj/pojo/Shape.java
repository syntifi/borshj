package com.syntifi.near.borshj.pojo;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshSubTypes;

@BorshSubTypes({@BorshSubTypes.BorshSubType(when = Shape.SQUARE, use = Empty.class),
        @BorshSubTypes.BorshSubType(when = Shape.CIRCLE, use = Circle.class)})
public interface Shape extends Borsh {
    int SQUARE = 1;
    int CIRCLE = 0;
}
