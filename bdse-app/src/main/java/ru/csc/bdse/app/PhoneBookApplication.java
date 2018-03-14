package ru.csc.bdse.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.simple.SimplePhoneBookApi;

@SpringBootApplication
public class PhoneBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhoneBookApplication.class, args);
    }

    @Bean
    PhoneBookApi phoneBook() {
        String nodeUrl = "http://localhost:8080"; //todo: get this url from envs!
        return new SimplePhoneBookApi(nodeUrl);
    }
}
