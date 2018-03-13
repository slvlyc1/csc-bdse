package ru.csc.bdse.app.phonebook;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.csc.bdse.app.phonebook.simple.SimpleRecord;

import java.io.IOException;

//todo: temp class, to be removed
public class Helper {
    public static void getSimpleRecordByJson() throws IOException {
        String json = "{\n" +
                "    \"name\": \"Alena\",\n" +
                "    \"surname\": \"Koroteeva\",\n" +
                "    \"phone\": \"79111499815\",\n" +
                "    \"phone1\": \"7911149981115\"\n" +
                "    }";
        SimpleRecord record = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(json, SimpleRecord.class);
        System.out.println(record.toString());
    }

    public static void main(String[] args) throws IOException {
        getSimpleRecordByJson();
    }
}
