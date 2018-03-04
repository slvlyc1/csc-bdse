package ru.csc.bdse.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import ru.csc.bdse.util.Env;

@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "key-value";
    }

    @Override
    public Mongo mongo() {
        return new MongoClient(Env.get(Env.MONGO_HOSTNAME).orElse("localhost"),
                Integer.parseInt(Env.get(Env.MONGO_PORT).orElse("27017")));
    }
}
