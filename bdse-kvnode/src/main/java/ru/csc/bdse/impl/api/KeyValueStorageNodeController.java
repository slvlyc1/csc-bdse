package ru.csc.bdse.impl.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.model.kv.KeyValue;
import ru.csc.bdse.model.kv.KeyValueStorageNode;

import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

/**
 * Provide HTTP API for [[KeyValueStorageNode]]
 *
 * @author alesavin
 */
@RestController
public class KeyValueStorageNodeController {

    private final KeyValueStorageNode node;

    public KeyValueStorageNodeController(KeyValueStorageNode node) {
        this.node = node;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/upsert/{key}")
    public void upsert(@PathVariable String key,
                       @RequestBody byte[] value) throws Exception {
        node.upsert(key, value);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{key}")
    public byte[] get(@PathVariable String key) throws Exception {
        byte[] value = node.get(key);
        if (value == null)
            throw new NoSuchElementException(key);
        return value;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/keys")
    public String keys(@RequestParam(value = "prefix", required = false) Optional<String> prefix) throws Exception {
        final StringBuilder sb = new StringBuilder();
        if (prefix.isPresent()) {
            node.keys(k -> k.startsWith(prefix.get())).forEach(key -> sb.append(key).append('\n'));
        } else {
            node.keys(k -> true).forEach(key -> sb.append(key).append('\n'));
        }
        if (sb.length() == 0)
            return "";
        else
            return sb.deleteCharAt(sb.length() - 1).toString();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/status")
    public String status() throws Exception {
        return node.status().toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/command/{node}/{command}")
    public void down(@PathVariable String node,
                     @PathVariable String command) throws Exception {
        this.node.command(node, command);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(NoSuchElementException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(IllegalArgumentException e) {
        return e.getMessage();
    }

}

