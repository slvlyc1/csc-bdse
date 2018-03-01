package ru.csc.bdse.kv;

public class KeyValue {
    private final String key;
    private final byte[] value;

    public KeyValue(String key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }
}
