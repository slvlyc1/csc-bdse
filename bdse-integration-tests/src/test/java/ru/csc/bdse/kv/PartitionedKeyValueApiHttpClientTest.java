package ru.csc.bdse.kv;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test have to be implemented
 *
 * @author alesavin
 */
public abstract class PartitionedKeyValueApiHttpClientTest {

    protected abstract KeyValueApi newCluster1();
    protected abstract KeyValueApi newCluster2();
    protected abstract float expectedKeysLossProportion();

    private KeyValueApi cluster1 = newCluster1();
    private KeyValueApi cluster2 = newCluster2();
    private int keysCount = 1000;

/*
    private Collection<String> keys =
            Stream.generate(() -> RandomStringUtils.random(10)).limit(keysCount).collect(Collectors.toList());
*/

    @Test
    public void put1000KeysAndReadItCorrectlyOnCluster1() {
//        keys.forEach(key -> cluster1.put(key, key.getBytes()));

        // TODO put 1000 keys to storage ant read it: getKeys(), get()
    }

    @Test
    public void readKeysFromCluster2AndCheckLossProportion() {
        // TODO read all keys from cluster2
    }
}


