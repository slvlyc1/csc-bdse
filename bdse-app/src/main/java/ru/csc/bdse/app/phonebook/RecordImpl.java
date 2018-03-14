package ru.csc.bdse.app.phonebook;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.csc.bdse.util.Require;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class RecordImpl implements Record {
    private final String name;
    private final String surname;
    private final String phone;
    //private final String nickname;
    //private final String addi

    @JsonCreator
    public RecordImpl(@JsonProperty("name") String name, @JsonProperty("surname") String surname, @JsonProperty("phone") String phone) {
        Require.nonEmpty(name, "name should not be empty");
        Require.nonEmpty(surname, "surname should not be empty");
        Require.nonEmpty(phone, "phone should not be empty");
        this.name = name;
        this.surname = surname;
        this.phone = phone;
      //  this.nickname = nickname;
    }

    @Override
    public Set<Character> literals() {
        return Collections.singleton(surname.charAt(0));
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("surname")
    public String getSurname() {
        return surname;
    }

    @JsonGetter("phone")
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

    @Override
    public String toString() {
        return "SimpleRecord{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordImpl record = (RecordImpl) o;
        return Objects.equals(name, record.name) &&
                Objects.equals(surname, record.surname) &&
                Objects.equals(phone, record.phone);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, surname, phone);
    }
}
