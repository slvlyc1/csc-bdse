package ru.csc.bdse.impl.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.csc.bdse.model.kv.KeyValueApi;
import ru.csc.bdse.proto.ClusterInfo;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Provides HTTP API for the storage unit
 *
 * @author semkagtn
 */
@RestController
public class KeyValueApiController {

    private final KeyValueApi keyValueApi;

    public KeyValueApiController(final KeyValueApi keyValueApi) {
        this.keyValueApi = keyValueApi;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/key-value/{key}")
    public void put(@PathVariable final String key,
                    @RequestBody final byte[] value) {
        keyValueApi.put(key, value);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/key-value/{key}")
    public byte[] get(@PathVariable final String key) {
        return keyValueApi.get(key)
                .orElseThrow(() -> new NoSuchElementException(key));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/key-value/{key}")
    public void delete(@PathVariable final String key) {
        keyValueApi.delete(key);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cluster-info")
    public byte[] getClusterInfo() {
        return keyValueApi.getClusterInfo().toByteArray();
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle(NoSuchElementException e) {
        return Optional.ofNullable(e.getMessage()).orElse("");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handle(IllegalArgumentException e) {
        return Optional.ofNullable(e.getMessage()).orElse("");
    }
}
