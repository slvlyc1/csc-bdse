package ru.csc.bdse.kv;

public class RecordWithTimestamp {
    private byte[] payload;
    private long timestamp;
    private boolean isDeleted;
    private String nodeId;

    public RecordWithTimestamp(byte[] payload, long timestamp, boolean isDeleted, String nodeId) {
        this.payload = payload;
        this.timestamp = timestamp;
        this.isDeleted = isDeleted;
        this.nodeId = nodeId;
    }

    public RecordWithTimestamp(String value, long timestamp, boolean isDeleted, String nodeId) {
        this(value.getBytes(), timestamp, isDeleted, nodeId);
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

    public String getNodeId() {
        return nodeId;
    }
}