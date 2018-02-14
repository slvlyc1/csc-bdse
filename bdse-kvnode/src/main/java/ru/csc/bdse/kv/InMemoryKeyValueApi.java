package ru.csc.bdse.kv;

import ru.csc.bdse.proto.ClusterInfo;
import ru.csc.bdse.proto.NodeInfo;
import ru.csc.bdse.proto.NodeStatus;
import ru.csc.bdse.util.Require;
import ru.csc.bdse.kv.KeyValueApi;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

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
    public Set<String> getKeys(String prefix) {
        Require.nonNull(prefix, "null prefix");
        return map.keySet()
                .stream()
                .filter(key -> key.startsWith(prefix))
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(final String key) {
        Require.nonEmpty(key, "empty key");
        map.remove(key);
    }

    @Override
    public ClusterInfo getClusterInfo() {
        return ClusterInfo.newBuilder()
                .addNodes(NodeInfo.newBuilder()
                        .setName(name)
                        .setStatus(NodeStatus.UP))
                .build();
    }
}
