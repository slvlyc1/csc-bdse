package ru.csc.bdse.app.phonebook.advanced;

import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.Record;

import java.util.Set;

public class AdvancedPhoneBookApi implements PhoneBookApi {
    @Override
    public void put(Record record) {
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
