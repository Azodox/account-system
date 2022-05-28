package net.valneas.account;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.valneas.account.rank.Rank;
import net.valneas.account.rank.RankUnit;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AccountManager extends AbstractAccountManager{

    private final AccountSystem accountSystem;

    public AccountManager(AccountSystem accountSystem, Player player) {
        super(accountSystem.getMongo().getMongoClient(), player.getName(), player.getUniqueId().toString());
        this.accountSystem = accountSystem;
    }

    public AccountManager(AccountSystem accountSystem, String name, String uuid) {
        super(accountSystem.getMongo().getMongoClient(), name, uuid);
        this.accountSystem = accountSystem;
    }

    public void initDefaultAccount(){
        this.createAccount(RankUnit.JOUEUR.getId());
    }

    public Rank newRankManager(){
        return new Rank(this);
    }

    @Override
    protected MongoDatabase getDatabase() {
        return mongo.getDatabase(this.accountSystem.getConfig().getString("mongodb.database"));
    }

    /*
    Static methods
     */

    public static boolean existsByUUID(String uuid){
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
        if(provider != null){
            var main = provider.getProvider();
            final MongoClient mongo = main.getMongo().getMongoClient();
            final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
            final MongoCollection<Document> accounts = database.getCollection("accounts");

            for (Document document : accounts.find()) {
                if(document.getString("uuid").equals(uuid)){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean existsByName(String name){
        var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
        if(provider != null){
            var main = provider.getProvider();
            final MongoClient mongo = main.getMongo().getMongoClient();
            final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
            final MongoCollection<Document> accounts = database.getCollection("accounts");

            for (Document document : accounts.find()) {
                if(document.getString("name").equals(name)){
                    return true;
                }
            }
        }
        return false;
    }

    public static String getUuidByName(String name){
        if(!existsByName(name)) return null;

        var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
        if(provider != null){
            var main = provider.getProvider();
            final MongoClient mongo = main.getMongo().getMongoClient();
            final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
            final MongoCollection<Document> accounts = database.getCollection("accounts");

            for (Document document : accounts.find()) {
                if(document.getString("name").equals(name)){
                    return document.getString("uuid");
                }
            }
        }
        return null;
    }

    public static String getNameByUuid(String uuid){
        if(!existsByUUID(uuid)) return null;

        var provider = Bukkit.getServicesManager().getRegistration(AccountSystem.class);
        if(provider != null){
            var main = provider.getProvider();
            final MongoClient mongo = main.getMongo().getMongoClient();
            final MongoDatabase database = mongo.getDatabase(main.getConfig().getString("mongodb.database"));
            final MongoCollection<Document> accounts = database.getCollection("accounts");

            for (Document document : accounts.find()) {
                if(document.getString("uuid").equals(uuid)){
                    return document.getString("name");
                }
            }
        }
        return null;
    }
}
