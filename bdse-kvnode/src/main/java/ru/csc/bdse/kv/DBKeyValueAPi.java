package ru.csc.bdse.kv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

//todo: stupid class, need refactor
public class DBKeyValueAPi implements KeyValueApi {

    private Connection connection;
    private final String nodeName;

    private static final Logger log = LoggerFactory.getLogger(DBKeyValueAPi.class);

    public DBKeyValueAPi(String nodeName) {
        this.nodeName = nodeName;
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432","postgres", "");
        } catch (SQLException e) {
            log.error(e.getSQLState());
        }
    }

    @Override
    public void put(String key, byte[] value) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(String.format("UPDATE data set key = '%s', value = '%s'", key, Arrays.toString(value)));
        } catch (SQLException e) {
            log.error(e.getSQLState());
        }
    }

    @Override
    public Optional<byte[]> get(String key) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(String.format("SELECT value from data where key = '%s';", key));
        } catch (SQLException e) {
            log.error(e.getSQLState());
        }

        return Optional.empty();
    }

    @Override
    public Set<String> getKeys(String prefix) {
        return null;
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public Set<NodeInfo> getInfo() {
        return null;
    }

    @Override
    public void action(String node, NodeAction action) {

    }
}
