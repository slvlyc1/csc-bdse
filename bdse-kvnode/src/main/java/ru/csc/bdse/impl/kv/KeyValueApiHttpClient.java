package ru.csc.bdse.impl.kv;

import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.csc.bdse.model.kv.KeyValueApi;
import ru.csc.bdse.proto.ClusterInfo;
import ru.csc.bdse.proto.NodeInfo;
import ru.csc.bdse.proto.NodeStatus;
import ru.csc.bdse.util.Constants;
import ru.csc.bdse.util.Require;

import java.util.Optional;

/**
 * Http client for storage unit.
 *
 * @author semkagtn
 */
public class KeyValueApiHttpClient implements KeyValueApi {

    private final String baseUrl;
    private final RestTemplate rest = new RestTemplate();

    public KeyValueApiHttpClient(final String baseUrl) {
        Require.nonEmpty(baseUrl, "empty base url");
        this.baseUrl = baseUrl;
    }

    @Override
    public void put(String key, byte[] value) {
        Require.nonNull(key, "null key");
        Require.nonNull(value, "null value");

        final String url = baseUrl + "/key-value/" + key;
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.PUT, value);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Request error: " + responseEntity);
        }
    }

    @Override
    public Optional<byte[]> get(String key) {
        Require.nonNull(key, "null key");

        final String url = baseUrl + "/key-value/" + key;
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.GET, Constants.EMPTY_BYTE_ARRAY);
        switch (responseEntity.getStatusCode()) {
            case OK:
                return Optional.of(responseEntity.getBody());
            case NOT_FOUND:
                return Optional.empty();
            default:
                throw new RuntimeException("Request error: " + responseEntity);
        }
    }

    @Override
    public void delete(String key) {
        Require.nonNull(key, "null key");

        final String url = baseUrl + "/key-value/" + key;
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.DELETE, Constants.EMPTY_BYTE_ARRAY);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Request error: " + responseEntity);
        }
    }

    @Override
    public ClusterInfo getClusterInfo() {
        final String url = baseUrl + "/cluster-info";
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.GET, Constants.EMPTY_BYTE_ARRAY);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Request error: " + responseEntity);
        }
        try {
            return ClusterInfo.parseFrom(responseEntity.getBody());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<byte[]> request(final String url,
                                           final HttpMethod method,
                                           final byte[] body) {
        try {
            return rest.exchange(url, method, new HttpEntity<>(body), byte[].class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(Constants.EMPTY_BYTE_ARRAY, e.getStatusCode());
        }
    }

}
