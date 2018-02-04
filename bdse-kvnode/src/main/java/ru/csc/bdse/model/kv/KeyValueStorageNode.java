package ru.csc.bdse.model.kv;

import java.io.IOException;

/**
 * Interface for key-value storage node
 *
 * @author alesavin
 */
public interface KeyValueStorageNode
        extends KeyValue<String, byte[]>, NodeMaintenance {
}
