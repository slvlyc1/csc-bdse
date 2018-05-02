package ru.csc.bdse.app.phonebook.simple;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.csc.bdse.app.phonebook.Record;
import ru.csc.bdse.app.phonebook.RecordSerializer;

import java.io.IOException;

public class SimpleRecordSerializer implements RecordSerializer {

    @Override
    public byte[] serializeRecord(Record record) throws JsonProcessingException {
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(record);
    }


    @Override
    public Record deserializeRecord(byte[] json) throws IOException {
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(json, SimpleRecord.class);
    }
}
