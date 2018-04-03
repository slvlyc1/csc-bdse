package ru.csc.bdse.kv;

import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static ru.csc.bdse.kv.NodeStatus.DOWN;
import static ru.csc.bdse.kv.NodeStatus.UP;

public class CoordinatorKeyValueApi implements KeyValueApi {

    private List<KeyValueApi> keyValueNodes;
    private int writeConsistencyLevel;
    private int readConsistencyLevel;
    private long timeout;

    private final String nodeName;
    private volatile NodeStatus nodeStatus;

    private static final Logger log = LoggerFactory.getLogger(CoordinatorKeyValueApi.class);

    public CoordinatorKeyValueApi(String nodeName, List<KeyValueApi> keyValueNodes, int writeConsistencyLevel, int readConsistencyLevel, long timeout) {
        this.nodeName = nodeName;
        this.keyValueNodes = keyValueNodes;
        this.writeConsistencyLevel = writeConsistencyLevel;
        this.readConsistencyLevel = readConsistencyLevel;
        this.timeout = timeout;
        this.nodeStatus = UP;
    }

    @Override
    public void put(String key, byte[] value) {
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        writeToNodes(key, value, false);
    }

    private void writeToNodes(String key, byte[] value, boolean isDeleted) {
        CountDownLatch countDownLatch = new CountDownLatch(writeConsistencyLevel);

        List<Thread> putThreads = keyValueNodes.stream().map(node -> new Thread(() -> {
            node.put(key, wrapRecord(value, isDeleted, node.getInfo()));
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

    private byte[] wrapRecord(byte[] value, boolean isDeleted, Set<NodeInfo> info) {
        RecordWithTimestamp record = new RecordWithTimestamp(value, System.currentTimeMillis(), isDeleted, info.iterator().next().getName());
        String recordJson = new GsonBuilder().create().toJson(record);
        return recordJson.getBytes();
    }

    @Override
    public Optional<byte[]> get(String key) {

        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        CountDownLatch countDownLatch = new CountDownLatch(readConsistencyLevel);

        List<byte[]> values = Collections.synchronizedList(new ArrayList<byte[]>());

        List<Thread> getThreads = keyValueNodes.stream().map(node -> new Thread(() ->
            node.get(key).ifPresent(value -> {
                    values.add(value);
                    countDownLatch.countDown();
            }))).collect(Collectors.toList());

        getThreads.forEach(Thread::start);

        boolean completed = false;
        try {
            completed = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!completed) {
            throw new FailedOperationException(String.format("Failed to read with RCL level = %s",
                    readConsistencyLevel));
        }

        Set<RecordWithTimestamp> conflictingRecords = values.stream()
                .map(this::unwrapRecord)
                .collect(Collectors.toSet());

        if (!conflictingRecords.isEmpty()) {
            RecordWithTimestamp resolvedRecord = new LatestValueConflictResolver().resolve(conflictingRecords);

            return resolvedRecord.isDeleted() ? Optional.empty() : Optional.ofNullable(resolvedRecord.getPayload());
        }
        else {
            return Optional.empty();
        }
    }

    private RecordWithTimestamp unwrapRecord(byte[] value) {
        String recordJson = new String(value);
        RecordWithTimestamp record = new GsonBuilder().create().fromJson(recordJson, RecordWithTimestamp.class);

        return record;
    }

    @Override
    public Set<String> getKeys(String prefix) {
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        CountDownLatch countDownLatch = new CountDownLatch(readConsistencyLevel);

        Set<Set<String>> keys = new HashSet<>();

        List<Thread> getThreads = keyValueNodes.stream()
                .map(node -> new Thread(() -> {
                    keys.add(node.getKeys(prefix));
                    countDownLatch.countDown();
                })).collect(Collectors.toList());

        getThreads.forEach(Thread::start);

        boolean completed = false;
        try {
            completed = countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (!completed) {
            throw new FailedOperationException(String.format("Failed to read with RCL level = %s",
                    readConsistencyLevel));
        }

        return new LatestValueConflictResolver().resolveKeys(keys);
    }

    @Override
    public void delete(String key) {
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        byte[] value = get(key)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to delete on non-existing key '%s'", key)));

        writeToNodes(key, value, true);
    }

    @Override
    public Set<NodeInfo> getInfo() {
        return keyValueNodes.stream()
                .map(KeyValueApi::getInfo)
                .flatMap(Set::stream).collect(Collectors.toSet());
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
