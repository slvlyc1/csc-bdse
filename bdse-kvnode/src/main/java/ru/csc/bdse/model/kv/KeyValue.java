package ru.csc.bdse.model.kv;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Represent key-value interface
 *
 * @author alesavin
 */
public interface KeyValue<K, V> {

    void put(K key, V value) throws Exception;

    Optional<V> get(K key) throws Exception;

    Set<K> keys(Predicate<K> predicate) throws Exception;
}
