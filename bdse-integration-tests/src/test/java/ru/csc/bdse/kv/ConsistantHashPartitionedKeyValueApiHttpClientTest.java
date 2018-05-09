package ru.csc.bdse.kv;

import ru.csc.bdse.partitioning.ConsistentHashMd5Partitioner;
import ru.csc.bdse.partitioning.Partitioner;
import java.util.Set;

public class ConsistantHashPartitionedKeyValueApiHttpClientTest extends AbstractPartitionedKeyValueApiHttpClientTest {

    @Override
    Partitioner getPartitioner(Set<String> nodes) {
        return new ConsistentHashMd5Partitioner(nodes);
    }

    @Override
    protected float expectedKeysLossProportion() {
        return (float)node1.getKeys("").size()/keys.size();
    }

    protected float expectedUndeletedKeysProportion() {
        return expectedKeysLossProportion();
    }
}
