package ru.csc.bdse.model.kv;

import java.util.Properties;

/**
 * Interface for node maintenance
 *
 * @author alesavin
 */
public interface NodeMaintenance {

    Properties status();

    void action(String node, Action action);
}
