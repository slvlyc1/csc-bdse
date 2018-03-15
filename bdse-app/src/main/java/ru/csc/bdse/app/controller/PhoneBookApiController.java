package ru.csc.bdse.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.app.phonebook.PhoneBookApi;
import ru.csc.bdse.app.phonebook.RecordSerializer;
import java.io.IOException;

/**
 * Provides HTTP API for phone book
 *
 * @author pavlin256
 */
@RestController
public class PhoneBookApiController {
    private final PhoneBookApi phoneBookApi;
    private final RecordSerializer serializer;

    private static final Logger logger = LoggerFactory.getLogger(PhoneBookApiController.class);

    public PhoneBookApiController(PhoneBookApi phoneBookApi, RecordSerializer serializer) {
        this.phoneBookApi = phoneBookApi;
        this.serializer = serializer;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/phone-book")
    public void put(@RequestBody final byte[] value) {
        try {
            phoneBookApi.put(serializer.deserializeRecord(value));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/phone-book/{key}")
    public byte[] get(@PathVariable final String key) {
        if (key.length() > 1) return "Key should be literal".getBytes();
        return phoneBookApi.get(key.charAt(0)).toString().getBytes();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/phone-book/delete")
    public void delete(@RequestBody final byte[] value) {
        try {
            phoneBookApi.delete(serializer.deserializeRecord(value));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
