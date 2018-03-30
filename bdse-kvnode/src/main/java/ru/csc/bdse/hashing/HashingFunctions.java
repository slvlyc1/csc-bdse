package ru.csc.bdse.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Function;

/**
 * Provide some hashing for strings
 *
 * @author alesavin
 */
public final class HashingFunctions {

    public final static Function<String, Integer> hashCodeFunction =
            String::hashCode;

    public final static Function<String, Integer> md5Function = (String s) -> {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte[] bytes = md.digest();
            return (bytes[0] & 0xFF)
                    | ((bytes[1] & 0xFF) << 8)
                    | ((bytes[2] & 0xFF) << 16)
                    | ((bytes[3] & 0xFF) << 24);
        } catch (NoSuchAlgorithmException e) {
            return 0;
        }
    };
}