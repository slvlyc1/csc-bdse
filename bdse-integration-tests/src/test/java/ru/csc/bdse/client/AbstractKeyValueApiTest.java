package ru.csc.bdse.client;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;
import ru.csc.bdse.model.kv.KeyValueApi;
import ru.csc.bdse.util.Constants;
import ru.csc.bdse.util.Random;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author semkagtn
 */
public abstract class AbstractKeyValueApiTest {

    protected abstract KeyValueApi newKeyValueApi();

    private KeyValueApi api = newKeyValueApi();

    @Test
    public void createValue() {
        SoftAssertions softAssert = new SoftAssertions();

        String key = Random.nextKey();
        byte[] value = Random.nextValue();

        Optional<byte[]> oldValue = api.get(key);
        softAssert.assertThat(oldValue.isPresent()).as("old value").isFalse();

        api.put(key, value);
        byte[] newValue = api.get(key).orElse(Constants.EMPTY_BYTE_ARRAY);
        assertThat(newValue).as("new value").isEqualTo(value);

        softAssert.assertAll();
    }

    @Test
    public void updateValue() {
        SoftAssertions softAssert = new SoftAssertions();

        String key = Random.nextKey();
        byte[] oldValue = Random.nextValue();
        byte[] newValue = Random.nextValue();

        api.put(key, oldValue);
        byte[] actualOldValue = api.get(key).orElse(Constants.EMPTY_BYTE_ARRAY);
        softAssert.assertThat(actualOldValue).as("old value").isEqualTo(oldValue);

        api.put(key, newValue);
        byte[] actualNewValue = api.get(key).orElse(Constants.EMPTY_BYTE_ARRAY);
        softAssert.assertThat(actualNewValue).as("new value").isEqualTo(newValue);

        softAssert.assertAll();
    }

    @Test
    public void deleteValue() {
        SoftAssertions softAssert = new SoftAssertions();

        String key = Random.nextKey();
        byte[] value = Random.nextValue();

        api.put(key, value);
        byte[] actualOldValue = api.get(key).orElse(Constants.EMPTY_BYTE_ARRAY);
        softAssert.assertThat(actualOldValue).as("old value").isEqualTo(value);

        api.delete(key);
        Optional<byte[]> actualNewValue = api.get(key);
        softAssert.assertThat(actualNewValue.isPresent()).as("new value").isFalse();

        softAssert.assertAll();
    }

    @Test
    public void deleteNonexistentValue() {
        SoftAssertions softAssert = new SoftAssertions();

        String nonexistentKey = Random.nextKey();
        Optional<byte[]> actualOldValue = api.get(nonexistentKey);
        softAssert.assertThat(actualOldValue.isPresent()).as("old value").isFalse();

        api.delete(nonexistentKey);
        Optional<byte[]> actualNewValue = api.get(nonexistentKey);
        softAssert.assertThat(actualNewValue.isPresent()).as("new value").isFalse();

        softAssert.assertAll();
    }
}
