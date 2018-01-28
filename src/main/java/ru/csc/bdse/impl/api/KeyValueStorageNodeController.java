package ru.csc.bdse.impl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.model.kv.KeyValueStorageNode;

/**
 * TODO
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
                       @RequestBody byte[] value) {
        node.upsert(key, value);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/get/{key}")
    public byte[] get(@PathVariable String key) {
        return node.get(key);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/keys")
    public String keys() {
        final StringBuilder sb = new StringBuilder();
        node.keysIterator().forEachRemaining(key -> sb.append(key).append(','));
        if (sb.length() == 0)
            return "";
        else
            return sb.deleteCharAt(sb.length() - 1).toString();
    }
}
