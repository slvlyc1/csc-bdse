package ru.csc.bdse.app.phonebook.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.app.phonebook.RecordSerializer;
import ru.csc.bdse.kv.KeyValueApiClient;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SimplePhoneBookApi implements PhoneBookApi {

    private static final Logger logger = LoggerFactory.getLogger(SimplePhoneBookApi.class);
    private static final AtomicInteger uid = new AtomicInteger(0);

    private final KeyValueApiClient keyValueApiClient;
    private final RecordSerializer serializer;

    public SimplePhoneBookApi(String nodeUrl, RecordSerializer serializer) {
        keyValueApiClient = new KeyValueApiClient(nodeUrl);
        this.serializer = serializer;
    }

    @Override
    public void put(Record record) {
        try {
            final byte[] json = serializer.serializeRecord(record);

            String key = record.getSurname() + LocalTime.now() + uid.incrementAndGet();
            keyValueApiClient.put(key, json);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
            throw new RuntimeException(String.format("Failed to add record: '%s'. Incorrect format.", record));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Internal storage is unavailable");
        }
    }

    @Override
    public void delete(Record recordToBeDeleted) {
        try {
            Set<String> keys = new HashSet<>();
            for (Character literal : recordToBeDeleted.literals())
                keys.addAll(keyValueApiClient.getKeys(String.valueOf(literal)));

            for (String key : keys) {
                keyValueApiClient.getValue(key).ifPresent(value -> {
                    try {
                        Record record = serializer.deserializeRecord(value);
                        if (record.equals(recordToBeDeleted)) keyValueApiClient.delete(key);
                    } catch (IOException e) {
                        logger.error(e.toString());
                    }
                });

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Internal storage is unavailable");
        }
    }


    @Override
    public Set get(char literal) {
        try {
            Set<Record> res = new HashSet<>();

            Set<String> keys = keyValueApiClient.getKeys(String.valueOf(literal));

            for (String key : keys) {
                keyValueApiClient.getValue(key)
                        .ifPresent(value -> {
                            try {
                                res.add(serializer.deserializeRecord(value));
                            } catch (IOException e) {
                                logger.error(e.toString());
                            }
                        });
            }
            return res;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Internal storage is unavailable");
        }
    }
}
