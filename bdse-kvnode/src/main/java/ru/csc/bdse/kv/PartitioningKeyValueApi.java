package ru.csc.bdse.kv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.csc.bdse.partitioning.Partitioner;

import java.util.*;
import java.util.concurrent.*;

public class PartitioningKeyValueApi implements KeyValueApi {

    private final Map< String, KeyValueApi> nodes;
    private final long timeoutMs;
    private final Partitioner partitioner;
    private final ExecutorService executor;

    private static final Logger log = LoggerFactory.getLogger(PartitioningKeyValueApi.class);


    public PartitioningKeyValueApi(Map<String, KeyValueApi> nodes, long timeoutMs, Partitioner partitioner) {
        this.nodes = nodes;
        this.timeoutMs = timeoutMs;
        this.partitioner = partitioner;
        this.executor = Executors.newFixedThreadPool(nodes.size() * 2);
    }

    @Override
    public void put(String key, byte[] value) {
        KeyValueApi shard = nodes.get(partitioner.getPartition(key));

        Future f = executor.submit(()->shard.put(key, value));
        try {
            f.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            String error = String.format("Exception during attempt to put key '%s' to shard '%s'", key, shard);
            log.error(error, e);
            throw new IllegalStateException(error, e);
        }
    }

    @Override
    public Optional<byte[]> get(String key) {
        KeyValueApi shard = nodes.get(partitioner.getPartition(key));

        Future<Optional<byte[]>> f = executor.submit(()->shard.get(key));
        try {
            return f.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            String error = String.format("Exception during attempt to get value by key '%s' from shard '%s'", key, shard);
            log.error(error, e);
            throw new IllegalStateException(error, e);
        }
    }

    @Override
    public Set<String> getKeys(String prefix) {
        Set<Future<Set<String>>> futures = new HashSet<>();
        Set<String> res = new HashSet<>();

        nodes.values().forEach(node -> futures.add(executor.submit(()->node.getKeys(prefix))));
        futures.forEach(f -> {
            try {
                res.addAll(f.get(timeoutMs, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                String error = String.format("Exception during attempt to get all keys by prefix '%s'", prefix);
                log.error(error, e);
                throw new IllegalStateException(error, e);
            }
        });
        return res;
    }

    @Override
    public void delete(String key) {
        KeyValueApi shard = nodes.get(partitioner.getPartition(key));

        Future f = executor.submit(()->shard.delete(key));
        try {
            f.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            String error = String.format("Exception during attempt to delete value by key '%s' from shard '%s'", key, shard);
            log.error(error, e);
            throw new IllegalStateException(error, e);
        }
    }

    @Override
    public Set<NodeInfo> getInfo() {
        Set<Future<Set<NodeInfo>>> futures = new HashSet<>();
        Set<NodeInfo> res = new HashSet<>();

        nodes.values().forEach(node -> futures.add(executor.submit(node::getInfo)));
        futures.forEach(f -> {
            try {
                res.addAll(f.get(timeoutMs, TimeUnit.MILLISECONDS));
            } catch (Exception e) {
                String error = "Exception during attempt to get all nodes info";
                log.error(error, e);
                throw new IllegalStateException(error, e);
            }
        });
        return res;
    }

    @Override
    public void action(String nodeName, NodeAction action) {
        KeyValueApi node = nodes.get(nodeName);
        Future f = executor.submit(() -> node.action(nodeName, action));

        try {
            f.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            String error = String.format("Exception during attempt to perform action '%s' on Node '%s'", action, nodeName);
            log.error(error, e);
            throw new IllegalStateException(error, e);
        }
    }
}