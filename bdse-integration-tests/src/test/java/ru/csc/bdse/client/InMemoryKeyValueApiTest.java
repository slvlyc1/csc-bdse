package ru.csc.bdse.client;

import ru.csc.bdse.impl.kv.InMemoryKeyValueApi;
import ru.csc.bdse.model.kv.KeyValueApi;

/**
 * @author semkagtn
 */
public class InMemoryKeyValueApiTest extends AbstractKeyValueApiTest {

    @Override
    protected KeyValueApi newKeyValueApi() {
        return new InMemoryKeyValueApi("node");
    }
}
