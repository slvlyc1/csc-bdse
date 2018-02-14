package ru.csc.bdse.util;

import java.util.Optional;

/**
 * @author semkagtn
 */
public class Env {

    private Env() {

    }

    public static Optional<String> get(final String name) {
        return Optional.ofNullable(System.getenv(name));
    }
}
