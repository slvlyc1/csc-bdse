package ru.csc.bdse.partitioning;

import java.util.List;

/**
 * Selects partition by mod N
 *
 * @author alesavin
 */
public class ModNPartitioner implements Partitioner {

    private final List<String> partitions;

    public ModNPartitioner(List<String> partitions) {
        this.partitions = partitions;
    }

    @Override
    public String getPartition(String key) {
        int index = key.hashCode() % partitions.size();
        return partitions.get(index);
    }
}
