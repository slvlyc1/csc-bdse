package ru.csc.bdse.kv;

import ru.csc.bdse.proto.ClusterInfo;

import java.util.Optional;
import java.util.Set;

/**
 * @author semkagtn
 */
public interface KeyValueApi {

    /**
     * Puts value to the storage by specified key.
     */
    void put(String key, byte[] value);

    /**
     * Returns value associated with specified key.
     */
    Optional<byte[]> get(String key);

    /**
     * Returns all keys with specified prefix.
     */
    Set<String> getKeys(String prefix);

     /**
     * Deletes value associated with specified key from the storage.
     */
    void delete(String key);

    /**
     * Returns info about all nodes in the cluster.
     */
    ClusterInfo getClusterInfo();
}
