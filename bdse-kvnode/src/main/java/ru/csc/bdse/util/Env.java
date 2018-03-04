package ru.csc.bdse.util;

import java.util.Optional;

/**
 * @author semkagtn
 */
public class Env {

    private Env() {

    }

    public static final String KVNODE_NAME = "KVNODE_NAME";
    public static final String MONGO_HOSTNAME = "MONGO_HOSTNAME";
    public static final String MONGO_PORT = "MONGO_PORT";

    public static Optional<String> get(final String name) {
        return Optional.ofNullable(System.getenv(name));
    }
}
