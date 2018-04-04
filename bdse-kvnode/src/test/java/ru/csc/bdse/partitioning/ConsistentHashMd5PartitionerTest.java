package ru.csc.bdse.partitioning;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests for [[ConsistentHashMd5Partitioner]]
 *
 * @author alesavin
 */
public class ConsistentHashMd5PartitionerTest {

    @Test
    public void mapsToNodeByConsistentHashingForSerialKeys() {

        Set<String> keys =
                Stream.iterate(1000, n -> n + 3).limit(100).map(String::valueOf).collect(Collectors.toSet());

        final Set<String> partitions = new HashSet<>(Arrays.asList("0", "1", "2"));
        final Map<String, String> map = PartitionerUtils.getAll(
                new ConsistentHashMd5Partitioner(partitions),
                keys);

        final Set<String> partitions2 = new HashSet<>(Arrays.asList("0", "2"));
        final Map<String, String> map2 = PartitionerUtils.getAll(
                new ConsistentHashMd5Partitioner(partitions2),
                keys);

        // There less than half of the keys to be moved
        assertThat(PartitionerUtils.diffStatistics(partitions, map, map2).values().stream().mapToLong(Math::abs).sum()).as("no diffs")
                .isLessThan(keys.size() / 2);
    }
}