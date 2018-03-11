package ru.csc.bdse.app.phonebook.simple;

import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.util.Require;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class SimpleRecord implements Record {
    private final String name;
    private final String surname;
    private final String phone;

    public SimpleRecord(String name, String surname, String phone) {
        Require.nonEmpty(name, "name should not be empty");
        Require.nonEmpty(surname, "surname should not be empty");
        Require.nonEmpty(phone, "phone should not be empty");
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    @Override
    public Set<Character> literals() {
        return Collections.singleton(name.charAt(0));
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public Optional<Set<String>> getAdditionalPhones() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getNickname() {
        return Optional.empty();
    }
}
