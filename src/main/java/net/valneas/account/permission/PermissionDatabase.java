package net.valneas.account.permission;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.rank.RankUnit;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PermissionDatabase {

    private final AccountSystem accountSystem;
    private final MongoClient mongo;

    public PermissionDatabase(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
        this.mongo = accountSystem.getMongo().getMongoClient();
    }

    public void setPlayerPermission(Player player){
        var account = new AccountManager(this.accountSystem, player);
        var rank = account.newRankManager();
        var permissionDatabase = this.accountSystem.getPermissionDatabase();

        List<PermissionDatabase.Permission> permissions = new ArrayList<>();

        permissions.addAll(permissionDatabase.getRankPermissions(rank.getMajorRank()));
        rank.getRanks().forEach(rankUnit -> permissions.addAll(permissionDatabase.getRankPermissions(rankUnit)));
        permissions.addAll(permissionDatabase.getUUIDPermissions(player.getUniqueId()));

        var attachment = player.addAttachment(this.accountSystem);
        permissions.forEach(permission -> attachment.setPermission(permission.permission(), !permission.permission().startsWith("-")));
    }

    public List<Permission> getPermissions(){
        List<Permission> permissions = new ArrayList<>();

        for (Permission value : this.getCollection().find().map(document -> new Permission(document.getString("permission"),
                document.get("player") == null ? null : UUID.fromString(document.getString("player")),
                document.get("rank") == null ? null : RankUnit.valueOf(document.getString("rank"))))) {
            permissions.add(value);
        }

        return permissions;
    }

    public List<Permission> getRankPermissions(RankUnit rank){
        return this.getPermissions().stream().filter(permission -> permission.rank().equals(rank)).toList();
    }

    public List<Permission> getUUIDPermissions(UUID uuid){
        return this.getPermissions().stream().filter(permission -> permission.player().equals(uuid)).toList();
    }

    public MongoCollection<Document> getCollection(){
        return this.getDatabase().getCollection("permissions");
    }

    public MongoDatabase getDatabase(){
        return this.mongo.getDatabase(this.accountSystem.getConfig().getString("mongodb.database"));
    }

    public record Permission(String permission, UUID player, RankUnit rank) {

    }

}