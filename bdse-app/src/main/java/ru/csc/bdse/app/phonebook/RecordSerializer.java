package ru.csc.bdse.app.phonebook;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface RecordSerializer {
    byte[] serializeRecord(Record record) throws JsonProcessingException;

    Record deserializeRecord(byte[] json) throws IOException;
}
