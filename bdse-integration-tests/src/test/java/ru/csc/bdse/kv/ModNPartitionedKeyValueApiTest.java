package ru.csc.bdse.kv;

import ru.csc.bdse.partitioning.ModNPartitioner;
import ru.csc.bdse.partitioning.Partitioner;

import java.util.Set;

public class ModNPartitionedKeyValueApiTest extends AbstractPartitionedKeyValueApiHttpClientTest {

    @Override
    Partitioner getPartitioner(Set<String> nodes) {
        return new ModNPartitioner(nodes);
    }

    @Override
    float expectedKeysLossProportion() {
        return keys.stream()
                .filter(x -> !firstClusterPartitioner.getPartition(x).equals(secondClusterPartitioner.getPartition(x)))
                .count() * 1.0f / keys.size();
    }

    @Override
    float expectedUndeletedKeysProportion() {
        return expectedKeysLossProportion();
    }
}
