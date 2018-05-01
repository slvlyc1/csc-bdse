package ru.csc.bdse.kv;

import java.util.Set;

public interface ConflictResolver {
    RecordWithTimestamp resolve(Set<RecordWithTimestamp> in);
    Set<String> resolveKeys(Set<Set<String>> in);
}