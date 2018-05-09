package ru.csc.bdse.kv;

import ru.csc.bdse.partitioning.FirstLetterPartitioner;
import ru.csc.bdse.partitioning.Partitioner;

import java.util.Set;

public class FirstLetterPartitionerKeyValueApiTest extends AbstractPartitionedKeyValueApiHttpClientTest {
    @Override
    Partitioner getPartitioner(Set<String> nodes) {
        return new FirstLetterPartitioner(nodes);
    }

    @Override
    protected float expectedKeysLossProportion() {
        return (float)node1.getKeys("").size()/keys.size();
    }

    protected float expectedUndeletedKeysProportion() {
        return expectedKeysLossProportion();
    }
}
