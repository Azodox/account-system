package net.valneas.account.mongo;

import com.mongodb.*;

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

        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(10)
                .connectTimeout(100000)
                .maxWaitTime(100000)
                .socketTimeout(1000)
                .heartbeatConnectTimeout(600000)
                .writeConcern(WriteConcern.ACKNOWLEDGED)
        .build();

        this.mongoClient = new MongoClient(
                new ServerAddress(
                        host,
                        port),
                credential, options
        );
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
