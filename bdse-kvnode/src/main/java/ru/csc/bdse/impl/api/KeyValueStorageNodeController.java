package ru.csc.bdse.impl.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.model.kv.Action;
import ru.csc.bdse.model.kv.KeyValueStorageNode;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Provides HTTP API for [[KeyValueStorageNode]]
 *
 * @author alesavin
 */
@RestController
public class KeyValueStorageNodeController {

    private final KeyValueStorageNode node;

    public KeyValueStorageNodeController(KeyValueStorageNode node) {
        this.node = node;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/put/{key}")
    public void put(@PathVariable String key,
                    @RequestBody(required = false) byte[] value) throws Exception {
        node.put(key, value);
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

    @RequestMapping(method = RequestMethod.POST, value = "/action/{node}/{action}")
    public void action(@PathVariable String node,
                       @PathVariable String action) throws Exception {
        this.node.action(node, Action.valueOf(action));
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

