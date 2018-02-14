package ru.csc.bdse.kv;

/**
 * @author semkagtn
 */
public class InMemoryKeyValueApiTest extends AbstractKeyValueApiTest {

    @Override
    protected KeyValueApi newKeyValueApi() {
        return new InMemoryKeyValueApi("node");
    }
}
