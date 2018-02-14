package ru.csc.bdse.client;

import ru.csc.bdse.kv.InMemoryKeyValueApi;
import ru.csc.bdse.kv.KeyValueApi;

/**
 * @author semkagtn
 */
public class InMemoryKeyValueApiTest extends AbstractKeyValueApiTest {

    @Override
    protected KeyValueApi newKeyValueApi() {
        return new InMemoryKeyValueApi("node");
    }
}
