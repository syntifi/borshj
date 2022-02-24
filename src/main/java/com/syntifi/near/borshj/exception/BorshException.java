package com.syntifi.near.borshj.exception;

/**
 * Borsh Runtime custom exception
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class BorshException extends RuntimeException {
    public BorshException(Throwable t) {
        super(t);
    }

    public BorshException(String msg) {
        super(msg);
    }
}
