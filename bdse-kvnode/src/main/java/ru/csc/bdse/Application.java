package ru.csc.bdse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.csc.bdse.kv.CoordinatorKeyValueApi;
import ru.csc.bdse.kv.KeyValueApi;
import ru.csc.bdse.kv.MongoDBKeyValueApi;
import ru.csc.bdse.util.Env;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static String randomNodeName() {
        return "kvnode-" + UUID.randomUUID().toString().substring(4);
    }

    @Value("${coordinator.write.consistency.level}")
    public int writeConsistencyLevel;

    @Value("${coordinator.read.consistency.level}")
    public int readConsistencyLevel;

    @Value("${coordinator.nodes.number}")
    public int nodesNumber;

    @Value("${coordinator.timeout}")
    public int timeout;

    @Bean
    KeyValueApi node() {
        String nodeName = Env.get(Env.KVNODE_NAME).orElseGet(Application::randomNodeName);
        return new MongoDBKeyValueApi(nodeName);
    }

    @Bean
    CoordinatorKeyValueApi coordinator() {
        String nodeName = Env.get(Env.KVNODE_NAME).orElseGet(Application::randomNodeName);

        List<KeyValueApi> nodes = IntStream.range(0, nodesNumber)
                .mapToObj(x -> node()).collect(Collectors.toList());

        return new CoordinatorKeyValueApi(nodeName,
                nodes, writeConsistencyLevel, readConsistencyLevel, timeout);
    }
}