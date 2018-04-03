package ru.csc.bdse.kv;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CoordinatorKeyValueApiHttpClient implements KeyValueApi {

    private final List<KeyValueApiHttpClient> clients;

    public CoordinatorKeyValueApiHttpClient(List<String> nodeBaseUrls) {
        this.clients = nodeBaseUrls.stream()
                .map(KeyValueApiHttpClient::new)
                .collect(Collectors.toList());
    }

    @Override
    public void put(String key, byte[] value) {
        for (KeyValueApiHttpClient client : clients) {
            try {
                client.put(key, value);
                return;
            }
            catch (RuntimeException e) {
                // will try to call the next client
            }
        }

        throw new RuntimeException("No client succeeded in operation.");
    }

    @Override
    public Optional<byte[]> get(String key) {
        for (KeyValueApiHttpClient client : clients) {
            try {
                return client.get(key);
            }
            catch (RuntimeException e) {
                // will try to call the next client
            }
        }

        throw new RuntimeException("No client succeeded in operation.");
    }

    @Override
    public Set<String> getKeys(String prefix) {
        for (KeyValueApiHttpClient client : clients) {
            try {
                return client.getKeys(prefix);
            }
            catch (RuntimeException e) {
                // will try to call the next client
            }
        }

        throw new RuntimeException("No client succeeded in operation.");
    }

    @Override
    public void delete(String key) {
        for (KeyValueApiHttpClient client : clients) {
            try {
                client.delete(key);
                return;
            }
            catch (RuntimeException e) {
                // will try to call the next client
            }
        }

        throw new RuntimeException("No client succeeded in operation.");
    }

    @Override
    public Set<NodeInfo> getInfo() {
        for (KeyValueApiHttpClient client : clients) {
            try {
                return client.getInfo();
            }
            catch (RuntimeException e) {
                // will try to call the next client
            }
        }

        throw new RuntimeException("No client succeeded in operation.");
    }

    @Override
    public void action(String node, NodeAction action) {
        for (KeyValueApiHttpClient client : clients) {
            try {
                client.action(node, action);
                return;
            }
            catch (RuntimeException e) {
                // will try to call the next client
            }
        }

        throw new RuntimeException("No client succeeded in operation.");
    }
}
