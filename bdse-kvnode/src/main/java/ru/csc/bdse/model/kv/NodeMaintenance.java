package ru.csc.bdse.model.kv;

import java.util.Map;
import java.util.Properties;

/**
 * Interface for node maintenance
 *
 * @author alesavin
 */
public interface NodeMaintenance {

    Properties status();

    void command(String node, String command);
}
