package ru.csc.bdse.impl.client;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
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
    public void put(String key, byte[] value) throws IOException {
        HttpEntity<byte[]> requestEntity = value == null || value.length == 0 ?
                    new HttpEntity<>(headers) : new HttpEntity<>(value, headers);
        ResponseEntity<Void> responseEntity =
                rest.postForEntity(baseUrl + "/put/" + key, requestEntity, Void.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IOException("Node return " + responseEntity);
    }

    @Override
    public Optional<byte[]> get(String key) throws IOException {
        try {
            ResponseEntity<byte[]> responseEntity =
                    rest.getForEntity(baseUrl + "/get/" + key, byte[].class);
            if (responseEntity.getStatusCode().is2xxSuccessful())
                return Optional.of(responseEntity.getBody());
            else
                throw new IOException("Node return " + responseEntity);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                return Optional.empty();
            else
                throw e;
        }
    }

    @Override
    public String[] keys(Optional<String> prefix) throws IOException {
        StringBuilder sb = new StringBuilder().append(baseUrl).append("/keys");
        prefix.ifPresent(s -> sb.append("?prefix=").append(s));
        ResponseEntity<String> responseEntity =
                rest.getForEntity(sb.toString(), String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IOException("Node return " + responseEntity);
        String response = responseEntity.getBody();
        if (response == null)
            return new String[0];
        else
            return response.split("\n");
    }

    @Override
    public String status() throws IOException {
        ResponseEntity<String> responseEntity =
                rest.getForEntity(baseUrl + "/status", String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful())
            return responseEntity.getBody();
        else
            throw new IOException("Node return " + responseEntity);
    }

    @Override
    public void action(String node, String action) throws IOException {
        HttpEntity<Void> requestEntity = new HttpEntity<Void>(headers);
        ResponseEntity<Void> responseEntity =
                rest.postForEntity(baseUrl + "/action/" + node + "/" + action, requestEntity, Void.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IOException("Node return " + responseEntity);
    }
}
