package ru.csc.bdse.kv;

public class FailedOperationException extends RuntimeException {
    public FailedOperationException(String format) {
        super(format);
    }
}
