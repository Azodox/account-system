package net.valneas.account.permission;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import net.valneas.account.AccountManager;
import net.valneas.account.AccountSystem;
import net.valneas.account.rank.RankUnit;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionDatabase {

    private final AccountSystem accountSystem;
    private final MongoClient mongo;

    public PermissionDatabase(AccountSystem accountSystem){
        this.accountSystem = accountSystem;
        this.mongo = accountSystem.getMongo().getMongoClient();
    }

    public void init(Permission permission){
        if (this.getCollection().find(Filters.eq("permission", permission.permission())).first() != null) {
            return;
        }

        this.getCollection().insertOne(
                new Document("permission", permission.permission())
                .append("players", permission.players() == null ? null : permission.players().stream().map(UUID::toString).collect(Collectors.toList()))
                .append("ranks", permission.ranks() == null ? null : permission.ranks().stream().map(RankUnit::name).collect(Collectors.toList()))
                .append("except", permission.exceptions() == null ? null : permission.exceptions().stream().map(DatabaseParser::parse).collect(Collectors.toList()))
        );
    }

    public void update(Permission permission){
        if (this.getCollection().find(Filters.eq("permission", permission.permission())).first() == null) {
            this.init(permission);
        }

        if((permission.ranks() == null || permission.ranks().isEmpty())
                && (permission.players() == null || permission.players().isEmpty())
                && (permission.exceptions() == null || permission.exceptions().isEmpty())){
            this.getCollection().findOneAndDelete(Filters.eq("permission", permission.permission()));
            return;
        }

        this.getCollection().findOneAndUpdate(Filters.eq("permission", permission.permission()),
                Updates.combine(
                        Updates.set("players", permission.players() == null ? null : permission.players().stream().map(UUID::toString).collect(Collectors.toList())),
                        Updates.set("ranks", permission.ranks() == null ? null : permission.ranks().stream().map(RankUnit::name).collect(Collectors.toList()))
                ));
    }

    public Permission get(String permission) {
        var document = this.getCollection().find(Filters.eq("permission", permission)).first();
        if (document == null) {
            return null;
        }

        return new Permission(document.getString("permission"),
                document.getList("players", String.class) == null ? null : document.getList("players", String.class).stream().map(UUID::fromString).collect(Collectors.toSet()),
                document.getList("ranks", String.class) == null ? null : document.getList("ranks", String.class).stream().map(RankUnit::valueOf).collect(Collectors.toSet()),
                document.getList("except", String.class) == null ? null : document.getList("except", String.class).stream().map(DatabaseParser::parse).collect(Collectors.toSet()));
    }

    public void setPlayerPermission(Player player){
        var account = new AccountManager(this.accountSystem, player);
        var rank = account.newRankManager();

        player.getEffectivePermissions()
                .stream().map(PermissionAttachmentInfo::getAttachment)
                .toList().stream().filter(Objects::nonNull).forEach(PermissionAttachment::remove);

        List<PermissionDatabase.Permission> permissions = new ArrayList<>();

        permissions.addAll(this.getRankPermissions(rank.getMajorRank()));
        rank.getRanks().forEach(rankUnit -> permissions.addAll(this.getRankPermissions(rankUnit)));
        permissions.addAll(this.getUUIDPermissions(player.getUniqueId()));

        var attachment = player.addAttachment(this.accountSystem);
        permissions.forEach(permission -> attachment.setPermission(permission.permission().replace("-", ""), !permission.permission().startsWith("-")));
    }

    public List<Permission> getPermissions(){
        return this.getCollection().find().filter(Filters.ne("permission", null))
                .map(document -> new Permission(document.getString("permission"),
                            document.get("players") == null ? null : document.get("players") instanceof List ? document.getList("players", String.class).stream().map(UUID::fromString).collect(Collectors.toSet()) : null,
                            document.get("ranks") == null ? null : document.get("ranks") instanceof List ? document.getList("ranks", String.class).stream().map(RankUnit::valueOf).collect(Collectors.toSet()) : null,
                            document.get("except") == null ? null : document.get("except") instanceof List ? document.getList("except", String.class).stream().map(DatabaseParser::parse).collect(Collectors.toSet()) : null)
                        )
                .into(new ArrayList<>());
    }

    public List<Permission> getRankPermissions(RankUnit rank){
        return this.getPermissions().stream().filter(permission -> {
            if(permission.ranks() == null) return false;
            return permission.ranks().contains(rank);
        }).filter(permission -> {
            if (permission.exceptions() != null) {
                return !permission.exceptions().contains(rank);
            }
            return true;
        }).toList();
    }

    public List<Permission> getUUIDPermissions(UUID uuid){
        return this.getPermissions().stream().filter(permission -> {
            if(permission.players() == null) return false;
            return permission.players().contains(uuid);
        }).filter(permission -> {
            if(permission.exceptions() != null) {
                return !permission.exceptions().contains(uuid);
            }
            return true;
        }).toList();
    }

    public MongoCollection<Document> getCollection(){
        return this.getDatabase().getCollection("permissions");
    }

    public MongoDatabase getDatabase(){
        return this.mongo.getDatabase(this.accountSystem.getConfig().getString("mongodb.database"));
    }

    public record Permission(String permission, Set<UUID> players, Set<RankUnit> ranks, Set<Object> exceptions){ }

    public static class DatabaseParser {
        public static <T> T parse(String value){
            if(value == null) return null;

            try {
                return (T) RankUnit.valueOf(value);
            } catch (IllegalArgumentException e){
                try {
                    return (T) UUID.fromString(value);
                } catch (IllegalArgumentException e1){
                    return null;
                }
            }
        }

        public static String parse(Object value){
            if(value == null) return null;

            if(value instanceof RankUnit){
                return ((RankUnit) value).name();
            } else if(value instanceof UUID){
                return value.toString();
            } else {
                return null;
            }
        }
    }
}