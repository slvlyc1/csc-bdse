package ru.csc.bdse.partitioning;

import java.util.List;

/**
 * Selects partition by first letter of a key
 *
 * @author alesavin
 */
public class FirstLetterPartitioner implements Partitioner {

    private final List<String> partitions;

    public FirstLetterPartitioner(List<String> partitions) {
        this.partitions = partitions;
    }

    @Override
    public String getPartition(String key) {
        int index = key.charAt(0) * partitions.size() / ((int)Character.MAX_VALUE + 1);
        return partitions.get(index);
    }
}
