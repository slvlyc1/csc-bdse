package ru.csc.bdse.app.phonebook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.csc.bdse.app.phonebook.advanced.AdvancedRecord;
import ru.csc.bdse.app.phonebook.advanced.AdvancedRecordSerializer;
import ru.csc.bdse.app.phonebook.simple.SimpleRecord;
import ru.csc.bdse.app.phonebook.simple.SimpleRecordSerializer;
import ru.csc.bdse.util.Constants;
import ru.csc.bdse.util.Require;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PhoneBookApiHttpClient implements PhoneBookApi {

    private final String baseUrl;
    private final RestTemplate rest = new RestTemplate();
    private final SimpleRecordSerializer serializer = new SimpleRecordSerializer();
    private final AdvancedRecordSerializer advancedRecordSerializer = new AdvancedRecordSerializer();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PhoneBookApiHttpClient(String baseUrl) {
        Require.nonEmpty(baseUrl,"base phone book url should not be empty");
        this.baseUrl = baseUrl;
    }

    @Override
    public void put(Record record) {
        Require.nonNull(record, "Record should not be empty");
        String url = baseUrl + "/phone-book";
        byte[] value = serializeRecord(record);
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.PUT, value);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Response error: " + responseEntity);
        }
    }

    @Override
    public void delete(Record record) {
        Require.nonNull(record, "Record should not be empty");
        String url = baseUrl + "/phone-book/delete";
        byte[] value = serializeRecord(record);
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.PUT, value);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Response error: " + responseEntity);
        }
    }

    @Override
    public Set get(char literal) {
        final String url = baseUrl + "/key-value/" + literal;
        final ResponseEntity<byte[]> responseEntity = request(url, HttpMethod.GET, Constants.EMPTY_BYTE_ARRAY);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return new HashSet<>(Arrays.asList(readAs(responseEntity.getBody(), String[].class)));
        } else {
            throw new RuntimeException("Response error: " + responseEntity);
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

    private byte[] serializeRecord(Record record) {
        try {
            Require.nonNull(record, "Record should not be empty");
            if (record instanceof SimpleRecord) {
                return serializer.serializeRecord(record);
            } else if (record instanceof AdvancedRecord) {
                return advancedRecordSerializer.serializeRecord(record);
            } else
                throw new IllegalArgumentException("Record should be instanceof either SimpleRecord or AdvancesREcord");
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private <T> T readAs(byte[] src, Class<T> valueType) {
        try {
            return objectMapper.readValue(src, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Response error: " + e.getMessage());
        }
    }
}
