package ru.csc.bdse.model.client;

import java.io.IOException;
import java.util.Optional;

/**
 * TODO
 *
 * @author alesavin
 */
public interface KeyValueStorageNodeClient {

    void upsert(String key, byte[] value) throws IOException;

    byte[] get(String key) throws IOException;

    String[] keys(Optional<String> prefix) throws IOException;
}
