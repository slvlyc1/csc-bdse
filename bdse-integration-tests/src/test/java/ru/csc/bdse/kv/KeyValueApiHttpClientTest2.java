package ru.csc.bdse.kv;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.client.HttpServerErrorException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.RemoteDockerImage;
import org.testcontainers.images.builder.ImageFromDockerfile;
import ru.csc.bdse.util.Env;
import ru.csc.bdse.util.Random;

import java.io.File;
import java.time.Duration;
import java.util.stream.IntStream;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.*;

/**
 * Test have to be implemented
 *
 * @author alesavin
 */
public class KeyValueApiHttpClientTest2 {

    private static final int THREADS = 100;

    private static GenericContainer mongo;
    private static GenericContainer node;

    @BeforeClass
    public static void setUp() {
        final Network network = Network.newNetwork();
        mongo = new GenericContainer(new RemoteDockerImage("mongo", "latest"))
                .withExposedPorts(TestEnv.MONGO_PORT)
                .withNetwork(network)
                .withNetworkAliases(TestEnv.MONGO_HOST)
                .withStartupTimeout(Duration.of(30, SECONDS));

        mongo.start();

        node = new GenericContainer(
                new ImageFromDockerfile()
                        .withFileFromFile("target/bdse-kvnode-0.0.1-SNAPSHOT.jar", new File
                                ("../bdse-kvnode/target/bdse-kvnode-0.0.1-SNAPSHOT.jar"))
                        .withFileFromClasspath("Dockerfile", "kvnode/Dockerfile"))
                .withEnv(Env.KVNODE_NAME, TestEnv.NODE_NAME)
                .withEnv(Env.MONGO_HOSTNAME, TestEnv.MONGO_HOST)
                .withEnv(Env.MONGO_PORT, String.valueOf(TestEnv.MONGO_PORT))
                .withExposedPorts(8080)
                .withNetwork(network)
                .withStartupTimeout(Duration.of(30, SECONDS));
        node.start();

    }

    private KeyValueApi api = newKeyValueApi();

    private KeyValueApi newKeyValueApi() {
        final String baseUrl = "http://localhost:" + node.getMappedPort(8080);
        return new KeyValueApiHttpClient(baseUrl);
    }

    @Test
    public void
    concurrentPuts() {
        api.action(TestEnv.NODE_NAME, NodeAction.UP);

        final String key = "key";
        final byte[] value = Random.nextValue();
        final Thread[] threads = new Thread[THREADS];
        IntStream.range(0, THREADS).forEach(i -> threads[i] = new Thread(() -> api.put(key, value)));
        IntStream.range(0, THREADS).forEach(i -> threads[i].start());
        IntStream.range(0, THREADS).forEach(i -> {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        });
        assertTrue(api.get(key).isPresent());
        assertArrayEquals(value, api.get(key).get());
        api.delete(key);
    }

    @Test
    public void concurrentDeleteAndKeys() {
        api.action(TestEnv.NODE_NAME, NodeAction.UP);

        final String firstKey = "key_1";
        final byte[] firstValue = Random.nextValue();
        final String secondKey = "key_2";
        final byte[] secondValue = Random.nextValue();
        final Thread[] threads = new Thread[THREADS];

        api.put(firstKey, firstValue);
        api.put(secondKey, secondValue);

        IntStream.range(0, THREADS).forEach(i -> threads[i] = new Thread(() -> api.delete(firstKey)));
        IntStream.range(0, THREADS).forEach(i -> threads[i].start());
        IntStream.range(0, THREADS).forEach(i -> {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.out.println(e.toString());
            }
        });

        assertFalse(api.get(firstKey).isPresent());
        assertTrue(api.get(secondKey).isPresent());
        assertArrayEquals(secondValue, api.get(secondKey).get());

        api.delete(firstKey);
        api.delete(secondKey);
    }

    @Test
    public void actionUpDown() {
        api.action(TestEnv.NODE_NAME, NodeAction.DOWN);

        NodeInfo info = api.getInfo().stream()
                .filter(x -> x.getName().equals(TestEnv.NODE_NAME))
                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("Info for node '%s' was not found", TestEnv.NODE_NAME)));
        assertEquals(NodeStatus.DOWN, info.getStatus());

        api.action(TestEnv.NODE_NAME, NodeAction.UP);

        info = api.getInfo().stream()
                .filter(x -> x.getName().equals(TestEnv.NODE_NAME))
                .findFirst().orElseThrow(() -> new IllegalStateException(String.format("Info for node '%s' was not found", TestEnv.NODE_NAME)));
        assertEquals(NodeStatus.UP, info.getStatus());
    }

    @Test(expected = HttpServerErrorException.class)
    public void putWithStoppedNode() {
        api.action(TestEnv.NODE_NAME, NodeAction.DOWN);

        final String key = Random.nextKey();
        final byte[] value = Random.nextValue();

        api.put(key, value);
    }

    @Test(expected = HttpServerErrorException.class)
    public void getWithStoppedNode() {
        api.action(TestEnv.NODE_NAME, NodeAction.DOWN);
        final String key = Random.nextKey();
        api.get(key);
    }

    @Test(expected = HttpServerErrorException.class)
    public void getKeysByPrefixWithStoppedNode() {
        api.action(TestEnv.NODE_NAME, NodeAction.DOWN);
        final String prefix = Random.nextKey();
        api.getKeys(prefix);
    }

    @Test
    public void deleteByTombstone() {
        // TODO use tombstones to mark as deleted (optional)
    }

    @Test
    public void loadMillionKeys()  {
        //TODO load too many data (optional)
    }
}


