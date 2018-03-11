package ru.csc.bdse.app.phonebook.simple;

import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.kv.KeyValueApiClient;

import java.util.Set;

public class SimplePhoneBookApi implements PhoneBookApi {

    private final KeyValueApiClient keyValueApiClient;

    public SimplePhoneBookApi(String nodeUrl) {
        keyValueApiClient = new KeyValueApiClient(nodeUrl);
    }

    @Override
    public void put(Record record) {
        //keyValueApiClient.put(record.getName(), serialize(record));
        throw new RuntimeException("not implemented");
    }

    @Override
    public void delete(Record record) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Set get(char literal) {
        throw new RuntimeException("not implemented");    }

}
