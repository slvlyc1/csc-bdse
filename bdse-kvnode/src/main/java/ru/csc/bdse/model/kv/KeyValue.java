package ru.csc.bdse.model.kv;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represent key-value interface
 *
 * @author alesavin
 */
public interface KeyValue<K, V> {

    void upsert(K key, V value) throws Exception;

    V get(K key) throws Exception;

    Set<K> keys(Predicate<K> predicate) throws Exception;
}
