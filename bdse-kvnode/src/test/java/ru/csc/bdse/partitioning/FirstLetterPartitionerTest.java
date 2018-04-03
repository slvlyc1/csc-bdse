package ru.csc.bdse.partitioning;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Tests for [[FirstLetterPartitioner]]
 *
 * @author alesavin
 */
public class FirstLetterPartitionerTest {

    @Test
    public void mapsToNodeByFirstLetter() {
        List<String> nodes = Arrays.asList("0", "1", "2");
        final Partitioner partitioner = new FirstLetterPartitioner(nodes);
        assertThat(partitioner.getPartition("\u0000")).isEqualTo("0");
        assertThat(partitioner.getPartition("\uFFFF")).isEqualTo("2");
        assertThat(partitioner.getPartition("\uFFFA")).isEqualTo("2");

        assertThat(partitioner.getPartition("Aaaaa")).isEqualTo("0");
        assertThat(partitioner.getPartition("A")).isEqualTo("0");
        assertThat(partitioner.getPartition("affff")).isEqualTo("0");
        assertThat(partitioner.getPartition("a123")).isEqualTo("0");
        assertThat(partitioner.getPartition("b")).isEqualTo("0");
        assertThat(partitioner.getPartition("c")).isEqualTo("0");
        assertThat(partitioner.getPartition("D")).isEqualTo("0");
        assertThat(partitioner.getPartition("z")).isEqualTo("0");
        assertThat(partitioner.getPartition("Zde")).isEqualTo("0");

        char c1 = (char)(Character.MAX_VALUE / nodes.size());
        assertThat(partitioner.getPartition(new String(new char[]{c1}))).isEqualTo("0");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 + 1)}))).isEqualTo("1");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 + 5)}))).isEqualTo("1");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 2 + 1)}))).isEqualTo("2");
    }

    @Test
    public void mapsToRepeatedNodeByFirstLetter() {
        List<String> nodes = Arrays.asList("0", "1", "2", "0", "1", "2");
        final Partitioner partitioner = new FirstLetterPartitioner(nodes);
        char c1 = (char)(Character.MAX_VALUE / nodes.size());
        assertThat(partitioner.getPartition(new String(new char[]{c1}))).isEqualTo("0");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 + 1)}))).isEqualTo("1");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 + 5)}))).isEqualTo("1");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 2)}))).isEqualTo("1");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 3)}))).isEqualTo("2");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 4)}))).isEqualTo("0");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 5)}))).isEqualTo("1");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 6)}))).isEqualTo("2");
        assertThat(partitioner.getPartition(new String(new char[]{(char)(c1 * 7)}))).isEqualTo("0");
    }


}