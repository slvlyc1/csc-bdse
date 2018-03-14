package ru.csc.bdse.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.app.phonebook.Helper;
import ru.csc.bdse.app.phonebook.PhoneBookApi;
import java.io.IOException;

/**
 * Provides HTTP API for phone book
 *
 * @author pavlin256
 */
@RestController
public class PhoneBookApiController {
    private final PhoneBookApi phoneBookApi;
    private static final Logger logger = LoggerFactory.getLogger(PhoneBookApiController.class);

    public PhoneBookApiController(PhoneBookApi phoneBookApi) {
        this.phoneBookApi = phoneBookApi;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/phone-book")
    public void put(@RequestBody final byte[] value) {
        try {
            phoneBookApi.put(Helper.deserializeRecord(value));
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
            phoneBookApi.delete(Helper.deserializeRecord(value));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
