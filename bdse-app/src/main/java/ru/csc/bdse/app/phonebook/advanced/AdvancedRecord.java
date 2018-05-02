package ru.csc.bdse.app.phonebook.advanced;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.util.Require;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class AdvancedRecord implements Record {
    private final String name;
    private final String surname;
    private final String phone;
    private final String nickname;
    private final String additionalPhone; //todo: should be Set<>

    @JsonCreator
    public AdvancedRecord(@JsonProperty("name") String name, @JsonProperty("surname") String surname, @JsonProperty("phone") String phone,
                          @JsonProperty(value = "nickname") String nickname, @JsonProperty(value = "additionalPhone") String additionalPhone) {
        Require.nonEmpty(name, "name should not be empty");
        Require.nonEmpty(surname, "surname should not be empty");
        Require.nonEmpty(phone, "phone should not be empty");
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.nickname = nickname;
        this.additionalPhone = additionalPhone;
    }

    @Override
    public Set<Character> literals() {
        Set<Character> res = new HashSet<>();
        res.add(surname.charAt(0));
        res.add(nickname.charAt(0));
        return res;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public String getAdditionalPhone() {
        return additionalPhone;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String toString() {
        return "AdvancedRecord{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", nickname='" + nickname + '\'' +
                ", additionalPhone='" + additionalPhone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdvancedRecord that = (AdvancedRecord) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(nickname, that.nickname) &&
                Objects.equals(additionalPhone, that.additionalPhone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, phone, nickname, additionalPhone);
    }
}
