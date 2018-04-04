package ru.csc.bdse.partitioning;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Some convenient methods for tests
 *
 * @author alesavin
 */
public class PartitionerUtils {

    public static Map<String, String> getAll(final Partitioner partitioner,
                                             Set<String> keys) {
        return keys.stream().collect(Collectors.toMap(key -> key, partitioner::getPartition));
    }

    public static Map<String, Long> statistics(Map<String, String> all) {
        return all.values().stream().collect(Collectors.groupingBy(
                v -> v,
                Collectors.counting()));
    }

    public static Map<String, Long> diffStatistics(Set<String> partitions,
                                                   Map<String, String> left,
                                                   Map<String, String> right) {
        Map<String, Long> leftStatistics = statistics(left);
        Map<String, Long> rightStatistics = statistics(right);
        Map<String, Long> diff = new HashMap<>();

        for (String partition: partitions) {
            Long l = leftStatistics.getOrDefault(partition, 0L);
            Long r = rightStatistics.getOrDefault(partition, 0L);
            diff.put(partition, l - r);
        }
        return diff;
    }
}
