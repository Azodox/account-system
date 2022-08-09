package net.valneas.account.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.valneas.account.PaperAccountManager;
import net.valneas.account.PaperAccountSystem;
import org.bson.Document;
import org.bukkit.entity.Player;

public class MongoUtil {

    private final PaperAccountSystem main;
    private final MongoClient mongo;

    public MongoUtil(PaperAccountSystem main) {
        this.main = main;
        this.mongo = main.getMongo().getMongoClient();
    }


    /** @deprecated */
    @Deprecated
    public PaperAccountManager getAccountManager(Player player){
        return new PaperAccountManager(main, player);
    }

    public Document getByName(String name){
        if(!PaperAccountManager.existsByName(name)){
            return null;
        }

        final MongoClient mongo = main.getMongo().getMongoClient();
        final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
        final MongoCollection<Document> accounts = database.getCollection("accounts");

        for (Document account : accounts.find()) {
            if(account.getString("name").equals(name)){
                return account;
            }
        }
        return null;
    }

    public Document getByUUID(String uuid){
        if(!PaperAccountManager.existsByUUID(uuid)){
            return null;
        }

        final MongoClient mongo = main.getMongo().getMongoClient();
        final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
        final MongoCollection<Document> accounts = database.getCollection("accounts");

        for (Document account : accounts.find()) {
            if(account.getString("uuid").equals(uuid)){
                return account;
            }
        }
        return null;
    }

    public Document getByObjectID(String objectID){
        final MongoClient mongo = main.getMongo().getMongoClient();
        final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
        final MongoCollection<Document> accounts = database.getCollection("accounts");

        for (Document account : accounts.find()) {
            if(account.getObjectId("_id").toString().equals(objectID)){
                return account;
            }
        }
        return null;
    }
}
