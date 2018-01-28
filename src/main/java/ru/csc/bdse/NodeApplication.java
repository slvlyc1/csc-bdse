package ru.csc.bdse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.csc.bdse.impl.kv.InMemoryKeyValueStorageNode;
import ru.csc.bdse.model.kv.KeyValueStorageNode;

@SpringBootApplication
public class NodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(NodeApplication.class, args);
	}

	@Bean
    KeyValueStorageNode node() {
	    return new InMemoryKeyValueStorageNode("node0");
    }
}
