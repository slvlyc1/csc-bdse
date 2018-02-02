package ru.csc.bdse.client;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import ru.csc.bdse.impl.client.HttpKeyValueStorageNodeClient;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * TODO
 *
 * @author alesavin
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpKeyValueStorageNodeClientTests {

    @Rule
    public GenericContainer node = new GenericContainer(
            new ImageFromDockerfile()
                .withFileFromFile("target/bdse-0.0.1-SNAPSHOT.jar", new File("target/bdse-0.0.1-SNAPSHOT.jar"))
                .withFileFromClasspath("Dockerfile", "node/Dockerfile"))
                .withCommand("java",  "-jar", "/app.jar",  "ru.csc.bdse.NodeApplication")
                .withExposedPorts(8080)
                .withStartupTimeout(Duration.of(5, SECONDS));

    private HttpKeyValueStorageNodeClient client;

    @Before
    public void before() {
        client = new HttpKeyValueStorageNodeClient("http://localhost:" + node.getMappedPort(8080));
    }

    @Test
    public void a() throws IOException {
        assertThat(client.keys(Optional.empty())).isEmpty();
        assertThat(client.get("A")).isEmpty();
    }
}
