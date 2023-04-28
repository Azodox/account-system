package net.valneas.account.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterConnectionMode;
import org.bson.UuidRepresentation;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Azodox_ (Luke)
 * 14/5/2022.
 */

public class Mongo {

    private final MongoClient mongoClient;

    public Mongo(String username, String authDatabase, String password, String host, int port) {
        MongoCredential credential = MongoCredential.createCredential(
                username,
                authDatabase,
                password.toCharArray()
        );

        MongoClientSettings options = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> {
                    builder.hosts(List.of(new ServerAddress(host, port)));
                    builder.mode(ClusterConnectionMode.MULTIPLE);
                    builder.serverSelectionTimeout(10, TimeUnit.SECONDS);
                })
                .applyToConnectionPoolSettings(builder ->{
                    builder.maxWaitTime(30, TimeUnit.SECONDS);
                    builder.maxConnectionLifeTime(2, TimeUnit.HOURS);
                    builder.maxConnectionIdleTime(30, TimeUnit.MINUTES);
                })
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(10, TimeUnit.SECONDS);
                    builder.readTimeout(30, TimeUnit.SECONDS);
                })
                .writeConcern(WriteConcern.ACKNOWLEDGED)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .credential(credential)
        .build();

        this.mongoClient = MongoClients.create(options);
    }

    public Mongo(String host, int port) {
        MongoClientSettings options = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> {
                    builder.hosts(List.of(new ServerAddress(host, port)));
                    builder.mode(ClusterConnectionMode.MULTIPLE);
                    builder.serverSelectionTimeout(10, TimeUnit.SECONDS);
                })
                .applyToConnectionPoolSettings(builder ->{
                    builder.maxWaitTime(30, TimeUnit.SECONDS);
                    builder.maxConnectionLifeTime(2, TimeUnit.HOURS);
                    builder.maxConnectionIdleTime(30, TimeUnit.MINUTES);
                })
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(10, TimeUnit.SECONDS);
                    builder.readTimeout(30, TimeUnit.SECONDS);
                })
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .writeConcern(WriteConcern.ACKNOWLEDGED)
                .build();

        this.mongoClient = MongoClients.create(options);
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
