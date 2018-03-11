package ru.csc.bdse.app.controller;

import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.app.phonebook.PhoneBookApi;

/**
 * Provides HTTP API for phone book
 *
 * @author pavlin256
 */
@RestController
public class PhoneBookApiController {
    private final PhoneBookApi phoneBookApi;

    public PhoneBookApiController(PhoneBookApi phoneBookApi) {
        this.phoneBookApi = phoneBookApi;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/phone-book")
    public void put(@RequestBody final byte[] value) {
        throw new RuntimeException("not implemented");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/phone-book/{key}")
    public byte[] get(@PathVariable final String key) {
        throw new RuntimeException("not implemented");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/phone-book")
    public void delete(@RequestBody final byte[] value) {
        throw new RuntimeException("not implemented");
    }

}
