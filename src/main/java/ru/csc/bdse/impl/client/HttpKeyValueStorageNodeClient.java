package ru.csc.bdse.impl.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.csc.bdse.model.client.KeyValueStorageNodeClient;

import java.io.IOException;
import java.util.Optional;

/**
 * Client for [[KeyValueStorageNode]] HTTP API
 *
 * @author alesavin
 */
public class HttpKeyValueStorageNodeClient implements KeyValueStorageNodeClient {

    private final String baseUrl;
    private final RestTemplate rest;
    private final HttpHeaders headers;

    public HttpKeyValueStorageNodeClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.rest = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    @Override
    public void upsert(String key, byte[] value) throws IOException {
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(value, headers);
        ResponseEntity<Void> responseEntity =
                rest.postForEntity(baseUrl + "/upsert/" + key, requestEntity, Void.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IOException("Node return " + responseEntity);
    }

    @Override
    public byte[] get(String key) throws IOException {
        ResponseEntity<byte[]> responseEntity =
                rest.getForEntity(baseUrl + "/get/" + key, byte[].class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IOException("Node return " + responseEntity);
        return responseEntity.getBody();
    }

    @Override
    public String[] keys(Optional<String> prefix) throws IOException {
        StringBuilder sb = new StringBuilder().append(baseUrl).append("/keys");
        prefix.ifPresent(s -> sb.append("?prefix=").append(s));
        ResponseEntity<String> responseEntity =
                rest.getForEntity(sb.toString(), String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IOException("Node return " + responseEntity);
        return responseEntity.getBody().split("\n");
    }
}
