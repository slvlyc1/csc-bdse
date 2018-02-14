package ru.csc.bdse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.csc.bdse.impl.kv.InMemoryKeyValueApi;
import ru.csc.bdse.model.kv.KeyValueApi;

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
        String nodeName = System.getenv("KVNODE_NAME");
        if (nodeName == null)
            nodeName = randomNodeName();
        return new InMemoryKeyValueApi(nodeName);
    }
}
