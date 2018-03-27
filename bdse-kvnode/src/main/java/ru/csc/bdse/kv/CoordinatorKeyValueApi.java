package ru.csc.bdse.kv;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CoordinatorKeyValueApi implements KeyValueApi {

    private List<KeyValueApi> keyValueNodes;
    private int writeConsistencyLevel;
    private int readConsistencyLevel;
    private long timeout;

    private static final Logger log = LoggerFactory.getLogger(CoordinatorKeyValueApi.class);

    public CoordinatorKeyValueApi(List<KeyValueApi> keyValueNodes, int writeConsistencyLevel, int readConsistencyLevel, long timeout) {
        this.keyValueNodes = keyValueNodes;
        this.writeConsistencyLevel = writeConsistencyLevel;
        this.readConsistencyLevel = readConsistencyLevel;
        this.timeout = timeout;
    }

    @Override
    public void put(String key, byte[] value) {
        byte[] record = wrapRecord(value);

        CountDownLatch countDownLatch = new CountDownLatch(writeConsistencyLevel);

        List<Thread> putThreads = keyValueNodes.stream().map(node -> new Thread(() -> {
            node.put(key, record);
            countDownLatch.countDown();
        })).collect(Collectors.toList());

        putThreads.forEach(Thread::start);

        boolean completed = false;
        try {
            completed = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!completed) {
            throw new FailedOperationException(String.format("Failed to write with WCL level = %s", writeConsistencyLevel));
        }

    }

    private byte[] wrapRecord(byte[] value) {
        RecordWithTimestamp record = new RecordWithTimestamp(value, System.currentTimeMillis());
        String recordJson = new GsonBuilder().create().toJson(record);
        return recordJson.getBytes();
    }

    @Override
    public Optional<byte[]> get(String key) {

        CountDownLatch countDownLatch = new CountDownLatch(writeConsistencyLevel);

        List<byte[]> values = Collections.synchronizedList(new ArrayList<byte[]>());

        List<Thread> putThreads = keyValueNodes.stream().map(node -> new Thread(() ->
            node.get(key).ifPresent(value -> {
                    values.add(value);
                    countDownLatch.countDown();
            }))).collect(Collectors.toList());

        putThreads.forEach(Thread::start);

        boolean completed = false;
        try {
            completed = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!completed) {
            throw new FailedOperationException(String.format("Failed to write with WCL level = %s", writeConsistencyLevel));
        }

        Set<RecordWithTimestamp> conflictRecords = values.stream().map(this::unwrapRecord).collect(Collectors.toSet());
        RecordWithTimestamp resolvedRecord = new LatestValueConflictResolver().resolve(conflictRecords);

        return Optional.ofNullable(resolvedRecord.getPayload());
    }

    private RecordWithTimestamp unwrapRecord(byte[] value) {
        String recordJson = new String(value);
        RecordWithTimestamp record = new GsonBuilder().create().fromJson(recordJson, RecordWithTimestamp.class);

        return record;
    }

    @Override
    public Set<String> getKeys(String prefix) {
        return null;
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public Set<NodeInfo> getInfo() {
        return null;
    }

    @Override
    public void action(String node, NodeAction action) {

    }

    public List<KeyValueApi> getKeyValueNodes() {
        return keyValueNodes;
    }

    public int getWriteConsistencyLevel() {
        return writeConsistencyLevel;
    }

    public int getReadConsistencyLevel() {
        return readConsistencyLevel;
    }

    public long getTimeout() {
        return timeout;
    }
}
