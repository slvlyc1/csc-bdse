package ru.csc.bdse.kv;

import java.util.Set;

public class LatestValueConflictResolver implements ConflictResolver {
    @Override
    public RecordWithTimestamp resolve(Set<RecordWithTimestamp> in) {
        return null;
    }

    @Override
    public Set<String> resolveKeys(Set<Set<String>> in) {
        return null;
    }
}
