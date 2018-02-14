package ru.csc.bdse.util;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.csc.bdse.proto.ClusterInfo;
import ru.csc.bdse.proto.StringList;

import java.util.HashSet;
import java.util.Set;

/**
 * @author semkagtn
 */
public class Serializing {

    private Serializing() {

    }

    public static ClusterInfo deserializeClusterInfo(final byte[] bytes) {
        try {
            return ClusterInfo.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] serializeStringSet(Set<String> set) {
        return StringList.newBuilder()
                .addAllValues(set)
                .build()
                .toByteArray();
    }

    public static Set<String> deserializeStringSet(final byte[] bytes) {
        try {
            return new HashSet<>(StringList.parseFrom(bytes).getValuesList());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }
}
