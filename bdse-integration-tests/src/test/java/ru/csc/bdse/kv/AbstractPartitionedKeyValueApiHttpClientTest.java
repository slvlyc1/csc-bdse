package ru.csc.bdse.kv;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import java.util.Set;


/**
 * Test have to be implemented
 *
 * @author alesavin
 */
@FixMethodOrder(MethodSorters.JVM)
public abstract class AbstractPartitionedKeyValueApiHttpClientTest {


    protected abstract KeyValueApi newCluster1();
    protected abstract KeyValueApi newCluster2();
    protected abstract Set<String> keys();
    protected abstract float expectedKeysLossProportion();
    protected abstract float expectedUndeletedKeysProportion();

    private KeyValueApi cluster1 = newCluster1();
    private KeyValueApi cluster2 = newCluster2();

    private Set<String> keys = keys();

    @Test
    public void put1000KeysAndReadItCorrectlyOnCluster1() {
        //put 1000 keys to storage ant read it
        for (String key: keys) {
            cluster1.put(key, intToByteArray(Integer.valueOf(key)));
        }

        for (String key: keys) {
            Assert.assertArrayEquals(intToByteArray(Integer.valueOf(key)), cluster1.get(key).get());
        }
    }

    @Test
    public void readKeysFromCluster2AndCheckLossProportion() {
        //read all keys from cluster2 and made some statistics (related to expectedKeysLossProportion)
        int lost = 0;
        for (String key: keys) {
            if (!cluster2.get(key).isPresent()) lost++;
        }
        Assert.assertEquals(expectedKeysLossProportion(), (float) lost/keys.size(), 0);
    }

    @Test
    public void deleteAllKeysFromCluster2() {
        //try to delete all keys on cluster2
        keys.forEach(cluster2::delete);
        keys.forEach(key -> Assert.assertFalse(cluster2.get(key).isPresent()) );
    }

    @Test
    public void readKeysFromCluster1AfterDeletionAtCluster2() {
        //read all keys from cluster1, made some statistics (related to expectedUndeletedKeysProportion)
        Assert.assertEquals(expectedUndeletedKeysProportion(), (float)cluster1.getKeys("").size()/keys.size(), 0);
    }

    private byte[] intToByteArray( int data ) {

        byte[] result = new byte[4];

        result[0] = (byte) ((data & 0xFF000000) >> 24);
        result[1] = (byte) ((data & 0x00FF0000) >> 16);
        result[2] = (byte) ((data & 0x0000FF00) >> 8);
        result[3] = (byte) ((data & 0x000000FF));

        return result;
    }
}