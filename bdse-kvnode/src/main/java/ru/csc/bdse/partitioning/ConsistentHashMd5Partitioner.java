package ru.csc.bdse.partitioning;

import java.util.List;

/**
 * Selects partition by consistent hashing circle
 *
 * @author alesavin
 */
public class ConsistentHashMd5Partitioner implements Partitioner {

    private final ConsistentHash ch;

    public ConsistentHashMd5Partitioner(List<String> partitions) {
        this.ch = new ConsistentHash(HashingFunctions.md5Function, 3, partitions);
    }

    @Override
    public String getPartition(String key) {
        return ch.get(key);
    }
}
