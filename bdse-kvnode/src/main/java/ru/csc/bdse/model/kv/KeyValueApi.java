package ru.csc.bdse.model.kv;

import java.util.Optional;

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
     * Deletes value associated with specified key from the storage.
     */
    void delete(String key);

    // TODO getInfo

    // TODO turnOn/turnOff
}
