package ru.csc.bdse.model.kv;

/**
 * Interface for key-value storage node
 *
 * @author alesavin
 */
public interface KeyValueStorageNode
        extends KeyValue<String, byte[]>, NodeMaintenance {
}
