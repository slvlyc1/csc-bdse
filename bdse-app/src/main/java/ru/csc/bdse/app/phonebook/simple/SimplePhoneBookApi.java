package ru.csc.bdse.app.phonebook.simple;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.csc.bdse.app.phonebook.Helper;
import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.kv.KeyValueApiClient;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class SimplePhoneBookApi implements PhoneBookApi {

    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    private static final AtomicInteger uid = new AtomicInteger(0);

    private final KeyValueApiClient keyValueApiClient;

    public SimplePhoneBookApi(String nodeUrl) {
        keyValueApiClient = new KeyValueApiClient(nodeUrl);
    }

    @Override
    public void put(Record record) {
        try {
            final byte[] json = Helper.serializeRecord(record);
            String key = record.getSurname() + LocalTime.now() + uid.incrementAndGet();
            keyValueApiClient.put(key, json);
        } catch (JsonProcessingException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void delete(Record recordToBeDeleted) {
        Set<String> keys = new HashSet<>();
        for (Character literal: recordToBeDeleted.literals())
            keys.addAll(keyValueApiClient.getKeys(String.valueOf(literal)));

        for (String key: keys) {
            keyValueApiClient.getValue(key).ifPresent(value -> {
                try {
                    Record record = Helper.deserializeRecord(value);
                    if (record.equals(recordToBeDeleted)) keyValueApiClient.delete(key);
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            });

        }
    }


    @Override
    public Set get(char literal) {
        Set<Record> res = new HashSet<>();

        Set<String> keys = keyValueApiClient.getKeys(String.valueOf(literal));

        for (String key: keys) {
            keyValueApiClient.getValue(key)
                    .ifPresent(value -> {
                        try {
                            res.add(Helper.deserializeRecord(value));
                        } catch (IOException e) {
                            logger.error(e.toString());
                        }
                    });
        }
        return res;
    }
}
