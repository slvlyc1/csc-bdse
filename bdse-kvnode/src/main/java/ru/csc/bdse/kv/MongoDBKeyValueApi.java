package ru.csc.bdse.kv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.csc.bdse.util.Require;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MongoDBKeyValueApi implements KeyValueApi {

    @Autowired
    private MongoOperations mongoTemplate;
    private final String nodeName;

    private static final Logger log = LoggerFactory.getLogger(MongoDBKeyValueApi.class);

    @Autowired
    public MongoDBKeyValueApi(String nodeName) {
        Require.nonEmpty(nodeName, "empty name");
        this.nodeName = nodeName;
    }

    @Override
    public void put(String key, byte[] value) {
        Require.nonEmpty(key, "empty key");
        Require.nonNull(value, "null value");

        Query query = new Query();
        query.addCriteria(Criteria.where("key").is(key));

        Update update = new Update();
        update.set("value", value);

        mongoTemplate.upsert(query, update, KeyValue.class);
    }

    @Override
    public Optional<byte[]> get(String key) {
        Require.nonEmpty(key, "empty key");

        Query byKey = new Query().addCriteria(Criteria.where("key").is(key));
        KeyValue result = mongoTemplate.findOne(byKey, KeyValue.class);

        return Optional.ofNullable(result.getValue());
    }

    @Override
    public Set<String> getKeys(String prefix) {
        Require.nonNull(prefix, "null prefix");

        Query query = new Query();
        query.addCriteria(Criteria.where("key").regex(String.format("^%s", prefix)));
        List<KeyValue> result = mongoTemplate.find(query, KeyValue.class);

        return result
                .stream().map(KeyValue::getKey).collect(Collectors.toSet());
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
