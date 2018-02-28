package ru.csc.bdse.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "key-value";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("localhost");
    }
}
