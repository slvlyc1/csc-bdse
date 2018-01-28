package ru.csc.bdse.model.kv;

import java.util.Iterator;

/**
 * Represent key-value interface
 *
 * @author alesavin
 */
public interface KeyValue<K, V> {

    void upsert(K key, V value);

    V get(K key);

    V delete(K key); // TODO use upsert?

    Iterator<K> keysIterator();
}
