package ru.csc.bdse.kv;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LatestValueConflictResolverTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private ConflictResolver resolver;

    @Before
    public void setUp() throws Exception {
        resolver = new LatestValueConflictResolver();
    }

    @Test
    public void resolveLatestValue() throws Exception {
        // Given
        RecordWithTimestamp recordOne = new RecordWithTimestamp("valueOne", 123000, false, "NODE_ONE");
        RecordWithTimestamp recordTwo = new RecordWithTimestamp("valueTwo", 123010, false,"NODE_TWO");
        RecordWithTimestamp recordThree = new RecordWithTimestamp("valueThree", 123005, false, "NODE_THREE");

        HashSet<RecordWithTimestamp> conflictingRecords = new HashSet<>(Arrays.asList(recordOne, recordTwo, recordThree));

        // When
        RecordWithTimestamp resolvedRecord = resolver.resolve(conflictingRecords);

        // Then
        assertEquals(recordTwo, resolvedRecord);
    }

    @Test
    public void resolveMostCommonValue() throws Exception {
        //Given
        RecordWithTimestamp recordOne = new RecordWithTimestamp("valueOne", 123000, false, "NODE_ONE");
        RecordWithTimestamp recordTwo = new RecordWithTimestamp("commonValue", 123000, false, "NODE_TWO");
        RecordWithTimestamp recordThree = new RecordWithTimestamp("commonValue", 123000, false, "NODE_THREE");
        RecordWithTimestamp recordFour = new RecordWithTimestamp("valueFour", 123000, false, "NODE_FOUR");

        HashSet<RecordWithTimestamp> conflictingRecords = new HashSet<>(Arrays.asList(recordOne, recordTwo, recordThree, recordFour));

        // When
        RecordWithTimestamp resolvedRecord = resolver.resolve(conflictingRecords);

        // taking recrods with the latest timestamp -> 123000
        // then taking record with the most common value -> "commonValue"
        // then taking record with the least nodeId hash code:

        // Then
        assertEquals(recordThree, resolvedRecord);
    }

    @Test
    public void resolveLatestRareValue() throws Exception {
        //Given
        RecordWithTimestamp recordOne = new RecordWithTimestamp("commonValue", 123000, false, "NODE_ONE");
        RecordWithTimestamp recordTwo = new RecordWithTimestamp("commonValue", 123000, false, "NODE_TWO");
        RecordWithTimestamp recordThree = new RecordWithTimestamp("rareValue", 123010, false, "NODE_THREE");
        RecordWithTimestamp recordFour = new RecordWithTimestamp("commonValue", 123000, false, "NODE_FOUR");

        HashSet<RecordWithTimestamp> conflictingRecords = new HashSet<>(Arrays.asList(recordOne, recordTwo, recordThree, recordFour));

        // When
        RecordWithTimestamp resolvedRecord = resolver.resolve(conflictingRecords);

        // taking recrods with the latest timestamp -> 123010
        // it is rare but latest

        // Then
        assertEquals(recordThree, resolvedRecord);
    }

    @Test
    public void resolveSingleValue() throws Exception {
        //Given
        RecordWithTimestamp recordOne = new RecordWithTimestamp("valueOne", 123000, false, "NODE_ONE");

        HashSet<RecordWithTimestamp> conflictingRecords = new HashSet<>(Collections.singletonList(recordOne));

        // When
        RecordWithTimestamp resolvedRecord = resolver.resolve(conflictingRecords);

        // Then
        assertEquals(recordOne, resolvedRecord);
    }

    @Test
    public void resolveNoValue() throws Exception {
        //Given
        HashSet<RecordWithTimestamp> conflictingRecords = new HashSet<>();

        // Then
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Resolve input set should not be null or empty");

        // When
        RecordWithTimestamp resolvedRecord = resolver.resolve(conflictingRecords);
    }

    @Test
    public void resolveKeys() throws Exception {

        // Given
        Set<Set<String>> keys = new HashSet<>(Arrays.asList(
                new HashSet<>(Arrays.asList("keyOne", "keyTwo")),
                new HashSet<>(Arrays.asList("keyOne", "keyThree")),
                new HashSet<>(Arrays.asList("keyTwo", "keyFour", "keyFive"))
        ));

        HashSet<String> correctKeys = new HashSet<>(Arrays.asList("keyOne", "keyTwo", "keyThree", "keyFour", "keyFive"));

        // When
        Set<String> resolvedKeys = resolver.resolveKeys(keys);

        // Then
        assertEquals(correctKeys, resolvedKeys);
    }
}