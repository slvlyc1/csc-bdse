package ru.csc.bdse.kv;

import ru.csc.bdse.partitioning.ConsistentHashMd5Partitioner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsistantHashPartitionedKeyValueApiHttpClientTest extends AbstractPartitionedKeyValueApiHttpClientTest {

    private static final KeyValueApi node0 = new InMemoryKeyValueApi("node0");
    private static final KeyValueApi node1 = new InMemoryKeyValueApi("node1");
    private static final KeyValueApi node2 = new InMemoryKeyValueApi("node2");
    private static final Set<String> keys = IntStream.range(0, 1000).mapToObj(String::valueOf).collect(Collectors.toSet());

    @Override
    protected KeyValueApi newCluster1() {
        Map<String, KeyValueApi> nodes = new HashMap<String, KeyValueApi>(){{
            put("node0", node0);
            put("node1", node1);
            put("node2", node2);
        }};

        return new PartitioningKeyValueApi(nodes, 3000, new ConsistentHashMd5Partitioner(nodes.keySet()));
    }

    @Override
    protected KeyValueApi newCluster2() {
        Map<String, KeyValueApi> nodes = new HashMap<String, KeyValueApi>(){{
            put("node0", node0);
            put("node2", node2);
        }};

        return new PartitioningKeyValueApi(nodes, 3000, new ConsistentHashMd5Partitioner(nodes.keySet()));
    }

    @Override
    protected Set<String> keys() {
        return keys;
    }

    @Override
    protected float expectedKeysLossProportion() {
        return (float)node1.getKeys("").size()/keys.size();
    }

    @Override
    protected float expectedUndeletedKeysProportion() {
        return expectedKeysLossProportion();
    }
}
