package ru.csc.bdse.impl.kv;

import ru.csc.bdse.model.kv.KeyValueStorageNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of [[KeyValueStorageNode]] in memory
 *
 * @author alesavin
 */
public class InMemoryKeyValueStorageNode implements KeyValueStorageNode {

    private final String name;

    private Map<String, byte[]> map = new HashMap<>();
    private boolean isShutdown = false;

    public InMemoryKeyValueStorageNode(String name) {
        this.name = name;
    }

    @Override
    public void upsert(String key, byte[] value) throws Exception {
        wrapIsShutdown(() -> map.put(key, value));
    }

    @Override
    public byte[] get(String key) throws Exception {
        return wrapIsShutdown(() -> map.get(key));
    }

    @Override
    public Set<String> keys(Predicate<String> predicate) throws Exception {
        return wrapIsShutdown(() -> map.keySet().stream().filter(predicate).collect(Collectors.toSet()));
    }

    @Override
    public Properties status() {
        final Properties properties = new Properties();
        properties.setProperty(name, isShutdown ? "DOWN" : "UP");
        return properties;
    }

    @Override
    public void shutdown(String node) {
        if (name.equals(node) && !isShutdown) isShutdown = true;
    }

    @Override
    public void start(String node) {
        if (name.equals(node) && isShutdown) isShutdown = false;
    }

    private <R> R wrapIsShutdown(Callable<R> callable) throws Exception {
        if (!isShutdown) {
            return callable.call();
        } else {
            throw new IOException("Node " + name + " is shut down");
        }
    }
}
