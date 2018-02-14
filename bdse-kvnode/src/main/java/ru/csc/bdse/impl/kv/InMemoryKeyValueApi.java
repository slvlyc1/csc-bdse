package ru.csc.bdse.impl.kv;

import ru.csc.bdse.util.Require;
import ru.csc.bdse.model.kv.KeyValueApi;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Trivial in-memory implementation of the storage unit.
 *
 * @author semkagtn
 */
public class InMemoryKeyValueApi implements KeyValueApi {

    private final String name;
    private final ConcurrentMap<String, byte[]> map = new ConcurrentHashMap<>();

    public InMemoryKeyValueApi(final String name) {
        Require.nonEmpty(name, "empty name");
        this.name = name;
    }

    @Override
    public void put(final String key, final byte[] value) {
        Require.nonEmpty(key, "empty key");
        Require.nonNull(value, "null value");
        map.put(key, value);
    }

    @Override
    public Optional<byte[]> get(final String key) {
        Require.nonEmpty(key, "empty key");
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public void delete(final String key) {
        Require.nonEmpty(key, "empty key");
        map.remove(key);
    }
}
