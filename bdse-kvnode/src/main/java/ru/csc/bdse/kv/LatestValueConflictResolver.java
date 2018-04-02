package ru.csc.bdse.kv;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LatestValueConflictResolver implements ConflictResolver {
    @Override
    public RecordWithTimestamp resolve(Set<RecordWithTimestamp> in) {

        if (in == null || in.isEmpty()) {
            throw new IllegalArgumentException("Resolve input set should not be null or empty");
        }

        long maxTimestamp = in.stream().mapToLong(RecordWithTimestamp::getTimestamp).max().getAsLong();

        RecordWithTimestamp recordWithTimestamp = in.stream()
                // filter out the latest version only
                .filter(record -> record.getTimestamp() == maxTimestamp)
                // grouping by the value
                .collect(Collectors.groupingBy(record -> new String(record.getPayload())))
                // taking the most common value
                .entrySet().stream().max(Comparator.comparingInt(entry -> entry.getValue().size()))
                .map(Map.Entry::getValue).get().stream()
                // taking the record with the least NodeId hashCode
                .min(Comparator.comparingInt(record -> record.getNodeId().hashCode())).get();

        return recordWithTimestamp;
    }

    @Override
    public Set<String> resolveKeys(Set<Set<String>> in) {
        return null;
    }
}
