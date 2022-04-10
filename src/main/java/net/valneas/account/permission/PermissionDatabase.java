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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import javax.annotation.Nonnull;
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
                        .append("default", permission.defaults())
                .append("players", permission.players().stream().map(UUID::toString).collect(Collectors.toList()))
                .append("ranks", permission.ranks().stream().map(RankUnit::name).collect(Collectors.toList()))
                .append("except", permission.exceptions().stream().map(DatabaseParser::parse).collect(Collectors.toList()))
        );
    }

    public void update(Permission permission){
        if (this.getCollection().find(Filters.eq("permission", permission.permission())).first() == null) {
            this.init(permission);
        }

        this.getCollection().findOneAndUpdate(Filters.eq("permission", permission.permission()),
                Updates.combine(
                        Updates.set("default", permission.defaults()),
                        Updates.set("players", permission.players().stream().map(UUID::toString).collect(Collectors.toList())),
                        Updates.set("ranks", permission.ranks().stream().map(RankUnit::name).collect(Collectors.toList()))
                ));

        if(permission.ranks().isEmpty() && permission.players().isEmpty() && permission.exceptions().isEmpty() && !permission.defaults()){
            this.getCollection().findOneAndDelete(Filters.eq("permission", permission.permission()));
        }
    }

    public Permission get(String permission) {
        var document = this.getCollection().find(Filters.eq("permission", permission)).first();
        if (document == null) {
            return null;
        }

        return new Permission(document.getString("permission"),
                document.getBoolean("default"),
                document.getList("players", String.class) == null ? Set.of() : document.getList("players", String.class).stream().map(UUID::fromString).collect(Collectors.toSet()),
                document.getList("ranks", String.class) == null ? Set.of() : document.getList("ranks", String.class).stream().map(RankUnit::valueOf).collect(Collectors.toSet()),
                document.getList("except", String.class) == null ? Set.of() : document.getList("except", String.class).stream().map(DatabaseParser::parse).collect(Collectors.toSet()));
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
                            document.getBoolean("default"),
                            document.get("players") == null ? Set.of() : document.get("players") instanceof List ? document.getList("players", String.class).stream().map(UUID::fromString).collect(Collectors.toSet()) : Set.of(),
                            document.get("ranks") == null ? Set.of() : document.get("ranks") instanceof List ? document.getList("ranks", String.class).stream().map(RankUnit::valueOf).collect(Collectors.toSet()) : Set.of(),
                            document.get("except") == null ? Set.of() : document.get("except") instanceof List ? document.getList("except", String.class).stream().map(DatabaseParser::parse).collect(Collectors.toSet()) : Set.of())
                        )
                .into(new ArrayList<>());
    }

    public List<Permission> getRankPermissions(RankUnit rank){
        return this.getPermissions().stream().filter(permission -> permission.ranks().contains(rank)).filter(permission -> !permission.exceptions().contains(rank)).toList();
    }

    public List<Permission> getUUIDPermissions(UUID uuid){
        return this.getPermissions().stream().filter(permission -> permission.players().contains(uuid)).filter(permission -> !permission.exceptions().contains(uuid)).toList();
    }

    public MongoCollection<Document> getCollection(){
        return this.getDatabase().getCollection("permissions");
    }

    public MongoDatabase getDatabase(){
        return this.mongo.getDatabase(this.accountSystem.getConfig().getString("mongodb.database"));
    }

    public record Permission(String permission, boolean defaults, @Nonnull Set<UUID> players, @Nonnull Set<RankUnit> ranks, @Nonnull Set<Object> exceptions){ }

    public static class DatabaseParser {
        public static Object parse(String value){
            if(value == null) return null;

            try {
                return RankUnit.valueOf(value);
            } catch (IllegalArgumentException e){
                try {
                    return UUID.fromString(value);
                } catch (IllegalArgumentException e1){
                    try {
                        return Bukkit.getPlayer(value);
                    } catch (NullPointerException e2){
                        return null;
                    }
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