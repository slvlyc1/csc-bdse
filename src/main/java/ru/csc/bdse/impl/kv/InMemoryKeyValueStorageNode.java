package ru.csc.bdse.impl.kv;

import ru.csc.bdse.model.kv.KeyValueStorageNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TODO
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
    public void upsert(String key, byte[] value) {
        map.put(key, value);
    }

    @Override
    public byte[] get(String key) {
        return map.get(key);
    }

    @Override
    public byte[] delete(String key) {
        return map.remove(key);
    }

    @Override
    public Iterator<String> keysIterator() {
        return map.keySet().iterator();
    }

    @Override
    public Map<String, Integer> status() {
        Map<String,Integer> m = new HashMap<>();
        m.put(name, isShutdown ? -1 : 0);
        return m;
    }

    @Override
    public void shutdown(String node) {
        if (name.equals(node) && !isShutdown) isShutdown = true;
    }

    @Override
    public void start(String node) {
        if (name.equals(node) && isShutdown) isShutdown = false;
    }
}
