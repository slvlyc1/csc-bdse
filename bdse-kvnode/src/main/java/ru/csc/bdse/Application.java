package ru.csc.bdse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.csc.bdse.kv.CoordinatorKeyValueApi;
import ru.csc.bdse.kv.KeyValueApi;
import ru.csc.bdse.kv.MongoDBKeyValueApi;
import ru.csc.bdse.util.Env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static String randomNodeName() {
        return "kvnode-" + UUID.randomUUID().toString().substring(4);
    }

    @Bean
    KeyValueApi node() {
        String nodeName = Env.get(Env.KVNODE_NAME).orElseGet(Application::randomNodeName);
        return new MongoDBKeyValueApi(nodeName);
    }

    @Bean
    CoordinatorKeyValueApi coordinator() {
        return new CoordinatorKeyValueApi(
                new ArrayList<>(
                    Arrays.asList(
                        node(),
                        node(),
                        node()
                    )
                ), 4, 2, 1000
        );
    }
}