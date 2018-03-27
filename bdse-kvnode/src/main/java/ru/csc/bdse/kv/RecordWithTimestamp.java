package ru.csc.bdse.kv;

public class RecordWithTimestamp {
    private byte[] payload;
    private long timestamp;
    private boolean isDeleted;

    public RecordWithTimestamp(byte[] payload, long timestamp) {
        this.payload = payload;
        this.timestamp = timestamp;
        this.isDeleted = false;
    }

    public byte[] getPayload() {
        return payload;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public long getTimestamp() {
        return timestamp;
    }
}