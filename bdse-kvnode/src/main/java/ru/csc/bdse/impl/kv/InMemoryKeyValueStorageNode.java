package ru.csc.bdse.impl.kv;

import ru.csc.bdse.model.kv.KeyValueStorageNode;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
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
    private boolean isDown = false;

    public InMemoryKeyValueStorageNode(String name) {
        this.name = name;
    }

    @Override
    public void upsert(String key, byte[] value) throws Exception {
        wrapIsShutdown(() -> {
            if (value == null)
                return map.remove(key);
            else
                return map.put(key, value);
        });
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
        properties.setProperty(name, isDown ? "down" : "up");
        return properties;
    }

    @Override
    public void command(String node, String command) {
        switch (command.toLowerCase()) {
            case "down":
                if (name.equals(node) && !isDown) isDown = true;
                break;
            case "up":
                if (name.equals(node) && isDown) isDown = false;
                break;
            default:
                throw new IllegalArgumentException("Invalid command [" + command + "] for node " + node);
        }
    }

    private <R> R wrapIsShutdown(Callable<R> callable) throws Exception {
        if (!isDown) {
            return callable.call();
        } else {
            throw new IOException("Node " + name + " is shut down");
        }
    }
}
