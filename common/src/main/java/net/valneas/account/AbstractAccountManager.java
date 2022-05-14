package net.valneas.account;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

public abstract class AbstractAccountManager {

    protected final MongoClient mongo;
    private final String name, uuid;

    public AbstractAccountManager( MongoClient mongo, String name, String uuid) {
        this.mongo = mongo;
        this.name = name;
        this.uuid = uuid;
    }

    public void createAccount(int defaultRank){
        if(hasAnAccount()) return;
        final MongoCollection<Document> accounts = getAccountCollection();

        Document account = new Document("name", name)
                .append("uuid", uuid)
                .append("xp", 0.0d)
                .append("level", 0.0d)
                .append("money", 0.0d)
                .append("points", 0.0d)
                .append("farming-points", 0.0d)
                .append("farming-prestige", 0.0d)
                .append("first-connection", null)
                .append("last-connection", null)
                .append("last-disconnection", null)
                .append("last-ip", null)
                .append("major-rank", defaultRank)
                .append("ranks", new ArrayList<Integer>())
                .append("moderation-mod", null)
        ;
        accounts.insertOne(account);
    }

    public boolean hasAnAccount(){
        final MongoCollection<Document> accounts = getAccountCollection();

        for (Document document : accounts.find()) {
            if(document.getString("uuid").equals(uuid)){
                return true;
            }

        }
        return false;
    }

    public void update(Document newDocument){
        if(!hasAnAccount()) return;
        Document account = getAccount();
        getAccountCollection().findOneAndReplace(account, newDocument);
    }

    public void updateOnLogin(){
        if(!hasAnAccount()) return;
        Document account = getAccount();

        if(!account.getString("name").equals(name)){
            account.replace("name", name);
        }

        update(account);
    }

    public Object get(String key){
        if(!hasAnAccount()) return null;
        return getAccount().get(key);
    }

    public void set(String key, Object value){
        if(!hasAnAccount()) return;
        Document account = getAccount();

        account.replace(key, value);
        update(account);
    }

    public Document getAccount(){
        final MongoCollection<Document> accounts = getAccountCollection();

        for (Document document : accounts.find()) {
            if(document.getString("uuid").equals(uuid)){
                return document;
            }

        }
        return null;
    }

    private MongoCollection<Document> getAccountCollection(){
        return getDatabase().getCollection("accounts");
    }

    abstract protected MongoDatabase getDatabase();

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}
