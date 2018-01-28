package ru.csc.bdse.model.kv;

import java.util.Map;

/**
 * Interface for node maintenance
 *
 * @author alesavin
 */
public interface NodeMaintenance {

    Map<String, Integer> status(); //TODO

    void shutdown(String node);

    void start(String node);
}
