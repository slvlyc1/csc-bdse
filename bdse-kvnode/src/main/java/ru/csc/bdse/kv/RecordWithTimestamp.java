package ru.csc.bdse.kv;

public class RecordWithTimestamp {
    private byte[] payload;
    private long timestamp;
    private boolean isDeleted;
    private String nodeId;

    public RecordWithTimestamp(byte[] payload, long timestamp, String nodeId) {
        this.payload = payload;
        this.timestamp = timestamp;
        this.nodeId = nodeId;
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

    public String getNodeId() {
        return nodeId;
    }
}