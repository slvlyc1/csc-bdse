package ru.csc.bdse.kv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.csc.bdse.util.Require;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.csc.bdse.kv.NodeStatus.*;

public class MongoDBKeyValueApi implements KeyValueApi {

    @Autowired
    private MongoOperations mongoTemplate;
    private final String nodeName;
    private volatile NodeStatus nodeStatus;

    private static final Logger log = LoggerFactory.getLogger(MongoDBKeyValueApi.class);

    @Autowired
    public MongoDBKeyValueApi(String nodeName) {
        Require.nonEmpty(nodeName, "empty name");
        this.nodeName = nodeName;
        this.nodeStatus = UP;
    }

    @Override
    public void put(String key, byte[] value) {
        Require.nonEmpty(key, "empty key");
        Require.nonNull(value, "null value");
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));

        Update update = new Update();
        update.set("value", value);

        mongoTemplate.upsert(query, update, KeyValue.class);
    }

    @Override
    public Optional<byte[]> get(String key) {
        Require.nonEmpty(key, "empty key");
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        Query byKey = new Query().addCriteria(Criteria.where("key").is(key));
        KeyValue result = mongoTemplate.findOne(byKey, KeyValue.class);

        return Optional.ofNullable(result).map(KeyValue::getValue);
    }

    @Override
    public Set<String> getKeys(String prefix) {
        Require.nonNull(prefix, "null prefix");
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        Query query = new Query();
        query.addCriteria(Criteria.where("key").regex(String.format("^%s", prefix)));
        List<KeyValue> result = mongoTemplate.find(query, KeyValue.class);

        return result
                .stream().map(KeyValue::getKey).collect(Collectors.toSet());
    }

    @Override
    public void delete(String key) {
        Require.nonEmpty(key, "empty key");
        if (nodeStatus.equals(DOWN)) throw new IllegalStateException(String.format("Node '%s' is down", nodeName));

        Query query = new Query(Criteria.where("key").is(key));
        mongoTemplate.remove(query, KeyValue.class);
    }

    @Override
    public Set<NodeInfo> getInfo() {
        return Collections.singleton(new NodeInfo(nodeName, nodeStatus));
    }

    @Override
    public void action(String node, NodeAction action) {
        Require.nonEmpty(node, "empty node name");
        Require.nonNull(action, "empty action");

        if (!node.equals(nodeName)) return;

        switch (action) {
            case UP:
                nodeStatus = UP;
                break;
            case DOWN:
                nodeStatus = DOWN;
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown NodeAction '%s'", action));
        }
    }
}
