package ru.csc.bdse.app.phonebook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Helper {

    public static byte[] serializeRecord(Record record) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsBytes(record);
    }

    public static Record deserializeRecord(byte[] json) throws IOException {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(json, RecordImpl.class);
    }
}
