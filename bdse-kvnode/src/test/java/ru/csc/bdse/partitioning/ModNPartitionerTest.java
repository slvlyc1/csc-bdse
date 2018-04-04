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
 * Tests for [[ModNPartitioner]]
 *
 * @author alesavin
 */
public class ModNPartitionerTest {

    @Test
    public void mapsToNodeByModN() {
        Set<String> partitions = new HashSet<>(Arrays.asList("0", "1", "2"));
        final Partitioner partitioner = new ModNPartitioner(partitions);
        assertThat(partitioner.getPartition("123")).isEqualTo("0");
        assertThat(partitioner.getPartition("1234")).isEqualTo("1");
        assertThat(partitioner.getPartition("12345")).isEqualTo("0");
        assertThat(partitioner.getPartition("Aaaa")).isEqualTo("2");
    }

    @Test
    public void mapsToNodeByModNForSerialKeys() {

        Set<String> keys =
                Stream.iterate(1000, n -> n + 3).limit(100).map(String::valueOf).collect(Collectors.toSet());

        final Set<String> partitions = new HashSet<>(Arrays.asList("0", "1", "2"));
        final Map<String, String> map = PartitionerUtils.getAll(
                new ModNPartitioner(partitions),
                keys);

        final Set<String> partitions2 = new HashSet<>(Arrays.asList("0", "2"));
        final Map<String, String> map2 = PartitionerUtils.getAll(
                new ModNPartitioner(partitions2),
                keys);

        // There more than half of the keys to be moved
        assertThat(PartitionerUtils.diffStatistics(partitions, map, map2).values().stream().mapToLong(Math::abs).sum()).as("no diffs")
                .isGreaterThan(keys.size() / 2);
    }
}