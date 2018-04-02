package ru.csc.bdse.kv;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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
        RecordWithTimestamp recordOne = new RecordWithTimestamp("valueOne".getBytes(), 123000, "NODE_ONE");
        RecordWithTimestamp recordTwo = new RecordWithTimestamp("valueTwo".getBytes(), 123010, "NODE_TWO");
        RecordWithTimestamp recordThree = new RecordWithTimestamp("valueThree".getBytes(), 123005, "NODE_THREE");

        HashSet<RecordWithTimestamp> conflictingRecords = new HashSet<>(Arrays.asList(recordOne, recordTwo, recordThree));

        // When
        RecordWithTimestamp resolvedRecord = resolver.resolve(conflictingRecords);

        // Then
        assertEquals(recordTwo, resolvedRecord);
    }

    @Test
    public void resolveMostCommonValue() throws Exception {
        //Given
        RecordWithTimestamp recordOne = new RecordWithTimestamp("valueOne".getBytes(), 123000, "NODE_ONE");
        RecordWithTimestamp recordTwo = new RecordWithTimestamp("commonValue".getBytes(), 123000, "NODE_TWO");
        RecordWithTimestamp recordThree = new RecordWithTimestamp("commonValue".getBytes(), 123000, "NODE_THREE");
        RecordWithTimestamp recordFour = new RecordWithTimestamp("valueFour".getBytes(), 123000, "NODE_FOUR");

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
    public void resolveSingleValue() throws Exception {
        //Given
        RecordWithTimestamp recordOne = new RecordWithTimestamp("valueOne".getBytes(), 123000, "NODE_ONE");

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
}