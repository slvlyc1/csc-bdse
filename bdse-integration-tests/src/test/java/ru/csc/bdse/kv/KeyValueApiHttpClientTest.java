package ru.csc.bdse.kv;

import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.images.RemoteDockerImage;
import org.testcontainers.images.builder.ImageFromDockerfile;
import ru.csc.bdse.util.Env;

import java.io.File;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * @author semkagtn
 */
public class KeyValueApiHttpClientTest extends AbstractKeyValueApiTest {

    private static final Network network = Network.newNetwork();

    @ClassRule
    public static final GenericContainer node = new GenericContainer(
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

    @ClassRule
    public static final GenericContainer mongo = new GenericContainer(new RemoteDockerImage("mongo", "latest"))
                .withExposedPorts(TestEnv.MONGO_PORT)
                .withNetwork(network)
                .withNetworkAliases(TestEnv.MONGO_HOST)
                .withStartupTimeout(Duration.of(30, SECONDS));

    @Override
    protected KeyValueApi newKeyValueApi() {
        final String baseUrl = "http://localhost:" + node.getMappedPort(8080);
        return new KeyValueApiHttpClient(baseUrl);
    }
}
