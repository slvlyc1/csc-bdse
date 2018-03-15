package ru.csc.bdse.app.phonebook.advanced;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.app.phonebook.RecordSerializer;
import ru.csc.bdse.kv.KeyValueApiClient;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AdvancedPhoneBookApi implements PhoneBookApi {

    private static final Logger logger = LoggerFactory.getLogger(AdvancedPhoneBookApi.class);
    private static final AtomicInteger uid = new AtomicInteger(0);

    private final KeyValueApiClient keyValueApiClient;
    private final RecordSerializer serializer;

    public AdvancedPhoneBookApi(String nodeUrl, RecordSerializer serializer) {
        keyValueApiClient = new KeyValueApiClient(nodeUrl);
        this.serializer = serializer;
    }

    @Override
    public void put(Record record) {

        try {
            final byte[] json = serializer.serializeRecord(record);
            String key1 = record.getSurname() + LocalTime.now() + uid.incrementAndGet();
            String key2 = record.getNickname() + LocalTime.now() + uid.incrementAndGet();

            keyValueApiClient.put(key1, json);
            keyValueApiClient.put(key2, json);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override //todo: move to common code
    public void delete(Record recordToBeDeleted) {
        Set<String> keys = new HashSet<>();
        for (Character literal: recordToBeDeleted.literals())
            keys.addAll(keyValueApiClient.getKeys(String.valueOf(literal)));

        for (String key: keys) {
            keyValueApiClient.getValue(key).ifPresent(value -> {
                try {
                    Record record = serializer.deserializeRecord(value);
                    if (record.equals(recordToBeDeleted)) keyValueApiClient.delete(key);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            });

        }
    }

    @Override
    public Set get(char literal) {

        Set<Record> res = new HashSet<>();

        Set<String> keys = keyValueApiClient.getKeys(String.valueOf(literal));

        for (String key : keys) {
            keyValueApiClient.getValue(key)
                    .ifPresent(value -> {
                        try {
                            res.add(serializer.deserializeRecord(value));
                        } catch (Exception e) {
                            logger.error(e.toString());
                        }
                    });
        }
        return res;
    }
}
