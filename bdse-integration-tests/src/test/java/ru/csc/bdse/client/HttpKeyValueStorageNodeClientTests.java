package ru.csc.bdse.client;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import ru.csc.bdse.impl.client.HttpKeyValueStorageNodeClient;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * Integration tests for kv-node
 *  Use http client and docker image
 *
 * @author alesavin
 */
public class HttpKeyValueStorageNodeClientTests {

    @Rule
    public GenericContainer node = new GenericContainer(
            new ImageFromDockerfile()
                    .withFileFromFile("target/bdse-kvnode-0.0.1-SNAPSHOT.jar", new File
                            ("../bdse-kvnode/target/bdse-kvnode-0.0.1-SNAPSHOT.jar"))
                    .withFileFromClasspath("Dockerfile", "kvnode/Dockerfile"))
            .withEnv("KVNODE_NAME", "node-0")
            .withExposedPorts(8080)
            .withStartupTimeout(Duration.of(20, SECONDS));

    private HttpKeyValueStorageNodeClient client;

    @Before
    public void before() {
        client = new HttpKeyValueStorageNodeClient("http://localhost:" + node.getMappedPort(8080));
    }


    @Test
    public void presentsKeysOnStart() throws IOException {
        assertThat(client.keys(Optional.empty())).isEmpty();
        assertThat(client.keys(Optional.of("A"))).isEmpty();
        assertThat(client.keys(Optional.of("B"))).isEmpty();
        assertThat(client.get("A").isPresent()).isFalse();
        assertThat(client.get("B").isPresent()).isFalse();
    }

    @Test
    public void writeAndRead() throws IOException {
        client.upsert("A", "aaaa".getBytes());
        assertThat(client.keys(Optional.empty())).isNotEmpty();
        assertThat(client.keys(Optional.of("A"))).isNotEmpty();
        assertThat(client.keys(Optional.of("B"))).isEmpty();
        assertThat(client.get("A").isPresent()).isTrue();
        assertThat(client.get("A").map(String::new)).isEqualTo(Optional.of("aaaa"));
        assertThat(client.get("B").isPresent()).isFalse();

        client.upsert("B", "bb".getBytes());
        client.upsert("CDEffffffffff", "".getBytes());

        assertThat(client.keys(Optional.empty()).length).isEqualTo(3);
        assertThat(client.get("A").map(String::new)).isEqualTo(Optional.of("aaaa"));
        assertThat(client.get("B").map(String::new)).isEqualTo(Optional.of("aaaa"));
    }

    @Test
    public void maintenance() throws IOException {
        assertThat(client.status()).isEqualTo("{node-0=up}");
        assertThat(client.keys(Optional.empty())).isEmpty();
        client.command("node-0", "down");
        assertThat(client.status()).isEqualTo("{node-0=down}");

        try {
            client.keys(Optional.empty());
            fail("Expected an HttpServerErrorException to be thrown");
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR)
                throw e;
        }
        try {
            client.get("A");
            fail("Expected an HttpServerErrorException to be thrown");
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR)
                throw e;
        }
        try {
            client.upsert("A", "".getBytes());
            fail("Expected an HttpServerErrorException to be thrown");
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() != HttpStatus.INTERNAL_SERVER_ERROR)
                throw e;
        }

        client.command("node-0", "UP");
        assertThat(client.status()).isEqualTo("{node-0=up}");
        assertThat(client.keys(Optional.empty())).isEmpty();
    }

    @Test
    public void getKeysByPrefix() throws IOException {
    }

    @Test
    public void deleteByKey() throws IOException {
        // TODO use tombstones
    }

    @Test
    public void concurrentUpdate() throws IOException {
        // TODO simultanious update of the key value
    }

    @Test
    public void concurrentDeleteAndKeys() throws IOException {
        //TODO simultanious delete by key and keys listing
    }

    @Test
    public void updateGetWithStoppedDB() throws IOException {
        //TODO test update, get, keys if DBMS container was stopped
    }

    @Test
    public void loadMillionKeys() throws IOException {
    }
}