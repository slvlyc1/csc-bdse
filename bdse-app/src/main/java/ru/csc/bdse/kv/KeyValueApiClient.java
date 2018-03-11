package ru.csc.bdse.kv;

import java.util.Set;

public class KeyValueApiClient {

    private final KeyValueApiHttpClient api;

    public KeyValueApiClient(String url) {
        this.api = new KeyValueApiHttpClient(url);
    }

    public void put(String key, byte[] value) {
        api.put(key, value);
    }

    public Set<String> getKeys(String prefix) {
        return api.getKeys(prefix);
    }

    public void delete(String key) {
        api.delete(key);
    }
}
