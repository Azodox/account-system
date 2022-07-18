package net.valneas.account.permission;

import com.mongodb.client.result.UpdateResult;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.updates.UpdateOperators;
import net.valneas.account.PaperAccountManager;
import net.valneas.account.PaperAccountSystem;
import net.valneas.account.rank.PaperRankUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PaperPermissionDispatcher implements PermissionDispatcher {

    private final PaperAccountSystem accountSystem;

    public PaperPermissionDispatcher(PaperAccountSystem accountSystem){
        this.accountSystem = accountSystem;
    }

    public void onEnable(){
        this.accountSystem.getPermissionDatabase().getPermissions().stream().filter(AbstractPermission::isDefault).forEach(permission -> {
            if (Bukkit.getPluginManager().getPermission(permission.getPermission().replace("-", "")) == null) {
                Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission(permission.getPermission().replace("-", ""), "", permission.getPermission().startsWith("-") ? PermissionDefault.FALSE : PermissionDefault.TRUE));
            }
        });
        Bukkit.getOnlinePlayers().forEach(this::reloadPermissions);
    }

    public void reloadPermissions() {
        this.onEnable();
    }

    public void reloadPermissions(Object target){
        if(target instanceof Player player) {
            this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
        }else if(target instanceof UUID uuid){
            this.accountSystem.getPermissionDatabase().setPlayerPermission(Bukkit.getPlayer(uuid));
        }else if(target instanceof PaperRankUnit rankUnit){
            Bukkit.getOnlinePlayers().forEach(player -> {
                var account = new PaperAccountManager(this.accountSystem, player);
                var rank = account.newRankManager();

                if(rank.hasExactRank(rankUnit.getId())){
                    this.accountSystem.getPermissionDatabase().setPlayerPermission(player);
                }
            });
        }
    }

    public void setDefault(String permission){
        this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.set("isDefault", true)).execute();
        reloadPermissions();
    }

    public void unsetDefault(String permission){
        this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.set("isDefault", false)).execute();
        reloadPermissions();
    }

    @Override
    public <T> void set(Object target, T permission, String field, Object value){
        if(permission instanceof PaperPermission perm){
            set(target, perm.getPermission(), field, value);
        }else{
            throw new IllegalArgumentException("permission must be of type PaperPermission");
        }
    }

    @Override
    public void set(Object target, String permission, String field, Object value){
        this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.set(field, value)).execute();
        reloadPermissions(target);
    }

    @Override
    public <T> void set(T player) {
        if(player instanceof Player p){
            this.accountSystem.getPermissionDatabase().setPlayerPermission(p);
        }else {
            throw new IllegalArgumentException("player must be of type Player");
        }
    }

    @Override
    public UpdateResult addException(Object exception, String permission){
        UpdateResult result = null;
        if(exception instanceof Player player) {
            result = this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.addToSet("exceptions", player.getUniqueId())).execute();
        } else if(exception instanceof UUID uuid){
            result = this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.addToSet("exceptions", uuid)).execute();
        } else if(exception instanceof PaperRankUnit rankUnit){
            result = this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.addToSet("exceptions", rankUnit.getId())).execute();
        }
        return result;
    }

    @Override
    public UpdateResult removeException(Object exception, String permission){
        UpdateResult result = null;
        if(exception instanceof Player player) {
            result = this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.pullAll("exceptions", List.of(player.getUniqueId()))).execute();
        } else if(exception instanceof UUID uuid){
            result = this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.pullAll("exceptions", List.of(uuid))).execute();
        } else if(exception instanceof PaperRankUnit rankUnit){
            result = this.accountSystem.getPermissionDatabase().getAsQuery(permission).update(UpdateOperators.pullAll("exceptions", List.of(rankUnit.getId()))).execute();
        }
        return result;
    }

    @Override
    public UpdateResult addPermissionToObject(Object target, String permission){
        if(this.accountSystem.getPermissionDatabase().getAsQuery(permission).count() == 0){
            this.accountSystem.getPermissionDatabase().init(new PaperPermission(permission, false, Set.of(), Set.of(), Set.of()));
        }
        return this.addAccordingToObject(target, this.accountSystem.getPermissionDatabase().getAsQuery(permission));
    }

    @Override
    public UpdateResult removePermissionFromObject(Object target, String permission){
        return this.removeAccordingToObject(target, this.accountSystem.getPermissionDatabase().getAsQuery(permission));
    }

    @Override
    public <T> UpdateResult addAccordingToObject(Object target, Query<T> query) {
        UpdateResult result = null;
        if(target instanceof Player player) {
            result = query.update(UpdateOperators.addToSet("players", player.getUniqueId().toString())).execute();
        }else if(target instanceof UUID uuid){
            result = query.update(UpdateOperators.addToSet("players", uuid.toString())).execute();
        }else if(target instanceof PaperRankUnit rankUnit){
            result = query.update(UpdateOperators.addToSet("ranks", rankUnit.getId())).execute();
        }
        reloadPermissions(target);
        return result;
    }

    @Override
    public <T> UpdateResult removeAccordingToObject(Object target, Query<T> query) {
        UpdateResult result = null;
        if(target instanceof Player player) {
            result = query.update(UpdateOperators.pullAll("players", List.of(player.getUniqueId().toString()))).execute();
        }else if(target instanceof UUID uuid){
            result = query.update(UpdateOperators.pullAll("players", List.of(uuid.toString()))).execute();
        }else if(target instanceof PaperRankUnit rankUnit){
            result = query.update(UpdateOperators.pullAll("ranks", List.of(rankUnit.getId()))).execute();
        }
        reloadPermissions(target);
        return result;
    }
}